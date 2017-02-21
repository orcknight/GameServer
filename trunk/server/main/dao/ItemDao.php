<?php

namespace dao;

use DbHelper;

require_once __DIR__ . '/../db/DbHelper.php';

class ItemDao{
    
    public function queryItems($cityName, $tileName){
        
        $db = new DbHelper();
        $querySql = "SELECT * FROM item WHERE cityName='$cityName' AND tileName = '$tileName'";
        echo $querySql;
        $result = $db->query($querySql);
        if(!$result){
            
            return array();
        }
        
        return $result;
    }
    
    public function queryItem($cityName, $tileName, $name){
        
        $db = new DbHelper();
        $querySql = "SELECT * FROM item WHERE cityName='$cityName' AND tileName = '$tileName' AND name = '$name'";
        echo $querySql;
        $result = $db->query($querySql);
        if(!$result){
            
            return array();
        }
        
        return $result[0];    
    }
}

?>
