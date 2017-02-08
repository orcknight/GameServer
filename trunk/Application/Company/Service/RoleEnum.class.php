<?php
namespace Company\Service;

header("Content-Type: text/html; charset=utf-8");

class RoleEnum {

    //角色类型
    CONST ROLE_CUSTOM = 0; //普通用户
    CONST ROLE_AGENT= 1; //代理商
    CONST ROLE_CITY_PM = 2; //城市经理  
    CONST ROLE_FRANCHISE = 5; //加盟商
}
