<?php

namespace dao;

use DbHelper;

require_once __DIR__ . '/../db/DbHelper.php';

class UserDao{
    
    public function getUserId($name, $pass){
        
        $db = new DbHelper();
        $pass = md5($pass);
        $querySql = "SELECT id FROM user WHERE name = '$name' AND password = '$pass'";
        $result = $db->query($querySql);
        if(!$result){
            
            return 0;
        }
        
        return $result[0]['id'];

    }
    
}

?>
