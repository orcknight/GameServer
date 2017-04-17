<?php

namespace model\Player;

class Player{
    
    private $id; //玩家id
    private $userId; //用户id
    private $userName; //用户名
    private $name; //玩家名称
    private $sex; //性别
    private $character; //性格
    private $level; //等级
    private $str; //膂力
    private $int; //悟性
    private $con; //根骨
    private $dex; //身法
    private $kar; //福缘
    private $per; //容貌
    private $sta; //耐力
    private $role; //
    
    public function set($name, $value){ 
        
        $this->$name = $value; 
    } 
    //__get()方法用来获取私有属性 
    public function get($name){ 
        
        return $this->$name; 
    }
}

?>
