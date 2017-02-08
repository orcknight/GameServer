<?php
namespace Company\Controller;

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
        
        $this->trackLog("execute", "WalletController()");
        
        //回掉函数不能调用OAuth验证
        if( ACTION_NAME != 'notify') {
            //执行函数之前每次都进行登录验证
            $this->checkIsLogin();
            parent::_initialize();
        }
    } 
    
    #region 为前端提供数据的接口
    

    /**
    * 获取钱包信息
    */
    public function getWalletDetail(){
        
        $this->trackLog("execute", "getWalletDetail()");
        
        if(!array_key_exists('id',  I('session.x_company'))){
            
            $data['code']  = 0;         
            $data['msg']  = "执行失败！";  
            $this->ajaxReturn($data);
        }
        
        $companyId = $this->getCompanyId();
        
        //确保用户id正确有效
        if($companyId <= 0){
            
            $data['code'] =  0;
            $data['msg'] = "用户id不正确！" ;
            $this->ajaxReturn($data);
        }
            
        $db = new Model();
        $sql = "SELECT * FROM wallet WHERE companyId='$companyId'";
        $this->trackLog("sql", $sql);
        $walletInfo = $db->query($sql);
        $this->trackLog("WalletInfo", $walletInfo);
        
        if($walletInfo){
            //分转化为元
            $walletInfo[0]['money'] = $walletInfo[0]['money'] / 100;
            $walletInfo[0]['gains'] = $walletInfo[0]['gains'] / 100;
            $walletInfo[0]['income'] = $walletInfo[0]['income'] / 100;
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
        
        $companyId = $this->getCompanyId();
        $db = new Model();
        $sql ="SELECT * FROM wallet WHERE companyId='$companyId'";
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
        $orderTypes = "0,20";
        
        $walletId = $wallet[0]['id'];
        $sql = "SELECT COUNT(*) as count FROM wallet_order WHERE ((type='$increaseType' AND toWalletId='$walletId') OR (type='$decreaseType' AND fromWalletId='$walletId')) AND IF(orderType IN($orderTypes) AND status <> 2, 0, 1)=1";
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
        
        
        $sql = "SELECT * FROM wallet_order WHERE ((type='$increaseType' AND toWalletId='$walletId') OR (type='$decreaseType' AND fromWalletId='$walletId')) AND IF(orderType IN($orderTypes) AND status <> 2, 0, 1)=1 ORDER BY id DESC LIMIT $offset, $pageSize";
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
     *获取用户注册时的手机号 
     */
    public function getCompanyPhone(){
        
        $this->trackLog("execute", "getCompanyPhone()");
        
        $data['code'] =  1;
        $data['msg'] = "查询成功！";
        $data['data'] = array("phone" => I('session.x_company')['phone']);
        $this->ajaxReturn($data);  
    }
    
    #endregion 为前端提供数据的接口
    
    
    #region 根据前台请求进行CURD操作的接口

    /*
     *判断旧密码输入是否正确 
     */
    public function IsPasswordCorrect(){
        
        $this->trackLog("execute", "IsPasswordCorrect()");
        
        $companyId = $this->getCompanyId();
        $userInput = md5(I('post.password'));
        
        $db = new Model();
        $sql = "SELECT password FROM wallet WHERE companyId='$companyId'";
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
    
    /**
    * 判断用户支付密码是否为空
    * 
    */
    public function IsPasswordEmpty(){
        
        $this->trackLog("execute", "IsPasswordEmpty()");
        
        $companyId = $this->getCompanyId();
        $this->trackLog("companyId", $companyId);
        
        $db = new Model();
        $sql = "SELECT password FROM wallet WHERE companyId='$companyId'";
        $this->trackLog("sql", $sql);
        $password = $db->query($sql);
        $password = $password[0]["password"];
        $this->trackLog("password", $password);
        
        if($password){
            $this->ajaxReturn(TRUE);
        }
        $this->ajaxReturn(FALSE);
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
        $companyId = $this->getCompanyId();
        //检查下设置的密码是不是跟以前相同,如果相同直接返回成功
        if($this->isPasswordEqual($md5Password, $companyId)){
            
            $data['code'] =  1;
            $data['msg'] = "密码设置成功！" ;
            $this->ajaxReturn($data);
        }
        
        $db = new Model();
        $sql = "UPDATE wallet SET password='$md5Password' WHERE companyId='$companyId'";
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
        $companyId = $this->getCompanyId();
        //检查下设置的密码是不是跟以前相同,如果相同直接返回成功
        if($this->isPasswordEqual($md5Password, $companyId)){
            
            $data['code'] =  1;
            $data['msg'] = "密码设置成功！" ;
            $this->ajaxReturn($data);
        }
        
        $db = new Model(); 
        $sql = "UPDATE wallet SET password='$md5Password' WHERE companyId='$companyId'";
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
    * 微信支付回掉函数
    * 
    */
    public function notify(){
        
        $this->trackLog("execute", "notify()");
        
        $xml = file_get_contents("php://input"); //接收POST数据
        
        //使用通用通知接口
        $wxPayNotify = new WxPayNotify();
        //存储微信的回掉
        $notifyArray = $wxPayNotify->FromXml($xml);
        $this->trackLog("$xml", $xml);
        $this->trackLog("notifyArray", $notifyArray);

        //验证签名，并回应微信。
        //对后台通知交互时，如果微信收到商户的应答不是成功或超时，微信认为通知失败，
        //微信会通过一定的策略（如30分钟共8次）定期重新发起通知，
        //尽可能提高通知的成功率，但微信不保证通知最终能成功。  
        $wxPayNotify->Handle(true);
        
        if(!array_key_exists("result_code", $notifyArray) || $notifyArray['result_code'] != "SUCCESS"){
                
            $this->trackLog("notify result", "回调返回结果错误，直接返回，等待下次回调!");
            return;            
        }

        $outTradeNo = $notifyArray['out_trade_no'];
        $transactionId = $notifyArray['transaction_id'];
        $toWalletId = $notifyArray['attach'];
        $totalFee = $notifyArray['total_fee'];
        $verifyStatus = WalletEnum::STATUS_ORDER_PROCESSING;
        $verifyOrderType = WalletEnum::ORDERTYPE_USER_RECHARGE . ',' . WalletEnum::ORDERTYPE_COMPANY_RECHARGE;
        $status = WalletEnum::STATUS_ORDER_SUCCEEDED;
        $this->trackLog("outTradeNo", $outTradeNo);
        $this->trackLog("transactionId", $transactionId);
        $this->trackLog("status", $status);
        
        //验证订单和订单状态
        //验证orderType
        $querySql = "SELECT * FROM wallet_order WHERE MD5(id) = '$outTradeNo' AND status = '$verifyStatus'
        AND toWalletId = '$toWalletId' AND money = '$totalFee' AND orderType IN($verifyOrderType)";
        $this->trackLog("querySql", $querySql);
        $verifyResult = M()->query($querySql);
        $this->trackLog("verifyResult", $verifyResult);
        if(!$verifyResult){
            
            //记录不存在，直接返回
            return;
        }
        
        //开启事务处理：处理完成之后更新订单号状态，给用户加钱
        $db = new Model();
        $db->startTrans();
        $walletOrderService = new WalletOrderService($db);
        $walletService = new WalletService($db);
        
        //更新订单状态
        $sql = "UPDATE wallet_order SET status = '$status', orderId = '$transactionId' WHERE MD5(id) = '$outTradeNo'";
        $this->trackLog("sql", $sql);
        $statusResult = $walletOrderService->updateStatusAndOrderIdByMD5Id($outTradeNo, $transactionId, $status);
        $this->trackLog("statusResult", $statusResult);
        
        //给用户加钱
        $walletId = $walletOrderService->getToWalletIdByMD5Id($outTradeNo);  
        $addResult = $walletService->addMoney($walletId, $totalFee);
        
        $this->trackLog("walletId", $walletId);
        $this->trackLog("totalFee", $totalFee);
        $this->trackLog("statusResult", $statusResult);
        $this->trackLog("addResult", $addResult);
        
        if($statusResult && $addResult){
            
            //成功提交事务
            $db->commit();
            $this->trackLog("Trans", "事务处理成功，订单状态和用户金额已经更新！");
        }else{
            
            //失败回滚
            $db->rollback();
            $this->trackLog("Trans", "事务处理失败，已经回滚！");
        }
        
    }
    
    /**
    * 企业用户充值
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
        
        $companyId = $this->getCompanyId();
        $this->trackLog("companyId", $companyId);
        $walletId = $walletService->getWalletIdByUserId($companyId, true);
        if($walletId < 1){
            
            $data['code'] =  0;
            $data['msg'] = "支付错误，请稍后重试！";
            $this->ajaxReturn($data);    
        }
        
        $walletOrderArray = array(
            "toWalletId" => $walletId,
            "money" => $amount * 100, //转化为分 
            "type" => WalletEnum::TYPE_INCREASE,
            "orderType" => WalletEnum::ORDERTYPE_COMPANY_RECHARGE,
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
        $unifiedOrder->SetBody("捷城科技充值");//商品描述
        $unifiedOrder->SetOut_trade_no(md5($walletOrderId));//商户订单号
        $unifiedOrder->SetTotal_fee($amount * 100);//设置金额(把单位转化成分)
        $unifiedOrder->SetNotify_url(C('WECHAT-HOST').C('WxPayConf.NOTIFY_URL'));//通知地址
        $unifiedOrder->SetTrade_type("JSAPI");//交易类型
        $unifiedOrder->SetLimit_pay("no_credit"); //禁用信用卡交易
        $unifiedOrder->SetAttach($walletId); //设置下萝卜钱包id用于在回调过程中进行验证。
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
    
    #endregion 根据前台请求进行CURD操作的接口
     
    #region 工具函数
    
    /*
     *检查用户是否登陆
     */
    public function checkIsLogin(){
        
        $this->trackLog("execute", "checkIsLogin()");
        
        if(I('session.x_company')){
            
            return;
        }
            
        //用户没有登录
        $data['code'] =  0;
        $data['msg']="您没有登录，请登录后再操作！" ;
        $this->ajaxReturn($data);
    }
    
    public function getCompanyId(){
        
        $this->trackLog("execute", "getCompanyId()");
        
        if(I('session.x_company')['id']){
            return I('session.x_company')['id'];
        }
      
      return 0;
    }
    
    public function isPasswordEqual($password, $companyId){
        
        $this->trackLog("execute", "isPasswordEqual()");
        
        $db = new Model();
        $sql = "SELECT * FROM wallet WHERE companyId='$companyId' AND password='$password'";
        $this->trackLog("sql", $sql);
        $walletInfo = $db->query($sql);
        $this->trackLog("walletInfo", $walletInfo);
        
        if($walletInfo){
            
            return TRUE;
        }else{
            
            return FALSE;
        } 
    }
    
    #endregion #region 工具函数  
}

?>