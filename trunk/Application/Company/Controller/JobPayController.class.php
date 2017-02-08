<?php

namespace Company\Controller;

use Think\Controller;
use Think\Log;
use Think\Model;
use Think\Exception;
use Home\Service\JobEnum;
use Enum\WalletEnum;
use Enum\JobPayEnum;
use Company\Service\JobPayService;
use Company\Service\WalletService;
use Company\Service\WalletOrderService;


class JobPayController extends BaseController {


    public function _initialize() {
        
        $this->trackLog("execute", "JobPayController()");
        
        //执行函数之前每次都进行登录验证
        $this->checkIsLogin();
    } 
        
    /**
    * 获取已经支付过的职位
    * 
    */
     public function getPaidJobs() {

        $this->trackLog("execute", "getPaidJobs()");

        $companyId = I('session.x_company')['id']? : I('request.companyId'); //优先从session中取，取不到就用参数中
        $currPage = I('currPage', 1, 'intval');
        $pageSize = I('pageSize', 10, 'intval');
        $status = JobPayEnum::STATUS_PAID;

        $this->trackLog("companyId", $companyId);
        $this->trackLog("currPage", $currPage);
        $this->trackLog("pageSize", $pageSize);

        $db = new Model();
        $jobPayService = new JobPayService($db);
        $total = $jobPayService->getJobsCountByPayStatus($companyId, $status);

        if ($total > 0) {

            $result = $jobPayService->getJobsByPayStatus($currPage, $pageSize, $companyId, $status);
            $resultData['code'] = 1;
            $resultData['msg'] = "查询成功";

            $data['currPage'] = $currPage;
            $data['pageSize'] = $pageSize;
            $data['total'] = $total;
            $data['data'] = $result;

            $resultData['data'] = $data;

            $this->ajaxReturn($resultData);
        } else {
            $data['code'] = 0;
            $data['msg'] = "没有已发工资记录！";
            $this->ajaxReturn($data);
        }
    }
    
    /**
    * 获取没有支付过的职位
    * 
    */
    public function getUnpaidJobs(){
    
        $this->trackLog("execute", "getPaidJobs()");

        $companyId = I('session.x_company')['id']? : I('request.companyId'); //优先从session中取，取不到就用参数中的
        $currPage = I('currPage', 1, 'intval');
        $pageSize = I('pageSize', 10, 'intval');
        $status = JobPayEnum::STATUS_UNPAID;

        $this->trackLog("companyId", $companyId);
        $this->trackLog("currPage", $currPage);
        $this->trackLog("pageSize", $pageSize);

        $db = new Model();
        $jobPayService = new JobPayService($db); 
        $total = $jobPayService->getJobsCountByPayStatus($companyId, $status);

        if ($total > 0) {

            $result = $jobPayService->getJobsByPayStatus($currPage, $pageSize, $companyId, $status);
            $resultData['code'] = 1;
            $resultData['msg'] = "查询成功";

            $data['currPage'] = $currPage;
            $data['pageSize'] = $pageSize;
            $data['total'] = $total;
            $data['data'] = $result;

            $resultData['data'] = $data;

            $this->ajaxReturn($resultData);
        } else {
            $data['code'] = 0;
            $data['msg'] = "没有待发工资记录！";
            $this->ajaxReturn($data);
        }    
    }
    
    /**
    * 获取需要支付的用户
    * 
    */
    public function getUnpaidUsers(){
        
        $this->trackLog("execute", "getUnpaidUsers()");
        
        $companyId = I('session.x_company')['id']? : I('request.companyId'); //优先从session中取，取不到就用参数中的
        $jobId = I('jobId', 0, 'intval');
        $status = JobPayEnum::STATUS_UNPAID;
        
        $this->trackLog("companyId", $companyId);
        $this->trackLog("jobId", $jobId);
        
        $db = new Model();
        $jobPayService = new JobPayService($db); 
        $total = $jobPayService->getPayUsersCountByStatus($companyId, $jobId, $status);  
        
        if ($total > 0) {
            
            //待发工资页不分页
            $users = $jobPayService->getPayUsersByStatus(1, $total, $companyId, $jobId, $status);
            $resultData['code'] = 1;
            $resultData['msg'] = "查询成功";

            $data['total'] = $total;
            $data['data'] = $users;
            $resultData['data'] = $data;
            $this->ajaxReturn($resultData);
        } else {
            
            $data['code'] = 0;
            $data['msg'] = "没有需要支付工资的人员！";
            $this->ajaxReturn($data);
        } 
        
    }
   
    /**
    * 获取已经支付过的用户
    *  
    */
    public function getPaidUsers(){
        
        $this->trackLog("execute", "getPaidUsers()");
        
        $companyId = I('session.x_company')['id']? : I('request.companyId'); //优先从session中取，取不到就用参数中的
        $currPage = I('currPage', 1, 'intval');
        $pageSize = I('pageSize', 10, 'intval');
        $jobId = I('jobId', 0, 'intval');
        $status = JobPayEnum::STATUS_PAID;
        
        $this->trackLog("companyId", $companyId);
        $this->trackLog("currPage", $currPage);
        $this->trackLog("pageSize", $pageSize);
        $this->trackLog("jobId", $jobId);
        
        $db = new Model();
        $jobPayService = new JobPayService($db); 
        $total = $jobPayService->getPayUsersCountByStatus($companyId, $jobId, $status);
        if ($total > 0) {

            $users = $jobPayService->getPayUsersByStatus($currPage, $pageSize, $companyId, $jobId, $status);
            $resultData['code'] = 1;
            $resultData['msg'] = "查询成功";

            $data['currPage'] = $currPage;
            $data['pageSize'] = $pageSize;
            $data['total'] = $total;
            $data['data'] = $users;
            $resultData['data'] = $data;

            $this->ajaxReturn($resultData);
        } else {
            
            $data['code'] = 0;
            $data['msg'] = "没有已支付工资的人员！";
            $this->ajaxReturn($data);
        }     
    }
    
    /**
    * 发工资给用户
    * 
    */
    public function payoffToUsers(){
        
        $this->trackLog("execute", "payoffToUsers()");
        
        //首先更改用户状态，生成交易记录，然后向用户钱包加钱，最后从商家钱包减钱
        $companyId = I('session.x_company')['id']? : I('request.companyId'); //优先从session中取，取不到就用参数中的
        $aids = I('aids', '');
        $uids = I('ids', '');
        $jobId = I('jobId', 0, 'intval');
        $password = I('password');
        $wage = I('wage', 0);
        
        $this->trackLog("companyId", $companyId);
        $this->trackLog("uids", $uids);
        $this->trackLog("aids", $aids);
        $this->trackLog("jobId", $jobId);
        $this->trackLog("password", $password);
        $this->trackLog("wage", $wage);
        
        if(empty($wage) || empty($uids) || empty($password)){
            
            $data['code'] =  0;
            $data['msg'] = "所有项目不能为空！";
            $this->trackLog("msg", $data['msg']);
            $this->ajaxReturn($data);
        }
        
        //初始化用到的服务类和数据库模型
        $db = new Model();
        $walletService = new WalletService($db);
        $jobPayService = new JobPayService($db);
        $walletOrderService = new WalletOrderService($db);
        
        //检查支付密码
        $md5Password = md5($password);
        if(!$walletService->checkPassword($companyId, $md5Password, true)){
            
            $data['code'] =  0;
            $data['msg'] = "支付密码错误！";
            $this->trackLog("msg", $data['msg']);
            $this->ajaxReturn($data);    
        }
        
        if(!is_numeric($wage)){
            
            $data['code'] =  0;
            $data['msg'] = "金额格式不对！";
            $this->trackLog("msg", $data['msg']);
            $this->ajaxReturn($data);  
        }
        
        //计算总金额
        $wage *= 100; //把钱转化成分
        $uidArray = explode(',', $uids);
        if ($aids) {
            $aidArray = explode(',', $aids);
        }
        $totalAmount = $wage * count($uidArray);
        
        //检查账户余额
        if(!$walletService->checkBalance($companyId, $totalAmount, true)){
            
            $data['code'] =  0;
            $data['msg'] = "余额不足，请充值！";
            $this->trackLog("msg", $data['msg']);
            $this->ajaxReturn($data);    
        }
        
        $fromWalletId = $walletService->getWalletIdByUserId($companyId, true);
        if($fromWalletId < 1){
        
            $data['code'] =  0;
            $data['msg'] = "获取钱包信息出错，请稍后重试！";
            $this->trackLog("msg", $data['msg']);
            $this->ajaxReturn($data);    
        }
        
        try{
            
            //开启事务
            $db->startTrans();
            
            $loopResult = true;
            for($i = 0; $i < count($uidArray); $i++){
                
                //更新状态
                $statusResult = $jobPayService->setStatus($companyId, $jobId, $uidArray[$i], $aidArray[$i], JobPayEnum::STATUS_PAID);
                if(!$statusResult){
                    $this->trackLog('BREAK HERE!', 'jobPayService->setStatus');
                    $loopResult = false;
                    break;     
                }
                
                //获取关联的job_pay表的id
                $jobPayId = $jobPayService->getId($companyId, $jobId, $uidArray[$i], $aidArray[$i]);
                if($jobPayId < 1){
                    $this->trackLog('BREAK HERE!', 'jobPayService->getId');
                    $loopResult = false;
                    break;    
                }
             
                //获取用户钱包ID
                $toWalletId = $walletService->getWalletIdByUserId($uidArray[$i]);
                if($toWalletId < 1){

                    $this->trackLog('BREAK HERE!', 'walletService->getWalletIdByUserId');
                    $loopResult = false;
                    break;    
                }
                
                //生成交易记录
                $walletOrderArray = array(
                "fromWalletId" => $fromWalletId,
                "toWalletId" => $toWalletId,
                "money" => $wage, 
                "type" => WalletEnum::TYPE_INCREASE,
                "orderType" => WalletEnum::ORDERTYPE_USER_WAGE,
                "businessId" => $jobPayId,
                "reitterName" => (I('session.x_company')['companyname']? : ''), 
                "status" => WalletEnum::STATUS_ORDER_SUCCEEDED
                );
                $orderResultInc = $walletOrderService->Add($walletOrderArray);
                if(!$orderResultInc){
                    $this->trackLog('BREAK HERE!', 'walletOrderService->Add');
                    $loopResult = false;
                    break;     
                }
                
                $walletOrderArray['type'] = WalletEnum::TYPE_DECREASE;
                $walletOrderArray['orderType'] = WalletEnum::ORDERTYPE_COMPANY_PAYOFF;
                $orderResultDec = $walletOrderService->Add($walletOrderArray);
                if(!$orderResultDec){
                    $this->trackLog('BREAK HERE!', 'walletOrderService->Add2');
                    $loopResult = false;
                    break;     
                }
                
                //给用户加钱
                $walletResult = $walletService->addMoney($toWalletId, $wage);
                
                if(!$walletResult){
                    $this->trackLog('BREAK HERE!', 'walletService->addMoney');
                    $loopResult = false;
                    break;     
                }
            }
            
            //给企业端扣钱
            $companyMoneyResult = $walletService->addMoney($fromWalletId, -$totalAmount);
            
            if($companyMoneyResult && $loopResult){
                
                $db->commit();
                $data['code'] =  1;
                $data['msg'] = "工资发放成功！";
                $this->trackLog("msg", $data['msg']);
                $this->ajaxReturn($data);       
            }else{
                
                $db->rollback();
                $data['code'] =  0;
                $data['msg'] = "工资发放失败，请稍后再试！";
                $this->trackLog("msg", $data['msg']);
                $this->ajaxReturn($data);    
            }
            
        }catch(Exception $e){
            
            $db->rollback();
            $data['code'] =  0;
            $data['msg'] = "工资发放失败，请稍后再试！";
            $this->trackLog("msg", $data['msg']);
            $this->ajaxReturn($data);    
        }
    }
    
    
    public function checkIsLogin(){
        
        $this->trackLog("execute", "checkIsLogin()");
        
        if(I('session.x_company')){
            
            return;
        }
            
        //用户没有登录
        $data['code'] =  0;
        $data['msg']="您没有登录，请登录后再操作！" ;
        $this->ajaxReturn($data);
    }

}

?>
