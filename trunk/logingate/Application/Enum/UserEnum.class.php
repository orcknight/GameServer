<?php

namespace Enum;

header("Content-Type: text/html; charset=utf-8");

class UserEnum {
    
    //用户状态
    CONST STATUS_UNCHECKED  = 0;    //未审核用户
    CONST STATUS_CHECKED    = 1;    //已审核用户
    CONST STATUS_DISABLED   = 2;    //停用用户
}

?>
