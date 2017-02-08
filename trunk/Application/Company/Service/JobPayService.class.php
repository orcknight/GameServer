<?php

namespace Company\Service;

use Think\Model;
use Enum\JobPayEnum;
use Home\Service\JobEnum;

class JobPayService extends BaseService{
    
    private $db;
    
    //为了在多表关联操作时，支持事务操作，这里db统一采用注入方式从外部注入
    public function __construct($db){
        
        $this->db = $db;
    }
    
    /**
    * 查重，如果返回true就不要再进行插入操作
    * 
    * @param mixed $userId
    * @param mixed $jobId
    * @param mixed $companyId
    */
    public function checkDuplicate($applyId, $userId, $jobId, $companyId){
        $this->trackLog("execute", "checkDuplicate()");
        
        $sql = "SELECT * FROM job_pay WHERE applyId = '$applyId' AND userId = '$userId' AND jobId = '$jobId' AND companyId = '$companyId'";
        $this->trackLog("sql", $sql);
        $jobPay = $this->db->query($sql);
        $this->trackLog("jobPay", $jobPay);
        if($jobPay){
            
            return true;
        }
        
        return false;    
    }
    
    /**
    * 插入一条纪录到job_pay表
    * 
    * @param mixed $userId
    * @param mixed $jobId
    * @param mixed $companyId
    */
    public function Add($applyId, $userId, $jobId, $companyId){
        
        $this->trackLog("execute", "Add()");
        
        $status = JobPayEnum::STATUS_UNPAID;
        $sql = "INSERT INTO job_pay(applyId, userId, jobId, companyId, status) VALUES ('$applyId', '$userId', '$jobId', '$companyId', '$status')";
        $this->trackLog("sql", $sql);
        $result = $this->db->execute($sql);
        $this->trackLog("result", $result);
        
        if($result){
            
            return true;
        }else{
            
            return false;
        }
    }
    
    public function getId($companyId, $jobId, $userId, $applyId){
        
        $this->trackLog("execute", "getId()");

        if ($applyId) {
            $subSql = " AND applyId = '$applyId'";
        } else {
            $subSql = "";
        }
        
        $sql = "SELECT id FROM job_pay WHERE userId = '$userId' AND jobId = '$jobId' AND companyId = '$companyId'" . $subSql;
        $this->trackLog("sql", $sql);
        $result = $this->db->query($sql);
        $this->trackLog("result", $result);
        
        return $result[0]['id'];
    }
    
    public function getJobsCountByPayStatus($companyId, $status){
        
        $this->trackLog("execute", "getJobsCountByPayStatus()");
        
        $totalSql = "SELECT count(temp.total) as total FROM (SELECT count(*) AS total 
        FROM job LEFT JOIN job_pay AS jp ON job.id = jp.jobId WHERE jp.companyId = $companyId AND jp.status = '$status'  GROUP BY job.id ) temp";
        $this->trackLog("totalSql", $totalSql);
        
        $total = $this->db->query($totalSql);
        $total = $total['0']['total'];
        $this->trackLog("total", $total); 
        
        return $total;   
    }
    
    public function getJobsByPayStatus($currPage, $pageSize, $companyId, $status){
        
        $this->trackLog("execute", "getJobsByPayStatus()");
        
        $offset = ($currPage - 1) * $pageSize; //记录的游标
        //companyId所有工作的投递记录
        $extSql = "SELECT job.id, job.pCateId, job.title,  job.paytype,job.createTime, r.name AS region ,
        p.name AS province ,c.name AS city ,COUNT(*) AS total 
        FROM job LEFT JOIN job_pay AS jp ON job.id = jp.jobId 
        LEFT JOIN gp_region r ON job.`regionId`=r.id
        LEFT JOIN gp_province p ON job.`provinceId` =p.`id` 
        LEFT JOIN gp_city c ON job.`cityId` =c.id
        WHERE jp.companyId = $companyId AND jp.status = '$status' 
        GROUP BY job.id ORDER BY job.createTime  DESC 
        LIMIT $offset, $pageSize";
        $this->trackLog("extSql", $extSql);
        $result = $this->db->query($extSql);
        $result = $this->associateAttributes($result);
        //一级类型
        for ($i = 0; $i < count($result); $i++) {
            $cateType = $result[$i]['pcateid'];
            $cateType = $this->getParentCate($cateType);
            $result[$i]['category'] = $cateType;
            $result[$i]['paytype'] = JobEnum::getPayType($result[$i]['paytype']);
        }

        $this->trackLog("result", $result);
        return $result;          
    }
    
    public function getPayUsersCountByStatus($companyId, $JobId, $status){
        
        $this->trackLog("execute", "getPayUsersCountByStatus()");
        
        $sql = "SELECT count(*) as count FROM user AS u 
        LEFT JOIN job_pay AS jp ON jp.userId = u.id
        WHERE jp.companyId = '$companyId' AND jp.jobId = '$JobId' AND jp.status = '$status'";
        $this->trackLog("sql", $sql);
        $count = $this->db->query($sql);
        $this->trackLog("count", $count);
        
        return $count[0]['count'];    
    }
    
    public function getPayUsersByStatus($currPage, $pageSize, $companyId, $JobId, $status){
       
        $this->trackLog("execute", "getPayUsersByStatus()");
         
        $offset = ($currPage - 1) * $pageSize; 
        $sql = "SELECT jp.`applyId` as applyId, u.id as userid, realname as username, u.phone, gs.`name` as schoolname, u.headImgUrl 
        FROM `user` u 
        LEFT JOIN job_pay  jp ON jp.userId = u.id
        LEFT JOIN gp_school gs ON u.schoolId = gs.id
        WHERE jp.companyId = '$companyId' AND jp.jobId = '$JobId' AND jp.status = '$status'
        ORDER BY u.id DESC
        LIMIT $offset, $pageSize";
        $this->trackLog("sql", $sql);
        $users = $this->db->query($sql);
        $this->trackLog("users", $users);
        
        return $users;    
    }
    
    public function setStatus($companyId, $JobId, $userId, $applyId, $status){
        
        $this->trackLog("execute", "setStatus()");

        if ($applyId) {
            $subSql = " AND applyId = '$applyId'";
        } else {
            $subSql = " AND applyId IS NULL";
        }
        
        $sql = "UPDATE job_pay SET status = '$status' WHERE companyId = '$companyId' AND jobId= '$JobId' AND userId = '$userId'" . $subSql;
        $this->trackLog("sql", $sql);
        $result = $this->db->execute($sql);
        $this->trackLog("result", $result);
        
        return $result;
    }
    
    function associateAttributes($jobs) {
        
        $this->trackLog("execute", "associateAttributes()");
        
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
    
    function getParentCate($pCateId) {
        
        $this->trackLog("execute", "getParentCate()");
        
        $sql = "SELECT name FROM job_category where id= '$pCateId'";

        $result = $this->db->query($sql);

        return $result[0]['name'];
    }

}

?>
