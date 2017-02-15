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
use OSS\OssClient;
use OSS\Core\OssException;
use Company\Service\CompanyService;


class UserController extends BaseController{


    /**
    * 企业登录接口
    */
    public function signIn() {
        $this->trackLog("execute", "doSignIn()");

        $phone = I('phone', '', 'trim');
        $password = I('password', '', 'trim');

        $this->trackLog("phone", $phone);
        $this->trackLog("password", $password);
        $this->trackLog("md5(password)", md5($password));

        if(empty($phone)|| empty($password)) {
            $data['code']  = 0;
            $data['msg']  = "缺少必要参数";
            $this->ajaxReturn($data);
        }

        $password = md5($password);

        $extSql = "SELECT id, phone, companyName, password, status, roleId  FROM company WHERE phone='$phone' AND password='$password'";
        $this->trackLog("extSql", $extSql);

        $Model = new Model();
        $result = $Model->query($extSql);
        $this->trackLog("result", $result);

        if(!empty($result)) {

            $user = $result[0];
            session('x_company', $user);

	        $oldOpen = $Model->query("SELECT openid from company where phone = $phone ");
            $oldOpen = $oldOpen[0]['openid'];
            $this->trackLog("oldOpen",$oldOpen);

            if(!$oldOpen){
                $openId =$_SESSION['wt_user']['openid'];

                $this->trackLog("openId", $openId);

                $openSQL = "UPDATE company set openid ="."'".$openId."'"." where phone = ".$phone;
                $this->trackLog("openSQL", $openSQL);

                $openRes = $Model->execute($openSQL);
                $this->trackLog("openRes", $openRes);
            }

            $data['code']  = 1;
            $data['msg']  = "企业已经查找到。";
            $this->ajaxReturn($data);
        }else{
            
            $data['code']  = 0;
            $data['msg']  = "用户名或者密码错误！";
            $this->ajaxReturn($data);
        }
        
    }
    public function pcSignIn() {
        
        $this->trackLog("execute", "doSignIn()");

        $phone = I('phone', '', 'trim');
        $password = I('password', '', 'trim');

        $this->trackLog("phone", $phone);
        $this->trackLog("password", $password);
        $this->trackLog("md5(password)", md5($password));

        if(empty($phone)|| empty($password)) {
  	        $data['code']  = 0;
  	        $data['msg']  = "缺少必要参数";
  	        $this->ajaxReturn($data);
        }

        $password = md5($password);

        $extSql = "SELECT id, phone, companyName, password, status, roleId  FROM company WHERE phone='$phone' AND password='$password'";
        $this->trackLog("extSql", $extSql);

        $Model = new Model();
        $result = $Model->query($extSql);
        $this->trackLog("result", $result);

        if($result) {

  	        $user = $result[0];

  	        $data['code']  = 1;
  	        $data['msg']  = "企业已经查找到。";
  	        $data['data'] =$user;
  	        $this->ajaxReturn($data);
        }else{
  	        $data['code']  = 0;
  	        $data['msg']  = "用户名或者密码错误！";
  	        $this->ajaxReturn($data);
        }

    }

    /**
     * pc调用wechat企业注册
     */
    public function pcSignUp(){
        
        $this->trackLog("execute", "pcSignUp()");
        
        $phone = I('phone', '', 'trim');
        $password = I('password', '', 'trim');
        
        $args = array(
        'phone' => $phone,
        'password' => md5($password),
        'agree' => '1',
        'openId' => '',
        );
        $this->trackLog("args", $args);

        $companyService = new CompanyService();
        $this->ajaxReturn($companyService->pcSignUp($args));
    }

    /**
    * 新版企业注册
    */
    public function signUP() {

        $this->trackLog("execute", "doSignUP()");

        $phone = I('phone', '', 'trim');
        $password = I('password', '', 'trim');
        $captcha = I('captcha', '', 'trim');
        $agree = I('agree', '', 'trim');
        $openId= $_SESSION['wt_user']['openid'];

        $args = array(
        'phone' => $phone,
        'password' => md5($password),
        'captcha' => $captcha,
        'agree' => $agree,
        'openId' => $openId,
        );
        $this->trackLog("args", $args);

        $companyService = new CompanyService();
        $this->ajaxReturn($companyService->signUp($args));   
    }

    public function signOut() {

        $this->trackLog("execute", "signOut()");

        $_SESSION['x_company'] = null;

        $check = $_SESSION['x_company'];
        if(empty($check)) {
            $data['code']  = 1;
            $data['msg']  = "退出成功！";
            $this->ajaxReturn($data);
        }else{

            $data['code']  = 0;
            $data['msg']  = "退出失败！";
            $this->ajaxReturn($data);
        }
    }

    //企业用户详情
    public function detail(){
       
      $this->trackLog("execute", "detail()");
      $uId = I('session.x_company')['id']?: I('request.companyId');//优先从session中取，取不到就用参数中的
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
          u.status,
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

      if($result) {
          $data['code']  = 1;
          $data['msg']  = "查询成功";
          $data['data'] = $result[0];
          $this->ajaxReturn($data);
      } else {
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
     * 完善基本信息
     */
    public function doPerfectInfo(){
      
        $uid = I('session.x_company')['id'];
        $this->trackLog("session", I('session.x_company'));
        $this->trackLog("uid", $uid);

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
        
        if(empty($address) || empty($cityId) || empty($provinceId) || empty($companyName) || empty($licenceimage)){
              $data['code']  = 0;
              $data['msg']  = "表单信息不完整";
              $this->ajaxReturn($data);
        }
      
        $extSql= "update company set address= '{$address}' "
                . ",companyName='{$companyName}',"
                . "licenceImage='{$licenceimage}',"
                . "localLicenceImage='{$locallicenceimage}',"                
                . "cityId='{$cityId}',provinceId='{$provinceId}', status=1 "
                . "where id = {$uid}";
         
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
        try {

            $this->trackLog("execute", "uploadUserPhoto()");

            $uId = session("x_company")['id'];
            if(empty($uId)){
                 $data['code']=0;
                 $data['msg']='unauthorized';
                 $this->ajaxReturn($data);
            }

            $upload           = new \Think\Upload();// 实例化上传类
            $upload->maxSize  = 2097152;
            $upload->exts     = array ('jpg', 'gif', 'png', 'jpeg');
            $upload->rootPath = './Uploads/';
            $upload->savePath = 'LicenceIMG/'; // 设置附件上传（子）目录

            // 上传文件
            $info = $upload->upload();
            if (!$info) {// 上传错误提示错误信息
                 $data['code']=0;
                 $data['msg']=$upload->getError();
                 $this->ajaxReturn($data);
            } else {// 上传成功

                $this->trackLog("LicenceIMGUpdateinfo", $info);
                // 本地用户头像
                $localLicenceImage = '/Uploads/' . $info['file']['savepath'] . $info['file']['savename'];
                $this->trackLog("localLicenceImage", $localLicenceImage);

                // 同步到OSS
                Vendor('OSS.autoload');
                $ossClient = new OssClient(C('OSS_ACCESS_KEY_ID'), C('OSS_ACCESS_KEY_SECRET'), C('OSS_ENDPOINT'));
                $headImgKey = $info['file']['savename'];
                $content = file_get_contents("./".$localLicenceImage);
                $ossClient->putObject(C('OSS_BUCKET'), $headImgKey, $content);

                $licenceImage = C('OSS_URL') . $headImgKey;
                $this->trackLog("licenceImage", $licenceImage);

                $data['code']=1;
                $data['msg']="上传成功" ;
                $data['data']['url'] = $licenceImage;
                $data['data']['localURL'] = $localLicenceImage;
                $this->ajaxReturn($data);
            }
        } catch(OssException $e) {
            $data['code']=0;
            $data['msg']="上传失败" ;
            $this->ajaxReturn($data);
            //printf(__FUNCTION__ . ": FAILED\n");
            //printf($e->getMessage() . "\n");
        }
    }

    public function uploadCompanyLogo(){
         try {
            $this->trackLog("execute", "uploadCompanyLogo()");

            $uId = session("x_company")['id'];
            if(empty($uId)){
                 $data['code']=0;
                 $data['msg']='unauthorized';
                 $this->ajaxReturn($data);
            }

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

                $this->trackLog("LicenceIMGUpdateinfo", $info);
                // 本地用户头像
                $localLogo = '/Uploads/' . $info['file']['savepath'] . $info['file']['savename'];
                $this->trackLog("localLogo", $localLogo);

                // 同步到OSS
                Vendor('OSS.autoload');
                $ossClient = new OssClient(C('OSS_ACCESS_KEY_ID'), C('OSS_ACCESS_KEY_SECRET'), C('OSS_ENDPOINT'));
                $headImgKey = $info['file']['savename'];
                $content = file_get_contents("./".$localLogo);
                $ossClient->putObject(C('OSS_BUCKET'), $headImgKey, $content);

                $logo = C('OSS_URL') . $headImgKey;
                $this->trackLog("logo", $logo);

                $db = new Model();
                $extSql = "UPDATE COMPANY SET logo = '$logo', localLogo = '$localLogo' where id = $uId";
                $re = $db->execute( $extSql);
                $this->trackLog("extSql", $extSql);
                if($re){
                    $data['code']=1;
                    $data['msg']="上传成功" ;
                    $data['data']['url'] = $localLogo;
                    $this->ajaxReturn($data);
                }else{
                    //TODO添加 失败后 删除照片的逻辑
                    $data['code']=0;
                    $data['msg']="上传失败" ;
                    $this->ajaxReturn($data);
                }
            }
        } catch(OssException $e) {
            printf(__FUNCTION__ . ": FAILED\n");
            printf($e->getMessage() . "\n");
            return;
        }
    }
    /***
    * 为完成跨域请求而做的冗余
    **/
    public function getCompanyId(){
        
        $this->trackLog("execute", "getCompanyId()");
        
        if(I('session.x_company')['id']){
            return I('session.x_company')['id'];
        }
      
      return 0;
    }
    public function IsPasswordEmpty(){
        
        $this->trackLog("execute", "IsPasswordEmpty()");
        
        $companyId = $this->getCompanyId();
        $this->trackLog("companyId", $companyId);
        
        $db = new Model();
        $sql = "SELECT password FROM wallet WHERE companyId='$companyId'";
        $this->trackLog("sql", $sql);
        $password = $db->query($sql);
        $password = $password[0]["password"];
        $this->trackLog("password", $password);
        
        if($password){
            $this->ajaxReturn(TRUE);
        }
        $this->ajaxReturn(FALSE);
    }
}