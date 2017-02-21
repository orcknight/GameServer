<?php

header("Content-Type: text/html; charset=utf-8");

use Workerman\Worker;
use Workerman\WebServer;
use Workerman\Autoloader;
use Workerman\Lib\Timer;
use PHPSocketIO\SocketIO;

// composer autoload
require_once __DIR__ . '/../vendor/autoload.php';
require_once __DIR__ . '/cmdEngine.php';


$io = new SocketIO(2020);
$cmdEngine = new cmdEngine();
$io->on('connection', function($socket) use($io){
    $socket->addedUser = false;
    
    /*$socket->timer_id = Timer::add(1, function()use($socket){

        $socket->emit("stream", "\n012\$5,5,28,45#杨萎:100/100:#000000║气血.100:100/100/100:#99FF0000:exert recover║内力.0:0/0/0:#990066FF║精神.100:100/100/100:#996600CC:exert regenerate║精力.0:0/0/200:#99006600║怒气.0:0/0:#99990000║食物.197:197/200:#99FF6600║饮水.197:197/200:#990000FF║经验.0:0/1000:#99FF0066║潜能.99:99/2901/3000:#99FF00FF\n↵");    

    }); */
    
    $socket->on('stream', function($msg) use($socket) {
        
        
        Global $cmdEngine;
        
        $replyTxt = $cmdEngine->Parse($msg, $socket);
        $socket->emit('stream', $replyTxt);
        return;

        
        if($msg == "\n"){
            
            $socket->emit('stream', "版本验证成功\r\n");    
            $socket->emit('stream', chr(13).chr(10). 
            "0000007" . chr(13).chr(10). 
            "015登录成功，正在加载世界。。。".chr(13).chr(10). 
            "目前权限：(player)" .chr(13).chr(10). 
            "006b12:[1;32m常用\$br#指令[2;37;0m:mycmds ofen\$zj#b13:[1;33m技能\$br#相关[2;37;0m:mycmds skill\$zj#b14:[1;31m战斗\$br#相关[2;37;0m:mycmds fight\$zj#b15:[1;35m任务\$br#相关[2;37;0m:mycmds quest\$zj#b16:[1;37m游戏\$br#指南[2;37;0m:mycmds help\$zj#b17:[1;36m频道\$br#交流[2;37;0m:liaotian" . chr(13).chr(10).
            "021 飞行 :help mapb\$zj# 附近 :map view" .chr(13).chr(10). 
            "───────────────────────────────" . chr(13).chr(10). 
            "你可以进入不同的方向选择品质和先天属性，然后就投胎做人了。" .chr(13).chr(10). 
            "───────────────────────────────" . chr(13).chr(10). 
            "你连线进入了拍拍熊专列[立志传一线]。" . chr(13).chr(10). 
            "002奈何桥" . chr(13).chr(10). 
            "004这里骨气森森，旁边有一个牌子，你也许应该仔细看看。" .chr(13).chr(10). 
            "003north:心狠手辣\$zj#south:狡黠多变\$zj#east:光明磊落\$zj#west:阴险奸诈" . chr(13).chr(10). 
            "005牛头:look /d/register/npc/diyun#8118014\$zj#马面:look /d/register/npc/shuisheng#8118011" . chr(13).chr(10). 
            "005【牌子】:look 【牌子】" . chr(13).chr(10));
        }
        

         

    });

    // when the client emits 'new message', this listens and executes
    $socket->on('new message', function ($data)use($socket){
        // we tell the client to execute 'new message'
        $socket->broadcast->emit('new message', array(
            'username'=> $socket->username,
            'message'=> $data
        ));
    });

    // when the client emits 'add user', this listens and executes
    $socket->on('add user', function ($username) use($socket){
        global $usernames, $numUsers;
        // we store the username in the socket session for this client
        $socket->username = $username;
        // add the client's username to the global list
        $usernames[$username] = $username;
        ++$numUsers;
        $socket->addedUser = true;
        $socket->emit('login', array( 
            'numUsers' => $numUsers
        ));
        // echo globally (all clients) that a person has connected
        $socket->broadcast->emit('user joined', array(
            'username' => $socket->username,
            'numUsers' => $numUsers
        ));
    });

    // when the client emits 'typing', we broadcast it to others
    $socket->on('typing', function () use($socket) {
        $socket->broadcast->emit('typing', array(
            'username' => $socket->username
        ));
    });

    // when the client emits 'stop typing', we broadcast it to others
    $socket->on('stop typing', function () use($socket) {
        $socket->broadcast->emit('stop typing', array(
            'username' => $socket->username
        ));
    });

    // when the user disconnects.. perform this
    $socket->on('disconnect', function () use($socket) {
        
        echo 'disconnect';
        
        if($socket->timer_id > 0){
            
            Timer::del($socket->timer_id);    
        }
        
        
        
        
        /*global $usernames, $numUsers;
        // remove the username from global usernames list
        if($socket->addedUser) {
            unset($usernames[$socket->username]);
            --$numUsers;

           // echo globally that this client has left
           $socket->broadcast->emit('user left', array(
               'username' => $socket->username,
               'numUsers' => $numUsers
            ));
        }*/
   });
   
});

if (!defined('GLOBAL_START')) {
    Worker::runAll();
}
