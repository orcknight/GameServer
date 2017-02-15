<?php
namespace Company\Controller;

use Think\Controller;
use Think\Model;


/**
 * Class HtmlController
 * 前端测试页面入口
 *
 * @package Home\Controller
 */
class HtmlController extends BaseController {
    

    public function _initialize(){
    }

    public function index(){

        $this->display();

    }

}
