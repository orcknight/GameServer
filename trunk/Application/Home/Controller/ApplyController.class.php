<?php
// +----------------------------------------------------------------------
// | 基础业务CRUD方法范例
// +----------------------------------------------------------------------
// | Author: James.Yu <zhenzhouyu@jiechengkeji.cn>
// +----------------------------------------------------------------------
namespace Home\Controller;
use Think\Controller;
use Think\Model;
use Think\Log;
use Home\Service\JobEnum;

class ApplyController extends BaseController {

    public function _initialize() {
        
        if($_SERVER["REMOTE_ADDR"] != C('PC-HOST')){
            
            $this->trackLog("host", $_SERVER['HTTP_HOST']);
            
            $validate = validateLogin();//1 为已登录 0 为未登录
            if($validate==0){
                $data['code'] = 0 ;
                $data['msg'] = 'unauthorized';
                $this->ajaxReturn($data);

            }        
        }
    }

    /*
     * 查看user对应的"待录用"、"待完成"、"已完成"列表
     * 用户端状态 1:申请中[待录用] 2:已取消 3:待完成[待完成] 4:已完成[已完成]  
     * 企业端状态 1:待通过[待通过] 2:已通过[待完成] 3:已拒绝 4:已完成[已完成] 5:未完成
     * input: session userId, status
     * output: 分页数据  
     */
    public function filter() {

        $this->trackLog("execute", "filter()");
        
        $userId = session("x_user")['id']? : I('request.userId'); //用户登录后，获取的用户信息。
        $status = strip_tags(I('status'));

        $this->trackLog("userId", $userId);
        $this->trackLog("status", $status);
        
        $currPage = I('currPage', 1, 'intval');
        $pageSize = I('pageSize', 10 ,'intval');
        $this->trackLog("currPage", $currPage);
        $this->trackLog("pageSize", $pageSize);

        $statusSQl = " uStatus = $status";
        if(count(explode("_", $status))) {
            $status = str_replace("_", ",", $status);
            $statusSQL = " uStatus IN ($status) ";
        }
        
        if($status == JobEnum::USTATUS_APPLYING || $status == JobEnum::USTATUS_FINISHING){
            
            $statusSQL .= " AND job.status = 1 ";
        }

        $totalSql = "SELECT count(*) AS total FROM job_apply ja LEFT JOIN job ON job.id = ja.jobId WHERE ja.userId = $userId AND" . $statusSQL;
        $this->trackLog("totalSql", $totalSql);

        $db = new Model();
        $total = $db->query($totalSql);
        $this->trackLog("total", $total);

        $total = $total['0']['total'];
        if($total>0) { 

            $offset = ($currPage-1) * $pageSize; // 记录的游标

            //$jobId所有待通过的投递记录
            $extSql = "SELECT ja.id AS applyId, job.id AS id, job.superType, job.pCateId, job.title, job.paytype, job.createTime, job.income, job.incomeUnit, ja.uStatus,p.name as province ,c.name as city,r.name as region FROM job LEFT JOIN job_apply AS ja ON job.id = ja.jobId 
                 LEFT JOIN gp_province p on job.provinceId =p.id  left join gp_city c on job.cityId=c.id left join gp_region r on job.regionid =r.id
                 WHERE" . $statusSQL . "AND userId = $userId LIMIT $offset , $pageSize";
            $this->trackLog("extSql", $extSql);

            $result = $db->query($extSql);

            //一级类型 & incomeUnit
            for($i = 0; $i<count($result);$i++) {
                $incomeUnit = $result[$i]['incomeunit'];
                $incomeUnit =JobEnum::getPriceUnit($incomeUnit);
                $result[$i]['incomeUnit'] =  $incomeUnit;

                $payType = $result[$i]['paytype'];
                $payType =JobEnum::getPayType($payType);
                $result[$i]['payType'] = $payType;
                
                $cateType = $result[$i]['pcateid'];
                $cateType =getParentCate($cateType);
                $result[$i]['category'] =  $cateType;
            }
            $result = $this->associateAttributes($result);
            $this->trackLog("result", $result);

            $resultData['code']  = 1;
            $resultData['msg']  = "查询成功";

            $data['currPage'] = $currPage;
            $data['pageSize'] = $pageSize;
            $data['total'] = $total;
            
            $data['data'] = $result;
            $resultData['data'] = $data;

            $this->ajaxReturn($resultData);

        }else {
            $data['code'] = 0;
            $data['msg'] = "没有待录用的记录！";
            $this->ajaxReturn($data);
        }
    }
    
    public function pcFilter(){
        
        $this->trackLog("execute", "filter()");

        $userId = session("x_user")['id']; //用户登录后，获取的用户信息。

        
        $status = strip_tags(I('status'));

        $this->trackLog("userId", $userId);
        $this->trackLog("status", $status);
        
        $currPage = I('currPage', 1, 'intval');
        $pageSize = I('pageSize', 10 ,'intval');
        $this->trackLog("currPage", $currPage);
        $this->trackLog("pageSize", $pageSize);

        $statusSQl = " uStatus = $status";
        if(count(explode("_", $status))) {
            $status = str_replace("_", ",", $status);
            $statusSQL = " uStatus IN ($status) ";
        }
        
        if($status == JobEnum::USTATUS_APPLYING || $status == JobEnum::USTATUS_FINISHING){
            
            $statusSQL .= " AND job.status = 1 ";
        }

        $totalSql = "SELECT count(*) AS total FROM job_apply ja LEFT JOIN job ON job.id = ja.jobId WHERE ja.userId = $userId AND" . $statusSQL;
        $this->trackLog("totalSql", $totalSql);

        $db = new Model();
        $total = $db->query($totalSql);
        $this->trackLog("total", $total);

        $total = $total['0']['total'];
        if($total>0) { 

            $offset = ($currPage-1) * $pageSize; // 记录的游标

            //$jobId所有待通过的投递记录
            $extSql = "SELECT ja.id AS applyId, job.id AS id, job.superType, job.pCateId, job.title, job.paytype, job.createTime, job.income, job.incomeUnit, ja.uStatus,p.name as province ,c.name as city,r.name as region FROM job LEFT JOIN job_apply AS ja ON job.id = ja.jobId 
                 LEFT JOIN gp_province p on job.provinceId =p.id  left join gp_city c on job.cityId=c.id left join gp_region r on job.regionid =r.id
                 WHERE" . $statusSQL . "AND userId = $userId LIMIT $offset , $pageSize";
            $this->trackLog("extSql", $extSql);

            $result = $db->query($extSql);

            //一级类型 & incomeUnit
            for($i = 0; $i<count($result);$i++) {
                $incomeUnit = $result[$i]['incomeunit'];
                $incomeUnit =JobEnum::getPriceUnit($incomeUnit);
                $result[$i]['incomeUnit'] =  $incomeUnit;

                $payType = $result[$i]['paytype'];
                $payType =JobEnum::getPayType($payType);
                $result[$i]['payType'] = $payType;
                
                $cateType = $result[$i]['pcateid'];
                $cateType =getParentCate($cateType);
                $result[$i]['category'] =  $cateType;
            }
            $result = $this->associateAttributes($result);
            $this->trackLog("result", $result);

            $resultData['code']  = 1;
            $resultData['msg']  = "查询成功";

            $data['currPage'] = $currPage;
            $data['pageSize'] = $pageSize;
            $data['total'] = $total;
            
            $data['data'] = $result;
            $resultData['data'] = $data;

            $this->ajaxReturn($resultData);

        }else {
            $data['code'] = 0;
            $data['msg'] = "没有待录用的记录！";
            $this->ajaxReturn($data);
        }    
        
    }

    /*
     * 投递记录状态更改
     * 用户端状态 1:申请中[待录用] 2:已取消 3:待完成[待完成] 4:已完成[已完成] 5:未通过 6:未完成
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
        
        $extSql = "UPDATE job_apply SET uStatus = $status WHERE id = $id";
        $this->trackLog("extSql", $extSql);
        
        $db = new Model();
        $uResult = $db->execute($extSql);

        $cResult ='';
        if($status == 2) { // 已通过
            $extSql = "UPDATE job_apply SET cStatus = 5 WHERE id = $id";
            $cResult =$db->execute($extSql);
        }

        if($uResult && $cResult){
            $resultData['code']  = 1;
            $resultData['msg']  = "状态修改成功";
            $this->ajaxReturn($resultData);
        } else {
            $data['code']  = 0;
            $data['msg']  = "状态修改失败";
            $this->ajaxReturn($data);
        }
    }   

    //申请工作的方法，
    public function job() {
        
        $this->trackLog("execute", "applyJob()");

        $jobId = I('id','','trim');//这是拿到传过来的id
        $uId = session("x_user")['id']? : I('request.userId');//session取得用户ID
        $companyId=I('companyId','','trim');
        $this->trackLog("uId", $uId);
        $this->trackLog("jobId", $jobId);
        
        if(!is_numeric($jobId) || !is_numeric($uId) || $uId < 1){
			
            $data['code']  = 0;         
            $data['msg']  = "参数格式不正确！";  
            $this->ajaxReturn($data);        
        }
        
        $db=new Model();

        $querySql = "SELECT * FROM job WHERE id = '$jobId' AND status = 1";
        $this->trackLog('querySql', $querySql);
        $result = M()->query($querySql);
        $this->trackLog('result', $result);

        if (!$result) {
            $data['code'] = 0;
            $data['msg'] = "该职位暂不能申请~";
            $this->ajaxReturn($data);
        }

        $checkSql="SELECT * FROM JOB_APPLY WHERE userId = $uId AND jobid=$jobId AND uStatus IN (1,3)";
        $this->trackLog("checkSql", $checkSql);
        
        $checkResult=$db->query($checkSql);
        $this->trackLog("checkResult", $checkResult);
        if($checkResult){
			
            $data['code']  = 0;
            $data['msg']  = "您已经申请过该职位！";
            $this->ajaxReturn($data); 
        }
        $extSql = "INSERT INTO job_apply (userId, jobId,companyId) VALUES ($uId,$jobId,$companyId)";
        $this->trackLog("extSql", $extSql);
        
        $result = $db->execute($extSql);
        if($result){
            
            $titlePhone=$db->query("select phone,title,company,source from job where id = ".$jobId);
            $this->trackLog("titlePhone", $titlePhone);

            $jobPhone = $titlePhone[0]['phone'];
            $title = $titlePhone[0]['title'];
            $companyName = $titlePhone[0]['company'];
            $jobSource = $titlePhone[0]['source'];

            $userName = $_SESSION["x_user"]['realname'];

            $userPhone =$_SESSION["x_user"]['phone'];

            $this->trackLog("companyName", $companyName);
            
            $this->trackLog("userName", $userName);
            $this->trackLog("userPhone", $userPhone);
            $this->trackLog("title", $title);
            $this->trackLog("jobPhone", $jobPhone);
            $this->trackLog("jobSource", $jobSource);
            
            $resultData['code']  = 1;
            $resultData['msg']  = "申请成功！";
            
            //Todo:后面要给同窗兼职的职位另外配置一个模板，暂时用不发处理
            if($jobSource != "65cc70a4d2ea8ccd3c17a573d7e3021f"){

                sendAplMsgToCop($uId,$companyId,$jobId,$jobPhone, $companyName, $title, $userName, $userPhone);
                $this->trackLog("sendAplMsgToCop", "sendAplMsgToCop()");
            }
            //推送微信消息
            $companyOpenId=$db->query("select openid from company where id = ".$companyId);
            $this->trackLog("companyOpenId", $companyOpenId);
            sendAplWechatMsgToCop($uId,$companyName,$title,$userName, $userPhone,$companyOpenId[0]["openid"]);

            $this->sync_apply($result);
            $this->ajaxReturn($resultData);
        }else{
			
            $data['code']  = 0;
            $data['msg']  = "执行失败！";
            $this->ajaxReturn($data);
        }
    }

    function sync_apply($applyId){
        $data['applyId'] = $applyId;
        request_post(C('MANAGER-HOST')."/sync/job/apply",$data);
    }
    
     function associateAttributes($jobs){
        $this->trackLog("execute", "associateAttributes()");
        $jobIds= array();
        for($i=0;$i<count($jobs);$i++) { 
           $jobIds[]=$jobs[$i]['id'];
        } 
        $querySql = " select * from job_attribute where job_id in (".join(",",$jobIds).")";
        $this->trackLog("querySql", $querySql);
        $attrs= M()->query($querySql);
        if (!$attrs) {
            for($i=0; $i < count($jobs); $i++) {
                $jobs[$i]['attributes']= array();
            }
           
        }else{
            
            for($i=0; $i < count($jobs); $i++) {
                $temp=array();

                for($j=0; $j < count($attrs); $j++){
                   
                        if($attrs[$j]['job_id'] == $jobs[$i]['id']) {
                                array_push($temp, intval($attrs[$j]['attr_id']));
                        }
                }
                $jobs[$i]['attributes']= $temp;
            }
        }
       return $jobs;
    }
}
