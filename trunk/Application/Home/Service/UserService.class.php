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
    public function PcsignIn($args){
        $this->trackLog("execute", "PcsignIn()");

        try{

            //插入用户并创建session
            $this->db->startTrans();
            $userId = $this->userDao->add($args);
            if($userId < 0){

                $this->db->rollback();
                $data['code']  = 0;
                $data['msg']  = "用户创建失败啦！";
                return $data;
            }
            $user = $this->userDao->query(array( 'id' => $userId ));

            //给用户创建钱包
            $walletResult = $this->addWallet(array("userId" => $userId));
            if($walletResult['code'] == 0){
                $this->db->rollback();
                $data['code']  = 0;
                $data['msg']  = "用户创建失败啦！";
                return $data;
            }
            $walletId = $walletResult['data'];

            $this->db->commit();
            session('x_user', $user[0]);
            $data['code']  = 1;
            $data['msg']  = "用户注册成功啦！";
            return $data;

        }catch(Exception $e){

            $this->db->rollback();
            $data['code']  = 0;
            $data['msg']  = "用户创建失败啦！";
            return $data;
        }
    }


    public function signUp($args){
        
        $this->trackLog("execute", "signUp()");
        
        //添加测试号码段
        if(!isWhitePhoneNumber($args['phone'])){
            
            //查重
            $result = $this->userDao->query(array("phone" => $args['phone']));
            if($result){
                
                $data['code'] = 0;
                $data['msg']  = "该手机号已经注册过了！";
                return $data;    
            }
        }
        
        try{
            
            //插入用户并创建session
            $this->db->startTrans();
            $userId = $this->userDao->add($args);
            if($userId < 0){
                
                $this->db->rollback();
                $data['code']  = 0;
                $data['msg']  = "用户创建失败啦！";
                return $data;     
            }
            $user = $this->userDao->query(array( 'id' => $userId ));
            
            //给用户创建钱包
            $walletResult = $this->addWallet(array("userId" => $userId));
            if($walletResult['code'] == 0){
                $this->db->rollback();
                $data['code']  = 0;
                $data['msg']  = "用户创建失败啦！";
                return $data;    
            }
            $walletId = $walletResult['data'];

            //绑定加盟商和头领，并且增加推广收益
            if(!$this->bindChiefAndAdmin(array( 'openid' => $args['openid'], 'userId' => $userId, 
            'walletId' => $walletId, 'inviteCode' => $args['chiefInviteCode'], 'tel' => $args['phone'] )))
            {
                $this->db->rollback();
                $data['code']  = 0;
                $data['msg']  = "用户创建失败啦！"; 
                return $data;    
            }
            
            $this->db->commit();
            session('x_user', $user[0]);
            $data['code']  = 1;
            $data['msg']  = "用户注册成功啦！";
            return $data;
            
        }catch(Exception $e){
            
            $this->db->rollback();
            $data['code']  = 0;
            $data['msg']  = "用户创建失败啦！";
            return $data;    
        }
    }
    
    private function bindChiefAndAdmin($args){
        
        $this->trackLog("execute", "bindChiefAndAdmin()");
        
        $openid = $args['openid'];
        $userId = $args['userId'];
        $walletId = $args['walletId'];
        $inviteCode = $args['inviteCode'];
        $tel = $args['tel'];
        $codeChiefId = 0;
        $openidChiefId = 0;
        
        //如果有邀请码，直接从邀请码去获取取chiefId
        if(!empty($inviteCode)){
            
            $codeChiefId = $this->chiefCodeDao->getChiefIdByInviteCode($inviteCode);
        }
        //获取openid的chiefId
        $fansResult = $this->chiefDao->queryFans(array("openid" => $openid, "bind" => 0));   
        if(!$fansResult){
            
            $openidChiefId = 0;
        }else{
            
            $openidChiefId = $fansResult[0]['chiefid'];    
        }
        
        //没有头领直接返回成功
        if($codeChiefId < 1 && $openidChiefId < 1){ 
        
            return true;
        }
        
        //根据条件分配chiefId
        if($codeChiefId > 0){
            
            //如果两边获取的头领不一样,减掉旧头领的粉丝数,然后增加新头领的粉丝数
            if($openidChiefId > 0 && $codeChiefId != $openidChiefId){
                
                $this->chiefDao->changeNumberById($openidChiefId, -1);
                
            }
            if($codeChiefId != $openidChiefId){
                
                $this->chiefDao->changeNumberById($codeChiefId, 1);
            }
            
            $chiefId = $codeChiefId;
        }else{
            
            $chiefId = $openidChiefId;
            //根据chiefId获取inviteCode
            $inviteCode = $this->chiefCodeDao->getInviteCodeByChiefId($chiefId);
        }
        
        //存储关系到三级分销关系
        if(!$this->addThirdLevelMap($userId, $chiefId, $inviteCode, ThirdLevelEnum::LEVEL_CHIEF_USER)){
            
            return false;
        }
        
        //存在头领
        //1.更新粉丝绑定状态
        //Todo有时候获取不到，通过wxid来关联
        $fansResult = $this->chiefFansDao->setChiefId(array("openid" => $args['openid'], "chiefId" => $chiefId));
        //2.累加头领人数
        $chiefResult = $this->chiefDao->changeRegisterById($chiefId, 1);
        //3.更新用户的chiefId adminId 和isNewUser和inviteCode
        $chiefUserId = $this->getChiefUserIdByChief($chiefId);
        $userRet = $this->getAdminIdAndInviteCode($chiefId);
        $userresult = $this->userDao->update(array( "chiefUserId" => $chiefUserId, "adminId" => $userRet[0]['adminid'],
        "isNewUser" => 0, "chiefInviteCode" => $inviteCode, "adminInviteCode" => $userRet[0]['admininvitecode'], "cond_id" => $userId));
        
        //给头领和加盟商加钱
        if(!$this->addPromotGains(array( 'chiefUserId' => $chiefUserId, 'adminId' => $userRet[0]['adminid'], 'fromWalletId' => $walletId )))
        {
            return false;
        }    
        
        return true;
    }
    
    public function setChiefInviteCode($args){
        
        $this->trackLog("execute", "setChiefInviteCode()");
        
        $userId = $args['userId'];
        $openid = $args['openid'];
        $chiefInviteCode = $args['chiefInviteCode'];
        
        //检查邀请码的正确性
        if(!is_numeric($chiefInviteCode) || strlen($chiefInviteCode) != 4){
            
            $data['code']  = 0;
            $data['msg']  = "邀请码格式错误，正确邀请码是四位数字！";
            return $data;    
        }
        
        $chiefId = $this->chiefCodeDao->getChiefIdByInviteCode($chiefInviteCode);
        if($chiefId < 1){
            
            $data['code']  = 0;
            $data['msg']  = "邀请码不存在请检查邀请码是否正确！";
            return $data;
        }
        
        //1.首先检查自己的邀请码是否为空，不为空就不允许用户修改了
        $curCode = $this->userDao->getChiefInviteCodeById($userId);
        if(!empty($curCode)){
            
            $data['code']  = 0;
            $data['msg']  = "邀请码只允许修改一次！";
            return $data;    
        }
        
        //2.检查钱包，获取钱包id
        $walletId = $this->walletDao->getIdByUserId($userId);
        if($walletId < 1){
            
            $data['code']  = 0;
            $data['msg']  = "你的钱包不存在，请重试！";
            return $data;    
        }
        
        //获取openid的chiefId
        $fansResult = $this->chiefDao->queryFans(array("openid" => $openid));   
        if(!$fansResult){
            
            $openidChiefId = 0;
        }else{
            
            $openidChiefId = $fansResult[0]['chiefid'];    
        }

        try{
            
            $this->db->startTrans();
            
            //如果两边获取的头领不一样
            if($chiefId != $openidChiefId){
                
                //减掉旧头领的粉丝数和用户数,然后增加新头领的粉丝数和用户数
                $this->chiefDao->changeNumberById($openidChiefId, -1);
                $this->chiefDao->changeRegisterById($openidChiefId, -1);
                $this->chiefDao->changeNumberById($chiefId, 1);
                $this->chiefDao->changeRegisterById($chiefId, 1);
                $this->chiefFansDao->setChiefId(array("openid" => $args['openid'], "chiefId" => $chiefId));
            }
            
            //存储关系到三级分销关系
            if(!$this->addThirdLevelMap($userId, $chiefId, $chiefInviteCode, ThirdLevelEnum::LEVEL_CHIEF_USER)){
                
                $this->db->rollback();
                $data['code']  = 0;
                $data['msg']  = "绑定邀请码失败！";
                return $data; 
            }
            
            //更新用户的chiefId adminId 和isNewUser和inviteCode
            $chiefUserId = $this->getChiefUserIdByChief($chiefId);
            $userRet = $this->getAdminIdAndInviteCode($chiefId);
            $userresult = $this->userDao->update(array( "chiefUserId" => $chiefUserId, "adminId" => $userRet[0]['adminid'],
            "isNewUser" => 0, "chiefInviteCode" => $chiefInviteCode, "adminInviteCode" => $userRet[0]['admininvitecode'], "cond_id" => $userId));
            
            //给头领和加盟商加钱
            if(!$this->addPromotGains(array( 'chiefUserId' => $chiefUserId, 'adminId' => $userRet[0]['adminid'], 'fromWalletId' => $walletId )))
            {
                $this->db->rollback();
                $data['code']  = 0;
                $data['msg']  = "绑定邀请码失败！";
                return $data;   
            }   
            
            $this->db->commit();
            $data['code']  = 1;
            $data['msg']  = "绑定邀请码成功！";
            return $data;
        }catch(Exception $e){
            
            $this->db->rollback();
            $data['code']  = 0;
            $data['msg']  = "绑定邀请码失败！";
            return $data;    
        }
    }
    
    public function setAdminInviteCode($args){
        
        $this->trackLog("execute", "setAdminInviteCode()");
        
        $adminInviteCode = $args['adminInviteCode'];
        $userId = $args['userId'];

        //检查邀请码的正确性
        if(!is_numeric($args['adminInviteCode']) || strlen($args['adminInviteCode']) != 4){
            
            $data['code']  = 0;
            $data['msg']  = "邀请码格式错误，正确邀请码是四位数字！";
            return $data;    
        }
        
        $adminUserDao = new AdminUserDao();
        $adminId = $adminUserDao->getIdByInviteCode($adminInviteCode);
        if($adminId < 1){
            
            $data['code']  = 0;
            $data['msg']  = "邀请码不存在请检查邀请码是否正确！";
            return $data;    
        }
        
        //1.首先检查自己的邀请码是否为空，不为空就不允许用户修改了
        $curCode = $this->userDao->getAdminInviteCodeById($userId);
        if(!empty($curCode)){
            
            $data['code']  = 0;
            $data['msg']  = "邀请码只允许修改一次！";
            return $data;    
        }
        
        try{
            
            $this->db->startTrans();
            //更新自己的adminId和adminInviteCode
            $this->userDao->update(array( "adminId" => $adminId, "adminInviteCode" => $adminInviteCode, "cond_id" => $userId));
            //更新注册用户的adminId和adminInviteCode
            $this->userDao->updateFansAdminIdAndCode(array( "adminId" => $adminId, "adminInviteCode" => $adminInviteCode, 
            "chiefUserId" => $userId));
            
            //获取头领ID
            $chiefId = $this->getChiefIdByUserId($userId);
            if($chiefId < 1){
                
                $this->db->rollback();
                $data['code']  = 0;
                $data['msg']  = "绑定邀请码失败！";
                return $data;
            }
            
            //存储关系到三级分销关系
            if(!$this->addThirdLevelMap($chiefId, $adminId, $adminInviteCode, ThirdLevelEnum::LEVEL_ADMIN_CHIEF)){
                
                $this->db->rollback();
                $data['code']  = 0;
                $data['msg']  = "绑定邀请码失败！";
                return $data;
            }
            
            $this->db->commit();
            $data['code']  = 1;
            $data['msg']  = "绑定邀请码成功！";
            return $data;
        }catch(Exception $e){
            
            $this->db->rollback();
            $data['code']  = 0;
            $data['msg']  = "绑定邀请码失败！";
            return $data;
        }
    }
    
    private function checkParamForSetInviteCode($args){
        
        
    }
    
    private function checkDuplicate($phone){
        
        //手机号是为唯一标识，注册之前先查重
        $model = new Model();
        $extSql = "SELECT * FROM user WHERE phone='$phone'";
        $this->trackLog("extSql", $extSql);
        $result = $model->query($extSql);
        $this->trackLog("result", $result);
        
        if($result)
        {
            $data['code']  = 1;
            $data['msg']  = "该手机号已经注册过了！";
            $this->ajaxReturn($data);     
        }
    }

    private function getChiefUserIdByChief($chiefId){
        //Todo:如果通过userId获取不到，再通过openid获取一次
        $chiefResult = $this->chiefDao->query(array("id" => $chiefId));
        if(!$chiefResult){
            
            return 0;
        }
        
        return $chiefResult[0]['userid'];
    }
    
    private function getAdminIdAndInviteCode($chiefId){
        
        $chiefResult = $this->chiefDao->query(array("id" => $chiefId));
        if(!$chiefResult){
            
            return 0;
        }
        
        $chiefUserId = $chiefResult[0]['userid'];
        $userResult = $this->userDao->query(array("id" => $chiefUserId));

        return $userResult;  
    }

    private function addWallet($args){
        
        //首先检查用户钱包存在不存在
        $result = $this->walletDao->query(array("userId" => $args['userId']));
        
        if(!$result){
            
            //给用户创建钱包
            $walletId = $this->walletDao->add(array("userId" => $args['userId']));
            if($walletId < 0){
                
                $data['code'] = 0;
                $data['msg'] = '申请失败！';
                return $data;
            }    
        }
        
        $data['code'] = 1;
        $data['msg'] = '申请成功！';
        $data['data'] = $walletId;
        return $data;
    }
    
    private function addPromotGains($args){
        
        $chiefGains = C('GAINS_PROMOT_CHIEF');
        $adminGains = C('GAINS_PROMOT_ADMIN');
        $chiefUserId = $args['chiefUserId'];
        $adminId = $args['adminId'];
        $fromWalletId = $args['fromWalletId'];
               
        if($chiefUserId < 1){  //没有头领直接返回true
            
            return true;
        }
        
        $chiefWalletInfo = $this->walletDao->query(array('userId' => $chiefUserId));
        if(!$chiefWalletInfo){ //获取头领钱包信息失败，返回false
            return false;
        }
        $chiefWalletId = $chiefWalletInfo[0]['id'];
        
        $args = array(
        'fromWalletId' => $fromWalletId,
        'toWalletId' => $chiefWalletId,
        'money' => $chiefGains * 100, //转化为分
        'type' => WalletEnum::TYPE_INCREASE,
        'orderType' => WalletEnum::ORDERTYPE_PROMOT_GASINS,
        'businessId' => 0,
        'status' => WalletEnum::STATUS_ORDER_SUCCEEDED,
        'remark' => '推广收益'
        );
        //创建订单
        $orderId = $this->walletOrderDao->add($args);
        if($orderId < 1){
            
            return false;
        }
        
        //加钱
        $walletResult = $this->walletDao->update(array("cond_id" => $chiefWalletId, "money" => 'money+' . $chiefGains * 100));
        if(!$walletResult){
            
            return false;
        } 
        
        if($adminId < 1){ //没有加盟商直接返回true
            
            return true;
        }
        
        $adminWalletInfo = $this->walletDao->query(array('adminId' => $adminId));
        if(!$adminWalletInfo){ //获取加盟商钱包信息失败，返回false
            return false;
        }
        $adminWalletId = $adminWalletInfo[0]['id'];
        
        $args = array(
        'fromWalletId' => $fromWalletId,
        'toWalletId' => $adminWalletId,
        'money' => $adminGains * 100, //转化为分
        'type' => WalletEnum::TYPE_INCREASE,
        'orderType' => WalletEnum::ORDERTYPE_PROMOT_GASINS,
        'businessId' => 0,
        'status' => WalletEnum::STATUS_ORDER_SUCCEEDED,
        'remark' => '推广收益'
        );
        //创建订单
        $orderId = $this->walletOrderDao->add($args);
        if($orderId < 1){
            
            return false;
        }
        
        //加钱
        $walletResult = $this->walletDao->update(array("cond_id" => $adminWalletId, "money" => 'money+' .$adminGains * 100));
        if(!$walletResult){
            
            return false;
        } 
        
        return true;
    }
    
    private function addThirdLevelMap($cid, $pid, $inviteCode, $level){
        
        $thirdLevelMap = new ThirdLevelUserMapDao($this->db);
        
        //判断是否存在
        $id = $thirdLevelMap->getIdByCidAndLevel($cid, $level);
        
        if($id < 1){ //不存在直接添加
            
            if(!$thirdLevelMap->add(array('cid' => $cid, 'pid' => $pid, 'inviteCode' => $inviteCode, 'level' => $level)))
            {
                return false;
            }
        }else{  //存在更新
        
            $thirdLevelMap->updateById($id, array('cid' => $cid, 'pid' => $pid, 'inviteCode' => $inviteCode, 'level' => $level));
        }

        return true;
    }
    
    private function getChiefIdByUserId($id){
        
        $querySql = "SELECT ac.id FROM ac_chief ac LEFT JOIN user u ON u.phone = ac.tel WHERE u.id = '$id'";
        $result = $this->db->query($querySql);
        if(!$result){
            
            return 0;
        }
        
        return $result[0]['id'];
    }
    
}

?>
