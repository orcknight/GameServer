<?php

namespace Enum;

header("Content-Type: text/html; charset=utf-8");

class TaskEnum {
    
    //排序类型
    CONST SORT_NORMAL   = 0; //原始创建时间倒序
    CONST SORT_PRICE    = 1; //单价排序  目前单价最高的在前
    CONST SORT_AUDIT    = 2; //审核时间  目前审核最快的在前
    
    //线上兼职次数限制
    CONST TYPE_UNLIMITED    = 0; //不限次数
    CONST TYPE_ONCE         = 1; //每人一次
    CONST TYPE_DAILY        = 2; //每天一次
    
    //线上兼职任务状态
    CONST STATUS_GOING = 1; //进行中
    CONST STATUS_ENDED = 2; //已经结束
    
    //申请的用户端状态
    CONST APPLY_USTATUS_APPLIED     = 1; //进行中
    CONST APPLY_USTATUS_FINISHED    = 2; //已完成
    CONST APPLY_USTATUS_UNFINISHED  = 3; //未完成
    CONST APPLY_USTATUS_PASSED      = 4; //审核通过
    CONST APPLY_UNSTATUS_UNPASSED   = 5; //审核未通过
    
    //申请的企业端状态
    CONST APPLY_CSTATUS_APPLIED     = 1; //进行中
    CONST APPLY_CSTATUS_FINISHED    = 2; //待审核
    CONST APPLY_CSTATUS_UNFINISHED  = 3; //未完成
    CONST APPLY_CSTATUS_PASSED      = 4; //审核通过
    CONST APPLY_CNSTATUS_UNPASSED   = 5; //审核未通过
    
}

?>
