<?php

namespace Home\Service;

use Think\Model;
use Think\Exception;

class JobService extends BaseService {
    
    private $db;
    
    public function __construct() {
        
        $this->db = new Model();
    }
    
    public function getIndexPageJobs($args){
        
        $this->trackLog("execute", "getJobsByHot()");
  
        $start = $args['start'];
        $length =$args['length'];
        $cId = $args['cId'];
        $statuses = $args['statuses'];
        $whereStr = $this->getWhereStrByParam($args);
        
        $total = $this->getCountByParam($cId, $statuses, $whereStr);
        if($total < 1) {
            
            $resultData['code']  = 0;
            $resultData['msg']  = "没有数据";
            return $resultData;
        }
        
        $status = JobEnum::USTATUS_APPLYING . ',' . JobEnum::USTATUS_UNPOSTED;
        $execSql = "SELECT ca.name AS category, j.superType, j.hot, j.pCateId,j.id, j.phone,j.weight, j.status, j.type, j.income, j.title,j.payType,j.incomeUnit, j.source, j.intro, j.createTime, j.updateTime, j.beginTime, j.endTime,j.beginHour,j.endHour, p.name AS province, c.name AS city, r.name AS region, co.companyName AS company,
        (SELECT COUNT(*) FROM job_apply ja1 WHERE jobId = j.id AND ja1.uStatus IN($status)) AS count 
        FROM job j 
        LEFT JOIN gp_province p ON j.provinceId = p.id 
        LEFT JOIN gp_city c ON j.cityId=c.id 
        LEFT JOIN gp_region r ON j.regionId = r.id 
        LEFT JOIN company co ON j.companyId = co.id 
        LEFT JOIN job_category ca ON j.pCateId = ca.id
        LEFT JOIN job_attribute ja ON j.id = ja.job_id
        LEFT JOIN job_pos jp ON j.id = jp.id
        WHERE j.cityId = $cId AND j.superType = 0 AND j.status = '$statuses' " . $whereStr .
        " ORDER BY weight DESC, if(ja.attr_id =1 ,0,1), if(j.payType =1 and ja.attr_id = 1,0,1), if(count < j.number,0,1), createtime DESC
        LIMIT $start, $length";
        $this->trackLog("execSql", $execSql);
        $result=M()->query($execSql);
        $this->trackLog("result", $result);
        $result = $this->associateAttributes($result);
        
        //把查出来的 incomeUnit/payType 的数字类型改为文字
        for($i = 0; $i<count($result);$i++) {
            $incomeUnit = $result[$i]['incomeunit'];
            $incomeUnit =JobEnum::getPriceUnit($incomeUnit);
            $result[$i]['incomeunit'] =  $incomeUnit;
            
            $result[$i]['payenum'] = $result[$i]['paytype'];
            $payType = $result[$i]['paytype'];
            $payType =JobEnum::getPayType($payType);
            $result[$i]['paytype'] =  $payType;

            $result[$i]['begintime'] = explode(" ", $result[$i]['begintime'])[0];
            $result[$i]['endtime'] = explode(" ", $result[$i]['endtime'])[0];
            $result[$i]['createtime'] = explode(" ", $result[$i]['createtime'])[0];
        }

        $resultData['code']  = 1;
        $resultData['msg']  = "查询成功";
        $data['currPage']=floor($start/$length) + 1;
        $data['pageSize']=$length;
        $data['total']=$total;
        $data['data'] = $result;
        $resultData['data']=$data;
        
        return $resultData;
    }
    
    /**
    * 处理名企招聘
    * 
    * @param mixed $args
    */
    public function getFullTimeJobs($args){
        
        $this->trackLog("execute", "getJobsByHot()");
  
        $start = $args['start'];
        $length =$args['length'];
        $cId = $args['cId'];
        $statuses = $args['statuses'];
        $whereStr = $this->getWhereStrByParam($args);
        
        $total = $this->getCountByParam($cId, $statuses, $whereStr);
        if($total < 1) {
            
            $resultData['code']  = 0;
            $resultData['msg']  = "没有数据";
            return $resultData;
        }
        
        $execSql = "SELECT ca.name AS category, j.superType, j.hot, j.pCateId,j.id, j.phone,j.weight, j.status, j.type, j.income, j.title,j.payType, j.incomeUnit, j.source, j.intro, j.createTime, j.updateTime, j.beginTime, j.endTime,j.beginHour,j.endHour, p.name AS province, c.name AS city, r.name AS region, co.companyName AS company 
        FROM job j 
        LEFT JOIN gp_province p ON j.provinceId = p.id 
        LEFT JOIN gp_city c ON j.cityId=c.id 
        LEFT JOIN gp_region r ON j.regionId = r.id 
        LEFT JOIN company co ON j.companyId = co.id 
        LEFT JOIN job_category ca ON j.pCateId = ca.id
        LEFT JOIN job_attribute ja ON j.id = ja.job_id
        LEFT JOIN job_pos jp ON j.id = jp.id
        WHERE j.cityId = $cId AND j.superType = 1 AND j.status = '$statuses' " . $whereStr .
        "ORDER BY j.hot DESC, j.createTime DESC
        LIMIT $start, $length";
        $this->trackLog("execSql", $execSql);
        $result=M()->query($execSql);
        $this->trackLog("result", $result);
        $result = $this->associateAttributes($result);
        
        //把查出来的 incomeUnit/payType 的数字类型改为文字
        for($i = 0; $i<count($result);$i++) {
            $incomeUnit = $result[$i]['incomeunit'];
            $incomeUnit =JobEnum::getPriceUnit($incomeUnit);
            $result[$i]['incomeunit'] =  $incomeUnit;
            
            $result[$i]['payenum'] = $result[$i]['paytype'];
            $payType = $result[$i]['paytype'];
            $payType =JobEnum::getPayType($payType);
            $result[$i]['paytype'] =  $payType;

            $result[$i]['begintime'] = explode(" ", $result[$i]['begintime'])[0];
            $result[$i]['endtime'] = explode(" ", $result[$i]['endtime'])[0];
            $result[$i]['createtime'] = explode(" ", $result[$i]['createtime'])[0];
        }

        $resultData['code']  = 1;
        $resultData['msg']  = "查询成功";
        $data['currPage']=floor($start/$length) + 1;
        $data['pageSize']=$length;
        $data['total']=$total;
        $data['data'] = $result;
        $resultData['data']=$data;
        
        return $resultData;
    }
    
    /**
    * 通过条件获取数量
    * 
    * @param mixed $cId
    * @param mixed $statuses
    * @param mixed $whereStr
    * @return mixed
    */
    private function getCountByParam($cId, $statuses, $whereStr){
        
        $execSqlCount="SELECT count(*) AS count FROM `job` AS j 
        LEFT JOIN job_attribute ja ON j.id = ja.job_id
        LEFT JOIN job_pos jp ON j.id = jp.id
        WHERE cityId = $cId AND j.superType = 1 AND j.status = '$statuses' " . $whereStr;
        $resultTotal=M()->query($execSqlCount);
        if(!$resultTotal){
            
            return 0;
        }
        
        $total=$resultTotal[0]['count'];
        $this->trackLog("total", $total);
        
        return $total;
        
    }
    
    
    
    /**
    * 根据变量设置情况获取where语句的查询sql
    * 
    * @param mixed $args
    */
    private function getWhereStrByParam($args){
        
        $this->trackLog("execute", "getWhereStrByParam()");
        
        //获取传入的变量
        $timeId = $args['timeId'];
        $pCateId = $args['pCateId'];
        $cateId = $args['cateId'];
        $regionId = $args['regionId'];
        $keyword = $args['keyword'];
        
        //拼接查询字符串
        $whereStr = "";
        if($timeId > 0) {
            $startTime = $this->getTimeById($timeId);
            $endTime = date("Y-m-d 23:59:59");
            $whereStr .= " AND j.createtime BETWEEN '".$startTime."' AND '".$endTime."' ";    
        }
        if($pCateId > 0) {
            $whereStr .= ' AND j.pCateId=' . $pCateId . ' ';
        }
        if($cateId > 0){
            $whereStr .= ' AND j.cateId = ' . $cateId . ' ';
        }
        if($regionId > 0){
            $whereStr .= ' AND j.regionId='.$regionId . ' ';
        }
        if(!empty($keyword)){
            $whereStr .= ' AND j.title like "%'.$keyword.'%" ' ;
        }
        $this->trackLog("whereStr", $whereStr);
        
        return $whereStr;
    }
    
    
    private function getTimeById($timeId){
        
        switch ($timeId) {
            case 1:
            $startTime=date('Y-m-d 00:00:00');//当天
            break;
            case 2:
            $startTime=date("Y-m-d 00:00:00",strtotime("-3 day"));//三天内
            break;
            case 3:
            $startTime=date("Y-m-d 23:59:59:",strtotime("last Sunday"));//本周
            break;
            case 4:
            $startTime=date("Y-m-01 00:00:00");//当月
            break;
            default:
            $startTime=date("Y-m-d 00:00:00");
            break;
        }
        
        return $startTime;
    }
    
    
    private function associateAttributes($jobs){
        
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

?>
