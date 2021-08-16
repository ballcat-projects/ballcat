-- 添加字典项唯一索引
ALTER TABLE `sys_dict_item`
    ADD UNIQUE INDEX `uqx_value_dict_code`(`value`, `dict_code`, `deleted`);

-- 更新字典项【是否】的 dict_code，从 tf 到 yes_or_no
update sys_dict set `code`='yes_or_no' where `code` = 'tf';
update sys_dict_item  set `dict_code`='yes_or_no' where dict_code = 'tf';
-- 新增字典项：操作类型，导入、导出、其他
INSERT INTO `sys_dict_item` (`dict_code`, `value`, `name`, `attributes`, `sort`, `remarks`, `deleted`, `create_time`, `update_time`) VALUES ('operation_type', '0', '其他', '{\"tagColor\": \"\", \"languages\": {\"en-US\": \"Other\", \"zh-CN\": \"其他\"}, \"textColor\": \"\"}', 0, NULL, 0, '2021-08-16 16:59:28', '2021-08-16 17:00:03');
INSERT INTO `sys_dict_item` (`dict_code`, `value`, `name`, `attributes`, `sort`, `remarks`, `deleted`, `create_time`, `update_time`) VALUES ('operation_type', '1', '导入', '{\"tagColor\": \"green\", \"languages\": {\"en-US\": \"Import\", \"zh-CN\": \"导入\"}, \"textColor\": \"\"}', 1, NULL, 0, '2021-08-16 16:59:52', '2021-08-16 17:14:38');
INSERT INTO `sys_dict_item` (`dict_code`, `value`, `name`, `attributes`, `sort`, `remarks`, `deleted`, `create_time`, `update_time`) VALUES ('operation_type', '2', '导出', '{\"tagColor\": \"blue\", \"languages\": {\"en-US\": \"Export\", \"zh-CN\": \"导出\"}, \"textColor\": \"\"}', 2, NULL, 0, '2021-08-16 17:02:07', '2021-08-16 17:14:42');


-- 填充字典项的 attributes 属性
update sys_dict_item  set `attributes`= '{}' where `attributes` is NULL;
-- 更新字典项的标签颜色
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.tagColor", "orange")  where dict_code =  'dict_property'	  and value =  0;
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.tagColor", "green")   where dict_code =  'dict_property'	  and value =  1;
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.tagColor", "cyan")    where dict_code =  'login_event_type' and value =  1;
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.tagColor", "pink")    where dict_code =  'login_event_type' and value =  2;
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.tagColor", "purple")  where dict_code =  'operation_type'	  and value =  3;
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.tagColor", "cyan")    where dict_code =  'operation_type'	  and value =  4;
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.tagColor", "orange")  where dict_code =  'operation_type'	  and value =  5;
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.tagColor", "pink")    where dict_code =  'operation_type'	  and value =  6;
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.tagColor", "orange")  where dict_code =  'role_type'        and value =  1;
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.tagColor", "green")   where dict_code =  'role_type'        and value =  2;
-- 更新字典项的文本颜色
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.textColor", "#34890A")   where dict_code =  'log_status'	  and value =  1;
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.textColor", "#FF0000")    where dict_code =  'log_status'	  and value =  0;



-- 提高 title 的容量，防止国际化 code 不够存储
ALTER TABLE `ballcat`.`sys_menu`
    MODIFY COLUMN `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单名称' AFTER `parent_id`;