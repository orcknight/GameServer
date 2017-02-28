<?php

namespace dao;

use DbHelper;

require_once __DIR__ . '/../db/DbHelper.php';

class NpcDao{
    
    public function queryNpcs($roomName){
        
        $db = new DbHelper();
        $querySql = "SELECT * FROM npc WHERE roomName = '$roomName'";
        $result = $db->query($querySql);
        if(!$result){
            
            return array();
        }
        
        return $result;
    }
    
    public function queryNpc($roomName, $name){
        
        $db = new DbHelper();
        $querySql = "SELECT * FROM npc WHERE roomName = '$roomName' AND name = '$name'";
        $result = $db->query($querySql);
        if(!$result){
            
            return array();
        }
        
        return $result[0];    
    }
}

?>
