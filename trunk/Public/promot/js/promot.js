/**
 * Created by StandOpen on 16/2/27.
 */
function hide_load(){
    $('.am-toast').fadeOut();
    $('.am-toast').remove();
}

function show_load(className , text){

    $('body').append("<div class='am-toast' style='z-index: 13500;position: absolute'>" +
        "<div class='am-toast-text'>" +
        "<span class='am-toast-icon am-icon' am-mode='toast-"+className+"'></span>" +
        "<strong>"+text+"</strong>" +
        "</div>" +
        "</div>"
    );

    if(className == "fail" || className == "success")
    {
        setTimeout(function(){
            hide_load();
        },2000);
    }

}

function show_alert(title,msg,txt,callback)
{

    var dlg_html = '<div class="weui_dialog_alert" id="dialog2" style="z-index: 13500;display: none;position: absolute">'+
        '<div class="weui_mask"></div>'+
        '<div class="weui_dialog">'+
        '<div class="weui_dialog_hd"><strong class="weui_dialog_title">'+title+'</strong></div>'+
        '<div class="weui_dialog_bd">'+msg+'</div>'+
        '<div class="weui_dialog_ft">'+
        '<a id="dialog2_btn" class="weui_btn_dialog primary">'+txt+'</a>'+
        '</div>'+
        '</div>'+
        '</div>';

    $('body').append(dlg_html);

    $('#dialog2').show();

    $('#dialog2_btn').on('click',function(){
        $('#dialog2').hide();
        $('#dialog2').remove();
        callback();
    });

}


function show_alert(title,msg,txt,callback)
{

    var dlg_html = '<div class="weui_dialog_alert" id="dialog2" style="z-index: 13500;display: none;position: absolute">'+
        '<div class="weui_mask"></div>'+
        '<div class="weui_dialog">'+
        '<div class="weui_dialog_hd"><strong class="weui_dialog_title">'+title+'</strong></div>'+
        '<div class="weui_dialog_bd">'+msg+'</div>'+
        '<div class="weui_dialog_ft">'+
        '<a id="dialog2_btn" class="weui_btn_dialog primary">'+txt+'</a>'+
        '</div>'+
        '</div>'+
        '</div>';

    $('body').append(dlg_html);

    $('#dialog2').show();

    $('#dialog2_btn').on('click',function(){
        $('#dialog2').hide();
        $('#dialog2').remove();
        callback();
    });

}


function show_comfirm(title,msg,left_txt,right_txt,callback)
{

    var dlg_html = '<div class="weui_dialog_confirm modal1 modal1-in" id="dialog1" style="z-index: 13500;display: none;position: absolute">'+
        '<div class="weui_mask"></div>'+
        '<div class="weui_dialog">'+
        '<div class="weui_dialog_hd"><strong class="weui_dialog_title">'+title+'</strong></div>'+
        '<div class="weui_dialog_bd">'+msg+'</div>'+
        '<div class="weui_dialog_ft">'+
        '<a id="tbb_dlg_left" class="weui_btn_dialog default">'+left_txt+'</a>'+
        '<a id="tbb_dlg_right"  class="weui_btn_dialog primary">'+right_txt+'</a>'+
        '</div>'+
        '</div>'+
        '</div>';

    $('body').append(dlg_html);

    $('#dialog1').show();

    $('#tbb_dlg_left').on('click',function(){
        $('#dialog1').hide();
        $('#dialog1').remove();
        callback(1);
    });
    $('#tbb_dlg_right').on('click',function(){
        $('#dialog1').hide();
        $('#dialog1').remove();
        callback(2);
    });
}

function dopost(url,form_id,type,jump_url)
{
    show_load('loading','加载中');
    var options = {
        url: url,
        type: 'post',
        dataType: 'json',
        data: $('#'+form_id).serializeArray(),
        success: function (data) {
            hide_load();
            if (data.errcode == 0) {
                if(type == 1)
                {
                    window.history.back();
                }
                else
                {
                    window.location.href=jump_url;
                }
            }
            else {

                show_load('fail',data.errmsg);
            }
        }
    };
    $.ajax(options);
}

function doget(url,type,jump_url)
{
    show_load('loading','加载中');
    var options = {
        url: url,
        type: 'get',
        dataType: 'json',
        success: function (data) {
            hide_load();
            if (data.errcode == 0) {
                if(type == 1)
                {
                    window.history.back();
                }
                else if(type == 2)
                {
                    window.location.href=jump_url;
                }
                else if(type == 3)
                {
                    window.location.reload();
                }
            }
            else {

                show_load('fail',data.errmsg);
            }
        }
    };
    $.ajax(options);
}


function change_prov()
{
    
   
    var prov_id = $('#province').val();
    var url = '/index.php/Home/Promot/getCity';
 
    show_load('loading','加载中....');
    var options = {
        url: url,
        type: 'post',
        dataType: 'json',
        data:{prov_id:prov_id},
        success: function (data) {
        	
            hide_load();
            if (data.errcode == 0) {

                var html = '';
                var city_list = data.city_list;
                for(var i=0;i<city_list.length;i++)
                {
                    html += '<option value="'+city_list[i]['id']+'">'+city_list[i]['name']+'</option>';
                }
                 
                $('#city').html(html);
                $("#city").trigger('change');
                
                change_city();
            }
            else {
                show_load('fail',data.errmsg);
            }
        }
    };
    $.ajax(options);
}

function change_city()
{
    var city_id = $('#city').val();

    var url = '/index.php/home/Promot/getRegion';
    show_load('loading','加载中');
    var options = {
        url: url,
        type: 'post',
        dataType: 'json',
        data:{city_id:city_id},
        success: function (data) {

            hide_load();
            if (data.code == 1) {
          
                var html = '';
                var regionList = data.data;
               
                for(var i=0;i<regionList.length;i++)
                {
                    html += '<option value="'+regionList[i]['id']+'">'+regionList[i]['name']+'</option>';
                }
               
                $("#region").trigger('change');

                $('#region').html(html);
                loadSchool();
       
            }
            else {
                $("#regionList").hide();
                $("#school").val('');
                $("#schoolList").hide();
                show_load('fail',data.msg);
            }
        }
    };

    $.ajax(options);
}


function loadSchool()
{


    var region_id = $('#region').val();
    var url = '/index.php/home/Promot/getSchool';
    show_load('loading','加载中');
    var options = {
        url: url,
        type: 'post',
        dataType: 'json',
        data:{region_id:region_id},
        success: function (data) {
        	
            hide_load();
            if (data.code == 1) {
        
                var html = '';
                var school_list = data.data;
                for(var i=0;i<school_list.length;i++) {
                    html += '<option value="'+school_list[i]['id']+'">'+school_list[i]['name']+'</option>';
                }
                $("#schoolList").show();
                $("#school").trigger('change');
                $('#school').html(html);

            } else {
                show_load('fail','没有找到学校');

               $("#schoolList").hide();
               return;                
            }
        }
    };
    $.ajax(options);
}

function biLog(data){
    $init = {
        "source": "wechat.luobojianzhi.com"
    };
    $.ajax({
        type: 'POST',
        url:  '/index.php/Home/Tools/biLog',
        data: { biData : $.extend($init, data)}
    });
}