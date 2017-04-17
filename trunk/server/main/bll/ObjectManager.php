<?php

namespace bll;

use Dao\NpcDao;
use Dao\ItemDao;

require_once __DIR__ . '/../dao/NpcDao.php';
require_once __DIR__ . '/../dao/ItemDao.php';

class ObjectManager {
    
    private $npcDao = null;
    private $itemDao = null;
    private $objectsMap = null;
    
    public function __construct(){
    }
    
    public function loadObjectToMap(&$objectsMap){
        
        $items = $this->getItemDao()->queryAllItem();
        foreach($items as $item){
            
            $item['type'] = "item";
            $objectsMap[$item['roomName']][] = $item;
        }
    }
    
    public function loadObject($roomName, &$objectsMap){
        
        $count = 0;
        $npcs = $this->getNpcDao()->queryNpcs($roomName);
        $npcTxt = "";
        foreach($npcs as $item){
            
            if($count % 10 == 0){
                           
                $npcTxt = rtrim($npcTxt, "\$zj#");
                $npcTxt .= "\r\n005";    
            }
            
            $npcTxt .= ($item['cname'] . ":look " . $item['name'] . "\$zj#"); 
            $count++;  
        }
        
        $items = $objectsMap[$roomName];
        foreach($items as $item){
            
            //echo $item['cname'] . "\n";
            
            if($count % 10 == 0){
                           
                $npcTxt = rtrim($npcTxt, "\$zj#");
                $npcTxt .= "\r\n005";    
            }
            
            $npcTxt .= ($item['cname'] . ":look {$item['type']}/{$item['name']}#{$item['id']}" . "\$zj#"); 
            $count++;  
        }
        $npcTxt = ltrim($npcTxt, "\r\n");
        $npcTxt = rtrim($npcTxt, "\$zj#") . "\n";
        return $npcTxt;
    }
    
    public function doLookCmd($msg, &$socket){
        
        $msg = rtrim($msg, "\n");
        if(1 == substr_count($msg, "npc")){
            
        }else if(1 == substr_count($msg, "item")){

            return $this->getItemDao()->queryItem($socket->roomName, explode(" ", $msg)[1])['long'];
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
