<?php

namespace Company\Service;

use Think\Model;
use Enum\JobPayEnum;
use Home\Service\JobEnum;

class WalletOrderService extends BaseService{

    private $db;
    
    //为了在多表关联操作时，支持事务操作，这里db统一采用注入方式从外部注入
    public function __construct($db){
        
        $this->db = $db;
    }
    
    public function Add($walletOrderArray){
        
        $this->trackLog("execute", "Add()");
        
        $fromWalletId = isset($walletOrderArray['fromWalletId']) ? $walletOrderArray['fromWalletId'] : '';
        $toWalletId = isset($walletOrderArray['toWalletId']) ? $walletOrderArray['toWalletId'] : '';
        $money = $walletOrderArray['money'];
        $type = $walletOrderArray['type'];
        $orderType = $walletOrderArray['orderType'];
        $businessId = isset($walletOrderArray['businessId']) ? $walletOrderArray['businessId'] : '';
        $orderId = isset($walletOrderArray['orderId']) ? $walletOrderArray['orderId'] : '';
        $remitter =  isset($walletOrderArray['remitter']) ? $walletOrderArray['remitter'] : '';
        $remitterName =  isset($walletOrderArray['reitterName']) ? $walletOrderArray['reitterName'] : '';
        $payee = isset($walletOrderArray['payee']) ? $walletOrderArray['payee'] : '';
        $payeeName = isset($walletOrderArray['payeeName']) ? $walletOrderArray['payeeName'] : '';
        $status = $walletOrderArray['status'];
        //生成流水号
        $serialName = '';
        if($orderType == 0 || $orderType = 20){
            $start = date("Y-m-d 00:00:00", strtotime());
            $end = date("Y-m-d 23:59:59", strtotime());
            $countSql = "SELECT count(*) as total FROM wallet_order WHERE orderType = $orderType AND createTime BETWEEN '$start' AND '$end'";
            $number = $this->db->execute($countSql);

            $number = $number[0]['total']+1;
            str_pad($number,4,0,STR_PAD_LEFT);
            $time = date('Ymd',strtotime($start));
            $serialName = $time.$number;
        }
        
        $sql = "INSERT INTO wallet_order(fromWalletId, toWalletId, money, type, 
        orderType, businessId, orderId, remitter, remitterName, payee, payeeName, status,serialNumber)VALUES(
        '$fromWalletId', '$toWalletId', '$money', '$type', '$orderType', '$businessId', '$orderId',
        '$remitter', '$remitterName', '$payee', '$payeeName', '$status','$serialName')";
        $this->trackLog("sql", $sql);
        $result = $this->db->execute($sql);
        $this->trackLog("result", $result);
        
        //mysql插入操作串行进行的，“SELECT last_insert_id() as id”是线程相关的所以获取的插入id是正确的
        $sql = "SELECT last_insert_id() as id";
        $this->trackLog("sql", $sql);
        $result = $this->db->query($sql);
        $this->trackLog('result', $result);
        
        return $result[0]['id'];  
    }
    
    public function getToWalletIdByMD5Id($md5Id){
        
        $this->trackLog("execute", "getToWalletIdByMD5Id()");
        
        $sql = "SELECT toWalletId FROM wallet_order WHERE md5(id) = '$md5Id'";
        $this->trackLog("sql", $sql);
        $toWalletId = $this->db->query($sql);
        $this->trackLog("toWalletId", $toWalletId);
        
        return $toWalletId[0]['towalletid'];
    }
    
    public function updateStatusAndOrderIdByMD5Id($md5Id, $transactionId, $status){
        
        $this->trackLog("execute", "updateStatusAndOrderIdByMD5Id()");
        
        $sql = "UPDATE wallet_order SET status = '$status', orderId = '$transactionId' WHERE MD5(id) = '$md5Id'";
        $this->trackLog("sql", $sql);
        $statusResult = $this->db->execute($sql);
        $this->trackLog("statusResult", $statusResult);
        
        return $statusResult;
    }
    
    public function setStatusById($id, $status){
        
        $this->trackLog("execute", "setStatusById()");
        
        $sql = "UPDATE wallet_order SET status = '$status' WHERE id = '$id'"; 
        $this->trackLog("sql", $sql);
        $result = $this->db->execute($sql);
        $this->trackLog("result", $result);
        
        return $result;   
    }
    
    
    
    
    
      
}
?>
