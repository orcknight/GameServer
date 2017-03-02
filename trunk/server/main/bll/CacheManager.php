<?php

namespace bll;

use Dao\NpcDao;
use Dao\ItemDao;
use Dao\RoomDao;
use bll\ObjectManager;

require_once __DIR__ . '/../dao/NpcDao.php';
require_once __DIR__ . '/../dao/ItemDao.php';
require_once __DIR__ . '/../dao/RoomDao.php';
require_once __DIR__ . '/ObjectManager.php';


class CacheManager {
    
    private $payersMap = null;
    private $roomsMap = null;
    private $objectsMap = null;
    
    public function __construct()
    {
        $this->payersMap = array();
        $this->roomsMap = $this->getRoomDao()->loadRoomToCache();  
        $this->objectsMap = $this->getRoomDao()->loadRoomStruct();
        $this->getObjectManager()->loadObjectToMap($this->objectsMap);
        
        foreach($this->objectsMap as $key => $item){
            
            echo $key . "\n";
            
            foreach($item as $itm){
                
                echo $itm['cname'] . "\n";
            }
        }   
    }
    
    public function setPlayerInfo($userId, $key, $value){
        
        if(!isset($this->payersMap[$userId])){
            
            $this->payersMap[$userId] = array();
        }
        
        $this->payersMap[$userId][$key] = $value;
    }
    
    public function getPlayerInfo($userId, $key){
        
        if(!isset($this->payersMap[$userId][$key])){
            
            return "";
        }
        
        return $this->payersMap[$userId][$key];
    }
    
    public function &getPlayerRef($userId){
        
        if(!isset($this->payersMap[$userId])){
            
            $this->payersMap[$userId] = array();
        }
        
        return $this->payersMap[$userId];   
    }
    
    public function &getObjectRef(){
        
        return $this->objectsMap;
    }
    
    public function getRoom($roomName){
        
        return $this->roomsMap[$roomName];
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
    
    private function getRoomDao(){
        
        if($this->roomDao == null){
            
            $this->roomDao = new RoomDao();
        }
        
        return  $this->roomDao;     
    }
    
    private $objectManager = null;
    public function getObjectManager(){
        
        if($this->objectManager == null){
        
            $this->objectManager = new ObjectManager();    
        }
        
        return $this->objectManager; 
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
    
    public function __set($name, $value){ 
        
        $this->$name = $value; 
    } 
    //__get()方法用来获取私有属性 
    public function &__get($name){ 
        
        return $this->$name; 
    } 
    
}
  
?>
