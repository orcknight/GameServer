<?php
  
namespace dao;

use DbHelper;

require_once __DIR__ . '/../db/DbHelper.php';

class TileDao{
    
    public function queryTile($name){
        
        $db = new DbHelper();
        $querySql = "SELECT * FROM tile WHERE name = '$name'"; 
        $result = $db->query($querySql); 
        if(!$result){
            
            return null;
        }
        
        return $result[0];
        
    }
    
    public function loadTileToCache(){
        
        $mapArray = array();
        $db = new DbHelper();
        $querySql = "SELECT * FROM tile"; 
        $result = $db->query($querySql); 
        if(!$result){
            
            return $mapArray;
        }
        
        for($i = 0; $i < sizeof($result); $i++){
            
            $item = $result[$i];
            $mapArray[$item['name']] = $item;
        }
        unset($result);
        
        return $mapArray;    
        
    }

    public function buildTileTxt($name){
        
        $tileInfo = $this->queryTile($name);
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
            
            $txt .= "north:" . $this->queryTile($info['nname'])['cname'] . $contact;    
        }
        if(!empty($info['sname'])){
            
            $txt .= "south:" . $this->queryTile($info['sname'])['cname'] . $contact;    
        }
        if(!empty($info['ename'])){
            
            $txt .= "east:" . $this->queryTile($info['ename'])['cname'] . $contact;    
        }
        if(!empty($info['wname'])){
            
            $txt .= "west:" . $this->queryTile($info['wname'])['cname'] . $contact;    
        }
        
        $txt = rtrim($txt, $contact);
        $txt = $txt . "\r\n";
        
        return $txt;
    }
}

?>
