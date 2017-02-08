<?php
namespace Home\Controller;

use Think\Controller;
use Think\Page;
use Think\Model;
use Enum\WalletEnum;

use Company\Service\WalletOrderService;
use Company\Service\WalletService;

use Org\WxPay\WxPayApi;
use Org\WxPay\WxPayUnifiedOrder;
use Org\WxPay\WxPayNotify;
use Org\WxPay\JsApiPay;

class WalletController extends WtAuthController{
    
    public function _initialize() {
        //执行函数之前每次都进行登录验证
       $this->checkIsLogin(); 
       parent::_initialize();
    }  
    
    #region 为前端提供数据的接口
    /*
     *获取钱包信息 
     */
    public function getWalletDetail(){
        
        $this->trackLog("execute", "getWalletDetail()");
        
         if(!array_key_exists('id',  I('session.x_user'))){
            
            $data['code']  = 0;         
            $data['msg']  = "执行失败！";  
            $this->ajaxReturn($data);
        }
        
        $userId = I('session.x_user')['id'];
        
        //确保用户id正确有效
        if($userId <= 0){
            
            $data['code'] =  0;
            $data['msg'] = "用户id不正确！" ;
            $this->ajaxReturn($data);
        }
            
        $db = new Model();
        $sql = "SELECT * FROM wallet WHERE userId='$userId'";
        $this->trackLog("sql", $sql);
        $walletInfo = $db->query($sql);
        $this->trackLog("WalletInfo", $walletInfo);
        
        if($walletInfo){
            
            $openid = session("wt_user")['openid'];
            
            $isChief = 0;
            $querySql = "SELECT * FROM ac_chief ac
            LEFT JOIN wt_user wu ON wu.id = ac.wxid
            WHERE wu.wechat_openid = '$openid'";
            $chiefResult = M()->query($querySql);
            if($chiefResult){
                
                $isChief = 1;
            }
            //分转化为元
            $walletInfo[0]['money'] = $walletInfo[0]['money'] / 100;
            $walletInfo[0]['gains'] = $walletInfo[0]['gains'] / 100;
            $walletInfo[0]['income'] = $walletInfo[0]['income'] / 100;
            $walletInfo[0]['rate'] = C('GAINS_YEAR_RATE');
            $walletInfo[0]['ischief'] = $isChief;
            $data['code'] =  1;
            $data['msg'] = "查询成功！" ;
            $data['data'] = $walletInfo[0];
            $this->ajaxReturn($data);
        }
        
        $data['code'] =  0;
        $data['msg'] = "查询失败！";
        $this->ajaxReturn($data);      
    }
    
    /*
     *获取收支明细 
     */
    public function getIncomeDetail(){
        
        $this->trackLog("execute", "getIncomeDetail()");
        
        $currPage = I('currPage','1','trim'); //当前页数
        $pageSize=I('pageSize','10','trim'); //每页数据条数
        $offset=($currPage-1) * $pageSize;
        
        $userId = I('session.x_user')['id'];
        $db = new Model();
        $sql ="SELECT * FROM wallet WHERE userId='$userId'";
        $this->trackLog("sql", $sql);
        $wallet = $db->query($sql);
        $this->trackLog("wallet", $wallet);
        
        if(!$wallet){
        
            $data['code'] =  0;
            $data['msg'] = "找不到钱包信息";
            $this->ajaxReturn($data);
        }
        
        $increaseType = WalletEnum::TYPE_INCREASE;
        $decreaseType = WalletEnum::TYPE_DECREASE;
        $orderTypes="0,20";
        $nonSearchTypes = WalletEnum::ORDERTYPE_PROMOT_GASINS . "," . WalletEnum::ORDERTYPE_FINANCIAL_GAINS;
        
        $walletId = $wallet[0]['id'];
        $sql = "SELECT COUNT(*) as count FROM wallet_order WHERE ((type='$increaseType' AND toWalletId='$walletId') OR (type='$decreaseType' AND fromWalletId='$walletId')) AND IF(orderType IN($orderTypes) AND status <> 2, 0, 1)=1 
        AND orderType NOT IN($nonSearchTypes)";
        $this->trackLog("sql", $sql);
        $total = $db->query($sql);
        $this->trackLog("total", $total);
        
        if(!$total){
            
            $data['code'] =  0;
            $data['msg'] = "查询失败！";
            $this->ajaxReturn($data);
        }
        
        $data['total'] = $total[0]['count'];
        
        //如果没有交易记录直接返回
        if($total[0]['count'] == 0){
            $returnData['code'] =  1;
            $returnData['msg'] = "没有交易记录！";
            $returnData['data'] = $data;
            $this->ajaxReturn($returnData);
        }
        

        $data['currPage'] = $currPage;
        $data['pageSize'] = $pageSize;
        
        
        $sql = "SELECT * FROM wallet_order WHERE ((type='$increaseType' AND toWalletId='$walletId') OR (type='$decreaseType' AND fromWalletId='$walletId')) AND IF(orderType IN($orderTypes) AND status <> 2, 0, 1)=1 
        AND orderType NOT IN($nonSearchTypes) ORDER BY id DESC LIMIT $offset, $pageSize";
        $this->trackLog("sql", $sql);
        $incomeDetail = $db->query($sql);
        $this->trackLog("incomeDetail", $incomeDetail);
        
        if(!$incomeDetail){
            
            $data['code'] =  0;
            $data['msg'] = "查询失败！";
            $this->ajaxReturn($data); 
        }
        
        for($i=0; $i < count($incomeDetail); $i++){
            
          //存的是分，转化成元  
          $incomeDetail[$i]["money"] = $incomeDetail[$i]["money"] / 100;
          
          //拆分一下createTime
          $incomeDetail[$i]["orderdate"] = date('Y-m-d', strtotime($incomeDetail[$i]['createtime']));
          $incomeDetail[$i]["ordertime"] = date('H:i:s', strtotime($incomeDetail[$i]['createtime']));  
        }
        
        $data['data'] = $incomeDetail;
        $returnData['code'] =  1;
        $returnData['msg'] = "查询成功！";
        $returnData['data'] = $data;
        $this->ajaxReturn($returnData);
        
    }
    
    /**
    * 获取头领收益详情
    * 
    */
    public function getPromotDetail(){
        
        $this->trackLog("execute", "getPromotDetail()");
        
        $promotGains = 0;
        $financialGains = 0;
        $userId = I('session.x_user')['id'];   
        $orderType = WalletEnum::ORDERTYPE_PROMOT_GASINS;
        
        $querySql = "SELECT SUM(wo.money) AS money FROM wallet_order wo
        LEFT JOIN wallet w ON w.id = wo.toWalletId
        LEFT JOIN `user` u ON u.id = w.userId 
        WHERE wo.orderType = '$orderType' AND u.id = '$userId'";
        $this->trackLog("querySql", $querySql);
        $result = M()->query($querySql);
        $this->trackLog("result", $result);
        if($result){
            
            $promotGains = $result[0]['money']; 
        }

        $orderType = WalletEnum::ORDERTYPE_FINANCIAL_GAINS;
        $querySql = "SELECT SUM(wo.money) AS money FROM wallet_order wo
        LEFT JOIN wallet w ON w.id = wo.toWalletId
        LEFT JOIN `user` u ON u.id = w.userId 
        WHERE wo.orderType = '$orderType' AND u.id = '$userId'";
        $this->trackLog("querySql", $querySql);
        $result = M()->query($querySql);
        $this->trackLog("result", $result);
        if($result){
            
            $financialGains = $result[0]['money'];    
        }
        
        $data['promotgains'] = $promotGains/100.00; //推广收益
        $data['financialgains'] = $financialGains/100.00; //理财收益
        $returnData['code'] =  1;
        $returnData['msg'] = "查询成功！";
        $returnData['data'] = $data;
        $this->ajaxReturn($returnData); 
    }
    
    /**
    * 获取头领的推广收益记录
    * 
    */
    public function getPromotLog(){
        
        $this->trackLog("execute", "getPromotDetail()");
        
        $currPage = I('currPage','1','trim'); //当前页数
        $pageSize=I('pageSize','10','trim'); //每页数据条数
        $offset=($currPage-1) * $pageSize;
        
        $userId = I('session.x_user')['id'];
        $db = new Model();
        $sql ="SELECT * FROM wallet WHERE userId='$userId'";
        $this->trackLog("sql", $sql);
        $wallet = $db->query($sql);
        $this->trackLog("wallet", $wallet);
        
        if(!$wallet){
        
            $data['code'] =  0;
            $data['msg'] = "找不到钱包信息";
            $this->ajaxReturn($data);
        }
        
        $increaseType = WalletEnum::TYPE_INCREASE;
        $decreaseType = WalletEnum::TYPE_DECREASE;
        $orderTypes="0,20";
        $searchTypes = WalletEnum::ORDERTYPE_PROMOT_GASINS;
        
        $walletId = $wallet[0]['id'];
        $sql = "SELECT COUNT(*) as count FROM wallet_order WHERE ((type='$increaseType' AND toWalletId='$walletId') OR (type='$decreaseType' AND fromWalletId='$walletId')) AND IF(orderType IN($orderTypes) AND status <> 2, 0, 1)=1 
        AND orderType IN($searchTypes)";
        $this->trackLog("sql", $sql);
        $total = $db->query($sql);
        $this->trackLog("total", $total);
        
        if(!$total){
            
            $data['code'] =  0;
            $data['msg'] = "查询失败！";
            $this->ajaxReturn($data);
        }
        
        $data['total'] = $total[0]['count'];
        
        //如果没有交易记录直接返回
        if($total[0]['count'] == 0){
            $returnData['code'] =  1;
            $returnData['msg'] = "没有交易记录！";
            $returnData['data'] = $data;
            $this->ajaxReturn($returnData);
        }

        $data['currPage'] = $currPage;
        $data['pageSize'] = $pageSize;
        
        $sql = "SELECT wo.money, cf.headimgurl, cf.nickname, wo.createTime,wo.type, wo.orderType FROM wallet_order wo
        LEFT JOIN wallet w ON wo.fromWalletId = w.id 
        LEFT JOIN `user` u on w.userId = u.id
        LEFT JOIN `ac_chief_fans` cf ON u.openid = cf.openid
        WHERE ((wo.type='$increaseType' AND wo.toWalletId='$walletId') 
        OR (wo.type='$decreaseType' AND wo.fromWalletId='$walletId')) 
        AND IF(wo.orderType IN($orderTypes) AND wo.status <> 2, 0, 1)=1 
        AND wo.orderType IN($searchTypes) ORDER BY wo.id DESC LIMIT $offset, $pageSize";
        $this->trackLog("sql", $sql);
        $incomeDetail = $db->query($sql);
        $this->trackLog("incomeDetail", $incomeDetail);
        
        if(!$incomeDetail){
            
            $data['code'] =  0;
            $data['msg'] = "查询失败！";
            $this->ajaxReturn($data); 
        }
        
        for($i=0; $i < count($incomeDetail); $i++){
            
          //存的是分，转化成元  
          $incomeDetail[$i]["money"] = $incomeDetail[$i]["money"] / 100;
          
          //拆分一下createTime
          $incomeDetail[$i]["orderdate"] = date('Y-m-d', strtotime($incomeDetail[$i]['createtime']));
          $incomeDetail[$i]["ordertime"] = date('H:i', strtotime($incomeDetail[$i]['createtime']));  
        }
        
        $data['data'] = $incomeDetail;
        $returnData['code'] =  1;
        $returnData['msg'] = "查询成功！";
        $returnData['data'] = $data;
        $this->ajaxReturn($returnData);    
    }
    
    /**
    * 获取头领的理财收益记录
    * 
    */
    public function getFinancialLog(){
        
        $this->trackLog("execute", "getPromotDetail()");
        
        $currPage = I('currPage','1','trim'); //当前页数
        $pageSize=I('pageSize','10','trim'); //每页数据条数
        $offset=($currPage-1) * $pageSize;
        
        $userId = I('session.x_user')['id'];
        $db = new Model();
        $sql ="SELECT * FROM wallet WHERE userId='$userId'";
        $this->trackLog("sql", $sql);
        $wallet = $db->query($sql);
        $this->trackLog("wallet", $wallet);
        
        if(!$wallet){
        
            $data['code'] =  0;
            $data['msg'] = "找不到钱包信息";
            $this->ajaxReturn($data);
        }
        
        $increaseType = WalletEnum::TYPE_INCREASE;
        $decreaseType = WalletEnum::TYPE_DECREASE;
        $orderTypes="0,20";
        $searchTypes = WalletEnum::ORDERTYPE_FINANCIAL_GAINS;
        
        $walletId = $wallet[0]['id'];
        $sql = "SELECT COUNT(*) as count FROM wallet_order WHERE ((type='$increaseType' AND toWalletId='$walletId') OR (type='$decreaseType' AND fromWalletId='$walletId')) AND IF(orderType IN($orderTypes) AND status <> 2, 0, 1)=1 
        AND orderType IN($searchTypes)";
        $this->trackLog("sql", $sql);
        $total = $db->query($sql);
        $this->trackLog("total", $total);
        
        if(!$total){
            
            $data['code'] =  0;
            $data['msg'] = "查询失败！";
            $this->ajaxReturn($data);
        }
        
        $data['total'] = $total[0]['count'];
        
        //如果没有交易记录直接返回
        if($total[0]['count'] == 0){
            $returnData['code'] =  1;
            $returnData['msg'] = "没有交易记录！";
            $returnData['data'] = $data;
            $this->ajaxReturn($returnData);
        }

        $data['currPage'] = $currPage;
        $data['pageSize'] = $pageSize;
        
        $sql = "SELECT wo.money, cf.headimgurl, cf.nickname, wo.createTime,wo.type, wo.orderType FROM wallet_order wo
        LEFT JOIN wallet w ON wo.fromWalletId = w.id 
        LEFT JOIN `user` u on w.userId = u.id
        LEFT JOIN `ac_chief_fans` cf ON u.openid = cf.openid
        WHERE ((wo.type='$increaseType' AND wo.toWalletId='$walletId') 
        OR (wo.type='$decreaseType' AND wo.fromWalletId='$walletId')) 
        AND IF(wo.orderType IN($orderTypes) AND wo.status <> 2, 0, 1)=1 
        AND wo.orderType IN($searchTypes) ORDER BY wo.id DESC LIMIT $offset, $pageSize";
        $this->trackLog("sql", $sql);
        $incomeDetail = $db->query($sql);
        $this->trackLog("incomeDetail", $incomeDetail);
        
        if(!$incomeDetail){
            
            $data['code'] =  0;
            $data['msg'] = "查询失败！";
            $this->ajaxReturn($data); 
        }
        
        for($i=0; $i < count($incomeDetail); $i++){
            
          //存的是分，转化成元  
          $incomeDetail[$i]["money"] = $incomeDetail[$i]["money"] / 100;
          
          //拆分一下createTime
          $incomeDetail[$i]["orderdate"] = date('Y-m-d', strtotime($incomeDetail[$i]['createtime']));
          $incomeDetail[$i]["ordertime"] = date('H:i', strtotime($incomeDetail[$i]['createtime']));  
        }
        
        $data['data'] = $incomeDetail;
        $returnData['code'] =  1;
        $returnData['msg'] = "查询成功！";
        $returnData['data'] = $data;
        $this->ajaxReturn($returnData);    
    }
    
    /*
     *获取绑定支付宝账号和绑定的用户名 
     */
    public function getBindAlipay(){
        
        $this->trackLog("execute", "getBindAlipay()");
        
        $userId = I('session.x_user')['id'];
        
        $db = new Model();
        $sql = "SELECT alipay, alipayName FROM wallet WHERE userId='$userId'";
        $this->trackLog("sql", $sql);
        $alipayInfo = $db->query($sql);
        $this->trackLog("alipayInfo", $alipayInfo);
        
        if($alipayInfo !== false){
            
            $data['code'] =  1;
            $data['msg'] = "查询成功！";
            $data['data'] = $alipayInfo[0];
            $this->ajaxReturn($data);           
        }
        
        $data['code'] =  0;
        $data['msg'] = "查询失败！";
        $this->ajaxReturn($data);  
    }
    
    /*
     *获取用户注册时的手机号 
     */
    public function getUserPhone(){
        $this->trackLog("execute", "getUserPhone()");
        
        $data['code'] =  1;
        $data['msg'] = "查询成功！";
        $data['data'] = array("phone" => I('session.x_user')['phone']);
        $this->ajaxReturn($data);  
    }
    
    #endregion 为前端提供数据的接口
    
    
    #region 根据前台请求进行CURD操作的接口

    /*
     *判断旧密码输入是否正确 
     */
    public function tellOldPassword(){
        
        $this->trackLog("execute", "tellOldPassword()");
        
        $userId = I('session.x_user')['id'];
        $userInput = md5(I('post.oldPwd'));
        $phone = I('phone', '', 'trim');
        $captcha = I('captcha', '', 'trim');
        
        $this->trackLog('userId', $userId);
        $this->trackLog('userInput', $userInput);
        $this->trackLog('phone', $phone);
        $this->trackLog('captcha', $captcha);
        
        if ($phone != session('captchaPhone') || $captcha != session('captcha')) {
            $data['code']  = 0;
            $data['msg']  = "手机验证码错误！";
            $this->ajaxReturn($data);
        }
        
        $db = new Model();
        $sql = "SELECT password FROM wallet WHERE userId='$userId'";
        $this->trackLog("sql", $sql);
        $password = $db->query($sql);
        $password = $password[0]["password"];
        $this->trackLog("password", $password);
        
        if($password && $password === $userInput){
            
            $data['code'] =  1;
            $data['msg'] = "查询成功！";
            $data['data'] = $alipayInfo[0];
            $this->ajaxReturn($data);           
        }
        
        $data['code'] =  0;
        $data['msg'] = "查询失败！";
        $this->ajaxReturn($data);  
    }
    
    /*
     *绑定支付宝账户 
     */
    public function bindAlipay(){
        
        $this->trackLog("execute", "bindAilpay()");
        
        $alipay = I("post.alipay");
        $aliapyName = I("post.alipayName");
        
        //Todo:这里要验证账号的真实性
        if(!$alipay || !$aliapyName){
            
            $data['code'] =  0;
            $data['msg'] = "支付宝账号和姓名不能为空";
            $this->ajaxReturn($data);
        }
        
        //支付宝账号可能是邮箱也可能是手机号
        if(!preg_match("/^1[34578]{1}\d{9}$/",$alipay) 
        && !filter_var($alipay, FILTER_VALIDATE_EMAIL)){
            
            $data['code'] =  0;
            $data['msg'] = "支付宝账号格式不正确";
            $this->ajaxReturn($data);
        } 
        
        $db = new Model();
        $sql = "UPDATE wallet SET alipay='$alipay', alipayName='$aliapyName' WHERE userId='".I('session.x_user')['id']."'";
        $this->trackLog("sql", $sql);
        $result = $db->execute($sql);
        $this->trackLog("result", $result);
        if($result){
            
            $data['code'] =  1;
            $data['msg'] = "绑定成功！" ;
            $this->ajaxReturn($data);
        }
        
        $data['code'] =  0;
        $data['msg'] = "绑定失败！" ;
        $this->ajaxReturn($data);
             
    }
    
    /*
     *绑定微信支付帐号 
     */
    public function bindWtpay(){
        
        $this->trackLog("execute", "bindWtpay()");

        $wtpay = I("post.wtpay");
        
        //Todo:这里要验证账号的真实性
        if(!$alipay){

        $data['code'] =  0;
        $data['msg'] = "财付通账号不能为空";
        $this->ajaxReturn($data);
        }
        
        $db = new Model();
        $sql = "UPDATE wallet SET wtpay='$wtpay' WHERE userId='".I('session.x_user')['id']."'";
        $this->trackLog("sql", $sql);
        $result = $db->execute($sql);
        $this->trackLog("result", $result);
        
        if($result !== false){

            $data['code'] =  1;
            $data['msg'] = "绑定成功！" ;
            $this->ajaxReturn($data);
        }     
    }
    
    /*
     *设置用户的支付密码 
     */
    public function setPassword(){
        
        $this->trackLog("execute", "setPassword()");
        
        $password = I('password');
        $confirm = I('confirm');
        
        if(empty($password)){
            
            $data['code'] =  0;
            $data['msg'] = "密码不能为空！";
            $this->ajaxReturn($data);
        }
        
        if(!is_numeric($password) || strlen($password) != 6){
            
            $data['code'] =  0;
            $data['msg'] = "密码必须为6位纯数字！";
            $this->ajaxReturn($data);
        }
        
        if($password !== $confirm){
            
            $data['code']  = 0;
            $data['msg']  = "两次密码输入不相同！";
            $this->ajaxReturn($data);
        }
        
        $md5Password = md5($password);
        $userId = I('session.x_user')['id'];
        //检查下设置的密码是不是跟以前相同,如果相同直接返回成功
        if($this->isPasswordEqual($md5Password, $userId)){
            
            $data['code'] =  1;
            $data['msg'] = "密码设置成功！" ;
            $this->ajaxReturn($data);
        }
        
        $db = new Model();
        $sql = "UPDATE wallet SET password='$md5Password' WHERE userId='$userId'";
        $this->trackLog("sql", $sql);
        $result = $db->execute($sql);
        $this->trackLog("result", $result);  
        
        if($result){
            
            $data['code'] =  1;
            $data['msg'] = "密码设置成功！" ;
            $this->ajaxReturn($data);
        }
        
        $data['code'] =  0;
        $data['msg'] = "密码设置失败！" ;
        $this->ajaxReturn($data);
        
    }
    
    /*
     *重置用户的支付密码 
     */
    public function resetPassword(){
       
        $this->trackLog("execute", "resetPassword()");
        
        $phone = I('phone', '', 'trim');
        $password = I('password');
        $confirm = I('confirm');
        $captcha = I('captcha', '', 'trim');

        $this->trackLog('phone', $phone);
        $this->trackLog('password', $password);
        $this->trackLog('captcha', $captcha);
        $this->trackLog('md5(password)', md5($password));
        
        
        if(!is_numeric($phone) || empty($password)) {
            $data['code']  = 0;
            $data['msg']  = "参数格式不正确！";
            $this->ajaxReturn($data);
        }
        
        if(!is_numeric($password) || strlen($password) != 6){
            
            $data['code'] =  0;
            $data['msg'] = "密码必须为6位纯数字！";
            $this->ajaxReturn($data);
        }
        
        if($password !== $confirm){
            
            $data['code']  = 0;
            $data['msg']  = "两次密码输入不相同！";
            $this->ajaxReturn($data);
        }

        if ($phone != session('captchaPhone') || $captcha != session('captcha')) {
            $data['code']  = 0;
            $data['msg']  = "手机验证码错误！";
            $this->ajaxReturn($data);
        }
        
        $md5Password = md5($password);
        $userId = I('session.x_user')['id'];
        //检查下设置的密码是不是跟以前相同,如果相同直接返回成功
        if($this->isPasswordEqual($md5Password, $userId)){
            
            $data['code'] =  1;
            $data['msg'] = "密码设置成功！" ;
            $this->ajaxReturn($data);
        }
        
        $db = new Model(); 
        $sql = "UPDATE wallet SET password='$md5Password' WHERE userId='".I('session.x_user')['id']."'";
        $this->trackLog("sql", $sql);
        $result = $db->execute($sql);
        $this->trackLog("result", $result);
        
        if($result){
            
            $data['code'] =  1;
            $data['msg'] = "密码重置成功！" ;
            $this->ajaxReturn($data);
        }
        
        $data['code'] =  0;
        $data['msg'] = "密码重置失败！" ;
        $this->ajaxReturn($data); 
         
    }
    
    /**
    * 用户充值
    * 
    */
    public function confirmRecharge(){
       
        $this->trackLog("execute", "confirmRecharge()");
         
        $openid = session("wt_user")['openid'];
        $amount = I('amount');
        $payType = I('payType', WalletEnum::PAY_TYPE_WECHAT);
        
        $this->trackLog("openid", $openid);
        $this->trackLog("amount", $amount);
        $this->trackLog("payType", $payType);     
        
        if(empty($openid)){
           
            $data['code'] =  0;
            $data['msg'] = "获取微信用户信息失败,请稍后再试!" ;
            $this->ajaxReturn($data); 
        }
        
        if(empty($amount)){
            
            $data['code'] =  0;
            $data['msg'] = "金额不能为空！";
            $this->ajaxReturn($data);
        }
        
        if(!is_numeric($amount)){
            
            $data['code'] =  0;
            $data['msg'] = "金额必须为数字！";
            $this->ajaxReturn($data);    
        }
        
        //产生订单，状态设置为处理中 
        $db = new Model();
        $walletOrderService = new WalletOrderService($db);
        $walletService = new WalletService($db);
        
        $userId = $this->getUserId();
        $this->trackLog("userId", $userId);
        $walletId = $walletService->getWalletIdByUserId($userId);
        if($walletId < 1){
            
            $data['code'] =  0;
            $data['msg'] = "支付错误，请稍后重试！";
            $this->ajaxReturn($data);    
        }
        
        $walletOrderArray = array(
            "toWalletId" => $walletId,
            "money" => $amount * 100, //转化为分 
            "type" => WalletEnum::TYPE_INCREASE,
            "orderType" => WalletEnum::ORDERTYPE_USER_RECHARGE,
            "status" => WalletEnum::STATUS_ORDER_PROCESSING
            );
        

        $walletOrderId = $walletOrderService->Add($walletOrderArray);
        $this->trackLog("walletOrderId", $walletOrderId);
        if($walletOrderId < 0){
            
            $data['code'] =  0;
            $data['msg'] = "支付错误，请稍后重试！";
            $this->ajaxReturn($data);       
        }
       
        //=========步骤1：使用统一支付接口，获取prepay_id============
        //使用统一支付接口
        $unifiedOrder = new WxPayUnifiedOrder();
        
        //设置统一支付接口参数
        //设置必填参数
        //appid已填,商户无需重复填写
        //mch_id已填,商户无需重复填写
        //noncestr已填,商户无需重复填写
        //spbill_create_ip已填,商户无需重复填写
        //sign已填,商户无需重复填写
        $unifiedOrder->SetOpenid($openid); //用户标识
        $unifiedOrder->SetBody("捷城科技用户充值");//商品描述
        $unifiedOrder->SetOut_trade_no(md5($walletOrderId));//商户订单号
        $unifiedOrder->SetTotal_fee($amount * 100);//设置金额(把单位转化成分)
        $unifiedOrder->SetNotify_url(C('WECHAT-HOST').C('WxPayConf.NOTIFY_URL'));//通知地址
        $unifiedOrder->SetTrade_type("JSAPI");//交易类型
        $unifiedOrder->SetAttach($walletId); //设置下萝卜钱包id用于在回调过程中进行验证。
        $whiteList = explode(',', C('CREDIT_CARD_WHITE_LIST'));
        if(!in_array($openid, $whiteList))
            $unifiedOrder->SetLimit_pay("no_credit"); //禁用信用卡交易
        $order = WxPayApi::unifiedOrder($unifiedOrder);
        $this->trackLog("order", $order);
        
        if(!array_key_exists("result_code", $order) || $order['result_code'] != "SUCCESS"){
             
             //出现错误，把订单状态改为失败
             $walletOrderService->setStatusById($walletOrderId, WalletEnum::STATUS_ORDER_FAILED);
             $data['code'] =  0;
             $data['msg'] = "申请prepay_id失败！";
             $this->ajaxReturn($data);    
        }
        
        $jsApi = new JsApiPay();
        $jsApiParameters = $jsApi->GetJsApiParameters($order);
        $this->trackLog('jsApiParameters',$jsApiParameters);
        
        $data['code'] =  1;
        $data['msg'] = "申请prepay_id成功！";
        $data['jsApiParameters'] = json_decode($jsApiParameters, true);
        $data['orderId'] = $walletOrderId;
        $this->ajaxReturn($data);
        
    }
    
    /**
    * 取消充值，修改订单号状态为取消
    * 
    */
    public function cancelRecharge(){
        
        $this->trackLog("execute", "cancelRecharge()");

        $orderId = I('orderId', 0);
        $args = array(
        'id' => $orderId,
        'status' => WalletEnum::STATUS_ORDER_CANCELED,
        );
        $this->trackLog("args", $args);
        
        $walletService = new \Home\Service\WalletService();
        $this->ajaxReturn($walletService->cancelRecharge($args));
    }
    
    /*
     *提现 
     */
    public function withDrawCash(){
        
        $this->trackLog("execute", "withDrawCash()");
        
        $amount = I('post.amount') * 100; //元转换成分
        $password = I('post.password');
        $alipay = I('post.alipay');
        $alipayName = I('post.alipayname');

        if(empty($amount) || empty($password) || empty($alipay)|| empty($alipayName)){
            
            $data['code'] =  0;
            $data['msg'] = "所有项目不能为空！";
            $this->trackLog("msg", $data['msg']);
            $this->ajaxReturn($data);
        }
        
        if($amount <= 0){
            
            $data['code'] =  0;
            $data['msg'] = "金额不能小于等于0！";
            $this->trackLog("msg", $data['msg']);
            $this->ajaxReturn($data);    
        }
        
        //支付宝账号可能是邮箱也可能是手机号
        if(!preg_match("/^1[34578]{1}\d{9}$/",$alipay) 
        && !filter_var($alipay, FILTER_VALIDATE_EMAIL)){
            
            $data['code'] =  0;
            $data['msg'] = "支付宝账号格式不正确！";
            $this->ajaxReturn($data);
        }
        
        $userId = I('session.x_user')['id'];
        $md5password = md5($password);
        $db = new Model();
        $sql = "SELECT * FROM wallet WHERE userId='$userId' AND password='$md5password'";
        $this->trackLog("sql", $sql);
        $wallet = $db->query($sql);
        $this->trackLog("result", $wallet);
        
        if(!$wallet){
            
            $data['code'] =  0;
            $data['msg'] = "密码错误！";
            $this->ajaxReturn($data); 
        }
        
        $money = $wallet[0]['money'];
        if($amount > $money){
            
            $data['code'] =  0;
            $data['msg'] = "您的余额不足无法取现！";
            $this->ajaxReturn($data); 
        }
        
        //订单和金额处理要用事务
        $db->startTrans();
        
        //计算提现后的金额
        $curMoney = $money - $amount;
        $this->trackLog("curMoney", $curMoney);
        
        //从账户里扣掉金额
        $sql = "UPDATE wallet SET money='$curMoney' WHERE userId='$userId'";
        $this->trackLog("sql", $sql);
        $walletRet = $db->execute($sql);
        $this->trackLog("walletRet", $walletRet);
        
        
        //生成订单
        $walletId = $wallet[0]['id'];
        $type = WalletEnum::TYPE_DECREASE;
        $orderType = WalletEnum::ORDERTYPE_USER_WITHDRAWCASH;
        $status = WalletEnum::STATUS_ORDER_PROCESSING;
        
        $sql = "INSERT INTO wallet_order(fromWalletId, money, type, orderType, remitter, remitterName,"
        ."payee, payeeName, status) VALUES('$walletId', '$amount', '$type', '$orderType', '', '威海捷城信息科技有限公司', "
        ."'$alipay', '$alipayName', '$status')";
        $this->trackLog("sql", $sql);
        $result = $db->execute($sql);
        $this->trackLog("sql", $sql);
        
        //全部执行成功，提交事务
        if($result && $walletRet){
            
            $db->commit();
            $data['code'] =  1;
            $data['msg'] = "提现成功！";
            $this->ajaxReturn($data);
        }
        
        //失败全部回滚，不提交
        $db->rollback();
        $data['code'] =  0;
        $data['msg'] = "提现失败！";
        $this->ajaxReturn($data);
        
    }
    
    #endregion 根据前台请求进行CURD操作的接口
     
    #region 工具函数
    
    /*
     *检查用户是否登陆
     */
    public function checkIsLogin(){
        
        if(I('session.x_user')){
            
            return;
        }
            
        //用户没有登录
        $data['code'] =  0;
        $data['msg']="您没有登录，请登录后再操作！" ;
        $this->ajaxReturn($data);
    }
    
    public function buildOrderNumber(){
        return date('Ymd').substr(implode(NULL, array_map('ord', str_split(substr(uniqid(), 7, 13), 1))), 0, 8);  
    }
    
    public function isPasswordEqual($password, $userId){
        
        $db = new Model();
        $sql = "SELECT * FROM wallet WHERE userId='$userId' AND password='$password'";
        $this->trackLog("sql", $sql);
        $walletInfo = $db->query($sql);
        $this->trackLog("walletInfo", $walletInfo);
        
        if($walletInfo){
            
            return TRUE;
        }else{
            
            return FALSE;
        } 
    }
    
    public function getUserId(){
        
        $this->trackLog("execute", "getUserId()");
        
        if(I('session.x_user')['id']){
            return I('session.x_user')['id'];
        }
      
      return 0;
    }
    
    #endregion #region 工具函数  
}

?>