<?php
namespace Home\Controller;

use Think\Controller;
use Think\Model;

use Org\Wechat\MyWechat;

use Think\Verify;


/**
 * Class IndexController
 * 微信消息接口入口
 * 微信公众平台后台填写的api地址则为该操作的访问地址
 *
 * @package Home\Controller
 */
class IndexController extends BaseController {

    public function wechat(){
        $this->trackLog("excute", "wechat");
        $wechat = new MyWechat(C('WT_APP_TOKEN'), true);
        $wechat->run();
    }

    public function index() {
        $cId = 0;
        $city = session("curr_city");
        if(!empty($city)){
            $cId = $city["id"];
        }
        $this->assign('cId', $cId);
        $this->display();
    }

    public function signIn() {
        $this->display();
    }

    public function signUp() {
        $this->display();
    }

    // 生成图形验证码
    public function getThinkCode(){

        $config =   array(
            'useImgBg'  =>  false,           // 使用背景图片
            'fontSize'  =>  12,              // 验证码字体大小(px)
            'useCurve'  =>  false,            // 是否画混淆曲线
            'useNoise'  =>  true,            // 是否添加杂点
            'imageH'    =>  30,               // 验证码图片高度
            'imageW'    =>  90,               // 验证码图片宽度
            'length'    =>  4,               // 验证码位数
            'fontttf'   =>  '',              // 验证码字体，不设置随机获取
            'bg'        =>  array(243, 251, 254),  // 背景颜色
            'reset'     =>  true,           // 验证成功后是否重置
        );
        $this->trackLog("thinkCode", "1");
        // 使用tp自带的验证码类
        $Verify = new Verify($config);
        $this->trackLog("thinkCode", "1");
        $Verify->entry();
        $this->trackLog("thinkCode", "1");
    }

}
