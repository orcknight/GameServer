<?php
// +----------------------------------------------------------------------
// | 基础业务CRUD方法范例
// +----------------------------------------------------------------------
// | Author: James.Yu <zhenzhouyu@jiechengkeji.cn>
// +----------------------------------------------------------------------

namespace Home\Controller;
use Home\Service\FormService;
class FormController extends BaseController {

    private static $formService;

    public function _initialize() {
        parent::_initialize();
        self::$formService = new FormService();
    }

    /**
     * 根据id获取组件信息
     */
    public function getFormById(){
        $id = I('id', 0, 'intval');
        $this->trackLog("id", $id);
        $args['id'] = $id;

        $result = self::$formService->getFormById($args);
        $this->ajaxReturn($result);
    }

    /**
     * 添加表单
     */
    public function addFormResult() {
        $formId = I("formid",0, 'intval'); //表单id
        $userId = session("x_company")['id']; //用户id；可为空
        $openId = session("wt_user")['openid']; //微信openid
        $content = I('content', "","strip_tags"); //内容 json格式
        $content=decodeUnicode(json_encode($content));
        $this->trackLog("formId", $formId);
        $this->trackLog("userId", $userId);
        $this->trackLog("openId", $openId);
        $this->trackLog("content", $content);

        $args['formId'] = $formId;
        $args['userId'] = $userId;
        $args['openId'] = $openId;
        $args['content'] = trim($content);

        $result = self::$formService->addFormResult($args);

        $this->ajaxReturn($result);
    }
}
