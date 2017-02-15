<?php
namespace Home\Controller;

use Think\Controller;
use Think\Model;
use Think\Log;

class PersonalController extends BaseController
{
    //获取申请过的职位列表
	public function getApplyJobList()
	{
		/*if(empty(session['user']))//验证用户是否登陆
		{
			 $this->error("您还未登录系统！", U('Mobile/newLogin'));
            exit();
        }*/
        $this->trackLog("execute", "getAppliJobList()");

		//$uId=session['user']['id'];//取出session保存的用户id
		$uId=2;//暂时的假数据
		$currPage=I('currPage','','trim');
		$pageSize=I('pageSize','','trim');

		$this->trackLog("uId", $uId);
		$this->trackLog("currPage", $currPage);
		$this->trackLog("pageSize", $pageSize);
		

		if(!is_numeric($currPage)||!is_numeric($pageSize))
		{
			$data['code']  = 0;
			$data['msg']  = "参数格式不正确！";
			$this->ajaxReturn($data);
		}
		$offset=($currPage-1)*$pageSize;

		$extSql ="SELECT * FROM job LEFT JOIN job_apply ON job.id=job_apply.jobid 
		WHERE job_apply.uid=$uId ORDER BY job_apply.`createTime` DESC LIMIT $offset,$pageSize";
		$db=new Model();
		$result=$db->query($extSql);

		$this->trackLog("extSql", $extSql);
		$this->trackLog("result", $result);
		$totalSql="SELECT COUNT(*) as count FROM job LEFT JOIN job_apply ON job.id=job_apply.jobid 
		WHERE job_apply.uid=$uId";
		//$totalSql="SELECT COUNT(*) as count FROM job_apply WHERE uid=$uId";
		$total=$db->query($totalSql);

		$this->trackLog("totalSql", $totalSql);
		$this->trackLog("total", $total);

		$total=$total['0']['count'];
		if($total>0) {
			$resultData['code']  = 1;
			$resultData['msg']  = "查询成功";

			$data['currPage']=$currPage;
			$data['pageSize']=$pageSize;
			$data['total']=$total;
			$data['data'] = $result;

			$resultData['data']=$data;

			$this->ajaxReturn($resultData);

		}else {
			$data['code']  = 0;
			$data['msg']  = "没有工作申请记录！";
			$this->ajaxReturn($data);
		}
	}
	//获取收藏过的职位列表
	public function getSavedJobList()
	{
		/*if(empty(session['user']))//验证用户是否登陆
		{
			 $this->error("您还未登录系统！", U('Mobile/newLogin'));
            exit();
        }*/
        $this->trackLog("execute", "getSavedJobList()");

		//$uId=session['user']['id'];//取出session保存的用户id
		$uId=2;//暂时的假数据
		$currPage=I('currPage','','trim');
		$pageSize=I('pageSize','','trim');

		$this->trackLog("uId", $uId);
		$this->trackLog("currPage", $currPage);
		$this->trackLog("pageSize", $pageSize);
		

		if(!is_numeric($currPage)||!is_numeric($pageSize))
		{
			$data['code']  = 0;
			$data['msg']  = "参数格式不正确！";
			$this->ajaxReturn($data);
		}
		$offset=($currPage-1)*$pageSize;

		$extSql ="SELECT * FROM job LEFT JOIN job_collect ON job.id=job_collect.jobid 
		WHERE job_collect.uid=$uId ORDER BY job_collect.`createTime` DESC LIMIT $offset,$pageSize";
		$db=new Model();
		$result=$db->query($extSql);

		$this->trackLog("extSql", $extSql);
		$this->trackLog("result", $result);
		$totalSql="SELECT COUNT(*) as count FROM job LEFT JOIN job_collect ON job.id=job_collect.jobid 
		WHERE job_collect.uid=$uId";
		$total=$db->query($totalSql);

		$this->trackLog("totalSql", $totalSql);
		$this->trackLog("total", $total);

		$total=$total['0']['count'];
		if($total>0) {
			$resultData['code']  = 1;
			$resultData['msg']  = "查询成功";

			$data['currPage']=$currPage;
			$data['pageSize']=$pageSize;
			$data['total']=$total;
			$data['data'] = $result;

			$resultData['data']=$data;

			$this->ajaxReturn($resultData);

		}else {
			$data['code']  = 0;
			$data['msg']  = "查询失败！";
			$this->ajaxReturn($data);
		}
	}
}