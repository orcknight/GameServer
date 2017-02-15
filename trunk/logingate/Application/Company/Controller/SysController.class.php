<?php
namespace Company\Controller;
use Think\Controller;
use Think\Log;
use Think\Model;
use Think\Verify;


class SysController extends BaseController {

    // 用户添加反馈
    public function feedBack() {

        $this->trackLog("execute", "feedBack()");

        /* $uId = session("x_company")['id'];//session取得用户ID */
        $uId = I('session.x_company')['id']?: I('request.companyId');//优先从session中取，取不到就用参数中的

        $params = I('post.');
        $content = $params['content'];
        $title = $params['title'];
        $type = $params['type'];

        //$content = I('content', "", 'strip_tags');
        //$title = I('title', "", 'strip_tags');
        //$type = I('type', 0, 'intval');

        $type =1;
        $this->trackLog("uId", $uId);
        $this->trackLog("content", $content);
        $this->trackLog("title", $title);
        $this->trackLog("type", $type);
       

        if( $content=='' || $content==null || $uId ==null || $uId ==''  ){
          $data['code']  = 0;         
          $data['msg']  = "参数格式不正确！";  
          $this->ajaxReturn($data);        
        }
        $content = "'".$content."'";

        $extSql = "INSERT INTO sys_feedback(content,title,type,uid ) values ($content,$title,$type,$uId)";

        if($title ==null || $title ==''){
            $extSql = "INSERT INTO sys_feedback(content,type,uid ) values ($content,$type,$uId)";
        }

        $db = new Model();
      
        $this->trackLog("extSql", $extSql);
        $result =$db->execute($extSql);
        $this->trackLog("result", $result);
      
        if($result){
            $data['code']  = 1;         
            $data['msg']  = "反馈成功！"; 
            $this->ajaxReturn($data);
        }else{
            $data['code']  = 0;         
            $data['msg']  = "反馈失败！"; 
            $this->ajaxReturn($data);
        }

    }
}