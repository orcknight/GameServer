<?php

// +----------------------------------------------------------------------
// |相关业务逻辑
// +----------------------------------------------------------------------
// | Author: James.Yu <zhenzhouyu@jiechengkeji.cn>
// | CreateTime: 2016/05/13
// | UpdateTime: 2016/05/13
// +----------------------------------------------------------------------

namespace Company\Controller;

use Think\Controller;
use Think\Model;
use Think\Log;
use Think\Exception;
use Home\Service\JobEnum;
use Enum\WalletEnum;
use Company\Service\JobPayService;
use Company\Service\WalletService;
use Company\Service\WalletOrderService;

class ApplyController extends BaseController {

    public function _initialize() {
        $this->oAuth();
    }

    /**
     * 查询所有&待通过的投递记录
     * 企业端状态 1:待通过[待通过] 2:已通过[待完成] 3:已拒绝 4:已完成[已完成] 5:未完成
     * 用户端状态 1:申请中[待录用] 2:已取消 3:待完成[待完成] 4:已完成[已完成]	 
     * input: session companyId
     * output: 分页数据		 
     */
    public function all() {

        $this->trackLog("execute", "all()");

        $companyId = I('session.x_company')['id']? : I('request.companyId'); //优先从session中取，取不到就用参数中的
        //$status = I('status', 1, 'intval');
        //$this->trackLog("status", $status);

        $currPage = I('currPage', 1, 'intval');
        $pageSize = I('pageSize', 10, 'intval');


        $this->trackLog("companyId", $companyId);
        $this->trackLog("currPage", $currPage);
        $this->trackLog("pageSize", $pageSize);


        $totalSql = "SELECT count(temp.total) as total FROM (SELECT count(*) AS total FROM job LEFT JOIN job_apply AS ja ON job.id = ja.jobId WHERE ja.companyId = $companyId GROUP BY job.id ) temp";
        $this->trackLog("totalSql", $totalSql);

        $db = new Model();
        $total = $db->query($totalSql);
        $total = $total['0']['total'];
        $this->trackLog("total", $total);

        if ($total > 0) {

            $offset = ($currPage - 1) * $pageSize; //记录的游标
            // $companyId所有工作的投递记录
            $extSql = "SELECT job.id, job.pCateId, job.title,  job.paytype,job.createTime, r.name AS region, 
            p.name AS province ,c.name AS city ,COUNT(*) AS total 
            FROM job 
            LEFT JOIN job_apply AS ja ON job.id = ja.jobId AND ja.uStatus <> 2 AND ja.uStatus <> 5
            LEFT JOIN gp_region r ON job.`regionId`=r.id
            LEFT JOIN user u ON ja.userId = u.id 
            LEFT JOIN gp_province p ON job.`provinceId` =p.`id` LEFT JOIN gp_city c ON job.`cityId` =c.id
            WHERE ja.companyId = $companyId AND u.id > 0 GROUP BY job.id ORDER BY job.createTime  DESC LIMIT $offset, $pageSize";
            $this->trackLog("extSql", $extSql);
            $result = $db->query($extSql);
            $result = $this->associateAttributes($result);
            //一级类型
            for ($i = 0; $i < count($result); $i++) {
                $cateType = $result[$i]['pcateid'];
                $cateType = $this->getParentCate($cateType);
                $result[$i]['category'] = $cateType;
                $result[$i]['payenum'] = $result[$i]['paytype'];
                $result[$i]['paytype'] = JobEnum::getPayType($result[$i]['paytype']);
                
            }

            $this->trackLog("result", $result);

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
            $data['msg'] = "没有工作申请记录！";
            $this->ajaxReturn($data);
        }
    }

    /*
     * 查看$jobId对应的"待通过"、"待完成"、"已完成"列表
     * 企业端状态 1:待通过[待通过] 2:已通过[待完成] 3:已拒绝 4:已完成[已完成] 5:未完成
     * 用户端状态 1:申请中[待录用] 2:已取消 3:待完成[待完成] 4:已完成[已完成]
     * input: jobId, status
     * output: 分页数据	 
     */

    public function filter() {

        $this->trackLog("execute", "filter()");

        $jobId = I('jobId', 0, 'intval');
        $status = I('status', 0, 'intval');

        $this->trackLog("jobId", $jobId);
        $this->trackLog("status", $status);

        $currPage = I('currPage', 1, 'intval');
        $pageSize = I('pageSize', 10, 'intval');
        $this->trackLog("currPage", $currPage);
        $this->trackLog("pageSize", $pageSize);

        $totalSql = "SELECT count(*) AS total FROM job_apply WHERE jobId = $jobId AND cStatus = $status";
        $this->trackLog("totalSql", $totalSql);

        $db = new Model();
        $total = $db->query($totalSql);
        $this->trackLog("total", $total);

        $total = $total['0']['total'];
        if ($total > 0) {

            $offset = ($currPage - 1) * $pageSize; // 记录的游标
            //$jobId所有待通过的投递记录
            $extSql = "SELECT p.name AS province,c.name AS city,r.name AS region,user.headImgUrl, user.realname, user.id as userId, gs.name, user.schoolId, user.phone, ja.id, ja.cStatus, ja.jobId, ja.companyId, ja.createTime FROM USER LEFT JOIN job_apply AS ja ON user.id = ja.userId 
                LEFT JOIN gp_school AS gs ON user.schoolId = gs.id
                LEFT JOIN gp_province p ON user.`provinceId`=p.`id` LEFT JOIN gp_city c ON user.`cityId`=c.`id` LEFT JOIN gp_region r ON user.`regionId`=r.`id`
                WHERE ja.jobId = $jobId AND ja.cStatus = $status ORDER BY ja.createTime limit $offset, $pageSize";
            $this->trackLog("extSql", $extSql);

            $result = $db->query($extSql);
            $this->trackLog("result", $result);
            
            foreach($result as &$item){
                
                $userId = $item['userid'];
                $item['escaperate'] = $this->getEscapeRate($userId, $jobId);
            }

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
            $data['msg'] = "没有记录！";
            $this->ajaxReturn($data);
        }
    }

    /*
     * 投递记录状态更改
     * 企业端状态 1:待通过[待通过] 2:已通过[待完成] 3:已拒绝 4:已完成[已完成] 5:未完成
     * input: applyId, status
     * output: 分页数据	 
     */

    public function setStatus() {

        $this->trackLog("execute", "setStatus()");

        $ids = I('ids', '', 'trim');
        $status = I('status', 0, 'intval');

        $this->trackLog("ids", $ids);
        $this->trackLog("status", $status);
        
        //ids是用逗号分割的id号
        $ids = trim($ids, ','); //先去掉多余的逗号
        if(empty($ids)){
            
            $data['code'] = 0;
            $data['msg'] = "ids不能为空！";
            $this->ajaxReturn($data);    
        }
        
        //开启事务
        $db = new Model();
        $db->startTrans();
        $idArray = explode(',', $ids);
        foreach($idArray as $id){
            
            if(!$this->doByStatus($id, $status, $db))
            {
                //失败回滚
                $db->rollback();
                $data['code'] = 0;
                $data['msg'] = "状态修改失败";
                $this->ajaxReturn($data);    
            }
        }
        
        //成功提交
        $db->commit();
        $resultData['code'] = 1;
        $resultData['msg'] = "状态修改成功";
        $this->ajaxReturn($resultData);
    }

    function getParentCate($pCateId) {
        $db = new Model();

        $sql = "SELECT name FROM job_category where id= '$pCateId'";

        $result = $db->query($sql);

        return $result[0]['name'];
    }

    function associateAttributes($jobs) {
        $this->trackLog("execute", "associateAttributes()");
        
        if(empty($jobs)){
            
            return;
        }
        
        $jobIds = array();
        for ($i = 0; $i < count($jobs); $i++) {
            $jobIds[] = $jobs[$i]['id'];
        }
        $querySql = " select * from job_attribute where job_id in (" . join(",", $jobIds) . ")";
        $this->trackLog("querySql", $querySql);
        $attrs = M()->query($querySql);
        if (!$attrs) {
            for ($i = 0; $i < count($jobs); $i++) {
                $jobs[$i]['attributes'] = array();
            }
        } else {

            for ($i = 0; $i < count($jobs); $i++) {
                $temp = array();

                for ($j = 0; $j < count($attrs); $j++) {

                    if ($attrs[$j]['job_id'] == $jobs[$i]['id']) {
                        array_push($temp, intval($attrs[$j]['attr_id']));
                    }
                }
                $jobs[$i]['attributes'] = $temp;
            }
        }
        return $jobs;
    }
    
    /**
    * 获取逃单比
    * 
    * @param mixed $userId
    * @param mixed $jobId
    */
    private function getEscapeRate($userId, $jobId){

        $escapteCount = 0;
        $totalCount = 0;
        
        //逃单数
        $status = JobEnum::CSTATUS_UNFINISHED;
        $querySql = "SELECT COUNT(*) AS count FROM job_apply WHERE userId = '$userId' AND jobId = '$jobId' AND cStatus = $status";
        $result = M()->query($querySql);
        if($result){
            
            $escapteCount = $result[0]['count'];
        }
        
        //工作过的总数
        $status = JobEnum::CSTATUS_POSTED . ',' . JobEnum::CSTATUS_FINISHED . ',' . JobEnum::CSTATUS_UNFINISHED;
        $querySql = "SELECT COUNT(*) AS count FROM job_apply WHERE userId = '$userId' AND jobId = '$jobId' AND cStatus IN($status)";
        $result = M()->query($querySql);
        if($result){
            
            $totalCount = $result[0]['count'];
        }    
        
        
        return $escapteCount . '/' . $totalCount;
        
    }
    
    private function doByStatus($id, $status, $db){
        
        $extSql = "UPDATE job_apply SET cStatus = $status WHERE id = $id";
        $cResult = $db->execute($extSql);
        $this->trackLog("extSql", $extSql); 

        $uResult = '';
        if ($status == 2) { // 已通过
            $extSql = "UPDATE job_apply SET uStatus = 3 WHERE id = $id";

            $agreeSMS = "SELECT a.userid,a.companyid,a.jobid,u.phone as userPhone ,u.realname ,u.openid,j.title ,j.phone as jobPhone,j.company FROM job_apply  a 
                         LEFT JOIN USER u ON a.userid=u.id 
                         LEFT JOIN job j ON a.jobid = j.id 
                          WHERE a.id=  " . $id;

            $msgResult = $db->query($agreeSMS);
            $msgResult = $msgResult[0];

            sendAgrMsgToUser($msgResult['userid'],$msgResult['companyid'],$msgResult['jobid'],$msgResult['userphone'], $msgResult['company'], $msgResult['title'], $msgResult['realname'], $msgResult['jobphone']);
            $this->trackLog("sendAgrMsgToUser","sendAgrMsgToUser()");
            //向用户推送微信消息 TODO
            $sourceUrl='http://test-wechat.luobojianzhi.com/index.php/Home/Job/applyDetail/'.$msgResult["jobid"];
            sendAgrWechatMsgToUser($msgResult['userid'],$msgResult['title'], $msgResult['realname'],$msgResult['company'],$msgResult['jobphone'],$msgResult['openid'],$sourceUrl);
            $this->trackLog("sendAgrWechatMsgToUser","sendAgrWechatMsgToUser()");
            $uResult = $db->execute($extSql);
        }
        if ($status == 3) { // 已拒绝
            $extSql = "UPDATE job_apply SET uStatus = 5 WHERE id = $id";
            $uResult = $db->execute($extSql);
        }
        if ($status == 4) { // 已完成
            $extSql = "UPDATE job_apply SET uStatus = 4 WHERE id = $id";
            $uResult = $db->execute($extSql);
        }
        if ($status == 5) { // 未完成
            $extSql = "UPDATE job_apply SET uStatus = 6 WHERE id = $id";
            $uResult = $db->execute($extSql);
        }
        $this->trackLog("extSql", $extSql);
        
        if ($cResult && $uResult) {
            
            return true;
        } else {
            
            return false;
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
        $aids = I('aids', ''); //申请记录id
        $uids = I('ids', '');  //
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
                "businessId" => $uidArray[$i],
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

}
