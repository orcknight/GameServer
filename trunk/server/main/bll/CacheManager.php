<?php

namespace bll;

use Dao\NpcDao;
use Dao\ItemDao;
use Dao\TileDao;

require_once __DIR__ . '/../dao/NpcDao.php';
require_once __DIR__ . '/../dao/ItemDao.php';
require_once __DIR__ . '/../dao/RoomDao.php';


class CacheManager {
    
    
    
    public function __construct()
    {
         $this->tileMap = $this->getTileDao()->loadTileToCache();       
    }
    
    
    
    public function loadObject($cityName, $roomName){
        
        $count = 0;
        $npcs = $this->getNpcDao()->queryNpcs($cityName, $roomName);
        $npcTxt = "";
        foreach($npcs as $item){
            
            if($count % 10 == 0){
                           
                $npcTxt = rtrim($npcTxt, "\$zj#");
                $npcTxt .= "\r\n005";    
            }
            
            $npcTxt .= ($item['cname'] . ":look " . $item['name'] . "\$zj#"); 
            $count++;  
        }
        
        $items = $this->getItemDao()->queryItems($cityName, $roomName);
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
            
            return $this->getItemDao()->queryItem($socket->cityName, $socket->roomName, explode(" ", $msg)[1])['long'];
        }
    }

    //Dao层属性
    private $npcDao = null;
    private $itemDao = null;
    private $tileDao = null;
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
    
    private function getTileDao(){
        
        if($this->tileDao == null){
            
            $this->tileDao = new TileDao();
        }
        
        return  $this->tileDao;     
    }
    
    //系统缓存数据
    public $socketMap = array(); 
    public $tileMap = array();
    
    public function getTileMap(){
        
        return $this->tileMap;  
    }
    
    public function getSocketById($id){
        
        if($id < 1){
            
            return null;
        }
        
        if(!isset($this->socketMap[$id])) {
            
            return null;    
        }
        
        return $this->socketMap[$id];
        
    }
    
    public function setSocketMap($id, &$socket){
        
        $this->socketMap[$id] = $socket;
        
    }
    
    public function delSocketMap($id){
        
        unset($this->socketMap[$id]);
        
    }
    
}
  
?>
