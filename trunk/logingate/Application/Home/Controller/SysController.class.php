<?php

namespace Home\Controller;

use Think\Controller;
use Think\Model;
use Think\Log;

class SysController extends BaseController
{
	//已完成 微信端根据一个城市cid获取该ID城市的轮播图
	public function bannerList()
	{
		
		$this->trackLog("execute", "bannerList()");
                
		$cId = I('cId', 1, 'trim');
		$this->trackLog('cId', $cId);
                
        $type= I('type',1,'trim');
        $this->trackLog('type', $type);
                
                
		if(!is_numeric($cId)||!is_numeric($type)) {
			$data['code']  = 0;
			$data['msg']  = "参数格式不正确！";
			$this->ajaxReturn($data);
		}

		$db=new Model();
		$extSql ="select * from sys_banner where cId in ($cId ,0) and type=$type and status=1 order by weight desc";

		$result=$db->query($extSql);
		$this->trackLog("result", $result);
		$this->trackLog("extSql", $extSql);

		if($result) {
			$data['code']  = 1;
			$data['msg']  = "查询成功";
			$data['data'] = $result;
			$this->ajaxReturn($data);
		} else {
			$data['code']  = 0;
			$data['msg']  = "没有该条记录！";
			$data['data'] = $result[0];
			$this->ajaxReturn($data);
		}
	}
	public function initAdvList()
	{

		$this->trackLog("execute", "initAdvList()");

		$cId = I('cId', 1, 'trim');
		$this->trackLog('cId', $cId);
                
                $type= I('type',1,'trim');
                $this->trackLog('type', $type);
                
                
		if(!is_numeric($cId)||!is_numeric($type)) {
			$data['code']  = 0;
			$data['msg']  = "参数格式不正确！";
			$this->ajaxReturn($data);
		}

		$db=new Model();
		$extSql ="select * from sys_advice where cId in ($cId ,0) and type=$type and status=1 order by weight desc";

		$result=$db->query($extSql);
		$this->trackLog("result", $result);
		$this->trackLog("extSql", $extSql);

		if($result) {
			$data['code']  = 1;
			$data['msg']  = "查询成功";
			$data['data'] = $result;
			$this->ajaxReturn($data);
		} else {
			$data['code']  = 0;
			$data['msg']  = "没有该条记录！";
			$data['data'] = $result[0];
			$this->ajaxReturn($data);
		}
	}
	// 用户添加反馈
	public function feedBack()
	{
        $this->trackLog("execute", "feedBack()");
      
         $uId = session("x_user")['id'];//session取得用户ID
       
         $params = I('post.');
      
         $content = $params['content'];
         $title = $params['title'];
         $type = $params['type'];
        
          $type =0;
         

         $this->trackLog("uId", $uId);
         $this->trackLog("content", $content);
         $this->trackLog("title", $title);
         $this->trackLog("type", $type);
         

         if( $content=='' || $content==null || $uId ==null || $uId ==''  )
		{
			$data['code']  = 0;         
			$data['msg']  = "参数格式不正确！";  
			$this->ajaxReturn($data);        
		}
         $content = "'".$content."'";

	    $extSql = "INSERT INTO sys_feedback(content,title,type,uid ) values ($content,$title,$type,$uId)";

        if($title ==null || $title =='')
        {
        	$extSql = "INSERT INTO sys_feedback(content,type,uid ) values ($content,$type,$uId)";
        }
	
      $db = new Model();
      
      $this->trackLog("extSql", $extSql);
      $result =$db->execute($extSql);
      $this->trackLog("result", $result);
      
      if($result)
      {
      	$data['code']  = 1;         
		$data['msg']  = "反馈成功！"; 
		$this->ajaxReturn($data);
      }else
      {
      	$data['code']  = 0;         
		$data['msg']  = "反馈失败！"; 
		$this->ajaxReturn($data);
      }

	}
	//未完成
	public function getFeedback()
	{
		echo 'to do';
	}
	
	
}