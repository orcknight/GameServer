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
        
        $roomName = self::$cacheManager->getPlayerInfo($socket->userId, "roomName");
        
        echo $roomName;
    
        if($cmd == "east"){
            
            $eastName = $this->getCacheManager()->getTileMap()[$roomName]['ename'];
            echo $eastName;
            self::$cacheManager->setPlayerInfo($socket->userId, "roomName", $eastName);
            return $this->getTileInfoFromCache($eastName, $socket);
        }elseif($cmd == "south"){
            $southName = $this->getCacheManager()->getTileMap()[$roomName]['sname'];
            self::$cacheManager->setPlayerInfo($socket->userId, "roomName", $southName);
            return $this->getTileInfoFromCache($southName, $socket);
        }elseif($cmd == "north"){
            $northName = $this->getCacheManager()->getTileMap()[$roomName]['nname'];
            self::$cacheManager->setPlayerInfo($socket->userId, "roomName", $northName);
            return $this->getTileInfoFromCache($northName, $socket);
        }elseif($cmd == "west"){
            $westName = $this->getCacheManager()->getTileMap()[$roomName]['wname'];
            self::$cacheManager->setPlayerInfo($socket->userId, "roomName", $westName);
            return $this->getTileInfoFromCache($westName, $socket);
        }elseif($cmd == "out"){
            $westName = $this->getCacheManager()->getTileMap()[$roomName]['outname'];
            self::$cacheManager->setPlayerInfo($socket->userId, "roomName", $westName);
            return $this->getTileInfoFromCache($westName, $socket);
        }
        
    }
    
    private function moveto($cityName, $roomName, $directName){

        $curRoom = self::$cacheManager->getRoom($cityName, $roomName);
        $name = $curRoom[$directName];
        $directRoom = self::$cacheManager->getRoom($cityName, $roomName);
        self::$cacheManager->setPlayerInfo($this->socket->userId, "roomName", $name);
        return $this->getTileInfoFromCache($name, $this->socket);
        
    }
    
    
    private function getTileInfoFromCache($name, &$socket){
        
        $myPlayerInfo = &$this->getPlayerInfo();

        $tileInfo = $this->getCacheManager()->getTileMap()[$name];
        $txt = "↵\r\n";
        $txt .= "002" . $tileInfo['cname'] . "\r\n";
        $txt .= "004" . $tileInfo['describe'] . "\r\n";
        $txt .= $this->buildARoundTxtByCache($tileInfo);
        $cityName = self::$cacheManager->getPlayerInfo($this->socket->userId, 'cityName');
        $roomName = self::$cacheManager->getPlayerInfo($this->socket->userId, 'roomName');
        $txt .= $this->getObjectManager()->loadObject($cityName, $roomName);
        
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
    
    private function &getPlayerInfo()
    { 
        $temp = $this->socket->playerInfo;
        return $temp;
    }
    
    
}

?>
