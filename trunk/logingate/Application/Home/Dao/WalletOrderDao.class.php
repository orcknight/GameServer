<?php

namespace Home\Dao;

use Think\Model;

class WalletOrderDao extends BaseDao{
    
    public function __construct($db){
        
        parent::__construct($db);
    }
    
    public function add($walletOrderArray){
        
        $this->trackLog("execute", "add()");
        
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
        $remark = isset($walletOrderArray['remark']) ? $walletOrderArray['remark'] : '';
        
        $sql = "INSERT INTO wallet_order(fromWalletId, toWalletId, money, type,
        orderType, businessId, orderId, remitter, remitterName, payee, payeeName, status, remark)VALUES(
        '$fromWalletId', '$toWalletId', '$money', '$type', '$orderType', '$businessId', '$orderId',
        '$remitter', '$remitterName', '$payee', '$payeeName', '$status', '$remark')";
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
    
    public function getFromWalletIdByMD5Id($md5Id){
        
        $this->trackLog("execute", "getFromWalletIdByMD5Id()");
        
        $sql = "SELECT fromWalletId FROM wallet_order WHERE md5(id) = '$md5Id'";
        $this->trackLog("sql", $sql);
        $toWalletId = $this->db->query($sql);
        $this->trackLog("fromWalletId", $toWalletId);
        
        return $toWalletId[0]['fromwalletid'];
    }
    
    public function getStatusByMD5Id($md5Id){
        
        $this->trackLog("execute", "getStatusByMD5Id()");
        
        $sql = "SELECT status FROM wallet_order WHERE md5(id) = '$md5Id'";
        $this->trackLog("sql", $sql);
        $status = $this->db->query($sql);
        $this->trackLog("status", $status);
        
        return $status[0]['status'];
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
    
    public function getByIds($ids){
        
        $this->trackLog("execute", "getByIds()");
        
        $querySql = "SELECT * FROM wallet_order WHERE id IN($ids) AND status = 1";
        $this->trackLog("querySql", $querySql);
        $result = $this->db->query($querySql);
        
        return $result;
    }
    
    public function getManualRechargeOrder($args){
        
        $this->trackLog("execute", "getManualRechargeOrder()");
        
        $start = $args['start'];
        $length = $args['length'];
        
        $querySql = "SELECT
        wo.id, wo.fromWalletId, wo.money, wo.type, c.companyName AS userName, c.phone AS phone,
        wo.orderId, wo.remitter,wo.remitterName,wo.payee,wo.payeeName,
        wo.orderType,wo.status,wo.createTime,wo.updateTime
        FROM wallet_order wo
        LEFT JOIN wallet w ON wo.toWalletId = w.id
        LEFT JOIN company c ON w.companyId = c.id 
        WHERE wo.orderId = -1 AND wo.status = 2 AND wo.orderType = 20
        ORDER BY wo.id DESC
        LIMIT $start,$length";
        $this->trackLog("querySql", $querySql);
        $result = $this->db->query($querySql);
        
        return $result;
    }
    
    public function getManualRechargeOrderCount($args){
        
        $this->trackLog("execute", "getManualRechargeOrderCount()");
        
        $querySql = "SELECT COUNT(*) AS count FROM wallet_order 
        WHERE orderId = -1 AND status = 2 AND orderType = 20";
        $this->trackLog("querySql", $querySql);
        $result = $this->db->query($querySql);
        $this->trackLog("result", $result);
        
        if(empty($result))
        {
            return 0;
        }
        return $result[0]['count'];
    }
    
    public function update($args){
        
        $this->trackLog("execute", "DAO-update()");   
        
        $setSql = '';
        $whereSql = '';
        if(isset($args['status'])){
           
            $setSql .= ('status=' . $args['status'] . ', ');
        }
        $this->trackLog("setSql", $setSql);
        if(strlen($setSql) > 0){
            
            $setSql = rtrim($setSql, ', ');
        }
        if(isset($args['cond_id'])){
           
            $whereSql .= ('id=' . $args['cond_id'] . ' AND ');
        }
        if(strlen($whereSql) > 0){
            
            $whereSql = rtrim($whereSql, ' AND ');
        }    
        
        $executeSql = "UPDATE wallet_order SET " . $setSql . " WHERE " . $whereSql;
        $this->trackLog("executeSql", $executeSql);        
        $result = $this->db->execute($executeSql);
        $this->trackLog("result", $result);
        
        return $result; 
        
    }
}

?>
