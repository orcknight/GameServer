<?php

/**
 * Auth: James.Yu<zhenzhouyu@jiechengkeji.cn>
 * CreateTime: 05/12/16
 * UpdateTime: 05/12/16
 */

namespace Company\Controller;

use Think\Controller;
use Think\Log;

use Company\Service\SysEnum;

class BaseController extends Controller {


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


    // 鉴权
    protected function oAuth() {

        if(C('ENV') == "TEST") {
            return true;
        }

        if($_SERVER['HTTP_HOST'] != C('WECHAT-HOST')) {
            $nonce = I('nonce', 0, 'intval');
            $signature = I('signature', "", 'strip_tags');
            if(!checkSignature($nonce, $signature)){
                $data['code']  = 0;
                $data['msg']  = "未经授权的访问!";
                $this->ajaxReturn($data);
            }
        }
    }



}