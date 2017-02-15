<?php
namespace Home\Controller;
use Think\Controller;
use Think\Log;
use Think\Model;
use OSS\OssClient;
use OSS\Core\OssException;

class DataController extends BaseController {


   static $chooseDB = 'test_luobojianzhi';//设定好用到的数据库
   //执行全部SQL转换方法，实现调节好每个小方法
   public function executeAll()
   {
    /*
   $this->convertProvince();
    $this->convertRegion();
    $this->convertCity();
    $this->conventSchool();
    $this->convertCateL1();
    $this->convertCateL2();
    $this->createTimeSpan();*/
    $this->conventJob();
   }

    //地区转换id程序
    public function convertRegion() {

          set_time_limit(0);//设置程序执行时间，默认是执行完120秒就不执行
          ini_set('memory_limit','1280M');//设置临时数据内存占用大小

        header("Content-type:text/html;charset=utf-8");

        $this->trackLog("execute", "convertRegion()");

        // 注意原表字段不区分驼峰写法。
        $sourceTable = "county";
        $targetTable = "gp_region";
        
        $filedsMap =array('id'=>'id','cid'=>'cId','name'=>'name'
            );

        $useDbSql = 'use '.self::$chooseDB;

        $this->trackLog("useDbSql", $useDbSql);

        $extSql = "SELECT * FROM ".$sourceTable;
        $this->trackLog("extSql", $extSql);

        $model = new Model();
        $model->execute( $useDbSql);

        $rows = $model->query($extSql);

        $fileds = "";
        $values = "";

        $extSqlList = "";
        foreach ($rows as $row) {

            foreach ($filedsMap as $key => $value) 
            {
                //$value=addslashes($value);

                if(strpos($value, ":")) 
                {

                    $_fileds = explode(':', $value);

                    $fileds = $fileds . $_fileds[0]. " ,";

                    if($_fileds[1] == "timestamp") {
                        $_value = date("Y-m-d H:i:s", $row[$key]); 
                    }

                    $values = $values . '"'.$_value.'"' . " ,";//把用户数据的单引号用双引号处理掉


                } else {
                    
                    $fileds = $fileds . $value." ,";

                    $values = $values . '"'. $row[$key]. '" ,';

                }   
            }

            $fileds = rtrim($fileds, ",");
            $values = rtrim($values, ",");

            $this->trackLog("fileds:", $fileds);
            $this->trackLog("values:", $values);

            

            $extSql = "INSERT INTO " . $targetTable . "(" . $fileds . ") VALUES(" . $values . ");";


            $this->trackLog("extSql", $extSql);
          

            $result = $model->execute($extSql);
            $this->trackLog("result", $result); 

            $fileds = "";
            $values = "";

            echo " region success!!!!<br>";

        }   

        
    }


      public function convertProvince() {

          set_time_limit(0);//设置程序执行时间，默认是执行完120秒就不执行
          ini_set('memory_limit','1280M');//设置临时数据内存占用大小

        header("Content-type:text/html;charset=utf-8");

        $this->trackLog("execute", "convertRegion()");

        // 注意原表字段不区分驼峰写法。
        $sourceTable = "province";
        $targetTable = "gp_province";
        
        $filedsMap =array('id'=>'id','name'=>'name'                                                                                                                                                                                                                                                                                                          
            );

        $useDbSql = 'use '.self::$chooseDB;

        $this->trackLog("useDbSql", $useDbSql);

        $extSql = "SELECT * FROM ".$sourceTable;
        $this->trackLog("extSql", $extSql);

        $model = new Model();
        $model->execute( $useDbSql);

        $rows = $model->query($extSql);

        $fileds = "";
        $values = "";

        $extSqlList = "";
        foreach ($rows as $row) {

            foreach ($filedsMap as $key => $value) 
            {
                //$value=addslashes($value);

                if(strpos($value, ":")) 
                {

                    $_fileds = explode(':', $value);

                    $fileds = $fileds . $_fileds[0]. " ,";

                    if($_fileds[1] == "timestamp") {
                        $_value = date("Y-m-d H:i:s", $row[$key]); 
                    }

                    $values = $values . '"'.$_value.'"' . " ,";//把用户数据的单引号用双引号处理掉


                } else {
                    
                    $fileds = $fileds . $value." ,";

                    $values = $values . '"'. $row[$key]. '" ,';

                }   
            }

            $fileds = rtrim($fileds, ",");
            $values = rtrim($values, ",");

            $this->trackLog("fileds:", $fileds);
            $this->trackLog("values:", $values);

            

            $extSql = "INSERT INTO " . $targetTable . "(" . $fileds . ") VALUES(" . $values . ");";


            $this->trackLog("extSql", $extSql);
          

            $result = $model->execute($extSql);
            $this->trackLog("result", $result); 

            $fileds = "";
            $values = "";

            echo " province success!!!!".'<br>';

        }   

        
    }

     public function convertCity() {

          set_time_limit(0);//设置程序执行时间，默认是执行完120秒就不执行
          ini_set('memory_limit','1280M');//设置临时数据内存占用大小

        header("Content-type:text/html;charset=utf-8");

        $this->trackLog("execute", "convertCity()");

        // 注意原表字段不区分驼峰写法。
        $sourceTable = "city";
        $targetTable = "gp_city";
          
        $filedsMap =array('id'=>'id','pid'=>'pId','name'=>'name'
            );

        $useDbSql = 'use '.self::$chooseDB;

        $this->trackLog("useDbSql", $useDbSql);

        $extSql = "SELECT * FROM ".$sourceTable;
        $this->trackLog("extSql", $extSql);

        $model = new Model();
        $model->execute( $useDbSql);

        $rows = $model->query($extSql);

        $fileds = "";
        $values = "";

        $extSqlList = "";
        foreach ($rows as $row) {

            foreach ($filedsMap as $key => $value) 
            {
                //$value=addslashes($value);

                if(strpos($value, ":")) 
                {

                    $_fileds = explode(':', $value);

                    $fileds = $fileds . $_fileds[0]. " ,";

                    if($_fileds[1] == "timestamp") {
                        $_value = date("Y-m-d H:i:s", $row[$key]); 
                    }

                    $values = $values . '"'.$_value.'"' . " ,";//把用户数据的单引号用双引号处理掉


                } else {
                    
                    $fileds = $fileds . $value." ,";

                    $values = $values . '"'. $row[$key]. '" ,';

                }   
            }

            $fileds = rtrim($fileds, ",");
            $values = rtrim($values, ",");

            $this->trackLog("fileds:", $fileds);
            $this->trackLog("values:", $values);
            
            $extSql = "INSERT INTO " . $targetTable . "(" . $fileds . ") VALUES(" . $values . ");";

            $this->trackLog("extSql", $extSql);
          
            $result = $model->execute($extSql);
            $this->trackLog("result", $result); 

            $fileds = "";
            $values = "";

            echo " city  success!!!!<br>";

        }   

        
    }

//只转换了第一级类别
      public function convertCateL1() {

          set_time_limit(0);//设置程序执行时间，默认是执行完120秒就不执行
          ini_set('memory_limit','1280M');//设置临时数据内存占用大小

        header("Content-type:text/html;charset=utf-8");

        $this->trackLog("execute", "convertCategory()");

        // 注意原表字段不区分驼峰写法。
        $sourceTable = "job_level1";
        $targetTable = "job_category";
          
        $filedsMap =array('id'=>'id','name'=>'name'
            );

        $useDbSql = 'use '.self::$chooseDB;

        $this->trackLog("useDbSql", $useDbSql);

        $extSql = "SELECT * FROM ".$sourceTable;
        $this->trackLog("extSql", $extSql);

        $model = new Model();
        $model->execute( $useDbSql);

        $rows = $model->query($extSql);

        $fileds = "";
        $values = "";

        $extSqlList = "";
        foreach ($rows as $row) {

            foreach ($filedsMap as $key => $value) 
            {
                //$value=addslashes($value);

                if(strpos($value, ":")) 
                {

                    $_fileds = explode(':', $value);

                    $fileds = $fileds . $_fileds[0]. " ,";

                    if($_fileds[1] == "timestamp") {
                        $_value = date("Y-m-d H:i:s", $row[$key]); 
                    }

                    $values = $values . '"'.$_value.'"' . " ,";//把用户数据的单引号用双引号处理掉


                } else {
                    
                    $fileds = $fileds . $value." ,";

                    $values = $values . '"'. $row[$key]. '" ,';

                }   
            }

            $fileds = rtrim($fileds, ",");
            $values = rtrim($values, ",");

            $this->trackLog("fileds:", $fileds);
            $this->trackLog("values:", $values);
            
            $extSql = "INSERT INTO " . $targetTable . "(" . $fileds . ") VALUES(" . $values . ");";

            $this->trackLog("extSql", $extSql);
          
            $result = $model->execute($extSql);
            $this->trackLog("result", $result); 

            $fileds = "";
            $values = "";

            echo " category L1  success!!!!<br>";

        }   

        
    }

    function convertCateL2()
    {
        set_time_limit(0);//设置程序执行时间，默认是执行完120秒就不执行
        ini_set('memory_limit','1280M');//设置临时数据内存占用大小

        $db=  new Model();
        $useDbSql = 'use '.self::$chooseDB;

        $quertL1SQL="SELECT id,name FROM job_category";
        $db->execute( $useDbSql);

        $resultL1 = $db->query($quertL1SQL);

        $quertL2SQL = "SELECT fname,name FROM job_level2";
        $resultL2 = $db->query($quertL2SQL);
        
       
    
        for($i=0;$i<count($resultL1);$i++)
        {
            
             for($j=0;$j<count($resultL2);$j++)
             {
                

                  if($resultL2[$j]['fname']==$resultL1[$i]['name'])
                  {
                     $pid=$resultL1[$i]['id'];
                     $L2Name=$resultL2[$j]['name'];
                     $L2Name="'".$L2Name."'";
                      $testSQL= "INSERT into job_category (pid,name) values ($pid,$L2Name)";
                     $result=$db->execute($testSQL);
                    
                   
                  }

             }
             
        }
        
          echo " category L2  success!!!!<br>";

     
    }

     function regionTransfer($regionId)
    {
        $db =new Model();
        $regionId="'".$regionId."'";
        $transSQL ="SELECT id from gp_region where name =$regionId";
       
        $result = $db->query($transSQL);
    
        $result =$result[0]['id'];
        return $result;
    }

    function cityTransfer($city=0)
    {
      
       $db =new Model();
       
       $city = "'".$city."'";
       
       $useDbSql = 'use '.self::$chooseDB;

       $db -> execute($useDbSql);
       $transSQL ="SELECT id from gp_city where name =$city";
       
       $result = $db->query($transSQL);
     
       return $result[0]['id'];
    }

      function schoolTransfer($school)
    {
       $db =new Model();
       $school = "'".$school."'";
       $useDbSql = 'use '.self::$chooseDB;

       $db -> execute($useDbSql);
       $transSQL ="SELECT id from gp_school where name =$school";
       
       $result = $db->query($transSQL);

       return $result[0]['id'];
       
    }
      function companyTransfer($companyId)
    {
       $db =new Model();
       $companyId = "'".$companyId."'";
       $useDbSql = 'use '.self::$chooseDB;

       $db -> execute($useDbSql);
       $transSQL ="SELECT id from company where companyName =$companyId";
       
       $result = $db->query($transSQL);

       return $result[0]['id'];
       
    }
   

      function level1Transfer($pCateId)
    {
       $db =new Model();
       $pCateId = "'".$pCateId."'";
       $useDbSql = 'use '.self::$chooseDB;

       $db -> execute($useDbSql);
       $transSQL ="SELECT id from job_category where name =$pCateId";
       
       $result = $db->query($transSQL);

       return $result[0]['id'];
       
    }
      function level2Transfer($cateId)
    {
       $db =new Model();
       $cateId = "'".$cateId."'";
       $useDbSql = 'use '.self::$chooseDB;

       $db -> execute($useDbSql);
       $transSQL ="SELECT id from job_category where name =$cateId";
       
       $result = $db->query($transSQL);

       return $result[0]['id'];
       
    }
     function priceUnitTransfer($incomeunit){
      
        switch ($incomeunit) {
        case "元/小时":
        return  1;
        break;

        case "元/天":
        return  2;
        break;
        case "元/周":
        return  3;
        break;
        case "元/月":
        return  4;
        break;

        default:
        return  "";
        break;
        }

     }

     function payTypeTransfer($payType)
     {

       switch ($payType) {
        case "日结":
        return  1;
        break;

        case "周结":
        return  2;
        break;
        case "月结":
        return  3;
        break;
        case "次结":
        return  4;
        break;
        case "完工结算":
        return 5;
        default:
        return  "";
        break;

      }
     }


      function provinceTransfer($provinceId)
    {
       
        if(is_numeric($provinceId))
        {
           return $provinceId;
        }
        else
        {
            $db =new Model();
            $useDbSql = 'use '.self::$chooseDB;
            $db -> execute($useDbSql);
            $province = "'".$province."'";
            $transSQL ="SELECT id from gp_province where name ='$provinceId'";
             
            $result = $db->query($transSQL);

            return $result[0]['id'];
        }
      
    }


  
    public function conventSchool()
    {
        set_time_limit(0);//设置程序执行时间，默认是执行完120秒就不执行
        ini_set('memory_limit','1280M');//设置临时数据内存占用大小

        

        $db=  new Model();
        $useDbSql = 'use '.self::$chooseDB;
        $db->execute( $useDbSql);

        $sourceTable = "schools";
        $targetTable = "gp_school";

        $sourceSQL="SELECT * FROM $sourceTable";
        
        $resultOld=$db->query( $sourceSQL);
         $name='';
         $pId='';
         $cId='';
        

        
        for($i=0;$i<count( $resultOld);$i++)
        {
           
            $resultOld[$i]['city']=$this->cityTransfer($resultOld[$i]['city']);
            $cId=$resultOld[$i]['city'];
            
         
            $resultOld[$i]['province']=$this->provinceTransfer($resultOld[$i]['province']);
            $pId=$resultOld[$i]['province'];
           
            $name="'".$resultOld[$i]['school']."'";
             $insertSQL= "INSERT into gp_school(name,pId,cId) values($name,$pId,$cId)";

           $db->execute($insertSQL);
          
        }
       

    }

    public function createTimeSpan()
    {
        $db=  new Model();
        $useDbSql = 'use '.self::$chooseDB;
        $db->execute( $useDbSql);

        $timeSpanSQL="CREATE TABLE if not exists`job_timespan`
        (
          `id` int(5) unsigned NOT NULL AUTO_INCREMENT,
          `name` varchar(32) DEFAULT NULL COMMENT '时间段名字',
          `status` tinyint(4) DEFAULT '1' COMMENT '0：不可用 1：可用',
          `weight` int(11) DEFAULT '0' COMMENT '权重',
          `creatTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
          `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
          PRIMARY KEY (`id`)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
                    ); 
           
             ";
             $db->execute($timeSpanSQL);
             $db->execute(" insert into `job_timespan` (`name`) values('今天');
            insert into `job_timespan` ( `name`) values('三天内');
            insert into `job_timespan` ( `name`) values('一周内');
            insert into `job_timespan` ( `name`) values('一月内');
            insert into `job_timespan` ( `name`) values('2小时内');");
             echo "time span success<br>";
    }

    //未完成
   
    public function conventJob() {

        header("Content-type:text/html;charset=utf-8");
        set_time_limit(0);//设置程序执行时间，默认是执行完120秒就不执行
        ini_set('memory_limit','1280M');//设置临时数据内存占用大小

        $db=  new Model();
        $useDbSql = 'use '.self::$chooseDB;
        $db->execute( $useDbSql);
        
        //$sourceSQL= "select * from jobs where id between 26397 and 500000 ";
        $sourceSQL= "select * from jobs ";

        $sourceRES =$db->query($sourceSQL);
        
        for($i=0;$i<count($sourceRES);$i++) {
            $id=$sourceRES[$i]['id'];
            $companyId =$sourceRES[$i]['uid'];
           /* $companyId=$this->companyTransfer( $companyId);*/
            $title=$sourceRES[$i]['title'];
            $type =1;//待定
            $status =1;
            $address=$sourceRES[$i]['address'];
            $linkman =$sourceRES[$i]['contacter'];
            $phone =$sourceRES[$i]['phone'];
            $beginTime=$sourceRES[$i]['starttime'];
            $endTime =$sourceRES[$i]['endtime'];
            $number=$sourceRES[$i]['number'];
            $income =$sourceRES[$i]['price'];
            $incomeunit =$sourceRES[$i]['pricetype'];
            $incomeunit =$this->priceUnitTransfer($incomeunit);
            
            $payType=$sourceRES[$i]['paytype'];
            
            $payType=$this->payTypeTransfer($payType);
            
            $intro =$sourceRES[$i]['intro'];
            $intro = addslashes($intro);
            $hot =$sourceRES[$i]['view'];

            $cateId =$sourceRES[$i]['level2'];
            $cateId= $this->level2Transfer($cateId);
            $pCateId=$sourceRES[$i]['level1'];
            $pCateId=$this->level1Transfer($pCateId);

            $provinceId = $sourceRES[$i]['province'];
            
            $provinceId = $this->provinceTransfer($provinceId);
            
            $cityId = $sourceRES[$i]['city'];
            
            $cityId = $this->cityTransfer($cityId);

            $regionId = $sourceRES[$i]['county'];
            
            $regionId = $this->regionTransfer($regionId);

            $createTime =$sourceRES[$i]['date'];
            //$updateTime=$sourceRES[$i]['updatetime'];防止updateTime字段是‘00000000’数据
            $source =$sourceRES[$i]['source'];
            
            $insertSQL ="INSERT into job(companyId,title,type,status,address,linkman,phone,beginTime,endTime,
            number,income,incomeunit,payType,intro,hot,cateId,pCateId,provinceId,cityId,regionId,createTime,source) values('$companyId','$title','$type','$status','$address','$linkman','$phone','$beginTime','$endTime','$number',
            '$income','$incomeunit','$payType','$intro','$hot','$cateId','$pCateId','$provinceId','$cityId','$regionId','$createTime','$source')";
            $db->execute($insertSQL);
        }

           echo ' job -->OK';

    }


    function updateTransfer()
    {
       $db=new Model();
       $table ='job';
       $idsSQL= "select id from $table ";
       $ids =$db->query($idsSQL);

       for($i=0;$i<count($ids);$i++)
       {
         $id=$ids[$i]['id'];
         $createTimeSQL= "select createTime from job where id= $id";
         $createTime=$db->query($createTimeSQL);
         $createTime="'".$createTime[$i]['createtime']."'";
         $updateSQL="update $table set updateTime =$createTime where id=$id";
         $db->execute($updateSQL);
       }
    echo "Ok";
       
    }


    public function getLatestData()
    {
        header("Content-type:text/html;charset=utf-8");
        set_time_limit(0);//设置程序执行时间，默认是执行完120秒就不执行
        ini_set('memory_limit','1280M');//设置临时数据内存占用大小

        $db = new Model();
        //第一次使用必须在job表里放入已经转化好的jobs最大ID
        $idSQL = "select id from job order by id desc";
        $idRES = $db->query($idSQL);
        $id= $idRES[0]['id'];
        

        $dataSQL = "SELECT * from jobs where id > $id";
       

        $sourceRES =$db->query($dataSQL);//得到数据相差的那部分

        
        
      if(count($sourceRES))
      {
          $clearSQL= "DELETE from job";
          $db->execute($clearSQL);

           $count =0;

         for($i=0;$i<count($sourceRES);$i++)
         {
            $count ++;
            $id=$sourceRES[$i]['id'];
            $companyId =$sourceRES[$i]['uid'];
           /* $companyId=$this->companyTransfer( $companyId);*/
            $title=$sourceRES[$i]['title'];
            $type =1;//待定
            $status =1;
            $address=$sourceRES[$i]['address'];
            $linkman =$sourceRES[$i]['contacter'];
            $phone =$sourceRES[$i]['phone'];
            $beginTime=$sourceRES[$i]['starttime'];
            $endTime =$sourceRES[$i]['endtime'];
            $number=$sourceRES[$i]['number'];
            $income =$sourceRES[$i]['price'];
            $incomeunit =$sourceRES[$i]['pricetype'];
            $incomeunit =$this->priceUnitTransfer($incomeunit);
            
            $payType=$sourceRES[$i]['paytype'];
            
            $payType=$this->payTypeTransfer($payType);
            
            $intro =$sourceRES[$i]['intro'];
            $intro = addslashes($intro);
            $hot =$sourceRES[$i]['view'];

            $cateId =$sourceRES[$i]['level2'];
            $cateId= $this->level2Transfer($cateId);
            $pCateId=$sourceRES[$i]['level1'];
            $pCateId=$this->level1Transfer($pCateId);

            $provinceId = $sourceRES[$i]['province'];
            
            $provinceId = $this->provinceTransfer($provinceId);
            
            $cityId = $sourceRES[$i]['city'];
            
            $cityId = $this->cityTransfer($cityId);

            $regionId = $sourceRES[$i]['county'];
            
            $regionId = $this->regionTransfer($regionId);

            $createTime =$sourceRES[$i]['date'];
            
            $source =$sourceRES[$i]['source'];
            
            $insertSQL ="INSERT into job(id,companyId,title,type,status,address,linkman,phone,beginTime,endTime,
            number,income,incomeunit,payType,intro,hot,cateId,pCateId,provinceId,cityId,regionId,createTime,source) values('$id','$companyId','$title','$type','$status','$address','$linkman','$phone','$beginTime','$endTime','$number',
            '$income','$incomeunit','$payType','$intro','$hot','$cateId','$pCateId','$provinceId','$cityId','$regionId','$createTime','$source')";
            $db->execute($insertSQL);
         }

        echo '查完了,一共查到差的部分 数据为：'.$count.' 条';
      }  
      else
      {
        echo "没有新数据！";
      }

        
       


    }
    



=======
>>>>>>> .r1061
}