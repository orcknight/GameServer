<?php
  require('Db.php');
  
  //ob_start();;
  $name = $_POST['name'];
  $password = md5($_POST['password']);
  
  $db = new Db();
  $result = $db->query("SELECT * FROM player WHERE name = '$name' AND password='$password'");
  
  $data['code'] = 0;
  $data['data'] = $result;
  
  //ob_end_clean();
  header('Content-Type:application/json; charset=utf-8');
  exit(json_encode($data));
  
?>
