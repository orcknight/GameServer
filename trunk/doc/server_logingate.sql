/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50617
Source Host           : localhost:3306
Source Database       : server_logingate

Target Server Type    : MYSQL
Target Server Version : 50617
File Encoding         : 65001

Date: 2017-02-19 23:55:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for city
-- ----------------------------
DROP TABLE IF EXISTS `city`;
CREATE TABLE `city` (
  `int` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT '' COMMENT '城市命令名',
  `cname` varchar(255) DEFAULT '' COMMENT '中文名字',
  PRIMARY KEY (`int`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of city
-- ----------------------------
INSERT INTO `city` VALUES ('1', 'born', '出生地');

-- ----------------------------
-- Table structure for player
-- ----------------------------
DROP TABLE IF EXISTS `player`;
CREATE TABLE `player` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) DEFAULT '0' COMMENT '用户id',
  `name` varchar(255) DEFAULT '' COMMENT '玩家名字',
  `sex` varchar(32) DEFAULT '' COMMENT '性别',
  `character` varchar(255) DEFAULT '' COMMENT '性格',
  `level` int(11) DEFAULT '1' COMMENT '等级',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of player
-- ----------------------------
INSERT INTO `player` VALUES ('24', '4', '杨威\n', '男性', '', '1');

-- ----------------------------
-- Table structure for player_info
-- ----------------------------
DROP TABLE IF EXISTS `player_info`;
CREATE TABLE `player_info` (
  `playerId` int(11) DEFAULT NULL,
  `isLine` tinyint(4) DEFAULT '1' COMMENT '是否在线',
  `ip` varchar(255) DEFAULT '' COMMENT '上次登陆的ip',
  `loginTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上次登陆的时间',
  `tileName` varchar(255) DEFAULT '' COMMENT '瓦片名称'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of player_info
-- ----------------------------
INSERT INTO `player_info` VALUES ('24', '1', '127.0.0.1', '2017-02-19 18:29:12', 'shengmingzhigu');

-- ----------------------------
-- Table structure for tile
-- ----------------------------
DROP TABLE IF EXISTS `tile`;
CREATE TABLE `tile` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT '' COMMENT '(英文名)命令名',
  `cname` varchar(255) DEFAULT '' COMMENT '中文名',
  `pname` varchar(255) DEFAULT '' COMMENT '父亲名，城市名',
  `describe` varchar(2048) DEFAULT '' COMMENT '描述',
  `ename` varchar(255) DEFAULT '' COMMENT '东方瓦片名字',
  `wname` varchar(255) DEFAULT '' COMMENT '西部瓦片名字',
  `sname` varchar(255) DEFAULT '' COMMENT '南部瓦片名字',
  `nname` varchar(255) DEFAULT '' COMMENT '北部瓦片名字',
  `wnname` varchar(255) DEFAULT '' COMMENT '西北瓦片',
  `enname` varchar(255) DEFAULT '' COMMENT '东北瓦片',
  `wsname` varchar(255) DEFAULT '' COMMENT '西南瓦片',
  `esname` varchar(255) DEFAULT '' COMMENT '东南瓦片',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of tile
-- ----------------------------
INSERT INTO `tile` VALUES ('1', 'shengmingzhigu', '生命之谷', 'register', '混沌初分盘古开天地，迷雾、微风，天地初开，四周混沌，雾茫茫一片，这里就是子天丑地人寅出的生命之谷。谷中有一个石头砌成的池子，其中水清澈却不见底，不知道它究竟有多深，池子正上方悬挂着太极两仪四象，不知有何奥妙？！', 'guangmingleiluo', 'yinxianjiaozha', 'jiaojieduobian', 'xinhenshoula', '', '', '', '');
INSERT INTO `tile` VALUES ('2', 'guangmingleiluo', '光明磊落', 'register', '这里是一间小竹屋，朴素异常，屋中立着一位气宇轩昂的大汉，正满怀笑意的看着你', '', 'shengmingzhigu', '', '', '', '', '', '');
INSERT INTO `tile` VALUES ('3', 'yinxianjiaozha', '阴险狡诈', 'register', '', 'shengmingzhigu', '', '', '', '', '', '', '');
INSERT INTO `tile` VALUES ('4', 'jiaojieduobian', '狡黠多变', 'register', '', '', '', '', 'shengmingzhigu', '', '', '', '');
INSERT INTO `tile` VALUES ('5', 'xinhenshoula', '心狠手辣', 'register', '', '', '', 'shengmingzhigu', '', '', '', '', '');

-- ----------------------------
-- Table structure for tile_content
-- ----------------------------
DROP TABLE IF EXISTS `tile_content`;
CREATE TABLE `tile_content` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of tile_content
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT '' COMMENT '用户名',
  `password` varchar(255) DEFAULT '' COMMENT '密码',
  `phone` varchar(255) DEFAULT '' COMMENT '手机',
  `email` varchar(255) DEFAULT '' COMMENT '邮箱',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('4', 'orcknight', 'ec878b54e20a467b7f1c8e46f14d21c4', '15562135585', '407496032@qq.com', '2017-02-16 23:15:56', '2017-02-16 23:15:56');
