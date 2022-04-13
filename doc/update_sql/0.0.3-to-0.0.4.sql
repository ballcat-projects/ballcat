update sys_dict set `code` = 'log_status' where `code` = 'log_type';
update sys_dict_item set `dict_code` = 'log_status' where `dict_code` = 'log_type';

ALTER TABLE `ballcat`.`admin_access_log`
ADD COLUMN `result` text NULL COMMENT '响应信息' AFTER `http_status`;

ALTER TABLE sys_dict ADD `value_type` TINYINT ( 1 ) NULL DEFAULT 1 COMMENT '值类型,1:Number 2:String 3:Boolean' AFTER `editable`;
INSERT INTO `sys_dict`(`code`, `title`, `remarks`, `editable`, `hash_code`, `deleted`, `create_time`, `update_time`, `value_type`) VALUES ('dict_value_type', '字典数据类型', NULL, 1, '582ed0dc179d4c99929b6dc5b63847fb', 0, now(), NULL, 1);

INSERT INTO `sys_dict_item`(`dict_code`, `value`, `name`, `sort`, `remarks`, `deleted`, `create_time`, `update_time`) VALUES ('dict_value_type', '1', 'Number', 1, NULL, 0, '2020-08-12 16:10:22', '2020-08-12 16:12:33');
INSERT INTO `sys_dict_item`(`dict_code`, `value`, `name`, `sort`, `remarks`, `deleted`, `create_time`, `update_time`) VALUES ('dict_value_type', '2', 'String', 1, NULL, 0, '2020-08-12 16:10:31', '2020-08-12 16:12:27');
INSERT INTO `sys_dict_item`(`dict_code`, `value`, `name`, `sort`, `remarks`, `deleted`, `create_time`, `update_time`) VALUES ('dict_value_type', '3', 'Boolean', 1, NULL, 0, '2020-08-12 16:10:38', '2020-08-12 16:12:23');