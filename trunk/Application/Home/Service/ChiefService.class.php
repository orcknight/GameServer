<?php

namespace Home\Service;

use Think\Model;
use Think\Exception;
use Org\Wechat\Jssdk;
use Home\Dao\ChiefDao;
use Home\Dao\UserDao;
use Home\Dao\WalletDao;
use Home\Dao\ChiefCodeDao;
use Home\Dao\ThirdLevelUserMapDao;
use Enum\UserEnum;
use Enum\ThirdLevelEnum;


class ChiefService extends BaseService {
    
    private $db;
    private $chiefDao;
    private $userDao;
    private $walletDao;
    
    public function __construct() {
        
        $this->db = new Model();
        $this->chiefDao = new ChiefDao($this->db);
        $this->userDao = new UserDao($this->db);
        $this->walletDao = new WalletDao($this->db);
    }
    
    public function chiefSignUp($args){

        try{
            
            //启动事务
            $this->db->startTrans();
            
            //申请头领
            $chiefResult = $this->applyCheif($args);
            if($chiefResult['code'] == 0){
                
                $this->db->rollback();
                $data['code'] = 0;
                $data['msg'] = '申请失败！';
                return $data;
            }
            
            //保存用户的邀请码头领ID等信息
            $chiefId = $chiefResult['data'];
            $inviteCode = $args['inviteCode'];
            $adminId = $this->getAmdinIdByCode($inviteCode);
            $level = ThirdLevelEnum::LEVEL_ADMIN_CHIEF;
            if($adminId > 1){
                
                $mapResult = $this->addThirdLevelMap($chiefId, $adminId, $inviteCode, $level);
                if(!$mapResult){
                    
                    $this->db->rollback();
                    $data['code'] = 0;
                    $data['msg'] = '申请失败！';
                    return $data;    
                }
            }

            $this->db->commit();
            $data['code'] = 1;
            $data['msg'] = '申请成功！';
            return $data;        
        }catch(Execption $e){
            
            $this->db->rollback();
            $data['code'] = 0;
            $data['msg'] = '申请失败！';
            return $data;    
        }
    }
    
    public function getInviteCode($wxid){
        
        //首先检查用户有没有申请过头领
        $chiefResult = $this->chiefDao->query(array('wxid' => $wxid));
        if(empty($chiefResult)){
            
            $chiefId = 0; 
        }else{
            
            $chiefId = $chiefResult[0]['id'];
        }
        
        $chiefCodeDao = new ChiefCodeDao();
        $inviteCode = $chiefCodeDao->getInviteCodeByChiefId($chiefId);
        
        $data['code'] = 1;
        $data['msg'] = '获取成功';
        $data['data'] = $inviteCode;
        
        return $data;
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
    
    /**
    * 申请头领
    * 
    * @param mixed $args
    */
    private function applyCheif($args){
        
        //首先检查用户有没有申请过头领
        $chiefResult = $this->chiefDao->query(array('wxid' => $args['wxid']));
        if(empty($chiefResult)){
            
            $chiefId = 0; 
        }else{
            
            $chiefId = $chiefResult[0]['id'];    
        }

        if($chiefId > 0){ //如果以前申请过，只更新信息
            
            $args['cond_id'] = $chiefId;
            $updateResult = $this->chiefDao->update($args);

            $data['code'] = 1;
            $data['msg'] = '修改成功,请等待审核';
            $data['data'] = $chiefId;
            return $data;

        }else{ //没有申请过，创建
            
            $chiefId = $this->chiefDao->add($args);
            if($chiefId < 0){
                
                $data['code'] = 0;
                $data['msg'] = '修改失败,请稍后重试';
                return $data;    
            }
            
            //产生二维码
            $data = array(
                'action_name' => "QR_LIMIT_SCENE",
                'action_info' => array(
                    'scene' => array(
                        'scene_id' => $chiefId
                    )
                )
            );
            $this->trackLog("begin to jssdk", $args['appId']. ' , ' .$args['appsecret']);
            $jssdk = new Jssdk($args['appId'],$args['appsecret']);
            $arr = $jssdk->createQr(json_encode($data));
            if(!$arr)
            {
                $data['code'] = 0;
                $data['msg'] = '修改失败,请稍后重试';
                return $data;
            }
            
            $url = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=".urlencode($arr['ticket']);
            $img_arr = $this->GrabImage($url,$chiefId);
            if(!$img_arr)
            {
                $data['code'] = 0;
                $data['msg'] = '修改失败,请稍后重试';
                return $data;
            }
            
            if(filesize('.'.$img_arr['path']) < 100)
            {
                $img_arr = $this->GrabImage($url,$chiefId);
            }

            $res = $this->chiefDao->update(array("path" => $img_arr['path'], "cond_id" => $chiefId));
            $this->trackLog("res", $res);
            
            if(!$res){
                
                $data['code'] = 0;
                $data['msg'] = '修改失败,请稍后重试';
                return $data;    
            }

            $data['code'] = 1;
            $data['msg'] = '申请成功,请等待审核';
            $data['data'] = $chiefId;
            return $data;
        }  
    }
    
    // 持久化網絡圖片
    private function grabImage($url, $chiefId) {

        $this->trackLog("excute", "grabImage");

        if($url=="") {
            return false;
        }

        $filename = "qr_".$chiefId.".jpg";
        $path ="./Uploads/qr_img/".$filename;
        $this->trackLog("path", $path);

        ob_start();
        readfile($url);
        $img = ob_get_contents();
        ob_end_clean();
        $size = strlen($img);
        $fp2=@fopen($path, "a");
        fwrite($fp2,$img);
        fclose($fp2);
        if(is_file($path)) {
            $arr = array(
                'filename' => $filename,
                'path' => "/Uploads/qr_img/".$filename
            );
            return $arr;
            $this->trackLog("arr", json_decode($arr));
        } else {
            $this->trackLog("grabImage", "fail");
            return false;
        }
    }
    
    private function getAmdinIdByCode($code){
        
        if(!is_numeric($code) || strlen($code) != 4){
            
            return 0;
        }
        
        $querySql = "SELECT id FROM admin_user WHERE inviteCode = '$code'";
        $this->trackLog("querySql", $querySql);
        $result = M()->query($querySql);
        $this->trackLog("result", $result);
        if(!$result){
            
            return 0;
        }
        
        return $result[0]['id'];
    }
    
    private function addUser($args){
    
        //检查用户是否存在
        $userResult = $this->userDao->query(array("phone" => $args['tel']));
        $msgType = 0; //0发送绑定通知
        if($userResult){ //存在更新
            
            $userId = $userResult[0]['id'];
            $result = $this->userDao->update(array(
            "cond_id" => $userId,
            "openid" => $args['openid'],
            "adminId" => $args['adminId'],
            "adminInviteCode" => $args['adminInviteCode'],
            ));
            
        }else{ //不存在，注册用户

            $param = array(
            "phone" => $args['tel'],
            "password" => md5('123456'),
            "chiefId" => 0,
            "adminId" => $args['adminId'],
            "adminInviteCode" => $args['adminInviteCode'],
            "openid" => $args['openid'],
            "isNewUser" => 0,
            "status" => UserEnum::STATUS_CHECKED,
            );    
            
            $userId = $this->userDao->add($param);
            if($userId < 0){
                
                $data['code'] = 0;
                $data['msg'] = '创建失败！';
                return $data;
            }
            $msgType = 1;
        }
        
        $data['code'] = 1;
        $data['msg'] = '创建用户成功！';
        $data['data']['userId'] = $userId;
        $data['data']['msgType'] = $msgType;
        return $data;   
        
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
        return $data;
    }
    
}
?>
