<?php

namespace event;

use bll\CacheManager;
                                       
require_once __DIR__ . '/../bll/CacheManager.php';

class BaseEventHandler {
    
    protected static $cacheManager = null;
    public static function getCacheManager(){
        
        if(self::$cacheManager == null){
            
            self::$cacheManager = new CacheManager();
        }
        
        return self::$cacheManager;
    }
    
    protected $socket = null;
    public function setSocket(&$socket){
        
        $this->socket = $socket;
    }
    
    public function set($name, $value){ 
        
        $this->$name = $value; 
    } 
    //__get()方法用来获取私有属性 
    public function get($name){ 
        
        return $this->$name; 
    } 
    
    
}

?>
