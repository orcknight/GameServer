<?php
namespace Home\Service;

header("Content-Type: text/html; charset=utf-8");

class WalletEnum {

    //交易类型
    CONST TYPE_INCREASE = 0; //增加
    CONST TYPE_DECREASE = 1; //减少
    
    //订单类型
    CONST ORDERTYPE_WAGES = 0; //工资
    CONST ORDERTYPE_DEPOSIT = 1;  //转入 存款
    CONST ORDERTYPE_LOAN = 2;  //借贷
    CONST ORDERTYPE_WITHDRAWCASH = 3; //提现
    
    //订单状态
    CONST STATUS_ORDER_FAILED  = 0;    //失败
    CONST STATUS_ORDER_PROCESSING = 1; //处理中
    CONST STATUS_ORDER_SUCCEEDED = 2;  //成功
    
    //钱包状态
    CONST STATUS_DISABLED = 0; //禁用
    CONST STATUS_NORMAL = 1; //正常
    CONST STATUS_FREEZED = 2; //冻结

}
?>
