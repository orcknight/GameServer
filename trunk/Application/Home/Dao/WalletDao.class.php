<?php

namespace Home\Dao;

class WalletDao extends BaseDao{
    
    public function __construct($db){
        
        parent::__construct($db);
    }
    
    public function add($args){

        $this->trackLog("excute", "add()");
        
        $userId = isset($args['userId']) ? $args['userId'] : 0;
        $companyId = isset($args['companyId']) ? $args['companyId'] : 0;
        $adminId = isset($args['adminId']) ? $args['adminId'] : 0;
        $money = isset($args['money']) ? $args['money'] : 0;
        $gains = isset($args['gains']) ? $args['gains'] : 0;
        $income = isset($args['income']) ? $args['income'] : 0;
        $status = isset($args['status']) ? $args['status'] : 1;
        
        $sql = "INSERT INTO wallet(userId, companyId, adminId, money, gains, income, status) 
        VALUES('$userId', '$companyId', '$adminId', '$money', '$gains', '$income', '$status')";
        $this->trackLog("sql", $sql);
        $result = $this->db->execute($sql);
        $this->trackLog("result", $result);
        
        //mysql插入操作串行进行的，“SELECT last_insert_id() as id”是线程相关的所以获取的插入id是正确的
        $sql = "SELECT last_insert_id() as id";
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
        
        if(isset($args['userId'])){
           
            $whereSql .= (" AND userId='" . $args['userId'] . "' ");
        }
        
        if(isset($args['companyId'])){
           
            $whereSql .= (" AND companyId='" . $args['companyId'] . "' ");
        }
        
        if(isset($args['adminId'])){
           
            $whereSql .= (" AND adminId='" . $args['adminId'] . "' ");
        }

        if(isset($args['start']) && isset($args['length'])){
           
            $limitSql .= (' LIMIT ' . $args['start'] . ', ' . $args['length']);
        }
        
        $querySql = "SELECT * FROM wallet WHERE " . $whereSql . " ORDER BY
        id DESC" . $limitSql;
        $this->trackLog("querySql", $querySql);
        $result = $this->db->query($querySql);

        return $result;               
    }
    
    public function update($args){
        
        $this->trackLog("execute", "DAO-update()");
        
        $setSql = '';
        $whereSql = '';
        if(isset($args['money'])){
           
            $setSql .= ('money=' . $args['money'] . ', ');
        }
        if(isset($args['gains'])){
           
            $setSql .= ('gains=' . $args['gains'] . ', ');
        }
        if(isset($args['income'])){
           
            $setSql .= ('income=' . $args['income'] . ', ');
        }
        $this->trackLog("setSql", $setSql);
        if(strlen($setSql) > 0){
            
            $setSql = rtrim($setSql, ', ');
        }
        if(isset($args['cond_id'])){
           
            $whereSql .= ('id=' . $args['cond_id'] . ' AND ');
        }
        if(strlen($whereSql) > 0){
            
            $whereSql = rtrim($whereSql, ' AND ');
        }    
        
        $executeSql = "UPDATE wallet SET " . $setSql . " WHERE " . $whereSql;
        $this->trackLog("executeSql", $executeSql);        
        $result = $this->db->execute($executeSql);
        $this->trackLog("result", $result);
        
        return $result;
    }
    
    public function getIdByUserId($userId){
        
        $querySql = "SELECT id FROM wallet WHERE userId = $userId";
        $result = $this->getDb()->query($querySql);
        $this->trackLog("result", $result);
        
        if(!$result){
            
            return 0;
        }
        
        return $result[0]['id'];
    }
}
?>
