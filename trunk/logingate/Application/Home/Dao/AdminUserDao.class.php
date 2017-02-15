<?php

namespace Home\Dao;

class AdminUserDao extends BaseDao{
    
    public function getIdByInviteCode($inviteCode){
        
        $this->trackLog("execute", "getIdByInviteCode()");
        
        $querySql = "SELECT id FROM admin_user WHERE inviteCode = '$inviteCode'";
        $result = $this->getDb()->query($querySql);
        $this->trackLog("result", $result);
        if(!$result){
            
            return 0;
        }
        
        return $result[0]['id'];
    }
}

?>
