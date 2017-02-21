/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50617
Source Host           : localhost:3306
Source Database       : server_logingate

Target Server Type    : MYSQL
Target Server Version : 50617
File Encoding         : 65001

Date: 2017-02-21 16:42:11
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
-- Table structure for npc
-- ----------------------------
DROP TABLE IF EXISTS `npc`;
CREATE TABLE `npc` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cityName` varchar(255) DEFAULT '',
  `tileName` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT '',
  `name` varchar(255) DEFAULT '',
  `cname` varchar(255) DEFAULT '',
  `long` varchar(2048) DEFAULT '',
  `gender` varchar(255) DEFAULT NULL,
  `attitude` varchar(255) DEFAULT NULL,
  `shenType` tinyint(4) DEFAULT '0',
  `per` int(18) DEFAULT '0',
  `age` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of npc
-- ----------------------------
INSERT INTO `npc` VALUES ('1', 'register', 'yanluodian', '南嫖', 'yang wei', '杨威', '他容貌猥琐，不可一世。哇！他可是性爱导师。', '男性', 'peaceful', '-1', '20', '25');
INSERT INTO `npc` VALUES ('2', 'register', 'yanluodian', '偏属', '偏属', '偏属', '\r\n007请选择你的属性偏属，然后进行洗点！\r\n008猛士型：高臂力偏属。:pianshu msx$zj#智慧型：高悟性偏属。:pianshu zhx$zj#耐力型：高根骨偏属。:pianshu nlx$zj#敏捷型：高身法偏属。:pianshu mjx$zj#均衡型：各项平均20属性。:pianshu jhx\r\n\r\n', null, null, '0', '0', '0');
INSERT INTO `npc` VALUES ('3', 'register', 'yanluodian', '转生', '转生', '转生', '\r\n007投胎乃人生大事，切记不可草率！$br#请选择你的转生地点：\r\n008 1.扬州人氏:born 扬州人氏\\$zj# 2.齐鲁人氏:born 齐鲁人氏\\$zj# 3.福建人氏:born 福建人氏\\$zj# 4.荆州人氏:born 荆州人氏\\$zj# 5.巴蜀人氏:born 巴蜀人氏\\$zj# 6.两广人氏:born 两广人氏\\$zj# 7. 关外人氏:born 关外人氏\\$zj# 8. 中原人氏:born 中原人氏\\$zj# 9. 秦晋人氏:born 秦晋人氏\\$zj#10. 云南人氏:born 云南人氏\\$zj#11. 燕赵人氏:born 燕赵人氏\\$zj#12. 杭州人氏:born 杭州人氏\\$zj#13. 苏州人氏:born 苏州人氏\\$zj#14. 西域人氏:born 西域人氏\\$zj#15. 慕容世家:born 慕容世家\\$zj#16. 段氏皇族:born 段氏皇族\\$zj#17. 关外胡家:born 关外胡家\\$zj#18. 欧阳世家:born 欧阳世家\\$zj#\r\n\r\n', null, null, '0', '0', '0');
INSERT INTO `npc` VALUES ('4', 'register', 'yanluodian', '洗点', '洗点', '洗点', '\r\n007投胎之前可以先洗一下属性点，直到自己满意：\r\n008偏属随机洗点:wash$zj#指定分配洗点:washto\r\n', null, null, '0', '0', '0');

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
  `tileName` varchar(255) DEFAULT '' COMMENT '瓦片名称',
  `cityName` varchar(255) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of player_info
-- ----------------------------
INSERT INTO `player_info` VALUES ('24', '1', '127.0.0.1', '2017-02-19 18:29:12', 'yanluodian', 'register');

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
  `outname` varchar(255) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of tile
-- ----------------------------
INSERT INTO `tile` VALUES ('1', 'shengmingzhigu', '生命之谷', 'register', '混沌初分盘古开天地，迷雾、微风，天地初开，四周混沌，雾茫茫一片，这里就是子天丑地人寅出的生命之谷。谷中有一个石头砌成的池子，其中水清澈却不见底，不知道它究竟有多深，池子正上方悬挂着太极两仪四象，不知有何奥妙？！', 'guangmingleiluo', 'yinxianjiaozha', 'jiaojieduobian', 'xinhenshoula', '', '', '', '', '');
INSERT INTO `tile` VALUES ('2', 'guangmingleiluo', '光明磊落', 'register', '这里是一间小竹屋，朴素异常，屋中立着一位气宇轩昂的大汉，正满怀笑意的看着你', '', 'shengmingzhigu', '', '', '', '', '', '', 'yanluodian');
INSERT INTO `tile` VALUES ('3', 'yinxianjiaozha', '阴险狡诈', 'register', '这里是一间小石屋，看不出有什么特别，屋中立着一位正气凛然的大汉，正盯着你看。', 'shengmingzhigu', '', '', '', '', '', '', '', 'yanluodian');
INSERT INTO `tile` VALUES ('4', 'jiaojieduobian', '狡黠多变', 'register', '这里是一间小瓦房，布置的非常舒适，一小鬼舒舒服服的靠在椅子上，轻摇羽扇，有如关羽之长，宛若诸葛之亮。正笑吟吟的看着你进来。', '', '', '', 'shengmingzhigu', '', '', '', '', 'yanluodian');
INSERT INTO `tile` VALUES ('5', 'xinhenshoula', '心狠手辣', 'register', '这里是一间小茅屋，屋内一位大汉坦胸露乳，正大碗酒、大块肉落肚，看见你进来，只是略略一点头。', '', '', 'shengmingzhigu', '', '', '', '', '', 'yanluodian');
INSERT INTO `tile` VALUES ('6', 'yanluodian', '阎罗殿', 'register', '这里阴深恐怖，让人感觉到一阵阵的寒意，两旁列着牛头马面，冥府狱卒，各个威严肃穆。十殿阎罗，尽皆在此，地藏王坐在大堂之上，不怒自威，你的腿脚不禁都有些软了。大殿角落有一个石头砌成的池子，其中水清澈却不见底，不知道它究竟有多深。旁边有一个牌子(paizi)，你也许应该仔细看看。', '', '', '', '', '', '', '', '', '');

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
