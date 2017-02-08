<?php

namespace Home\Service;

use Think\Model;
use Think\Exception;
use Home\Dao\WalletDao;
use Home\Dao\WalletOrderDao;
use Enum\WalletEnum;


class WalletService extends BaseService {
    
    private $db;
    private $walletOrderDao;
    
    public function __construct() {
        
        $this->db = new Model();
        $this->walletOrderDao = new WalletOrderDao($this->db);
    }
    
    public function cancelRecharge($args){
        
        $this->trackLog("execute", "Service-cancelRecharge()");
        
        if($args['id'] < 1){
            
            $data['code'] =  0;
            $data['msg'] = "订单号错误！";
            return $data;
        }
        
        $result = $this->walletOrderDao->update(array('cond_id' => $args['id'], 'status' => $args['status']));
        if(!$result){
            
            $data['code'] =  0;
            $data['msg'] = "取消失败！";
            return $data;
        }
        
        $data['code'] =  1;
        $data['msg'] = "取消成功！";
        return $data;
    }
    
}

?>
