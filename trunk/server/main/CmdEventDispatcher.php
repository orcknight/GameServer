<?php

use event\ObjectEventHandler;
use event\UserEventHandler;
use event\DefaultEventHandler;
                                       
require_once __DIR__ . '/event/ObjectEventHandler.php';
require_once __DIR__ . '/event/UserEventHandler.php';
require_once __DIR__ . '/event/DefaultEventHandler.php';

class CmdEventDispatcher
{
    function __construct(){
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
}
  
?>
