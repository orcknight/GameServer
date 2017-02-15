<?php
namespace Home\Controller;
use Think\Controller;
use Think\Log;
use Think\Model;

/**
 * 微信工具类，提供接入验证、刷新AccessToken、创建菜单等接口。
 * Todo，加入消息响应，后期抽象成JCWechat微信服务类。
 */
class ToolController extends BaseController {

    protected $token = "wechat_luobojianzhi_com";

    // 上海大机
    protected $appId = "wxf1ba80a05dfd53b4";
    protected $appSecret = "443e077bdcf598886dec83f7545ad840";

    // 萝卜助手
    // protected $appId = "wx9b0ac7d9a41b05c2";
    // protected $appSecret = "f0ee412502356aed3c793238ac956b75";

    // 萝卜小助手
    // protected $appid = "wx996b17f54b321bee";
    // protected $appkey = "aa37cd699d7076a53ea51e14f7491659";

    protected $apiAccesstoken = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";

    /**
     * 微信接入配置验证
     * URL   : http://wechat.luobojianzhi.com/index.php/home/tool/valid
     * Token : 与验证配置一致
     * Tips  : "公众号暂时无法提供服务"，可以直接检查接入配置是否正确
     */
    public function valid() {

        $echoStr = $_GET["echostr"];

        //valid signature , option
        if($this->checkSignature()){
            echo $echoStr;
            exit;
        }
    }
    //http://wechat.luobojianzhi.com/index.php/home/Tool/cleanSession
    public function cleanSession() {

        $this->trackLog("excute", "cleanSession()");
        $this->trackLog("ac_pt_user", json_encode(session("ac_pt_user")));

        session("ac_pt_user", null);
        session("wt_user", null);

        $this->trackLog("ac_pt_user", json_encode(session("ac_pt_user")));

        echo "activity seesion is cleaned!";
        exit;
    }

    //http://wechat.luobojianzhi.com/index.php/home/Tool/refreshAccessToken/wt/3
    public function refreshAccessToken() {

        $appInfo = $this->getAppInfo();

        $appId = $appInfo["appid"];
        $appSecret = $appInfo["appsecret"];
        $appAlias = $appInfo["alias"];
        $this->trackLog("appId", $appId);
        $this->trackLog("appSecret", $appSecret);

        $url = $this->apiAccesstoken. "&appid=" . $appId . "&secret=" . $appSecret;
        $this->trackLog("url", $url);

        $json = file_get_contents($url);
        $this->trackLog("json", $json);
        $baseInfo = json_decode($json);
        
        //更新jssdk和wechatPush使用的access_token.json文件，修复之前生成二维码失败的诡异错误
        $accessToken = $baseInfo->access_token;
        $this->trackLog("access_token", $accessToken);
        if ($accessToken && C('WT_APP_ID') == $appId) {
            
            $json_file = json_decode(file_get_contents("./Public/token/access_token.json"));
            $json_file->expire_time = time() + 7000;
            $json_file->access_token = $accessToken;
            $fp = fopen("./Public/token/access_token.json", "w");
            fwrite($fp, json_encode($json_file));
            fclose($fp);
        }
        
        $model = new Model();
        $result = $model->query("SELECT * FROM wt_account WHERE appId='$appId'");
        $this->trackLog("result", json_encode($result));

        if(!empty($result)) {
            $result = $model->execute("UPDATE wt_account SET accessToken = '$accessToken' WHERE appId = '$appId'");
        }

        $data['code']  = 1;
        $data['msg']  = $appAlias . " accessToken refresh successfully!";
        $data['accesstoken']  = $accessToken;
        $this->ajaxReturn($data);
    }


    private function getAppInfo() {
        $wechatAlias = I('wt');
        $this->trackLog("wechatAlias", $wechatAlias);

        if(!is_string($wechatAlias)) {
            $data['wechatAlias'] = $wechatAlias;
            $data['code']  = 1;
            $data['msg']  = "wechatId参数不正确";
            $this->ajaxReturn($data);
        } 

        $model = new Model();

        $data = $model->query("SELECT * FROM wt_account WHERE alias='$wechatAlias'");
        $this->trackLog("data", json_encode($data));

        if(empty($data)) {
            $data['code']  = 1;
            $data['msg']  = "微信账户不存在!";
            $this->ajaxReturn($data);
        }

        return $data[0];
    }
    // http://wechat.luobojianzhi.com/index.php/home/tool/createMenu/wt/3
    public function createMenu() {

        $appInfo = $this->getAppInfo();

        $appId = $appInfo["appid"];
        $appSecret = $appInfo["appsecret"];
        $appAlias = $appInfo["alias"];
        $this->trackLog("appId", $appId);
        $this->trackLog("appSecret", $appSecret);

        //获取配置的菜单
        $model = new Model();

        $result = $model->query("SELECT content FROM wt_menu WHERE appId='$appId'");
        $this->trackLog("$result", json_encode($result));

        $menuContent = "";
        if(!empty($result)) {
            $menuContent = $result[0]["content"];
            $this->trackLog("menuContent", $menuContent);
        }

        $accessToken = "";
        $result = $model->query("SELECT accessToken FROM wt_account WHERE appId='$appId'");
        if(!empty($result)) {
            $accessToken = $result[0]["accesstoken"];
            $this->trackLog("accessToken", $accessToken);
        }

        $post_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=$accessToken";
        $ch = curl_init($post_url);
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($ch, CURLOPT_POSTFIELDS, $menuContent);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER,true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, array(
                'Content-Type: application/json',
                'Content-Length: ' . strlen($menuContent))
        );
        $respose_data = curl_exec($ch);

        $data['code']  = 1;
        $data['msg']  = "$appAlias menu create successfully!";
        $data['resposeData']  = $respose_data;
        $this->ajaxReturn($data);
    }



    private function checkSignature() {
        // you must define TOKEN by yourself
        if (!$this->token) {
            throw new Exception('TOKEN is not defined!');
        }

        $signature = $_GET["signature"];
        $timestamp = $_GET["timestamp"];
        $nonce = $_GET["nonce"];

        $token = $this->token;
        $tmpArr = array($token, $timestamp, $nonce);
        // use SORT_STRING rule
        sort($tmpArr, SORT_STRING);
        $tmpStr = implode( $tmpArr );
        $tmpStr = sha1( $tmpStr );

        if( $tmpStr == $signature ){
            return true;
        }else{
            return false;
        }
    }

    public function responseMsg() {
        //get post data, May be due to the different environments
        $postStr = $GLOBALS["HTTP_RAW_POST_DATA"];

        //extract post data
        if (!empty($postStr)){
            /* libxml_disable_entity_loader is to prevent XML eXternal Entity Injection,
               the best way is to check the validity of xml by yourself */
            libxml_disable_entity_loader(true);
            $postObj = simplexml_load_string($postStr, 'SimpleXMLElement', LIBXML_NOCDATA);
            $fromUsername = $postObj->FromUserName;
            $toUsername = $postObj->ToUserName;
            $keyword = trim($postObj->Content);
            $time = time();
            $textTpl = "<xml>
                            <ToUserName><![CDATA[%s]]></ToUserName>
                            <FromUserName><![CDATA[%s]]></FromUserName>
                            <CreateTime>%s</CreateTime>
                            <MsgType><![CDATA[%s]]></MsgType>
                            <Content><![CDATA[%s]]></Content>
                            <FuncFlag>0</FuncFlag>
                            </xml>";
            if(!empty( $keyword ))
            {
                $msgType = "text";
                $contentStr = "Welcome to wechat world!";
                $resultStr = sprintf($textTpl, $fromUsername, $toUsername, $time, $msgType, $contentStr);
                echo $resultStr;
            }else{
                echo "Input something...";
            }

        }else {
            echo "";
            exit;
        }
    }
}