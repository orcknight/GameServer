<?php

namespace Home\Service;

use Think\Model;
use Home\Dao\FormDao;
/**
*
*/
class FormService extends BaseService {
    
    private static $db;
    private static $formDAO;
    
    public function __construct() {
        
        $this->db = new Model();
        self::$formDAO = new FormDao($this->db);
    }

    public function getFormById($args){
        $db = new Model();
        $result= self::$formDAO->getFormById($db,$args["id"]);
        if(!empty($result)){
            $result['code'] = 1;
            $result['msg'] = "查询成功";
        }else{
            $result['code'] = 0;
            $result['msg'] = "查询失败";
        }

        return $result;
    }

    public function addFormResult($args){
        $db = new Model();
        $result=self::$formDAO->addFormResult($db,$args);
        if($result) {
            $data['code'] = 1;
            $data['msg'] = "信息添加成功";
        } else {
            $data['code'] = 0;
            $data['msg'] = "信息添加失败";
        }
        return $data;
    }
}

?>
