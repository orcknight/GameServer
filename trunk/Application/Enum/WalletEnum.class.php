<?php
namespace Enum;

header("Content-Type: text/html; charset=utf-8");

class WalletEnum {

    //交易类型
    CONST TYPE_DECREASE = 0; //减少
    CONST TYPE_INCREASE= 1; //增加
    
    //用户订单类型
    CONST ORDERTYPE_USER_RECHARGE = 0; //用户充值 
    CONST ORDERTYPE_USER_WAGE = 1; //用户获得薪水
    CONST ORDERTYPE_USER_WITHDRAWCASH = 2; //用户提现
    //企业订单类型
    CONST ORDERTYPE_COMPANY_RECHARGE = 20;  //企业充值
    CONST ORDERTYPE_COMPANY_PAYOFF = 21; //企业发工资
    
    //通用类型
    CONST ORDERTYPE_GAINS           = 100;  //普通的钱包收益
    CONST ORDERTYPE_PROMOT_GASINS   = 101;  //推广收益
    CONST ORDERTYPE_FINANCIAL_GAINS = 102;  //下级产生的理财收益
    CONST ORDERTYPE_WAGE_GAINS      = 103;  //经销商和头领的工资收益
    CONST ORDERTYPE_TASK_GAINS      = 104;  //线上兼职收入
    
    //订单状态
    CONST STATUS_ORDER_FAILED  = 0;    //失败
    CONST STATUS_ORDER_PROCESSING = 1; //处理中
    CONST STATUS_ORDER_SUCCEEDED = 2;  //成功
    CONST STATUS_ORDER_CANCELED = 3;   //取消
    
    //钱包状态
    CONST STATUS_DISABLED = 0; //禁用
    CONST STATUS_NORMAL = 1; //正常
    CONST STATUS_FREEZED = 2; //冻结
    
    //充值时使用的支付方式
    CONST PAY_TYPE_WECHAT = 0; //微信支付
    CONST PAY_TYPE_ALIPAY = 1; //支付宝支付

}
?>
