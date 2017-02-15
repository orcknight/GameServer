<?php

namespace Home\Service;

header("Content-Type: text/html; charset=utf-8");

class JobEnum {

    //支付单位
	const YuanPerHour =1;
	const YuanPerDay = 2;
	const YuanPerWeek = 3;
	const YuanPerMonth = 4;
    //const YuanPerMonth = 5；

    //支付周期
	const paybyDay = 1;
	const payByWeek = 2;
	const payByMonth = 3;
	const payByTime = 4;

    // 一级分类
    const software = 1; //软件开发
    const leaflet = 2;//传单
    const waiter = 3; // 服务员
    const etiquette = 4;//礼仪
    const cuntomerService = 5; //客服
    const hourlyWorker = 6; //小时工
    const part_time = 7; //勤工助学
    const familyTeacher = 8 ;//家教
    const market =9; //销售
    const others =11 ;//其他
    const intern =12 ;//实习生
    const holiday = 16; //假期工
    const line = 17;  //线上兼职

    CONST STATUS_RUNNING = 0; // 报名中
    CONST STATUS_FINISHED = 1; // 已报满
    CONST STATUS_EXPIRED = 2; // 已过期
    CONST STATUS_PACKAGE = 3; // 红包
    CONST STATUS_DOUBLE_PRICE = 4; //双薪
    
    //首页的推荐兼职列表类型
    CONST SEARCH_TYPE_TIME      = 0; //最新兼职
    CONST SEARCH_TYPE_HOT       = 1; //最热兼职
    CONST SEARCH_TYPE_GUARANTEE = 2; //担保交易
    CONST SEARCH_TYPE_LINE      = 3; //线上兼职
    CONST SEARCH_TYPE_TRAINEE   = 4; //名企实习
    CONST SEARCH_TYPE_DISTANCE  = 5; //附近职位
    CONST SEARCH_TYPE_HOLIDAY   = 6; //寒暑假工
    CONST SEARCH_TYPE_INDEXPAGE = 8; //首页显示列表
    CONST SEARCH_TYPE_FULLTIME  = 9; //名企招聘
    
    //工作属性枚举
    CONST ATTR_GUARANTEE    = 1; //担保
    CONST ATTR_PACKAGE      = 2; //红包
    CONST ATTR_DOUBLE       = 3; //双薪
    
    
    CONST USTATUS_APPLYING      = 1; //申请中
    CONST USTATUS_CANCELED      = 2; //已取消
    CONST USTATUS_FINISHING     = 3;//待完成
    CONST USTATUS_FINISHED      = 4; //已完成
    CONST USTATUS_UNPOSTED      = 5; //未通过
    CONST USTATUS_UNFINISHED    = 6; //未完成
    
    CONST CSTATUS_POSTING       = 1; //待通过
    CONST CSTATUS_POSTED        = 2; //已通过
    CONST CSTATUS_REFUSED       = 3; //已拒绝
    CONST CSTATUS_FINISHED      = 4; //已完成
    CONST CSTATUS_UNFINISHED    = 5; //未完成
    
    //工作分类的类型
    CONST CATEGORY_TYPE_PARTTIME = 0; //兼职工作的分类
    CONST CATEGORY_TYPE_FULLTIME = 1; //全职工作的分类

    static function getPriceUnit($key) {

    	switch ($key) {
    		case 1:
    		return  "元/时";
    		break;

    		case 2:
    		return  "元/天";
    		break;
    		case 3:
    		return  "元/周";
    		break;
    		case 4:
    		return  "元/月";
    		break;
            case 5:
            return  "元/次";
            break;

    		default:
    		return  "元/天";
    		break;
    	}

    }

    static function getPayType($key)
    {
    	switch ($key) {
    		case 1:
    		return  "日结";
    		break;

    		case 2:
    		return  "周结";
    		break;
    		case 3:
    		return  "月结";
    		break;
    		case 4:
    		return  "次结";
            case 5:
            return "完工结算";
    		break;
    		default:
    		return  "";
    		break;

    	}
    }

    static  function getParentCate($key)
    {
    	switch ($key) {
    		case 1:
    		return  "派单员";
    		break;

    		case 2:
    		return  "送餐员";
    		break;
    		case 3:
    		return  "服务员";
    		break;
    		case 4:
    		return  "翻译";
    		break;
    		case 5:
    		return  "促销员";
    		break;
    		case 6:
    		return  "家教";
    		break;

    		case 7:
    		return  "助教";
    		break;
    		case 8:
    		return  "问卷";
    		break;
    		case 9:
    		return  "会场";
    		break;
    		case 10:
    		return  "销售";
    		break;
    		case 11:
    		return  "文职";
    		break;
    		case 12:
    		return  "设计";
    		break;
    		case 13:
    		return  "实习";
    		break;
    		case 14:
    		return  "模特";
    		break;
    		case 15:
    		return  "校代";
    		break;
    		case 16:
    		return  "手工艺";
    		break;
    		case 17:
    		return  "收银员";
    		break;
    		case 18:
    		return  "话务员";
    		break;
    		case 19:
    		return  "公益";
    		break;
    		case 20:
    		return  "软件";
    		break;
    		case 21:
    		return  "其他";
    		break;
    		default:
    		return  "其他";
    		break;

    	}
    }
    	static function getCategory($key)
    	{
    		switch ($key) {
    			case 1:
    			return  "android";
    			break;

    			case 2:
    			return  "ios";
    			break;
    			case 3:
    			return  "java";
    			break;
    			case 4:
    			return  "php";
    			break;
    			case 5:
    			return  "客服";
    			break;
    			case 6:
    			return  "小时工";
    			break;
		
    			case 7:
    			return  "家教";
    			break;
    			case 8:
    			return  "销售";
    			break;
    			case 9:
    			return  "实习生";
    			break;
    			case 10:
    			return  "其他";
    			break;
    			default:
    			return  "其他";
    			break;
    		}
    	}

   }

