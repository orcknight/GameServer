<?php

namespace Home\Controller;
use Think\Controller;
use Think\Log;
use Think\Model;
use Think\Verify;
use Think\Image;
use OSS\OssClient;
use OSS\Core\OssException;
use Home\Service\UtilsService;


class ToolsController extends BaseController {


    // 用户行为记录
    public function biLog() {

        $biData = I('biData', "", "strip_tags");
        $this->trackLog("biData", $biData);

        if($biData == "") {
            $data['code']  = 0;
            $data['msg']  = "参数格式不正确！";
            $this->ajaxReturn($data);
        } else {
            $this->trackLog("biData", $biData);

            $fieldSql = "";
            $valueSql = "";
            $user = session('x_user');
            $this->trackLog("user", $user);
            if(!empty($user)){
                $fieldSql = ", userId";
                $valueSql = ", ".$user["id"];
            };

            $wt_user = session('wt_user');
            $this->trackLog("wt_user", $wt_user);
            if(!empty($wt_user)){
                $fieldSql = ", openid";
                $valueSql = ", '".$wt_user["openid"]."'";
            };

            foreach ($biData as $key => $value) {
                $fieldSql = $fieldSql . ", $key";
                $valueSql = $valueSql . ", '$value'";
            }

            $fieldSql = substr($fieldSql, (strpos($fieldSql, ",")+1));
            $valueSql = substr($valueSql, (strpos($valueSql, ",")+1));

            $biSql = "INSERT INTO bi_log ($fieldSql) VALUES ($valueSql)";
            $this->trackLog("biSql", $biSql);

            $Model = new Model();
            $Model->execute($biSql);
        }
        exit;
    }


    public function testOSS() {

        try {

            Vendor('OSS.autoload');
            $ossClient = new OssClient(C('OSS_ACCESS_KEY_ID'), C('OSS_ACCESS_KEY_SECRET'), C('OSS_ENDPOINT'));

            $object = "oss.png";
            $filePath = $_SERVER['DOCUMENT_ROOT'].__ROOT__.'/Uploads/oss.png';
            $content = file_get_contents($filePath);
            $ossClient->putObject(C('OSS_BUCKET'), $object, $content);
        } catch(OssException $e) {
            printf(__FUNCTION__ . ": FAILED\n");
            printf($e->getMessage() . "\n");
            return;
        }
    }

    // 发送6位手机验证码
    public function sendSMSCaptcha($phone) {

        $this->trackLog("execute", "sendSMSCaptcha()");

        $enum    = "0123456789";
        $captcha = "";

        for ($i = 0; $i < 6; $i++) {
            $num[$i] = rand(0, 9);
            $captcha .= $enum[$num[$i]];
        }

        session("captchaPhone", $phone);
        session("captcha", $captcha);

        $this->trackLog("captchaPhone", session("captchaPhone"));
        $this->trackLog("captcha", session("captcha"));

        sendCaptcha($phone, $captcha, '');

    }

    public function getCaptcha(){

        $this->trackLog("execute", "getCaptcha()");

        //判断用户名及手机是否存在
        $phone = I('phone', "trim");
        $verifyCode = I('verifyCode', "luobojianzhi", "trim");
        $type = I('type', "0", "trim"); // type:0 注册获取验证码 / type:1 找回密码验证码 / type:2 修改绑定支付宝 / type:3 修改支付密码

        $this->trackLog("phone", $phone);
        $this->trackLog("verifyCode", $verifyCode);
        $this->trackLog("type", $type);

        if(!is_numeric($phone)) {
            $data['code']  = 0;
            $data['msg']  = "数据格式不正确！";
            $this->ajaxReturn($data);
        }

        $extSql = "SELECT * FROM user WHERE phone='$phone'";
        $this->trackLog("extSql", $extSql);

        $Model = new Model();
        $result = $Model->query($extSql);
        $this->trackLog("result", $result);

        if($type == 0 && $result) {
            $data['code']  = 0;
            $data['msg']  = "非常遗憾，手机号已经被注册啦！";
            $this->ajaxReturn($data);
        }

        if($type == 1 && !$result) {
            $data['code']  = 0;
            $data['msg']  = "非常遗憾，手机号在系统不存在！";
            $this->ajaxReturn($data);
        }
        
        if($type == 2 && !$result) {
            $data['code']  = 0;
            $data['msg']  = "非常遗憾，手机号在系统不存在！";
            $this->ajaxReturn($data);
        }

        if ($type == 3 && !$result) {
            $data['code']  = 0;
            $data['msg']  = "非常遗憾，手机号在系统不存在！";
            $this->ajaxReturn($data);
        }

        $verifyService = new UtilsService();
        $this->ajaxReturn($verifyService->getCaptcha($phone, $verifyCode, $type));
        
    }


    // 限制获取手机验证码的频率
    public function limitIP(){

        return true;

        $this->trackLog("execute", "checkIP()");

        $ip = get_client_ip();
        $this->trackLog("ip", "$ip");


        if(!isset($_COOKIE['ip'])) {

            setcookie('ip', $ip, time()+60);

            $this->trackLog("unLimit", "$ip");

            return true;

        } else {

            $this->trackLog("cookie_ip", $_COOKIE['ip']);
            $this->trackLog("limit", "$ip");
            return false;
        }

    }
    
    public function uploadImage($nosync){
        
        $this->trackLog("execute", "uploadUserPhoto()");

        $upload           = new \Think\Upload();// 实例化上传类
        $upload->maxSize  = 6291456;
        $upload->exts     = array ('jpg', 'gif', 'png', 'jpeg');
        $upload->rootPath = './Uploads/';
        $upload->savePath = 'CommonImg/Raw/'; // 设置附件上传（子）目录

        // 上传文件
        $info = $upload->upload();
        if (!$info) {// 上传错误提示错误信息
             $data['code']=0;
             $data['msg']=$upload->getError();
             $this->ajaxReturn($data);
        } else {// 上传成功

            $this->trackLog("uploadImage Info", $info);
            // 本地用户头像
            $localHeadImgUrl = '/Uploads/' . $info['file']['savepath'] . $info['file']['savename'];
            $this->trackLog("localHeadImgUrl", $localHeadImgUrl);

            if (!$nosync) {
                $this->trackLog('--sync to OSS start');
                // 同步到OSS
                Vendor('OSS.autoload');
                $ossClient = new OssClient(C('OSS_ACCESS_KEY_ID'), C('OSS_ACCESS_KEY_SECRET'), C('OSS_ENDPOINT'));
                $headImgKey = $info['file']['savename'];
                $content = file_get_contents("./".$localHeadImgUrl);
                $ossClient->putObject(C('OSS_BUCKET'), $headImgKey, $content);

                $headImgUrl = C('OSS_URL') . $headImgKey;
                $this->trackLog("headImgUrl", $headImgUrl);
                $this->trackLog('--sync to OSS end--');
            }
            
            $tempPath = str_replace('Raw', 'Thumb', './Uploads/' . $info['file']['savepath']);
            if(!file_exists($tempPath))
            { 
                mkdir($tempPath);
            }
            $thumbPath = str_replace('Raw', 'Thumb', '.' . $localHeadImgUrl);
            $this->trackLog("thumbPath", $thumbPath);
            //产生缩略图
            $image = new Image();
            $image->open('.' . $localHeadImgUrl);
            $image->thumb(300, 200)->save($thumbPath);

            $data['code']=1;
            $data['msg']="上传成功" ;
            $data['data']['url'] = $localHeadImgUrl;
            $data['data']['thumburl'] = str_replace('Raw', 'Thumb', $localHeadImgUrl);
            $this->ajaxReturn($data);
        }
    }
    
    
    public function add_material(){
        
        $file_info=array(
            'filename'=>'/gxtd.jpg',  //国片相对于网站根目录的路径
            'content-type'=>'image/jepg',  //文件类型
            'filelength'=>'60540'         //图文大小
        );
        $access_token= "6RGcJx4JOvRNuKfTpG7gfCu47cAbzKp7SUCmgDHyG4eFkjoaDctOh5jp1K8Mv-zhns2OPFdHLjYEWTo2peUE67W61NuVPDM64MBDkuMOYkd9Ju0lbpQkuqvsuBb5fWoXMGGhADAHBB";
        $url="https://api.weixin.qq.com/cgi-bin/material/add_material?access_token={$access_token}&type=image";
        $ch1 = curl_init ();
        $timeout = 5;
        $real_path="{$_SERVER['DOCUMENT_ROOT']}{$file_info['filename']}";
        //$real_path=str_replace("/", "\\", $real_path);
        $data= array("media"=>"@{$real_path}",'form-data'=>$file_info);
        $this->trackLog("data", $data);
        curl_setopt ( $ch1, CURLOPT_URL, $url );
        curl_setopt ( $ch1, CURLOPT_POST, 1 );
        curl_setopt ( $ch1, CURLOPT_RETURNTRANSFER, 1 );
        curl_setopt ( $ch1, CURLOPT_CONNECTTIMEOUT, $timeout );
        curl_setopt ( $ch1, CURLOPT_SSL_VERIFYPEER, FALSE );
        curl_setopt ( $ch1, CURLOPT_SSL_VERIFYHOST, false );
        curl_setopt ( $ch1, CURLOPT_POSTFIELDS, $data );
        $result = curl_exec ( $ch1 );
        curl_close ( $ch1 );
 
        $result=json_decode($result,true);
        $this->trackLog("media_id", $result['media_id']); 
    }
    
}