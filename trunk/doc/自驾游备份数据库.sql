/*
Navicat MySQL Data Transfer

Source Server         : 自驾游
Source Server Version : 50173
Source Host           : bdm25683027.my3w.com:3306
Source Database       : bdm25683027_db

Target Server Type    : MYSQL
Target Server Version : 50173
File Encoding         : 65001

Date: 2017-02-28 08:48:16
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for action_map
-- ----------------------------
DROP TABLE IF EXISTS `action_map`;
CREATE TABLE `action_map` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `action` varchar(255) NOT NULL,
  `provinceId` int(11) DEFAULT NULL,
  `cityId` int(11) DEFAULT NULL,
  `countyId` int(11) DEFAULT NULL,
  `scenicAreaId` int(11) DEFAULT NULL,
  `createTime` timestamp NULL DEFAULT NULL,
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of action_map
-- ----------------------------
INSERT INTO `action_map` VALUES ('1', '/index.php/Home/Index/shandong', '1', '0', '0', '0', '2016-08-29 16:37:52', '2016-11-29 10:34:27');

-- ----------------------------
-- Table structure for city
-- ----------------------------
DROP TABLE IF EXISTS `city`;
CREATE TABLE `city` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `provinceId` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `createTime` timestamp NULL DEFAULT NULL,
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of city
-- ----------------------------
INSERT INTO `city` VALUES ('1', '1', '威海市', '2016-08-29 14:45:17', '2016-08-29 14:45:17');

-- ----------------------------
-- Table structure for county
-- ----------------------------
DROP TABLE IF EXISTS `county`;
CREATE TABLE `county` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `cityId` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `createTime` timestamp NULL DEFAULT NULL,
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of county
-- ----------------------------
INSERT INTO `county` VALUES ('1', '1', '市区', '2016-08-29 14:46:03', '2016-11-29 10:10:52');
INSERT INTO `county` VALUES ('2', '1', '乳山', '2016-08-29 14:45:39', '2016-08-29 14:45:39');
INSERT INTO `county` VALUES ('3', '1', '荣成', '2016-08-29 14:45:54', '2016-08-29 14:45:54');
INSERT INTO `county` VALUES ('4', '1', '文登', '2016-08-29 14:45:32', '2016-11-29 10:10:55');

-- ----------------------------
-- Table structure for player
-- ----------------------------
DROP TABLE IF EXISTS `player`;
CREATE TABLE `player` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT '',
  `phone` varchar(255) DEFAULT '',
  `mail` varchar(255) DEFAULT NULL,
  `createTime` timestamp NULL DEFAULT NULL,
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player
-- ----------------------------
INSERT INTO `player` VALUES ('1', 'root', 'e10adc3949ba59abbe56e057f20f883e', '', null, null, '2017-02-08 21:47:19');

-- ----------------------------
-- Table structure for province
-- ----------------------------
DROP TABLE IF EXISTS `province`;
CREATE TABLE `province` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '省的名字',
  `pinyin` varchar(255) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `createTime` timestamp NULL DEFAULT NULL,
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of province
-- ----------------------------
INSERT INTO `province` VALUES ('1', '山东省', 'shandong', '/Uploads/ProvinceIcon/shandong.jpeg', '1', '2016-08-29 14:45:02', '2017-01-04 16:35:11');

-- ----------------------------
-- Table structure for scenic_area
-- ----------------------------
DROP TABLE IF EXISTS `scenic_area`;
CREATE TABLE `scenic_area` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `countyId` int(11) NOT NULL,
  `createTime` timestamp NULL DEFAULT NULL,
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of scenic_area
-- ----------------------------
INSERT INTO `scenic_area` VALUES ('1', '刘公岛', '4', '2016-08-29 14:46:53', '2016-08-29 14:47:58');

-- ----------------------------
-- Table structure for user_dist
-- ----------------------------
DROP TABLE IF EXISTS `user_dist`;
CREATE TABLE `user_dist` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `provinceId` int(11) DEFAULT NULL,
  `cityId` int(11) DEFAULT NULL,
  `countyId` int(11) DEFAULT NULL,
  `scenicAreaId` int(11) DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  `createTime` timestamp NULL DEFAULT NULL,
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_dist
-- ----------------------------
INSERT INTO `user_dist` VALUES ('1', '1', '0', '0', '0', '2', '2016-08-29 16:38:23', '2016-09-22 16:57:48');

-- ----------------------------
-- Table structure for user_record
-- ----------------------------
DROP TABLE IF EXISTS `user_record`;
CREATE TABLE `user_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip` varchar(255) DEFAULT NULL,
  `userId` int(11) DEFAULT '0' COMMENT '用户id',
  `action` varchar(255) DEFAULT NULL,
  `line` tinyint(4) DEFAULT '0' COMMENT '用户在线状态 0:离线 1:在线',
  `createTime` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_record
-- ----------------------------
INSERT INTO `user_record` VALUES ('1', '127.0.0.1', '0', '/index.php/Home/Index/index', '1', '2016-11-29 14:40:29', '2016-11-29 14:48:37');
INSERT INTO `user_record` VALUES ('2', '60.217.72.151', '0', '/index.php/Home/Gp/provinces', '1', null, '2017-01-04 11:50:03');
INSERT INTO `user_record` VALUES ('3', '117.136.95.153', '0', '/index.php/Home/Index/shandong', '1', null, '2016-12-12 09:41:53');
INSERT INTO `user_record` VALUES ('4', '101.254.166.140', '0', '/index.php/Home/Index/shandong', '1', null, '2016-12-12 09:53:05');
INSERT INTO `user_record` VALUES ('5', '218.59.172.24', '0', '/index.php/Home/Gp/provinces', '1', null, '2016-12-13 17:17:40');
INSERT INTO `user_record` VALUES ('6', '58.59.82.38', '0', '/index.php/Home/Gp/provinces', '1', null, '2017-01-04 12:33:02');
