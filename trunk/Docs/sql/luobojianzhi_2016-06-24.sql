# ************************************************************
# Sequel Pro SQL dump
# Version 4499
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: rm-m5ezm1323798xbhnyo.mysql.rds.aliyuncs.com (MySQL 5.6.16-log)
# Database: test_luobojianzhi
# Generation Time: 2016-06-24 08:14:31 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table admin_auth_resource
# ------------------------------------------------------------

DROP TABLE IF EXISTS `admin_auth_resource`;

CREATE TABLE `admin_auth_resource` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(11) DEFAULT NULL COMMENT '资源名称',
  `url` varchar(256) DEFAULT NULL COMMENT '资源路径',
  `status` tinyint(4) DEFAULT '1' COMMENT '0:停用 1:启用',
  `weight` tinyint(4) DEFAULT '0' COMMENT '资源权重',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;



# Dump of table admin_auth_role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `admin_auth_role`;

CREATE TABLE `admin_auth_role` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(11) DEFAULT NULL COMMENT '角色名称',
  `status` tinyint(4) DEFAULT '1' COMMENT '0:停用 1:启用',
  `weight` tinyint(4) DEFAULT '0' COMMENT '角色权重排序',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;



# Dump of table admin_auth_role_resource_rel
# ------------------------------------------------------------

DROP TABLE IF EXISTS `admin_auth_role_resource_rel`;

CREATE TABLE `admin_auth_role_resource_rel` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `roleId` int(11) DEFAULT NULL COMMENT '角色Id',
  `resourceId` int(11) DEFAULT NULL COMMENT '资源Id',
  `status` tinyint(4) DEFAULT '1' COMMENT '0:停用 1:启用',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;



# Dump of table admin_auth_user_role_rel
# ------------------------------------------------------------

DROP TABLE IF EXISTS `admin_auth_user_role_rel`;

CREATE TABLE `admin_auth_user_role_rel` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `userId` int(11) DEFAULT NULL COMMENT '用户Id',
  `roleId` int(11) DEFAULT NULL COMMENT '角色Id',
  `status` tinyint(4) DEFAULT '1' COMMENT '0:停用 1:启用',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;



# Dump of table admin_user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `admin_user`;

CREATE TABLE `admin_user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(128) CHARACTER SET utf8 DEFAULT NULL,
  `password` varchar(128) CHARACTER SET utf8 DEFAULT NULL,
  `provinceId` int(11) DEFAULT NULL COMMENT '省份Id',
  `cityId` int(11) DEFAULT NULL COMMENT '城市Id',
  `state` tinyint(4) DEFAULT '1',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='管理员用户表';



# Dump of table apply_tcjz_relation
# ------------------------------------------------------------

DROP TABLE IF EXISTS `apply_tcjz_relation`;

CREATE TABLE `apply_tcjz_relation` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `job_id` int(11) DEFAULT NULL,
  `x_job_id` varchar(300) NOT NULL COMMENT '第三方平台提供的唯一标识',
  `createTime` timestamp NULL DEFAULT NULL,
  `updateTime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table bi_log
# ------------------------------------------------------------

DROP TABLE IF EXISTS `bi_log`;

CREATE TABLE `bi_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `behavior` varchar(512) DEFAULT '',
  `source` varchar(512) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  `categoryId` int(11) DEFAULT NULL,
  `jobId` int(11) DEFAULT NULL,
  `extend` varchar(512) DEFAULT NULL,
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;



# Dump of table company
# ------------------------------------------------------------

DROP TABLE IF EXISTS `company`;

CREATE TABLE `company` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `phone` varchar(32) DEFAULT '' COMMENT '注册手机号',
  `username` varchar(128) DEFAULT '' COMMENT '联系人',
  `realname` varchar(128) DEFAULT '' COMMENT '真实姓名',
  `password` varchar(128) DEFAULT '' COMMENT '密码',
  `companyName` varchar(128) DEFAULT '' COMMENT '公司名称',
  `logo` varchar(512) DEFAULT '' COMMENT '企业logo',
  `localLogo` varchar(512) DEFAULT NULL COMMENT '本地企业logo',
  `email` varchar(128) DEFAULT '' COMMENT '公司邮件地址',
  `address` varchar(256) DEFAULT '' COMMENT '详细地址',
  `licenceCode` varchar(128) DEFAULT '' COMMENT '营业执照编号',
  `licenceImage` varchar(512) DEFAULT '' COMMENT 'CDN营业执照路径',
  `localLicenceImage` varchar(512) DEFAULT '' COMMENT '本地营业执照路径',
  `idcardImage` varchar(512) DEFAULT '' COMMENT 'CDN身份证路径',
  `localIdcardImage` varchar(512) DEFAULT NULL COMMENT '本地身份证路径',
  `intro` varchar(1024) DEFAULT '' COMMENT '企业简介',
  `status` tinyint(4) DEFAULT '0' COMMENT '0:注册未审核 1:待审核 2:审核通过 3:审核未通过',
  `openid` varchar(128) DEFAULT '' COMMENT '微信单应用唯一标识',
  `unionid` varchar(128) DEFAULT '' COMMENT '微信跨应用唯一标识',
  `provinceId` int(11) DEFAULT '1' COMMENT '省',
  `cityId` int(11) DEFAULT '1' COMMENT '市',
  `regionId` int(11) DEFAULT '1' COMMENT '区',
  `reviewTime` timestamp NULL DEFAULT NULL COMMENT '审核实现',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;



# Dump of table gp_city
# ------------------------------------------------------------

DROP TABLE IF EXISTS `gp_city`;

CREATE TABLE `gp_city` (
  `id` int(5) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL COMMENT '城市名字',
  `pid` int(11) DEFAULT NULL COMMENT '省id',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态 0:关闭 1:开启',
  `weight` int(11) DEFAULT '0' COMMENT '权重',
  `latitude` double DEFAULT NULL COMMENT '经度',
  `longitude` double DEFAULT NULL COMMENT '纬度',
  `code` varchar(8) DEFAULT NULL,
  `creatTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table gp_province
# ------------------------------------------------------------

DROP TABLE IF EXISTS `gp_province`;

CREATE TABLE `gp_province` (
  `id` int(5) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(96) CHARACTER SET utf8 DEFAULT NULL COMMENT '省名字',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态 0:关闭 1:开启',
  `weight` int(11) DEFAULT '0' COMMENT '权重',
  `code` varchar(8) DEFAULT NULL,
  `creatTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table gp_region
# ------------------------------------------------------------

DROP TABLE IF EXISTS `gp_region`;

CREATE TABLE `gp_region` (
  `id` int(5) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL COMMENT '区域名字',
  `cId` int(11) DEFAULT NULL COMMENT '城市ID',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态 0:关闭 1:开启',
  `weight` int(11) DEFAULT '0' COMMENT '权重',
  `code` varchar(8) DEFAULT NULL,
  `creatTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table gp_school
# ------------------------------------------------------------

DROP TABLE IF EXISTS `gp_school`;

CREATE TABLE `gp_school` (
  `id` int(5) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL COMMENT '学校名字',
  `pId` int(11) DEFAULT NULL COMMENT '省ID',
  `cId` int(11) DEFAULT NULL COMMENT '城市ID',
  `rid` int(11) DEFAULT NULL COMMENT '区id',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态 0:关闭 1:开启',
  `weight` int(11) DEFAULT '0' COMMENT '权重',
  `creatTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table job
# ------------------------------------------------------------

DROP TABLE IF EXISTS `job`;

CREATE TABLE `job` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `companyId` int(11) DEFAULT NULL COMMENT '职位发布者Id',
  `title` varchar(256) DEFAULT NULL COMMENT '职位名称',
  `type` tinyint(4) DEFAULT '1' COMMENT '1:普工作 2:红包 3:双薪',
  `status` tinyint(4) DEFAULT '0' COMMENT '职位状态 0:未审核 1:审核通过 2:审核未通过 3:结束招聘 4:招聘过期 5:取消发布',
  `company` varchar(128) DEFAULT NULL COMMENT '公司名称',
  `address` varchar(512) DEFAULT NULL COMMENT '公司地址',
  `linkman` varchar(64) DEFAULT NULL COMMENT '联系人',
  `phone` varchar(32) DEFAULT NULL COMMENT '职位发布人电话',
  `beginTime` timestamp NULL DEFAULT NULL COMMENT '职位开始时间',
  `endTime` timestamp NULL DEFAULT NULL COMMENT '职位结束时间',
  `number` int(8) DEFAULT NULL COMMENT '职位人数',
  `income` varchar(64) DEFAULT NULL COMMENT '职位报酬',
  `incomeUnit` tinyint(4) DEFAULT NULL COMMENT '报酬单位 1:元/小时  2:元/天 3:元/星期 4:元/月',
  `payType` tinyint(4) DEFAULT NULL COMMENT '支付周期 1:天结 2:周结 3:月结 4:次结',
  `intro` varchar(2048) DEFAULT NULL COMMENT '职位描述',
  `hot` int(11) DEFAULT '0' COMMENT '职位浏览次数',
  `pCateId` int(11) DEFAULT NULL COMMENT '职位一级分类ID',
  `cateId` int(11) DEFAULT NULL COMMENT '职位二级分类ID',
  `category` varchar(128) DEFAULT NULL COMMENT '职位分类名称(冗余)',
  `provinceId` int(11) DEFAULT NULL COMMENT '省份ID',
  `province` varchar(64) DEFAULT NULL COMMENT '职位省域',
  `cityId` int(11) DEFAULT NULL COMMENT '城市ID',
  `city` varchar(64) DEFAULT NULL COMMENT '职位市域',
  `regionId` int(11) DEFAULT NULL COMMENT '城市下面区域ID',
  `region` varchar(128) DEFAULT NULL COMMENT '职位区域',
  `weight` int(11) DEFAULT '0' COMMENT '职位权重',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `source` varchar(128) DEFAULT NULL COMMENT '职位来源',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table job_apply
# ------------------------------------------------------------

DROP TABLE IF EXISTS `job_apply`;

CREATE TABLE `job_apply` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `userId` int(11) DEFAULT NULL COMMENT '申请者Id',
  `jobId` int(11) DEFAULT NULL COMMENT '职位Id',
  `companyId` int(11) DEFAULT NULL COMMENT '职位公司Id(冗余)',
  `cStatus` tinyint(4) DEFAULT '1' COMMENT '企业端状态 1:待通过[待通过] 2:已通过[待完成] 3:已拒绝 4:已完成[已完成] 5:未完成',
  `uStatus` tinyint(4) DEFAULT '1' COMMENT '用户端状态 1:申请中[待录用] 2:已取消 3:待完成[待完成] 4:已完成[已完成] 5:未通过 6:未完成',
  `source` varchar(128) DEFAULT NULL COMMENT '渠道来源，同job表此字段',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table job_category
# ------------------------------------------------------------

DROP TABLE IF EXISTS `job_category`;

CREATE TABLE `job_category` (
  `id` int(5) unsigned NOT NULL AUTO_INCREMENT,
  `pId` int(11) DEFAULT '0' COMMENT '父分类ID',
  `name` varchar(128) DEFAULT NULL COMMENT '分类名称',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态 0:关闭 1:开启',
  `weight` int(11) DEFAULT '0' COMMENT '权重',
  `creatTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table job_collect
# ------------------------------------------------------------

DROP TABLE IF EXISTS `job_collect`;

CREATE TABLE `job_collect` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `uId` int(11) DEFAULT NULL COMMENT '用户ID',
  `jobId` int(11) DEFAULT NULL COMMENT '工作ID',
  `status` tinyint(4) DEFAULT '0' COMMENT '职位收藏状态 0：未收藏 1：已收藏',
  `createtime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table job_tcjz_relation
# ------------------------------------------------------------

DROP TABLE IF EXISTS `job_tcjz_relation`;

CREATE TABLE `job_tcjz_relation` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `job_id` int(11) NOT NULL,
  `x_job_id` varchar(300) NOT NULL COMMENT '第三方平台提供的唯一标识',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table job_timespan
# ------------------------------------------------------------

DROP TABLE IF EXISTS `job_timespan`;

CREATE TABLE `job_timespan` (
  `id` int(5) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL COMMENT '时间段名字',
  `status` tinyint(4) DEFAULT '1' COMMENT '0：不可用 1：可用',
  `weight` int(11) DEFAULT '0' COMMENT '权重',
  `creatTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table st_promot
# ------------------------------------------------------------

DROP TABLE IF EXISTS `st_promot`;

CREATE TABLE `st_promot` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `wxid` int(11) NOT NULL DEFAULT '0' COMMENT 'third',
  `realname` varchar(50) NOT NULL DEFAULT '',
  `tel` varchar(16) NOT NULL DEFAULT '',
  `qr_path` varchar(200) NOT NULL DEFAULT '',
  `add_time` varchar(16) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `wallet` double(10,2) NOT NULL DEFAULT '0.00',
  `province` int(11) NOT NULL DEFAULT '0',
  `city` int(11) NOT NULL DEFAULT '0',
  `regionId` int(11) DEFAULT NULL,
  `school` int(11) NOT NULL DEFAULT '0',
  `qq` varchar(20) NOT NULL DEFAULT '',
  `province_name` varchar(20) DEFAULT NULL,
  `city_name` varchar(20) DEFAULT NULL,
  `region_name` varchar(20) DEFAULT NULL,
  `school_name` varchar(20) DEFAULT NULL,
  `from_nickname` varchar(50) NOT NULL DEFAULT '',
  `from_wxid` int(11) NOT NULL DEFAULT '0',
  `number` int(11) NOT NULL DEFAULT '0',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '0：未审核 1：审核通过 2：被拒绝',
  `remark` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;



# Dump of table st_promot_follow
# ------------------------------------------------------------

DROP TABLE IF EXISTS `st_promot_follow`;

CREATE TABLE `st_promot_follow` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `wxid` int(11) NOT NULL DEFAULT '0',
  `promot_id` int(11) NOT NULL DEFAULT '0',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否关注',
  `add_time` varchar(16) NOT NULL DEFAULT '',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `nickname` varchar(50) DEFAULT NULL,
  `headimgurl` varchar(200) DEFAULT '',
  `openid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table sys_advice
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_advice`;

CREATE TABLE `sys_advice` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(128) DEFAULT NULL COMMENT '广告标题',
  `image` varchar(512) DEFAULT NULL COMMENT '图片path',
  `linkURL` varchar(512) DEFAULT NULL COMMENT '外链URL地址',
  `type` varchar(96) DEFAULT NULL COMMENT '0：APP 1：Wechat 2：PC',
  `cId` int(11) DEFAULT '0' COMMENT '城市ID',
  `weight` int(11) DEFAULT '0' COMMENT '权重',
  `status` tinyint(4) DEFAULT '1' COMMENT '0：不可用 1：可用',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sys_banner
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_banner`;

CREATE TABLE `sys_banner` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `image` varchar(512) DEFAULT NULL COMMENT '图片CDN路径',
  `localImage` varchar(512) DEFAULT NULL COMMENT '图片本地路径',
  `linkURL` varchar(1024) DEFAULT NULL COMMENT '图片URL',
  `type` varchar(96) DEFAULT NULL COMMENT '0：APP 1：Wechat 2：PC',
  `cId` int(11) DEFAULT '0' COMMENT '城市ID 0代表通用',
  `weight` int(11) DEFAULT '0' COMMENT '权重',
  `status` tinyint(4) DEFAULT '1' COMMENT '0：不可用 1：可用',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sys_comment
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_comment`;

CREATE TABLE `sys_comment` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `uId` int(11) DEFAULT NULL COMMENT '用户ID',
  `title` varchar(128) DEFAULT NULL COMMENT '反馈标题',
  `content` varchar(512) DEFAULT NULL COMMENT '反馈内容',
  `type` tinyint(4) DEFAULT NULL COMMENT '0：个人用户 1：企业用户',
  `status` tinyint(4) DEFAULT NULL COMMENT '0：禁用 1：正常',
  `createTime` timestamp NULL DEFAULT NULL,
  `updateTime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;



# Dump of table sys_feedback
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_feedback`;

CREATE TABLE `sys_feedback` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `uId` int(11) DEFAULT NULL COMMENT '用户ID',
  `title` varchar(128) DEFAULT NULL COMMENT '反馈标题',
  `content` varchar(512) DEFAULT NULL COMMENT '反馈内容',
  `type` tinyint(4) DEFAULT '0' COMMENT '0：个人用户 1：企业用户',
  `status` tinyint(4) DEFAULT '1' COMMENT '0：禁用 1：正常',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;



# Dump of table user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(32) DEFAULT NULL COMMENT '用户昵称',
  `realname` varchar(32) DEFAULT NULL COMMENT '真实姓名',
  `password` varchar(128) DEFAULT NULL COMMENT 'MD5(密码)',
  `phone` varchar(32) DEFAULT NULL COMMENT '电话号码',
  `status` tinyint(4) DEFAULT '1' COMMENT '用户状态 0:待审核 1:审核通过 2:停用',
  `credit` int(11) DEFAULT NULL COMMENT '用户积分',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `gender` tinyint(11) DEFAULT NULL COMMENT '0女的 1男的',
  `intro` varchar(512) DEFAULT NULL COMMENT '个人简介',
  `schoolId` int(11) DEFAULT NULL COMMENT '学校Id',
  `grade` int(11) DEFAULT NULL COMMENT '年级',
  `degree` varchar(128) DEFAULT NULL COMMENT '最高学历',
  `nation` varchar(128) DEFAULT NULL COMMENT '民族',
  `idCardFrontUrl` varchar(512) DEFAULT NULL COMMENT '身份证照片正面图片URL',
  `idCardBackUrl` varchar(512) DEFAULT NULL COMMENT '身份证照片反面图片URL',
  `countryId` int(11) DEFAULT NULL,
  `provinceId` int(11) DEFAULT NULL,
  `cityId` varchar(32) DEFAULT NULL COMMENT '市',
  `regionId` int(4) DEFAULT NULL COMMENT '区',
  `address` varchar(512) DEFAULT NULL COMMENT '个人详细地址',
  `headImgUrl` varchar(512) DEFAULT NULL COMMENT '个人头像CDN地址',
  `localHeadImgUrl` varchar(512) DEFAULT NULL COMMENT '个人头像本地地址',
  `openid` varchar(128) DEFAULT NULL COMMENT '微信单应用唯一标识',
  `unionid` varchar(128) DEFAULT NULL COMMENT '微信跨应用唯一标识',
  `qq` int(11) DEFAULT NULL COMMENT 'OICQ',
  `email` varchar(128) DEFAULT NULL COMMENT '邮箱地址',
  `source` int(11) DEFAULT NULL COMMENT '1:WX 11:NewWX 2:App 21:NewAPP 3:Web 31:NewWeb ',
  `major` varchar(64) DEFAULT NULL COMMENT '专业名称',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `QRCode` int(11) DEFAULT NULL COMMENT '推广人自己的号码',
  `inviteCode` int(11) DEFAULT NULL COMMENT '学生注册时输入的推广人的号码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table user_third
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user_third`;

CREATE TABLE `user_third` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `wechat_openid` varchar(50) NOT NULL DEFAULT '',
  `wechat_unionid` varchar(50) NOT NULL DEFAULT '',
  `nickname` varchar(20) NOT NULL DEFAULT '',
  `headimgurl` varchar(300) NOT NULL DEFAULT '',
  `access_token` varchar(50) NOT NULL DEFAULT '',
  `add_time` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `sex` int(11) DEFAULT NULL,
  `city` varchar(11) DEFAULT NULL,
  `province` varchar(11) DEFAULT NULL,
  `country` varchar(11) DEFAULT NULL,
  `is_get` int(11) NOT NULL DEFAULT '0',
  `status` int(11) NOT NULL DEFAULT '0',
  `from_promot_id` int(11) NOT NULL DEFAULT '0',
  `subscribe_time` varchar(16) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;



# Dump of table wt_account
# ------------------------------------------------------------

DROP TABLE IF EXISTS `wt_account`;

CREATE TABLE `wt_account` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT NULL,
  `alias` varchar(128) DEFAULT NULL COMMENT '应用别名',
  `appId` varchar(128) DEFAULT NULL,
  `appSecret` varchar(128) DEFAULT '',
  `status` tinyint(11) DEFAULT '1' COMMENT '0：停用 1：启用',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;



# Dump of table wx_access_token
# ------------------------------------------------------------

DROP TABLE IF EXISTS `wx_access_token`;

CREATE TABLE `wx_access_token` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `appId` varchar(128) DEFAULT NULL,
  `accessToken` varchar(512) DEFAULT NULL,
  `status` tinyint(11) DEFAULT NULL COMMENT '状态类型：1:启用 0:关闭',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;



# Dump of table wx_menu
# ------------------------------------------------------------

DROP TABLE IF EXISTS `wx_menu`;

CREATE TABLE `wx_menu` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `appId` varchar(128) DEFAULT NULL,
  `content` varchar(2048) DEFAULT NULL,
  `status` tinyint(11) DEFAULT '1' COMMENT '状态类型：1:启用 0:关闭',
  `intro` varchar(256) DEFAULT NULL COMMENT '账号描述',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
