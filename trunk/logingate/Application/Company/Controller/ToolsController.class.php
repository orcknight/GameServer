<?php
namespace Company\Controller;
use Think\Controller;
use Think\Log;
use Think\Model;
use Think\Verify;


class ToolsController extends BaseController {

    // 发送6位手机验证码
    public function sendSMSCaptcha($phone,$type) {

        $this->trackLog("execute", "sendSMSCaptcha()");

        $enum    = "0123456789";
        $captcha = "";

        if ($type != 0) {
            $companyId = I('session.x_company')['id'];
        } else {
            $companyId = 'NULL';
        }

        for ($i = 0; $i < 6; $i++) {
            $num[$i] = rand(0, 9);
            $captcha .= $enum[$num[$i]];
        }

        session("captchaPhone", $phone);
        session("captcha", $captcha);

        $this->trackLog("captchaPhone", session("captchaPhone"));
        $this->trackLog("captcha", session("captcha"));

        if ($type == 3) {
            payReset($companyId,$phone,$captcha,0);
            $this->trackLog("payReset","payReset()");
        } else {
            sendCaptcha($companyId,$phone, $captcha, $type,0);
            $this->trackLog("sendCaptcha","sendCaptcha()");//新短信平台
        }

    }

    public function getCaptcha(){

        $this->trackLog("execute", "getCaptcha()");

        //判断用户名及手机是否存在
        $phone = I('phone', "trim");
        $verifyCode = I('verifyCode', "luobojianzhi", "trim");
        $type = I('type', "0", "trim"); // type:0 注册获取验证码 / type:1 找回密码验证码

        $this->trackLog("phone", $phone);
        $this->trackLog("verifyCode", $verifyCode);
        $this->trackLog("type", $type);

       

        if(!is_numeric($phone)) {
            $data['code']  = 0;
            $data['msg']  = "数据格式不正确！";
            $this->ajaxReturn($data);
        }

        $extSql = "SELECT * FROM Company WHERE phone='$phone'";
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

        if ($type == 3 && !$result) {
            $data['code']  = 0;
            $data['msg']  = "非常遗憾，手机号在系统不存在！";
            $this->ajaxReturn($data);
        }

        $verify = new Verify();

        if ($this->limitIP()) {

            $verify = new Verify();

            if(!empty($verifyCode)) {


                $valid = $verify->check($verifyCode);

            } else {

                $valid = $verify->simpleCheck();

            }
            $this->trackLog("valid", $valid);
            
            $valid =true;

            if ($valid){ // 图形验证码验证

                $this->sendSMSCaptcha($phone,$type);

                $data['code'] = 1;
                $data['msg'] = "短信验证码已发出，请耐心等候！";
                $this->ajaxReturn($data);

            }else{

                $data['code'] = 0;
                $data['msg'] = "请不要恶意请求短信验证码哦！";
                $this->ajaxReturn($data);                

            }
        
        }else{

            $result['code'] = 1;
            $result['msg'] = "短信验证码已发出，1分钟内请不要重复申请啦。";
            $this->ajaxReturn($result);

        }
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
}