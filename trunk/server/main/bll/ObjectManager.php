<?php

namespace bll;

use Dao\NpcDao;
use Dao\ItemDao;

require_once __DIR__ . '/../dao/NpcDao.php';
require_once __DIR__ . '/../dao/ItemDao.php';

class ObjectManager {
    
    private $npcDao = null;
    private $itemDao = null;
    
    public function __construct()
    {
    }
    
    
    
    public function loadObject($cityName, $tileName){
        
        $count = 0;
        $npcs = $this->getNpcDao()->queryNpcs($cityName, $tileName);
        $npcTxt = "";
        foreach($npcs as $item){
            
            if($count % 10 == 0){
                           
                $npcTxt = rtrim($npcTxt, "\$zj#");
                $npcTxt .= "\r\n005";    
            }
            
            $npcTxt .= ($item['cname'] . ":look " . $item['name'] . "\$zj#"); 
            $count++;  
        }
        
        $items = $this->getItemDao()->queryItems($cityName, $tileName);
        foreach($items as $item){
            
            if($count % 10 == 0){
                           
                $npcTxt = rtrim($npcTxt, "\$zj#");
                $npcTxt .= "\r\n005";    
            }
            
            $npcTxt .= ($item['cname'] . ":look " . $item['name'] . "\$zj#"); 
            $count++;  
        }
        $npcTxt = ltrim($npcTxt, "\r\n");
        $npcTxt = rtrim($npcTxt, "\$zj#") . "\n";
        return $npcTxt;
    }
    
    public function doLookCmd($msg, &$socket){
        
        $msg = rtrim($msg, "\n");
        if(1 == substr_count($msg, "npc")){
            
        }else{
            
            return $this->getItemDao()->queryItem($socket->cityName, $socket->tileName, explode(" ", $msg)[1])['long'];
        }    
        
        
        
    }
    
    private function getItemDao(){
        
        if($this->itemDao == null){
            
            $this->itemDao = new ItemDao();
        }
        
        return  $this->itemDao;    
    }
    
    private function getNpcDao(){
        
        if($this->npcDao == null){
            
            $this->npcDao = new NpcDao();
        }
        
        return  $this->npcDao;     
    }
    
    
    
}


?>
