<?php

namespace Home\Dao;

class UserDao extends BaseDao{
    
    public function __construct($db){
        
        parent::__construct($db);
    }
    
    public function add($args){
    
        $this->trackLog("execute", "add()");
        
        $name = $args['name'];
        $pass = $args['pass'];
        $phone = $args['phone'];
        $email = $args['email'];
        
        $sql = "INSERT INTO user (name, password, phone, email) VALUES ('$name', '$pass', '$phone', '$email')";
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
    
    public function isDuplicate($name){
        
        $querySql = "SELECT * FROM user WHERE name = '$name'";
        $result = M()->query($querySql);
        if($result){
            return true;
        }
        
        return false;
        
    }
    
    
}
?>
