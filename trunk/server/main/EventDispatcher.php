<?php

use event\ObjectEventHandler;
use event\UserEventHandler;
                                       
require_once __DIR__ . '/event/ObjectEventHandler.php';
require_once __DIR__ . '/event/UserEventHandler.php';

class EventDispatcher
{
    private $event;
    private $event
    function __construct($eventStr){
        $this->event = $eventStr;
    }

    function handleEvent($event){

        $eventReactorClass = "{$this->event}EventHandler";
        if (class_exists($eventReactorClass )){
            $handler_obj = new $eventReactorClass ($this->event);
            $response = $handler_obj->handle();
            return $response;
        }else{
            echo "I can’t handle this!";
        }
    }
}
  
?>
