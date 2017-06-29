<?php if (!defined('THINK_PATH')) exit();?><!DOCTYPE html>
<html>
    <head>
        <title>萝卜兼职</title>
        <meta charset="UTF-8">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<!--禁止自动识别电话号码/邮箱-->
<meta name="format-detection" content="telephone=no,email=no,date=no,address=no">    
<meta name="viewport" content="maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,initial-scale=1.0"/>
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">

<link rel="shortcut icon"  type="image/x-icon" href="/favicon.ico">
<link rel="icon" type="image/png" href="/favicon-32x32.png" sizes="32x32">
<link rel="apple-touch-icon" sizes="152x152" href="/apple-touch-icon-152x152.png">


<link rel="stylesheet" href="/Public/source/debug/styles/sm.css">
<link rel="stylesheet" href="/Public/source/debug/styles/sm-extend.css">
<link rel="stylesheet" href="/Public/source/debug/styles/main.css?t=1484274672352">

<link rel="stylesheet" href="/Public/source/debug/styles/shop.css?t=1484274672336">

<!-- <link rel="stylesheet" href="/Public/source/dist/luobo.min.css"> -->
<script>
    var ROOT ="";
    var APP_PATH = "/index.php";
    var MOD_PATH = "/index.php/Home";
    var CON_PATH = "/index.php/Home/Index";
</script>

<!-- 模板-工作详情 -->
<script id="job-detail-tpl" type="text/html">
    <div class="wrapJobDetail">
        <section class="marg_bottom_10">
            <div class="line_b pos_rela">
                <p class="fc_orange line_h1">{{title}}</p>  
                <p class="f12 fc_lightGray line_h2">
                    <i class="iconf iconf-clock f12"></i><span class="marg_left_10 marg_right_10">{{createtime}}</span>
                    <i class="iconf iconf-eyes f12"></i><span class="marg_left_10">{{hot}}</span>
                </p> 
            </div>
            <div class="f14">
                <p class="row no-gutter line_h2">
                    <span class="col-25 fc_lightGray">发布机构：</span>
                    <span class="col-75 tx_ellips">{{company}}</span>
                </p>
                <p class="row no-gutter line_h1">
                    <span class="col-25 fc_lightGray">招聘人数：</span>
                    <span class="col-75 tx_ellips">{{number}}人</span>
                </p>
                <p class="row no-gutter line_b pos_rela line_h2">
                    <span class="col-25 fc_lightGray">工资待遇：</span>
                    <span class="col-75 tx_ellips">{{income}}{{incomeunit}}</span>
                </p>
                <p class="row no-gutter line_b pos_rela line_h2">
                    <span class="col-25 fc_lightGray">结算方式：</span>
                    <span class="col-75 tx_ellips">{{paytype}}</span>
                </p>
                <p class="row no-gutter line_b pos_rela line_h2">
                    <span class="col-25 fc_lightGray">兼职类型：</span>
                    <span class="col-75 tx_ellips detail_jobType">
                        {{each attributes as value index}}
                            {{if value == 1}}
                                <img src="/Public/images/danbao.png" alt="担保">
                            {{else if value == 2}}
                                <img src="/Public/images/hongbao.png" alt="红包">
                            {{else if value == 3}}
                                <img src="/Public/images/shuangxin.png" alt="双薪">
                            {{/if}}
                        {{/each}}
                        {{if attributes[0] == undefined}}
                            普通
                        {{/if}}
                    </span>
                </p>                 
                <p class="row no-gutter line_b pos_rela line_h22">
                    <span class="col-25 fc_lightGray">工作地点：</span>
                    <span class="col-75">{{address}}</span>
                    <a href="/index.php/Home/Job/map.html?address={{address}}" style="display: none;">
                        <span class="col-33 tx_ellips f12 fc_lightGray" style="margin-top: .15rem;">点击查看地图&nbsp;&nbsp;></span>
                    </a>
                    <div class="wrapMap" id ="allmap"></div>
                </p>
                <p class="fc_orange line_h2">
                    <i class="iconf iconf-notice f14"></i><span class="iconf f12 marg_left_10">凡是收费的兼职信息，请谨慎选择！</span>
                </p>
            </div>
        </section> 
        <section class="marg_bottom_10">
            <div class="f14">
                <p class="row no-gutter pos_rela line_h2 line_b">
                    <span class="col-25 fc_lightGray">工作日期：</span>
                    <span class="col-75 tx_ellips">{{begintime}} ~ {{endtime}}</span>
                </p>
                <p class="row no-gutter line_h2">
                    <span class="col-25 fc_lightGray">上班时段：</span>
                    <span class="col-75 tx_ellips">{{beginhour}} ~ {{endhour}}</span>
                </p>
            </div>
        </section> 
        <section class="marg_bottom_10">
            <div class="line_b pos_rela fc_orange line_h1 padd_bottom_10">
              <i class="iconf iconf-edit"></i><span class="iconf marg_left_10">职位描述</span>
            </div>
            <div class="f12 fc_lightGray marg_top_10 jobIntro">
              {{intro}}
            </div>
        </section> 
        <section class="marg_bottom_10">
            <div class="line_b pos_rela fc_orange line_h1 padd_bottom_10">
              <i class="iconf iconf-edit"></i><span class="iconf marg_left_10">企业信息</span>
            </div>
            <div class="f12 fc_lightGray marg_top_10">
              {{companyinfo}}
            </div>
        </section> 
        <section class="marg_bottom_10">
            <div class="f14">
                <p class="row no-gutter line_b pos_rela line_h2">
                    <span class="col-25 fc_lightGray">报名截止：</span>
                    <span class="col-75 tx_ellips">{{deadline}}</span>
                </p>
                <p class="job-connect row no-gutter line_b pos_rela line_h2">
                    <span class="col-25 fc_lightGray">联系人：</span>
                    <span class="col-75 tx_ellips">{{linkman}}</span>
                </p>
                <p class="job-connect row no-gutter line_h2">
                    <span class="col-25 fc_lightGray">联系电话：</span>
                    <span class="col-75 tx_ellips">{{phone}}</span>
                </p>
            </div>
        </section> 
        <section class="marg_bottom_10">
            <div class="f14">
                <p class="row no-gutter pos_rela line_h2">
                    <span class="col-33 fc_lightGray">已报名人数：</span>
                    <span class="col-66 tx_ellips fc_orange text-right">{{appliedCount}}人</span>
                </p>
            </div>
        </section> 
    </div>
</script> 
<!-- 模板-工作详情 -名企招聘 -->
<script id="job-detail-tpl-02" type="text/html">
    <div class="wrapJobDetail">
        <section class="marg_bottom_10">
            <div class="line_b pos_rela">
                <p class="fc_orange line_h1">{{title}}</p>  
                <p class="f12 fc_lightGray line_h2">
                    <i class="iconf iconf-clock f12"></i><span class="marg_left_10 marg_right_10">{{createtime}}</span>
                    <i class="iconf iconf-eyes f12"></i><span class="marg_left_10">{{hot}}</span>
                </p> 
            </div>
            <div class="f14">
                <p class="row no-gutter line_h2">
                    <span class="col-25 fc_lightGray">发布机构：</span>
                    <span class="col-75 tx_ellips">{{company}}</span>
                </p>
                <p class="row no-gutter line_h1">
                    <span class="col-25 fc_lightGray">招聘人数：</span>
                    <span class="col-75 tx_ellips">{{number}}人</span>
                </p>
                <p class="row no-gutter line_b pos_rela line_h2">
                    <span class="col-25 fc_lightGray">工资待遇：</span>
                    <span class="col-75 tx_ellips">{{income}}{{incomeunit}}</span>
                </p>           
                <p class="row no-gutter line_b pos_rela line_h22">
                    <span class="col-25 fc_lightGray">工作地点：</span>
                    <span class="col-75">{{address}}</span>
                    <a href="/index.php/Home/Job/map.html?address={{address}}" style="display: none;">
                        <span class="col-33 tx_ellips f12 fc_lightGray" style="margin-top: .15rem;">点击查看地图&nbsp;&nbsp;></span>
                    </a>
                    <div class="wrapMap" id ="allmap"></div>
                </p>
                <p class="fc_orange line_h2">
                    <i class="iconf iconf-notice f14"></i><span class="iconf f12 marg_left_10">凡是收费的兼职信息，请谨慎选择！</span>
                </p>
            </div>
        </section> 
        <section class="marg_bottom_10">
            <div class="f14">
                <p class="row no-gutter pos_rela line_h2">
                    <span class="col-25 fc_lightGray">报名时间：</span>
                    <span class="col-75 tx_ellips">{{begintime}} ~ {{endtime}}</span>
                </p>
            </div>
        </section> 
        <section class="marg_bottom_10">
            <div class="line_b pos_rela fc_orange line_h1 padd_bottom_10">
              <i class="iconf iconf-edit"></i><span class="iconf marg_left_10">职位描述</span>
            </div>
            <div class="f12 fc_lightGray marg_top_10 jobIntro">
              {{intro}}
            </div>
        </section> 
        <section class="marg_bottom_10">
            <div class="line_b pos_rela fc_orange line_h1 padd_bottom_10">
              <i class="iconf iconf-edit"></i><span class="iconf marg_left_10">企业信息</span>
            </div>
            {{ if companyinfo == '' || companyinfo == 'undefined'}}
            <div class="f12 fc_lightGray marg_top_10">
                山东盈帆信息科技股份有限公司，成立于2013年12月，是目前山东地区具有活力的互联网公司之一。公司凭借杰出的移动互联网技术和业务实力，服务于威海智慧城市、新北洋集团、上海铁路局等企事业单位，被列入威海市千帆计划科技型企业，并先后受CCTV-1晚间新闻和山东新闻联播报道。“萝卜兼职”是盈帆科技推出的一款基于大数据分析的兼职移动众包平台，通过智能推荐系统为全国大学生提供准确可靠的兼职岗位，同时为企业提供多元化的人力资源解决方案。
            </div>
            {{else}}
                <div class="f12 fc_lightGray marg_top_10">
                    {{companyinfo}}
                </div>
            {{/if}}
        </section> 
        <section class="marg_bottom_10">
            <div class="f14">
                <p class="row no-gutter line_b pos_rela line_h2">
                    <span class="col-25 fc_lightGray"></span>
                    <span class="col-75 tx_ellips">亲：申请后即能查看联系人和联系方式</span>
                </p>
                <p class="job-connect row no-gutter line_b pos_rela line_h2">
                    <span class="col-25 fc_lightGray">联系人：</span>
                    <span class="col-75 tx_ellips">{{linkman}}</span>
                </p>
                <p class="job-connect row no-gutter line_h2">
                    <span class="col-25 fc_lightGray">联系电话：</span>
                    <span class="col-75 tx_ellips">{{phone}}</span>
                </p>
            </div>
        </section> 
    </div>
</script> 
<!-- 模板-工作列表 -->
<script id="job-list-tpl" type="text/html">
    {{each jobs as job i}}
        <li class="jobLi" iIndex="jobLi{{i}}">
            <a href="/index.php/Home/Job/detail/id/{{#job.id}}/superType/{{#job.supertype}}">
                <span class="iType cateId{{#job.pcateid%12}}">{{#job.category}}</span>
                <div class="row no-gutter">
                    <div class="col-80 tx_ellips">
                        {{#job.title}}
                    </div>
                    <div class="col-20 text-center">
                        <div class="bg_status bgGreen">
                            <i class="i_triangle"></i>
                            {{if job.type == 1 }} 报名中 
                            {{else if job.type == 2}} 红包
                            {{else if job.type == 3}} 双薪
                            {{/if}}
                        </div>
                    </div>
                </div>
                <div class="row no-gutter pos_rela">
                    <div class="col-66 fc_lightGray">
                        <p class="f12 pos_rela">
                            <span class="iconf iconf-location"></span>
                            <span class="marg_left_10 tx_ellips pos_ab">{{#job.city}}{{#job.region}}</span>
                        </p>
                        <p class="f12 pos_rela">
                            <span class="jobType">
                                {{each job.attributes as value index}}
                                    {{if value == 1}}
                                        <img src="/Public/images/danbao.png" alt="担保">
                                    {{else if value == 2}}
                                        <img src="/Public/images/hongbao.png" alt="红包">
                                    {{else if value == 3}}
                                        <img src="/Public/images/shuangxin.png" alt="双薪">
                                    {{/if}}
                                {{/each}}
                            </span>
                            <span class="jobType">
                                <img src="/Public/images/paytype_{{job.payenum}}.png" alt="">
                            </span>
                        </p>
                    </div>
                    <div class="col-33 text-right pos_income">
                        <strong class="f16">{{#job.income}}</strong>
                        <small class="fc_lightGray">{{#job.incomeunit}}</small>
                    </div>
                </div>
            </a>
        </li>
    {{/each}}
</script>       

<!-- 模板-SELECT-OPTIONS -->
<script id ="select-option-tpl" type='text/html'>
    {{each option as value index}}
        {{if value.status==1}}
            <option value="{{value.id}}"{{value.id == current?'selected':''}}>{{value.name}}</option>
        {{/if}}
    {{/each}}
</script> 

<script id="scroll-base-tpl" type="text/html">
    <div class="content infinite-scroll infinite-scroll-bottom" data-distance="10">
        <div class="wrapJob">
            <ul class="jobList"></ul>
            <div class="infinite-scroll-preloader">
                <div class="preloader"></div>
            </div>
        </div>
        <footer class="text-center padd_20">
            <p class="fc_orange f16">萝卜兼职网</p>
            <p class="fc_orange f10">www.luobojianzhi.com</p>
            <p class="f10">最靠谱的大学生兼职平台</p>
        </footer>
    </div>
</script>

<script type="text/javascript" src='/Public/source/debug/scripts/common.js' charset='utf-8'></script>
<script id ="school-province-list-tpl" type='text/html'>
    {{each option as value index}}
        {{if value.status==1}}
            <option value="{{value.id}}"{{value.id == current?'selected':''}}>{{value.name}}</option>
        {{/if}}
    {{/each}}
</script> 
<script id ="city-list-tpl" type='text/html'>
    {{each option as value index}}
        {{if value.status==1}}
            <option value="{{value.id}}"{{value.id == current?'selected':''}}>{{value.name}}</option>
        {{/if}}
    {{/each}}
</script> 
<script id ="school-list-tpl" type='text/html'>
    {{each option as value index}}
        {{if value.status==1}}
            <option value="{{value.id}}"{{value.id == current?'selected':''}}>{{value.name}}</option>
        {{/if}}
    {{/each}}
</script> 
<script id ="grade-list-tpl" type='text/html'>
    {{each option as value index}}
        <option value="{{value}}"{{value == current ?'selected':''}}>{{value}}</option>
    {{/each}}
</script>
<script id ="region-list-tpl" type='text/html'>
    {{each option as value index}}
        <option value="{{value.id}}"{{value.id == current ?'selected':''}}>{{value.name}}</option>
    {{/each}}
</script>
<script type="text/html" id="tpl-fullInfo">
    <div class="content-block f14 wrapRegCont list-block">
        <!--baseInfo-->
        <div class="row no-gutter  item-content">
            <div class="item-inner">
                <div class="col-25 text-left item-title pos_rela line_r">
                    姓&emsp;&emsp;名<span class="need-info-star">*</span>
                </div>
                <div class="col-75 item-input">
                    <input id="" class="form-control gttv" type="text" name="realname" value="{{realname}}" placeholder="请填写真实姓名" />
                </div>
            </div>
        </div>
        <div class="row no-gutter  item-content">
            <div class="item-inner">
                <div class="col-25 text-left item-title pos_rela line_r">
                    性&emsp;&emsp;别<span class="need-info-star">*</span>
                </div>
                <div class="col-75 item-input tx_indent">
                    <input id="s_male" class="marg_left_10 marg_right_10" type="radio" name="gender" value="1" {{gender==1?'checked':''}} /><label for="s_male" class="marg_right_10">男</label>
                    <input id="s_female" class="marg_left_10 marg_right_10" type="radio" name="gender" value="0" {{gender==1?'':'checked'}}/><label for="s_female" class="">女</label>
                </div>   
            </div>
        </div>
        <!--schoolInfo-->
        <div class="row no-gutter item-content">
            <div class="item-inner">
                <div class="col-25 text-left item-title pos_rela line_r">
                    城&emsp;&emsp;市<span class="need-info-star">*</span>
                </div>
                <div class="col-75">
                    <div class="col-50 f_left selectProvince pos_rela item-input">
                        <select class="tx_indent" name="provinceId" id="province-list">

                        </select>
                        <span class="icon icon-right pos_arr"></span>
                    </div>
                    <div class="col-50 f_right selectCity pos_rela item-input">
                        <select class="tx_indent" name="cityId" id="city-list">
                          
                        </select>
                        <span class="icon icon-right pos_arr" style="right: -.45rem;"></span>
                    </div>
                </div>
            </div>
        </div>
        <div class="row no-gutter item-content item-link">
            <div class="item-inner">
                <div class="col-25 text-left item-title pos_rela line_r">
                    区&emsp;&emsp;域<span class="need-info-star">*</span>
                </div>
                <div class="col-75 ">
                    <div class="f_left pos_rela item-input">
                        <!-- <span class="fc_lightGray">点击选择区域</span> -->
                        <select class="tx_ellips tx_indent" name="regionId" id="region-list">
                          
                        </select>
                    </div>
                </div>
            </div>
        </div>
        <div class="row no-gutter item-content item-link">
            <div class="item-inner">
                <div class="col-25 text-left item-title pos_rela line_r">
                    学&emsp;&emsp;校<span class="need-info-star">*</span>
                </div>
                <div class="col-75 ">
                    <div class="f_left pos_rela selectSchool item-input">
                        <!-- <span class="fc_lightGray">点击选择学校</span> -->
                        <select class="tx_ellips tx_indent" name="schoolId" id="school-list">
                          
                        </select>
                    </div>
                </div>
            </div>
        </div>
        <div class="row no-gutter selectClass  item-content item-link">
            <div class="item-inner">
                <div class="col-25 text-left pos_rela line_r">
                    年&emsp;&emsp;级<span class="need-info-star">*</span>
                </div>
                <div class="col-75 ">
                    <div class="f_left pos_rela tx_indent  item-input">
                        <select class="tx_indent" name="grade" id="grade-list">
                        </select>
                    </div>
                </div>    
            </div>
        </div>
        <div class="row no-gutter  item-content">
            <div class="item-inner">
                <div class="col-25 text-left item-title pos_rela line_r">
                    专&emsp;&emsp;业<span class="need-info-star">*</span>
                </div>
                <div class="col-75 item-input">
                    <input id="userMajor" class="form-control gttv" type="text" name="major" placeholder="请填写主修专业" />
                </div>
            </div>
        </div>

        <!--moreInfo-->
        <div class="row no-gutter item-content item-link">
            <div class="item-inner">
                <div class="col-25 text-left item-title pos_rela line_r">
                    学&emsp;&emsp;历<span class="need-info-star">*</span>
                </div>
                <div class="col-75">
                    <div class="f_left pos_rela item-input">
                        <select class="tx_indent" name="degree" id="degree-list">
                            {{if !degree}}
                            <option value="">请选择学历</option>
                            {{/if}}
                            <option value="3" {{degree == 3 ? 'selected' : ''}}>研究生</option>
                            <option value="2" {{degree == 2 ? 'selected' : ''}}>本科</option>
                            <option value="1" {{degree == 1 ? 'selected' : ''}}>大专</option>
                            <option value="0" {{degree == 0 ? 'selected' : ''}}>大专以下</option>
                        </select>
                    </div>
                </div>
            </div>
        </div>
        <div class="row no-gutter item-content item-link">
            <div class="item-inner">
                <div class="col-25 text-left item-title pos_rela line_r">
                    期望月薪<span class="need-info-star">*</span>
                </div>
                <div class="col-75 ">
                    <div class="f_left pos_rela item-input">
                        <!-- <span class="fc_lightGray">点击选择区域</span> -->
                        <select name="expectPay" class="tx_ellips tx_indent" id="wage-list">
                            {{if !expectpay}}
                            <option value="">请选择期望薪资</option>
                            {{/if}}
                            <option value="0" {{expectpay == 0 ? 'selected' : ''}}>2000-3500</option>
                            <option value="1" {{expectpay == 1 ? 'selected' : ''}}>3500-5000</option>
                            <option value="2" {{expectpay == 2 ? 'selected' : ''}}>5000-8000</option>
                            <option value="3" {{expectpay == 3 ? 'selected' : ''}}>8000以上</option>
                        </select>
                    </div>
                </div>
            </div>
        </div>
        <div class="row no-gutter item-content">
            <div class="item-inner">
                <div class="col-25 text-left item-title pos_rela line_r">
                    实践|培训<span class="need-info-star">*</span>
                </div>
                <div class="col-75 item-input wrapTextarea">
                    <textarea class="form-control gttv" type="textarea" name="practice" placeholder="从社会实践或培训经历中获得的知识技能" >{{practice}}</textarea>
                </div>
            </div>
        </div>
        <div class="row no-gutter item-content">
            <div class="item-inner">
                <div class="col-25 text-left item-title pos_rela line_r">
                    自我评价<span class="need-info-star">*</span>
                </div>
                <div class="col-75 item-input wrapTextarea">
                    <textarea class="form-control gttv" type="textarea" name="intro" placeholder="不少于50字说明你的优势或特长" >{{intro}}</textarea>
                </div>
            </div>
        </div>
        <div class="row no-gutter item-content">
            <div class="item-inner">
                <div class="col-25 text-left item-title pos_rela line_r">
                    职业规划
                </div>
                <div class="col-75 item-input wrapTextarea">
                    <textarea class="form-control gttv" type="textarea" name="planning" placeholder="假如被录用,未来两年如何规划(清晰的个人规划让你拥有更多机会)">{{planning}}</textarea>
                </div>
            </div>
        </div>
    </div>
</script>

        <script type="text/javascript">
            // 跳转到职位分类页面
            function redirectURL(type) {
                var cId = $(".location-info").data('cid');
                var path = location.pathname;
                console.log(path);
                if(path.indexOf("cId")!=-1) {
                    cId = path.substring(path.lastIndexOf("/cId/")+5, path.length);
                    console.log(cId);
                } 
                var goURL = "/index.php/home/job/categories/cId/"+cId+"/type/"+type;
                //商城入口test
                if(type == 11){
                    var goURL = "/index.php/home/job/shopIndex/cId/"+cId+"/type/"+type;
                }
                console.log("goURL", goURL);
                location.href = goURL;
            }
        </script>
    </head>
    <body>
        <div class="page-group">
                <div class="page page-current" id="index">
                    <header class="bar bar-nav">
                        <a class="button button-link button-nav pull-left" href="/index.php/home/Gp/cities">
                            <span class="iconf iconf-location pos_i"></span><span class="location-info" data-cId='1'>威海</span>
                        </a>
                        <h1 class='title'>萝卜兼职</h1>
                        <a class="button button-link button-nav pull-right external" id='user-center-btn'>
                            <span class="iconf iconf-user02"></span>
                        </a>
                    </header>
                    <div class="content infinite-scroll infinite-scroll-bottom bg_white" id="indexScroll" data-distance="10">

                        <!-- 轮播图 -->
                        <div class="swiper-container" data-space-between="10">
                        </div>

                        <!-- 筛选条件，主标签 -->
                        <div class="tabGroup pos_rela">
                            <div class="row no-gutter line_b">
                                <div class="col-50 text-center pos_rela line_r" id="job_new">
                                    <a class="f_left text-right" style="width: 55%;" onclick="redirectURL(9);return true" external>名企招聘</a>
                                    <span class="i_jobTrainee  f_left marg_left_20"></span>
                                </div>
                                <div class="col-50 text-center pos_rela line_r" id="job_all" style="display:none;">
                                    <a class="f_left text-right" style="width: 55%;" onclick="redirectURL(0);return true" external>最新兼职</a>
                                    <span class="i_jobNew f_left marg_left_20"></span>
                                </div>
                                <div class="col-50 text-center pos_rela" id="job_all">
                                    <a class="f_left text-right" style="width: 55%;" href="/index.php/Home/Task/index" external>线上兼职</a>
                                    <span class="i_jobNew f_left marg_left_20"></span>
                                </div>
                            </div>
                            <div class="row no-gutter">
                                <div class="col-50 text-center pos_rela line_r" id="">
                                    <a class="f_left text-right" style="width: 55%;" onclick="redirectURL(6);return true" external>寒暑假工</a>
                                    <span class="i_jobHoliday f_left marg_left_20"></span>
                                </div>
                                <div class="col-50 text-center" id="job_new" style="display: none;">
                                    <a class="f_left text-right" style="width: 55%;" onclick="redirectURL(4);return true" external>名企实习</a>
                                    <span class="i_jobTrainee  f_left marg_left_20"></span>
                                </div>
                                <div class="col-50 text-center" id="job_all">
                                    <a class="f_left text-right" style="width: 55%;" onclick="redirectURL(2);return true" external>担保兼职</a>
                                    <span class="i_jobGuarantee f_left marg_left_20"></span>
                                </div>
                            </div>
                            <div class="row no-gutter" style="display: none;">
                                <div class="col-50 text-center pos_rela line_r" id="">
                                    <a class="f_left text-right" style="width: 55%;" onclick="redirectURL(11);return true" external>商城入口</a>
                                    <span class="i_jobHoliday f_left marg_left_20"></span>
                                </div>
                            </div>
                        </div>

                        <div class="list-block">
                            <ul class="jobList list-container"></ul>
                            <!-- 加载提示符 -->
                            <div class="infinite-scroll-preloader">
                                <div class="preloader"></div>
                            </div>
                        </div>
                         <!-- 底部版权信息 -->
                        <footer class="text-center padd_20">
                            <p class="fc_orange f16">萝卜兼职网</p>
                            <p class="fc_orange f10">www.luobojianzhi.com</p>
                            <p class="f10">最靠谱的大学生兼职平台</p>
                        </footer>                   
                    </div>
                </div>
                <div class="panel-overlay"></div>
        </div>

        <!-- 模板-轮播图 -->
        <script id="banner-list-tpl" type="text/html">
            <div class="swiper-wrapper">
                {{each banners as banner i}}
                    <div class="swiper-slide">
                        {{if banner.linkurl == '' }} 
                        <!-- 取消轮播图的链接 -->
                        <!-- <a href="javascript:void(0)"> -->
                        <a>
                            <img src="{{#banner.image}}" alt="{{#banner.id}}">
                        </a>
                        {{else}} 
                        <!-- <a href="{{banner.linkurl}}"> -->
                        <a href="{{#banner.linkurl}}" external>
                            <img src="{{#banner.image}}" alt="{{#banner.id}}">
                        </a>
                        {{/if}}
                    </div>
                {{/each}}
            </div>
            <div class="swiper-pagination"></div>
        </script>     
        <script id="province-list-tpl" type="text/html">
            <ul>
                {{each provinces as province i}}
                    <li class="padd-20 pos_rela">
                        <h3 class="pos_rela fc_orange">{{#province.name}}</h3>
                        <ul class="bor_top">
                        {{each province.data as city }}
                            <a href="/index.php/Home/Index/index/cId/{{city.id}}" class="external"><li class="padd-20 pos_rela fc_lightGray">{{#city.name}}</li></a>
                        {{/each}}
                        </ul>
                    </li>
                {{/each}}
            </ul>
        </script>
        <!--
<script type='text/javascript' src='/Public/source/debug/scripts/zepto.js' charset='utf-8'></script>
<script type='text/javascript' src='/Public/source/debug/scripts/sm.js' charset='utf-8'></script>
<script type='text/javascript' src='/Public/source/debug/scripts/sm-extend.js' charset='utf-8'></script>
<script type='text/javascript' src='/Public/source/debug/scripts/webuploader.custom.js' charset='utf-8'></script>
<script type='text/javascript' src='/Public/source/debug/scripts/swiper.js' charset='utf-8'></script>
<script type='text/javascript' src='/Public/source/debug/scripts/template-debug.js' charset='utf-8'></script>
-->
<script type='text/javascript' src='/Public/source/dist/luobo.js' charset='utf-8'></script>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=P5AXQi2Q4I1wGcSGdIYZMPOG"></script>
<script type="text/javascript">
    function server_error() {
        $.toast("(^_^) 同学们太热情，萝卜表示鸭梨很大！");
    }
    function data_warn() {
         $.toast("(^_^)  木有兼职信息啦！", 1000);
    }
    function opt_success() {
         $.toast("(^_^)  木有兼职信息啦！", 1000);
    }
    function biLog(data){
        $init = {
            "source": "wechat.luobojianzhi.com"
        }
        $.ajax({
            type: 'POST',
            url:  MOD_PATH+'/Tools/biLog',
            data: { biData : $.extend($init, data)}
        });
    }
</script>
<script>
    //调整截至日期为工作开始日期的前一天
    template.helper("reducer", function (begintime) {
        var padStartWithZero = function (tar, num, str) {
            var len = tar.length;
            while (len < num) {
                tar = str + tar;
                len = tar.length;
            }
            return tar.slice(tar.length - num);
        };
        var d = new Date(new Date(begintime) - 24 * 60 * 60 * 1000);
        var m = {
            y: d.getFullYear(),
            m: padStartWithZero(String(d.getMonth() + 1), 2, '0'),
            d: padStartWithZero(String(d.getDate()), 2, '0')
        };
        return m.y + "-" + m.m + "-" + m.d;
    });
</script>     
        <script  type='text/javascript' src='/Public/source/debug/scripts/home-job-detail.js?t=1484211825100' charset='utf-8'></script>
        <script>
            $(function () {

                var path = location.pathname;
                var cId  = path.slice(path.lastIndexOf('/')+1);

                $(document).on("pageInit", "#index", function(e, pageId, $page) {

                    console.log("inited page::", "index!");

                    // 默认进首页后,需定位后才能得到cId和name,或进选择城市页面后返回首页也会获得cId和name;
                    if(isNaN(cId) || !cId){
                        if (condition="$cId eq 0"){
                            $.showPreloader('萝卜卫星定位中，请稍后...');
                            getH5Location();
                            cId =1;
                            initDefaultCity(cId);
                        }else{
                            cId = <?php echo ($cId); ?>;
                            initDefaultCity(cId);
                        }
                    } else {
                        $.ajax({
                            type: 'POST',
                            url: '/index.php/Home/Gp/getNameByCId',
                            data: { "cId": cId },
                            dataType: 'json',
                            timeout: 3000,
                            context: $('.location-info'),
                            success: function(data){
                                this.html(data.data.name);
                                initDefaultCity(cId);
                            },
                            error: function(xhr, type){
                                console.log("xhr", xhr);
                            }
                        });
                    };

                    function locationSucc(value) {
                        var latitude = value.coords.latitude;
                        var longitude = value.coords.longitude;
                        console.log("latitude:", latitude);
                        console.log("longitude:", longitude);
                        getCity(latitude, longitude);
                    }

                    function locationFail(value) {
                        console.log(value);
                        $.hidePreloader();
                        $.toast("(^_^) 火星太危险，请选择返回地球城市！", 1500);  
                    }

                    //使用H5定位
                    function getH5Location() {
                        if (navigator.geolocation) {
                            navigator.geolocation.getCurrentPosition(locationSucc, locationFail, {
                                enableHighAccuracy: true, // 指示浏览器获取高精度的位置，默认为false
                                timeout: 5000, // 指定获取地理位置的超时时间，默认不限时，单位为毫秒
                                maximumAge: 1000 // 最长有效期，在重复获取地理位置时，此参数指定多久再次获取位置。
                            });
                        } else {
                            $.hidePreloader();
                            $.toast("火星太危险，请选择返回地球城市！", 1500);  
                        }
                    }
                    // 根据经纬度，获取城市信息
                    function getCity(lati, longi){
                        $.ajax({
                            type: 'POST',
                            url: '/index.php/Home/gp/returnCity',
                            data: {
                                "latitude": lati,
                                "longitude": longi
                            },
                            dataType: 'json',
                            timeout: 3000,
                            context: $('.location-info'),
                            success: function(data){
                                if(data.code !=0){
                                    var city = data.data;
                                    var cName = city.name;
                                    this.html(cName);
                                    cId = city.id;

                                    $.hidePreloader();

                                    $goURL = CON_PATH+"/index/cId/"+cId;   
                                    cName = "大" + cName.replace("市", "");
                                    $.confirm('确定前往？', '您所在 "' + cName + '"',
                                        function () {
                                            //if(cId != 1) {
                                                location.href = $goURL;
                                            //}
                                        },
                                        function () {
                                        }
                                    ); 
                                }else {
                                    $.toast("火星太危险，请选择返回地球城市！", 1500);  
                                    $.hidePreloader();
                                }
                            },
                            error: function(xhr, type){
                                server_error();
                            }
                        });
                    }
                
                    // 跳转用户中心
                    $('#user-center-btn').on('click', function (e){
                        $.ajax({
                            type: 'POST',
                            url:  '/index.php/Home/User/isLogin',
                            dataType: 'json',
                            timeout: 3000,
                            success:function(res){
                                if(res.code==1){
                                    location.href ='/index.php/Home/User/userCenter/cId/'+cId;
                                }else{
                                    $.toast("(^_^) 请先登录", 500);   
                                    setTimeout(function(){
                                        location.href='/index.php/Home/Wechat/signIn/cId/'+cId;
                                    }, 500);
                                }
                            },
                            error:function(){
                                server_error();
                            }
                        });
                    });

                     
                    function initDefaultCity (cId){
                        $.ajax({
                            type: 'GET',
                            url: '/index.php/Home/sys/bannerList',
                            data: { "cId": cId},
                            dataType: 'json',
                            timeout: 3000,
                            context: $('.swiper-container'),
                            success: function(data){
                                if(data.code) {
                                    var data = {
                                        "banners": data.data
                                    };
                                    var html = template('banner-list-tpl', data);
                                    this.html(html);
                                    var bannerLgt = data.banners.length;
                                    $(".swiper-container").swiper({
                                        pagination: (bannerLgt<= 1)?'':'.swiper-pagination',
                                        paginationClickable: true,
                                        spaceBetween: 0,
                                        centeredSlides: true,
                                        loop: (bannerLgt<= 1)?false:true,
                                        autoplay: 3000,
                                        autoplayDisableOnInteraction: false
                                    });
                                } else {
                                    $('.swiper-container').hide();
                                }
                            },
                            error: function(xhr, type){
                                $('.swiper-container').hide();
                                console.log("xhr", xhr);
                            }
                        });
                        
                        addItems(cId, true);
                    };
                                 
                });


                var pageSize = 10;
                var currPage = 1;
                var loading = false;
                var maxItems = 100;
                var hasLoaded = false;

                var type = 8; // 0:时间 1:热度 8：权重，时间
                var subType = {"regionId":0, "pCateId":0, "timeId":0};

                var currDate = new Date().getTime();

                function addItems(cId, notScroll) {
                    
                    if (!notScroll || (notScroll && !hasLoaded)) {
                        
                        $.ajax({
                            type: 'POST',
                            url: '/index.php/Home/Job/jobs?t=' + currDate,
                            cache: false,
                            data: { "cId": cId, 
                                "currPage": currPage, 
                                "pageSize": pageSize, 
                                "type": type,
                                "subType": subType,
                                 statuses: 1
                            },
                            dataType: 'json',
                            timeout: 3000,
                            context: $('.jobList'),
                            success: function(data){
                                console.log("data", data);
                                if(data.code) {
                                    maxItems = data.data.total;
                                    var data = { "jobs": data.data.data };

                                    //电话咨询时“元/月”不显示
                                    for(var i = 0;i<data.jobs.length;i++){
                                        var num = /^[0-9,\.,\-,\+,\~]*$/;
                                        var income = data.jobs[i].income;
                                        var bool = num.test(income);
                                        if(!bool){
                                           data.jobs[i].incomeunit = '';
                                        }
                                    }
                                    console.log(data);
                                    var html = template('job-list-tpl', data);
                                    this.append(html);

                                    if (notScroll && !hasLoaded) {
                                        hasLoaded = true;
                                    }

                                    // 每次追加完DOM，就隐藏掉loading
                                    $('.infinite-scroll-preloader').hide();
                                    // 重置加载flag
                                    loading = false;
                                    // 页数+1
                                    currPage++;
                                } else {
                                    $.toast("(^_^) 暂无匹配的兼职信息！", 1000);
                                    $.detachInfiniteScroll($('.infinite-scroll'));
                                    $('.infinite-scroll-preloader').hide();
                                }
                            },
                            error: function(xhr, type){
                                console.log("xhr", xhr);
                                server_error();
                                $.detachInfiniteScroll($('.infinite-scroll'));
                                $('.infinite-scroll-preloader').hide();
                            }
                        });
                    } else {
                        if ($('.jobList li').length === 0) {
                            currPage = 1;
                            hasLoaded = false;
                            addItems(cId || 1, true);
                        }
                    }
                }   
                // 注册'infinite'事件处理函数
                $(document).on('infinite', '.infinite-scroll-bottom', function() {
                    $('.infinite-scroll-preloader').show();
                    // 如果正在加载，则退出
                    if (loading) return;
                    // 设置flag
                    loading = true;
                    // 延迟100s的加载过程
                    setTimeout(function() {
                        if (maxItems <= (currPage-1) * pageSize) {
                            // 加载完毕，则注销无限加载事件，以防不必要的加载
                            $.detachInfiniteScroll($('.infinite-scroll'));
                            // $('.infinite-scroll-preloader').html("<span class='f14'>(^_^) 木有兼职信息啦！</span>");
                            $.toast("(^_^) 木有兼职信息啦！", 1000);
                            $('.infinite-scroll-preloader').hide();
                            return;
                        }
                        // 添加新条目
                        addItems(cId);
                        //容器发生改变,如果是js滚动，需要刷新滚动
                        $.refreshScroller();
                    }, 500);
                });      

                $(document).on("pageInit", "#chooseProvince", function(e, pageId, $page) {
                    $.ajax({
                        type: 'POST',
                        url: APP_PATH+'/Home/Gp/cityList',

                        data: { "pId": '1' },
                        dataType: 'json',
                        timeout: 3000,
                        context: $('#wrapTabCity'),

                        success: function(data){
                            if(data.code) {
                                var data = {
                                    provinces: data.data
                                };
                                var html = template('province-list-tpl', data);
                                this.html(html);
                            } else {
                                this.append("非常抱歉，暂无城市数据！");
                            }
                        },
                        error: function(xhr, type){
                            this.append("非常抱歉，服务器开小差了！");
                        }
                    });
                });
                $.init();
            });
        </script>
    </body>
</html>