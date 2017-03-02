<?php

namespace dao;

use DbHelper;

require_once __DIR__ . '/../db/DbHelper.php';

class ItemDao{
    
    public function queryAllItem(){
        
        $db = new DbHelper();
        $querySql = "SELECT * FROM item";
        $result = $db->query($querySql);
        if(!$result){
            
            return array();
        }
        
        return $result;    
    }
    
    public function queryItems($roomName){
        
        $db = new DbHelper();
        $querySql = "SELECT * FROM item WHERE roomName = '$roomName'";
        $result = $db->query($querySql);
        if(!$result){
            
            return array();
        }
        
        return $result;
    }
    
    public function queryItem($roomName, $name){
        
        $db = new DbHelper();
        $querySql = "SELECT * FROM item WHERE roomName = '$roomName' AND name = '$name'";
        echo $querySql . "\n";
        $result = $db->query($querySql);
        if(!$result){
            
            return array();
        }
        
        return $result[0];    
    }
}

?>
