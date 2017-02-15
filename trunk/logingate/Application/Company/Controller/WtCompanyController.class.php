<?php
namespace Company\Controller;

use Think\Controller;
use Think\Page;
use Think\Model;


class WtCompanyController extends WtAuthController{
    
    public function _initialize() {
        
        $this->trackLog("execute", "WtCompanyController()");

        // 每次调用前做oauth验证
        parent::_initialize();
    }

    /**
     * 获取用户微信信息判断画面跳转情况
     */
    public function signEntrance(){
        $this->trackLog("execute", "getUserWtInfo()");
        // 微信信息
        $wtUser = session("wt_user");
        // 账号信息
        $x_company=session('x_company');

        if(isset($wtUser)){
            $openId=$wtUser["openid"];
            // 判断该账号是否已注册
            $extSql = "SELECT phone,username FROM company WHERE openid='$openId'";
            $this->trackLog("extSql", $extSql);

            $Model = new Model();
            $result = $Model->query($extSql);
            $this->trackLog("result", $result);

            // 已注册账号 但是未登录
            if(count($result)>0 && !isset($x_company)){
                redirect("/index.php/Company/Index/signIn");
            }

            // 未注册账号 未登录
            if(count($result)==0 && !isset($x_company)){
                redirect("/index.php/Company/Index/signUp");
            }

            // 已注册账号 已登录
            if(count($result)>0 && isset($x_company)){
                redirect("/index.php/Company/User/bizCenter");
            }
        }
        // 以上情况之外时跳转到登录
        redirect("/index.php/Company/Index/signIn");
    }
}

?>