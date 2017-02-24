<?php

namespace event;

use event\BaseEventHandler;
use bll\ObjectManager;

require_once __DIR__ . '/BaseEventHandler.php';
require_once __DIR__ . '/../bll/ObjectManager.php';

class MoveEventHandler extends BaseEventHandler{
    
    public function handle($msg){
        
        $cmd = rtrim($msg, "\n");
        $socket = $this->socket;
    
        if($cmd == "east"){
            
            $eastName = $this->getCacheManager()->getTileMap()[$socket->tileName]['ename'];
            echo $eastName;
            $socket->tileName = $eastName;
            return $this->getTileInfoFromCache($eastName, $socket);
        }elseif($cmd == "south"){
            $southName = $this->getCacheManager()->getTileMap()[$socket->tileName]['sname'];
            $socket->tileName = $southName;
            return $this->getTileInfoFromCache($southName, $socket);
        }elseif($cmd == "north"){
            $northName = $this->getCacheManager()->getTileMap()[$socket->tileName]['nname'];
            $socket->tileName = $northName;
            return $this->getTileInfoFromCache($northName, $socket);
        }elseif($cmd == "west"){
            $westName = $this->getCacheManager()->getTileMap()[$socket->tileName]['wname'];
            $socket->tileName = $westName;
            return $this->getTileInfoFromCache($westName, $socket);
        }elseif($cmd == "out"){
            $westName = $this->getCacheManager()->getTileMap()[$socket->tileName]['outname'];
            $socket->tileName = $westName;
            return $this->getTileInfoFromCache($westName, $socket);
        }
        
    }
    
    
    private function getTileInfoFromCache($name, &$socket){

        $tileInfo = $this->getCacheManager()->getTileMap()[$name];
        $txt = "↵\r\n";
        $txt .= "002" . $tileInfo['cname'] . "\r\n";
        $txt .= "004" . $tileInfo['describe'] . "\r\n";
        $txt .= $this->buildARoundTxtByCache($tileInfo);
        $txt .= $this->getObjectManager()->loadObject($socket->cityName, $socket->tileName);
        
        return $txt;    
        
    }
    
    private function buildARoundTxtByCache($info){
        
        $contact = "\$zj#";
        $txt = '003';
        if(!empty($info['nname'])){
            
            $txt .= "north:" . $this->getCacheManager()->getTileMap()[$info['nname']]['cname'] . $contact;    
        }
        if(!empty($info['sname'])){
            
            $txt .= "south:" . $this->getCacheManager()->getTileMap()[$info['sname']]['cname'] . $contact;    
        }
        if(!empty($info['ename'])){
            
            $txt .= "east:" . $this->getCacheManager()->getTileMap()[$info['ename']]['cname'] . $contact;    
        }
        if(!empty($info['wname'])){
            
            $txt .= "west:" . $this->getCacheManager()->getTileMap()[$info['wname']]['cname'] . $contact;    
        }
        
        if(!empty($info['outname'])){
            
            $txt .= "out:" . $this->getCacheManager()->getTileMap()[$info['outname']]['cname'] . $contact;    
        }
        
        $txt = rtrim($txt, $contact);
        if(strlen($txt) < 5){
            
            return '';
        }
        $txt = $txt . "\r\n";

        return $txt;
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
