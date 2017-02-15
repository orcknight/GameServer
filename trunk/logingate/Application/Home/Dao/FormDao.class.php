<?php

namespace Home\Dao;

class FormDao extends BaseDao{
    
    public function __construct($db){
        
        parent::__construct($db);
    }

    /**
     * @param $db
     * @param $args
     * @return mixed
     */
    public function addFormResult($db, $args){
        $formId=$args["formId"];
        $userId=$args["userId"];
        $openId=$args["openId"];
        $content = $args["content"];

        $querySql = "SELECT * FROM form where id=$formId";
        $this->trackLog("Sql", $querySql);
        $comData = M()->query($querySql);

        // 表单中不存在该表单时
        if (count($comData) <= 0) {
            return false;
        }

        $insertSql = "INSERT INTO form_com_result (formId,userId,openId,content)
                      VALUES ('$formId','$userId','$openId','$content')";
        $this->trackLog('insertSql', $insertSql);
        $id=$db->execute($insertSql);
        $this->trackLog('id', $id);
        return $id;
    }

    /**
     * @param $db
     * @param $id
     * @return mixed通过ID获取组件信息
     */
    public function getFormById($db,$id){
        $querySql = "SELECT * FROM `form` where id=$id";

        $this->trackLog("Sql", $querySql);

        $formData = M()->query($querySql);
        $this->trackLog("data", $formData);

        if(count($formData)>0){
            $data=$formData[0];
            // 表单组件
            $querySql = "SELECT form_com_relation.* FROM form_com 
            LEFT JOIN form_com_relation 
            on form_com_relation.formComId=form_com.id 
            where form_com_relation.status=0 
            and form_com.status=0 
            and form_com_relation.formId=$id
            order by form_com_relation.sequence asc";

            $this->trackLog("Sql", $querySql);

            $formComData = M()->query($querySql);
            $this->trackLog("data", $formComData);
            $data["formcoms"]=$formComData;
            // 轮播图
            $queryImgSql = "SELECT form_image.imageUrl FROM form_image 
            LEFT JOIN form 
            on form_image.formId=form.id
            where form_image.formId=$id";
            $this->trackLog("Sql", $queryImgSql);
            $imagesData = M()->query($queryImgSql);
            $this->trackLog("data", $imagesData);
            $data["images"]=$imagesData;
        }

        $result['data'] = $data;
        return $result;
    }
}
?>
