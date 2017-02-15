
# 创建bi_log_v2
# ------------------------------------------------------------

DROP TABLE IF EXISTS `bi_log_v2`;

CREATE TABLE `bi_log_v2` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `behavior` varchar(512) DEFAULT '' COMMENT '行为名称',
  `source` varchar(512) DEFAULT NULL COMMENT '行为来源',
  `action` varchar(512) DEFAULT NULL COMMENT '行为请求',
  `data` varchar(2048) DEFAULT NULL COMMENT '行为JSON数据',
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

