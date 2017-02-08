<?php

require('DbConfig.php');

class Db {
    
    private $db = null;
    
    /**
    *初始化
    *@param String $host ip地址
    *@param int $port 端口
    *@param int $backlog 最大连接数
    */
    public function __construct()
    {
    
        $this->db = $this->getDb(); 
    }
    
    public function getDb(){
        
        if($this->db == null){

            $conn = mysql_connect(DbConfig::DBHOST, DbConfig::DBUSER, DbConfig::DBPWD) or die("error connecting") ; //连接数据库
            mysql_query("set names 'utf8'"); //数据库输出编码 应该与你的数据库编码保持一致.
            mysql_select_db(DbConfig::DBNAME); //打开数据库
            $this->db = $conn;
            
            return $this->db;
        }else{
            
            return $this->db;
        }
    }
    
    public function query($querySql){
        
        $retArray = array();
        $index = 0;
        $result = mysql_query($querySql, $this->getDb()); //查询    
        while($row = mysql_fetch_array($result))
        {
            $retValue[$index] = $row;
            $index++;
        }
        
        return $retArray;
    }
    
    public function close(){
        
        mysql_close();
    }
    
    
    
}
?>
