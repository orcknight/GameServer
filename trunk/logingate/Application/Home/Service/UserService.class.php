<?php

namespace Home\Service;

use Think\Model;
use Think\Exception;
use Home\Dao\ChiefDao;
use Home\Dao\UserDao;
use Home\Dao\WalletDao;
use Home\Dao\WalletOrderDao;
use Home\Dao\ChiefCodeDao;
use Home\Dao\ChiefFansDao;
use Home\Dao\AdminUserDao;
use Home\Dao\ThirdLevelUserMapDao;
use Enum\UserEnum;
use Enum\WalletEnum;
use Enum\ThirdLevelEnum;

class UserService extends BaseService {
    
    private $db;
    private $chiefDao;
    private $userDao;
    private $walletDao;
    private $walletOrderDao;
    private $chiefCodeDao;
    private $chiefFansDao;
    
    public function __construct() {
        
        $this->db = new Model();
        $this->chiefDao = new ChiefDao($this->db);
        $this->userDao = new UserDao($this->db);
        $this->walletDao = new WalletDao($this->db);
        $this->walletOrderDao = new WalletOrderDao($this->db);
        $this->chiefCodeDao = new ChiefCodeDao($this->db);
        $this->chiefFansDao = new ChiefFansDao($this->db);
    }

    public function signUp($args){
        
        $this->trackLog("execute", "signUp()");
        
        if(empty($args['name']) || empty($args['pass']) || 
           empty($args['email']) || empty($args['phone'])){
            
            $data['code'] = 0;
            $data['msg']  = "所有字段不能为空！";
            return $data;    
        }
        
        if($args['pass'] != $args['pass2']){
            
            $data['code'] = 0;
            $data['msg']  = "两次输入的密码不一样！";
            return $data;      
        }

        $userDao = new UserDao($this->db);
        if($userDao->isDuplicate($args['name'])){
            
            $data['code'] = 0;
            $data['msg']  = "该名字已经被注册！";
            return $data;     
        }
        
        $args['pass'] = md5($args['pass']);
        $userId = $userDao->add($args);
        if($userId < 1){
            
            $data['code'] = 0;
            $data['msg']  = "注册失败！";
            return $data;     
        }
        
        $data['code'] = 1;
        $data['msg']  = "注册成功！";
        return $data;
    }
    
    private function verifyName($name){
        
        if(preg_match('/^[A-Z][a-z\d\_]{5,15}$/i', $name)){
            
            return true;
        }else{
            
            return false;
        }
    }
    
}

?>
