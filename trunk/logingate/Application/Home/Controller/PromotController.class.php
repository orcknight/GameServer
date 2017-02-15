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

class PromotController extends WtAuthController {

    public function _initialize() {
        parent::_initialize();
    }

    public function index() {

        $this->trackLog("excute", "PromotController index()");
        
        $model = new Model();
        $extSql = "SELECT * FROM ac_chief WHERE wxid='".$this->wx_id."'";
        $this->trackLog("extSql", $extSql); 
        $promot_info = $model->query($extSql);
        $this->trackLog("promot_info", $promot_info);
        //返回来是个二维数组，取第0个元素
        if($promot_info){
            
            $promot_info = $promot_info[0];
        }

        // 获取排名
        if($promot_info) {
            
            $extSql = "SELECT count(*) as count FROM ac_chief WHERE status = 1 AND number > '".$promot_info['number']."'";
            $this->trackLog("extSql", $extSql); 
            $count = $model->query($extSql);
            $this->trackLog("count", $count);
            
            $this->assign('rank_number', $count[0]['count']+1);
        } else {
            
            $this->assign('rank_number',0);
        }

        // 获取推荐人信息
        $from_promot_id = $this->wx_info['from_promot_id'];
        $this->trackLog("from_promot_id", $from_promot_id);

        if($from_promot_id) {
            $extSql = "SELECT realname FROM ac_chief WHERE id='$from_promot_id'";
            $this->trackLog("extSql", $extSql); 
            $realname = $model->query($extSql);
            $this->trackLog("realname", $realname);
            $from_promot_name = $realname[0]['realname'];
            $this->assign('from_promot_name',$from_promot_name);
        } else {
            $this->assign('from_promot_name','萝卜');
        }

        $this->assign('promot_info',$promot_info);
        $this->assign('wx_info',$this->wx_info);

        $this->display();
    }

    // 生成2维码
    public function qrcode() {

        $this->trackLog("excute", "qrcode");

        $from = addslashes($_REQUEST['f']);
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
            $this->trackLog("qr_size", filesize('.'.$qr_path));

            if(strlen($qr_path) == 0) {
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

            $this->trackLog("chief_info", $chief_info);
            $this->assign('chief_info', $chief_info);

            //分享
            $jssdk = new Jssdk(C('WT_APP_ID'), C('WT_APP_SECRET'));
            $this->trackLog("signPackage", json_encode($jssdk->getSignPackage()));
            $this->assign('jssdk', $jssdk->getSignPackage());
            
            $model = new Model();
            $extSql = "SELECT * FROM wt_user WHERE id='".$chief_info['wxid']."'";
            $this->trackLog("extSql", $extSql);
            $wx_info = $model->query($extSql);
            $this->trackLog("wx_info", $wx_info[0]);
            $this->assign('wx_info', $wx_info[0]);

            $this->display();
        }
    }

    // 我的家族成员
    public function myfollow() {

        $this->assign('excute', "myfollow");
                
        $model = new Model();
        $extSql = "SELECT * FROM ac_chief WHERE wxid='".$this->wx_id."'";
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
        
        $this->assign('count', $count);

        //分页显示
        $page = new Page($count,50);
        $extSql = "SELECT * FROM ac_chief_fans WHERE status=1 AND chiefId='".$chief_info['id']."' LIMIT ".$page->firstRow.",".$page->listRows;
        $this->trackLog("extSql", $extSql);
        $follow_list = $model->query($extSql);
        $this->trackLog("follow_list", $follow_list);
        
        $this->assign('follow_list', $follow_list);

        $this->display();
    }

    // 萝卜头领信息
    public function join() {

        $this->trackLog("excute", "join()");
        
        $db = new Model();
        $sql = "SELECT * FROM ac_chief WHERE wxid='$this->wx_id'";
        $this->trackLog("sql", $sql);
        $promot_info = $db->query($sql);
        $this->trackLog("promot_info", $promot_info);

        if($promot_info)
        {
            $promot_info = $promot_info[0];
            $this->assign('status',$promot_info['status']);
            return $this->display('repeatjoin');
        }

        //获取省列表
        $sql = "SELECT * FROM gp_province WHERE status=1";
        $this->trackLog("sql", $sql);
        $prov_list = $db->query($sql);
        $this->trackLog("prov_list", $prov_list);
        $this->assign('prov_list', $prov_list);

        //获取城市列表
        $prov = $prov_list[0]['id'];

        if($promot_info) {
            $prov = $promot_info['province'];
        }
        $this->trackLog("prov", $prov);    
        
        $sql = "SELECT * FROM gp_city WHERE status=1 AND pid='$prov'";
        $this->trackLog("sql", $sql);
        $city_list = $db->query($sql);
        $this->trackLog("city_list", $city_list);

        $this->assign('city_list',$city_list);

        //获取第一个城市
        $city = $city_list[0]['id'];
        
        $sql = "SELECT * FROM gp_region WHERE status=1 AND cid='$city'";
        $this->trackLog("sql", $sql);
        $region_list = $db->query($sql);
        $this->trackLog("region_list", $region_list);

        $this->assign('region_list',$region_list);
       
        $this->trackLog("city", $city);
        $region =$region_list[0]['id'];
        $this->trackLog("region", $region);
        
        $sql = "SELECT * FROM gp_school WHERE status=1 AND rid='$region'";
        $this->trackLog("sql", $sql);
        $school_list = $db->query($sql);
        $this->trackLog("school_list", $school_list);

        $this->assign('school_list',$school_list);

        $this->assign('info',$promot_info);

        $this->display();
    }

    // 申请萝卜头领
    public function doApply() {

        $this->trackLog("excute", "doApply");


        if(IS_POST){

            $data = array(
                'wxid' => $this->wx_id,
                'status' => 0
            );
            $fields = array('realname' => '真实姓名','province'=>'省份','city' => '城市','school' => '学校','regionId'=>'区域','tel' => '手机号','qq' => 'qq号码');
            foreach($fields as $key => $value) {
                $form_val = addslashes($_REQUEST[$key]);

                if(empty($form_val)) {
                    json_error($value.'不能为空');
                }
                $data[$key] = $form_val;
            }
            if(!checkMobile($data['tel'])) {
                json_error('请输入正确的手机号码');
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
            
            $sql = "SELECT name FROM gp_region where id='".$data['regionId']."'";
            $this->trackLog("sql", $sql);
            $region_name = $db->query($sql);
            $this->trackLog("region_name", $region_name);
            

            $data['province_name'] = $province_name[0]['name'];
            $data['city_name'] = $city_name[0]['name'];
            $data['region_name'] = $region_name[0]['name'];
            $data['school_name'] = $school_name[0]['name'];

            $id= intval($_REQUEST['id']);
            
            
            $sql = "SELECT * FROM ac_chief where wxid='$this->wx_id'";
            $this->trackLog("sql", $sql);
            $promot_info = $db->query($sql);
            $this->trackLog("promot_info", $promot_info);
            if($promot_info) {
                $id = $promot_info[0]['id'];
            }
            if($id)
            {
                $sql = "UPDATE ac_chief SET"
                ."wxid='$this->wx_id',status=0,realname='".$data['realname']."',province='".$data['province']
                ."',city='".$data['city']."',school='".$data['school']."',regionId='".$data['regionId']
                ."',tel='".$data['tel']."',qq='".$data['qq']."',province_name='".$data['province_name']
                ."',city_name='".$data['city_name']."',region_name='".$data['region_name']."',school_name='".$data['school_name']
                ."' WHERE id='$id'";
                $this->trackLog("sql", $sql);
                $res = $db->execute($sql);
                $this->trackLog("res", $res);
                        
                if(mysqli_affected_rows())
                {
                    json_success('修改成功,请等待审核');
                }
                else
                {
                    json_error('修改失败,请稍后重试');
                }
            }
            else {
                                       
                $sql = "INSERT INTO ac_chief ("
                ."wxid,status,realname,province,city,school,regionId,tel,qq,"
                ."province_name,city_name,region_name,school_name) VALUES('$this->wx_id',0,'"
                .$data['realname']."','".$data['province']."','".$data['city']."','".$data['school']."','"
                .$data['regionId']."','".$data['tel']."','".$data['qq']."','".$data['province_name']."','"
                .$data['city_name']."','".$data['region_name']."','".$data['school_name']."')";
                $this->trackLog("sql", $sql);
                $res = $db->execute($sql);
                $this->trackLog("res", $res);
                
                $sql = "SELECT id FROM  ac_chief WHERE wxid='$this->wx_id'";
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
                    $jssdk = new Jssdk($this->wx_id, $this->appsecret);
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

                    json_success('申请成功,请等待审核');
                }
                else{
                    json_error('申请失败,请稍后重试');
                }

            }

        }
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

    public function getCity(){

        $prov_id = intval($_REQUEST['prov_id']);

        if($prov_id){
            
            $db = new Model();
            $sql = "SELECT * FROM gp_city WHERE pid='$prov_id' AND status=1";
            $this->trackLog("sql", $sql);
            $city_list = $db->query($sql);
            $this->trackLog("city_list", city_list);

            if($city_list){

                $arr = array(
                    'errcode' => 0,
                    'errmsg' => 'success',
                    'city_list' => $city_list
                );
                json_echo($arr);
            }
            else{

                json_error('没有找到城市');
            }
        }
    }


    public function getSchool()
    {
        $region_id = intval($_REQUEST['region_id']);
        $db = new Model();
        $sql ="select id,name from gp_school where rid =".$region_id." and status =1 ";
        $result = $db->query($sql);
        if(empty($result)){

            $data['code']=0;
            $data['msg'] ='没有找到学校';
            $this->ajaxReturn($data);
        }else
        {
            $data['code']=1;
            $data['msg'] ='学校列表已取得';
            $data['data'] =$result;
            $this->ajaxReturn($data);
        }

    }
    public function getRegion(){

        $city_id = intval($_REQUEST['city_id']);
        $db = new Model();
        $sql ="select id,name from gp_region where cid =".$city_id." and status = 1 ";
        $result = $db->query($sql);
        if(empty($result)){

            $data['code']=0;
            $data['msg'] ='没有找到地区';
            $this->ajaxReturn($data);
        }else
        {
            $data['code']=1;
            $data['msg'] ='地区列表已取得';
            $data['data'] =$result;
            $this->ajaxReturn($data);
        }

    }
    
}
