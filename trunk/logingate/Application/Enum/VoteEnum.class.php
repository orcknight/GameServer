<?php
namespace Enum;

header("Content-Type: text/html; charset=utf-8");

class VoteEnum {

    // 投票类型
    CONST TYPE_LIMITED = 0; // 有限投票
    CONST TYPE_UN_LIMITED = 1; // 无限投票
    
    //属性类型
    CONST ATTR_TYPE_ITEM_NUM    = 1001; //条目数
    CONST ATTR_TYPE_VIEW_NUM    = 1002; //浏览数量
    CONST ATTR_TYPE_VOTED_NUM   = 1003; //投票数量

}
?>
