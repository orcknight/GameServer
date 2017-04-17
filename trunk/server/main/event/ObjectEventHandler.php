<?php

namespace event;

use event\BaseEventHandler;
use bll\ObjectManager;

require_once __DIR__ . '/BaseEventHandler.php';
require_once __DIR__ . '/../bll/ObjectManager.php';

class ObjectEventHandler extends BaseEventHandler{
    
    
    
    public function handle($msg){
        
        $msg = rtrim($msg, "\n");
        $socket = $this->socket;
        
        $cmd = explode(" ", $msg)[0];
        if($cmd == "look"){
            
            echo $msg;
            return $this->doLookCmd($msg, $socket);    
        }
        
    }
    
    public function doLookCmd($msg, &$socket){
        
        $msg = rtrim($msg, "\n");
        if(1 == substr_count($msg, "npc")){
            
        }else if(1 == substr_count($msg, "item")){
            
            $roomName = self::$cacheManager->getPlayerInfo($socket->userId, "roomName");
            $objects = &self::$cacheManager->getRoomObjectRef($roomName);
            
            foreach($objects as $item){
                
                if("{$item['type']}/{$item['name']}#{$item['id']}" == explode(" ", $msg)[1] ){
                    
                    return $item['long'];
                }
            }
           
        }
        
    }
    
    
    private $objectManager = null;
    private function getObjectManager(){
        
        if($this->objectManager == null){
            
            $this->objectManager = new ObjectManager();
        }
        
        return  $this->objectManager;
    }
    
    
}
  
?>
