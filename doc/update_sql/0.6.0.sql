ALTER TABLE `sys_dict` DROP COLUMN `editable`;
ALTER TABLE `sys_dict` ADD COLUMN `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态 1：启用 0：禁用' AFTER `title`;
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `icon`, `permission`, `path`, `target_type`, `uri`, `sort`, `keep_alive`, `hidden`, `type`, `remarks`, `deleted`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (100505, 100500, '字典状态变更', NULL, 'system:dict:updateStatus', NULL, 1, '', 4, 0, 0, 2, NULL, 0, 1, NULL, '2022-01-05 20:41:08', NULL);
