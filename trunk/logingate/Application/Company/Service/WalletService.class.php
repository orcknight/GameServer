<?php
namespace Company\Service;

use Think\Model;
use Enum\JobPayEnum;

class WalletService extends BaseService{
    
    private $db;
    
    //为了在多表关联操作时，支持事务操作，这里db统一采用注入方式从外部注入
    public function __construct($db){
        
        $this->db = $db;
    }

    /**
    * 检查账户余额，如果余额不足，无法进行交易
    * 
    * @param mixed $userId 用户id，isCompany为true代表这是companyId
    * @param mixed $totalAmount 要从账户扣掉的总金额
    * @param mixed $isCompany 是否是企业用户
    */
    public function checkBalance($userId, $totalAmount, $isCompany = false){
        
        $this->trackLog("execute", "checkBalance()");
        
        $sql = "SELECT money FROM wallet WHERE"; 
        if($isCompany){
            
            $sql .= " companyId = '$userId'"; 
        }else{
            
            $sql .= " userId = '$userId'";
        }
        
        $this->trackLog("sql", $sql);
        $money = $this->db->query($sql);
        $this->trackLog("money", $money); 
        $money = $money[0]['money'];
        
        if($money - $totalAmount >= 0){
            
            return true;
        }else{
            
            return false;
        }   
    }
    
    public function checkPassword($userId, $md5Password, $isCompany = false){
        
        $this->trackLog("execute", "checkPassword()");
        
        $sql = "SELECT password FROM wallet WHERE"; 
        if($isCompany){
            
            $sql .= " companyId = '$userId'"; 
        }else{
            
            $sql .= " userId = '$userId'";
        }
        
        $this->trackLog("sql", $sql);
        $password = $this->db->query($sql);
        $this->trackLog("password", $password); 
        if($md5Password == $password[0]['password']){
            
            return true;
        }else{
            
            return false;
        } 
    }
    
    public function addMoney($walletId, $amount){
        
        $this->trackLog("execute", "addMoney()");
        
        $sql = "UPDATE wallet SET money = money+'$amount' WHERE id = '$walletId'" ;
        $this->trackLog("sql", $sql);
        $result = $this->db->execute($sql); 
        $this->trackLog("result", $result);
        
        return $result; 
    }
    
    public function getWalletIdByUserId($userId, $isCompany = false){
        
        $this->trackLog("execute", "getWalletIdByUserId()");
        
        $sql = "SELECT id FROM wallet WHERE"; 
        if($isCompany){
            
            $sql .= " companyId = '$userId'"; 
        }else{
            
            $sql .= " userId = '$userId'";
        }
        
        $this->trackLog("sql", $sql);
        $walletId = $this->db->query($sql);
        $this->trackLog("walletId", $walletId); 
        
        return $walletId[0]['id'];    
    }
    
    public function Add($userId, $money, $gains, $income, $status, $isCompany = false){
        
        $this->trackLog("execute", "Add()");
        
        $sql = "";
        if($isCompany){
            
            $sql .= "INSERT INTO wallet(companyId,money,gains,income,status) VALUES('$userId','$money','$gains','$income','$status')"; 
        }else{
            
            $sql .= "INSERT INTO wallet(userId,money,gains,income,status) VALUES('$userId','$money','$gains','$income','$status')";
        }
        
        $this->trackLog("sql", $sql);
        $result = $this->db->execute($sql);
        $this->trackLog("result", $result);
        
        return $result;
    }
    
}

?>
