<?php

class EventDispatcher
{
    private $event;
    function __construct($eventStr){
        $this->event = $eventStr;
    }

    function handleEvent(){

        $eventReactorClass = "{$this->event}_Handler";
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
