<?php

return array(

	'DB_TYPE'=>'mysql',// 数据库类型
	'DB_HOST'=>'bdm25683027.my3w.com',// 服务器地址
	'DB_NAME'=>'bdm25683027_db',// 数据库名
	'DB_USER'=>'bdm25683027',// 用户名
	'DB_PWD'=>'aq123456',// 密码

	'DB_PORT'=>3306,// 端口
	//'DB_PREFIX'=>'think_',// 数据库表前缀
	'DB_CHARSET'=>'utf8',// 数据库字符集
	//'LOG_RECORD' => true, // 开启日志记录
	//'LOG_LEVEL'  =>'DEBUG', // 只记录EMERG ALERT CRIT ERR 错误	
	'SHOW_PAGE_TRACE' =>false,
	'LOG_FILE_SIZE'=>20971520,


	// 阿里云OSS相关配置
	'OSS_ENDPOINT' => "oss-cn-qingdao.aliyuncs.com",
	'OSS_ACCESS_KEY_ID' => "I0kThHwa60ISiCRK",
	'OSS_ACCESS_KEY_SECRET' => 'VpRMwZQ2xFeWDaj2ZgiEAU5AELKoF9',
	'OSS_URL' => 'http://luobojianzhi-image.oss-cn-qingdao.aliyuncs.com/',
	'OSS_BUCKET' => 'luobojianzhi-image',
    
	//后台系统地址
	'MANAGER-HOST' => "http://test-manager.luobojianzhi.com",
	'WECHAT-HOST' => "wechat.luobojianzhi.com",
    'PC-HOST' => "115.28.186.139",

	//Wechat相关配置(萝卜助手)
	'WT_ACCOUNT' => 'luobozhushou',
	'WT_APP_ID' => 'wx9b0ac7d9a41b05c2',
	'WT_APP_SECRET' => 'f0ee412502356aed3c793238ac956b75',
	'WT_APP_TOKEN' => 'wechat_luobojianzhi_com',

	//鉴权配置
	'TOKEN' => "LUOBOJIANZHICOM",

	//测试环境
	'ENV' => 'TEST',
    
    //微信支付环境设置
    'WxPayConf'=>array(
        'MCHID' => '1378399002',
        'KEY' => '1qaz2wsx3edc4rfv5tgb6yhn7ujm7ujm',
        'JS_API_CALL_URL' => '/index.php/Home/WxJsAPI/jsApiCall',
        'SSLCERT_PATH' => '/ThinkPHP/Library/Org/WxPay/cert/apiclient_cert.pem',
        'SSLKEY_PATH' => '/ThinkPHP/Library/Org/WxPay/cert/apiclient_key.pem',
        'NOTIFY_URL' =>  '/index.php/Company/Wallet/notify',
        'CURL_TIMEOUT' => 30
    ),
    
    //钱包收益相关设置
    'GAINS_YEAR_RATE' => 0.10, //年收益率10%
    'GAINS_PROMOT_ADMIN' => 0.2, //加盟商每推广一个人加0.2元
    'GAINS_PROMOT_CHIEF' => 0.4, //头领每推广一个人加0.4元

	'CREDIT_CARD_WHITE_LIST' => 'ozd8juDzk3psbH10GqH3KsL1Uujk,ozd8juDzk3psbH10GqH3KsL1Uujk',
    
    'TEST_PHONE_PREFIX' => '1880631',
);