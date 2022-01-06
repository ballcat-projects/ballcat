ALTER TABLE `sys_dict` DROP COLUMN `editable`;
ALTER TABLE `sys_dict` ADD COLUMN `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态 1：启用 0：禁用' AFTER `title`;