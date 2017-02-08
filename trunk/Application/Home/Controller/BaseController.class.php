<?php

/**
 * User: James.Yu<zhenzhouyu@jiechengkeji.cn>
 * CreateTime: 03/23/16
 * UpdateTime: 05/10/16
 */

namespace Home\Controller;

use Think\Controller;
use Think\Log;

use Home\Service\SysEnum;

class BaseController extends Controller {

    CONST SYSTEM_SECRET = "LUOBOJIANZHI";

    public function _initialize() {
        $this->trackLog("initializing", "BaseController");
        $this->bilog_v2();
        $this->trackLog("initialized", "BaseController");
    }
    
    // 跟踪日志
    protected function trackLog($title, $content, $source = 0, $type = "DEBUG") {

        if(!is_string($content)) {
            $content = json_encode($content);
        }

        $_prefix = "[ Jiechengkeji ]";
        $_date = "[ " . date("H:i:s", time()) . " ]";
        $title = "[ " . $title . " ]";

        if($source == SysEnum::TrackSource) {

            $source = I('source', 0, 'intval');
            Log::record($_prefix ." ". $_date ." ". $title . "source::" . $source . "::" . __ACTION__ , $type);

        } else {
            Log::record($_prefix ." ". $_date ." ". $title . $content, $type);
        }
    }

    // 行为日志
    private function bilog_v2() {

        if(__ACTION__==="/index.php/Home/Tools/biLog")
            return;

        $_DATA = array();
        if (IS_POST){
            
            parse_str(file_get_contents('php://input'), $_DATA);    
        }else if(IS_GET){

            $queryString = urldecode($_SERVER['QUERY_STRING']);
            $_DATA = array_merge($_DATA, $this->convertGetParam($queryString));
        }

        $user = session('x_user');
        if(!is_null($user))
            $_DATA['userId'] = $user["id"];
        $wt_user = session('wt_user');
        if(!is_null($wt_user))
            $_DATA['openid'] = $wt_user["openid"];

        $city = session("curr_city");
        if(!is_null($city)){
            $_DATA['cityId'] = $city["id"];
        }

        $data['data'] = json_encode($_DATA);

        $data['source'] = $_SERVER['SERVER_NAME'];
        $data['action'] = __ACTION__;

        $this->trackLog("data", $data);

        $fieldSql = "";
        $valueSql = "";
        foreach ($data as $key => $value) {
            $fieldSql = $fieldSql . ", $key";
            $valueSql = $valueSql . ", '$value'";
        }

        $fieldSql = substr($fieldSql, (strpos($fieldSql, ",")+1));
        $valueSql = substr($valueSql, (strpos($valueSql, ",")+1));

        $biSql = "INSERT INTO bi_log_v2($fieldSql) VALUES ($valueSql)";
        $this->trackLog("biSql", $biSql);
        M()->execute($biSql);
    }


    // 鉴权
    protected function xAuth() {

        if($_SERVER['HTTP_HOST'] == "wechat.luobojianzhi.com") 
            return ture;

        $token = I('token', '', 'strip_tags');
        $this->trackLog("token", $token);

        $auth = explode(":", $token);
        $key = $auth[0];
        $secret = $auth[1];

        $this->trackLog("key", $key);
        $this->trackLog("secret", $secret);

        $xToken = md5($key . SELF::SYSTEM_SECRET);
        $this->trackLog("xToken", $xToken);

        if(substr($xToken, 6, 6) != $secret) {
            $data['code']  = 0;
            $data['msg']  = "非授权访问！";
            $this->ajaxReturn($data);
        } else {
            $this->trackLog("access successfully", $token);
        }
    }
    
    private function convertGetParam($queryString){
        
        $retArray = [];
        $strArray = explode('&', $queryString);
        foreach($strArray as $item){
            
            $valueArray = explode('=', $item);
            $retArray[$valueArray[0]] = $valueArray[1];   
        }
        
        return $retArray;
    }
}