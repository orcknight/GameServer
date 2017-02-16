<?php if (!defined('THINK_PATH')) exit();?><!DOCTYPE html> 
<html> 
<head> 
<title>指间MUD用户注册</title> 
  <meta http-equiv='Content-Type' content='text/html; charset=utf-8' />
  <meta name="viewport" content="user-scalable=no, initial-scale=1, maximum-scale=1, minimum-scale=1, width=device-width" />
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="apple-mobile-web-app-capable" content="yes">
  <meta name="apple-mobile-web-app-status-bar-style" content="black">
  <meta name="msapplication-tap-highlight" content="no" />
  <meta name="format-detection" content="telephone=no" />
  
<link rel="stylesheet" type="text/css" href="/Public/css/register.css" />
<script type="text/javascript" src='/Public/js/checkbox.js' charset='utf-8'></script>
</head> 
<body> 
<form id="f1" action="signUp" method="post"> 
<table width="100%" align="center" cellspacing="0" class="table"> 
<tr class="thead"> 
<td align="center"> 
  用户注册</td> 
</tr> 
<tr> 
<td align="center"> 
  账　号： 
  <input type="text" name="id" placeholder="用户名" required/> 
</td> 
</tr> 
<tr> 
<td align="center"> 
  密　码： 
  <input type="password" name="pass" placeholder="密码" required/><br>
</td> 
</tr> 
<tr>
<td align="center"> 
  确　认： 
  <input type="password" name="pass2" placeholder="确认密码" required/> 
</td>
</tr>
<tr>
  <td align="center"> 
    邮　箱： 
    <input type="email" name="email" placeholder="邮箱" required/> 
  </td>
  </tr>
<tr> 
<td align="center"> 
  手　机： 
  <input type="tel" pattern="[0-9]{11}" name="phone" placeholder="请输入11位数字" /> 
</td> 
</tr> 
<tr> 
  <td align="center"><input type="submit" accesskey="enter" value="注册" id="btn" onmousemove="changeBgColor('btn')" onmouseout="recoverBgColor('btn');" class="submit" formmethod="post"/> 
  </td> 
</tr> 
</table> 
</form> 
</body> 
</html>