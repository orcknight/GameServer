<?php

namespace bll;

use Dao\PlayerDao;

require_once __DIR__ . '/../dao/PlayerDao.php';

class PlayerManager {
    
    private $playerInfo = null;
    private $playerDao = null;
     
    public function __construct(&$info)
    {
        $this->playerInfo = &$info;
    }
    
    public function setPlayerInfo(&$info){
        
        $this->playerInfo = &$info;
    }
    
    public function updateLocation($roomName){
        
        $this->playerInfo['roomName'] = $roomName;
        
        echo $this->playerInfo['roomName'] . "\n";
        
        if(explode('/', $roomName)[0] == "register"){
            
            $this->updatePlayerCharacter($roomName);
        }
        
        $this->getPlayerDao()->updatePlayerLocation($this->playerInfo['playerId'], $roomName);
    }
    
    public function setPianShu($pianshu){
        
        $txt = "\r\n你跳入忘忧池，顿时被一股激流卷了进去。\r\n";
        
        $this->playerInfo['pianshu'] = $pianshu;    
    }
    
    private function getPlayerDao(){
        
        if($this->playerDao == null){
            
            $this->playerDao = new PlayerDao();
        }
        
        return  $this->playerDao;     
    }
    
    private function updatePlayerCharacter($roomName){
        
        $characterArray = ['register/guangmingleiluo' => '光明磊落', 'register/yinxianjiaozha' => '阴险狡诈', 
        'register/jiaojieduobian' => '狡黠多变', 'register/xinhenshoula' => '心狠手辣'];
        
        if(array_key_exists($roomName, $characterArray)){
            
            $this->getPlayerDao()->updatePlayerCharacter($this->playerInfo['id'], $characterArray[$roomName]);
            $this->playerInfo['character'] =  $characterArray[$roomName];
            
            echo  'character' . $this->playerInfo['character'] . "\n";
        }
    }
    
    /*
    private function doWash($arg)
    {
            int i;
            int points;
            $tmpstr, tmpint, tmpcon, tmpdex;
            object me;
            mapping my;

            if (! objectp(me = this_player()) ||
                ! userp(me))
                    return 1;

            write(HIC "你跳入忘忧池，顿时被一股激流卷了进去。\n" NOR, me);

            tmpstr = tmpint = tmpcon = tmpdex = 13;

            switch(query("type", me) )
            {
            case "猛士型":
                    tmpstr = 20;
                    break;

            case "智慧型":
                    tmpint = 20;
                    break;

            case "耐力型":
                    tmpcon = 20;
                    break;

            case "敏捷型":
                    tmpdex = 20;
                    break;

            default:
                    break;
            }

            points = 80 - (tmpstr + tmpint + tmpcon + tmpdex);
            for (i = 0; i < points; i++) {
                    switch (random(4)) {
                    case 0: if (tmpstr < 30) tmpstr++; else i--; break;
                    case 1: if (tmpint < 30) tmpint++; else i--; break;
                    case 2: if (tmpcon < 30) tmpcon++; else i--; break;
                    case 3: if (tmpdex < 30) tmpdex++; else i--; break;
                    }
            }

            my = me->query_entire_dbase();

            my["str"] = tmpstr;
            my["int"] = tmpint;
            my["con"] = tmpcon;
            my["dex"] = tmpdex;
            my["kar"] = 10 + random(21);
            my["per"] = 10 + random(21);

            write(HIC "“啪”的一声，你被湿漉漉的抛了出来。\n" NOR, me);
            write(sprintf(HIY "\n你这次获得的四项先天天赋分别是：\n"
                              "膂力：【 " HIG "%d" HIY " 】 "
                              "悟性：【 " HIG "%d" HIY " 】 "
                              "根骨：【 " HIG "%d" HIY " 】 "
                              "身法：【 " HIG "%d" HIY " 】\n"
                              "如果你满意，就去投胎(born)吧！\n\n" NOR,
                          tmpstr, tmpint, tmpcon, tmpdex));
            set_temp("washed", 1, me);

            return 1;
    } */
}

?>
