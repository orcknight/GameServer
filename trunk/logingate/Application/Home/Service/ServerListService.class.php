<?php

namespace Home\Service;

use Think\Model;
use Home\Dao\ServerListDao;

class ServerListService extends BaseService {
    
    private $db;
    
    public function __construct() {
        
        $this->db = new Model();
    }
    
    
    public function getList() {

        $this->trackLog("execute", "getList()");
        
        $serverListDao = new ServerListDao($this->db);
        $serverList = $serverListDao->getList();
        
        $data['code'] = 1;
        $data['msg']  = "获取成功！";
        $data['data'] = $serverList;
        
        return $data;
    }
}
?>
