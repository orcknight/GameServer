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
            return $this->getObjectManager()->doLookCmd($msg, $socket);    
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
