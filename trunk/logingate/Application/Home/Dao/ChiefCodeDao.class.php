<?php

namespace Home\Dao;

class ChiefCodeDao extends BaseDao{
    
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
    
    public function getInviteCodeByChiefId($chiefId){
        
        $querySql = "SELECT inviteCode FROM ac_chief_code_map WHERE chiefId = '$chiefId'";
        $this->trackLog("querySql", $querySql);
        $result = $this->getDb()->query($querySql);
        $this->trackLog("result", $result);
        if(!$result){
            
            return 0;
        }
        
        return $result[0]['invitecode'];
    }
}
?>
