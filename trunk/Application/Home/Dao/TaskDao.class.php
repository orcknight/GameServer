<?php

namespace Home\Dao;

use Enum\TaskEnum;

class TaskDao extends BaseDao{
    
    public function __construct($db){
        
        parent::__construct($db);
    }
    
    public function query($args, $fields = null){
        
        $this->trackLog("execute", "query()");
        
        if($fields == null){
            
            $fields = " t.id, t.category_id, tc.name AS category, t.title, t.intro, t.type, t.number, t.price, t.begin_time, t.end_time, 
            t.review_time, t.finish_time, t.price/100 AS price ";    
        }
        $curTime = date('Y-m-d H:i:s', time());
        $whereSql = " number > 0 AND  end_time > '$curTime' ";    
        $limitSql = '';
        $sort = array_key_exists('sort', $args) ? $args['sort'] : TaskEnum::SORT_NORMAL;
        if($sort == TaskEnum::SORT_PRICE){
            $sortSql = " ORDER BY t.price DESC ";
        }else if($sort == TaskEnum::SORT_AUDIT){
            $sortSql = " ORDER BY t.review_time ASC ";    
        }else{
            $sortSql = " ORDER BY t.create_time DESC ";    
        }
        if(isset($args['id'])){
           
            $whereSql .= (" AND t.id='" . $args['id'] . "' ");
        }
        if(isset($args['pCateId'])){
           
            $whereSql .= (" AND t.p_category_id='" . $args['pCateId'] . "' ");
        }
        if(isset($args['cateId'])){
           
            $whereSql .= (" AND t.category_id='" . $args['cateId'] . "' ");
        }
        if(isset($args['status'])){
           
            $whereSql .= (" AND t.status='" . $args['status'] . "' ");
        }
        if(isset($args['start']) && isset($args['length'])){
           
            $limitSql .= (' LIMIT ' . $args['start'] . ', ' . $args['length']);
        }
        
        $curTime = date('Y-m-d H:i:s', time());
        $querySql = "SELECT ". $fields . "FROM task t LEFT JOIN task_category tc ON category_id = tc.id WHERE " . $whereSql . $sortSql . $limitSql;
        $this->trackLog("querySql", $querySql);
        $result = $this->db->query($querySql);

        return $result;           
    }
    
    public function getQueryCount($args){
        
        $this->trackLog("execute", "getQueryCount()");
        
        $curTime = date('Y-m-d H:i:s', time());
        $whereSql = " number > 0 AND  end_time > '$curTime' ";
        if(isset($args['id'])){
           
            $whereSql .= (" AND id='" . $args['id'] . "' ");
        }
        if(isset($args['pCateId'])){
           
            $whereSql .= (" AND p_category_id='" . $args['pCateId'] . "' ");
        }
        if(isset($args['cateId'])){
           
            $whereSql .= (" AND category_id='" . $args['cateId'] . "' ");
        }
        if(isset($args['status'])){
           
            $whereSql .= (" AND status='" . $args['status'] . "' ");
        }
        
        
        $querySql = "SELECT COUNT(*) AS count FROM task WHERE  " . $whereSql;
        $this->trackLog("querySql", $querySql);
        $result = $this->db->query($querySql);
        $this->trackLog("result", $result);
        if(!$result){
            
            return 0;
        }

        return $result[0]['count'];               
    }
    
    public function queryApply($args){
        
        $this->trackLog("execute", "queryApply()");
        
        $whereSql = ' 1=1 ';    
        $limitSql = '';
        if(isset($args['id'])){
           
            $whereSql .= (" AND ta.id='" . $args['id'] . "' ");
        }
        if(isset($args['taskId'])){
           
            $whereSql .= (" AND ta.task_id='" . $args['taskId'] . "' ");
        }
        if(isset($args['userId'])){
           
            $whereSql .= (" AND ta.user_id='" . $args['userId'] . "' ");
        }
        if(isset($args['cStatus'])){
           
            $whereSql .= (" AND ta.c_status IN(" . $args['cStatus'] . ") ");
        }
        if(isset($args['uStatus'])){
           
            $whereSql .= (" AND ta.u_status IN(" . $args['uStatus'] . ") ");
        }
        if(isset($args['start']) && isset($args['length'])){
           
            $limitSql .= (' LIMIT ' . $args['start'] . ', ' . $args['length']);
        }
        
        $querySql = "SELECT t.id AS task_id, ta.id AS apply_id, t.category_id, tc.name AS category, t.end_time, t.title, t.price,t.number, ta.create_time, t.`status`, t.finish_time, ta.create_time,
        ta.u_status 
        FROM task_apply ta
        LEFT JOIN task t ON t.id = ta.task_id
        LEFT JOIN task_category tc ON t.category_id = tc.id
        WHERE " . $whereSql . " ORDER BY t.id DESC " . $limitSql;
        $this->trackLog("querySql", $querySql);
        $result = $this->db->query($querySql);
        $this->trackLog("result", $result);

        return $result;     
    }
    
    public function getQueryApplyCount($args){
        
        $this->trackLog("execute", "getQueryApplyCount()");    
        
        $whereSql = ' 1=1 ';
        if(isset($args['id'])){
           
            $whereSql .= (" AND ta.id='" . $args['id'] . "' ");
        }
        if(isset($args['taskId'])){
           
            $whereSql .= (" AND ta.task_id='" . $args['taskId'] . "' ");
        }
        if(isset($args['userId'])){
           
            $whereSql .= (" AND ta.user_id='" . $args['userId'] . "' ");
        }
        if(isset($args['cStatus'])){
           
            $whereSql .= (" AND ta.c_status IN (" . $args['cStatus'] . ") ");
        }
        if(isset($args['uStatus'])){
           
            $whereSql .= (" AND ta.u_status IN(" . $args['uStatus'] . ") ");
        }
        
        $querySql = "SELECT COUNT(*) AS count FROM task_apply ta WHERE " . $whereSql;
        $this->trackLog("querySql", $querySql);
        $result = $this->db->query($querySql);
        $this->trackLog("result", $result);
        if(!$result){
            
            return 0;
        }
        
        return $result[0]['count'];
    }
    
    public function update($args){
        
        $this->trackLog("execute", "update()");
        
        $setSql = '';
        $whereSql = '';
        
        if(isset($args['number'])){
           
            $setSql .= ("number=" . $args['number'] . ", ");
        }
        if(isset($args['hot'])){
            
            $setSql .= ("hot=" . $args['hot'] . ", ");
        }
        if(strlen($setSql) > 0){
            
            $setSql = rtrim($setSql, ', ');
        }
        if(isset($args['cond_id'])){
           
            $whereSql .= ('id=' . $args['cond_id'] . ' ');
        }
        
        $executeSql = "UPDATE task SET " . $setSql . " WHERE " . $whereSql;
        $this->trackLog("executeSql", $executeSql);        
        $result = $this->db->execute($executeSql);
        $this->trackLog("result", $result);
        
        return $result;
    }
    
    public function updateApply($args){
        
        $this->trackLog("execute", "updateApply()");
        
        $setSql = '';
        $whereSql = '';
        
        if(isset($args['cStatus'])){
           
            $setSql .= ("c_status=" . $args['cStatus'] . ", ");
        }
        if(isset($args['uStatus'])){
            
            $setSql .= ("u_status=" . $args['uStatus'] . ", ");
        }
        if(isset($args['curr_step'])){
            
            $setSql .= ("curr_step=" . $args['curr_step'] . ", ");
        }
        if(strlen($setSql) > 0){
            
            $setSql = rtrim($setSql, ', ');
        }
        if(isset($args['cond_id'])){
           
            $whereSql .= ('id=' . $args['cond_id'] . ' ');
        }
        
        $executeSql = "UPDATE task_apply SET " . $setSql . " WHERE " . $whereSql;
        $this->trackLog("executeSql", $executeSql);        
        $result = $this->db->execute($executeSql);
        $this->trackLog("result", $result);
        
        return $result;
    }

    public function queryTaskStep($args){
        
        $this->trackLog("execute", "queryTaskStep()");
        
        $whereSql = ' 1=1 ';    
        $limitSql = '';
        $sort = 'ASC';
        if(isset($args['sort'])){
            
            $sort = $args['sort'];
        }
        if(isset($args['id'])){
           
            $whereSql .= (" AND id='" . $args['id'] . "' ");
        }
        if(isset($args['taskId'])){
           
            $whereSql .= (" AND task_id='" . $args['taskId'] . "' ");
        }
        if(isset($args['status'])){
           
            $whereSql .= (" AND status='" . $args['status'] . "' ");
        }
        if(isset($args['start']) && isset($args['length'])){
           
            $limitSql .= (' LIMIT ' . $args['start'] . ', ' . $args['length']);
        }
        
        $querySql = "SELECT * FROM task_step WHERE " . $whereSql . " ORDER BY number " . $sort . $limitSql;
        $this->trackLog("querySql", $querySql);
        $result = $this->db->query($querySql);
        $this->trackLog("result", $result);

        return $result;        
    }
    
    public function queryTaskStepResult($args){
        
        $this->trackLog("execute", "queryTaskStepResult()");
        
        $whereSql = ' 1=1 ';    
        $limitSql = '';
        if(isset($args['id'])){
           
            $whereSql .= (" AND id='" . $args['id'] . "' ");
        }
        if(isset($args['taskId'])){
           
            $whereSql .= (" AND task_id='" . $args['taskId'] . "' ");
        }
        if(isset($args['taskApplyId'])){
           
            $whereSql .= (" AND task_apply_id='" . $args['taskApplyId'] . "' ");
        }
        if(isset($args['start']) && isset($args['length'])){
           
            $limitSql .= (' LIMIT ' . $args['start'] . ', ' . $args['length']);
        }
        
        $querySql = "SELECT * FROM task_step_result WHERE " . $whereSql . " ORDER BY id DESC " . $limitSql;
        $this->trackLog("querySql", $querySql);
        $result = $this->db->query($querySql);
        $this->trackLog("result", $result);
        
        for($i = 0; $i < sizeof($result); $i++){
            
            $resultId = $result[$i]['id'];
            
            $querySql = "SELECT * FROM task_step_result_attr WHERE result_id = '$resultId'";
            $this->trackLog("querySql", $querySql);
            $attrResult = $this->db->query($querySql);
            $this->trackLog("attrResult", $attrResult);
            $result[$i]['attr'] = $attrResult;
            
        }

        return $result;     
    }
    
    public function addApply($args){
        
        $this->trackLog("execute", "addApply()");
        
        $taskId = $args['taskId'];
        $userId = $args['userId'];
        
        $sql = "INSERT INTO task_apply (task_id, user_id, c_status, u_status, curr_step) 
        VALUES ('$taskId', '$userId', 1, 1, 1)";
        $this->trackLog("sql", $sql);
        $res = $this->db->execute($sql);
        $this->trackLog("res", $res);
                
        $sql = "SELECT last_insert_id() as id";
        $this->trackLog("sql", $sql);
        $result = $this->db->query($sql);
        $this->trackLog('result', $result);
        
        return $result[0]['id']; 
    }
    

    public function addStepResult($args){
        
        $this->trackLog("execute", "addStepResult()");
        
        $taskId = $args['taskId'];
        $stepId = $args['stepId'];
        $taskApplyId = $args['taskApplyId'];
        
        $sql = "INSERT INTO task_step_result (task_id, step_id, task_apply_id)
        VALUES ('$taskId', '$stepId', '$taskApplyId')";
        $this->trackLog("sql", $sql);
        $res = $this->db->execute($sql);
        $this->trackLog("res", $res);
                
        $sql = "SELECT last_insert_id() as id";
        $this->trackLog("sql", $sql);
        $result = $this->db->query($sql);
        $this->trackLog('result', $result);
        
        $stepResultId = $result[0]['id']; 
        if($stepResultId < 1){
            
            return false;
        }
        
        $attrs = $args['attr'];
        $loopStatus = true;
        for($i = 0; $i < sizeof($attrs); $i++){
            
            $type =   $attrs[$i]['type'];
            $content = $attrs[$i]['content'];  
            $execSql = "INSERT INTO task_step_result_attr (result_id, type, content)
            VALUES ('$stepResultId', '$type', '$content')";
            $this->trackLog("execSql", $execSql);
            $attrRet = $this->db->execute($execSql);
            $this->trackLog("attrRet", $attrRet);
            
            if(!$attrRet){
                
                $loopStatus = false;
                break;    
            }
        }
        
        return $loopStatus;
    }
    
    public function incomeDetail($args){
        
        $this->trackLog("execute", "incomeDetail()");    
        
        $userId = $args['userId'];
        $status = $args['status'];
        $type = $args['type'];
        $orderType = $args['orderType'];
        $beginTime = date("Y-m-d 00:00:00", time());
        $endTime = date("Y-m-d 23:59:59", time());
        $totalIncome = 0;
        $todayIncome = 0;
        
        $querySql = "SELECT (sum(wo.money)/100.00) AS money
        FROM wallet_order wo
        LEFT JOIN task_apply ta ON wo.businessId = ta.id
        WHERE ta.user_id = '$userId' AND wo.status = '$status' AND wo.type = '$type'
        AND wo.orderType = '$orderType'";
        $this->trackLog("querySql", $querySql);
        $result = $this->db->query($querySql);
        $this->trackLog("result", $result);
        if($result){
            
            $totalIncome = $result[0]['money'];        
        }
        
        $querySql = "SELECT (sum(wo.money)/100.00) AS money
        FROM wallet_order wo
        LEFT JOIN task_apply ta ON wo.businessId = ta.id
        WHERE ta.user_id = '$userId' AND wo.status = '$status' AND wo.type = '$type'
        AND wo.orderType = '$orderType' AND wo.createTime BETWEEN '$beginTime' AND '$endTime'";
        $this->trackLog("querySql", $querySql);
        $result = $this->db->query($querySql);
        $this->trackLog("result", $result);
        if($result){
            
            $todayIncome = $result[0]['money'];        
        }
        
        $data['total_income'] = $totalIncome;
        $data['today_income'] = $todayIncome;
        return $data;
    }
    
    public function incomeLogs($args){
        
        $this->trackLog("execute", "incomeLogs()");
        
        $userId = $args['userId'];
        $status = $args['status'];
        $type = $args['type'];
        $orderType = $args['orderType'];
        $start = $args['start'];
        $length = $args['length'];
        
        $querySql = "SELECT t.title, tc.id AS category_id, tc.name AS category, wo.type, (wo.money/100.00) AS money, wo.createTime AS create_time
        FROM wallet_order wo
        LEFT JOIN task_apply ta ON wo.businessId = ta.id
        LEFT JOIN task t ON ta.task_id = t.id
        LEFT JOIN task_category tc ON t.category_id = tc.id
        WHERE ta.user_id = '$userId' AND wo.status = '$status' AND wo.type = '$type' AND wo.orderType = '$orderType'
        ORDER BY wo.createTime DESC
        LIMIT $start,$length";
        $this->trackLog("querySql", $querySql);
        $result = $this->db->query($querySql);
        
        return $result;
    }
    
    public function getIncomeLogsCount($args){
        
        $this->trackLog("execute", "getIncomeLogsCount()");
        
        $userId = $args['userId'];
        $status = $args['status'];
        $type = $args['type'];
        $orderType = $args['orderType'];

        
        $querySql = "SELECT COUNT(*) AS count
        FROM wallet_order wo
        LEFT JOIN task_apply ta ON wo.businessId = ta.id
        WHERE ta.user_id = '$userId' AND wo.status = '$status' AND wo.type = '$type' AND wo.orderType = '$orderType'";
        $this->trackLog("querySql", $querySql);
        $result = $this->db->query($querySql);
        $this->trackLog("result", $result);
        
        if(!$result){
            
            return 0;
        }
        
        return $result[0]['count'];
    }
    
    public function isUnlimitedTaskCanApply($args){
        
        $this->trackLog("execute", "getIncomeLogsCount()");
        
        $taskId = $args['taskId'];
        $userId = $args['userId'];
        $uStatus = TaskEnum::APPLY_USTATUS_APPLIED . ',' . TaskEnum::APPLY_USTATUS_FINISHED;
        
        $querySql = "SELECT t.id AS task_id, ta.id AS apply_id, t.category_id, tc.name AS category, t.end_time, t.title, t.price, t.number, ta.create_time, t.`status`, t.finish_time, ta.create_time,
        ta.u_status 
        FROM task_apply ta
        LEFT JOIN task t ON t.id = ta.task_id
        LEFT JOIN task_category tc ON t.category_id = tc.id
        WHERE ta.task_id = '$taskId' AND ta.user_id = '$userId' AND ta.u_status IN($uStatus)";
        $this->trackLog("querySql", $querySql);
        $result = $this->db->query($querySql);
        $this->trackLog("result", $result);
        
        return $result;
    }
    
    public function isDailyTaskCanApply($args){
        
        $this->trackLog("execute", "getIncomeLogsCount()");
        
        $beginTime = $args['beginTime'];
        $endTime = $args['endTime'];
        $taskId = $args['taskId'];
        $userId = $args['userId'];
        $uStatus = TaskEnum::APPLY_USTATUS_APPLIED . ',' . TaskEnum::APPLY_USTATUS_FINISHED;
        
        //首先检查有没有未完成的申请
        $querySql = "SELECT t.id AS task_id, ta.id AS apply_id, t.category_id, tc.name AS category, t.end_time, t.title, t.price, t.number, ta.create_time, t.`status`, t.finish_time, ta.create_time,
        ta.u_status 
        FROM task_apply ta
        LEFT JOIN task t ON t.id = ta.task_id
        LEFT JOIN task_category tc ON t.category_id = tc.id
        WHERE ta.task_id = '$taskId' AND ta.user_id = '$userId' AND ta.u_status IN($uStatus)";
        $this->trackLog("querySql", $querySql);
        $result = $this->db->query($querySql);
        $this->trackLog("result", $result);        
        
        if($result){
            
            return $result;
        }
        
        //然后检查今天有没有申请过
        $querySql = "SELECT t.id AS task_id, ta.id AS apply_id, t.category_id, tc.name AS category, t.end_time, t.title, t.price, t.number, ta.create_time, t.`status`, t.finish_time, ta.create_time,
        ta.u_status 
        FROM task_apply ta
        LEFT JOIN task t ON t.id = ta.task_id
        LEFT JOIN task_category tc ON t.category_id = tc.id
        WHERE ta.task_id = '$taskId' AND ta.user_id = '$userId' AND (ta.create_time BETWEEN '$beginTime' AND '$endTime')";
        $this->trackLog("querySql", $querySql);
        $result = $this->db->query($querySql);
        $this->trackLog("result", $result);
        
        return $result;
        
    }
    
    public function findTaskById($args){
        
        $this->trackLog("execute", "getIncomeLogsCount()");
        
        $id = $args['id'];
        
        $querySql = "SELECT t.id, t.category_id, tc.name AS category, t.title, t.intro, t.type, t.number, t.price, 
        t.begin_time, t.end_time, t.review_time, t.finish_time, t.price/100 AS price 
        FROM task t LEFT JOIN task_category tc ON category_id = tc.id WHERE t.id = '$id'";
        $this->trackLog("querySql", $querySql);
        $result = $this->db->query($querySql);
        $this->trackLog("result", $result);
        
        return $result;
    }

}
?>
