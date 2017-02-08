<?php

namespace Home\Dao;

class UserDao extends BaseDao{
    
    public function __construct($db){
        
        parent::__construct($db);
    }
    
    public function add($args){
    
        $this->trackLog("execute", "add()");
        
        $chiefUserId = isset($args['chiefUserId']) ? $args['chiefUserId'] : 0;
        $adminId = isset($args['adminId']) ? $args['adminId'] : 0;
        $isNewUser = isset($args['isNewUser']) ? $args['isNewUser'] : 0;
        $phone = $args['phone'];
        $password = $args['password'];
        $openid = $args['openid'];
        $status = $args['status'];
        $chiefInviteCode = $args['chiefInviteCode'];
        
        $sql = "INSERT INTO user (chiefUserId, adminId, isNewUser, phone, password, openid, status, chiefInviteCode) 
        VALUES('$chiefUserId', '$adminId', '$isNewUser', '$phone', '$password', '$openid', '$status', '$chiefInviteCode')";
        $this->trackLog("sql", $sql);
        $res = $this->db->execute($sql);
        $this->trackLog("res", $res);
                
        $sql = "SELECT last_insert_id() as id";
        $this->trackLog("sql", $sql);
        $result = $this->db->query($sql);
        $this->trackLog('result', $result);
        
        return $result[0]['id'];   
    }
    
    public function update($args){
        
        $this->trackLog("execute", "update()");
        
        $setSql = '';
        $whereSql = '';
        
        if(isset($args['chiefUserId'])){
           
            $setSql .= ("chiefUserId='" . $args['chiefUserId'] . "', ");
        }
        if(isset($args['adminId'])){
            
            $setSql .= ("adminId='" . $args['adminId'] . "', ");
        }
        if(isset($args['isNewUser'])){
           
            $setSql .= ("isNewUser='" . $args['isNewUser'] . "', ");
        }
        if(isset($args['phone'])){
           
            $setSql .= ("phone='" . $args['phone'] . "', ");
        }
        if(isset($args['password'])){
           
            $setSql .= ("password='" . $args['password'] . "', ");
        }
        if(isset($args['openid'])){
           
            $setSql .= ("openid='" . $args['openid'] . "', ");
        }
        if(isset($args['status'])){
           
            $setSql .= ("status='" . $args['status'] . "', ");
        }
        if(isset($args['chiefInviteCode'])){
           
            $setSql .= ("chiefInviteCode='" . $args['chiefInviteCode'] . "', ");
        }
        if(isset($args['adminInviteCode'])){
           
            $setSql .= ("adminInviteCode='" . $args['adminInviteCode'] . "', ");
        }
        
        
        if(strlen($setSql) > 0){
            
            $setSql = rtrim($setSql, ', ');
        }
        if(isset($args['cond_id'])){
           
            $whereSql .= ('id=' . $args['cond_id'] . ' AND ');
        }
        if(strlen($whereSql) > 0){
            
            $whereSql = rtrim($whereSql, ' AND ');
        }
        
        $executeSql = "UPDATE user SET " . $setSql . " WHERE " . $whereSql;
        $this->trackLog("executeSql", $executeSql);        
        $result = $this->db->execute($executeSql);
        $this->trackLog("result", $result);
        
        return $result;
    }
    
    public function query($args){
        
        $this->trackLog("execute", "query()");
        
        $whereSql = ' 1=1 AND ';    
        $limitSql = '';
        
        if(isset($args['id'])){
           
            $whereSql .= ("id='" . $args['id'] . "' AND ");
        }
        
        if(isset($args['phone'])){
           
            $whereSql .= ("phone='" . $args['phone'] . "' AND ");
        }
        
        if(isset($args['openid'])){
           
            $whereSql .= ("openid='" . $args['openid'] . "' AND ");
        }
        
        if(isset($args['chiefUserId'])){
           
            $whereSql .= ("chiefUserId='" . $args['chiefUserId'] . "' AND ");
        }
        
        if(isset($args['adminId'])){
           
            $whereSql .= ("adminId='" . $args['adminId'] . "' AND ");
        }
        
        if(isset($args['isNewUser'])){
           
            $whereSql .= ("isNewUser='" . $args['isNewUser'] . "' AND ");
        }

        if(strlen($whereSql) > 0){
            
            $whereSql = rtrim($whereSql, ' AND ');
        }
        if(isset($args['start']) && isset($args['length'])){
           
            $limitSql .= (' LIMIT ' . $args['start'] . ', ' . $args['length']);
        }
        
        $querySql = "SELECT * FROM user WHERE " . $whereSql . " ORDER BY
        id DESC" . $limitSql;
        $this->trackLog("querySql", $querySql);
        $result = $this->db->query($querySql);

        return $result;               
    }
    
    public function getChiefInviteCodeById($id){
        
        $querySql = "SELECT chiefInviteCode FROM user WHERE id = $id";
        $this->trackLog("querySql", $querySql);
        $result = $this->getDb()->query($querySql);
        $this->trackLog("result", $result);
        
        if(!$result){
            
            return '';
        }
        
        return $result[0]['chiefinvitecode'];
    }
    
    public function getAdminInviteCodeById($id){
        
        $querySql = "SELECT adminInviteCode FROM user WHERE id = $id";
        $this->trackLog("querySql", $querySql);
        $result = $this->getDb()->query($querySql);
        $this->trackLog("result", $result);
        
        if(!$result){
            
            return '';
        }
        
        return $result[0]['admininvitecode'];
        
    }
    
    public function updateFansAdminIdAndCode($args){
        
        $chiefUserId = $args['chiefUserId'];
        $adminId = $args['adminId'];
        $adminInviteCode = $args['adminInviteCode'];
        
        $execSql = "UPDATE user SET adminId = '$adminId', adminInviteCode = '$adminInviteCode' WHERE chiefUserId = '$chiefUserId'";
        $result = $this->getDb()->execute($execSql);
        $this->trackLog("result", $result);
        
        return $result;
    }
    
    
}
?>
