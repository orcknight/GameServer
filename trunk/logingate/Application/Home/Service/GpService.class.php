<?php

namespace Home\Service;

use Think\Model;

class GpService extends BaseService {

	public function getCidByName($name) {

		$db=new Model();

		$extSql = "SELECT id FROM gp_city WHERE NAME LIKE '%".$name."%'";

		$this->trackLog("extSql", $extSql);

		$result = $db->query($extSql);

		$this->trackLog("result", $result);

		$cId = -1;


		if(!empty($result[0])) {
			$cId = $result[0]["id"];
		}

		$this->trackLog("cId", $cId);
		
		return $cId;
	}
}