<?php

namespace Home\Dao;
use Enum\ThirdLevelEnum;

class ThirdLevelUserMapDao extends BaseDao{
    
    public function checkDuplicate($cid, $level){
        
        $querySql = "SELECT * FROM third_level_user_map WHERE cid = '$cid' AND level = $level";
        $result = $this->getDb()->query($querySql);
        $this->trackLog("result", $result);
        
        return $result;
    }
    
    public function add($args){

        $cid = $args['cid'];
        $pid = $args['pid'];
        $inviteCode = $args['inviteCode'];
        $level = $args['level'];
        $status = isset($args['status']) ? $args['status'] : ThirdLevelEnum::STATUS_VALID;
        
        $execSql = "INSERT INTO third_level_user_map(cid, pid, inviteCode, level, status) VALUES (
        '$cid','$pid','$inviteCode','$level','$status')";
        $result = $this->getDb()->execute($execSql);
        $this->trackLog("result", $result);
        
        return $result;
    }
    
    public function getIdByCidAndLevel($cid, $level){
        
        $querySql = "SELECT id FROM third_level_user_map WHERE cid = '$cid' AND level = $level";
        $result = $this->getDb()->query($querySql);
        $this->trackLog("result", $result);
        if(!$result){
            
            return 0;
        }
        
        return $result[0]['id'];    
        
    }
    
    public function updateById($id, $params){
        
        $setSql = $this->getUpdateString($params);
        $execSql = "UPDATE third_level_user_map SET $setSql WHERE id = $id";
        $result = $this->getDb()->execute($execSql);
        
        return $result;
    }
    
    
    private function getQueryString($args){
        
        
        $queryStr = ' 1=1 ';
        foreach($args as $key => $value){
            
            $querySql .=  " AND $key='$value' ";
        }
        
        return $querySql;
    }
    
    private function getUpdateString($args){
        
        $updateStr = '';
        foreach($args as $key => $value){
            
            $updateStr .= "$key = '$value',";
        }
        $updateStr = rtrim($updateStr, ',');
        
        return $updateStr;
    }
}

?>
