<?php

use Think\Model;

/**
 * 进行oauth验证
 * @param $nonce
 * @return string
 */
function checkSignature($nonce, $sign) {

    $signature = substr(md5($nonce . C('TOKEN')), 6, 6);
    if($sign == $signature) {
        return true;
    } else {
        return false;
    }

}

function decodeUnicode($str)
{
    return preg_replace_callback('/\\\\u([0-9a-f]{4})/i',
        create_function(
            '$matches',
            'return mb_convert_encoding(pack("H*", $matches[1]), "UTF-8", "UCS-2BE");'
        ),
        $str);
}

/**
 * 根据客户端IP地址获取城市相关信息
 *
 * @param string $clientIP
 *
 * @return array 返回地理信息Object
 */
function getIP4TaoBao($clientIP){

    $dataURL = 'http://ip.taobao.com/service/getIpInfo.php?ip=' . $clientIP;

    $data = json_decode(@file_get_contents($dataURL));

    return $data;
}

/**
 * 判断电话号码是否合法
 * @param $phone_mob
 */
function checkMobile($phoneMob) {

    $pattern = '/^1[34578][0-9]{9}$/';

    if (!preg_match($pattern, $phoneMob)) {
        return false;
    }

    return true;
}

/**
 * 分页方法，生成前面分页需要的模板内容
 *
 * @param string $count 总数
 * @param string $limit 每页个数
 * @param int    $p     当前页数
 * @param string $prev
 * @param string $nextv
 * @return array 返回page,二维数组
 */
function gtPage($count, $limit, $p = 1, $prev = "上一页", $nextv = "下一页")
{
    $page = array ();
    if ($count == 0) {
        return $page;
    }
    $self = __SELF__;
    if ($pos = strpos($self, "&p=")) {
        $self = substr(__SELF__, 0, $pos);
    }

    if (!strpos($self, "?")) {
        $self .= "?";
    }

    //计算页数
    if ($count % $limit == 0) {
        $number = $count / $limit;
    } else {
        $number = floor($count / $limit) + 1;
    }
    //处理前一页和下一页

    $pre['value']  = $prev;
    $next['value'] = $nextv;
    if ($p <= 1) {
        $pre['url'] = $self . "&p=" . "1";
    } else {
        $pre['url'] = $self . "&p=" . ($p - 1);
    }
    if ($p >= $number) {
        $next['url'] = $self . "&p=" . $number;
    } else {
        $next['url'] = $self . "&p=" . ($p + 1);

    }

    if ($count > $limit) {
        $page[] = $pre;
    }

    for ($i = 1; $i <= $number; $i++) {
        $row['url']   = $self . "&p=" . $i;
        $row['value'] = $i;
        $row['p']     = $p;
        $page[]       = $row;
    }
    if ($count > $limit) {
        $page[] = $next;
    }
    return $page;
}

function getSMSToken() {
    $time = time();
    $token = substr(md5($time . "LUOBOJIANZHI2018"), 6, 6);
    return array('token'=>$token, 'time'=>$time);
}

/**
 * 发送验证码
 * @param $userId 用户ID
 * @param $phone        string 手机号
 * @param $phonecaptcha string 验证码
 * @param $captchaType  string 验证类型
 * @param $role int 区分角色用于短息日志记录 0：企业用户，1：普通用户
 */
function sendCaptcha($userId,$phone, $phonecaptcha, $captchaType = '',$role)
{

    //类型为1时，是找回密码
    if($captchaType == 1){

        //新版服务器短信接口
        $tempid = "JSM40699-0016";
        $clxTempId = "LUOBO_COM_10004";
        $content = '验证码：'.$phonecaptcha.'，在10分钟内有效（非本人操作请忽略本短信，如有疑问拨打客服400-886-3334转1）。【萝卜兼职】';
        //$content = '亲，您正在找回密码，验证码：'.$phonecaptcha.'，切勿将验证码泄露于他人！';  //用于存入数据库记录短信发送日志表
        $smsType = 6; //6表示找回密码
    } elseif ($captchaType == 2){

        //修正绑定的支付宝验证短信
        $tempid = "JSM40699-0017";
        $clxTempId = "LUOBO_COM_10004";
        $content = '验证码：'.$phonecaptcha.'，在10分钟内有效（非本人操作请忽略本短信，如有疑问拨打客服400-886-3334）。【萝卜兼职】';
//        $content = '亲，您正在修改绑定的支付宝，验证码：'.$phonecaptcha.'，切勿将验证码泄露于他人！';
        $smsType = 8;
    } elseif ($captchaType == 4){
      
        $tempid = "JSM40699-0003";
        $clxTempId = "LUOBO_COM_10006";
        $content = '亲，您正在注册萝卜头领，验证码：'.$phonecaptcha.'，在10分钟内有效（非本人操作请忽略本短信，如有疑问拨打客服400-886-3334）。【萝卜兼职】';
        $smsType = 5; //5表示注册
    } else {

        $tempid = "JSM40699-0003";
        $clxTempId = "LUOBO_COM_10006";
        $content = '亲，您正在注册帐号，验证码：'.$phonecaptcha.'，在10分钟内有效（非本人操作请忽略本短信，如有疑问拨打客服400-886-3334）。【萝卜兼职】';
//        $content = '亲，您正在注册帐号，验证码：'.$phonecaptcha.'，切勿将验证码泄露于他人！'; //用于存入数据库记录短信发送日志表
        $smsType = 5; //5表示注册
    }

    $params['templateId'] = $tempid;
    $params['clxTempId'] = $clxTempId;
    $params['template'] = $content;
    $params['args'] = array("@1@"=>$phonecaptcha);

    $params['phone'] = $phone;
    if ($role == 0) {
        $params['companyId'] = $userId;
    }else{
        $params['userId'] = $userId;
    }

    $token = getSMSToken();
    $params = array_merge($token, $params);
    $result = request_post('http://sms.luobojianzhi.com/index.php/Home/Index/send', http_build_query($params));

    return $result;
}

function sendHead($userId,$phone,$type){
    if ($type == 0){
        $tempid = "JSM40699-0022";
        $clxTempId = "LUOBO_COM_10008";
        $content = '亲，您注册萝卜头领成功，并与'.$phone.'用户关联成功，获取的头领收益会发放到用户钱包中！【萝卜兼职】';
    }else{
        $tempid = "JSM40699-0023";
        $clxTempId = "LUOBO_COM_10009";
        $content = '亲，您注册萝卜头领成功，系统自动帮你创建了用户账号，获取的头领收益会发放到用户钱包中。用户名：'.$phone. '初始密码：123456 ！【萝卜兼职】';
    }

    $params['templateId'] = $tempid;
    $params['clxTempId'] = $clxTempId;
    $params['template'] = $content;
    $params['args'] = array("@1@"=>$phone,"@2@"=>123456);
    $params['phone'] = $phone;
    $params['userId'] = $userId;

    $token = getSMSToken();
    $params = array_merge($token, $params);
    $result = request_post('http://sms.luobojianzhi.com/index.php/Home/Index/send', http_build_query($params));

    return $result;
}

/**
 * 诚立业短信平台发送验证码 （注册&找回密码）
 * @param $userId 用户ID
 * @param $phone 手机号
 * @param $phonecaptcha 手机验证码
 * @param $captchaType  验证类型
 * @param $role int 区分角色用于短息日志记录 0：企业用户，1：普通用户
 */
function sendCaptchaTwo($userId,$phone,$phonecaptcha,$captchaType='',$role){
    //类型为1时，是找回密码
    if($captchaType == 1){

        $content = '验证码：'.$phonecaptcha.'，在10分钟内有效（非本人操作请忽略本短信，如有疑问拨打客服400-886-3334转1）。【萝卜兼职】';
        $smsType = 6; //6表示找回密码
        $userId = 'NULL';  //解决忘记密码时无法获取userid导致后面的sql语句无法执行问题
    } elseif ($captchaType == 2){

        //修正绑定的支付宝验证短信
        $content = '验证码：'.$phonecaptcha.'，在10分钟内有效（非本人操作请忽略本短信，如有疑问拨打客服400-886-3334）。【萝卜兼职】';
        $smsType = 8;
    } else {

        $content = '亲，您正在注册帐号，验证码：'.$phonecaptcha.'，在10分钟内有效（非本人操作请忽略本短信，如有疑问拨打客服400-886-3334）。【萝卜兼职】';
        $smsType = 5; //5表示找回密码
    }
    $url = "http://www.sms-cly.cn/smsSend.do?";
    $username = 'clylxlbjz';
    $pwd = 'w9ufzrze';
    $password = md5($username.''.md5($pwd));
    $param = http_build_query(
        array(
            'username'=>$username,
            'password'=>$password,
            'mobile'=>$phone,
            'content'=>$content
        )
    );
    $ch = curl_init();
    curl_setopt($ch,CURLOPT_URL,$url);
    curl_setopt($ch,CURLOPT_HEADER,0);
    curl_setopt($ch,CURLOPT_RETURNTRANSFER,1);
    curl_setopt($ch,CURLOPT_POST,1);
    curl_setopt($ch,CURLOPT_POSTFIELDS,$param);
    $result = curl_exec($ch);
    curl_close($ch);

    if ($result > 0) {
        if ($role == 0) {
            smsLog('NULL','NULL',$userId,$phone,$smsType,$content,2);
        } else {
            smsLog($userId,'NULL','NULL',$phone,$smsType,$content,2);
        }
    } else {
        if ($role == 0) {
            smsLog('NULL','NULL',$userId,$phone,$smsType,$content,3);
        } else {
            smsLog($userId,'NULL','NULL',$phone,$smsType,$content,3);
        }
    }
    return $result;
}


//向企业发送用户申请的短信
function sendAplMsgToCop($userId,$companyId,$jobId,$phone, $cop, $title, $username, $userphone)
{
    if($username) {
        $tempid = "JSM40699-0015";
        $clxTempId = "LUOBO_COM_10003";
        $content = '尊敬的“'.$cop.'”，萝卜兼职提醒您招聘的职位“'.$title.'”收到了“'.$username.'”的申请，联系方式：'.$userphone.'。请及时联系（招兼职拨打400-886-3334）。【萝卜兼职】';
//        $content = '尊敬的“'.$cop.'”，您发布的“'.$title.'”收到了来自萝卜兼职用户“'.$username.'”的申请。联系方式 '.$userphone.'，请及时登陆微信公众号"萝卜小助手"企业中心处理。';
    } else {
        $tempid = "JSM40699-0012";
        $clxTempId = "LUOBO_COM_10001";
        $content = '尊敬的“'.$cop.'”，萝卜兼职提醒您招聘的职位“'.$title.'”收到了来自“萝卜兼职”会员的申请，联系方式：'.$userphone.'。请及时联系（招兼职拨打400-886-3334）。【萝卜兼职】';
//        $content = '尊敬的“'.$cop.'”，您发布的“'.$title.'”收到了来自“萝卜兼职”会员的申请。联系方式 '.$userphone.'，请及时登陆微信公众号“萝卜小助手”-“企业中心”处理。';
    }

    $params['templateId'] = $tempid;
    $params['clxTempId'] = $clxTempId;
    $params['template'] = $content;
    $params['args'] = array("@1@"=>$cop,"@2@"=>$title,"@3@"=>$userphone,"@4@"=>$username);

    $params['phone'] = $phone;
    $params['companyId'] = $companyId;
    $params['userId'] = $userId;
    $params['jobId'] = $jobId;


    $token = getSMSToken();
    $params = array_merge($token, $params);
    $result = request_post('http://sms.luobojianzhi.com/index.php/Home/Index/send', http_build_query($params));

    return $result;
}

//向企业发送用户申请的微信
function sendAplWechatMsgToCop($userId, $cop, $title, $username, $userphone, $openId,$source="http://test-wechat.luobojianzhi.com") {
    if ($openId) {
        if ($username) {
            $tempid = "1000";
            $data = array("first" => array(
                "value" => "尊敬的".$cop."，萝卜兼职提醒您招聘的职位收到了申请。\n",
                "color" => "#C0C0C0"
            ),
                "keyword1" => array(
                    "value" => "  ".$title,
                    "color" => "#000000"
                ),
                "keyword2" => array(
                    "value" => "  ".$username."\n",
                    "color" => "#000000"
                ),
                "remark" => array(
                    "value" => "申请人联系方式：" . $userphone . "。请及时联系（招兼职拨打400-886-3334）。",
                    "color" => "#FF6100",
                ));
        } else {
            $tempid = "1000";
            $data = array("first" => array(
                "value" => "尊敬的".$cop."，萝卜兼职提醒您招聘的职位收到了申请。\n",
                "color" => "#C0C0C0"
            ),
                "keyword1" => array(
                    "value" => "  ".$title,
                    "color" => "#000000"
                ),
                "keyword2" => array(
                    "value" => "  萝卜兼职会员\n",
                    "color" => "#000000"
                ),
                "remark" => array(
                    "value" => "申请人联系方式：" . $userphone . "。请及时联系（招兼职拨打400-886-3334）。",
                    "color" => "#FF6100",
                ));
        }

        $params['templateCode'] = $tempid;
        $params['openId'] = $openId;
        $params['userId'] = $userId;
        $params['data'] = $data;
        // TODO 测试环境
        $params['source'] = $source;
        // 正式环境
        //$params['source'] = "http://wechat.luobojianzhi.com";
        $token = getSMSToken();
        $params = array_merge($token, $params);
        // TODO 测试环境
        $result = request_urlencode_post('http://test-scrum.luobojianzhi.com/index.php/Home/Wechat/doSend', http_build_query($params));
        //  正式环境
//    $result=request_urlencode_post('http://sms.luobojianzhi.com/index.php/Home/Wechat/doSend', http_build_query($params));

    }
    return $result;
}


//诚立业平台向企业发送用户申请的短信
function sendAplMsgToCopTwo($userId,$companyId,$jobId,$phone,$cop,$title,$username,$userphone) {
    if ($username) {
        $content = '尊敬的“'.$cop.'”，萝卜兼职提醒您招聘的职位“'.$title.'”收到了“'.$username.'”的申请，联系方式：'.$userphone.'。请及时联系（400-886-3334）。【萝卜兼职】';
    } else {
        $content = '尊敬的“'.$cop.'”，萝卜兼职提醒您招聘的职位“'.$title.'”收到了来自“萝卜兼职”会员的申请，联系方式：'.$userphone.'。请及时联系（400-886-3334）。【萝卜兼职】';
    }
    $url = "http://www.sms-cly.cn/smsSend.do?";
    $username = 'clylxlbjz';
    $pwd = 'w9ufzrze';
    $password = md5($username.''.md5($pwd));
    $param = http_build_query(
        array(
            'username'=>$username,
            'password'=>$password,
            'mobile'=>$phone,
            'content'=>$content
        )
    );
    $ch = curl_init();
    curl_setopt($ch,CURLOPT_URL,$url);
    curl_setopt($ch,CURLOPT_HEADER,0);
    curl_setopt($ch,CURLOPT_RETURNTRANSFER,1);
    curl_setopt($ch,CURLOPT_POST,1);
    curl_setopt($ch,CURLOPT_POSTFIELDS,$param);
    $result = curl_exec($ch);
    curl_close($ch);

    if ($result > 0) {
        smsLog($userId,$jobId,$companyId,$phone,1,$content,2);
    } else {
        smsLog($userId,$jobId,$companyId,$phone,1,$content,3);
    }

    return $result;
}

//向用户发送企业同意的短信
function sendAgrMsgToUser($userId,$companyId,$jobId,$phone, $cop, $title, $username, $copphone)
{
    $tempid = "JSM40699-0013";
    $clxTempId = "LUOBO_COM_10002";
    $content = '亲爱的“'.$username.'”，萝卜兼职提醒您申请的“'.$title.'”职位已通过企业审核，企业联系方式：'.$copphone.'，请及时联系（萝卜头领全国招募进行时，报名热线400-886-3334）。【萝卜兼职】';
//    $content = '亲爱的“'.$username.'”，您申请“'.$cop.'”的“'.$title.'”职位已通过。联系方式 '.$copphone.'，请及时登陆微信公众号“萝卜小助手”-“个人中心”查看。';

    $params['templateId'] = $tempid;
    $params['clxTempId'] = $clxTempId;
    $params['template'] = $content;
    $params['args'] = array("@1@"=>$username,"@3@"=>$title,"@4@"=>$copphone);

    $params['phone'] = $phone;
    $params['companyId'] = $companyId;
    $params['userId'] = $userId;
    $params['jobId'] = $jobId;


    $token = getSMSToken();
    $params = array_merge($token, $params);
    $result = request_post('http://sms.luobojianzhi.com/index.php/Home/Index/send', http_build_query($params));

    return $result;

}

//向用户发送企业同意的短信
function sendAgrWechatMsgToUser($userId, $title, $username,$copname, $copphone, $openId,$source="http://test-wechat.luobojianzhi.com")
{
    if ($openId) {
            $username=empty($username)?"萝卜兼职会员":$username;
            $tempid = "1001";
            $data = array("first" => array(
                "value" => "亲爱的".$username."，萝卜兼职提醒您申请的职位已通过".$copname."企业审核。\n",
                "color" => "#C0C0C0"
            ),
                "keyword1" => array(
                    "value" => '  “'.$title.'”职位',
                    "color" => "#000000"
                ),
                "keyword2" => array(
                    "value" => "  审核通过",
                    "color" => "#000000"
                ),
                "keyword3" => array(
                    "value" => "  ".$username."\n",
                    "color" => "#000000"
                ),
                "remark" => array(
                    "value" => "企业联系方式：".$copphone."，请及时联系（萝卜头领全国招募进行时，报名热线400-886-3334）。【萝卜兼职】",
                    "color" => "#FF6100",
                ));

        $params['templateCode'] = $tempid;
        $params['openId'] = $openId;
        $params['userId'] = $userId;
        $params['data'] = $data;
        // TODO 测试环境
        $params['source'] = $source;
        // 正式环境
        //$params['source'] = "http://wechat.luobojianzhi.com";
        $token = getSMSToken();
        $params = array_merge($token, $params);
        // TODO 测试环境
        $result = request_urlencode_post('http://test-scrum.luobojianzhi.com/index.php/Home/Wechat/doSend', http_build_query($params));
        //  正式环境
//    $result=request_urlencode_post('http://sms.luobojianzhi.com/index.php/Home/Wechat/doSend', http_build_query($params));

    }
    return $result;
}

//诚立业平台向用户发送企业同意的短信
function sendAgrMsgToUserTwo($userId,$companyId,$jobId,$phone, $cop, $title, $username, $copphone) {
    $content = '亲爱的“'.$username.'”，萝卜兼职提醒您申请的“'.$title.'”职位已通过企业审核，企业联系方式：'.$copphone.'，请及时联系（400-886-3334）。【萝卜兼职】';
    $url = "http://www.sms-cly.cn/smsSend.do?";
    $username = 'clylxlbjz';
    $pwd = 'w9ufzrze';
    $password = md5($username.''.md5($pwd));
    $param = http_build_query(
        array(
            'username'=>$username,
            'password'=>$password,
            'mobile'=>$phone,
            'content'=>$content
        )
    );
    $ch = curl_init();
    curl_setopt($ch,CURLOPT_URL,$url);
    curl_setopt($ch,CURLOPT_HEADER,0);
    curl_setopt($ch,CURLOPT_RETURNTRANSFER,1);
    curl_setopt($ch,CURLOPT_POST,1);
    curl_setopt($ch,CURLOPT_POSTFIELDS,$param);
    $result = curl_exec($ch);
    curl_close($ch);

    if ($result > 0) {
        smsLog($userId,$jobId,$companyId,$phone,2,$content,2);
    } else {
        smsLog($userId,$jobId,$companyId,$phone,2,$content,3);
    }

    return $result;
}

/**
 * 支付密码重置短信提醒
 * @param $userId  用户ID
 * @param $phone  手机号
 * @param $phonecaptcha 验证码
 * @param $role int 区分角色用于短息日志记录 0：企业用户，1：普通用户
 */
function payReset($userId,$phone,$phonecaptcha,$role){
    $tempid = 'JSM40699-0017';
    $clxTempId = "LUOBO_COM_10002";
    $content = '验证码：'.$phonecaptcha.'，在10分钟内有效（非本人操作请忽略本短信，如有疑问拨打客服400-886-3334）。【萝卜兼职】';
//    $content = '亲，您正在进行支付密码修改操作，短信验证码为：'.$phonecaptcha.'，如果非本人操作，请忽略此短信。';
    $params['templateId'] = $tempid;
    $params['clxTempId'] = $clxTempId;
    $params['template'] = $content;
    $params['args'] = array("@1@"=>$phonecaptcha);

    $params['phone'] = $phone;
    if ($role == 0) {
        # code...
        $params['companyId'] = $userId;
    }else{
        $params['userId'] = $userId;
    }

    $token = getSMSToken();
    $params = array_merge($token, $params);
    $result = request_post('http://sms.luobojianzhi.com/index.php/Home/Index/send', http_build_query($params));

    return $result;


}
/**
 * 诚立信支付密码重置短信提醒
 * @param $userId  用户ID
 * @param $phone  手机号
 * @param $phonecaptcha 验证码
 * @param $role int 区分角色用于短息日志记录 0：企业用户，1：普通用户
 */
function payResetTwo($userId,$phone,$phonecaptcha,$role) {
    $content = '验证码：'.$phonecaptcha.'，在10分钟内有效（非本人操作请忽略本短信，如有疑问拨打客服400-886-3334）。【萝卜兼职】';
    $url = "http://www.sms-cly.cn/smsSend.do?";
    $username = 'clylxlbjz';
    $pwd = 'w9ufzrze';
    $password = md5($username.''.md5($pwd));
    $param = http_build_query(
        array(
            'username'=>$username,
            'password'=>$password,
            'mobile'=>$phone,
            'content'=>$content
        )
    );
    $ch = curl_init();
    curl_setopt($ch,CURLOPT_URL,$url);
    curl_setopt($ch,CURLOPT_HEADER,0);
    curl_setopt($ch,CURLOPT_RETURNTRANSFER,1);
    curl_setopt($ch,CURLOPT_POST,1);
    curl_setopt($ch,CURLOPT_POSTFIELDS,$param);
    $result = curl_exec($ch);

    curl_close($ch);
    if ($result > 0) {
        if ($role == 0) {
            smsLog('NULL','NULL',$userId,$phone,7,$content,2);
        } else {
            smsLog($userId,'NULL','NULL',$phone,7,$content,2);
        }
    } else {
        if ($role == 0) {
            smsLog('NULL','NULL',$userId,$phone,7,$content,3);
        } else {
            smsLog($userId,'NULL','NULL',$phone,7,$content,3);
        }
    }

    return $result;
}

/**
 * 短信发送日志记录
 * @param $userId 用户ID
 * @param $jobId 工作ID
 * @param $companyId 公司ID
 * @param $phone 手机号
 * @param $type 发送短信的类型 包括：1：申请工作 2：通过申请 3：拒绝申请 4：完成工作 5:注册 6：找回密码 7：更改支付密码
 * @param $content 内容
 * @param $status 状态  （短信是否发送成功）发送状态 1：成功 2：转发成功 3：失败
 */
function smsLog($userId,$jobId,$companyId,$phone,$type,$content,$status){
    $db=new Model();
    $insertSql = "INSERT INTO SMS_LOG (userId,companyId,jobId,phone,type,content,status) VALUES ($userId,$companyId,$jobId,$phone,$type,'$content',$status)";
    return $db->execute($insertSql);
}

/**
 * 判断是否键存在，并不为空值
 *
 * @param $must  array 需要存在的键
 * @param $no    array 不能存在的键
 * @param $array array
 * @return string null
 */
function haskey($array, $must = array (), $no = array ())
{
    $re = NULL;
    foreach ($must as $value) {
        if ($array[$value] == "" || $array[$value] == NULL) {
            $re .= $value . " is necessary;";
        }
    }
    foreach ($no as $value) {
        if (array_key_exists($no, $array)) {
            $re .= $value . " is refused;";
        }
    }
    if ($re != NULL) {
        $result['info']  = $re;
        $result['state'] = "0";
        $result          = json_encode($result);
        return $result;
    } else {
        return NULL;
    }
}

function getIPaddress()
{
    $IPaddress = '';
    if (isset($_SERVER)) {
        if (isset($_SERVER["HTTP_X_FORWARDED_FOR"])) {
            $IPaddress = $_SERVER["HTTP_X_FORWARDED_FOR"];
        } else {
            if (isset($_SERVER["HTTP_CLIENT_IP"])) {
                $IPaddress = $_SERVER["HTTP_CLIENT_IP"];
            } else {
                $IPaddress = $_SERVER["REMOTE_ADDR"];
            }
        }
    } else {
        if (getenv("HTTP_X_FORWARDED_FOR")) {
            $IPaddress = getenv("HTTP_X_FORWARDED_FOR");
        } else {
            if (getenv("HTTP_CLIENT_IP")) {
                $IPaddress = getenv("HTTP_CLIENT_IP");
            } else {
                $IPaddress = getenv("REMOTE_ADDR");
            }
        }
    }
    return $IPaddress;
}

function taobaoIP($clientIP)
{
    $taobaoIP = 'http://ip.taobao.com/service/getIpInfo.php?ip=' . $clientIP;
    $IPinfo   = json_decode(file_get_contents($taobaoIP));
    //$province = $IPinfo->data->region;
    $city = $IPinfo->data->city;
    if (empty($city)) {
        $data = '威海市';
    } else {
        $data = $city;
    }
    return $data;
}

//判断数组中是否有空值
function hasNull($arr)
{
    foreach ($arr as $key => $value) {
        if (empty($value)) {
            return TRUE;
        }
    }
    return FALSE;
}

/**
 * 是否移动端访问访问
 *
 * @return bool
 */
function isMobile()
{
    // 如果有HTTP_X_WAP_PROFILE则一定是移动设备
    if (isset ($_SERVER['HTTP_X_WAP_PROFILE'])) {
        return TRUE;
    }
    // 如果via信息含有wap则一定是移动设备,部分服务商会屏蔽该信息
    if (isset ($_SERVER['HTTP_VIA'])) {
        // 找不到为flase,否则为true
        return stristr($_SERVER['HTTP_VIA'], "wap") ? TRUE : FALSE;
    }
    // 脑残法，判断手机发送的客户端标志,兼容性有待提高
    if (isset ($_SERVER['HTTP_USER_AGENT'])) {
        $clientkeywords = array ('nokia',
                                 'sony',
                                 'ericsson',
                                 'mot',
                                 'samsung',
                                 'htc',
                                 'sgh',
                                 'lg',
                                 'sharp',
                                 'sie-',
                                 'philips',
                                 'panasonic',
                                 'alcatel',
                                 'lenovo',
                                 'iphone',
                                 'ipod',
                                 'blackberry',
                                 'meizu',
                                 'android',
                                 'netfront',
                                 'symbian',
                                 'ucweb',
                                 'windowsce',
                                 'palm',
                                 'operamini',
                                 'operamobi',
                                 'openwave',
                                 'nexusone',
                                 'cldc',
                                 'midp',
                                 'wap',
                                 'mobile'
        );
        // 从HTTP_USER_AGENT中查找手机浏览器的关键字
        if (preg_match("/(" . implode('|', $clientkeywords) . ")/i", strtolower($_SERVER['HTTP_USER_AGENT']))) {
            return TRUE;
        }
    }
    // 协议法，因为有可能不准确，放到最后判断
    if (isset ($_SERVER['HTTP_ACCEPT'])) {
        // 如果只支持wml并且不支持html那一定是移动设备
        // 如果支持wml和html但是wml在html之前则是移动设备
        if ((strpos($_SERVER['HTTP_ACCEPT'], 'vnd.wap.wml') !== FALSE) && (strpos($_SERVER['HTTP_ACCEPT'],
            'text/html') === FALSE || (strpos($_SERVER['HTTP_ACCEPT'],
            'vnd.wap.wml') < strpos($_SERVER['HTTP_ACCEPT'],
               'text/html')))
        ) {
            return TRUE;
        }
    }
    return FALSE;
}


//导出Excel表格
function export($data, $excelFileName, $sheetTitle, $firstrow)
{

    /* 实例化类 */
    import('Vendor.phpExcel.PHPExcel');
    $objPHPExcel = new PHPExcel();

    /* 设置输出的excel文件为2007兼容格式 */
    //$objWriter=new PHPExcel_Writer_Excel5($objPHPExcel);//非2007格式
    $objWriter = new PHPExcel_Writer_Excel2007($objPHPExcel);

    $csvWriter = new PHPExcel_Writer_CSV($objPHPExcel);
    /* 设置当前的sheet */
    $objPHPExcel->setActiveSheetIndex(0);

    $objActSheet = $objPHPExcel->getActiveSheet();

    /*设置宽度*/
    $objPHPExcel->getActiveSheet()->getColumnDimension('A')->setWidth(10);
    $objPHPExcel->getActiveSheet()->getColumnDimension('B')->setWidth(10);
    $objPHPExcel->getActiveSheet()->getColumnDimension('C')->setWidth(10);
    $objPHPExcel->getActiveSheet()->getColumnDimension('D')->setWidth(40);


    /* sheet标题 */
    $objActSheet->setTitle($sheetTitle);
    $j = 'A';
    foreach ($firstrow as $value) {
        $objActSheet->setCellValue($j . '1', $value);
        $j++;
    }

    $i = 2;
    foreach ($data as $value) {
        /* excel文件内容 */
        $j = 'A';
        foreach ($value as $value2) {
            //            $value2=iconv("gbk","utf-8",$value2);
            $objActSheet->setCellValue($j . $i, $value2);
            $j++;
        }
        $i++;
    }


    /* 生成到浏览器，提供下载 */
    ob_end_clean();  //清空缓存
    header("Pragma: public");
    header("Expires: 0");
    header("Cache-Control:must-revalidate,post-check=0,pre-check=0");
    header("Content-Type:application/force-download");
    header("Content-Type:application/vnd.ms-execl");
    header("Content-Type:application/octet-stream");
    header("Content-Type:application/download");
    header('Content-Disposition:attachment;filename="' . $excelFileName . '.xlsx"');
    header("Content-Transfer-Encoding:binary");
    $objWriter->save('php://output');
}

function exportCSV($data, $excelFileName, $sheetTitle, $firstrow)
{

    /* 实例化类 */
    import('Vendor.phpExcel.PHPExcel');
    $objPHPExcel = new PHPExcel();

    /* 设置输出的excel文件为2007兼容格式 */
    //$objWriter=new PHPExcel_Writer_Excel5($objPHPExcel);//非2007格式

    $csvWriter = new PHPExcel_Writer_CSV($objPHPExcel, 'CSV');
    /* 设置当前的sheet */
    $objPHPExcel->setActiveSheetIndex(0);

    $objActSheet = $objPHPExcel->getActiveSheet();

    /*设置宽度*/
    $objPHPExcel->getActiveSheet()->getColumnDimension('A')->setWidth(10);
    $objPHPExcel->getActiveSheet()->getColumnDimension('B')->setWidth(10);
    $objPHPExcel->getActiveSheet()->getColumnDimension('C')->setWidth(10);
    $objPHPExcel->getActiveSheet()->getColumnDimension('D')->setWidth(40);


    /* sheet标题 */
    $objActSheet->setTitle($sheetTitle);
    $j = 'A';
    foreach ($firstrow as $value) {
        $objActSheet->setCellValue($j . '1', $value);
        $j++;
    }

    $i = 2;
    foreach ($data as $value) {
        /* excel文件内容 */
        $j = 'A';
        foreach ($value as $value2) {
            //            $value2=iconv("gbk","utf-8",$value2);
            $objActSheet->setCellValue($j . $i, $value2);
            $j++;
        }
        $i++;
    }


    /* 生成到浏览器，提供下载 */
    ob_end_clean();  //清空缓存
    header("Pragma: public");
    header("Expires: 0");
    header('Content-Type: application/vnd.ms-excel;charset=gbk');
    header("Cache-Control:must-revalidate,post-check=0,pre-check=0");
    header("Content-Type:application/force-download");
    header("Content-Type:application/vnd.ms-execl");
    header("Content-Type:application/octet-stream");
    header("Content-Type:application/download");
    header('Content-Disposition:attachment;filename="' . $excelFileName . '.csv"');
    header("Content-Transfer-Encoding:binary");
    $csvWriter->save('php://output');
}

function exportCSVFile($data, $excelFileName, $firstrow)
{
    $content = "";

    foreach ($firstrow as $value) {
        $content .= $value . ',';
    }
    substr($content, 0, count($content) - 1);
    $content .= "\r\n";

    foreach ($data as $row) {
        foreach ($row as $value) {
            $content .= $value . ',';
        }
        substr($content, 0, count($content) - 1);

        $content .= "\r\n";
    }

    $file = APP_PATH . '../Public/export/' . $excelFileName . ".csv";
    $re   = file_put_contents($file, $content);
    if ($re) {
        header("Location: http://www.luobojianzhi.com/Public/export/".$excelFileName.".csv");
    }

}



function CalculationValidate(){
     session_start();
      $w=100;
      $h=30;
      $img = imagecreate($w,$h);
      $gray = imagecolorallocate($img,255,255,255);
      $black = imagecolorallocate($img,rand(0,200),rand(0,200),rand(0,200));
      $red = imagecolorallocate($img, 255, 0, 0);
      $white = imagecolorallocate($img, 255, 255, 255);
      $green = imagecolorallocate($img, 0, 255, 0);
      $blue = imagecolorallocate($img, 0, 0, 255);
      imagefilledrectangle($img, 0, 0, $w, $h, $black);
     
     
      for($i = 0;$i < 80;$i++){
        imagesetpixel($img, rand(0,$w), rand(0,$h), $gray);
      }

      $num1 = rand(1,99);
      $num2 = rand(1,99);
      $res=0;
      $code=rand(0,1);
       switch ($code) {
        case 0:
          $res=$num1+$num2;
          $symbol="+";
          break;
        case 1:
          $res=$num1-$num2;
          $symbol="-";
          break;
        default:
          # code...
          break;
        }
      imagestring($img, 5, 5, rand(1,10), $num1, $red);
      imagestring($img,5,30,rand(1,10),$symbol, $white);
      imagestring($img,5,45,rand(1,10),$num2, $green);
      imagestring($img,5,65,rand(1,10),"=", $blue);
      imagestring($img,5,80,rand(1,10),"?", $red);
      
      $_SESSION['res']=$res;
    
      header("content-type:image/png");
      imagepng($img);
      imagedestroy($img);
}

//获取月份的最后一天
function getMonthLastDay($year, $month){
    $t = mktime(0, 0, 0, $month + 1, 1, $year);
    $t = $t - 60 * 60 * 24;
    return $t;
}


/**
 * 检测当前用户是否为管理员
 * @return boolean true-管理员，false-非管理员
 * @author wxxd <wangyun1277.qq.com>
 */
function is_administrator($adminname = null)
{
    $adminname = is_null($adminname) ? $_SESSION['user']['adminname'] : $adminname;
    return $adminname =='luobo'?true:false;
}


function request_get($url)
{
    $curl = curl_init();
    curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($curl, CURLOPT_TIMEOUT, 500);
    curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($curl, CURLOPT_SSL_VERIFYHOST, false);
    curl_setopt($curl, CURLOPT_URL, $url);

    $res = curl_exec($curl);
    curl_close($curl);

    return $res;
}

/**
 * 发送post请求
 * @param string $url
 * @param string $param
 * @return bool|mixed
 */
function request_post($url = '', $param = '')
{
    if (empty($url) || empty($param)) {
        return false;
    }
    $postUrl = $url;
    $curlPost = $param;
    $ch = curl_init(); //初始化curl
    curl_setopt($ch, CURLOPT_URL, $postUrl); //抓取指定网页
    curl_setopt($ch, CURLOPT_HEADER, 0); //设置header
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1); //要求结果为字符串且输出到屏幕上
    curl_setopt($ch, CURLOPT_POST, 1); //post提交方式
    curl_setopt($ch, CURLOPT_POSTFIELDS, $curlPost);
    $data = curl_exec($ch); //运行curl
    curl_close($ch);
    return $data;
}

function request_urlencode_post($url = '', $param = '')
{
    if (empty($url) || empty($param)) {
        return false;
    }
    $postUrl = $url;
    $curl = curl_init(); //初始化curl
    curl_setopt($curl, CURLOPT_URL, $postUrl);
    curl_setopt($curl, CURLOPT_USERAGENT, 'Opera/9.80 (Windows NT 6.2; Win64; x64) Presto/2.12.388 Version/12.15');
    curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, false); // stop verifying certificate
    curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($curl, CURLOPT_POST, true);
    curl_setopt($curl, CURLOPT_HTTPHEADER, array('Content-Type: application/x-www-form-urlencoded'));
    curl_setopt($curl, CURLOPT_POSTFIELDS, $param);
    curl_setopt($curl, CURLOPT_FOLLOWLOCATION, true);
    $data = curl_exec($curl); //运行curl
    curl_close($curl);
    return $data;
}

function json_error($msg)
{
    $arr = array(
        'errcode' => 1,
        'errmsg' => $msg
    );

    echo json_encode($arr);
    exit();
}

function json_success($msg)
{
    $arr = array(
        'errcode' => 0,
        'errmsg' => $msg
    );

    echo json_encode($arr);
    exit();
}

function json_echo($arr)
{
    echo json_encode($arr);
    exit();
}

function get_promot_status($promot_info)
{
    if(!$promot_info)
    {
        return '未申请';
    }

    if($promot_info['status'] == 0)
    {
        return '审核中';
    }
    else if($promot_info['status'] == 1)
    {
        return '我是头领';
    }
    else if($promot_info['status'] == 2)
    {
        return '审核失败';
    }
}


/**
 * 系统加密方法
 * @param string $data 要加密的字符串
 * @param string $key  加密密钥
 * @param int $expire  过期时间 单位 秒
 * @return string
 * @author 麦当苗儿 <zuojiazi@vip.qq.com>
 */
function think_encrypt($data, $key = '', $expire = 0) {
    $key  = md5(empty($key) ? C('DATA_AUTH_KEY') : $key);
    $data = base64_encode($data);
    $x    = 0;
    $len  = strlen($data);
    $l    = strlen($key);
    $char = '';

    for ($i = 0; $i < $len; $i++) {
        if ($x == $l) $x = 0;
        $char .= substr($key, $x, 1);
        $x++;
    }

    $str = sprintf('%010d', $expire ? $expire + time():0);

    for ($i = 0; $i < $len; $i++) {
        $str .= chr(ord(substr($data, $i, 1)) + (ord(substr($char, $i, 1)))%256);
    }
    return str_replace(array('+','/','='),array('-','_',''),base64_encode($str));
}

/**
 * 系统解密方法
 * @param  string $data 要解密的字符串 （必须是think_encrypt方法加密的字符串）
 * @param  string $key  加密密钥
 * @return string
 * @author 麦当苗儿 <zuojiazi@vip.qq.com>
 */
function think_decrypt($data, $key = ''){
    $key    = md5(empty($key) ? C('DATA_AUTH_KEY') : $key);
    $data   = str_replace(array('-','_'),array('+','/'),$data);
    $mod4   = strlen($data) % 4;
    if ($mod4) {
        $data .= substr('====', $mod4);
    }
    $data   = base64_decode($data);
    $expire = substr($data,0,10);
    $data   = substr($data,10);

    if($expire > 0 && $expire < time()) {
        return '';
    }
    $x      = 0;
    $len    = strlen($data);
    $l      = strlen($key);
    $char   = $str = '';

    for ($i = 0; $i < $len; $i++) {
        if ($x == $l) $x = 0;
        $char .= substr($key, $x, 1);
        $x++;
    }

    for ($i = 0; $i < $len; $i++) {
        if (ord(substr($data, $i, 1))<ord(substr($char, $i, 1))) {
            $str .= chr((ord(substr($data, $i, 1)) + 256) - ord(substr($char, $i, 1)));
        }else{
            $str .= chr(ord(substr($data, $i, 1)) - ord(substr($char, $i, 1)));
        }
    }
    return base64_decode($str);


}

function validateLogin() {

    $user = session('x_user');
    if(empty($user)){
         return 0;
    } else {
        return 1;
    }
}

function validateCompanyLogin() {

    $user = session('x_company');
    if(empty($user)){
         return 0;
    } else {
        return 1;
    }
}

    function cityTransfer($city)
    {
       $db =new Model();
       $city = "'".$city."'";
       $transSQL ="SELECT id from gp_city where name =$city";
       
       $result = $db->execute($transSQL);
       
       return $result;
    }

      function schoolTransfer($school)
    {
       $db =new Model();
       $school = "'".$school."'";
       $transSQL ="SELECT id from gp_school where name =$school";
       
       $result = $db->execute($transSQL);
    
       return $result;
    }
	
	function getParentCate($pCateId)
	 {
       $db = new Model();

       $sql = "SELECT name FROM job_category where id= '$pCateId'";

       $result =$db->query($sql);

       return $result[0]['name'];
	 }
    
    function isWhitePhoneNumber($phone){
        
        $prefix = C('TEST_PHONE_PREFIX');
        
        if(!is_numeric($phone)){
            
            return false;
        }
        
        $prefixPhone = substr($phone, 0, strlen($prefix));
        $suffixPhone = substr($phone, strlen($prefix), strlen($phone)-strlen($prefix));
        if($prefix != $prefixPhone){
            
            return false;
        }
        
        $suffixInt = intval($suffixPhone);
        
        if($suffixInt >= 1 && $suffixInt <= 999){
            
            return true;
        }
        
        return false;
        
    } 



