<?php

namespace event;

use event\BaseEventHandler;
use bll\ObjectManager;
use bll\PlayerManager;

require_once __DIR__ . '/BaseEventHandler.php';
require_once __DIR__ . '/../bll/ObjectManager.php';
require_once __DIR__ . '/../bll/PlayerManager.php';

class MoveEventHandler extends BaseEventHandler{
    
    public function handle($msg){
        
        $cmd = rtrim($msg, "\n");
        $socket = $this->socket;
        
        $myPlayerInfo = self::$cacheManager->getPlayerRef($socket->userId);
        $roomName = $myPlayerInfo["roomName"];
        
        echo $roomName;
    
        if($cmd == "east"){
            
            return $this->moveto($roomName, "ename");
            $eastName = self::$cacheManager->getRoom($roomName)['ename'];
            echo $eastName;
            self::$cacheManager->setPlayerInfo($socket->userId, "roomName", $eastName);
            return $this->getTileInfoFromCache($eastName, $socket);
        }elseif($cmd == "south"){
            return $this->moveto($roomName, "sname");
            $southName = self::$cacheManager->getRoom($roomName)['sname'];
            self::$cacheManager->setPlayerInfo($socket->userId, "roomName", $southName);
            return $this->getTileInfoFromCache($southName, $socket);
        }elseif($cmd == "north"){
            return $this->moveto($roomName, "nname");
            $northName = self::$cacheManager->getRoom($roomName)['nname'];
            self::$cacheManager->setPlayerInfo($socket->userId, "roomName", $northName);
            return $this->getTileInfoFromCache($northName, $socket);
        }elseif($cmd == "west"){
            return $this->moveto($roomName, "wname");
            $westName = self::$cacheManager->getRoom($roomName)['wname'];
            self::$cacheManager->setPlayerInfo($socket->userId, "roomName", $westName);
            return $this->getTileInfoFromCache($westName, $socket);
        }elseif($cmd == "out"){
            return $this->moveto($roomName, "outname");
            $westName = self::$cacheManager->getRoom($roomName)['outname'];
            self::$cacheManager->setPlayerInfo($socket->userId, "roomName", $westName);
            return $this->getTileInfoFromCache($westName, $socket);
        }
        
    }
    
    private function moveto($roomName, $directName){
        
        $this->getPlayerManager()->updateLocation($roomName);

        $curRoom = self::$cacheManager->getRoom($roomName);
        $name = $curRoom[$directName];
        $directRoom = self::$cacheManager->getRoom($roomName);
        $this->getPlayerManager()->updateLocation($name);
        return $this->getTileInfoFromCache($name, $this->socket);
        
    }
    
    private function getTileInfoFromCache($name, &$socket){

        $tileInfo = self::$cacheManager->getRoom($name);
        $txt = "↵\r\n";
        $txt .= "002" . $tileInfo['cname'] . "\r\n";
        $txt .= "004" . $tileInfo['describe'] . "\r\n";
        $txt .= $this->buildARoundTxtByCache($tileInfo);
        $roomName = self::$cacheManager->getPlayerInfo($this->socket->userId, 'roomName');
        $txt .= $this->getObjectManager()->loadObject($roomName);
        
        return $txt;    
        
    }
    
    private function buildARoundTxtByCache($info){
        
        $contact = "\$zj#";
        $txt = '003';
        if(!empty($info['nname'])){
            
            $txt .= "north:" . self::$cacheManager->getRoom($info['nname'])['cname'] . $contact;    
        }
        if(!empty($info['sname'])){
            
            $txt .= "south:" . self::$cacheManager->getRoom($info['sname'])['cname'] . $contact;    
        }
        if(!empty($info['ename'])){
            
            $txt .= "east:" . self::$cacheManager->getRoom($info['ename'])['cname'] . $contact;    
        }
        if(!empty($info['wname'])){
            
            $txt .= "west:" . self::$cacheManager->getRoom($info['wname'])['cname'] . $contact;    
        }
        
        if(!empty($info['outname'])){
            
            $txt .= "out:" . self::$cacheManager->getRoom($info['outname'])['cname'] . $contact;    
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
    
    private $playerManager = null;
    private function getPlayerManager(){
        
        if($this->playerManager == null){
            
            $payerInfo = &self::$cacheManager->getPlayerRef($this->socket->userId);
            
            $this->playerManager = new PlayerManager($payerInfo);
        }
        
        return  $this->playerManager;
    }
    
}

?>
