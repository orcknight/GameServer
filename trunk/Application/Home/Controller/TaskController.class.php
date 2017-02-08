<?php

namespace Home\Controller;

use Think\Controller;
use Home\Service\TaskService;
use Enum\WalletEnum;
use Enum\TaskEnum;

class TaskController extends BaseController {
    
    private $taskService;
    
    public function _initialize() {
    
        parent::_initialize();
        
        $this->taskService = new TaskService();
    }
    
    /**
    * 线上兼职任务列表
    * 
    */
    public function tasks(){
        
        $this->trackLog("execute", "tasks()");
        
        $currPage = I('currPage', '1', 'trim'); //当前页数
        $pageSize=I('pageSize', '10', 'trim'); //每页数据条数
        $pCateId = I('pCateId', 0, 'trim');
        $cateId = I('cateId', 0, 'trim');
        $id = I('id', 0, 'intval');
        $sort = I("sort", 0, 'intval');
        
        $args = array(
        'start' => ($currPage - 1) * $pageSize,
        'length' => $pageSize,
        'status' => TaskEnum::STATUS_GOING,
        'sort' => $sort,
        );
        if($pCateId > 0){
            
            $args['pCateId'] = $pCateId;
        }
        if($cateId > 0){
            $args['cateId'] = $cateId;    
        }
        if($id > 0){
            $args['id'] = $id;
        }
        
        $this->ajaxReturn($this->taskService->tasks($args));
    }
    
    /**
    * 我的任务申请接口
    * 
    */
    public function applys(){
        
        $this->trackLog("execute", "applys()");
        
        $currPage = I('currPage', '1', 'trim'); //当前页数
        $pageSize= I('pageSize', '10', 'trim'); //每页数据条数
        $uStatus = I('uStatus', '', 'trim');    //状态
        
        $args = array(
        'start' => ($currPage - 1) * $pageSize,
        'length' => $pageSize,
        'userId' => session('x_user')['id'],
        );
        if(!empty($uStatus)){
            
            $args['uStatus'] = $uStatus;   
        }
        $this->trackLog("args", $args);
        
        $this->ajaxReturn($this->taskService->applys($args));
    }
    
    /**
    * 获取线上兼职任务详情
    * 
    */
    public function getTaskDetail(){
        
        $this->trackLog("execute", "getTaskDetail()");
        
        $id = I('id', 0, 'intval');     
        $args = array(
        'id' => $id,
        'userId' => session('x_user')['id'],
        );
        $this->trackLog("args", $args);
        
        $this->ajaxReturn($this->taskService->getTaskDetail($args));
    }
    
    /**
    * 获取线上兼职步骤详情
    * 
    */
    public function getTaskStepDetail(){
        
        $this->trackLog("execute", "getTaskStepDetail()");
        
        $id = I('id', 0, 'intval');   
        $applyId = I('applyId', 0, 'intval'); 
        $args = array(
        'taskId' => $id,
        'taskApplyId' => $applyId,
        'userId' => session('x_user')['id'],
        );
        $this->trackLog("args", $args);
        
        $this->ajaxReturn($this->taskService->getTaskStepDetail($args));
    }
    
    /**
    * 申请线上兼职
    * 
    */
    public function applyTask(){
        
        $this->trackLog("execute", "applyTask()");
        
        $id = I('id', 0, 'intval'); 
        $userId = session('x_user')['id'];
        
        $args = array(
        'id' => $id,
        'userId' => $userId,
        );
        $this->trackLog("args", $args);
        
        $this->ajaxReturn($this->taskService->applyTask($args));
    }
    
    /**
    * 提交线上兼职
    * 
    * @param mixed $args
    */
    public function sumbitTask(){
        
       $this->trackLog("execute", "sumbitTask()");
       
       $setpResult = I('stepResult', array());
       $applyId = I('applyId', 0, 'intval');
       $args = array(
       'stepResult' => $setpResult,
       'applyId' => $applyId,
       );
       
       $this->ajaxReturn($this->taskService->sumbitTask($args));
    }
    
    /**
    * 收入金额统计
    * 
    */
    public function incomeDetail(){
        
        $this->trackLog("execute", "incomeLogs()");
        
        $args = array(
        'userId' => session('x_user')['id'],
        'orderType' => WalletEnum::ORDERTYPE_TASK_GAINS,
        'status' => WalletEnum::STATUS_ORDER_SUCCEEDED,
        'type' => WalletEnum::TYPE_INCREASE,
        );
        $this->trackLog("args", $args);
        
        $this->ajaxReturn($this->taskService->incomeDetail($args));
    }
    
    /**
    * 线上兼职收支明细
    * 
    */
    public function incomeLogs(){
    
        $this->trackLog("execute", "incomeLogs()");
        
        $currPage = I('currPage', '1', 'trim'); //当前页数
        $pageSize= I('pageSize', '10', 'trim'); //每页数据条数
        
        $args = array(
        'start' => ($currPage - 1) * $pageSize,
        'length' => $pageSize,
        'userId' => session('x_user')['id'],
        'orderType' => WalletEnum::ORDERTYPE_TASK_GAINS,
        'status' => WalletEnum::STATUS_ORDER_SUCCEEDED,
        'type' => WalletEnum::TYPE_INCREASE,
        );
        $this->trackLog("args", $args);
        
        $this->ajaxReturn($this->taskService->incomeLogs($args));
    }

}  

?>
