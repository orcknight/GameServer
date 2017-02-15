<?php
// +----------------------------------------------------------------------
// | 用户相关业务逻辑
// +----------------------------------------------------------------------
// | Author: James.Yu <zhenzhouyu@jiechengkeji.cn>
// | CreateTime: 2016/05/13
// | UpdateTime: 2016/05/13
// +----------------------------------------------------------------------

namespace Company\Controller;

use Think\Controller;
use Think\Model;
use Think\Log;

class CompanyController extends BaseController{


  //企业用户详情
  public function detail(){
       
      $this->trackLog("execute", "detail()");
      $uId = session("x_company")['id'];//session取得用户ID
      $this->trackLog("uId", $uId);
      
      if(!is_numeric($uId))
      {
        $data['code']  = 0;
        $data['msg']  = "参数不合法！";
        $this->ajaxReturn($data);
      }

      $extSql = "SELECT  
			u.id,
			u.phone,
			u.username,
			u.realname,
			u.companyName,
			u.email,
			u.address,
                        u.logo,
			u.licenceCode,
			u.licenceImage,
			u.intro,
			u.openid,
			u.unionid,
			u.provinceId,
			u.cityId,
			u.regionId,
			p.name AS province,
			c.name AS city
                FROM company u
                        LEFT JOIN gp_province  p ON u. provinceId = p.id 
                        LEFT JOIN gp_city c ON u.cityId= c.id 
                WHERE u.id=$uId";
      $this->trackLog("extSql", $extSql);

      $db = new Model();
      $result=$db->query($extSql);
      $this->trackLog("result", $result);

      if($result)
      {
        $data['code']  = 1;
        $data['msg']  = "查询成功";
        $data['data'] = $result[0];
        $this->ajaxReturn($data);
      }else
      {
        $data['code']  = 0;
        $data['msg']  = "没有记录！";
        $this->ajaxReturn($data);
      }
    }

    
    /**
     * 修改联系人等信息
     */
    public function updateBizInfo(){
         
         $realname = I('realname');
         $intro = I("intro");
         $uid = I('session.x_company')['id'];
         
         if(!$uid){
              $data['code']  = 0;
              $data['msg']  = "请先登录";
              $this->ajaxReturn($data);
         }
         
         if(empty($realname)||empty($intro)){
              $data['code']  = 0;
              $data['msg']  = "提交的信息不完善";
              $this->ajaxReturn($data);
         }
         $this->trackLog("execute", "updateBizInfo()");
         $extSql= "update company set realname= '$realname' ,intro='$intro' where id = $uid";
         $db = new Model();
         $result=$db->execute($extSql);
         $this->trackLog("result", $result);
         if($result) {
	        $data['code']  = 1;
	        $data['msg']  = "信息已更新";
	        $this->ajaxReturn($data);
            }else{
	        $data['code']  = 0;
	        $data['msg']  = "信息更新失败";
	        $this->ajaxReturn($data);
            }
    }  
    /*
     * 完善信息
     */
  public function doPerfectInfo(){
      
       $uid = I('session.x_company')['id'];
        if(!$uid){
             $data['code']  = 0;
             $data['msg']  = "请先登录";
             $this->ajaxReturn($data);
        }
        
        $this->trackLog("execute", "updateBizInfo()");
  
        $companyName=I('request.companyName');
        $address =I('request.address');
        $cityId = I('request.cityId');
        $provinceId = I('request.provinceId');
        $licenceimage = I('request.licenceimage');
        
        if(empty($address)||empty($cityId)||empty($provinceId)||empty($companyName)||empty($licenceimage)){
              $data['code']  = 0;
              $data['msg']  = "表单信息不完整";
              $this->ajaxReturn($data);
        }
      
        $extSql= "update company set address= '{$address}' "
                . ",companyName='{$companyName}',"
                . "licenceImage='{$licenceimage}',"
                . "cityId='{$cityId}',provinceId='{$provinceId}',status=1 "
                . " where id = {$uid}";
         
        $db = new Model();
        $result=$db->execute($extSql);
        $this->trackLog("result", $result);
        if($result) {
               $data['code']  = 1;
               $data['msg']  = "信息已更新";
               $this->ajaxReturn($data);
           }else{
               $data['code']  = 0;
               $data['msg']  = "信息更新失败";
               $this->ajaxReturn($data);
           }
      
  }

    public function uploadLicenceImage(){

            $this->trackLog("execute", "uploadLicenceImage()");

            $upload           = new \Think\Upload();// 实例化上传类
            $upload->maxSize  = 2097152;
            $upload->exts     = array ('jpg', 'gif', 'png', 'jpeg');
            $upload->rootPath = './Uploads/';
            $upload->savePath = 'LicenceImage/'; // 设置附件上传（子）目录
            // 上传文件
            $info = $upload->upload();
            if (!$info) {// 上传错误提示错误信息
                 $data['code']=0;
                 $data['msg']=$upload->getError();
                 $this->ajaxReturn($data);
            } else {// 上传成功
                 $this->trackLog("uploadLicenceImage", $info);
                 $url = '/Uploads/' . $info['file']['savepath'] . $info['file']['savename'];
                 $data['code']=1;
                 $data['msg']="上传成功" ;
                 $data['data']['url']=$url;
                 $this->ajaxReturn($data);
            }

        }

  

  

    
     public function uploadCompanyLogo(){
        $uid = I('session.x_company')['id'];
        if(!$uid){
             $data['code']  = 0;
             $data['msg']  = "请先登录";
             $this->ajaxReturn($data);
        }
        $this->trackLog("execute", "uploadCompanyLogo()");

        $upload           = new \Think\Upload();// 实例化上传类
        $upload->maxSize  = 2097152;
        $upload->exts     = array ('jpg', 'gif', 'png', 'jpeg');
        $upload->rootPath = './Uploads/';
        $upload->savePath = 'CompanyLogo/'; // 设置附件上传（子）目录
        // 上传文件
        $info = $upload->upload();
        if (!$info) {// 上传错误提示错误信息
             $data['code']=0;
             $data['msg']=$upload->getError();
             $this->ajaxReturn($data);
        } else {// 上传成功
             $url = '/Uploads/' . $info['file']['savepath'] . $info['file']['savename'];
             $db = new Model();
             $extSql = "UPDATE Company SET logo='".$url."' where id = $uid";
             $re = $db->execute( $extSql);
             $this->trackLog("extSql", $extSql);
             if($re){
                $data['code']=1;
                $data['msg']="上传成功" ;
                $data['data']['url']=$url;
                $this->ajaxReturn($data);
             }else{
                //TODO添加 失败后 删除照片的逻辑
                $data['code']=0;
                $data['msg']="上传失败" ;
                $this->ajaxReturn($data);
             }
        }

    }

    

}