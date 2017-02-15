<?php
/**
 * Created by PhpStorm.
 * User: StandOpen
 * Date: 14-12-1
 * Time: 11:10
 */
namespace Home\Controller;

use Think\Controller;

abstract class WtAuthController extends BaseController {


    public function _initialize() {

        parent::_initialize();
        
        $this->trackLog("excute", "WtAuthController_initialize");

        $wtUser = session("wt_user");
        $this->trackLog("wt_user", $wtUser);

        if (!$wtUser) {
            if (isset($_REQUEST['code'])) {// 用户同意授权
                $this->trackLog("code", $_REQUEST['code']);
                $this->wechatCallBack();
            } else { //用户拒绝后，REDO授权请求。
                $this->trackLog("code", "用户拒绝授权！");
                $this->oAuth();
                exit();
            }
        }
        
    }

    /**
     * 微信oAuth认证回调
     */
    protected function oAuth() {
        $this->trackLog("excute", "oAuth()");

        $protocal = $_SERVER['SERVER_PROTOCOL'];
        if ($protocal['5'] == 's') {
            $protocal = 'https://';
        } else {
            $protocal = 'http://';
        }
        $http_host = $_SERVER['HTTP_HOST'];
        $origin_url = $protocal . $http_host;
        $this->trackLog("origin_url", $origin_url);

        $url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" . C('WT_APP_ID') . "&redirect_uri=" . urlencode($origin_url) . $_SERVER['REQUEST_URI'] . "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
        $this->trackLog("url", $url);
        header("Location:" . $url);
    }

    /**
     * 获取用户的个人信息
     */
    protected function wechatCallBack() {

        $this->trackLog("excute", "wechatCallBack()");

        $code = $_REQUEST['code'];
        $state = $_REQUEST['state'];
        $this->trackLog("code", $code);
        $this->trackLog("state", $state);

        $url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" . C('WT_APP_ID') . "&secret=" . C('WT_APP_SECRET') . "&code=" . $code . "&grant_type=authorization_code";
        $data = file_get_contents($url);
        $this->trackLog("url", $url);
        $this->trackLog("data", $data);

        $baseInfo = json_decode($data);
        $access_token = $baseInfo->access_token;
        $openid = $baseInfo->openid;

        $url = "https://api.weixin.qq.com/sns/userinfo?access_token=" . $access_token . "&openid=" . $openid . "&lang=zh_CN";
        $data = file_get_contents($url);
        $this->trackLog("url", $url);
        $this->trackLog("data", $data);
        $wtUserInfo = json_decode($data, true);

        header('Content-type: text/html;charset=utf-8');

        $this->doSession($wtUserInfo);
    }

    private function doSession($user) {
        session("wt_user", $user, 3600*24);
    }

    /**
     * 持久化信息到wechat_user
     */
    protected function snycUser($user) {

        $this->trackLog("excute", "snycUser()");
        $this->trackLog("user", $user);
        $data = array(
            'wechatOpenid' => $user['openid'],
            'nickname' => $user['nickname'],
            'headimgurl' => $user['headimgurl'],
            'sex' => $user['sex'],
            'country' => $user['country'],
            'province' => $user['province'],
            'city' => $user['city']
        );
    }
}