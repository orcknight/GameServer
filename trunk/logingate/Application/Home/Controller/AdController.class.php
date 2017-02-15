<?php
// +----------------------------------------------------------------------
// | 基础业务CRUD方法范例
// +----------------------------------------------------------------------
// | Author: James.Yu <zhenzhouyu@jiechengkeji.cn>
// +----------------------------------------------------------------------
namespace Home\Controller;
use Think\Controller;
use Think\Model;
use Think\Log;

class AdController extends WtAuthController {

    public function _initialize(){
        parent::_initialize();
    }

    // 北京入口
    public function signUpBeiJing(){

        $this->trackLog("execute", "signUpBeiJing()");

        $realName = I('realname', '', 'strip_tags');
        $phone = I('phone', '', 'strip_tags');
        $captcha = I('captcha', '', 'strip_tags');
        $password = I('password', '', 'strip_tags');
        $password =md5($password);
        $provinceId = I('province', '', 'strip_tags');
        $cityId = I('city', '', 'strip_tags');
        $regionId = I('region', '', 'strip_tags');
        $schoolId = I('school', '', 'strip_tags');
        $grade = I('grade', '', 'strip_tags');
    
        $sessionPhone = $_SESSION['captchaPhone'];
        $sessionCaptcha = $_SESSION['captcha'];
        $openid= $_SESSION['wt_user']['openid'];

        $this->trackLog("realName", $realName);
        $this->trackLog("phone", $phone);
        $this->trackLog("captcha", $captcha);
        $this->trackLog("password", $password);
        $this->trackLog("provinceId", $provinceId);
        $this->trackLog("cityId", $cityId);
        $this->trackLog("regionId", $regionId);
        $this->trackLog("schoolId", $schoolId);
        $this->trackLog("grade", $grade);
        $this->trackLog("openid", $openid);

        if( $captcha!=$sessionCaptcha) {
             $data['code']  = 0;
             $data['msg']  = "手机验证码出错啦！";
             $this->ajaxReturn($data);
        }

        $extSql = "INSERT into user(realname, grade, password, phone, provinceId, cityId, regionId, schoolId, openid) values
        ('$realName', '$grade', '$password', '$phone', '$provinceId', '$cityId', '$regionId', '$schoolId', '$openid')";
        $this->trackLog("extSql", $extSql);

        $db = new Model();
        $result =$db->execute($extSql);
        $this->trackLog("result", $result);

        $extSql = "SELECT id, phone, username, password, status FROM user WHERE phone='$phone' AND password='$password'";
        $this->trackLog("extSql", $extSql);
        $result = $db->query($extSql);
        $this->trackLog("result", $result);
        
        if($result) {
            $user = $result[0];
            session('x_user', $user);

            if($openid){ 
                
                 //用户与wechat绑定
                 $result = $db->query("SELECT openid from user where phone = $phone ");

                 $userOpenid = $result[0]['openid'];
                 $this->trackLog("userOpenid", $userOpenid);

                 if(!$userOpenid) {
                    $extSql = "UPDATE user set openid ='$openid' where phone = '$phone'";
                    $this->trackLog("extSql", $extSql);
                    $result = $db->execute($extSql);
                    $this->trackLog("result", $result);
                 }

                // 更新萝卜头领注册量
                $result = $db->query("SELECT chiefid FROM ac_chief_fans WHERE openid='$openid' AND bind=0");
                $chiefId = $result[0]['chiefid'];
                $this->trackLog("chiefId", $chiefId);

                if($chiefId) {
                    // 累加头领注册人数
                    $result = $db->execute("UPDATE ac_chief SET register=register+1 WHERE id='$chiefId'");
                    $this->trackLog("result", $result);
                    // 更新粉丝绑定状态
                    $result = $db->execute("UPDATE ac_chief_fans SET bind=1 WHERE openid='$openid'");
                    $this->trackLog("result", $result);
                }


           
           }
            $data['code']  = 1;
            $data['msg']  = "用户注册成功啦！";
            $data['data'] = $result[0];
            $this->ajaxReturn($data);
        } 
        else{
            $data['code']  = 0;
            $data['msg']  = "用户创建失败啦";
            $this->ajaxReturn($data);
        }
    }
 }


