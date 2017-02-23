<?php

namespace event;

use bll\CacheManager;
                                       
require_once __DIR__ . '/../bll/CacheManager.php';

class BaseEventHandler {
    
    
    protected static $cacheManager = null;
    public static function getCacheManager(){
        
        if($cacheManager == null){
            
            $cacheManager = new CacheManager();
        }
        
        return $cacheManager;
    }
    
}

?>
