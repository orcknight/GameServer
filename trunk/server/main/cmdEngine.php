<?php

require_once __DIR__ . '/event/Autoloader.php';

class cmdEngine{
    
    public function __construct(){
          
    }
    
    public function Parse($msg, &$socket){
        
        $eventName = $this->getEventName($msg);
        return $this->handleEvent($eventName, $msg, $socket);
    }
    
    function handleEvent($event, $msg, &$socket){

        $eventReactorClass = "event\\" . "{$event}EventHandler";
        if (class_exists($eventReactorClass, false)){
            $handler_obj = new $eventReactorClass();
            $handler_obj->setSocket($socket);
            $response = $handler_obj->handle($msg);
            return $response;
        }else{
            echo "I can’t handle this!";
        }
    }
    
    private function getEventName($msg){
        
        $msg = rtrim($msg, "\n");
        if(empty($msg)){
            
            return "Default";
        }else if(3 == substr_count($msg, "║") || substr_count($msg, "║001║") == 1
        || in_array ( explode(" ", $msg)[0] , ["pianshu"] )){
            
            return "User";    
        }else if( in_array ( explode(" ", $msg)[0] , ["east", "south", "north", "west", "out"]) ){
            
            return "Move";
        }else if(in_array ( explode(" ", $msg)[0] , ["look"])){
            
            return "Object";    
            
        }
        
        return "Default";
    }
    
}
  
?>
