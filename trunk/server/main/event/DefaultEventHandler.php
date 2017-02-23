<?php

namespace event;

use event\BaseEventHandler;

require_once __DIR__ . '/BaseEventHandler.php';

class DefaultEventHandler extends BaseEventHandler{
    
    public function handle($msg){
    
        if($msg == "\n"){
            
            return "版本验证成功\r\n";
        }else{
            
            return "";
        }   
        
    }
    
    
}

?>
