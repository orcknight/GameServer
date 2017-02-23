<?php
  
namespace event;

use event\BaseEventHandler;
use dao\UserDao;     
use dao\PlayerDao;
use dao\TileDao;
use dao\NpcDao;

require_once __DIR__ . '/BaseEventHandler.php';
require_once __DIR__ . '/../dao/UserDao.php';
require_once __DIR__ . '/../dao/PlayerDao.php';
require_once __DIR__ . '/../dao/TileDao.php';
require_once __DIR__ . '/../dao/NpcDao.php';

class UserEventHandler extends BaseEventHandler{
    
    public function handle($msg){  
        
        $msg = explode("\\", $msg)[1];
        
        echo $msg;
    
        if(3 == substr_count($msg, "║")){
            
            $name = explode("║", $msg)[0];
            $password = explode("║", $msg)[1];
            
            
            $userId = $this->getUserDao()->getUserId($name, $password);
            if($userId < 1){
                
                return '';
            }
            
            $this->socket->userId = $userId;
            
            //客户端连接检查，如果多个账号连接，发送下线消息并关闭连接
            $this->closeAndKickOffInfo($userId);
            
            //存储新连接到系统套接字缓存中
            self::$cacheManager->setSocketMap($userId, $this->socket);
            
            //检查是否创建了角色
            $player = $this->getPlayerDao()->queryPlayer($userId);
            if(!$player){
                
                return "\r\n0000008\r\n";
            }
            
            $playerInfo = $this->getPlayerDao()->queryPlayerInfo($player['id']);
            $this->socket->tileName = $playerInfo['tileName'];
            $this->socket->cityName = $playerInfo['cityName'];
            echo $this->socket->tileName;
            
            return chr(13).chr(10). 
            "0000007" . chr(13).chr(10). 
            "015登录成功，正在加载世界。。。".chr(13).chr(10). 
            "目前权限：(player)" .chr(13).chr(10). 
            "006b12:[1;32m常用\$br#指令[2;37;0m:mycmds ofen\$zj#b13:[1;33m技能\$br#相关[2;37;0m:mycmds skill\$zj#b14:[1;31m战斗\$br#相关[2;37;0m:mycmds fight\$zj#b15:[1;35m任务\$br#相关[2;37;0m:mycmds quest\$zj#b16:[1;37m游戏\$br#指南[2;37;0m:mycmds help\$zj#b17:[1;36m频道\$br#交流[2;37;0m:liaotian" . chr(13).chr(10).
            "021 飞行 :help mapb\$zj# 附近 :map view" .chr(13).chr(10). 
            "你连线进入了拍拍熊专列[立志传一线]。" . chr(13).chr(10). 
            $this->getTileInfoFromCache($playerInfo['tileName'], $this->socket);
            
        }elseif(substr_count($msg, "║001║") == 1){
            
            $msgArray = explode("║001║", $msg);
            $mySex = $msgArray[0];
            $myName = $msgArray[1];
            
            if(!preg_match("/^[\x{4e00}-\x{9fa5}]+$/u", $myName)){
                
                return "\r\n015对不起，请您用「中文」取名字(2-6个字)。\r\n";
                
            }

            if(mb_strlen($myName, 'utf-8') - 1 < 2 || mb_strlen($myName, 'utf-8') - 1 > 6){
                
                return "\r\n015对不起，你的中文姓名不能太长或太短(2-6个字)。\r\n";    
            }
            
            //通过检查，设置默认的血量等数值
            $playerId = $this->getPlayerDao()->addPlayer($this->socket->userId, $myName, $mySex);
            $playerInfo = $this->getPlayerDao()->addPlayerInfo($playerId, "shengmingzhigu");
            
            //发送↵0000007开始显示页面
            $socket->emit('stream', chr(13).chr(10) . "0000007" . chr(13).chr(10));
            
            //准备权限
            $retMsg = "\r\n目前权限：(player)\r\n" .
            "时间过得真快，不知不觉你已经十四岁了，今年的运气不知道怎么样。\r\n".
            "006b12:[1;32m常用\$br#指令[2;37;0m:mycmds ofen\$zj#b13:[1;33m技能\$br#相关[2;37;0m:mycmds skill\$zj#b14:[1;31m战斗\$br#相关[2;37;0m:mycmds fight\$zj#b15:[1;35m任务\$br#相关[2;37;0m:mycmds quest\$zj#b16:[1;37m游戏\$br#指南[2;37;0m:mycmds help\$zj#b17:[1;36m频道\$br#交流[2;37;0m:liaotian\r\n" .
            "021 飞行 :help mapb\$zj# 附近 :map view\r\n" . 
            "───────────────────────────────\r\n" .
            "你可以进入不同的方向选择品质和先天属性，然后就投胎做人了。\r\n" .
            "───────────────────────────────\r\n" . 
            "你连线进入了武林群侠[合一]。\r\n";

            $socket->tileName = "shengmingzhigu";
            $socket->cityName = "register";
            return $retMsg . $this->getTileInfoFromCache("shengmingzhigu", $this->socket);
            
        }
        
        return '';
        
    }
    
    private function closeAndKickOffInfo($userId){
        
        $preSocket = self::getCacheManager()->getSocketById($userId);
        if($preSocket != null){
            
            echo 'disconnect '. $userId . "\n";
            
            $preSocket->emit('stream', "你的账号在别处登录，你被迫下线了！" . chr(13) . chr(10) . 
            "与服务器断开连接。" . chr(13) . chr(10));
            
            if($preSocket->timer_id > 0){
        
                Timer::del($preSocket->timer_id);    
            }
            
            $preSocket->disconnect(true);   
        }    
    }
    
    private $userDao = null;
    private $playerDao = null;
    private $tileDao = null;
    private $npcDao = null;
    private $objectManager = null;
    
    private function getUserDao(){
    
        if($this->userDao == null){
            
            $this->userDao = new UserDao();
        }
        
        return  $this->userDao;
        
    }
    
    private function getPlayerDao(){
        
        if($this->playerDao == null){
            
            $this->playerDao = new PlayerDao();
        }
        
        return  $this->playerDao;

    }
    
    private function getTileDao(){
        
        if($this->tileDao == null){
            
            $this->tileDao = new TileDao();
        }
        
        return  $this->tileDao;    
    }
    
    private function getNpcDao(){
        
        if($this->npcDao == null){
            
            $this->npcDao = new NpcDao();
        }
        
        return  $this->npcDao;     
    }
    
    
    
    
    
}

?>
