<?php

namespace Home\Service;

use Think\Model;
use Think\Log;
use Think\Verify;

class UtilsService extends BaseService {

     public function getCaptcha($phone, $verifyCode, $type){

        $this->trackLog("execute", "getCaptcha()");

        $verify = new Verify();

        if ($this->limitIP()) {
            $verify = new Verify();
            if(!empty($verifyCode)) {
                $valid = $verify->check($verifyCode);
            } else {
                $valid = $verify->simpleCheck();
            }
            $this->trackLog("valid", $valid);
            $valid=true;
            if ($valid){ // 图形验证码验证
                $this->sendSMSCaptcha($phone, $type);
                $data['code'] = 1;
                $data['msg'] = "短信验证码已发出，请耐心等候！";
                return $data;
            }else{
                $data['code'] = 0;
                $data['msg'] = "请不要恶意请求短信验证码哦！";
                return $data;
            }
        }else{
            $data['code'] = 1;
            $data['msg'] = "短信验证码已发出，1分钟内请不要重复申请啦。";
            return $data;
        }
    }
    
     // 发送6位手机验证码
    public function sendSMSCaptcha($phone, $type) {

        $this->trackLog("execute", "sendSMSCaptcha()");

        $enum    = "0123456789";
        $captcha = "";
        if ($type != 0) {
            $userId = I('session.x_user')['id'];
        } else {
            $userId = 'NULL';
        }

        for ($i = 0; $i < 6; $i++) {
            $num[$i] = rand(0, 9);
            $captcha .= $enum[$num[$i]];
        }

        session("captchaPhone", $phone);
        session("captcha", $captcha);

        $this->trackLog("captchaPhone", session("captchaPhone"));
        $this->trackLog("captcha", session("captcha"));
        $this->trackLog("type",$type);
        if ($type == 3) {
            payReset($userId,$phone,$captcha,1);
            $this->trackLog("payReset","payReset()");
        } else {
            sendCaptcha($userId,$phone, $captcha, $type,1);
            $this->trackLog("sendCaptcha","sendCaptcha()");//美圣短信发送接口返回参数日志
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
    
    public function uploadImageToWechat(){
 
        /* 新增一个永久的素材 */
        $c = "1";
        if($c == "1"){
            $url = "http://api.weixin.qq.com/cgi-bin/material/add_material?access_token=".'j-7S4K5KESo_tSfjCKc7TK-20yw2fJ4g7XZfwY1GyMw6wj2NGohckX8p4IGIS3-udHqOyRrtOX8OHfTCIClhrbn_O36SRChqMOu4MHLXtqXbULu6iHOQrA6elxBffm2xRUSaAFAADB'."&type=image";
            
            $josn2 = array("media"=>"@"."/home/wwwroot/test-wechat.luobojianzhi.com/Uploads/gxtd20161104135427.jpg");
             
            $ret = $this->https_request($url,$josn2);
            $row = json_decode($ret);
            $this->trackLog("media_id", $row->media_id);
        }
    }
 
    private function https_request($url, $data = null){
        $curl = curl_init();
        curl_setopt($curl, CURLOPT_URL, $url);
        curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, FALSE);
        curl_setopt($curl, CURLOPT_SSL_VERIFYHOST, FALSE);
        if (!empty($data)){
            curl_setopt($curl, CURLOPT_POST, 1);
            curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
        }
        curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);
        $output = curl_exec($curl);
        curl_close($curl);
        return $output;
    }

} 
 
?>
