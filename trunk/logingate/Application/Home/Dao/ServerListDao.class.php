<?php
  
namespace Home\Dao;

class ServerListDao extends BaseDao{
    
    public function __construct($db){
        
        parent::__construct($db);
    }
    
    public function getList(){
        
        $querySql = "SELECT * FROM server_list ORDER BY id DESC";
        $this->trackLog("querySql", $querySql);
        $result = $this->db->query($querySql);

        return $result;
    }
    
    
}
  
  
?>
