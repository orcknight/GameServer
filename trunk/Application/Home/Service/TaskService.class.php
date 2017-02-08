<?php

namespace Home\Service;

use Think\Model;
use Think\Exception;

use Home\Dao\TaskDao;
use Enum\TaskEnum;

/**
* 线上兼职服务类
*/
class TaskService extends BaseService {
    
    private $db;
    private $taskDao;
    
    public function __construct() {
        
        $this->db = new Model();
        $this->taskDao = new TaskDao($this->db);
    }
    
    public function tasks($args){
        
        $this->trackLog("excute", "tasks()");
        
        $total = $this->taskDao->getQueryCount($args);
        if($total < 1){
            
            $resultData['code']  = 1;
            $resultData['msg']  = "没有数据！";
            return $resultData;
        }
        
        $result = $this->taskDao->query($args);
        $resultData['code']  = 1;
        $resultData['msg']  = "查询成功！";
        $data['total']=$total;
        $data['data'] = $result;
        $resultData['data']=$data;
        return $resultData;
    }
    
    public function applys($args){
        
        $this->trackLog("excute", "applys()");
        
        if($args['userId'] < 1){
            
            $resultData['code']  = 0;
            $resultData['msg']  = "您还没有登录，请登录！";
            return $resultData;
        }
        
        $total = $this->taskDao->getQueryApplyCount($args);
        if($total < 1){
            
            $resultData['code']  = 1;
            $resultData['msg']  = "没有数据！";
            return $resultData;
        }
        
        $result = $this->taskDao->queryApply($args);
        //填写过期时间
        for($i = 0; $i < sizeof($result); $i++){
            
            $expiredTime = strtotime($result[$i]['create_time']) + $result[$i]['finish_time']*60*60;
            if($expiredTime > time()){
                $result[$i]['expired_time'] = date("Y-m-d H:i:s", $expiredTime);    
            }
        }
        
        $resultData['code']  = 1;
        $resultData['msg']  = "查询成功！";
        $data['total']=$total;
        $data['data'] = $result;
        $resultData['data']=$data;
        return $resultData;    
    }
    
    public function getTaskDetail($args){
        
        $this->trackLog("excute", "getTaskDetail()");
        
        //1.参数检查
        if($args['id'] < 1){
            
            $data['code'] = 0;
            $data['msg'] = "参数错误！";
            return $data;
        }
        
        //2.增加热度
        $this->addHotForTask($args['id']);
        
        //3.获取基本信息
        $taskInfo = $this->taskDao->findTaskById($args);
        if(empty($taskInfo)){
            
            $data['code'] = 0;
            $data['msg'] = '获取失败！';
            return $data;
        }
        $taskInfo = $taskInfo[0];
        
        //4.获取是否申请
        $result = $this->getAppliedInfo(array(
        'id' => $args['id'],
        'userId' => $args['userId'],
        'type' => $taskInfo['type'],
        'finishTime' => $taskInfo['finish_time'],
        ));
        $taskInfo['is_applied'] = $result['isApplied'];
        $taskInfo['apply_id'] = $result['applyId'];
        $taskInfo['u_status'] = $result['uStatus'];
        if(isset($result['expiredTime'])){
            
            $taskInfo['expired_time'] = $result['expiredTime'];
        }
        
        $data['code'] = 1;
        $data['msg'] = '获取成功！';
        $data['data'] = $taskInfo;
        return $data;
    }
    
    public function getTaskStepDetail($args){
        
        $this->trackLog("excute", "getTaskStepDetail()");    
        
        //1.参数检查
        if($args['taskId'] < 1 || $args['userId'] < 1 || $args['taskApplyId'] < 1){
            
            $data['code'] = 0;
            $data['msg'] = "参数错误！";
            return $data;
        }
        
        //查询兼职步骤
        $taskStep = $this->taskDao->queryTaskStep($args);
        //查询用户步骤
        $taskStepResult = $this->taskDao->queryTaskStepResult($args);
        
        $data['code'] = 1;
        $data['msg'] = '获取成功！';
        $data['data']['taskStep'] = $taskStep;
        $data['data']['taskStepResult'] = $taskStepResult;
        return $data;
    }
    
    /**
    * 申请线上兼职
    * 
    * @param mixed $args
    */
    public function applyTask($args){
        
        $this->trackLog("excute", "applyTask()");
        
        //1.参数检查
        if($args['id'] < 1 || $args['userId'] < 1){
            
            $data['code'] = 0;
            $data['msg'] = "参数错误！";
            return $data;
        }
        
        //申请验证
        $taskInfo = $this->taskDao->query($args);
        if(empty($taskInfo)){
            
            $data['code'] = 0;
            $data['msg'] = '申请失败！';
            return $data;
        }
        $taskInfo = $taskInfo[0];
        
        //检查申请次数
        if($taskInfo['number'] < 1){
            
            $data['code'] = 0;
            $data['msg'] = '职位已经没有申请名额！';
            return $data;    
        }
        
        //检查过期时间
        if(strtotime($taskInfo['end_time'] < time())){
            $data['code'] = 0;
            $data['msg'] = '该职位已经过期！';
            return $data;     
        }
        
        //检查是否已经申请过了
        $result = $this->getAppliedInfo(array(
        'id' => $args['id'],
        'userId' => $args['userId'],
        'type' => $taskInfo['type'],
        'finishTime' => $taskInfo['finish_time'],
        ));
        if($result['isApplied'] == 1){
            
            $data['code'] = 0;
            $data['msg'] = "请不要重复申请！";
            return $data;
        }
        
        try{
            
            //启动事务
            $this->db->startTrans();
            
            //1.减少次数
            $countResult = $this->decNumForTask($args['id']);
            
            //2.添加申请
            $result = $this->taskDao->addApply(array( 'taskId' => $args['id'], 'userId' => $args['userId'] ));
            if($result < 1 || !$countResult){
                
                $this->db->rollback();
                $data['code'] = 0;
                $data['msg'] = "申请失败！";
                return $data;    
            }
            
            $this->db->commit();
            $data['code'] = 1;
            $data['msg'] = "申请成功！";
            $data['data'] = $result; //apply_id
            return $data; 
            
        }catch(Exception $e){
            
            $this->db->rollback();
            $data['code'] = 0;
            $data['msg'] = "申请失败！";
            return $data;      
        }
    }
    
    public function sumbitTask($args){
        
        $this->trackLog("excute", "sumbitTask()");
        
        $stepResult = $args['stepResult'];
        $taskApplyId = $args['applyId'];
        
        try{
            
             //开启事务
            $this->db->startTrans();
  
            //插入结果
            $loopStatus = true;
            $curStep = 0;
            for($i = 0; $i < sizeof($stepResult); $i++){
                
                $item = $stepResult[$i];
                $taskId = $item['taskId'];
                $addRet = $this->taskDao->addStepResult($item);
                if(!$addRet){
                    $loopStatus = false;
                    break;    
                }
                
                //保存当前步数
                $stepResult = $this->taskDao->queryTaskStep(array('id' => $item['stepId']));
                $currentStep = $stepResult[0]['number'];
                if($currentStep > $curStep){
                    
                    $curStep = $currentStep;
                } 
            }
            
            //判断插入是否成功
            if($loopStatus == false){
    
                $this->db->rollback();
                $data['code'] = 0;
                $data['msg'] = '提交失败！';        
                return $data;    
            }
            
            //更新当天步数
            $this->taskDao->updateApply(array( 'cond_id' => $taskApplyId, 'curr_step' => $curStep ));
            
            //获取当前最大
            $maxStep = $this->taskDao->queryTaskStep(array( 'taskId' => $taskId, 'sort' => 'DESC', 
            'start' => 0, 'length' => 1));
            if(!$maxStep){
                
                $this->db->rollback();
                $data['code'] = 0;
                $data['msg'] = '提交失败！';        
                return $data;     
            } 
            
            //更新申请状态
            $maxStep = $maxStep[0]['number'];
            if($maxStep <= $curStep){
            
                $updateStatus = $this->taskDao->updateApply(array( 'cond_id' => $taskApplyId, 
                'cStatus' => TaskEnum::APPLY_CSTATUS_FINISHED, 'uStatus' => TaskEnum::APPLY_USTATUS_FINISHED ));   
                $data['data']['is_finished'] = 1; 
            }else{
                $data['data']['is_finished'] = 0;
            }

            $this->db->commit();
            $data['code'] = 1;
            $data['msg'] = '提交成功！';   
            return $data;         
        }catch(Exception $e){
            
            $this->db->rollback();
            $data['code'] = 0;
            $data['msg'] = '提交失败！';        
            return $data;    
        }
    }
    
    public function incomeDetail($args){
        
        $this->trackLog("excute", "incomeDetail()");
        
        $result = $this->taskDao->incomeDetail($args);
        $data['code'] = 1;
        $data['msg'] = '获取成功！';
        $data['data'] = $result;
        
        return $data;
    }
    
    public function incomeLogs($args){
    
        $this->trackLog("excute", "incomeLogs()");
        
        $total = $this->taskDao->getIncomeLogsCount($args);
        if($total < 1){
            
            $resultData['code']  = 1;
            $resultData['msg']  = "没有数据！";
            return $resultData;
        }
        
        $result = $this->taskDao->incomeLogs($args);
        $resultData['code']  = 1;
        $resultData['msg']  = "查询成功！";
        $data['total']=$total;
        $data['data'] = $result;
        $resultData['data']=$data;
        return $resultData;
    }
    
    /**
    * 每次访问的时候增加任务的热度
    * 
    * @param mixed $args
    */
    private function addHotForTask($taskId){
        
        $this->trackLog("excute", "tasks()");
        
        $result = $this->taskDao->update(array(
        'cond_id' => $taskId,
        'hot' => 'hot+1',
        ));
        
        return $result;
    }
    
    /**
    * 减少任务的次数
    * 
    * @param mixed $taskId
    */
    
    private function decNumForTask($taskId){
        
        $this->trackLog("excute", "tasks()");
        
        $result = $this->taskDao->update(array(
        'cond_id' => $taskId,
        'number' => 'number-1',
        ));
        
        return $result;    
    }
    
    /**
    * 查看用户是否申请了
    * 
    * @param mixed $args
    */
    private function getAppliedInfo($args){
        
        $taskId = $args['id'];
        $userId = $args['userId'];
        $type = $args['type'];
        $finishTime = $args['finishTime'];
        
        if($type == TaskEnum::TYPE_UNLIMITED){
            
            //不限次数的，只要不在进行中就可以申请
            $result = $this->taskDao->isUnlimitedTaskCanApply( array( 'taskId' => $taskId, 'userId' => $userId));
        }else if($type == TaskEnum::TYPE_ONCE){
            
            //限制次数的，只要查到申请就不让申请了
            $result = $this->taskDao->queryApply( array( 'taskId' => $taskId, 'userId' => $userId ) );
        }else if($type == TaskEnum::TYPE_DAILY){
            
            //每天一次的，查看有没有进行中的，或者今天有没有申请过
            $beginTime = date("Y-m-d 00:00:00", time());
            $endTime = date("Y-m-d 23:59:59", time());
            $result = $this->taskDao->isDailyTaskCanApply( array( 'taskId' => $taskId, 'userId' => $userId, 
            'beginTime' => $beginTime, 'endTime' => $endTime));
        }

        if(!$result){
                
            $data['code'] = 1;
            $data['isApplied'] = 0;    
            return $data;  
        }
        
        $data['code'] = 1;
        $data['isApplied'] = 1;
        $data['applyId'] = $result[0]['apply_id'];
        $data['uStatus'] = $result[0]['u_status'];
        if($result[0]['u_status'] != TaskEnum::APPLY_USTATUS_APPLIED){
            
            return $data;               
        }
        
        $expiredTime = strtotime($result[0]['create_time']) + $finishTime*60*60;
        $this->trackLog('expiredTime', $expiredTime);
        $this->trackLog("time()", time());
        if($expiredTime > time()){
            $data['expiredTime'] = date("Y-m-d H:i:s", $expiredTime);    
        }
        return $data; 
    }
    
}

?>
