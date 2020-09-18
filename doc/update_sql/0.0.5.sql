-- 添加字典属性字段
ALTER TABLE `ballcat`.`sys_dict_item`
ADD COLUMN `attributes` json NULL COMMENT '附加属性' AFTER `name`;

INSERT INTO `ballcat`.`sys_dict`(`code`, `title`, `remarks`, `editable`, `value_type`, `hash_code`, `deleted`, `create_time`, `update_time`) VALUES ('login_event_type', '登陆事件类型', '1：登陆  2：登出', 0, 1, '6fe465274208421eb0619a516875e270', 0, '2020-09-17 14:44:00', NULL);
INSERT INTO `ballcat`.`sys_dict_item`(`dict_code`, `value`, `name`, `attributes`, `sort`, `remarks`, `deleted`, `create_time`, `update_time`) VALUES ('login_event_type', '1', '登陆', '{\"tagColor\": \"cyan\"}', 0, '', 0, '2020-03-27 01:05:52', '2019-03-25 12:49:18');
INSERT INTO `ballcat`.`sys_dict_item`(`dict_code`, `value`, `name`, `attributes`, `sort`, `remarks`, `deleted`, `create_time`, `update_time`) VALUES ('login_event_type', '2', '登出', '{\"tagColor\": \"pink\"}', 1, '', 0, '2020-03-27 01:05:52', '2019-03-25 12:49:13');

-- 更新已有字典项的属性
UPDATE `ballcat`.`sys_dict_item` SET `attributes` = '{"tagColor": "orange"}' WHERE `dict_code` = "dict_property" and `value` =  0;
UPDATE `ballcat`.`sys_dict_item` SET `attributes` = '{"tagColor": "green"}' WHERE `dict_code` = "dict_property" and `value` =  1;

UPDATE `ballcat`.`sys_dict_item` SET `attributes` = '{"textColor": "#34890A"}' WHERE `dict_code` = "log_status" and `value` =  1;
UPDATE `ballcat`.`sys_dict_item` SET `attributes` = '{"textColor": "red"}' WHERE `dict_code` = "log_status" and `value` =  0;

UPDATE `ballcat`.`sys_dict_item` SET `attributes` = '{"tagColor": "cyan"}' WHERE `dict_code` = "login_event_type" and `value` =  1;
UPDATE `ballcat`.`sys_dict_item` SET `attributes` = '{"tagColor": "pink"}' WHERE `dict_code` = "login_event_type" and `value` =  2;

UPDATE `ballcat`.`sys_dict_item` SET `attributes` = '{"tagColor": "purple"}' WHERE `dict_code` = "operation_type" and `value` =  3;
UPDATE `ballcat`.`sys_dict_item` SET `attributes` = '{"tagColor": "cyan"}' WHERE `dict_code` = "operation_type" and `value` =  4;
UPDATE `ballcat`.`sys_dict_item` SET `attributes` = '{"tagColor": "orange"}' WHERE `dict_code` = "operation_type" and `value` =  5;
UPDATE `ballcat`.`sys_dict_item` SET `attributes` = '{"tagColor": "pink"}' WHERE `dict_code` = "operation_type" and `value` =  6;

UPDATE `ballcat`.`sys_dict_item` SET `attributes` = '{"tagColor": "orange"}' WHERE `dict_code` = "role_type" and `value` =  1;
UPDATE `ballcat`.`sys_dict_item` SET `attributes` = '{"tagColor": "green"}' WHERE `dict_code` = "role_type" and `value` =  2;

-- 删除弃用的字典项
delete from `ballcat`.`sys_dict_item` WHERE `dict_code` = "operation_type" and ( `value` =  1 or `value` = 2 );

-- 登陆日志表
CREATE TABLE `admin_login_log` (
  `id` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `trace_id` char(24) DEFAULT NULL COMMENT '追踪ID',
  `username` varchar(64) DEFAULT NULL COMMENT '用户名',
  `ip` varchar(64) DEFAULT NULL COMMENT '登陆IP',
  `os` varchar(50) DEFAULT NULL COMMENT '操作系统',
  `status` tinyint(1) NOT NULL COMMENT '状态',
  `event_type` tinyint(1) DEFAULT NULL COMMENT '事件类型，1：登录 2：登出',
  `msg` varchar(255) DEFAULT NULL COMMENT '操作信息',
  `location` varchar(50) DEFAULT NULL COMMENT '登陆地点',
  `browser` varchar(50) DEFAULT NULL COMMENT '浏览器',
  `login_time` datetime DEFAULT NULL COMMENT '登录/登出时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `username` (`username`) USING BTREE,
  KEY `status` (`status`) USING BTREE,
  KEY `create_time` (`create_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='登陆日志';

-- 登录日志权限数据
INSERT INTO `ballcat`.`sys_permission`(`id`, `title`, `code`, `path`, `router_name`, `component`, `redirect`, `target`, `parent_id`, `icon`, `sort`, `keep_alive`, `hidden`, `type`, `deleted`, `create_time`, `update_time`) VALUES (110200, '登陆日志', NULL, '/log/adminloginlog', 'adminLoginLog', 'log/adminloginlog/AdminLoginLogPage', NULL, NULL, 110000, NULL, 1, 0, 0, 1, 0, NULL, '2019-10-13 22:00:24');
INSERT INTO `ballcat`.`sys_permission`(`id`, `title`, `code`, `path`, `router_name`, `component`, `redirect`, `target`, `parent_id`, `icon`, `sort`, `keep_alive`, `hidden`, `type`, `deleted`, `create_time`, `update_time`) VALUES (110201, '登陆日志查询', 'log:adminloginlog:read', NULL, NULL, NULL, NULL, NULL, 110200, NULL, 0, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);

-- lov 模块
DROP TABLE IF EXISTS `sys_lov`;
CREATE TABLE `sys_lov` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `keyword` varchar(50) DEFAULT NULL COMMENT '关键字，唯一，加载lov数据时通过关键字加载',
  `url` varchar(255) DEFAULT NULL COMMENT '获取数据时请求路径',
  `method` varchar(10) DEFAULT NULL COMMENT 'http请求方式',
  `position` varchar(10) DEFAULT NULL COMMENT 'http请求参数设置位置',
  `key` varchar(20) DEFAULT NULL COMMENT '数据的key',
  `fixed_params` varchar(255) DEFAULT '{}' COMMENT '固定请求参数，请设置 jsonString, 默认值 {}',
  `multiple` bit(1) DEFAULT NULL COMMENT '是否需要多选',
  `search` bit(1) DEFAULT NULL COMMENT '是否需要搜索框',
  `ret` bit(1) DEFAULT NULL COMMENT '是否需要返回数据, false则不会有确定按钮',
  `ret_field` varchar(50) DEFAULT NULL COMMENT '返回数据的字段',
  `ret_field_data_type` tinyint(1) DEFAULT NULL COMMENT '返回字段数据类型 1 String 2 Number',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `keyword` (`keyword`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='lov';

DROP TABLE IF EXISTS `sys_lov_body`;
CREATE TABLE `sys_lov_body` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `lov_id` int(11) DEFAULT NULL,
  `title` varchar(100) DEFAULT NULL COMMENT '标题',
  `field` varchar(50) DEFAULT NULL COMMENT '字段, 同一lov下，field不可重复`',
  `index` int(255) DEFAULT NULL COMMENT '索引，字段排序',
  `property` varchar(255) DEFAULT '{}' COMMENT '自定义属性，请设置 jsonString, 默认值 {}',
  `custom` bit(1) DEFAULT NULL COMMENT '是否自定义html',
  `html` text COMMENT '如果 custom=true 则当前值不能为空',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `lov_id` (`lov_id`,`field`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='lov body';

DROP TABLE IF EXISTS `sys_lov_search`;
CREATE TABLE `sys_lov_search` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `lov_id` int(11) DEFAULT NULL,
  `label` varchar(100) DEFAULT NULL COMMENT '标签文字',
  `field` varchar(50) DEFAULT NULL COMMENT '字段',
  `placeholder` varchar(255) DEFAULT NULL COMMENT 'placeholder',
  `tag` varchar(50) DEFAULT NULL COMMENT 'html 标签',
  `options` text COMMENT 'tag=SELECT时的选项',
  `min` int(1) DEFAULT NULL COMMENT 'tag=INPUT_NUMBER时的选项，设置数字最小值',
  `max` int(1) DEFAULT NULL COMMENT 'tag=INPUT_NUMBER时的选项，设置数字最大值',
  `dict_code` varchar(50) DEFAULT NULL COMMENT 'tag=DICT_SELECT时的选项，设置dict-code',
  `custom` bit(1) DEFAULT NULL COMMENT '是否自定义html',
  `html` text COMMENT '如果 custom=true 则当前值不能为空',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `lov_id` (`lov_id`,`field`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='lov search';

INSERT INTO `sys_permission` VALUES (100600, 'lov', NULL, '/sys/lov', 'Lov', 'sys/lov/Lov', NULL, NULL, 100000, NULL, 6, 0, 0, 1, 0, NULL, '2020-08-27 21:36:18');
INSERT INTO `sys_permission` VALUES (100601, 'lov查询', 'sys:lov:read', NULL, NULL, NULL, NULL, NULL, 100600, NULL, 0, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100602, 'lov新增', 'sys:lov:add', NULL, NULL, NULL, NULL, NULL, 100600, NULL, 1, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100603, 'lov修改', 'sys:lov:edit', NULL, NULL, NULL, NULL, NULL, 100600, NULL, 2, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100604, 'lov删除', 'sys:lov:del', NULL, NULL, NULL, NULL, NULL, 100600, NULL, 3, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);