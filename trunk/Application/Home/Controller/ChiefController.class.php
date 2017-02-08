<?php
/**
 * Created by PhpStorm.
 * User: StandOpen
 * Date: 16/2/27
 * Time: 11:42
 */
namespace Home\Controller;

use Think\Controller;
use Org\Wechat\Jssdk;
use Think\Page;
use Think\Model;
use Home\Service\ChiefService;
use Enum\WalletEnum;

class ChiefController extends WtAuthController {

    public function _initialize() {
        if (ACTION_NAME == 'code' || ACTION_NAME == 'qrcode') {
            $this->trackLog('action_name', 'code');
        } else {
            parent::_initialize();
        }
    }

    public function detail() {

        $this->trackLog("excute", "ChiefController detail()");
        
        $model = new Model();
        $wxid = $this->getWtUserIdByOpenId();
        if($wxid < 1){
            $data['code'] = 0;
            $data['msg'] = '获取信息失败~';  
            $this->ajaxReturn($data);  
        }
        
        $extSql = "SELECT * FROM ac_chief WHERE wxid='".$wxid."'";
        $this->trackLog("extSql", $extSql); 
        $chief_info = $model->query($extSql);
        $this->trackLog("chief_info", $chief_info);
        $this->trackLog("wx_info", session("wt_user"));
        //返回来是个二维数组，取第0个元素
        if($chief_info){
            
            $chief_info = $chief_info[0];
        }

        // 获取排名
        if($chief_info) {
            
            $extSql = "SELECT count(*) as count FROM ac_chief WHERE status = 1 AND number > '".$chief_info['number']."'";
            $this->trackLog("extSql", $extSql); 
            $count = $model->query($extSql);
            $this->trackLog("count", $count);
            $chief_info['rank_number'] = $count[0]['count']+1;
        } else {
            $chief_info['rank_number'] = 0;
        }
        
        //获取收益
        if($chief_info && $chief_info['userid'] > 0) {
            
            $orderType = WalletEnum::ORDERTYPE_PROMOT_GASINS;
            $userId = $chief_info['userid'];
            $extSql = "SELECT SUM(wo.money)/100 AS money FROM wallet_order wo LEFT JOIN wallet w ON w.id = wo.toWalletId
            WHERE orderType = '$orderType' AND w.userId = '$userId'";
            $this->trackLog("extSql", $extSql); 
            $money = $model->query($extSql);
            $this->trackLog("money", $money);
            $chief_info['promot_gains'] = $money[0]['money'];
        } else {
            $chief_info['promot_gains'] = 0;
        }
        
        $chief_info['id'] = think_encrypt($chief_info['id']);
        $data['code'] = 1;
        $data['msg'] = '获取信息成功~';
        $data['data'] = [
            'chief_info' => $chief_info,
            'wx_info' => session("wt_user"),
        ];
        $this->ajaxReturn($data);
    }

    public function code() {
        $this->trackLog("page", "code()");

        $from = addslashes($_REQUEST['f']);
        $this->trackLog('from', $from);
        $chiefId = intval(think_decrypt($from));
        $this->trackLog("chiefId", $chiefId); 
        if ($chiefId) {
            $extSql = "SELECT * FROM ac_chief WHERE id='$chiefId'";
            $this->trackLog("extSql", $extSql); 
            $chief_info = M()->query($extSql)[0];
            $this->trackLog("chief_info", $chief_info);
            $this->assign('realname', $chief_info['realname']);
            $this->display();
        }
    }

    // 生成2维码
    public function qrcode() {

        $this->trackLog("execute", "qrcode()");

        $from = addslashes($_REQUEST['f']);
        $this->trackLog('from', $from);
        $chiefId = intval(think_decrypt($from));
        $this->trackLog("chiefId", $chiefId);

        if($chiefId){
            $model = new Model();
            $extSql = "SELECT * FROM ac_chief WHERE id='$chiefId'";
            $this->trackLog("extSql", $extSql); 
            $chief_info = $model->query($extSql)[0];
            $this->trackLog("chief_info", $chief_info);
            
            $qr_path = $chief_info['qr_path'];
            $this->trackLog("qr_path", $qr_path);
            $qr_size = filesize('.'.$qr_path);
            $this->trackLog("qr_size", $qr_size);

            $chief_info['qr_path'] = $qr_path;

            if(strlen($qr_path) == 0 || !$qr_size) {
                //获取二维码,图片
                $data = array(
                    'action_name' => "QR_LIMIT_SCENE",
                    'action_info' => array(
                        'scene' => array(
                            'scene_id' => $chiefId
                        )
                    )
                );

                $this->trackLog("data", $data);
                $this->trackLog("appid", C('WT_APP_ID'));
                $this->trackLog("appsecret", C('WT_APP_SECRET'));
                $jssdk = new Jssdk(C('WT_APP_ID'), C('WT_APP_SECRET'));
                $arr = $jssdk->createQr(json_encode($data));

                if($arr) {

                    $this->trackLog("arr", $arr);
                    $url = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=".urlencode($arr['ticket']);
                    $this->trackLog("url", json_decode($url));

                    $img_arr = $this->grabImage($url, $chiefId);
                    $this->trackLog("img_arr", $img_arr);

                    if($img_arr) {
                        
                        $extSql = "UPDATE ac_chief SET qr_path='".$img_arr['path']."' WHERE id='$chiefId'";
                        $this->trackLog("extSql", $extSql); 
                        $result = $model->execute($extSql);
                        $this->trackLog("result", $result);
                    }
                    $chief_info['qr_path'] = $img_arr['path'];
                }
            }

            $chief_info['qr_path'] = 'http://' . $_SERVER['HTTP_HOST'] . $chief_info['qr_path'];
            $this->trackLog("chief_info", $chief_info);

            $resultData['data'] = [];
            $resultData['data']['chief_info'] = $chief_info;

            //分享
            $jssdk = new Jssdk(C('WT_APP_ID'), C('WT_APP_SECRET'));
            $this->trackLog("signPackage", json_encode($jssdk->getSignPackage()));
            $resultData['data']['jssdk'] = $jssdk->getSignPackage();

            $extSql = "SELECT * FROM wt_user WHERE id='".$chief_info['wxid']."'";
            $this->trackLog("extSql", $extSql);
            $wx_info = M()->query($extSql);
            $this->trackLog("wx_info", $wx_info[0]);
            $resultData['data']['wx_info'] = $wx_info[0];

            $resultData['code'] = 1;
            $resultData['msg'] = '获取二维码成功！';
            $this->ajaxReturn($resultData);
        }

    }

    // 我的家族成员
    public function myfollow() {

        $this->trackLog('excute', "myfollow()");
        
        $wxid = $this->getWtUserIdByOpenId();
        if($wxid < 1){
            
            $this->error('您还没有申请头领！');    
        }
                
        $model = new Model();
        $extSql = "SELECT * FROM ac_chief WHERE wxid='".$wxid."'";
        $this->trackLog("extSql", $extSql);
        $chief_info = $model->query($extSql);
        $this->trackLog("chief_info", $chief_info);

        if(!$chief_info) {
            $this->error('您还没有申请头领！');
        }
        
        //取第一个元素
        $chief_info =  $chief_info[0];
           
        $extSql = "SELECT count(*) as count FROM ac_chief_fans WHERE status=1 AND chiefId='".$chief_info['id']."'";
        $this->trackLog("extSql", $extSql);
        $count = $model->query($extSql)[0]['count'];
        $this->trackLog("count", $count);
        
        //分页显示
        $page = new Page($count,50);
        $extSql = "SELECT * FROM ac_chief_fans WHERE status=1 AND chiefId='".$chief_info['id']."' LIMIT ".$page->firstRow.",".$page->listRows;
        $this->trackLog("extSql", $extSql);
        $follow_list = $model->query($extSql);
        $this->trackLog("follow_list", $follow_list);

        if ($count) {
            $data['code'] = 1;
            $data['msg'] = '获取家族成员成功~';
            $data['data'] = [
                'count' => $count,
                'follow_list' => $follow_list
            ];
            $this->ajaxReturn($data);  
        } else {
            $data['code'] = 0;
            $data['msg'] = '没有家族成员';
            $this->ajaxReturn($data);
        }
    }

    public function judgeIsChiefOrNot() {
        
    	$this->trackLog('execute', 'judgeIsChiefOrNot()');
        
        $url2join = 'join';
        $url2look = 'index';
        $wxid = $this->getWtUserIdByOpenId();
        if($wxid < 1){
            
            header("Location:" . $url2join);
            return;
        }
        
    	$querySql = "SELECT * FROM ac_chief WHERE wxid='$wxid'";
    	$this->trackLog('querySql', $querySql);
    	$chief_info = M()->query($querySql);
    	$this->trackLog('chief_info', $chief_info);
    	
    	if ($chief_info) {
    		header("Location:" . $url2look);
    	} else {
    		header("Location:" . $url2join);
    	}
    }
    
    /**
    * 头领注册
    * 
    */
    public function signUp(){
    
        $this->trackLog("excute", "signUp()");
        
        if(!IS_POST){
            
             $data['code'] = 0;
             $data['msg'] = '申请失败,请稍后重试';
             $this->ajaxReturn($data);    
        }
        
        $wxid = $this->getWtUserIdByOpenId();
        if($wxid < 1){
            $data['code'] = 0;
            $data['msg'] = '申请失败,请稍后重试';
            $this->ajaxReturn($data);       
        }
        
        $data = array(
                'wxid' => $wxid,
                'appId' => C('WT_APP_ID'),
                'appsecret' => C('WT_APP_SECRET'),
                'openid' => session("wt_user")['openid'],
                'status' => 0
            );
        $fields = array('realname' => '真实姓名', 'tel' => '手机号', 'captcha' => '验证码');
        foreach($fields as $key => $value) {
            $form_val = addslashes($_REQUEST[$key]);

            if(empty($form_val)) {
                $data['code'] = 0;
                $data['msg'] = "$value.'不能为空'";
                $this->ajaxReturn($data);
            }
            $data[$key] = $form_val;
        }
        //邀请码可空
        $data['inviteCode'] = addslashes(I('inviteCode', '', 'trim'));
        
        if(!checkMobile($data['tel'])) {
            $data['code'] = 0;
            $data['msg'] = '请输入正确的手机号码';
            $this->ajaxReturn($data);
        }
        
        if(!isWhitePhoneNumber($data['tel'])){
            
            //检查手机验证码
            if ($data['tel'] != session('captchaPhone') || $data['captcha'] != session('captcha')) {
                $data['code']  = 0;
                $data['msg']  = "手机验证码错误！";
                $this->ajaxReturn($data);
            }    
        }
        $this->trackLog("data", $data);
        
        $chiefService = new ChiefService();
        $this->ajaxReturn($chiefService->chiefSignUp($data)); 
    }

    // 申请萝卜头领
    public function doApply() {

        $this->trackLog("excute", "doApply");

        if(IS_POST){

            $wxid = $this->getWtUserIdByOpenId();
            $data = array(
                'wxid' => $this->getWtUserIdByOpenId(),
                'status' => 0
            );
            $fields = array('realname' => '真实姓名', 'tel' => '手机号', 'province'=>'省份','city' => '城市','school' => '学校', 'major' => '专业');
            foreach($fields as $key => $value) {
                $form_val = addslashes($_REQUEST[$key]);

                if(empty($form_val)) {
                    $data['code'] = 0;
                    $data['msg'] = "$value.'不能为空'";
                    $this->ajaxReturn($data);
                }
                $data[$key] = $form_val;
            }
            if(!checkMobile($data['tel'])) {
                $data['code'] = 0;
                $data['msg'] = '请输入正确的手机号码';
                $this->ajaxReturn($data);
            }
            
            //获取省市学校名称
            $db = new Model();
            $sql = "SELECT name FROM gp_province where id='".$data['province']."'";
            $this->trackLog("sql", $sql);
            $province_name = $db->query($sql);
            $this->trackLog("province_name", $province_name);
            
            $sql = "SELECT name FROM gp_city where id='".$data['city']."'";
            $this->trackLog("sql", $sql);
            $city_name = $db->query($sql);
            $this->trackLog("city_name", $city_name);
            
            $sql = "SELECT name FROM gp_school where id='".$data['school']."'";
            $this->trackLog("sql", $sql);
            $school_name = $db->query($sql);
            $this->trackLog("school_name", $school_name);
            
            $data['province_name'] = $province_name[0]['name'];
            $data['city_name'] = $city_name[0]['name'];
            $data['school_name'] = $school_name[0]['name'];

            $id = intval($_REQUEST['id']);
            
            $sql = "SELECT * FROM ac_chief where wxid='$wxid'";
            $this->trackLog("sql", $sql);
            $chief_info = $db->query($sql);
            $this->trackLog("chief_info", $chief_info);
            if($chief_info) {
                $id = $chief_info[0]['id'];
            }
            if($id)
            {
                $sql = "UPDATE ac_chief SET "
                ."wxid='$wxid',status=0,realname='".$data['realname']."',province='".$data['province']
                ."',city='".$data['city']."',school='".$data['school']."',major='".$data['major']
                ."',tel='".$data['tel']."',province_name='".$data['province_name']
                ."',city_name='".$data['city_name']."',school_name='".$data['school_name']
                ."' WHERE id='$id'";
                $this->trackLog("sql", $sql);
                $res = $db->execute($sql);
                $this->trackLog("res", $res);
                        
                if($res)
                {
                    $data['code'] = 1;
                    $data['msg'] = '修改成功,请等待审核';
                    $this->ajaxReturn($data);
                }
                else
                {
                    $data['code'] = 0;
                    $data['msg'] = '修改失败,请稍后重试';
                    $this->ajaxReturn($data);
                }
            }
            else {

                $querySql = "SELECT * FROM ac_chief WHERE tel = " . addslashes($_REQUEST['tel']);
                $result = M()->query($querySql);
                if ($result) {
                    $data['code'] = 0;
                    $data['msg'] = 'has_applied';
                    $this->ajaxReturn($data);
                }
                                       
                $sql = "INSERT INTO ac_chief ("
                ."wxid,status,realname,province,city,school,major,tel,"
                ."province_name,city_name,school_name) VALUES('$wxid',0,'"
                .$data['realname']."','".$data['province']."','".$data['city']."','".$data['school']."','"
                .$data['major']."','".$data['tel']."','".$data['province_name']."','"
                .$data['city_name']."','".$data['school_name']."')";
                $this->trackLog("sql", $sql);
                $res = $db->execute($sql);
                $this->trackLog("res", $res);
                
                $sql = "SELECT id FROM ac_chief WHERE wxid='$wxid'";
                $this->trackLog("sql", $sql);
                $chiefId = $db->query($sql);
                $this->trackLog("chiefId", $chiefId);
                
                if($chiefId) {
                    $chiefId = $chiefId[0]['id'];
                    //获取二维码,图片
                    $data = array(
                        'action_name' => "QR_LIMIT_SCENE",
                        'action_info' => array(
                            'scene' => array(
                                'scene_id' => $chiefId
                            )
                        )
                    );
                    $jssdk = new Jssdk($wxid, $this->appsecret);
                    $arr = $jssdk->createQr(json_encode($data));
                    if($arr)
                    {
                        $url = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=".urlencode($arr['ticket']);
                        $img_arr = $this->GrabImage($url,$chiefId);

                        if($img_arr)
                        {
                            if(filesize('.'.$img_arr['path']) < 100)
                            {
                                $img_arr = $this->GrabImage($url,$chiefId);
                            }
                            
                            
                            $sql = "UPDATE ac_chief SET qr_path='".$img_arr['path']."' WHERE id='$chiefId'";
                            $this->trackLog("sql", $sql);
                            $res = $db->execute($sql);
                            $this->trackLog("res", $res);
                        }
                    }

                    $data['code'] = 1;
                    $data['msg'] = '申请成功,请等待审核';
                    $this->ajaxReturn($data);
                }
                else{
                    $data['code'] = 0;
                    $data['msg']('申请失败,请稍后重试');
                    $this->ajaxReturn($data);
                }

            }

        }
    }
    
    public function myInviteCode(){
        
        $wxid = $this->getWtUserIdByOpenId();
        if($wxid < 1){
            
            $this->error('您还没有申请头领！');
            return;    
        }
        
        $chiefService = new ChiefService();
        $this->ajaxReturn($chiefService->getInviteCode($wxid)); 
    }

    // 持久化網絡圖片
    protected function grabImage($url, $chiefId) {

        $this->trackLog("excute", "grabImage");

        if($url=="") {
            return false;
        }

        $filename = "qr_".$chiefId.".jpg";
        $path ="./Uploads/qr_img/".$filename;
        $this->trackLog("path", $path);

        ob_start();
        readfile($url);
        $img = ob_get_contents();
        ob_end_clean();
        $size = strlen($img);
        $fp2=@fopen($path, "a");
        fwrite($fp2,$img);
        fclose($fp2);
        if(is_file($path)) {
            $arr = array(
                'filename' => $filename,
                'path' => "/Uploads/qr_img/".$filename
            );
            return $arr;
            $this->trackLog("arr", json_decode($arr));
        } else {
            $this->trackLog("grabImage", "fail");
            return false;
        }
    }
    
    private function getWtUserIdByOpenId(){
        
        $this->trackLog("excute", "getWtUserIdByOpenId");
        
        $openid = session("wt_user")['openid'];
        if(empty($openid)){
            
            return 0;   
        }
        
        $querySql = "SELECT id FROM wt_user WHERE wechat_openid = '$openid'";
        $this->trackLog("querySql", $querySql);
        $result = M()->query($querySql);
        $this->trackLog("result", $result);
        
        if(!$result){
            
            return 0;
        }
        
        return $result[0]['id'];
    }
}
