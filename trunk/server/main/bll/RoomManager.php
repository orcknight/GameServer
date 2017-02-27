<?php

namespace bll;

class RoomManager {
    
    private $roomMap = null;
    
    public function __construct(&$roomMap)
    {
        $this->roomMap = $roomMap;
    }
    
    public function getRoomInfo($name){

        $tileInfo = $this->roomMap[$name];
        $txt = "↵\r\n";
        $txt .= "002" . $tileInfo['cname'] . "\r\n";
        $txt .= "004" . $tileInfo['describe'] . "\r\n";
        $txt .= $this->buildARoundTxt($tileInfo);
        
        return $txt;    
        
    }
    
    private function buildARoundTxt($info){
        
        $contact = "\$zj#";
        $txt = '003';
        if(!empty($info['nname'])){
            
            $txt .= "north:" . $this->roomMap[$info['nname']]['cname'] . $contact;    
        }
        if(!empty($info['sname'])){
            
            $txt .= "south:" . $this->roomMap[$info['sname']]['cname'] . $contact;    
        }
        if(!empty($info['ename'])){
            
            $txt .= "east:" . $this->roomMap[$info['ename']]['cname'] . $contact;    
        }
        if(!empty($info['wname'])){
            
            $txt .= "west:" . $this->roomMap[$info['wname']]['cname'] . $contact;    
        }
        if(!empty($info['outname'])){
            
            $txt .= "out:" . $this->roomMap[$info['outname']]['cname'] . $contact;    
        }
        
        $txt = rtrim($txt, $contact);
        if(strlen($txt) < 5){
            
            return '';
        }
        $txt = $txt . "\r\n";

        return $txt;
    }
    
    
}
  
?>
