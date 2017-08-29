<?php
// +----------------------------------------------------------------------
// | 基础业务CRUD方法范例
// +----------------------------------------------------------------------
// | Author: James.Yu <zhenzhouyu@jiechengkeji.cn>
// +----------------------------------------------------------------------

namespace Home\Controller;

use Think\Controller;
use Think\Model;
use OSS\OssClient;
use OSS\Core\OssException;
use Home\Service\UserService;

class UserController extends BaseController{

    public function PCresetPwd(){
        $this->trackLog("execute", "PCresetPwd()");

        $phone = I('phone', '', 'trim');
        $password = I('password', '', 'trim');

        $this->trackLog('phone', $phone);
        $this->trackLog('password', $password);
        $this->trackLog('md5(password)', md5($password));

        $password = md5($password);

        $extSql = "UPDATE user SET password = '$password' WHERE phone='$phone'";
        $this->trackLog("extSql", $extSql);


        $Model = new Model();
        $result = $Model->execute($extSql);
        $this->trackLog("result", $result);

        if($result) {

            $data['code']  = 1;
            $data['msg']  = "密码重置成功啦！";
            $data['data'] = $result[0];
            $this->ajaxReturn($data);

        }else{

            $data['code']  = 0;
            $data['msg']  = "密码重置失败啦！";
            $this->ajaxReturn($data);

        }
    }

    public function resetPwd() {

        $this->trackLog("execute", "resetPWD()");

        $phone = I('phone', '', 'trim');
        $password = I('password', '', 'trim');
        $captcha = I('captcha', '', 'trim');
        $this->trackLog('phone', $phone);
        $this->trackLog('password', $password);
        $this->trackLog('md5(password)', md5($password));
        $this->trackLog('captcha', $captcha);

        if(!is_numeric($phone)) {
          $data['code']  = 0;
          $data['msg']  = "参数格式不正确！";
          $this->ajaxReturn($data);
        }

        $this->trackLog("captchaPhone", session('captchaPhone'));
        $this->trackLog("captcha", session('captcha'));

        if ($phone != session('captchaPhone') || $captcha != session('captcha')) {
          $data['code']  = 0;
          $data['msg']  = "手机验证码错误！";
          $this->ajaxReturn($data);
        }

        $password = md5($password);

        $extSql = "UPDATE user SET password = '$password' WHERE phone='$phone'";
        $this->trackLog("extSql", $extSql);


        $Model = new Model();
        $result = $Model->execute($extSql);
        $this->trackLog("result", $result);

        if($result) {

          $data['code']  = 1;
          $data['msg']  = "密码重置成功啦！";
          $data['data'] = $result[0];
          $this->ajaxReturn($data);

        }else{

          $data['code']  = 0;
          $data['msg']  = "密码重置失败啦！";
          $this->ajaxReturn($data);

        }     

    }

    /**
     * TODO
     * 全量储存用户信息
     */
    public function saveUser() {

        $this->trackLog("execute", "saveUser()");

        $username = I('username');
        if($empty(username))
          $this->trackLog("ids", $ids);

        if(empty($ids)) {
          $data['code']  = 0;
          $data['msg']  = "缺少必要参数";
          $this->ajaxReturn($data);
        }

        $extSql = "SELECT * FROM user WHERE id IN ($ids)";
        $this->trackLog("extSql", $extSql);

        $Model = new Model();
        $result = $Model->query($extSql);
        $this->trackLog("result", $result);

        if($result) {
          $data['code']  = 1;
          $data['msg']  = "用户已经查找到。";
          $data['data'] = $result;
          $this->ajaxReturn($data);
        }else{
          $data['code']  = 0;
          $data['msg']  = "用户不存在！";
          $this->ajaxReturn($data);
        }        
    }

    /**
     * TODO
     * 根据用户Id更新用户数据
     */
    public function updateUserById() {

        $this->trackLog("execute", "updateUserById()");

        $id = I('id');
        $username = I('username');
        $realname = I('realname');
        $password = I('password');
        $phone = I('phone');
        $state = I('state');
        $type = I('type');
        $credit = I('credit');
        $birthday = I('birthday');
        $gender = I('gender');
        $school = I('school');
        $grade = I('grade');
        $country = I('country');

        $this->trackLog('username', $username);
        $this->trackLog('realname', $realname);
        $this->trackLog('password', md5($password));

        if(is_numeric(phone) || empty($password)) {
          $data['code']  = 0;
          $data['msg']  = "缺少必要参数";
          $this->ajaxReturn($data);
        }

        $user = M("User"); 
        $result = $user->save();

        if($result) {
          $data['code']  = 1;
          $data['msg']  = "用户注册成功！";
          $data['data'] = $result[0];
          $this->ajaxReturn($data);
        }else{
          $data['code']  = 0;
          $data['msg']  = "用户创建失败";
          $this->ajaxReturn($data);
        }
    }

    /**
     * 检查手机号码的唯一性
     */
    public function checkUniquePhone() {

        $this->trackLog("execute", "checkUniquePhone()");

        $phone = I('phone');

        $this->trackLog("phone", $phone);

        if(empty($phone)) {
          $data['code']  = 0;
          $data['msg']  = "缺少必要参数";
          $this->ajaxReturn($data);
        }

        $extSql = "SELECT count(*) AS total FROM user WHERE phone='$phone'";
        $this->trackLog("extSql", $extSql);

        $Model = new Model();
        $result = $Model->query($extSql);
        $this->trackLog("result", $result);

        if($result[0][total]) {
          $data['code']  = 1;
          $data['msg']  = "手机号已经存在";
          $this->ajaxReturn($data);
        }else{
          $data['code']  = 0;
          $data['msg']  = "手机号不存在！";
          $this->ajaxReturn($data);
        }
    }

    public function pcSignUp(){
        $this->trackLog("execute", "pcSignUp()");

        $phone = I('phone', '', 'trim');
        $password = I('password', '', 'trim');

        $args = array(
            'phone' => $phone,
            'password' => md5($password),
            'agree' => '1',
            'openId' => '',
            'status' => 1,
        );
        $this->trackLog("args", $args);
        $userService = new UserService();
        $this->ajaxReturn($userService->PcsignIn($args));
    }

    /**
     * 登录接口
     */
    public function login() {

        $this->trackLog("execute", "login()");

        $name = I('id', '', 'trim');
        $pass = I('pass', '', 'trim');
        $this->trackLog("name", $name);
        $this->trackLog("pass", $pass);
        if(empty($name)|| empty($pass)) {
          $data['cod']  = 0;
          $data['sta']  = "argerr";
          $this->ajaxReturn($data, 'JSONP');
        }

        $querySql = "SELECT password FROM user WHERE name = '$name'";
        $result = M()->query($querySql);
        $this->trackLog("result", $result);
        if(!$result){
            $this->trackLog("iderr", "iderr");
            $data['cod']  = 0;
            $data['sta']  = "iderr";
            $this->ajaxReturn($data, 'JSONP');
        }
        
        if(md5($pass) != $result[0]['password']){
            $this->trackLog("passerr", "passerr");
            $data['cod']  = 0;
            $data['sta']  = "passerr";
            $this->ajaxReturn($data, 'JSONP');    
        }

        $this->trackLog("null", "null");
        $data['cod']  = 1;
        $data['sta']  = getIPaddress() . "&null";
        $this->ajaxReturn($data, 'JSONP');
    }

    /**
     * 新版用户注册
     */
    public function signUp() {

        $this->trackLog("execute", "signUp()");
        
        $args = array(
            'name' => $_POST["id"],
            'pass' => $_POST["pass"],
            'pass2' => $_POST["pass2"],
            'email' => $_POST["email"],
            'phone' => $_POST["phone"],  
        );
        $this->trackLog("args", $args);
        
        $userService = new UserService();
        $this->ajaxReturn($userService->signUp($args));
    }

    //个人简介，直接从session里取得用户信息
    public function detail() {
        $this->trackLog("execute", "detail()");

        $uId = session("x_user")['id']? : I('request.userId');//session取得用户ID
        $this->trackLog("uId", $uId);

        if(!is_numeric($uId)) {
            $data['code']  = 0;
            $data['msg']  = "参数不合法！";
            $this->ajaxReturn($data);
        }

        $extSql = "SELECT  u.*, p.name AS province, c.name AS city, s.name AS school FROM USER u 
            LEFT JOIN gp_province p ON u. provinceId = p.id 
            LEFT JOIN gp_city c ON u.cityId= c.id LEFT JOIN gp_school s ON u.schoolId=s.id 
            WHERE u.id = $uId AND u.status = 1";
        $this->trackLog("extSql", $extSql);

        $db = new Model();
        $result=$db->query($extSql);
        $this->trackLog("result", $result);

        if($result) {
            $data['code']  = 1;
            $data['msg']  = "查询成功";
            
            $result[0]['ischief'] = $this->isChief($result[0]['phone']);
            $data['data'] = $result[0];

            $this->ajaxReturn($data);
        } else {
          $data['code']  = 0;
          $data['msg']  = "没有记录！";
          $this->ajaxReturn($data);
        }
    }


      public function update()
      {

        $this->trackLog("execute", "update()");

        

        $params = I('POST.');

        $uId = session("x_user")['id']? : I('request.userId');//session取得用户ID
        $this->trackLog("params", $params);

        $this->trackLog("uId", $uId);
        
        $total = count($params);

        if($total==0)
        {
          $data['code']  = 0;
          $data['msg']  = "没有修改用户资料！";
          $this->ajaxReturn($data);
        }

        $updateStr="";

        $regex = "/\/|\~|\!|\@|\#|\\$|\%|\^|\&|\*|\(|\)|\_|\+|\{|\}|\:|\<|\>|\?|\[|\]|\,|\.|\/|\;|\'|quot|\`|\-|\=|\\\|\|/";

        foreach ($params as $key => $value) 
        {
          if($key == 'userId'){
              
              continue;
          }
          
          if($key=='id' || $key == 'phone')
          {

          }else
          {

          $value = preg_replace($regex,"",$value);//去除用户的特殊符号
          if(!is_numeric($value))
          {
            $value="'".$value."'";
          }

          $updateStr.=$key."=".$value.',';
        }

      }
      $updateStr=rtrim($updateStr,',');
      
      $db = new Model();

      $extSql = "UPDATE USER SET ".$updateStr." where id = $uId";
      $this->trackLog("execSql", $extSql);

      $result = $db->execute($extSql);
      $this->trackLog("result", $result);


      if($result)
      {
        $data['code']  = 1;
        $data['msg']  = "修改成功";
        $this->ajaxReturn($data);
      }else
      {
        $data['code']  = 0;
        $data['msg']  = "没有修改！";
        $this->ajaxReturn($data);
      }
    }  

    public function signOut() {

        $this->trackLog("execute", "signOut()");

        $_SESSION['x_user'] = null;

        $check = $_SESSION['x_user'];
        if(empty($check)) {
          $data['code'] = 1;
          $data['msg'] = "退出成功！";
          $this->ajaxReturn($data);
        } else {
          $data['code']  = 0;
          $data['msg']  = "退出失败！";
          $this->ajaxReturn($data);
        }
    }

    public function checkOldPwd() {
        $this->trackLog("execute", "checkOldPwd()");

       $oldPwd = session("x_user")['password'];// session取得传过来
       $oldInput = I('oldInput','','trim');//用户输入的密码

       $regex = "/\/|\~|\!|\@|\#|\\$|\%|\^|\&|\*|\(|\)|\_|\+|\{|\}|\:|\<|\>|\?|\[|\]|\,|\.|\/|\;|\'|quot|\`|\-|\=|\\\|\|/";
       $oldInput= preg_replace($regex,"",$oldInput);

       $this->trackLog("oldPwd", $oldPwd);
       $this->trackLog("oldInput", $oldInput);

       $oldInput = md5($oldInput); 
       
      
       if( $oldInput==null || $oldInput=="" )
       {
        $data['code']  = 0;         
        $data['msg']  = "参数格式不正确！";  
        $this->ajaxReturn($data);        
      }

      if($oldPwd==$oldInput)
      {
        $data['code']  = 1;
        $data['msg']  = "旧密码输入正确";
        $this->ajaxReturn($data);
      }else
      {
        $data['code']  = 0;
        $data['msg']  = "旧密码输入错误！";
        $this->ajaxReturn($data);
      }
    }  

    public function changePwd() {
        $this->trackLog("execute", "changePwd()");
        
        $newPwd = I('newPwd','','trim');
        $reNewPwd = I('reNewPwd','','trim');

        $uId = session("x_user")['id'];//session取得用户ID

        $this->trackLog("uId", $uId);
        $this->trackLog("newPwd", $newPwd);
        $this->trackLog("reNewPwd", $reNewPwd);

        $result=$this->checkNewPwd($newPwd,$reNewPwd);

        if($result) {
            $reNewPwd = md5($reNewPwd);
            $reNewPwd = "'".$reNewPwd."'";
            $db = new Model();
            $extSql = "UPDATE USER SET PASSWORD= $reNewPwd WHERE id = $uId";

            $this->trackLog("extSql", $extSql);

            $re = $db->execute( $extSql);
            $this->trackLog("resultData", $re);

            if($re) {

                $resultData['code']  = 1;
                $resultData['msg']  = "修改成功";
                $this->ajaxReturn($resultData);

            } else {
                $resultData['code']  = 0;
                $resultData['msg']  = "修改失败";
                $this->ajaxReturn($resultData);
            }
        } else {
            $data['code']  = 0;
            $data['msg']  = "两次密码输入不一样！";
            $this->ajaxReturn($data);
        }
    }

    // [ToRe]验证两次新旧密码是否相同
    public function checkNewPwd($newPwd, $reNewPwd) {

        $this->trackLog("execute", "checkNewPwd()");

        $this->trackLog("newPwd", $newPwd);
        $this->trackLog("reNewPwd", $reNewPwd);

        if($newPwd==$reNewPwd) {
            return 1;
        } else {
            return 0;
        }

    }

    /**
     * 判断用户是否登录
     */
    public function isLogin() {

        $this->trackLog("execute", "isLogin()");
        $isLogin = validateLogin();

        $data['code']  = 0;
        $data['msg']  = "unAuthorized";

        if($isLogin) {
            $data['code']  = 1;
            $data['msg']  = "authorized";
        }
        $this->ajaxReturn($data);
    }
    
    public function isRegister(){
        
        //检查是否注册
        $openid = -1;
        if($_SESSION['wt_user']) {
            $openid = $_SESSION['wt_user']['openid'];
        }
        
        $querySql = "SELECT * FROM user u
                    LEFT JOIN wt_user wu ON wu.wechat_openid = u.openid
                    WHERE wu.wechat_openid = '$openid'";
        $this->trackLog("querySql", $querySql);
        $result = M()->query($querySql);
        $this->trackLog("result", $result);
        
        $data['code']  = 0;
        $data['msg']  = "unRegistered";

        if($result) {
            $data['code']  = 1;
            $data['msg']  = "registered";
        }
        $this->ajaxReturn($data);
    }

    public function uploadUserPhoto(){

        try {
            $this->trackLog("execute", "uploadUserPhoto()");

            $uId = session("x_user")['id'];
            if(empty($uId)){
                 $data['code']=0;
                 $data['msg']='unauthorized';
                 $this->ajaxReturn($data);
            }

            $upload           = new \Think\Upload();// 实例化上传类
            $upload->maxSize  = 2097152;
            $upload->exts     = array ('jpg', 'gif', 'png', 'jpeg');
            $upload->rootPath = './Uploads/';
            $upload->savePath = 'HeadImg/'; // 设置附件上传（子）目录

            // 上传文件
            $info = $upload->upload();
            if (!$info) {// 上传错误提示错误信息
                 $data['code']=0;
                 $data['msg']=$upload->getError();
                 $this->ajaxReturn($data);
            } else {// 上传成功

                $this->trackLog("userPhotoUpdateinfo", $info);
                // 本地用户头像
                $localHeadImgUrl = '/Uploads/' . $info['file']['savepath'] . $info['file']['savename'];
                $this->trackLog("localHeadImgUrl", $localHeadImgUrl);

                // 同步到OSS
                Vendor('OSS.autoload');
                $ossClient = new OssClient(C('OSS_ACCESS_KEY_ID'), C('OSS_ACCESS_KEY_SECRET'), C('OSS_ENDPOINT'));
                $headImgKey = $info['file']['savename'];
                $content = file_get_contents("./".$localHeadImgUrl);
                $ossClient->putObject(C('OSS_BUCKET'), $headImgKey, $content);

                $headImgUrl = C('OSS_URL') . $headImgKey;
                $this->trackLog("headImgUrl", $headImgUrl);

                $db = new Model();
                $extSql = "UPDATE USER SET headImgUrl = '$headImgUrl', localHeadImgUrl = '$localHeadImgUrl' where id = $uId";
                $re = $db->execute( $extSql);
                $this->trackLog("extSql", $extSql);
                if($re){
                    $data['code']=1;
                    $data['msg']="上传成功" ;
                    $data['data']['url'] = $localHeadImgUrl;
                    $this->ajaxReturn($data);
                }else{
                    //TODO添加 失败后 删除照片的逻辑
                    $data['code']=0;
                    $data['msg']="上传失败" ;
                    $this->ajaxReturn($data);
                }
            }
        } catch(OssException $e) {
            printf(__FUNCTION__ . ": FAILED\n");
            printf($e->getMessage() . "\n");
            return;
        }
    }

    //用于插入假数据，没有其他用途
    public function add() {


        $username = I('username', "", 'strip_tags');
        $realname = I('realname', "", 'strip_tags');
        $password = I('password', "", 'strip_tags');
        $phone = I('phone', "", 'strip_tags');
        $birthday = I('birthday', "", 'strip_tags');
        $intro = I('intro', 0, 'intval');
        $schoolId = I('schoolId', 0, 'intval');
        $grade = I('grade', 0, 'intval');
        $gender = I('gender', 0, 'intval');
        $provinceId = I('provinceId', 0, 'intval');
        $regionId = I('regionId', 0, 'strip_tags');
        $cityId = I('cityId', "", 'strip_tags');
        $address = I('address', "", 'strip_tags');
        $QRCode = I('QRCode', "", 'strip_tags');
        $inviteCode = I('inviteCode', "", 'strip_tags');
        $source = I('source', "", 'strip_tags');


        $extSql = "INSERT INTO user(username, realname, password, 
        phone, birthday, intro, schoolId, grade, 
        gender, provinceId, cityId, regionId, address, 
        QRCode, inviteCode, source) 
        VALUES('$username',  '$realname', '$password', 
        '$phone', '$birthday', '$intro', '$schoolId', '$grade', 
        '$gender', '$provinceId', '$cityId', '$regionId', '$address', 
        '$QRCode', '$inviteCode', '$source')";
     
     
        $Model = new Model();

        $result = $Model->execute($extSql);
     
  
    }
  
    /**
    * 设置用户的头领邀请码
    * 
    */
    public function setChiefInviteCode(){
        
        $this->trackLog("execute", "setInviteCode()");
        
        $chiefInviteCode = I('chiefInviteCode', '', 'trim');  
        if(!session("x_user") || session("x_user")['id'] < 1 || !is_numeric(session("x_user")['id'])){
            
            $data['code']  = 0;
            $data['msg']  = "您没有登录，请登录！";
            $this->ajaxReturn($data);    
        }
        $userId = session("x_user")['id'];//session取得用户ID 
        
        $openid = -1;
        if($_SESSION['wt_user']) {
            $openid = $_SESSION['wt_user']['openid'];
        }
        
        $args = array(
        "userId" => $userId,
        "openid" => $openid,
        "chiefInviteCode" => $chiefInviteCode,
        );

        $userService = new UserService();
        $this->ajaxReturn($userService->setChiefInviteCode($args));
        
    }
    
    /**
    * 设置头领用户的经销商邀请码
    * 
    */
    public function setAdminInviteCode(){
        
        $this->trackLog("execute", "setAdminInviteCode()");
        
        $chiefInviteCode = I('adminInviteCode', '', 'trim');  
        if(!session("x_user") || session("x_user")['id'] < 1 || !is_numeric(session("x_user")['id'])){
            
            $data['code']  = 0;
            $data['msg']  = "您没有登录，请登录！";
            $this->ajaxReturn($data);    
        }
        $userId = session("x_user")['id'];//session取得用户ID 
        
        $openid = -1;
        if($_SESSION['wt_user']) {
            $openid = $_SESSION['wt_user']['openid'];
        }
        
        $args = array(
        "userId" => $userId,
        "openid" => $openid,
        "adminInviteCode" => $chiefInviteCode,
        );

        $userService = new UserService();
        $this->ajaxReturn($userService->setAdminInviteCode($args));
        
    }
    
    private function setChiefUserId($phone, $userId){
        
        $execSql = "UPDATE ac_chief SET userId = $userId WHERE tel = '$phone' AND userId < 1";
        $this->trackLog("execSql", $execSql);
        $result = M()->execute($execSql);
        $this->trackLog("result", $result);
        
    }
    
    private function isChief($phone){
        
        $querySql = "SELECT * FROM ac_chief WHERE tel = '$phone' AND status = 1";
        $result = M()->query($querySql);
        
        if(!$result){
            
            return false;
        }
        
        return true;
    }
}