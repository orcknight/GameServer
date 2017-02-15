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

class GpController extends BaseController {

	//得到全部省市信息
	public function cityList() {
		
		$this->trackLog("execute", "cityList()");
		
		$db = new Model();
		$extSqlProvince = "SELECT * FROM gp_province  WHERE status=1";
		$provinces = $db->query($extSqlProvince);
		$this->trackLog("provinces", $provinces);

		if(empty($provinces)) {
			$data['code']  = 0;
			$data['msg']  = "没有任何省市信息！";
			$this->ajaxReturn($data);
		}

		$extSqlCity = "SELECT * FROM gp_city WHERE status=1";
		$cities = $db->query($extSqlCity);
		$this->trackLog("cities", $cities);

		$temp=array();
		for($i=0; $i < count($provinces); $i++) {
			for($j=0; $j < count($cities); $j++){
				if($cities[$j]['pid'] == $provinces[$i]['id']) {
					array_push($temp, $cities[$j]);
				}
			}
			$provinces[$i]['data']=$temp;
			$temp=array();
		}
		$this->trackLog("provinces", $provinces);
		$data['code'] = 1;
		$data['msg'] = "省市信息";
		$data['data'] = $provinces;
		$this->ajaxReturn($data);
	}
	
	//已实现
	public function regionList()
	{
		$this->trackLog("execute", "regionList()");
		$cId = I('cId', '', 'trim');
		$this->trackLog("cId", $cId);
		if(!is_numeric($cId)) 
		{
			$data['code']  = 0;
			$data['msg']  = "参数格式不正确！";
			$this->ajaxReturn($data);
		}
		
		$db=new Model();
		$extSql ="select * from gp_region where cId=$cId and status=1";
		$this->trackLog("extSql", $extSql);
		$result=$db->query($extSql);
		$this->trackLog("result", $result);
		if($result)
		{
			$data['code']  = 1;
			$data['msg']  = "查询成功";
			$data['data'] = $result;
			$this->ajaxReturn($data);
		}else
		{
			$data['code']  = 0;
			$data['msg']  = "没有地区记录！";
			$data['data'] = $result[0];
			$this->ajaxReturn($data);
		}
	}

	//根据城市id获取城市名字
	public function getNameByCId() {

		$this->trackLog("execute", "getNameByCid()");

		$cId = I('cId', '', 'trim');
		$this->trackLog("cId", "cId()");

		if(!is_numeric($cId)) {
			$data['code']  = 0;
			$data['msg']  = "参数不合法！";
			$this->ajaxReturn($data);
		}

		$extSql = "SELECT NAME FROM gp_city WHERE id=$cId";
		$this->trackLog("extSql", $extSql);

		$db=new Model();
		
		$result=$db->query($extSql);
		$this->trackLog("result", $result);

		if($result) {
			$data['code']  = 1;
			$data['msg']  = "查询成功";
			$data['data'] = $result[0];
			$this->ajaxReturn($data);
		} else {
			$data['code']  = 0;
			$data['msg']  = "没有记录！";
			$this->ajaxReturn($data);
		}
	}




	//根据城市id 获取全部学校
	public function schoolList() {
		$this->trackLog("execute", "schoolList()");

		$cId=I('cId', '', 'trim');
		$this->trackLog("cId", $cId);
        $rId=I('rId', '', 'trim');
        $this->trackLog("rId", $rId);

		if(!is_numeric($cId) && !is_numeric($rId)) {
			$data['code']  = 0;
			$data['msg']  = "参数不合法！";
			$this->ajaxReturn($data);
		}

        $extSql = "SELECT * FROM gp_school WHERE cid = $cId";
        if(is_numeric($rId)) {
            $extSql = "SELECT * FROM gp_school WHERE rid = $rId";
        }
		$this->trackLog("extSql", "extSql");

		$db = new Model();
		$result = $db->query($extSql);
        
        if($result) {
        	$data['code']  = 1;
			$data['msg']  = "查询成功";
			$data['data'] = $result;
			$this->ajaxReturn($data);
        } else {
        	$data['code']  = 0;
			$data['msg']  = "没有记录！";
			$this->ajaxReturn($data);
        }
	}

	public function returnCity() {

		$latitude =I('latitude', '', 'trim');
		$longitude = I('longitude', '', 'trim');

		$this->trackLog("latitude", $latitude);
		$this->trackLog("longitude", $longitude);

		if(empty($latitude)|| empty($longitude)) {
		    $data['msg']  = "参数错误！";
			$this->ajaxReturn($data);
		}

        //缓存经纬度
        session('lng', $longitude);
        session('lat', $latitude);
        
		$sql="SELECT id,name, ROUND(6378.138*2*ASIN(SQRT(POW(SIN(($latitude*PI()/180-`latitude`*PI()/180)/2),2)+COS($latitude*PI()/180)*COS(`latitude`*PI()/180)*POW
		(SIN(($longitude*PI()/180-`longitude`*PI()/180)/2),2)))*1000) AS juli FROM gp_city WHERE status=1 ORDER BY juli ASC";

	    $db = new Model();
		$result =$db->query($sql);

       
        $flag = false;  
		for($i=0;$i<count($result);$i++) {
			if($result[$i]['juli']!=null) {
				if( $result[$i]['juli'] >= 0 ) {
					$flag =true;
					$data['code'] = 1;
				    $data['msg'] = "定位成功！";
				    $city['id'] = $result[$i]['id'];
				    $city['name'] = $result[$i]['name'];
				    $data['data']=$city;
				    session('curr_city', $city);
				    $this->ajaxReturn($data);
				}
			}
		}

		if(!$flag) {
			$data['code']  = 0;
			$data['msg']  = "定位失败！";
			$this->ajaxReturn($data);
		}  
	}
        
        public function cityListWithInitial(){
            $db = new Model();
			$extSqlCity = "SELECT * FROM gp_city WHERE status=1 order by initials";
			$cities = $db->query($extSqlCity);
			$this->trackLog("cities", $cities);
			// print_r($cities);
			$sortedCities = array();
			foreach ($cities as $v){
				$sortedCities[$v['initials']][]=$v;
			}
			$data['code'] = 1;
			$data['msg'] = "按照字母排序的信息";
			$data['data'] = $sortedCities;
			$this->ajaxReturn($data);
        }
        
        //ip定位获得城市(供pc端调用)
        public function locateByIp(){
            $ip = I('post.ip');
            if(empty($ip)){
                $data['code']  = 0;
                $data['msg']  = "参数不正确";
                $this->ajaxReturn($data);
            }else{
                $param['ip']=$ip;
                $ipInfo = request_post('http://ip.taobao.com/service/getIpInfo.php',  http_build_query($param));
                $ipInfo = json_decode($ipInfo,true);
                $this->trackLog("ipInfo", $ipInfo);
                $city = str_replace("市",'', $ipInfo['data']['city']);
                $qrySql = "SELECT * FROM gp_city WHERE name like '".$city."%' limit 1";
                $db = new Model();
		$result = $db->query($qrySql);
                
                if($result){
                    $data['code']  = 1;
                    $data['msg']  = "查询成功";
                    $data['data'] = $result[0];
                    $this->ajaxReturn($data);
                }else{
                    $data['code']  = 0;
                    $data['msg']  = "查询失败";
                    $this->ajaxReturn($data);
                }
                
            }
            
          
        }

	//pc端调用获取热门城市
	public function getHotCityList(){
		$this->trackLog("execute", "getHotCityList()");
		$extSql = "SELECT bi.cityId, gp.name, count(*) AS total FROM bi_log AS bi left join gp_city AS gp on bi.cityId=gp.id WHERE bi.cityId!=0 GROUP BY cityId ORDER BY total DESC LIMIT 0,5";
		$this->trackLog("extSql", $extSql);
		$db = new Model();
		$result = $db->query($extSql);
		$this->trackLog("result", $result);
		if ($result) {
			$data['code']  = 1;
			$data['msg']  = "正确";
			$this->ajaxReturn($result);
		}
	}
}