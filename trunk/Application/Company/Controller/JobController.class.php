<?php

// +----------------------------------------------------------------------
// | 基础业务CRUD方法范例
// +----------------------------------------------------------------------
// | Author: James.Yu <zhenzhouyu@jiechengkeji.cn>
// +----------------------------------------------------------------------

namespace Company\Controller;

use Think\Controller;
use Think\Model;
use Think\Log;
use Home\Service\JobEnum;
use Home\Service\SysEnum;
use Company\Service\RoleEnum;

class JobController extends BaseController {


    /**
     * 获取工作类型
     * ToRe 代码逻辑 & SQL
     */
    public function getTypes() {

        $this->trackLog("execute", "getTypes");

        $roleId = I('roleid', false, 'trim');
        $type = I('type', 0, 'intval');
        $this->trackLog("roleid", $roleId);

        $types = array(array("id"=>0, "name"=>"普通"), array("id"=>2, "name"=>"红包"), array("id"=>3, "name"=>"双薪"));
        $this->trackLog("types", $types);

        if($roleId) {
            if($roleId == RoleEnum::ROLE_CITY_PM || $roleId == RoleEnum::ROLE_FRANCHISE) {
                array_push($types, array("id"=>1, "name"=>"担保"));
            };
        } else {
            $company = session("x_company");
            $this->trackLog("company", $company);
            if(array_key_exists("roleid", $company)){
                $this->trackLog("roleId", $company["roleid"]);
                if($company["roleid"] == RoleEnum::ROLE_CITY_PM || $company["roleid"] == RoleEnum::ROLE_FRANCHISE) {
                    array_push($types, array("id"=>1, "name"=>"担保"));
                };
            };
        }

        $this->trackLog("types", $types);

        $data['code'] = 1;
        $data['msg'] = "";
        $data['data'] = $types;
        $this->ajaxReturn($data);
    }

    /**
     * 发布职位
     * 传入的参数为该工作的id，
     */
    public function getJobDetails() {

        $this->trackLog("execute", "getJobDetails()");

        $jobId = I('id', '', 'trim');
        $this->trackLog("jobId", $jobId);

        if (!is_numeric($jobId)) {
            $data['code'] = 0;
            $data['msg'] = "参数格式不正确！";
            $this->ajaxReturn($data);
        }

        $db = new Model();
        $extSql = "SELECT j.id,title,j.createtime,j.number,j.income,j.linkman,j.phone,j.incomeunit,j.companyid,j.`payType`,j.`address`,j.`beginTime`,j.`endTime` ,j.beginHour,j.endHour, j.deadline, j.`intro`,
                   j.`hot`,j.`pCateId`,j.`weight`,j.`source` ,c.`companyName` as company ,ct.`name` AS city ,p.`name` as province FROM job j
                   LEFT JOIN company c ON j.`companyId` =c.id
                   LEFT JOIN gp_city ct ON j.`cityId` =ct.`id`
                   LEFT JOIN gp_province p ON j.`provinceId` =p.`id`
                   WHERE j.id=$jobId"; 
        $this->trackLog("extSql", $extSql);
        $result = $db->query($extSql);
        $this->trackLog("result", $result);

        if (!$result) {
            $data['code'] = 0;
            $data['msg'] = "参数格式不正确！";
            $this->ajaxReturn($data);
        }
        
        $job = $result[0];
        
        $job['attributes'] = array();
        $queryAttrSql = " select * from job_attribute where job_id =".$job['id'];
        $attrs= M()->query($queryAttrSql);
        for($j=0; $j < count($attrs); $j++){
            array_push( $job['attributes'], intval($attrs[$j]['attr_id']));
        }
        
        $job['incomeunit'] = JobEnum::getPriceUnit($job['incomeunit']);
        $job['paytype'] = JobEnum::getPayType($job['paytype']);
        $job['begintime'] = explode(" ", $job['begintime'])[0];
        $job['endtime'] = explode(" ", $job['endtime'])[0];
        $job['createtime'] = explode(" ", $job['createtime'])[0];

        $resultData['code'] = 1;
        $resultData['msg'] = "查询成功";
        $resultData['data'] = $job;

        $this->ajaxReturn($resultData);
    }

    public function add() {

        $uid = I('session.x_company')['id']? : I('request.companyId'); //优先从session中取，取不到就用参数中的
        if (!$uid) {
            $data['code'] = 0;
            $data['msg'] = "请先登录";
            $this->ajaxReturn($data);
        }
        $this->trackLog("execute", "doSendJob()");
        $company = I("post.company");
        $title = I("post.title");
        $pCateId = I("post.pCateId");
        $number = I("post.number");
        $income = I("post.income");
        $incomeUnit = I("post.incomeUnit");
        $provinceId = I("post.provinceId");
        $cityId = I("post.cityId");
        $regionId = I("post.regionId");
        $address = I("post.address");
        $beginTime = I("post.beginTime");
        $endTime = I("post.endTime");
        $beginHour = I("post.beginHour");
        $endHour = I("post.endHour");
        $deadline = I("post.deadline");
        $linkman = I("post.linkman");
        $phone = I("post.phone");
        $intro = I("post.intro");
        $payType = I("post.payType");
        $attributes = I("post.attributes");
        $source = I("post.source", "wechat.luobojianzhi.com");
        $superType = I("post.superType", 0, 'intval');
        
        //去掉中间的空格
        $beginHour = str_replace(' ', '', $beginHour);
        $endHour = str_replace(' ', '', $endHour);

        if (!is_numeric($income)) {
            $income = "电话咨询";
        }

        if (empty($company) ||
                empty($title) ||
                !is_numeric($pCateId) ||
                !is_numeric($number) ||
                !is_numeric($incomeUnit) ||
                !is_numeric($provinceId) ||
                !is_numeric($cityId) ||
                !is_numeric($regionId) ||
                empty($phone) ||
                empty($intro) ||
                empty($address) ||
                empty($beginTime) ||
                empty($endTime) ||
                empty($beginHour) ||
                empty($endHour) ||
                empty($linkman)) {

            $data['code'] = 0;
            $data['msg'] = "参数不正确！";
            $this->ajaxReturn($data);
        }
        
        //检验开始时间，开始时间不能小于今天
        if(strtotime($beginTime) < strtoTime(date("Y-m-d"))){
            
            $data['code'] = 0;
            $data['msg'] = "开始时间不能小于今天！";
            $this->ajaxReturn($data);    
        }
        
        //开始时间不能小于结束时间
        if(strtotime($beginTime." ".$beginHour) >= strtoTime($endTime." ".$endHour)){
            
            $data['code'] = 0;
            $data['msg'] = "开始时间不能大于结束时间！";
            $this->ajaxReturn($data);        
        }
        
        $checkParam = "+30 days";
        $endTimeMsg = "结束时间不能比今天多30天！";

        //假期工结束时间60天
        if($pCateId == 16){
            
            $checkParam = "+60 days";
            $endTimeMsg = "结束时间不能比今天多60天！";   
        }
        
        //检查结束时间，只能发布一个月内的职位
        if(strtotime($endTime) > strtoTime($checkParam)){
            
            $data['code'] = 0;
            $data['msg'] = $endTimeMsg;
            $this->ajaxReturn($data);       
        }

        $db = new Model();

        $getCompanyStatusSql = "SELECT status FROM company WHERE id= '{$uid}'";
        $this->trackLog("getCompanyStatusSql", $getCompanyStatusSql);

        $companyStatus = $db->query($getCompanyStatusSql);
        if ($companyStatus[0]['status'] == 2) {
            $status = 1;
        } else {
            $status = 0;
        }

        $insertSql = "insert into job (companyId, title, status, company, address,"
                . "linkman, phone, beginTime, endTime,beginHour,endHour, deadline, number, income, incomeUnit, intro, pCateId, provinceId, cityId,regionId, source,payType, superType)"
                . " values('{$uid}','{$title}','{$status}','{$company}','{$address}',"
                . "'{$linkman}','{$phone}','{$beginTime}','{$endTime}','{$beginHour}','{$endHour}', '{$deadline}', '{$number}','{$income}','{$incomeUnit}','{$intro}',"
                . "'{$pCateId}', '{$provinceId}', '{$cityId}','{$regionId}','{$source}','{$payType}', '{$superType}' )";

        $this->trackLog("insertSql", $insertSql);
        $re = $db->execute($insertSql);

        if ($re) {
            $data['code'] = 1;
            $data['msg'] = "发布成功";
            $data['data'] = $companyStatus[0];
            if (!empty($attributes)) {
                $this->insert_attributes($attributes, $re);
            }

            //为工作创建经纬度
            $this->createLocation($re, $cityId, $address);

            if ($status == 1) {
                $this->sync_job($re);
            }
            $this->ajaxReturn($data);
        } else {
            $data['code'] = 0;
            $data['msg'] = "发布失败";
            $this->ajaxReturn($data);
        }
    }

    public function filter() {

        $this->trackLog("execute", "filter()");

        $CompanyId = I('session.x_company')['id']? : I('request.companyId'); //session获取企业ID，优先从session中取，取不到就用参数中的;

        if (empty($CompanyId)) {
            $data['code'] = 0;
            $data['msg'] = '企业请先登录';
            $this->ajaxReturn($data);
        }

        $status = I('status', 0, 'intval');

        $this->trackLog("CompanyId", $CompanyId);
        $this->trackLog("status", $status);

        $currPage = I('currPage', 1, 'intval');
        $pageSize = I('pageSize', 10, 'intval');
        $this->trackLog("currPage", $currPage);
        $this->trackLog("pageSize", $pageSize);

        $totalSql = "SELECT count(*) AS total FROM job WHERE companyId = $CompanyId AND status = $status";
        $this->trackLog("totalSql", $totalSql);

        $db = new Model();
        $total = $db->query($totalSql);
        $this->trackLog("total", $total);

        $total = $total['0']['total'];


        if ($total > 0) {

            $offset = ($currPage - 1) * $pageSize; // 记录的游标


            $extSql = "SELECT j.`id`,j.`title`,j.`incomeUnit`,j.`pCateId`,j.`createTime`,j.`paytype`,j.`status` ,j.income,p.name AS province,c.`name` AS city,r.name AS region 
                      FROM job j LEFT JOIN gp_province p ON j.`provinceId`=p.`id` LEFT JOIN gp_city c ON j.`cityId`=c.id 
                      LEFT JOIN gp_region r ON j.`regionId` =r.`id`  WHERE j.`status` = $status and 
                      j.`companyId`= $CompanyId  order by j.id DESC limit $offset, $pageSize";


            $this->trackLog("extSql", $extSql);

            $result = $db->query($extSql);
            $result = $this->associateAttributes($result);
            $this->trackLog("result", $result);

            for ($i = 0; $i < count($result); $i++) {
                $result[$i]['incomeunit'] = JobEnum::getPriceUnit($result[$i]['incomeunit']);
                $result[$i]['paytype'] = JobEnum::getPayType($result[$i]['paytype']);
                $result[$i]['begintime'] = explode(" ", $result[$i]['begintime'])[0];
                $result[$i]['endtime'] = explode(" ", $result[$i]['endtime'])[0];
                $result[$i]['createtime'] = explode(" ", $result[$i]['createtime'])[0];
                $result[$i]['category'] = $this->getParentCate($result[$i]['pcateid']);
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
            $data['msg'] = "没有发布的记录！";
            $this->ajaxReturn($data);
        }
    }

    public function setStatus() {

        $this->trackLog("execute", "setStatus()");

        $CompanyId = I('session.x_company')['id']? : I('request.companyId'); //session获取企业ID
        $id = I('id', 0, 'intval'); //职位ID
        $status = I('status', 0, 'intval');

        if (empty($CompanyId)) {
            $data['code'] = 0;
            $data['msg'] = '企业请先登录';
            $this->ajaxReturn($data);
        }

        $this->trackLog("id", $id);
        $this->trackLog("status", $status);

        $extSql = "UPDATE job SET status = $status WHERE id = $id";
        $this->trackLog("extSql", $extSql);

        $db = new Model();
        $result = $db->execute($extSql);

        if ($result) {
            $resultData['code'] = 1;
            $resultData['msg'] = "状态修改成功";
            $this->ajaxReturn($resultData);
        } else {
            $data['code'] = 0;
            $data['msg'] = "状态修改失败";
            $this->ajaxReturn($data);
        }
    }

    function getParentCate($pCateId) {
        
        $type = I('type', 0, 'intval');
        $this->trackLog("type", $type);
        $db = new Model();

        $sql = "SELECT name FROM job_category where id= '$pCateId'";

        $result = $db->query($sql);

        return $result[0]['name'];
    }

    function sync_job($jobId) {
        $data['jobId'] = $jobId;
        request_post(C('MANAGER-HOST') . "/sync/job/add", $data);
    }

    function insert_attributes($attributes, $jobId) {

        $this->trackLog("execute", "insert_attributes()");

        $arr = array();

        foreach ($attributes as &$attr) {
            if (!is_numeric($attr) || $attr == 0) {
                continue;
            } else {
                $arr[] = "('" . $jobId . "','" . $attr . "')";
            }
        }

        if (count($arr) == 0) {
            return;
        }

        $insertSql = " insert into job_attribute (job_id,attr_id) values" . join(",", $arr);

        $this->trackLog("insertSql", $insertSql);

        $db = new Model();

        $db->execute($insertSql);
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
    
    /**
    * 遍历职位，并产生经纬度
    * 
    */
    public function processJobPos(){
        
        //首先计算所有招聘中职位数量，然后分页
        $querySql = "SELECT COUNT(*) AS count FROM job WHERE status = 1";
        $this->trackLog("querySql", $querySql);
        $result = M()->query($querySql);
        $this->trackLog("result", $result);
        
        $count = 0;
        if($result){
            
            $count = $result[0]['count'];
        }
        $pageSize = 500;
        $pageCount = floor($count / $pageSize);
        if($count % $pageSize > 0){
            
            $pageCount += 1;
        }
        $this->trackLog("pageCount", $pageCount);
        $total = 0;
        for($i = 0; $i < $pageCount; $i++){
            
             $start = $i * $pageSize;
             $length = $pageSize;
             
             $querySql = "SELECT j.id, j.address, gc.name FROM job j 
             LEFT JOIN gp_city gc ON gc.id = j.cityId
             WHERE j.status = 1 
             LIMIT $start, $length";
             $this->trackLog("querySql", $querySql);
             $items = M()->query($querySql);
             
             if(empty($items)){
                 
                 continue;
             }
             
             for($j = 0; $j < sizeof($items); $j++){
                 
                 $item = $items[$j];
                 $address = $item['address'];
                 $city = $item['name'];
                 $id = $item['id'];
                 $this->trackLog("strlen", mb_strlen($city));
                 $this->trackLog("strlen", mb_substr($city, 0, mb_strlen($city)-3));

                 if(stripos($address, mb_substr($city, 0, mb_strlen($city)-3)) === false){
                     $address = $city.$address;
                 }
                 $data = $this->getBaiduLocation($address, $city);
                 
                 
                 if($data['code'] == 0){
                     
                     continue;
                 }
                 
                 if($data['confidence'] >= 70){
                     $lng = $data['lng'];
                     $lat = $data['lat'];
                     $execSql = "INSERT INTO job_pos (id, longitude, latitude) VALUES ('$id', '$lng', '$lat')";
                     M()->execute($execSql);
                 }
             }
        }
        
        $this->trackLog("total", $total);      
    }
    
    private function createLocation($id, $cityId, $address){
        
        $this->trackLog("execute", "createLocation");
        
        $querySql = "SELECT name FROM gp_city WHERE id = '$cityId'";
        $this->trackLog("querySql", $querySql);
        $result = M()->query($querySql);
        $this->trackLog("result", $result);
        
        if(empty($result)){
            
            return false;
        }
        
        $city = $result[0]['name'];
        $data = $this->getBaiduLocation($address, $city);
        
        if($data['code'] == 0){
            
            return false;    
        }
     
        if($data['confidence'] < 20){
            
            return false;
        }
        
        $lng = $data['lng'];
        $lat = $data['lat'];
        $execSql = "INSERT INTO job_pos (id, longitude, latitude) VALUES ('$id', '$lng', '$lat')";
        $result = M()->execute($execSql);

        return true;        
    }
    
    /**
    * 通过地址和城市名获取经纬度，使用百度API
    * 
    * @param mixed $address
    * @param mixed $city
    */
    private function getBaiduLocation($address, $city){
        
        $this->trackLog("address", $address);
        $this->trackLog("city", $city);
        $newdata = urlencode($address);
        $city = urlencode($city);
        

        $url = "http://api.map.baidu.com/geocoder/v2/?address=".$newdata."&city=".$city."&output=json&ak=iCbncq8u5YxGINm7YHxtqiMGYk40cg4K";

        $address_data = file_get_contents($url);
        $json_data = json_decode($address_data);
        
        $this->trackLog("json_data", $json_data);

        if($json_data->status != 0){ //失败
            
            $data['code'] = 0;
        }
        else{ //成功
            
            $data['code'] = 1;
            $data['lng'] = $json_data->result->location->lng;
            $data['lat'] = $json_data->result->location->lat;
            $data['precise'] = $json_data->result->precise; //位置的附加信息，是否精确查找。1为精确查找，0为不精确。
            $data['confidence'] = $json_data->result->confidence; //可信度
        }
        
        return $data;     
    }
    
    /**
    * 通过城市和地址名获取经纬度，使用腾讯API
    * 
    * @param mixed $address
    * @param mixed $city
    */
    private function getTencentLocation($address, $city){
        
        $this->trackLog("address", $address);
        $this->trackLog("city", $city);
        
        if(stripos($address, mb_substr($city, 0, mb_strlen($city)-3)) === false){
                     $address = $city.$address;
        }
        $newdata = urlencode($address);
        $city = urlencode($city);
        

        $url = "http://apis.map.qq.com/ws/geocoder/v1/?address=".$newdata."&output=json&key=RC3BZ-3BTHU-QGIVP-4HQ4S-6O6K3-7VBVL";

        $address_data = file_get_contents($url);
        $json_data = json_decode($address_data);
        
        $this->trackLog("json_data", $json_data);

        if($json_data->status != 0){ //失败
            
            $data['code'] = 0;
        }
        else{ //成功
            
            $data['code'] = 1;
            $data['lng'] = $json_data->result->location->lng;
            $data['lat'] = $json_data->result->location->lat;
            $data['similarity'] = $json_data->result->similarity; //位置的附加信息，是否精确查找。1为精确查找，0为不精确。
            $data['reliability'] = $json_data->result->reliability; //可信度
            $data['deviation'] = $json_data->result->deviation; //误差距离
        }
        
        return $data;       
    }

}
