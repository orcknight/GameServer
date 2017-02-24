<?php

namespace dao;

use DbHelper;

require_once __DIR__ . '/../db/DbHelper.php';

class NpcDao{
    
    public function queryNpcs($cityName, $tileName){
        
        $db = new DbHelper();
        $querySql = "SELECT * FROM npc WHERE cityName='$cityName' AND tileName = '$tileName'";
        $result = $db->query($querySql);
        if(!$result){
            
            return array();
        }
        
        return $result;
    }
    
    public function queryNpc($cityName, $tileName, $name){
        
        $db = new DbHelper();
        $querySql = "SELECT * FROM npc WHERE cityName='$cityName' AND tileName = '$tileName' AND name = '$name'";
        $result = $db->query($querySql);
        if(!$result){
            
            return array();
        }
        
        return $result[0];    
    }
}

?>
