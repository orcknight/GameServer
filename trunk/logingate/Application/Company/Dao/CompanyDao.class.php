<?php

namespace Company\Dao;

class CompanyDao extends BaseDao{
    
    public function __construct($db){
        
        parent::__construct($db);
    }
    
    public function add($args){
        
        $this->trackLog("excute", "add()");
        
        $companyName = isset($args['companyName']) ? $args['companyName'] : '小萝卜';
        $phone = $args['phone'];
        $password = $args['password'];
        $openid = $args['openid'];
        $status = $args['status'];
        
        $execSql = "INSERT INTO Company(phone, companyName, password, status,openid) 
        VALUES('$phone', '$companyName', '$password', '$status','$openId')";
        $this->trackLog("execSql", $execSql);
        $result = $this->db->execute($execSql);
        $this->trackLog("result", $result);
        
        //mysql插入操作串行进行的，“SELECT last_insert_id() as id”是线程相关的所以获取的插入id是正确的
        $sql = "SELECT last_insert_id() AS id";
        $this->trackLog("sql", $sql);
        $result = $this->db->query($sql);
        $this->trackLog('result', $result);
        
        return $result[0]['id'];
    }
    
    public function query($args){
        
        $this->trackLog("execute", "query()");
        
        $whereSql = ' 1=1 ';    
        $limitSql = '';
        if(isset($args['id'])){
           
            $whereSql .= (" AND id='" . $args['id'] . "' ");
        }
        if(isset($args['phone'])){
           
            $whereSql .= (" AND phone='" . $args['phone'] . "' ");
        }
        if(isset($args['openid'])){
           
            $whereSql .= (" AND openid='" . $args['openid'] . "' ");
        }
        if(isset($args['start']) && isset($args['length'])){
           
            $limitSql .= (' LIMIT ' . $args['start'] . ', ' . $args['length']);
        }
        
        $querySql = "SELECT * FROM company WHERE " . $whereSql . " ORDER BY
        id DESC" . $limitSql;
        $this->trackLog("querySql", $querySql);
        $result = $this->db->query($querySql);

        return $result;
    }
    
    
}
?>
