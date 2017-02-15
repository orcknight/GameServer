<?php

namespace Home\Controller;

use Think\Controller;
use Think\Model;
use Think\Log;
use Home\Service\JobEnum;

class CollectController extends BaseController {

    public function _initialize() {

        $validate = validateLogin(); //1 为已登录 0 为未登录

        if ($validate == 0) {
            $data['code'] = 0;
            $data['msg'] = 'unauthorized';
            $this->ajaxReturn($data);
        }
    }

    public function collectJob() {

        $this->trackLog("execute", "collectJob()");

        $jobId = I('id', '', 'trim'); //这是拿到传过来的jobId
        $this->trackLog("jobId", $jobId);

        $uId = session("x_user")['id']; //session取得用户ID
        $this->trackLog("x_user", session("x_user"));
        $this->trackLog("uId", $uId);

        if (!is_numeric($jobId) || !is_numeric($uId)) {
            $data['code'] = 0;
            $data['msg'] = "参数格式不正确！";
            $this->ajaxReturn($data);
        }

        $db = new Model();
        $checkSql = "SELECT * FROM job_collect WHERE uId = $uId AND jobId = $jobId";
        $this->trackLog("checkSql", $checkSql);

        $checkResult = $db->query($checkSql);
        $this->trackLog("checkResult", $checkResult);

        if ($checkResult) {
            $data['code'] = 0;
            $data['msg'] = "您已经收藏过该职位";
            $this->ajaxReturn($data);
        }

        $extSql = "INSERT INTO job_collect (uId, jobId) VALUES ($uId, $jobId)";
        $this->trackLog("extSql", $extSql);

        $result = $db->execute($extSql);
        if ($result) {
            $resultData['code'] = 1;
            $resultData['msg'] = "收藏成功！";
            $this->ajaxReturn($resultData);
        } else {
            $data['code'] = 0;
            $data['msg'] = "收藏失败！";
            $this->ajaxReturn($data);
        }
    }

    //获取收藏过的职位列表
    public function listCollected() {

        $this->trackLog("execute", "listCollected()");

        $uId = session("x_user")['id']; //session取得用户ID
        $this->trackLog("uId", $uId);

        $currPage = I('currPage', '', 'trim');
        $pageSize = I('pageSize', '', 'trim');

        $this->trackLog("uId", $uId);
        $this->trackLog("currPage", $currPage);
        $this->trackLog("pageSize", $pageSize);

        if (!is_numeric($currPage) || !is_numeric($pageSize)) {

            $data['code'] = 0;
            $data['msg'] = "参数格式不正确！";
            $this->ajaxReturn($data);
        }

        $offset = ($currPage - 1) * $pageSize;

        $extSql = "SELECT job.companyId,job.id, job.superType, job.title,job.type,job.address,job.linkman,job.phone,job.beginTime,job.endTime,job.income,job.incomeUnit,job.payType,job. intro,job.pCateId,job.createTime,job.cateId,p.name as province,c.name as city,r.name as region  FROM job LEFT JOIN job_collect ON job.id=job_collect.jobid 

		    LEFT JOIN gp_province p on job.provinceId =p.id  left join gp_city c on job.cityId=c.id left join gp_region r on job.regionid =r.id
		    WHERE job_collect.uid=$uId ORDER BY job_collect.`createTime` DESC LIMIT $offset,$pageSize";

        $db = new Model();
        $result = $db->query($extSql);

        $this->trackLog("extSql", $extSql);
        $this->trackLog("result", $result);

        $totalSql = "SELECT COUNT(*) as count FROM job LEFT JOIN job_collect ON job.id=job_collect.jobid 
		WHERE job_collect.uid=$uId";
        $total = $db->query($totalSql);

        $this->trackLog("totalSql", $totalSql);
        $this->trackLog("total", $total);

        $total = $total['0']['count'];
        if ($total > 0) {
            $resultData['code'] = 1;
            $resultData['msg'] = "查询成功";

            $data['currPage'] = $currPage;
            $data['pageSize'] = $pageSize;
            $data['total'] = $total;


            //把查出来的 incomeUnit 的数字类型改为 文字
            for ($i = 0; $i < count($result); $i++) {
                $incomeUnit = $result[$i]['incomeunit'];
                $incomeUnit = JobEnum::getPriceUnit($incomeUnit);
                $result[$i]['incomeUnit'] = $incomeUnit;
                
                $result[$i]['payenum'] = $result[$i]['paytype'];
                $payType = $result[$i]['paytype'];
                $payType =JobEnum::getPayType($payType);
                $result[$i]['payType'] = $payType;
            }


            //把查出来的pCateId 的数字类型改为 文字

            for ($i = 0; $i < count($result); $i++) {
                $cateType = $result[$i]['pcateid'];
                $cateType = $this->getParentCate($cateType);
                $result[$i]['category'] = $cateType;
            }

            $result = $this->associateAttributes($result);
            $data['data'] = $result;

            $resultData['data'] = $data;

            $this->ajaxReturn($resultData);
        } else {
            $data['code'] = 0;
            $data['msg'] = "收藏记录为空！";
            $this->ajaxReturn($data);
        }
    }

    public function cancelCollect() {
        $this->trackLog("execute", "cancelCollect()");

        $jobId = I('id', '', 'trim'); //这是拿到传过来的jobId
        $uId = session("x_user")['id']; //session取得用户ID
        $this->trackLog("uId", $uId);
        $this->trackLog("jobId", $jobId);

        if (!is_numeric($jobId) || !is_numeric($uId)) {
            $data['code'] = 0;
            $data['msg'] = "参数格式不正确！";
            $this->ajaxReturn($data);
        }

        $db = new Model();

        $extSql = "DELETE FROM job_collect WHERE uid=$uId AND jobid= $jobId";
        $this->trackLog("extSql", $extSql);

        $result = $db->execute($extSql);
        $this->trackLog("result", $result);

        if ($result) {

            $data['code'] = 1;
            $data['msg'] = "取消成功！";
            $this->ajaxReturn($data);
        } else {
            $data['code'] = 0;
            $data['msg'] = "取消失败！";
            $this->ajaxReturn($data);
        }
    }

    function getParentCate($pCateId) {
        $db = new Model();

        $sql = "SELECT name FROM job_category where id= '$pCateId'";

        $result = $db->query($sql);

        return $result[0]['name'];
    }

    function associateAttributes($jobs) {
        $this->trackLog("execute", "associateAttributes()");
        $jobIds = array();
        for ($i = 0; $i < count($jobs); $i++) {
            $jobIds[] = $jobs[$i]['id'];
        }
        $querySql = " select * from job_attribute where job_id in (" . join(",", $jobIds) . ")";
        $this->trackLog("querySql", $querySql);
        $attrs = M()->query($querySql);
        if (!$attrs) {
            for ($i = 0; $i < count($jobs); $i++) {
                $jobs[$i]['attributes'] = array();
            }
        } else {

            for ($i = 0; $i < count($jobs); $i++) {
                $temp = array();

                for ($j = 0; $j < count($attrs); $j++) {

                    if ($attrs[$j]['job_id'] == $jobs[$i]['id']) {
                        array_push($temp, intval($attrs[$j]['attr_id']));
                    }
                }
                $jobs[$i]['attributes'] = $temp;
            }
        }
        return $jobs;
    }

}
