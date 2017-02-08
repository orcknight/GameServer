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
use Home\Service\JobEnum;
use Company\Service\JobPayService;

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
                $querySql = "SELECT COUNT(*) as count FROM job_apply WHERE userId = '$userId' AND jobId = '$jobId' AND cStatus = 5";
                $countRet = $db->query($querySql);
                $item['escapecount'] = $countRet[0]['count'];
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

        $id = I('id', 0, 'intval');
        $status = I('status', 0, 'intval');

        $this->trackLog("id", $id);
        $this->trackLog("status", $status);
        
        //开启事务
        $db = new Model();
        $db->startTrans();

        $extSql = "UPDATE job_apply SET cStatus = $status WHERE id = $id";
        $cResult = $db->execute($extSql);
        $this->trackLog("extSql", $extSql); 

        $uResult = '';
        $addResult = true;
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
            
            $queryJobApply = "SELECT * FROM job_apply WHERE id = '$id'"; 
            $jobApply = $db->query($queryJobApply);

            //插入需要支付工资的纪录
            $jobPayService = new JobPayService($db);
            if($jobPayService->checkDuplicate($id, $jobApply[0]['userid'], $jobApply[0]['jobid'], $jobApply[0]['companyid'])){
            
                $addResult = false;    
            }else{

                $addResult = $jobPayService->Add($id, $jobApply[0]['userid'], $jobApply[0]['jobid'], $jobApply[0]['companyid']);
            }
            
        }
        if ($status == 5) { // 未完成
            $extSql = "UPDATE job_apply SET uStatus = 6 WHERE id = $id";
            $uResult = $db->execute($extSql);
        }
        $this->trackLog("extSql", $extSql);

        if ($cResult && $uResult && $addResult) {
            //成功提交
            $db->commit();
            $resultData['code'] = 1;
            $resultData['msg'] = "状态修改成功";
            $this->ajaxReturn($resultData);
        } else {
            //失败回滚
            $db->rollback();
            $data['code'] = 0;
            $data['msg'] = "状态修改失败";
            $this->ajaxReturn($data);
        }
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

}
