<?php
/**
 * 报名&广告投放&加盟
 * Created by PhpStorm.
 * User: Freeman
 * Date: 2016/9/20*/

namespace Company\Controller;

use Think\Model;
use OSS\OssClient;
use OSS\Core\OssException;

class LeagueController extends BaseController {
    public function league() {
        $this->trackLog("execute", "league()");

        $linkMan = I('linkman','', 'trim');
        $phone = I('phone', '', 'trim');
        $provinceId=I('provinceId',0,'intval');
        $cityId=I('cityId',0,'intval');;

        $Model = new Model();

        if(empty($phone)|| empty($linkMan)) {
            $data['code']  = 0;
            $data['msg']  = "缺少必要参数";
            $this->ajaxReturn($data);
        }

        $extSql = "INSERT INTO league(linkman,phone,provinceId,cityId,applyType) VALUES('$linkMan','$phone','$provinceId','$cityId',0)";
        $this->trackLog("extSql",$extSql);
        $result = $Model->execute($extSql);
        $this->trackLog("result", $result);

        if($result) {
            $data['code']  = 1;
            $data['msg']  = "提交成功啦！";
            $this->ajaxReturn($data);
        }else{
            $data['code']  = 0;
            $data['msg']  = "提交失败啦！";
            $this->ajaxReturn($data);
        }
    }

   public function publicity() {
       $this->trackLog("execute", "publicity()");

       $linkMan = I('linkman','', 'trim');
       $phone = I('phone', '', 'trim');

       $Model = new Model();

       if(empty($phone)|| empty($linkMan)) {
           $data['code']  = 0;
           $data['msg']  = "缺少必要参数";
           $this->ajaxReturn($data);
       }

       $extSql = "INSERT INTO league(linkman,phone,applyType) VALUES('$linkMan','$phone',1)";
       $this->trackLog("extSql",$extSql);
       $result = $Model->execute($extSql);
       $this->trackLog("result", $result);

       if($result) {
           $data['code']  = 1;
           $data['msg']  = "提交成功啦！";
           $this->ajaxReturn($data);
       }else{
           $data['code']  = 0;
           $data['msg']  = "提交失败啦！";
           $this->ajaxReturn($data);
       }

   }

   public function apply() {
       try{
           $linkMan = I('linkman','', 'trim');
           $phone = I('phone', '', 'trim');
           

           $this->trackLog("execute", "publicity()");

           $Model = new Model();

           if(empty($phone)|| empty($linkMan)) {
               $data['code']  = 0;
               $data['msg']  = "缺少必要参数";
               $this->ajaxReturn($data);
           }

           $upload = new \Think\Upload();// 实例化上传类
           $upload->maxSize  = 7097152;
           $upload->exts     = array ('jpg', 'gif', 'png', 'jpeg');
           $upload->rootPath = './Uploads/';
           $upload->savePath = 'CompanyLicense/'; // 设置附件上传（子）目录

           // 上传文件
           $info = $upload->upload();
           if (!$info) {// 上传错误提示错误信息
               $data['code']=2;
               $data['msg']=$upload->getError();
               $this->ajaxReturn($data);
           } else {// 上传成功

               $this->trackLog("LicenceIMGUpdateinfo", $info);
               // 本地图片
               $localLicence = '/Uploads/' . $info['file']['savepath'] . $info['file']['savename'];
               $this->trackLog("localLicence", $localLicence);

               // 同步到OSS
               Vendor('OSS.autoload');
               $ossClient = new OssClient(C('OSS_ACCESS_KEY_ID'), C('OSS_ACCESS_KEY_SECRET'), C('OSS_ENDPOINT'));
               $headImgKey = $info['file']['savename'];
               $this->trackLog("headImgKey", $headImgKey);
               $content = file_get_contents("./".$localLicence);
               $ossClient->putObject(C('OSS_BUCKET'), $headImgKey, $content);

               $license = C('OSS_URL') . $headImgKey;
               $this->trackLog("license", $license);

                   $extSql = "INSERT INTO league(linkman,phone,license,applyType)VALUES ('$linkMan','$phone','$license',2)";
                   $this->trackLog("extSql",$extSql);
                   $result = $Model->execute($extSql);
                   $this->trackLog("result", $result);

                   if($result) {
                       $data['code']  = 1;
                       $data['msg']  = "提交成功啦！";
                       $this->ajaxReturn($data);
                   }else{
                       $data['code']  = 0;
                       $data['msg']  = "提交失败啦！";
                       $this->ajaxReturn($data);
                   }

           }
       }catch (OssException $e) {
           printf(__FUNCTION__ . ": FAILED\n");
           printf($e->getMessage() . "\n");
           return;
       }

   }
}
