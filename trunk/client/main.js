
var sock = io.connect("ws://127.0.0.1:2020/", { 'reconnect': true });
var loginok = false;
var textcol = '#666'; pd_on = 'chat';
var dirnw='　',dirn='　',dirne='　',dirw='　',dire='　',dirsw='　',dirso='　',dirse='　',tit = '指间MUD';
var strsss = "";
var ansi_flag = false;

function htmlEncode ( str ) {
  var ele = document.createElement('span');
  ele.appendChild( document.createTextNode( str ) );
  return ele.innerHTML;
};

function htmlDecode ( str ) {
  var ele = document.createElement('span');
  ele.innerHTML = str;
  return ele.textContent;
};

function ansistr(str)
{
	if(ansi_flag)
		str = ansi_up.ansi_to_html(str);
	else
		str = ansi_up.ansi_to_text(str);
	return str;
}

function setCookie(name,value)
{
	var Days = 30;
	var exp = new Date();
	exp.setTime(exp.getTime() + Days*24*60*60*1000);
	document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
};

function getCookie(name)
{
	var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
	if(arr=document.cookie.match(reg))
	{
		return unescape(arr[2]);
	}
	else
	{
		return "";
	}
};

function delCookie(name) 
{ 
    var exp = new Date(); 
    exp.setTime(exp.getTime() - 1); 
    var cval=getCookie(name); 
    if(cval!=null) 
        document.cookie= name + "="+cval+";expires="+exp.toGMTString(); 
} 

function GetStr(name)
{
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");

     var r = window.location.search.substr(1).match(reg);

     if(r!=null)return  decodeURI(r[2]);
     return "";
};

/* look指令等，点击物品或者玩家已经npc会调用这个接口 */
function cmds(str) {
	sock.emit('stream',str.id+'\n');
};

/* 打开互动窗口，如聊天，背包的时候调用此函数 */
function cmdsa(str) {
	sock.emit('stream',str.id+'\n');
	$('div#hudong').hide();
};
 
/* 用聊天窗口发送命令的时候调用 */
function cmdsb() {
	if($('input#chatmsg').val()=='') return;
	sock.emit('stream',$('input#chatmsg').val()+'\n');
	document.getElementById("chatmsg").value="";
};

/* 交互窗口调用 比如npc的购物，玩家的给予，查看装备等功能 */
function cmdsc(str) {
	$('div#hudong').hide();
	$('div#mycmds').hide();
	if(str.id.substr(0,4)=='020')
	{
		writeToPop(str.id.substr(4));
	}
	else
	{
		sock.emit('stream',str.id+'\n');
	}
};

/* 聊天的时候调用 */
function says(str) {
	if($('input#chatmsg').val()=='') return;
	sock.emit('stream',str.id.replace('$txt#',$('input#chatmsg').val())+'\n');
	document.getElementById("chatmsg").value="";
};

/* 刚进入房间的时候清空内容，并进行刷新 */
function writeToST(str) {
	$('div#long').html('');
	$('div#exits').html('');
	$('div#obj').html('');
	$('div#mycmds').html('');
	$('div#mycmds').hide();
	$('div#acts').html('');
	$('div#hudong').hide();
	$('div#hudong').html('');
	dirnw='　';
	dirn='　';
	dirne='　';
	dirw='　';
	dire='　';
	dirsw='　';
	dirso='　';
	dirse='　';
	tit = ansistr(str);
};

/* 创建地图列表, 其中dirnw等变量是由writeToEX函数提前写入的 */
function writedirs() {
	var out = $('div#exits');
	out.html('<table width="100%" border="0" cellspacing="1" cellpadding="0">'
					+'<tr>'
						+'<td width="33%" style="height:30px" align="center">'+dirnw+'</td>'
						+'<td width="33%" style="height:30px" align="center">'+dirn+'</td>'
						+'<td width="33%" style="height:30px" align="center">'+dirne+'</td>'
					+'</tr>'
					+'<tr>'
						+'<td width="33%" style="height:30px" align="center">'+dirw+'</td>'
						+'<td width="33%" style="height:30px" align="center">'+tit+'</td>'
						+'<td width="33%" style="height:30px" align="center">'+dire+'</td>'
					+'</tr>'
					+'<tr>'
						+'<td width="33%" style="height:30px" align="center">'+dirsw+'</td>'
						+'<td width="33%" style="height:30px" align="center">'+dirso+'</td>'
						+'<td width="33%" style="height:30px" align="center">'+dirse+'</td>'
					+'</tr>'
				+'</table>');
};

/* 解析服务端传回的地图位置字符串，并赋值给相应的变量 */
function writeToEX(str) {
	var out = $('div#exits');
	var strs = str.split("$zj#");
	var idss = "";
	for(var i=0;i<strs.length;i++)
	{
		var arr = '';
		var dirs = strs[i].split(':');
		if(dirs.length==3)
		{
			idss = dirs[2];
		}
		else
			idss = dirs[0];

		dirs[1] = ansistr(dirs[1]);
		var dddd = '<button style="color:'+textcol+';width:95%;height:95%" type="button" id="'+idss+'" onclick="cmds(this)">' + dirs[1]+ '</button>';
		if(dirs[0]=='east'||dirs[0]=='eastup'||dirs[0]=='eastdown')
		{
			dire = dddd;
		}
		else if(dirs[0]=='west'||dirs[0]=='westup'||dirs[0]=='westdown')
		{
			dirw = dddd;
		}
		else if(dirs[0]=='north'||dirs[0]=='northup'||dirs[0]=='northdown')
		{
			dirn = dddd;
		}
		else if(dirs[0]=='south'||dirs[0]=='southup'||dirs[0]=='southdown')
		{
			dirso = dddd;
		}
		else if(dirs[0]=='southwest')
		{
			dirsw = dddd;
		}
		else if(dirs[0]=='southeast')
		{
			dirse = dddd;
		}
		else if(dirs[0]=='northwest')
		{
			dirnw = dddd;
		}
		else if(dirs[0]=='northeast')
		{
			dirne = dddd;
		}
		else
		{
			arr = "act";
		}
		if(arr=="act")
		{
			var acts = $('div#acts');
			acts.append('<input class="newbt" style="color:'+textcol+';width:95%;height:30px" type="button" id="'+idss+'" onclick="cmds(this)" value="' + dirs[1]+ '">');
			if(i<(strs.length-1)) acts.append('<br>');
		}
		else
		{
			writedirs();
		}
	}
};

/* 点击人物以后创建命令栏，写入动作栏 给予 禁言等界面 */
function writeToACT(str) {
	var r = 1,w = 3,h = 9,s = 30;
	var out = $('div#hudong');
	var mcmd = $('div#mycmds');
	var rs = str.match(/^\$.*?\#/);
	var ss = null;
	if(rs!=null)
	{
		ss = rs[0].split(/[\$,\#]/);
		if(ss!=null)
		{
			r = parseInt(ss[1]);
			if(r==1)
				r=4;
			w = parseInt(ss[2]);
			h = parseInt(ss[3]);
			s = parseInt(ss[4]);
		}
		str = str.replace(rs[0],'');
	}
	var strs = str.split("$zj#");
	for(var i=0;i<(strs.length/r+1);i++)
	{
		for(var j=0;j<r;j++)
		{
			if((i*r+j)>(strs.length-1)) break;
			var dirs = strs[i*r+j].split(':');
			if(dirs.length<2) continue;
			var hi = $(window).height()/(2*h)+5;
			var wi = 100/w-1;
			dirs[0] = dirs[0].replace("$br#", "<br>");
			dirs[0] = ansistr(dirs[0]);
			out.append('<button style="color:'+textcol+';width:'+wi+'%;height:'+hi+'px;" type="button" id="'+dirs[1]+'" onclick="cmdsc(this)">' + dirs[0]+ '</button>');
		}
	}
	$('div#hudong').css('height','auto');
	if($('div#hudong').height()>($(window).height()-$('div#long').height()-250-$('div#hps').height()))
	{
		$('div#hudong').height($(window).height()-$('div#long').height()-250-$('div#hps').height());
	}
};

/* 写血量 经验等栏目 */
function writeToHP(str) {
	var r = 5,w = 5,h = 25,s = 33;
	var out = $('div#hps');
	out.html('');
	var rs = str.match(/^\$.*?\#/);
	var ss = null;
	if(rs!=null)
	{
		ss = rs[0].split(/[\$,\#]/);
		if(ss!=null)
		{
			r = parseInt(ss[1]);
			w = parseInt(ss[2]);
			h = parseInt(ss[3]);
			s = parseInt(ss[4]);
		}
		str = str.replace(rs[0],'');
	}
	var strs = str.split("║");
	var ssss = '<table width="100%" border="1" cellspacing="0" cellpadding="0">';
	for(var i=0;i<(strs.length/r+1);i++)
	{
		ssss += '<tr>';
		for(var j=0;j<r;j++)
		{
			if((i*r+j)>(strs.length-1)) break;
			var dirs = strs[i*r+j].split(':');
			var hi = $(window).height()*3/(5*h);
			var wi = 100/w;
			dirs[0] = dirs[0].replace("$br#", "\n");
			if(dirs.length>2)
			{
				if(dirs[2].length==9)
					dirs[2]='#'+dirs[2].substr(3);
				sss = dirs[1].split("/");
				if(sss.length==3)
				{
					var wwii = sss[0]*99/sss[1];
					if(wwii>100) wwii = 100;
					var wwi = sss[1]*99/sss[2];
					if(wwi>100) wwi = 100;
					ssss += '<td width="'+wi+'%" style="background-color:#444;height:'+hi+'px">'+
								'<div style="width:'+wwi+'%;background-color:#aaa;height:100%">'+
									'<div class="hpbt" style="background-color:'+dirs[2]+';width:'+wwii+'%;height:100%">'+
										'<input class="hpbt" style="width:'+($(window).width()/w-3)+'px;height:100%" type="button" value="' + dirs[0]+ '">'+
									'</div>'+
								'</div>'+
							'</td>';
				}
				else if(sss.length==2)
				{
					var wii = sss[0]*99/sss[1];
					if(wii>99) wii = 99;
					ssss += '<td width="'+wi+'%" style="background-color:#aaa;height:'+hi+'px">'+
								'<div class="hpbt" style="background-color:'+dirs[2]+';width:'+wii+'%;height:100%">'+
									'<input class="hpbt" style="width:'+($(window).width()/w-3)+'px;height:100%" type="button" value="' + dirs[0]+ '">'+
								'</div>'+
							'</td>';
				}
				else
				{
					ssss += '<td width="'+wi+'%" style="background-color:#aaa">'+
								'<div class="hpbt" style="background-color:'+dirs[2]+';width:99%;height:'+hi+'px;">'+
									'<input class="hpbt" style="width:'+($(window).width()/w-3)+'px;height:'+hi+'px;" type="button" value="' + dirs[0]+ '">'+
								'</div>'+
							'</td>';
				}
			}
			else
			{
				ssss += '<input class="hpbt" style="width:'+wi+'%;height:'+hi+'px;" type="button" value="' + dirs[0]+ '" />';
			}
		}
		ssss += '</tr>';
	}
	ssss += '</table>';
	out.html(ssss);
	$('div#hps').css('height','auto');
};

/* 写顶层按钮 比如选择技能以后出现的请教按钮 */
function writeToPop(str) {
	var r = 1,w = 2,h = 9,s = 30;
	var out = $('div#hudong');
	var mcmd = $('div#mycmds');
	out.show('');
	out.html('');
	mcmd.html('');
	mcmd.show();
	mcmd.append('<input type="button" style="color:'+textcol+';height:45px" onclick="close_hd()" value="关闭窗口">');
	var rs = str.match(/^\$.*?\#/);
	var ss = null;
	if(rs!=null)
	{
		ss = rs[0].split(/[\$,\#]/);
		if(ss!=null)
		{
			r = parseInt(ss[1]);
			w = parseInt(ss[2]);
			h = parseInt(ss[3]);
			s = parseInt(ss[4]);
		}
		str = str.replace(rs[0],'');
	}
	var strs = str.split("$z2#");
	for(var i=0;i<(strs.length/r+1);i++)
	{
		for(var j=0;j<r;j++)
		{
			if((i*r+j)>(strs.length-1)) break;
			var dirs = strs[i*r+j].split('|');
			if(dirs.length<2) continue;
			var hi = $(window).height()/(2*h)+5;
			var wi = 100/w-1;
			dirs[0] = dirs[0].replace("$br#", "<br>");
			dirs[0] = ansistr(dirs[0]);
			out.append('<button style="color:'+textcol+';width:'+wi+'%;height:'+hi+'px;" type="button" id="'+dirs[1]+'" onclick="cmdsa(this)">' + dirs[0]+ '</button>');
		}
	}
	$('div#hudong').css('height','auto');
	if($('div#hudong').height()>($(window).height()-$('div#long').height()-250-$('div#hps').height()))
	{
		$('div#hudong').height($(window).height()-$('div#long').height()-250-$('div#hps').height());
	}
};

function close_hd()
{
	$('#mycmds').hide();
	$('#hudong').hide();
	$('#map').hide();
};

/* 写人物简介按钮 */
function writeToHD(str) {
	var out = $('div#hudong');
	out.show();
	out.html('');
	var mcmd = $('div#mycmds');
	mcmd.html('');
	mcmd.show();
	mcmd.append('<input type="button" style="color:'+textcol+';height:45px" onclick="close_hd()" value="关闭窗口">');
/*
	var ss = str.match(/\[s.*?\]/g);
	if(ss!=null)
	{
		for(var i=0;i<ss.length;i++)
		{
			str = str.replace(ss[i],'');
		}
	}
*/
	var strs = str.split("$br#");

	for(var i=0;i<strs.length;i++)
	{
		if(strs[i].substr(0,15)=='一一一一一一一一一一一一一一一')
		{
			strs[i] = '一一一一一一一一一一一一一一一一一一一';
		}
		strs[i] = ansistr(strs[i]);
		out.append('<span class="out">'+ strs[i]+ '</span>');
		if(i<(strs.length-1)) out.append('<br>');
	}
	$('div#hudong').css('height','auto');
	if($('div#hudong').height()>($(window).height()-$('div#long').height()-250-$('div#hps').height()))
	{
		$('div#hudong').height($(window).height()-$('div#long').height()-250-$('div#hps').height());
	}
};

/* 写地图全景 */
function writeToMAP(str) {
	var out = $('div#map');
	out.show();
	out.html('');
	var mcmd = $('div#mycmds');
	mcmd.html('');
	mcmd.show();
	mcmd.append('<input type="button" style="color:'+textcol+';height:45px" onclick="close_hd()" value="关闭窗口">');
/*
	var ss = str.match(/\[s.*?\]/g);
	if(ss!=null)
	{
		for(var i=0;i<ss.length;i++)
		{
			str = str.replace(ss[i],'');
		}
	}
*/
	var strs = str.split("$br#");

	for(var i=0;i<strs.length;i++)
	{
		strs[i] = ansistr(strs[i]);
		out.append('<span class="map">'+ strs[i]+ '</span>');
		if(i<(strs.length-1)) out.append('<br>');
	}
	$('div#map').css('height','auto');
	if($('div#map').height()>($(window).height()-$('div#long').height()-250-$('div#hps').height()))
	{
		$('div#map').height($(window).height()-$('div#long').height()-250-$('div#hps').height());
	}
};

/* 写对象列表 */
function writeToOBJ(str) {
	var out = $('div#obj');
	var strs = str.split("$zj#");
	for(var i=0;i<strs.length;i++)
	{
		var dirs = strs[i].split(':');
		dirs[0] = ansistr(dirs[0]);
		out.append('<button style="color:'+textcol+';width:95%;height:30px" type="button" style="height:30px" id="'+dirs[1]+'" onclick="cmds(this)">'+ dirs[0]+ '</button>');
		if(i<(strs.length-1)) out.append('<br>');
	}
};

/* 移除一个对象 */
function removeOBJ(str) {
	var ob = document.getElementById(htmlDecode(str));
	if(!ob)
	{
		str = str.replace('\n','');		
		str = str.replace('\r','');		
		ob = document.getElementById(htmlDecode(str));
	}
	ob.parentNode.removeChild(ob);
};

/* 写聊天窗口 */
function writeToCHAT(str) {
	str = str.replace(/\【/, '[ ');
	str = str.replace(/\】/, ' ] ');
	var out = $('div#chat');
/*
	var ss = str.match(/\[s.*?\]/g);
	if(ss!=null)
	{
		for(var i=0;i<ss.length;i++)
		{
			str = str.replace(ss[i],'');
		}
	}
*/
	str = ansistr(str);
	out.append('<span class="chat">' + str + '</span><br>');
	out.scrollTop(out.prop("scrollHeight"));
};

/* 写场景描述 */
function writeToLO(str) {
	str = str.replace(/\【/, '[ ');
	str = str.replace(/\】/, ' ]');
	var out = $('div#long');
	out.append('<span class="out">' + str + '</span>');
};

function close_mycmds()
{
	$('#mycmds').hide();
};

/* 写命令窗口， 如查看背包，综合属性 */
function writeToMU(str) {
	var out = $('div#mycmds');
	out.show();
	var strs = str.split("$zj#");
	out.html('');
	for(var i=0;i<strs.length;i++)
	{
		var dirs = strs[i].split(':');
		if(dirs[1]=='***'||dirs[1]=='暂未设定') continue;
		dirs[1] = ansistr(dirs[1]);
		dirs[1] = dirs[1].replace("\n", '<br>');
		out.append('<button style="color:'+textcol+';margin:2px;width:15%;height:42px;" type="button" id="'+dirs[2]+'" onclick="cmdsa(this)">' + dirs[1]+ '</button>');
	}
	out.append('<button style="color:'+textcol+';margin:2px;width:15%;height:42px;" type="button" onclick="close_mycmds()">关闭<br>选项</button>');
};

/* 写聊天主窗口 */
function writeToScreen(str) {
	str = str.replace(/\【/, '[ ');
	str = str.replace(/\】/, ' ]');
	var out = $('div#out');
	str = ansistr(str);
	out.append('<span class="out">' + str + '</span>');
	var node = document.getElementById("out"); 
	if(node.hasChildNodes())
	{
		var childList = node.childNodes;
		if(childList.length>30)
			node.removeChild(childList[0]);
	}
	out.scrollTop(out.prop("scrollHeight"));
};

/* 写登陆窗口 */
function writelogin() {
	var out = $('div#hudong');
	var cha = $('div#chat');
	$('div#out').html('');
	$('div#hudong').show();
	out.html('');
	cha.html('');
	cha.append('<span class="out">欢迎进入'+GetStr("server")+'。。。</span><br>');
	out.append('<span class="out">请输入账号：</span><br>');
	out.append('<span class="out"><input type="text" value="'+getCookie('myid')+'" id="id"></span><br>');
	out.append('<span class="out">请输入密码：<br></span>');
	out.append('<span class="out"><input type="password" value="'+getCookie('mypass')+'" id="pass"></span><br><br>');
	out.append('<span class="out"><input type="button" id="loginok" value="登录游戏" onclick="logincheck()"></span>');
};

/* 写建立角色窗口 */
function writechar() {
	var out = $('div#hudong');
	var cha = $('div#chat');
	out.show();
	out.html('');
	cha.html('');
	cha.append('<span class="out">欢迎进入'+GetStr("server")+'。。。</span><br>');
	out.append('<span class="out">请输入昵称：</span><br>');
	out.append('<span class="out"><input type="text" id="nicheng"></span><br>');
	out.append('<span class="out">请选择性别：</span><br>');
	out.append('<form><input type="radio" style="width:50px;height:10px" name="sex" value="男性" id="nan"><span style="width:90px;height:20px;line-height:0px" class="out">男性　</span><input type="radio" style="width:50px;height:10px" name="sex" value="女性" id="nv"><span style="width:90px;height:20px;line-height:0px" class="out">女性　</span></form><br>');
	out.append('<span class="out"><input type="button" id="loginok" value="建立角色" onclick="charcheck()"></span>');
};

/* 登陆验证 */
function logincheck(id,pass) {
	var myid,mypass;
	if(id!=null)
		myid = id;
	else
	{
		myid = $('input#id').val();
	}
	if(pass!=null)
	{
		mypass = pass;
	}
	else
	{
		mypass = $('input#pass').val();
	}
	if(myid==''||mypass=='')
	{
		alert('账号密码不能为空！');
		return;
	}
	$('div#hudong').html('');
	$('div#chat').html('登录中。。。<br>');

    $.ajax({
        type : "get",
        async : false,
        url : "http://game.gate.com/index.php/Home/User/login",
        data : {"id" : myid,"pass" : mypass},
        cache : false, 
        dataType : "jsonp",
        jsonp: "callback",
        success : function(json){
            if(json.sta=='argerr')
			{
				$('div#hudong').append('参数错误！请返回重试。');
				$('div#hudong').append('<span class="out"><input type="button" id="loginok" value="返回登陆" onclick="writelogin()"></span>');
			}
            else if(json.sta=='passerr')
			{
				$('div#hudong').append('密码错误！请返回重试。');
				$('div#hudong').append('<span class="out"><input type="button" id="loginok" value="返回登陆" onclick="writelogin()"></span>');
			}
            else if(json.sta=='iderr')
			{
				$('div#hudong').append('无此账号！请返回重试。');
				$('div#hudong').append('<span class="out"><input type="button" id="loginok" value="返回登陆" onclick="writelogin()"></span>');
			}
			else
			{
				setCookie('myid',myid);
				setCookie('mypass',mypass);
				setCookie('auto_login',true);
				sock.emit('stream',myid+'║'+mypass+'║'+mypass+'║'+json.sta+'\n');
			}
        },
        error:function(xhr,status,error){
            alert(error);
        }
    });
};

/* 建立角色验证 */
function charcheck() {
	var myname = $('input#nicheng');
	var mysex = $("input[name='sex']:checked");
	if(myname.val()=='')
	{
		alert('昵称不能为空！');
		return;
	}
	if(mysex.val()!="男性"&&mysex.val()!="女性")
	{
		alert('请选择性别！');
		return;
	}
	$('div#hudong').html('');
	$('div#chat').html('角色创建中。。。<br>');

	sock.emit('stream',mysex.val()+'║001║'+myname.val()+'\n');
};

/* 重新登陆 */
function relogin()
{
	sock.emit('stream','quit\n');
	delCookie('auto_login');
	location.reload(true);
};

function filepath(obj)  
{  
	if(obj)  
	{
		if (window.navigator.userAgent.indexOf("MSIE")>=1)  
		{  
			obj.select();
			return document.selection.createRange().text;  
		}
		else if(window.navigator.userAgent.indexOf("Firefox")>=1)  
		{  
			if(obj.files)  
			{
				return obj.files.item(0).getAsDataURL();  
			}  
			return obj.value;  
		}  
		return obj.value;  
	}  
}  

/* 切换背景 */
function changebk(str)
{
	$('body').css('background','url(http://www.zjmud.cn/mud/img/'+str.id+'.jpg)');
	$('body').css('background-size',$(window).width()+'px '+$(window).height()+'px');
	$('div#hudong').css('background','url(http://www.zjmud.cn/mud/img/'+str.id+'.jpg)');
	$('div#hudong').css('background-size',$(window).width()+'px '+$(window).height()+'px');
	$('div#mycmds').css('background','url(http://www.zjmud.cn/mud/img/'+str.id+'.jpg)');
	$('div#mycmds').css('background-size',$(window).width()+'px '+$(window).height()+'px');
}

/* 写配置窗口 */
function config()
{
	var out = $('div#hudong');
	out.show();
	out.html('');
	out.append('<span class="out" style="margin:3px;font-size:15px">背景图片：'+
		'<br><input type="file" style="margin:3px;width:95%;height:25px" id="bgpic" accept="image/" /></span>');
	out.append('<span class="out" style="margin:3px;font-size:15px">情景模式：</span>');
	out.append('<br><input type="button" style="color:'+textcol+';margin:3px;width:50px;height:70px" id="daymod" value="日间" />');
	out.append('<input type="button" style="color:'+textcol+';margin:3px;width:50px;height:70px" id="nightmod" value="夜间" />');
	out.append('<input type="button" style="color:'+textcol+';margin:3px;width:50px;height:70px" id="ansitext" value="彩字" />');
	out.append('<input type="button" style="color:'+textcol+';background:url(http://www.zjmud.cn/mud/img/mod_b_2.jpg);background-size:50px 70px;margin:3px;width:50px;height:70px" id="mod_b_2" onclick="changebk(this)" value="湖畔" />');
	out.append('<input type="button" style="color:'+textcol+';background:url(http://www.zjmud.cn/mud/img/mod_b_5.jpg);background-size:50px 70px;margin:3px;width:50px;height:70px" id="mod_b_5" onclick="changebk(this)" value="远山" />');
	out.append('<input type="button" style="color:'+textcol+';background:url(http://www.zjmud.cn/mud/img/mod_b_7.jpg);background-size:50px 70px;margin:3px;width:50px;height:70px" id="mod_b_7" onclick="changebk(this)" value="绿洲" />');
	out.append('<input type="button" style="color:'+textcol+';background:url(http://www.zjmud.cn/mud/img/mod_b_8.jpg);background-size:50px 70px;margin:3px;width:50px;height:70px" id="mod_b_8" onclick="changebk(this)" value="禅心" />');
	out.append('<br><input type="button" style="color:'+textcol+';margin:3px;width:80px;height:45px" id="exit_login" value="返回登陆" onclick="relogin()" />');
	out.css('height','auto');
	var mcmd = $('div#mycmds');
	mcmd.html('');
	mcmd.show();
	mcmd.append('<input type="button" style="color:'+textcol+';height:45px" onclick="close_hd()" value="关闭窗口">');
	if(out.height()>($(window).height()-$('div#long').height()-250))
		out.height($(window).height()-$('div#long').height()-250);

	document.getElementById('daymod').onclick = function(){
		$('body').css('background','#f8f8f8');
		$('body').css('color','#666');
		$('input.menubt').css('color','#eee');
		textcol = '#666';
		$('div#hudong').css('background','#f8f8f8');
		$('div#mycmds').css('background','#f8f8f8');
	};

	document.getElementById('nightmod').onclick = function(){
		$('body').css('background','#333');
		$('body').css('color','#aaa');
		textcol = '#aaa';
		$('div#hudong').css('background','#333');
		$('div#mycmds').css('background','#333');
	};

	document.getElementById('ansitext').onclick = function(){
		if(ansi_flag)
			ansi_flag = false;
		else
			ansi_flag = true;
	};

	document.getElementById('bgpic').onchange = function(){
		$('body').css('background','url('+window.URL.createObjectURL(this.files[0])+') repeat');
		$('body').css('background-size',$(window).width()+'px '+$(window).height()+'px');
		$('div#hudong').css('background','url('+window.URL.createObjectURL(this.files[0])+') repeat');
		$('div#hudong').css('background-size',$(window).width()+'px '+$(window).height()+'px');
		$('div#mycmds').css('background','url('+window.URL.createObjectURL(this.files[0])+') repeat');
		$('div#mycmds').css('background-size',$(window).width()+'px '+$(window).height()+'px');
	};
};

/* 解析服务器数据并绘制图形 */
function writeServerData(buf) {
	var line = buf;
	
		if(line.length<4) return;

		if(line.substr(0,6)=='版本验证成功')
		{
			if(getCookie('myid')&&getCookie('mypass')&&getCookie('auto_login'))
			{
				logincheck(getCookie('myid'),getCookie('mypass'));
			}
			else
			{
				writelogin();
			}
			return;
		}

        /* 创建脚色窗口 */
		if(line.substr(0,8)=='0000008')
		{
			writechar();
			return;
		}
        /* 创建菜单 */
		if(line.substr(0,8)=='0000007')
		{
			loginok = true;
			$('div#long').show();
			$('div#out').html('');
			$('div#out').show();
			adjustLayout();
			var menu = $('div#menu');
			menu.html('<table width="100%" border="0" cellspacing="0" cellpadding="1"><tr>'
				+'<td align="center" valign="middle"><input class="menubt" type="button" name="b12" id="mycmds ofen" onclick="javascript:cmdsa(this)" value="常用"></td>'
				+'<td align="center" valign="middle"><input class="menubt" type="button" name="b13" id="mycmds skill" onclick="javascript:cmdsa(this)" value="技能"></td>'
				+'<td align="center" valign="middle"><input class="menubt" type="button" name="b15" id="mycmds fight" onclick="javascript:cmdsa(this)" value="绝技"></td>'
				+'<td align="center" valign="middle"><input class="menubt" type="button" name="b14" id="mycmds quest" onclick="javascript:cmdsa(this)" value="任务"></td>'
				+'<td align="center" valign="middle"><input class="menubt" type="button" name="b17" id="mycmds help" onclick="javascript:cmdsa(this)" value="指南"></td>'
				+'<td align="center" valign="middle"><input class="menubt" type="button" name="b16" id="liaotian" onclick="javascript:cmdsa(this)" value="聊天"></td>'
				+'<td align="center" valign="middle"><input class="menubt" type="button" name="b11" id="look" onclick="javascript:config()" value="系统"></td></tr>'
				+'</table>');
			return;
		}

		if(!loginok)
		{
			if(line.substr(0,4)=='015')//弹出提示
			{
				alert(line.substr(4));
				writelogin();
			}
			return;
		}
		if(line.substr(0,15)=='一一一一一一一一一一一一一一一')
		{
			line = '一一一一一一一一一一一一一一一一一一一\n';
		}
		if(line.substr(0,4)=='100')//频道信息
		{
			writeToCHAT(line.substr(4));
		}
		else if(line.substr(0,4)=='001')//输入框
		{
			var ss = line.substr(4).split("$zj#");
			ss[0] = ss[0].replace("$br#",'\n');
			writeToHD(ss[0]);
			$('#mycmds').hide();
			$('div#hudong').append('<input type="text" style="color:'+textcol+';width:98%;height:29px;font-weight:normal;padding:2px" id="chatmsg">');
			$('div#hudong').append('<br><input type="button" id="'+ss[1]+'" style="color:'+textcol+';width:32%;height:35px;margin-left:1px" '
							+'onclick="says(this)" value="确定">');
			$('div#hudong').append('<input type="button" id="'+ss[1]+'" style="color:'+textcol+';width:33%;height:35px;margin-left:1px" '
							+'onclick="close_hd()" value="取消">');
			$('div#hudong').append('<input type="button" id="'+ss[1]+'" style="color:'+textcol+';width:32%;height:35px;margin-left:1px" '
							+'onclick="cmdsb()" value="发送命令">');
		}
		else if(line.substr(0,4)=='002')
		{
			writeToST(line.substr(4));
		}
		else if(line.substr(0,4)=='003')
		{
			writeToEX(line.substr(4));
		}
		else if(line.substr(0,4)=='006')
		{
			line = line.replace(/\$br\#/g,'\n');
			writeToMU(line.substr(4));
		}
		else if(line.substr(0,4)=='004')
		{
			writeToLO(line.substr(4));
		}
		else if(line.substr(0,4)=='005')
		{
			writeToOBJ(line.substr(4));
		}
		else if(line.substr(0,4)=='905')
		{
			removeOBJ(line.substr(4));
		}
		else if(line.substr(0,4)=='007')
		{
			writeToHD(line.substr(4));
		}
		else if(line.substr(0,4)=='008'||line.substr(0,4)=='009')
		{
			writeToACT(line.substr(4));
		}
		else if(line.substr(0,4)=='011')
		{
			writeToMAP(line.substr(4));
		}
		else if(line.substr(0,4)=='012')
		{
			writeToHP(line.substr(4));
		}
		else if(line.substr(0,4)=='014')
		{
			sock.emit('stream',line.substr(4)+'\n');
		}
		else if(line.substr(0,4)=='015')
		{
			writeToScreen(line.substr(4));
		}
		else if(line.substr(0,4)=='020')
		{
			writeToPop(line.substr(4));
		}
		else
		{
			writeToScreen(line);
		}
	adjustLayout();
};

/* 适应屏幕 */
function adjustLayout() {
	if(!loginok) return;

	var w = $(window).width(), h = $(window).height();

	$('div#chat').css('width','100%');
	$('div#mleft').css('width','18%');
	$('div#mleft').height(h-127-$('div#long').height()-$('div#hps').height());
	$('div#mright').css('width','81%');
	$('div#mright').height(h-127-$('div#long').height()-$('div#hps').height());
	$('div#acts').css('width','100%');
	$('div#obj').css('width','100%');
	$('div#obj').height(h-127-$('div#long').height()-$('div#acts').height()-$('div#hps').height());
	$('div#exits').height(95);
	$('div#exits').css('width','100%');
	$('div#chat').height(62);
	$('div#chat').css('width',w-6);
	$('div#long').height(38);
	$('div#long').css('width',w-6);
	$('div#mycmds').height(92);
	$('div#mycmds').css('width','100%');
	$('div#menu').height(50);
	$('div#out').height(h-$('div#long').height()-222-$('div#hps').height());
	$('div#out').css('width','100%');
};

$(window).resize(adjustLayout);

$(document).ready(function(){
	document.title = GetStr("server");
	$('div#long').hide();
	$('div#hudong').hide();
	$('div#map').hide();
	$('div#out').hide();

	sock.on('stream', function(buf){
		buf = strsss+buf;
		strsss = "";
		var tmpstr = buf.split("\n");
		for(var ii=0;ii<tmpstr.length;ii++)
		{
			if(buf.charAt(buf.length-1)!="\n"&&ii==(tmpstr.length-1))
			{
				strsss += tmpstr[ii];
				return;
			}
			writeServerData(tmpstr[ii]);
		}
	});
	sock.on('status', function(str){
		writeToScreen(str);
	});
	sock.on('connected', function(){
		console.log('连接成功。');
	});
	sock.on('disconnect', function(){
		console.log('断开连接。');
	});

	var send = function(str) {
		if(sock) sock.emit('stream', str);
	};
	setTimeout(function(){
		adjustLayout();
		send('\n');
	},200);
});
