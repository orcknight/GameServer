<?php

namespace Company\Service;

use Think\Model;
use Think\Exception;
use Company\Dao\CompanyDao;
use Home\Dao\WalletDao;

class CompanyService extends BaseService{
    
    private $db;
    private $companyDao;
    private $walletDao;
    
    public function __construct() {
        
        $this->db = new Model();
        $this->companyDao = new CompanyDao($this->db);
        $this->walletDao = new WalletDao($this->db);
    }
    
    public function signUp($args){
        
        $this->trackLog("execute", "signUp()");
        
        if(!is_numeric($args['phone']) || empty($args['password']) ||empty($args['agree'])) {
            $data['code']  = 0;
            $data['msg']  = "参数格式不正确！";
            return $data;
        }
        if ($args['phone'] != session('captchaPhone') || $args['captcha'] != session('captcha')) {
            $data['code']  = 0;
            $data['msg']  = "手机验证码错误！";
            return $data;
        }
        
        $result = $this->companyDao->query(array( 'phone' => $args['phone'] ));
        if($result)
        {
            $data['code']  = 0;
            $data['msg']  = "非常遗憾，手机号已经被注册啦！";
            return $data;
        }
        
        try{
            
            $this->db->startTrans();
            $companyId = $this->companyDao->add($args);
            if($companyId < 1){
                
                $this->db->rollback();
                $data['code']  = 0;
                $data['msg']  = "企业创建失败啦！";
                return $data;
            }
            
            $walletId = $this->walletDao->add(array( 'companyId' => $companyId ));
            if($walletId < 1){
                
                $this->db->rollback();
                $data['code']  = 0;
                $data['msg']  = "企业创建失败啦！";
                return $data;
            }
            
            $result = $this->companyDao->query(array( 'id' => $companyId ));
            if(!$result){
                
                $this->db->rollback();
                $data['code']  = 0;
                $data['msg']  = "企业创建失败啦！";
                return $data;
            }

            $user = $result[0];
            session('x_company', $user);
            $this->db->commit();
            $data['code']  = 1;
            $data['msg']  = "企业注册成功啦！";
            return $data;
            
        }catch(Exception $e){
            
            $this->db->rollback();    
            $data['code']  = 0;
            $data['msg']  = "企业创建失败啦！";
            return $data;
        }
    }
    
    public function pcSignUp($args){
        
        $this->trackLog("execute", "pcSignUp()");
        
        if(!is_numeric($args['phone']) || empty($args['password'])) {
            $data['code']  = 0;
            $data['msg']  = "参数格式不正确！";
            return $data;
        }
        
        $result = $this->companyDao->query(array( 'phone' => $args['phone'] ));
        if($result)
        {
            $data['code']  = 0;
            $data['msg']  = "非常遗憾，手机号已经被注册啦！";
            return $data;
        }
        
        try{
            
            $this->db->startTrans();
            $companyId = $this->companyDao->add($args);
            if($companyId < 1){
                
                $this->db->rollback();
                $data['code']  = 0;
                $data['msg']  = "企业创建失败啦！";
                return $data;
            }
            
            $walletId = $this->walletDao->add(array( 'companyId' => $companyId ));
            if($walletId < 1){
                
                $this->db->rollback();
                $data['code']  = 0;
                $data['msg']  = "企业创建失败啦！";
                return $data;
            }
            
            $this->db->commit();
            $data['code']  = 1;
            $data['msg']  = "企业注册成功啦！";
            return $data;
            
        }catch(Exception $e){
            
            $this->db->rollback();    
            $data['code']  = 0;
            $data['msg']  = "企业创建失败啦！";
            return $data;
        }    
    }
}

?>
