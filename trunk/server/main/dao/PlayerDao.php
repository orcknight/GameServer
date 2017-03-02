<?php

namespace dao;

use DbHelper;

require_once __DIR__ . '/../db/DbHelper.php';

class PlayerDao{
    
    public function queryPlayer($userId){
        
        $db = new DbHelper();
        $querySql = "SELECT * FROM player WHERE userId = $userId";
        $result = $db->query($querySql);
        if(!$result){
            
            return null;
        }
        
        return $result[0];
    }
    
    public function queryPlayerInfo($playerId){
        
        $db = new DbHelper();
        $querySql = "SELECT * FROM player_info WHERE playerId = $playerId";
        $result = $db->query($querySql);
        if(!$result){
            
            return null;
        }
        
        return $result[0];    
        
    }
    
    public function addPlayer($userId, $name, $sex){
        
        $db = new DbHelper();
        $execSql = "INSERT INTO player (userId, name ,sex) 
        VALUES ('$userId', '$name', '$sex')";
        if(!$db->execute($execSql)){
            
            return 0;
        }
        
        $querySql = "SELECT id FROM player WHERE userId = '$userId'";
        $result = $db->query($querySql);
        if(!$result){
            
            return 0;
        }
        
        return $result[0]['id'];
        
    }
    
    public function addPlayerInfo($playerId, $roomName = "register/shengmingzhigu"){
        
        $db = new DbHelper();
        $execSql = "INSERT INTO player_info (playerId, ip, roomName) 
        VALUES ('$playerId', '127.0.0.1', '$roomName')";
        
        return $db->execute($execSql);
 
    }
    
    public function updatePlayerLocation($id, $roomName){
        
        $db = new DbHelper();
        $execSql = "UPDATE player_info SET roomName = '$roomName' WHERE playerId = $id";
        return $db->execute($execSql);
        
    }
    
    public function updatePlayerCharacter($id, $character){
        
        $db = new DbHelper();
        $execSql = "UPDATE player SET `character` = '$character' WHERE userId = $id";
        echo  $execSql;
        return $db->execute($execSql);
        
    }
    
}

?>
