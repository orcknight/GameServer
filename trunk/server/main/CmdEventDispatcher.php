<?php

use event\ObjectEventHandler;
use event\UserEventHandler;
                                       
require_once __DIR__ . '/event/ObjectEventHandler.php';
require_once __DIR__ . '/event/UserEventHandler.php';

class CmdEventDispatcher
{
    function __construct(){
    }

    function handleEvent($event){

        $eventReactorClass = "{$event}EventHandler";
        if (class_exists($eventReactorClass )){
            $handler_obj = new $eventReactorClass ($event);
            $response = $handler_obj->handle();
            return $response;
        }else{
            echo "I can’t handle this!";
        }
    }
}
  
?>
