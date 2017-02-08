<?php

namespace Home\Controller;
use Think\Controller;
use Think\Model;
use Home\Service\JobEnum;

class TestController extends BaseController {

    private $totalIncome = 0;
    private $dayIncome = 0;
    private $iceIncome = 0;
    private $totalIceIncome = 0;
    private $totalOut = 0;
    private $day = 1;
    private $danger = 0;
    public function ct() {
        $money = I('money');
        $total_day = I('day', 30);
        echo "<h1>投资:".$money."人民币, 周期: ".$total_day."</h1>";
        $this->count($money, $total_day);
    }

    private function count($value, $total_day) {
        if($value>1 & $this->day <= $total_day) {
            $this->totalOut = $this->totalOut + $value;
            $this->totalIncome = $this->totalIncome + $value*0.1;
            $this->dayIncome = $value*0.1;

            $this->totalIceIncome = $this->totalIceIncome + $value*0.15;
            $this->iceIncome = $value*0.15;

            $this->danger = $this->danger+($this->iceIncome-$this->dayIncome);
            if($this->day<10) {
                if($this->day<7) {
                    echo "</br>第0".$this->day."天,".
                        "  日投入: ". intval($value).
                       // " |日人头: ".
                       // (intval($value)/5000).
                        " |日收益: ".
                        intval($this->dayIncome).
                        " |日冻结资金: ".
                        intval($this->iceIncome).
                        " |止投风险金: ".
                        intval($this->iceIncome-$this->dayIncome).
                        " </br>|总投入: ".
                        intval($this->totalOut).
                        " |总收益: ".
                        intval($this->totalIncome).
                        " |总止投风险金: ".
                        intval($this->danger).
                        " |总冻结资金:".
                        intval($this->totalIceIncome)."</br></br>";
                } else {
                    echo "</br>第0".$this->day."天,".
                        "  日投入: ". intval($value).
                       // " |日人头: ". (intval($value)/5000).
                        " |日收益: ".$this->good_display(intval($this->dayIncome)).
                        " |日扣押资金: ".$this->good_display(intval($this->iceIncome)).
                        " |止投风险金: ".$this->good_display(intval($this->iceIncome-$this->dayIncome)).
                        " </br>|总投入: ".intval($this->totalOut).
                        " |总收益: ".$this->good_display(intval($this->totalIncome)).
                        " |总止投风险金: ".$this->good_display(intval($this->danger)).
                        " |总冻结资金:".$this->good_display(intval($this->totalIceIncome))."</br></br>";
                }

            } else {
                echo "</br>第".$this->day."天,".
                    "  日投入: ". intval($value).
                   // " |日人头: ". (intval($value)/5000).
                    " |日收益: ".$this->good_display(intval($this->dayIncome)).
                    " |日扣押资金: ".$this->good_display(intval($this->iceIncome)).
                    " |止投风险金: ".$this->good_display(intval($this->iceIncome-$this->dayIncome)).
                    " </br>|总投入: ". intval($this->totalOut).
                    " |总收益: ".$this->good_display(intval($this->totalIncome)).
                    " |总止投风险金: ".$this->good_display(intval($this->danger)).
                    " |总累积冻结资金:".$this->good_display(intval($this->totalIceIncome))."</br></br>";
            }

            $value = $value - $value*0.15;
            $this->day++;
            $this->count($value, $total_day);
        }
        return $value;
    }

    function good_display($num) {
        //return $num;
        $need_str = '';
        $num_len = strlen($num);
        $need_len =5-$num_len;
        if($num_len<5) {
            for($i=0; $i<$need_len; $i++){
                $need_str = $need_str."&nbsp;";
            }
        }
        return $num."".$need_str;
    }

    public function testEnum(){
        $this->trackLog("excute", "TestController");
        $value = JobEnum::getPriceUnit(1);
        echo "value = ".$value;
        echo "jobEnum::Monday = ".JobEnum::YuanPerDay;

    }
     
	 
    public function testSMS() {
        $tempid = "JSM40699-0003";
        $url               = "http://112.74.76.186:8030/service/httpService/httpInterface.do?method=sendMsg";
        $params            = array ("username" => 'JSM40699', "password" => 'xvwu4oqc', "veryCode" => "z7rzpjn16ld6","msgtype"=>"2","tempid" => "$tempid");
        $params['mobile']  = 17076407995;
        //$params['content'] = "@1@=" . $phonecaptcha;
        $params['content'] = "@1@=" . $phonecaptcha;
        $params['code'] = "utf-8";
        $postdata = http_build_query($params);
        $options  = array ('http' => array ('method'  => 'POST',
                                            'header'  => 'Content-type:application/x-www-form-urlencoded',
                                            'content' => $postdata,
                                            'timeout' => 15 * 60 ));
        $result   = file_get_contents($url, FALSE, $context);
        print_r($result);
        return false;
    }    

    public function mockJob() {
      	$count =60;
      	for ($i=60; $i >0; $i--) { 
  	        $data['companyId'] = 74;
  	        $data['pCateId'] = rand(1, 12);
  	        $data['cateId'] = rand(29, 51);
  	        $data['title'] = "找茬公司测试职位" . $i;
  	        $data['company'] = "找茬测试科技有限公司" . $i;
  	        $data['address'] = "威海云计算中心"; 
  	        $data['linkman'] = "于先生" . $i; 
  	        $data['phone'] = "13124720001";    
  	        $data['number'] = 5;
              $data['status'] = 3;
  	        $data['income'] = 100;
  	        $data['incomeUnit'] = 1;
  	        $data['payType'] = 1;
  	        $data['intro'] = "负责线上APP以及微信用户体验、流程、问题的反馈！";
  	        $data['beginTime'] = "2016-05-11";
  	        $data['endTime'] = "2016-05-31";

  	        $data['provinceId'] = 1;
  	        
  	        $data['cityId'] = 1;
  	       
  	        $data['regionId'] = rand(1, 3);
  	        
  	                                               
  	        $data['source'] = 1; // 1:pc 2:wechat 3:app 4:3th
  	        $data['token'] = getToken();

  	        $result = request_post("http://l.wechat.luobojianzhi.com/index.php/home/Job/add", 
  	        http_build_query($data));
      	}

    		$resultData['code']  = 1;
    		$resultData['msg']  = "mock $count jobs!";
    		$this->ajaxReturn($resultData);
        $this->ajaxReturn($result);

    }

    public function mockUser() {
        $count = 40;
        for ($i=40; $i >0; $i--) { 
            $data['username'] = "小萝卜".$i;
            $data['realname'] = "终结者".$i;
            $data['password'] = "e10adc3949ba59abbe56e057f20f883e";
            $data['phone'] = "156623710" . $i;
            $data['birthday'] = "2016-05-".$i;
            $data['gender'] = "0"; 
            $data['intro'] = " hello world".$i; 
            $data['schoolId'] = "1";    
            $data['grade'] ="2009";
            $data['gender'] = 0;
			      $data['provinceId'] = "1";
            $data['regionId'] = "1";
            $data['cityId'] = 1;
            $data['regionId'] = 1;
            $data['address'] = "威海地址".$i;
            $data['QRCode'] = "100001";
            $data['inviteCode'] = "100001";
                                             
            $data['source'] = 1; // 1:pc 2:wechat 3:app 4:3th
            $data['token'] = getToken();

            $result = request_post("http://l.wechat.luobojianzhi.com/index.php/home/User/add", 
            http_build_query($data));
        }
        $resultData['code']  = 1;
        $resultData['msg']  = "mock $count users!";
        $this->ajaxReturn($resultData);
        $this->ajaxReturn($result);
    }

    public function mockApply(){
        $db = new Model();
        $userIdSQL = " SELECT id from user ";
        $idResult = $db -> query($userIdSQL);

        $JobIdSQL ="SELECT id from job";
        $jobId = $db ->query($JobIdSQL);

        $jobId = $jobId[0]['id'];

        $companyIdSQL = "SELECT id from company";
        $companyId = $db-> query($companyIdSQL);

        $companyId =$companyId[0]['id'];
        $cStatus =1;
        $uStatus =1;

        for( $i=0; $i<count($idResult); $i++){
            $userId =$idResult[$i]['id'];
            $sql="insert into job_apply(userId,jobId,companyId,cStatus,uStatus) values($userId,$jobId,$companyId,$cStatus ,$uStatus)";
            $db->execute($sql);
        }
        echo 'apply inserted:'.count($idResult);
    }

    public function mockCompany($countComp=10){
        $db =new Model();
        for($i =0;$i<$countComp;$i++){

          $phone="1356317101".$i;
          $username="'"."大萝卜"."'";
          $password="'"."e10adc3949ba59abbe56e057f20f883e"."'";
          $companyName ="'"."auto_createdNO.".$i."'";
          $status =0;
          $provinceId=1;
          $cityId=1;
          $regionId=1;

          $insertSQL ="INSERT into company(phone,username,password,companyName,status,provinceId,cityId,regionId) values
                    ($phone,$username,$password,$companyName,$status,$provinceId,$cityId,$regionId)";
          $result=$db->execute($insertSQL);
        }
        echo "success";
    }

    public function changeApplyStatus($userId,$jobId,$cStatus,$uStatus){
         $db =new Model();
         $updateSQL= "UPDATE  job_apply set cStatus=$cStatus ,uStatus=$uStatus where jobId=$jobId and userId=$userId";
         $db->execute($updateSQL);
         echo 'apply success';
    }       
}
