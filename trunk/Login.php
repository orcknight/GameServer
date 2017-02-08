<?php
  require('Db.php');
  
  $name = $_POST['name'];
  $password = $_POST['password'];
  
  $db = new Db();
  $result = $db->query("SELECT * FROM player WHERE name = '$name' AND password='MD5($password)'");
  
  $data['code'] = 0;
  $data['data'] = $result;
  header('Content-Type:application/json; charset=utf-8');
  exit(json_encode($data));
  
?>
