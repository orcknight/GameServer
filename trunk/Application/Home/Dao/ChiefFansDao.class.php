<?php

namespace Home\Dao;

class ChiefFansDao extends BaseDao{
    
    public function getChiefIdByInviteCode($inviteCode){
        
        $querySql = "SELECT chiefId FROM ac_chief_code_map WHERE inviteCode = '$inviteCode'";
        $this->trackLog("querySql", $querySql);
        $result = $this->getDb()->query($querySql);
        $this->trackLog("result", $result);
        if(!$result){
            
            return 0;
        }
        
        return $result[0]['chiefid'];
    }
    
    public function setChiefId($args){
        
        $openid = $args['openid'];
        $chiefId = $args['chiefId'];
        
        $execSql = "UPDATE ac_chief_fans SET chiefId = $chiefId, bind = 1 WHERE openid = '$openid'";
        $this->trackLog("execSql", $execSql);
        $result = $this->getDb()->execute($execSql);
        
        return $result;
    }
    
    public function getChiefId($args){
        
        
    }
    
    private function getWhereParam($args){
        
        foreach($args as $key => $value){
            
            
        }
        
        
    }
    
}

?>
