<?php

require_once __DIR__ . '/DbConfig.php';

class DbHelper {
    
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

            $conn = mysqli_connect(DbConfig::DBHOST, DbConfig::DBUSER, DbConfig::DBPWD, DbConfig::DBNAME); //连接数据库
            $conn->set_charset('utf8');
            $this->db = $conn;
            
            return $this->db;
        }else{
            
            return $this->db;
        }
    }
    
    public function query($querySql){
        
        $retArray = array();
        $result = $this->getDb()->query($querySql); //查询  
        while($row = mysqli_fetch_assoc($result))
        {
            $retArray[] = $row;
        }
        
        return $retArray;
    }
    
    public function execute($execSql){
        
        $stmt = $this->getDb()->prepare($execSql);
        /* Execute the statement */
        return $stmt->execute();
    }
    
    public function close(){
        
        mysql_close();
    }
    
    
    
}
?>
