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
use Home\Service\SysEnum;
use Home\Service\JobService;

class JobController extends BaseController {

	/**
	 * 发布职位
	 */
	public function add() {

		$this->xAuth();

		$this->trackLog("execute", "add()", SysEnum::TrackSource);

		$companyId = I('companyId', 0, 'intval');
		$cateId = I('cateId', 0, 'intval');
		$pCateId = I('pCateId', 0, 'intval');

		$this->trackLog("companyId", $companyId);
		$this->trackLog("cateId", $cateId);
		$this->trackLog("pCateId", $pCateId);

		if($companyId == 0 || $cateId == 0 || $pCateId == 0) {
			$data['code']  = 0;
	        $data['msg']  = "类别参数格式不正确！";
	        $this->ajaxReturn($data);
		}

		$title = I('title', "", 'strip_tags');
		$company = I('company', "", 'strip_tags');
		$address = I('address', "", 'strip_tags');
		$linkman = I('linkman', "", 'strip_tags');
		$phone = I('phone', "", 'strip_tags');
		$number = I('number', 0, 'intval');
		$income = I('income', 0, 'intval');
		$incomeUnit = I('incomeUnit', 0, 'intval');
		$payType = I('payType', 0, 'intval');
		$intro = I('intro', 0, 'strip_tags');
		$beginTime = I('beginTime', "", 'strip_tags');
		$endTime = I('endTime', "", 'strip_tags');

		$this->trackLog("title", $title);
		$this->trackLog("company", $company);
		$this->trackLog("address", $address);
		$this->trackLog("linkman", $linkman);
		$this->trackLog("phone", $phone);
		$this->trackLog("number", $number);
		$this->trackLog("income", $income);
		$this->trackLog("incomeUnit", $incomeUnit);
		$this->trackLog("payType", $payType);
		$this->trackLog("intro", $intro);
		$this->trackLog("beginTime", $beginTime);
		$this->trackLog("endTime", $endTime);

		if($title == "" || $company == "" || $linkman == "" || $phone == 0 || $number == 0) {
			$data['code']  = 0;
	        $data['msg']  = "职位参数格式不正确！";
	        $this->ajaxReturn($data);
		}

		$provinceId = I('provinceId', 0, 'intval');
		$cityId = I('cityId', 0, 'intval');
		$regionId = I('regionId', 0, 'intval');
		$this->trackLog("provinceId", $provinceId);
		$this->trackLog("cityId", $cityId);
		$this->trackLog("regionId", $regionId);

		// 冗余属性
		$province = I('province', "", 'strip_tags');
		$city = I('city', "", 'strip_tags');
		$region = I('region', "", 'strip_tags');
		$this->trackLog("province", $province);
		$this->trackLog("city", $city);
		$this->trackLog("region", $region);

		// 默认属性 0:报名中 1:已报满 2:已过期 4:红包 5:双薪
		$type = JobEnum::STATUS_RUNNING;

		if($cityId == 0) {
			$data['code']  = 0;
	        $data['msg']  = "地域参数格式不正确！";
	        $this->ajaxReturn($data);
		}

        $extSql = "INSERT INTO job(companyId, cateId, pCateId, 
        title, company, address, linkman, phone, 
        number, income, incomeUnit, payType, intro, 
        beginTime, endTime, provinceId, province, cityId, 
        city, regionId, region) 
        VALUES('$companyId', '$cateId', '$pCateId', 
        '$title', '$company', '$address', '$linkman', '$phone', 
        '$number', '$income', '$incomeUnit', '$payType', '$intro', 
        '$beginTime', '$endTime', '$provinceId', '$province', '$cityId', 
        '$city', '$regionId', '$region')";
        
        $this->trackLog("extSql", $extSql);


      	$Model = new Model();

      	$result = $Model->execute($extSql);
      	$this->trackLog("result", $result);

      	if($result) {
	        $data['code']  = 1;
	        $data['msg']  = "职位发布成功啦！";
	        $this->ajaxReturn($data);
      	}else{
	        $data['code']  = 0;
	        $data['msg']  = "职位发布失败啦";
	        $this->ajaxReturn($data);
      	}
	}

	/**
	 * 查询全部分类信息(一级分类&二级分类)
	 * Bug 工作与2级分类关联，选择项为1级分类。
	 * ToRe 代码逻辑 & SQL
	 */
	public function getCategory() {

		$this->trackLog("execute", "getCategory");
        
        $type = I('type', JobEnum::CATEGORY_TYPE_PARTTIME, 'intval');

		$extSqlPtCategory="SELECT * FROM job_category WHERE pId=0 AND status=1 AND type=$type ORDER BY weight";
		$extSqlCategory="SELECT * FROM job_category WHERE pId IN (SELECT pId FROM job_category WHERE pId!=0 and status=1 AND type=$type)";
		$this->trackLog("extSqlPtCategory", $extSqlPtCategory);
		$this->trackLog("extSqlCategory", $extSqlCategory);

		$result01=array();
		$result02=array();

		$db=new Model();

		$result01=$db->query($extSqlPtCategory);
		$result02=$db->query($extSqlCategory);

		$this->trackLog("result01", $result01);
		$this->trackLog("result02", $result02);

		$result=array();

		$result['code']  = 1;
		$result['msg']  = "查询成功！";

		$temp=array();
		for($i=0;$i<count($result01);$i++)
		{
			for($j=0;$j<count($result02);$j++)
			{
				if($result02[$j]['pid']==$result01[$i]['id'])
				{
					array_push($temp,$result02[$j]);

				}
			}
			$result01[$i]['data']=$temp;
			$temp=array();
		}
		$result['data']=$result01;

		$this->ajaxReturn($result);
		
	}

	/**
	 * 查询 “时间跨度”
	 * ToRe 时间逻辑 & SQL优化
	 */	
	public function timeSpan() {

		$this->trackLog("execute", "timeSpan");
		
		$extSql="SELECT * FROM job_timespan WHERE status=1";
		$this->trackLog('extSql', $extSql);

		$db=new Model();
		$result=$db->query($extSql);
		$this->trackLog("result", $result);

		if($result) {

			$data['code']  = 1;
			$data['msg']  = "查询成功";
			$data['data'] = $result;
			$this->ajaxReturn($data);

		}else {
			$data['code']  = 0;
			$data['msg']  = "没有日期跨度记录！";
			$data['data'] = $result[0];
			$this->ajaxReturn($data);
		}
	}

	/**
	 * 根据城市id以及过滤条件查询工作列表
	 * ToRe 时间逻辑 & SQL优化
	 */	
	public function jobs() {

		$this->trackLog("execute", "getJobsList()");

		$cId = I('cId','','trim');
		if(empty($cId)) $cId = 1;
		$this->trackLog("cId", $cId);

		$type = I('type','','trim'); // 0:时间 1:热度
		$this->trackLog("type", $type);

		$queryPrms = I('subType','','trim');
		$this->trackLog("queryPrms", $queryPrms);

        $regionId = $queryPrms["regionId"]; //地区Id
		$pCateId = $queryPrms["pCateId"]; //工作分类Id
		$timeId = $queryPrms["timeId"]; //时间分类Id
        $keyword = I('keyword','','trim');//关键字
        $statuses = I('statuses', 1);  
        $cateId = 0;
             
        $this->trackLog("statuses", $statuses);
		$this->trackLog("regionId", $regionId);
		$this->trackLog("timeId", $timeId);
		$this->trackLog("pCateId", $pCateId);
        $this->trackLog("keyword", $keyword);

		$currPage = I('currPage','','trim'); //当前页数
		$pageSize=I('pageSize','','trim'); //每页数据条数
		$this->trackLog("currPage", $currPage);
		$this->trackLog("pageSize", $pageSize);


		if(!is_numeric($cId) || !is_numeric($type) || !is_numeric($regionId) || !is_numeric($pCateId) || !is_numeric($timeId) || !is_numeric(
			$currPage) || !is_numeric($pageSize)) {
			$data['code']  = 0;         
			$data['msg']  = "参数格式不正确！"; 
			$this->ajaxReturn($data);  
		}
        
        $lng = session('lng');
        $lat = session('lat');
        $this->trackLog("lng", $lng);
        $this->trackLog("lat", $lat);
        $distanceSql = '';
		if($type==JobEnum::SEARCH_TYPE_HOT) {
        
            $orderSql = 'j.hot';
		}else if($type==JobEnum::SEARCH_TYPE_INDEXPAGE) {
            $orderSql = 'j.weight desc,j.createtime';
		}else if($type == JobEnum::SEARCH_TYPE_TRAINEE) { //实习生工作
            //设置兼职类型
            $pCateId = JobEnum::intern;
            //$cateId = 57;
            $orderSql = "j.createtime";
        }else if($type == JobEnum::SEARCH_TYPE_LINE){
            $pCateId = JobEnum::line;
            $orderSql = "j.createtime";
        }else if($type == JobEnum::SEARCH_TYPE_DISTANCE){
            
            $orderSql = "ACOS(SIN(('$lat' * 3.1415) / 180 ) * SIN((jp.latitude * 3.1415) / 180 ) + COS(('$lat' * 3.1415) / 180 ) * COS((jp.latitude * 3.1415) / 180 ) * COS(('$lng'* 3.1415) / 180 - (jp.longitude * 3.1415) / 180 ) ) * 6380 ASC, j.createtime";
            $distanceSql = " AND jp.latitude > $lat-1 AND jp.latitude < $lat+1 AND jp.longitude > $lng-1 AND jp.longitude < $lng+1";
        }else if($type == JobEnum::SEARCH_TYPE_HOLIDAY) {
            
            $pCateId = JobEnum::holiday;
            $orderSql = "j.createtime";
        }else if($type == JobEnum::SEARCH_TYPE_FULLTIME){
            
            $args = array(
            'cId' => $cId,
            'regionId' => $regionId,
            'pCateId' => $pCateId,
            'cateId' => $cateId,
            'timeId' => $timeId,
            'keyword' => $keyword,
            'statuses' => $statuses,
            'start' => ($currPage-1) * $pageSize,
            'length' => $pageSize,
            );
            $jobService = new JobService();
            $this->ajaxReturn($jobService->getFullTimeJobs($args));
        }else{
            $orderSql = "j.createtime";
        }
               
		$offset=($currPage-1) * $pageSize;	
		$startTime = $this->getTimeById($timeId);
		$endTime = date("Y-m-d 23:59:59");

		$extSql = "SELECT ca.name AS category, j.superType, j.hot, j.pCateId,j.id, j.phone,j.weight, j.status, j.type, j.income, j.title,j.payType, j.incomeUnit, j.source, j.intro, j.createTime, j.updateTime, j.beginTime, j.endTime,j.beginHour,j.endHour, p.name AS province, c.name AS city, r.name AS region, co.companyName AS company 
		FROM job j 
        LEFT JOIN gp_province p ON j.provinceId = p.id 
        LEFT JOIN gp_city c ON j.cityId=c.id 
        LEFT JOIN gp_region r ON j.regionId = r.id 
        LEFT JOIN company co ON j.companyId = co.id 
        LEFT JOIN job_category ca ON j.pCateId = ca.id
        LEFT JOIN job_attribute ja ON j.id = ja.job_id
        LEFT JOIN job_pos jp ON j.id = jp.id
        WHERE j.cityId = $cId AND j.superType = 0".$distanceSql; 
 
		$extSqlCount="SELECT count(*) AS count FROM `job` AS j 
        LEFT JOIN job_attribute ja ON j.id = ja.job_id
        LEFT JOIN job_pos jp ON j.id = jp.id
        WHERE cityId = $cId AND j.superType = 0".$distanceSql;

		$sql_time='';
		$sql_cate='';
		$sql_region='';
        $sql_keyword='';
                
        $statuses_sql = " AND j.status = '$statuses' ";

		$end_sql=' ORDER BY '.$orderSql.'  DESC ';
		
		$page_sql= 'LIMIT '.$offset.','.$pageSize;

		if($timeId > 0) {
			$sql_time=" AND j.createtime BETWEEN '".$startTime."' AND '".$endTime."'";	
		}
        
		if($pCateId > 0) {
			$sql_cate=' AND j.pCateId='.$pCateId;
		}
        
        if($cateId > 0){
            $sql_cate .= ' AND j.cateId = ' . $cateId;
        }
        
		if($regionId > 0){
			$sql_region=' AND j.regionId='.$regionId ;
		}
        
        //处理担保交易列表
        if($type == JobEnum::SEARCH_TYPE_GUARANTEE){
            $jobAttr = JobEnum::ATTR_GUARANTEE;
            $extSqlCount .= " AND ja.attr_id = '$jobAttr' ";
            $extSql .= " AND ja.attr_id = '$jobAttr' ";
        }
        
        if(!empty($keyword)){
            $sql_keyword=' AND j.title like "%'.$keyword.'%"' ;
        }
                
		$totalSQL = $extSqlCount .$statuses_sql. $sql_region . $sql_time . $sql_keyword .$sql_cate . $end_sql;
		$this->trackLog("totalSQL", $totalSQL);

		$db=new Model();

		$resultTotal=$db->query($totalSQL);
		$total=$resultTotal[0]['count'];
		$this->trackLog("total", $total);

		if($total < 1) {
            
            $resultData['code']  = 0;
            $resultData['msg']  = "没有数据";
            $this->ajaxReturn($resultData);
        }
        
        //首页的列表优先显示担保日结的，这里处理下
        if(I('type','','trim') == JobEnum::SEARCH_TYPE_INDEXPAGE){
            
            $end_sql=' ORDER BY weight DESC, if(ja.attr_id =1 ,0,1), if(j.payType =1 and ja.attr_id = 1,0,1), createtime DESC ';
        }
        
		$extSql = $extSql .$statuses_sql. $sql_region . $sql_time .$sql_keyword . $sql_cate . $end_sql . $page_sql;
		$this->trackLog("extSql", $extSql);
		$result=$db->query($extSql);
		$this->trackLog("result", $result);
        $result = $this->associateAttributes($result);
	    //把查出来的 incomeUnit/payType 的数字类型改为文字
		for($i = 0; $i<count($result);$i++) {
			$incomeUnit = $result[$i]['incomeunit'];
			$incomeUnit =JobEnum::getPriceUnit($incomeUnit);
			$result[$i]['incomeunit'] =  $incomeUnit;

			$payType = $result[$i]['paytype'];
			$payType =JobEnum::getPayType($payType);
			$result[$i]['paytype'] =  $payType;

			$result[$i]['begintime'] = explode(" ", $result[$i]['begintime'])[0];
			$result[$i]['endtime'] = explode(" ", $result[$i]['endtime'])[0];
			$result[$i]['createtime'] = explode(" ", $result[$i]['createtime'])[0];
		}

		$resultData['code']  = 1;
		$resultData['msg']  = "查询成功";

		$data['currPage']=$currPage;
		$data['pageSize']=$pageSize;
		$data['total']=$total;
		$data['data'] = $result;
		$resultData['data']=$data;
		$this->ajaxReturn($resultData);
	}

	public function getJobDetails() {
		$this->trackLog("execute", "getJobDetails()");

		$jobId=I('id','','trim');
		$this->trackLog("jobId", $jobId);

		$uId = session("x_user")['id'];//session取得用户ID

		if(!is_numeric($jobId)) {
			$data['code']  = 0;         
			$data['msg']  = "参数格式不正确！";  
			$this->ajaxReturn($data);        
		}

		$db = new Model();
		$extSql = "SELECT j.id, j.superType, title,j.createtime,j.number,j.income,j.linkman,j.phone,j.incomeunit,j.companyid,j.`payType`,j.`address`,j.`beginTime`,j.`endTime` ,j.beginHour,j.endHour, j.deadline, j.`intro`,
                   j.`hot`,j.`pCateId`,j.`weight`,j.`source` ,c.`companyName` as company ,c.intro as companyinfo,ct.`name` AS city ,p.`name` as province FROM job j
                   LEFT JOIN company c ON j.`companyId` =c.id
                   LEFT JOIN gp_city ct ON j.`cityId` =ct.`id`
                   LEFT JOIN gp_province p ON j.`provinceId` =p.`id`
                   WHERE j.id=$jobId"; 
		$this->trackLog("extSql", $extSql);
		$result = $db->query($extSql);
		$this->trackLog("result", $result);

		if(!$result) {
			$data['code'] = 0;         
			$data['msg'] = "没有相关信息";  
			$this->ajaxReturn($data);  
		}

		$hot = $result[0]['hot'];
		$this->trackLog("hot", $hot);
		$hot++;

		$extSql = "UPDATE job SET hot = $hot WHERE id = $jobId";
	    $this->trackLog("extSql", $extSql);
      	$db->execute($extSql);
      	
		$job = $result[0];
		$job['incomeunit'] = JobEnum::getPriceUnit($job['incomeunit']);
		$job['paytype'] = JobEnum::getPayType($job['paytype']);
		$job['category'] = getParentCate($job['pcateid']);
		$job['begintime'] = explode(" ", $job['begintime'])[0];
		$job['endtime'] = explode(" ", $job['endtime'])[0];
		$job['createtime'] = explode(" ", $job['createtime'])[0];
		$job['applied'] = 0;
		$job['collected'] = 0;	
        $job['title'] =$result[0]['title'];
        if($uId) { 		
			$checkSql = "SELECT id FROM JOB_APPLY WHERE userId = $uId AND jobid=$jobId";
			$this->trackLog("checkSql", $checkSql);
			$checkResult = $db->query($checkSql);
			$this->trackLog("checkResult", $checkResult);
			if($checkResult) {
				$job['applied'] = 1;//已经申请过状态为1 没申请是 0
			}

			$checkCollectSql="SELECT id FROM JOB_COLLECT WHERE UID = $uId AND jobid = $jobId";
			$this->trackLog("checkCollectSql", $checkCollectSql);
			$collectResult=$db->query($checkCollectSql);
			$this->trackLog("collectResult", $collectResult);
			if($collectResult) {
				$job['collected'] = 1;//已经收藏过状态为1 没申请是 0
			}
		}
                $job['attributes'] = array();
                $queryAttrSql = " select * from job_attribute where job_id =".$job['id'];
                $attrs= M()->query($queryAttrSql);
                for($j=0; $j < count($attrs); $j++){
                    array_push( $job['attributes'], intval($attrs[$j]['attr_id']));
                }
                
		$resultData['code']  = 1;
		$resultData['msg']  = "查询成功";
		$resultData['data'] = $job;

		$this->ajaxReturn($resultData);
	}
        
    public function getSortedCategory(){
        $this->trackLog("execute", "getSortedCategory()");
       
        $startDate=date("Y-m-d H:i:s",strtotime("-7 day"));//7天内
        $endDate = date("Y-m-d H:i:s");
        $type = I('type', 0, 'intval');
        
        
        $querySql = "SELECT id,name FROM job_category cate "
                    ."left join "
                    ."(select categoryId,count(*) cnt from bi_log where behavior = 'm_b_tab_catg' and createTime between '".$startDate."' and '".$endDate
                    . "' group by categoryId ) log "
                    ."on cate.id = log.categoryId "
                    ." WHERE pId=0 AND status=1 AND type = '$type' order by log.cnt desc ";
 
        $result = M()->query($querySql);
        
        if( $result){
            $resultData['code']  = 1;
            $resultData['msg']  = "查询成功";
            $resultData['data'] = $result;
        }else{
            $resultData['code']  = 0;
            $resultData['msg']  = "查询失败";
        }
       $this->ajaxReturn($resultData);
        
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
    
    public function getTimeById($timeId){
        
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
    
    public function getNearbyJobs(){
        
        $lon = 122.064085;
        $lat = 37.527934;
        $querySql = "select ACOS(SIN(('$lat' * 3.1415) / 180 ) *SIN((latitude * 3.1415) / 180 ) +COS(('$lat' * 3.1415) / 180 ) * COS((latitude * 3.1415) / 180 ) *COS(('$lon'* 3.1415) / 180 - (longitude * 3.1415) / 180 ) ) * 6380 as length from job_pos where latitude > '$lat'-1 and latitude < '$lat'+1 and longitude > '$lon'-1 and longitude < '$lon'+1 order by ACOS(SIN(('$lat' * 3.1415) / 180 ) *SIN((latitude * 3.1415) / 180 ) +COS(('$lat' * 3.1415) / 180 ) * COS((latitude * 3.1415) / 180 ) *COS(('$lon'* 3.1415) / 180 - (longitude * 3.1415) / 180 ) ) * 6380 asc limit 10";
        
        $this->trackLog("querySql", $querySql);
        $result = M()->query($querySql);
        $this->trackLog("result", $result);
    }
}