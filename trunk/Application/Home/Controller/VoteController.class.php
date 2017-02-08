<?php
/**
 * Created by PhpStorm.
 * User: Administrator
 * Date: 2016/6/28 0028
 * Time: 15:11
 */

namespace Home\Controller;

use Enum\VoteEnum;
use Think\Model;
use Org\Wechat\Jssdk;

class VoteController extends WtAuthController {

    private $db = null;
    public function _initialize() {
        parent::_initialize();
        $this->syncUser(null);
        $this->db = new Model();
    }

    public function infi_vote() {
        $query_string = urldecode($_SERVER["QUERY_STRING"]);
        $strs = explode('&', $query_string);
        foreach ($strs as $item) {
            $arr = explode('=', $item);
            if ($arr[0] == 'id') {
                $id = $arr[1];
            }
        }
        $Sql = "SELECT title FROM vote WHERE id = $id";
        $this->trackLog("Sql", $Sql);
        $result = M()->query($Sql);
        $this->assign("title", $result[0]['title']);
        $this->display();
    }

    /**
     * 同步微信用户信息
     */
    protected function syncUser($user) {

        $voteUser = session("wt_user");
        $openid = "'".$voteUser['openid']."'";
        $this->trackLog("voteUser", $voteUser);

        $querySql = "SELECT id FROM vote_user WHERE wechatOpenid=$openid";
        $this->trackLog("querySql", $querySql);
        $result = M()->query($querySql);
        $this->trackLog("result", $result);

        $nickname = "'".$voteUser['nickname']."'";
        $headimgurl = "'".$voteUser['headimgurl']."'";
        $sex = "'".$voteUser['sex']."'";
        $country = "'".$voteUser['country']."'";
        $province = "'".$voteUser['province']."'";
        $city = "'".$voteUser['city']."'";


        if ($result) {
            $extSql = "UPDATE vote_user SET nickname = $nickname, headimgurl = $headimgurl, sex=$sex, country=$country, province=$province, city=$city WHERE wechatOpenid=$openid";
            $this->trackLog("extSql", $extSql);
            $result = M()->execute($extSql);
            $this->trackLog("result", $result);

        } else {
            $extSql = "INSERT INTO vote_user (wechatOpenid, nickname, headimgurl, sex, country, province, city) VALUES (
                $openid, $nickname, $headimgurl, $sex, $country, $province, $city)";
            $this->trackLog("extSql", $extSql);
            $result = M()->execute($extSql);
            $this->trackLog("result", $result);
        }

    }

    /**
     * 反馈登录用户信息
     */
    public function user() {
        $data['code'] = 0;
        if (session("wt_user")) {
            $data['code'] = 1;
            $data['data'] = session("wt_user");
        } else {
            $data['code'] = 0;
        }
        $this->ajaxReturn($data);
    }

    /**
     * 投票详情
     */
    public function detail(){
        
        $this->trackLog("execute", "detail");
        
        $id = I('id', 0, 'trim');
        $type = I('type', 0, 'trim');
        $page = I('page', 1, 'trim');
        $length = I('pageSize', 10, 'trim');
        $this->trackLog("id", $id);
        $this->trackLog("type", $type);
        $this->trackLog("page", $page);
        $this->trackLog("length", $length);
        
        $start = ($page - 1) * $length;
        $data = NULL;
        if($type == VoteEnum::TYPE_LIMITED) {
            $data = $this->limitedVote($id);
        }else if($type == VoteEnum::TYPE_UN_LIMITED){
            $data = $this->unlimitedVote($id, $start, $length);
        }
        $this->ajaxReturn($data);
    }
    
    /**
    * 获取无限投票用户详情
    * 
    */
    public function userDetail(){
        
        $voteItemId = I('voteItemId', 0, 'intval');
        $this->trackLog("voteItemId", $voteItemId);   
        
        if($voteItemId < 1){
            
            $data['code'] = 0;
            $data['msg'] = "获取失败！"; 
            $this->ajaxReturn($data);
        } 
        
        $querySql = "SELECT vu.nickname, vu.headimgurl, vi.id, vi.image, vi.title, vi.intro, COUNT(vl.id) AS count
                    FROM vote_item vi
                    LEFT JOIN vote_log vl ON vi.id = vl.voteItemId
                    LEFT JOIN vote_user vu ON vi.voteUserId = vu.id
                    WHERE vi.id = '$voteItemId'";
        $this->trackLog("querySql", $querySql);
        $result = M()->query($querySql);
        $this->trackLog("result", $result);
        
        if(empty($result)){
            
            $data['code'] = 0;
            $data['msg'] = "获取失败！";     
        }else{
            
            $data['code'] = 1;
            $data['msg'] = "获取成功！";   
            $data['data'] = $result[0];
        }
        
        return $this->ajaxReturn($data);
    }

    /**
     * @param $id
     * @return mixed
     */
    private function limitedVote($id) {

        $querySql = "SELECT id, title, info, reward, num, endtime FROM vote WHERE id = $id";
        $this->trackLog("querySql", $querySql);
        $result = M()->query($querySql);
        if($result) {
            $vote = $result[0];
            $voteId = $vote['id'];

            $querySql = "SELECT vi.id, vi.title, vi.intro, COUNT(vl.id) AS count FROM vote_item vi
                        LEFT JOIN vote_log vl ON vi.id = vl.voteItemId AND vl.voteId = '$voteId'
                        WHERE status = 1 AND vi.voteId = '$voteId'
                        GROUP BY vi.id";

            $this->trackLog("querySql", $querySql);
            $result = M()->query($querySql);
            $vote['items'] = $result;

            $data['code'] = 1;
            $data['msg'] = "投票信息获取成功!";
            $data['data'] = $vote;
        } else {
            $data['code'] = 0;
            $data['msg'] = "投票信息获取失败!";
        }
        return $data;
    }
    
    /**
    * 获取无限投票的选项
    * 
    * @param mixed $id
    */
    private function unlimitedVote($id, $start, $length){
        
        $querySql = "SELECT id, title, info, reward, num, endtime, status FROM vote WHERE id = $id";
        $this->trackLog("querySql", $querySql);
        $result = M()->query($querySql);
        if($result) {
            $vote = $result[0];

            $querySql = "SELECT COUNT(*) AS total FROM vote_item WHERE status = 1 AND voteId = '$id'";
            $this->trackLog("querySql", $querySql);
            $result = M()->query($querySql);
            $vote['total'] = $result[0]['total'];

            $querySql = "SELECT vi.id, vi.title, vi.image, vi.thumbImage, vi.intro, COUNT(vl.id) AS count FROM vote_item vi
                        LEFT JOIN vote_log vl ON vi.id = vl.voteItemId AND vl.voteId = '$id'
                        WHERE status = 1 AND vi.voteId = '$id'
                        GROUP BY vi.id  LIMIT $start, $length";
            $this->trackLog("querySql", $querySql);
            $result = M()->query($querySql);
            $vote['items'] = $result;

            $data['code'] = 1;
            $data['msg'] = "投票信息获取成功!";
            $data['data'] = $vote;
        } else {
            $data['code'] = 0;
            $data['msg'] = "投票信息获取失败!";
        }
        return $data;
    }

    /**
     * 投票提交方法
     * 
     */
    public function submit() {
        
        $id = I('id', 0, 'trim');
        $type = I('type', 0, 'trim');
        $itemId = I('itemId', '', 'trim');
        $openid = session("wt_user")['openid'];
        
        //参数检验
        if($id < 1 || empty($itemId) || empty($openid)){
            
            $data['code'] = 0;
            $data['msg'] = "参数错误!";
            $this->ajaxReturn($data);
        }
        
        //检查投票是否截至
        $querySql = "SELECT endtime FROM vote WHERE id = '$id'";
        $this->trackLog("querySql", $querySql);
        $result = M()->query($querySql);
        $this->trackLog("result", $result);
        
        if(!$result){
            
            $data['code'] = 0;
            $data['msg'] = "投票失败，请重试!";
            $this->ajaxReturn($data);    
        }
        
        $endTime = $result[0]['endtime'];
        if(time() > strtotime($endTime)){
            
            $data['code'] = 0;
            $data['msg'] = "投票已经结束!";
            $this->ajaxReturn($data);        
        }
        
        //检查投票类型是否正确
        $querySql = "SELECT * FROM vote WHERE id = '$id' AND type = '$type'";
        $this->trackLog("querySql", $querySql);
        $result = M()->query($querySql);
        $this->trackLog("result", $result);
        if(empty($result)){
            
            $data['code'] = 0;
            $data['msg'] = "投票类型错误!";
            $this->ajaxReturn($data);       
        }

        if(VoteEnum::TYPE_LIMITED == $type) { // 有限投票, 只能投一次.
           
           $data = $this->submitLimitedVote($id, $itemId, $openid);
        } else { //无限投票, 每天只能投一次票
            $data = $this->submitUnlimitedVote($id, $itemId, $openid);
        }
        $this->ajaxReturn($data);
    }
    
    /**
    * 是否投过票
    * 
    */
    public function isVoted(){
        
        $this->trackLog("execute", "isVoted");
        
        $id = I('id', 0, 'trim');
        $openid = session("wt_user")['openid'];
        $this->trackLog("id", $id);
        $this->trackLog("openId", $openId);
        
        if($id < 1){
            
            $data['code'] = 0;
            $data['msg'] = "参数错误!";
            $this->ajaxReturn($data);    
        }
        
        $querySql = "SELECT type FROM vote WHERE id = '$id'";
        $this->trackLog("querySql", $querySql);
        $result = M()->query($querySql);
        $this->trackLog("result", $result);
        if(empty($result)){
        
            $data['code'] = 0;
            $data['msg'] = "获取错误!";
            $this->ajaxReturn($data);
        }
        
        $type = $result[0]['type'];
        if($type == VoteEnum::TYPE_LIMITED){ //有限投票
            
            $querySql = "SELECT * FROM vote_log where voteId = '$id' AND userId = '$openid'";
            $this->trackLog("querySql", $querySql);
            $result = M()->query($querySql);
            $this->trackLog("result", $result);
            
            if(empty($result)){
                
                $data['code'] = 1;
                $data['msg'] = "获取成功";
                $data['data'] = 0;
            }else{
                
                $data['code'] = 1;
                $data['msg'] = "获取成功";
                $data['data'] = 1;
            }
        }else{
            //无限投票
            $start = date('Y-m-d 00:00:00');
            $end = date('Y-m-d 23:59:59');
            $querySql = "SELECT * FROM vote_log where voteId = '$id' AND userId = '$openid' AND (createTime between '$start' AND '$end')";
            $this->trackLog("querySql", $querySql);
            $result = M()->query($querySql);
            $this->trackLog("result", $result);
            
            if(empty($result)){
                
                $data['code'] = 1;
                $data['msg'] = "获取成功";
                $data['data'] = 0;
            }else{
                
                $data['code'] = 1;
                $data['msg'] = "获取成功";
                $data['data'] = 1;
            }    
        }
        
        $this->ajaxReturn($data); 
    }
    
    /**
    * 提交有限次数投票
    * 
    * @param mixed $id
    * @param mixed $itemId
    * @param mixed $openid
    */
    private function submitLimitedVote($id, $itemId, $openid){
        
        $this->trackLog("execute", "submitLimitedVote");
        
        $querySql = "SELECT id FROM vote_log WHERE voteId = $id AND userId = '$openid'";
        $this->trackLog("querySql", $querySql);
        $result = M()->query($querySql);
        $this->trackLog("result", $result);
        
        if(sizeof($result) > 0){
            
            $data['code'] = 0;
            $data['msg'] = "hasvoted"; 
            return $data;
        }
        
        return $this->addVoteLogs($id, $itemId, $openid);     ;
    }
    
    /**
    * 提交无限次数投票
    * 
    * @param mixed $id
    * @param mixed $itemId
    * @param mixed $openid
    */
    public function submitUnlimitedVote($id, $itemId, $openid){
        
        $this->trackLog("execute", "submitUnlimitedVote");
        
        $start = date('Y-m-d 00:00:00');
        $end = date('Y-m-d 23:59:59');
        
        $querySql = "SELECT id FROM vote_log WHERE voteId = $id AND userId = '$openid' AND (createTime between '$start' AND '$end')";
        $this->trackLog("querySql", $querySql);
        $result = M()->query($querySql);
        $this->trackLog("result", $result);
        
        if(sizeof($result) > 0){
            
            $data['code'] = 0;
            $data['msg'] = "您今天已经投过票了!"; 
            return $data;
        }

        return $this->addVoteLogs($id, $itemId, $openid);        
    }
    
    /**
    * 添加投票记录，并更新投票记录数量属性
    * 
    * @param mixed $id
    * @param mixed $itemId
    * @param mixed $openid
    */
    private function addVoteLogs($id, $itemId, $openid){
        
        //判断拆分投票数量，并验证
        $itemArray = explode(',', $itemId);
        $querySql = "SELECT * FROM vote WHERE id = '$id' AND num >= '" . sizeof($itemArray) . "'";
        $this->trackLog("querySql", $querySql);
        $result = M()->query($querySql);
        $this->trackLog("result", $result);
        
        if(empty($result)){
            
            $data['code'] = 0;
            $data['msg'] = "投票个数错误!"; 
            return $data;    
        }
                
        $this->db->startTrans();
        $loopRet = true;
        foreach($itemArray as $key => $value){
            
            //添加投票
            $extSql = "INSERT INTO vote_log (userId, voteId, voteItemId) VALUES ('".$openid."', $id, $value)";
            $this->trackLog("extSql", $extSql);
            $insertResult = $this->db->execute($extSql);
            $this->trackLog("insertResult", $insertResult);
            
            if(!$insertResult){
                
                $loopRet = false;
                break;
            }  
        }
        
        if($loopRet){
            
            $this->db->commit();
            $data['code'] = 1;
            $data['msg'] = "投票成功!"; 
                   
        }else{
            
            $this->db->rollback();
            $data['code'] = 0;
            $data['msg'] = "投票失败!"; 
        }
        
        return $data; 
    }

    /**
     * 投票结果
     */
    public function votedBI() {
        $id = I('id', 0, 'trim');
        $this->trackLog("id", $id);


        $querySql = "SELECT count(vl.id) as total , vi.id, vi.title, vi.thumbImage FROM vote_item AS vi LEFT JOIN vote_log AS vl ON vl.voteItemId = vi.id WHERE vi.voteId = $id AND vi.status = 1 GROUP BY vi.id ORDER BY total DESC";
        $this->trackLog("querySql", $querySql);

        $result = M()->query($querySql);

        $data['code'] = 1;
        $data['msg'] = "投票成功!";
        $data['data'] = $result;
        $this->ajaxReturn($data);
    }
    
    /**
    * 投票统计数据
    * 返回访问人数 投票人数 参加人数
    */
    public function voteStatistics(){
        
        $id = I('id', 0, 'trim');
        $this->trackLog("id", $id);

        if($id < 1){
            
            $data['code'] = 0;
            $data['msg'] = "获取统计数据失败!";
            $this->ajaxReturn($data);    
        }
        
        $accessCount = 0; //访问人数
        $votedCount = 0;  //投票人数
        $joinCount = 0;   //参加人数
        $voteBehavior = "'/index.php/Home/Vote/infi_vote','/index.php/Home/Vote/vote'";

        //获取访问人数
        $idString = '%"id":"'. $id .'"%';
        $queySql = "SELECT COUNT(*) AS count FROM bi_log_v2 WHERE action IN($voteBehavior) AND data LIKE '$idString'";
        $this->trackLog("querySql", $queySql);
        $result = M()->query($queySql);
        $this->trackLog("result", $result);
        if($result){
            
            $accessCount = $result[0]['count'];
        }
        
        //获取投票人数
        $queySql = "SELECT COUNT(DISTINCT(userId)) AS count FROM vote_log WHERE voteId = '$id'";
        $this->trackLog("queySql", $querySql);
        $result = M()->query($queySql);
        $this->trackLog("result", $result);
        if($result){
            
            $votedCount = $result[0]['count'];
        }
        
        //获取参加人数
        $queySql = "SELECT COUNT(*) AS count FROM vote_item WHERE voteId = '$id'";
        $this->trackLog("queySql", $querySql);
        $result = M()->query($queySql);
        $this->trackLog("result", $result);
        if($result){
            
            $joinCount = $result[0]['count'];
        }
        
        $data['code'] =  1;
        $data['msg'] = "获取成功!";
        $data['data']['accessCount'] = $accessCount;
        $data['data']['votedCount'] = $votedCount;
        $data['data']['joinCount'] = $joinCount;
        
        $this->ajaxReturn($data);
    }

    /**
     * 投票用户列表
     */
    public function VotedUser() {
        
        $id = I('id', 0, 'trim');
        $page = I('page', 1, 'trim');
        $length = I('pageSize', 10, 'trim');
        $this->trackLog("id", $id);
        $this->trackLog("page", $page);
        $this->trackLog("length", $length);
        $start = ($page - 1) * $length;

        $querySql = "SELECT COUNT(DISTINCT(vl.userId)) AS total FROM vote_log AS vl WHERE vl.voteId = $id";
        $this->trackLog("querySql", $querySql);
        $total = M()->query($querySql);
        $total = $total[0]['total'];
    
        $querySql = "SELECT DISTINCT(vu.id), vu.nickname, vu.province, vu.city, vu.headimgurl, vl.createTime FROM vote_log AS vl LEFT JOIN vote_user AS vu ON vl.userId = vu.wechatOpenid WHERE vl.voteId = $id ORDER BY createTime DESC LIMIT $start, $length";
        $this->trackLog("querySql", $querySql);
        $result['data'] = M()->query($querySql);
        $result['total'] = $total;

        $data['code'] = 1;
        $data['data'] = $result;
        $this->ajaxReturn($data);
    }

    /**
     * 创建投票
     */
    public function add() {
        $mode = "vote";
        $id = 1;
        $attrs = I('attrs', 0, 'trim');
        $this->addAttributes($mode, $id, $attrs);
    }
    
    /**
    * 无限投票添加选项
    * 
    */
    public function addItem(){
    
        $this->trackLog("execute", "addItem");
        
        $id = I("id", 0, 'trim');
        $title = I("title", '', 'trim');
        $phone = I("phone", '', 'trim');
        $intro = I("intro", '', 'trim');
        $image = I('image', '', 'trim');
        $thumbImage = I('thumbImage', '', 'trim');
        $openid = session("wt_user")['openid'];
        $this->trackLog("id", $id);
        $this->trackLog("title", $title);
        $this->trackLog("image", $image);
        
        if($id < 1 || empty($title) || empty($image) || empty($thumbImage) || empty($intro) || empty($phone)){
            
            $data['code'] = 0;
            $data['msg'] = "参数错误！";
            $this->ajaxReturn($data);  
        }
        
        //检查vote是不是无限投票
        $querySql = "SELECT type FROM vote WHERE id = '$id'";
        $this->trackLog("querySql", $querySql);
        $queryResult = M()->query($querySql);
        $this->trackLog("queryResult", $queryResult);
        
        if(empty($queryResult)){
            
            $data['code'] = 0;
            $data['msg'] = "添加失败！";
            $this->ajaxReturn($data); 
        }
        
        if($queryResult[0]['type'] != VoteEnum::TYPE_UN_LIMITED){
            
            $data['code'] = 0;
            $data['msg'] = "只有无限投票可以提交选项！";
            $this->ajaxReturn($data);    
        }
        
        //检查是否已经提交过
        $querySql = "SELECT id FROM vote_item WHERE phone = '$phone' AND voteId = '$id'";
        $this->trackLog("querySql", $querySql);
        $result = M()->query($querySql);
        $this->trackLog("result", $result);
        if($result){
            
            $data['code'] = 0;
            $data['msg'] = "该手机号已经提交过了！";
            $this->ajaxReturn($data);   
        }
        
        //获取voteUserId
        $querySql = "SELECT id FROM vote_user WHERE wechatOpenid = '$openid'";
        $this->trackLog("querySql", $querySql);
        $result = M()->query($querySql);
        $this->trackLog("result", $result);
        
        if(empty($result)){
            
            $data['code'] = 0;
            $data['msg'] = "添加失败！";
            $this->ajaxReturn($data);     
        }
        $voteUserId = $result[0]['id'];
        
        //插入投票
        $execSql = "INSERT INTO vote_item(voteId, voteUserId, title, phone, image, thumbImage, intro, status) 
        VALUES ('$id', '$voteUserId', '$title', '$phone', '$image', '$thumbImage', '$intro', 0)";
        $this->trackLog("execSql", $execSql);
            $result = M()->execute($execSql);
        $this->trackLog("result", $result);

        if($result){
            
            $data['code'] = 1;
            $data['msg'] = "提交成功！";
            $this->ajaxReturn($data);    
        }else{
            
            $data['code'] = 0;
            $data['msg'] = "提交失败！";
            $this->ajaxReturn($data);    
        }
    }

    /**
     * 更新投票
     */
    public function update() {
        $mode = "vote";
        $id = 1;
        $attrs = I('attrs', 0, 'trim');
        $this->updateAttributes($mode, $id, $attrs);
    }

    /**
     * @param $mode
     * @param $id
     * @return array
     */
    private function getAttributes($mode, $id)  {

        $idField = $mode."Id";
        $modeAttr = $mode."_attr";
        $modeType = $mode."_type";

        $querySql = "SELECT type.name, attr.value FROM $modeAttr as attr left join $modeType as type on attr.typeId = type.id WHERE $idField = $id";
        $this->trackLog("querySql", $querySql);
        $result = M()->query($querySql);

        if($result) {
            $attributes = array();
            foreach ($result as $item) {
                $attributes[$item["name"]]=$item["value"];
            }
        }
        
        $this->trackLog("attributes", $attributes);
        return $attributes;
    }

    /**
     * 增加属性
     * @param $mode
     * @param $id
     * @param $attrs
     */
    public function addAttributes($mode, $id, $attrs) {

        $extSql = "INSERT INTO vote_attr (".$mode."Id, typeId, value) VALUES ";
        $sqlAttr = array();
        foreach ($attrs as $key => $value) {
            $valueStr = "($id, '$key', '$value')";
            array_push($sqlAttr, $valueStr);
        }
        $extSql = $extSql. implode(",", $sqlAttr) .";";
        $this->trackLog("extSql", $extSql);
        echo $extSql;
        $result = M()->execute($extSql);
        $this->trackLog("result", $result);
    }

    /**
     * 修改属性
     * @param $mode
     * @param $id
     * @param $attrs
     */
    public function updateAttributes($mode, $id, $attrs) {
        
        foreach ($attrs as $key => $value) {
            $extSql = "UPDATE ".$mode."_attr SET value = '$value' WHERE ".$mode."Id=$id AND typeId = $key;";
            $result = $this->db->execute($extSql);
            
            if(!$result){
                return false;
            }
        }
        
        return true;
    }
}