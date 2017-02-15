<?php
/**
 * Created by PhpStorm.
 * User: Administrator
 * Date: 2016/6/28 0028
 * Time: 15:11
 */

namespace Home\Controller;

class WechatController extends WtAuthController {

    public function _initialize() {
        parent::_initialize();
    }
    
    public function signIn() {
        $this->display();
    }

    public function signUp() {
        $this->display();
    }
}