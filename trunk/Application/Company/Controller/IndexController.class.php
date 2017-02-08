<?php
namespace Company\Controller;

use Think\Controller;
use Think\Model;

use Com\Wechat;
use Com\WechatAuth;

use Org\Wechat\MyWechat;
use Org\Wechat\Jssdk;

use Think\Verify;


/**
 * Class IndexController
 * 微信消息接口入口
 * 微信公众平台后台填写的api地址则为该操作的访问地址
 *
 * @package Home\Controller
 */
class IndexController extends BaseController {
    
    static $wx;

    public function indexBak(){

        $this->trackLog("excute", "IndexController");

        $wechat = new MyWechat(C('WT_APP_TOKEN'), true);
        $wechat->run();

    }

  
    public function FindMyPwd() {
  
    $this->trackLog("execute", "FindMyPwd()");

    $phone = I('phone', '', 'trim');
    $password = I('password', '', 'trim');
    
    $captcha = I('captcha', '', 'trim');


    $this->trackLog('phone', $phone);
    $this->trackLog('password', $password);
    $this->trackLog('md5(password)', md5($password));
    $this->trackLog('captcha', $captcha);

    if(!is_numeric($phone)) {
        $data['code']  = 0;
        $data['msg']  = "参数格式不正确！";
        $this->ajaxReturn($data);
    }

    $this->trackLog("captchaPhone", session('captchaPhone'));
    $this->trackLog("captcha", session('captcha'));

    if ($phone != session('captchaPhone') || $captcha != session('captcha')) {
        $data['code']  = 0;
        $data['msg']  = "手机验证码错误！";
        $this->ajaxReturn($data);
    }

    $password = md5($password);
    $password = "'".$password."'";

    $extSql = "UPDATE company SET password = $password WHERE phone=$phone";
    $this->trackLog("extSql", $extSql);


    $Model = new Model();
    $result = $Model->execute($extSql);
    $this->trackLog("result", $result);


    if($result) {

        $data['code']  = 1;
        $data['msg']  = "密码重置成功啦！";
       
        $this->ajaxReturn($data);

    }else{

        $data['code']  = 0;
        $data['msg']  = "密码重置失败啦！";
        $this->ajaxReturn($data);

    }     

}

    public function changePwd(){

    $this->trackLog("execute", "changePwd()");
    
    $newPwd = I('newPwd','','trim');
    $reNewPwd = I('reNewPwd','','trim');
    $CompanyId = session("x_company")['id'];//session取得用户ID

    $this->trackLog("newPwd", $newPwd);
    $this->trackLog("reNewPwd", $reNewPwd);
    $this->trackLog("CompanyId", $CompanyId);

    $result=$this->checkNewPwd($newPwd,$reNewPwd);

    if($result){

        $reNewPwd = md5($reNewPwd);
        $reNewPwd = "'".$reNewPwd."'";
        $db = new Model();
        $extSql = "UPDATE company SET PASSWORD= $reNewPwd WHERE id = $CompanyId";

        $this->trackLog("extSql", $extSql);
        $resultData = $db->execute( $extSql);
        $this->trackLog("resultData", $resultData);
        if($resultData){

        $result['code']  = 1;
        $result['msg']  = "修改成功";
        $this->ajaxReturn($result);
      }else{

        $result['code']  = 0;
        $result['msg']  = "修改失败";
        $this->ajaxReturn($result);
      }

    }else{

      $data['code']  = 0;
      $data['msg']  = "两次密码输入不一样！";
      $this->ajaxReturn($data);
    }
  }


  public function checkOldPwd(){

     $this->trackLog("execute", "checkOldPwd()");

     $oldPwd = session("x_company")['password'];// session取得传过来
     $oldInput = I('oldInput','','trim');//用户输入的密码

     $regex = "/\/|\~|\!|\@|\#|\\$|\%|\^|\&|\*|\(|\)|\_|\+|\{|\}|\:|\<|\>|\?|\[|\]|\,|\.|\/|\;|\'|quot|\`|\-|\=|\\\|\|/";
     $oldInput= preg_replace($regex,"",$oldInput);

     $this->trackLog("oldPwd", $oldPwd);
     $this->trackLog("oldInput", $oldInput);

     $oldInput = md5($oldInput); 
     
    
     if( $oldInput==null || $oldInput=="" ){

      $data['code']  = 0;         
      $data['msg']  = "参数格式不正确！";  
      $this->ajaxReturn($data);        
    }

    if($oldPwd==$oldInput) {

      $data['code']  = 1;
      $data['msg']  = "旧密码输入正确";
      $this->ajaxReturn($data);
    }else{

      $data['code']  = 0;
      $data['msg']  = "旧密码输入错误！";
      $this->ajaxReturn($data);
    }
  }  

    //验证两次新密码是不是一样 1 一样 0  不一样
  public function checkNewPwd($newPwd,$reNewPwd){

    $this->trackLog("execute", "checkNewPwd()");

    $this->trackLog("newPwd", $newPwd);
    $this->trackLog("reNewPwd", $reNewPwd);

    if($newPwd==$reNewPwd){

      return 1;
    }else{

      return 0;
    }
  }


  public function isLogin(){

   $this->trackLog("execute", "isLogin()");
   $isLogin =validateCompanyLogin();

   $data['code']  = 0;
   $data['msg']  = "unauthorized";
   if($isLogin){

    $data['code']  = 1;
    $data['msg']  = "authorized";
  }

  $this->ajaxReturn($data);
}
    

    // 生成图形验证码
    public function getThinkCode(){

        $config =   array(
            'useImgBg'  =>  false,           // 使用背景图片
            'fontSize'  =>  12,              // 验证码字体大小(px)
            'useCurve'  =>  false,            // 是否画混淆曲线
            'useNoise'  =>  true,            // 是否添加杂点
            'imageH'    =>  30,               // 验证码图片高度
            'imageW'    =>  90,               // 验证码图片宽度
            'length'    =>  4,               // 验证码位数
            'fontttf'   =>  '',              // 验证码字体，不设置随机获取
            'bg'        =>  array(243, 251, 254),  // 背景颜色
            'reset'     =>  true,           // 验证成功后是否重置
        );
        // 使用tp自带的验证码类
        $Verify = new Verify($config);
        $Verify->entry();
    }

    //pc端调用接口
      public function pcChangePwd(){
           
        $regex = "/\/|\~|\!|\@|\#|\\$|\%|\^|\&|\*|\(|\)|\_|\+|\{|\}|\:|\<|\>|\?|\[|\]|\,|\.|\/|\;|\'|quot|\`|\-|\=|\\\|\|/";

        $oldInput = I('oldPwd','','trim');//用户输入的旧密码
        $oldInput= preg_replace($regex,"",$oldInput);
        $oldInput= md5($oldInput);
        $this->trackLog("oldInput",  $oldInput);

        $newInput = I('newPwd','','trim');//用户输入的新密码
        $newInput= preg_replace($regex,"",$newInput);
        $newInput= md5($newInput);
        $this->trackLog("newIput", $newInput);
        
        if($newInput == $oldInput){
            $data['code']  = 0;
            $data['msg']  = "新旧密码不能相同";
            $this->ajaxReturn($data);
        }
            
        $companyId = I('companyId','','trim');//PC端传过来的公司Id
        $this->trackLog("companyId", $companyId);

        $checkSql = "select * from  company  where id = '".$companyId."' and password = '".$oldInput."'";
        $oldIsRight = M()->query($checkSql);
       
        
        if($oldIsRight){
            $updateSql = "UPDATE company SET PASSWORD='".$newInput."'WHERE id = '".$companyId ."'";
            $updateSuccess = M()->execute($updateSql);
           
            if($updateSuccess){
                $data['code']  = 1;
                $data['msg']  = "更新成功";
                $this->ajaxReturn($data);
            }else{
                $data['code']  = 0;
                $data['msg']  = "更新失败";
                $this->ajaxReturn($data);
            }

        }else{
            $data['code']  = 0;
            $data['msg']  = "旧密码输入错误！";
            $this->ajaxReturn($data);
        }
           
      }
    //pc端调用接口(找回密码)
    public function pcFindMyPwd(){
        $this->trackLog("execute", "pcFindMyPwd()");
        $phone = I('phone','','trim');
        $newPwd = I('newPwd','','trim');
        $newPwd = md5($newPwd);
        $this->trackLog("phone", $phone);
        $this->trackLog("newPwd",$newPwd);

        $Model = new Model();
        $extSql = "SELECT * FROM Company WHERE phone='$phone'";
        $this->trackLog("extSql", $extSql);
        $result1 = $Model->query($extSql);
        $this->trackLog("result1", $result1);
        if($result1) {
            $Model = new Model();
            $extSql = "UPDATE Company SET password = '$newPwd' WHERE phone='$phone'";
            $this->trackLog("extSql", $extSql);
            $result = $Model->execute($extSql);
            $this->trackLog("result", $result);
            $data['code']  = 1;
            $data['msg']  = "重置密码成功！";
            $this->ajaxReturn($data);
        }else{
            $data['code']  = 0;
            $data['msg']  = '重置密码失败！';
            $this->ajaxReturn($data);
        }

    }
}





//$updataSql = "UPDATE company SET password = $newPwd WHERE phone=$phone";
//$updateSuccess = M()->execute($updateSql);
//
//if ($updateSuccess){
//    $data['code']  = 1;
//    $data['msg']  = '重置成功';
//    $this->ajaxReturn($data);
//}else{
//    $data['code']  = 0;
//    $data['msg']  = '重置失败';
//    $this->ajaxReturn($data);
//}







