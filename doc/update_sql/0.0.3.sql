-- 逻辑删除字段修改为bigint
ALTER TABLE `ballcat`.`sys_user`
MODIFY COLUMN `deleted` bigint(20) NULL DEFAULT NULL COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间' AFTER `type`;
ALTER TABLE `ballcat`.`sys_role`
MODIFY COLUMN `deleted` bigint(20) NULL DEFAULT NULL COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间' AFTER `note`;
ALTER TABLE `ballcat`.`sys_permission`
MODIFY COLUMN `deleted` bigint(20) NULL DEFAULT NULL COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间' AFTER `type`;
ALTER TABLE `ballcat`.`sys_dict`
MODIFY COLUMN `deleted` bigint(20) NULL DEFAULT NULL COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间' AFTER `hash_code`;
ALTER TABLE `ballcat`.`sys_dict_item`
MODIFY COLUMN `deleted` bigint(20) NULL DEFAULT NULL COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间' AFTER `remarks`;
ALTER TABLE `ballcat`.`sys_config`
ADD COLUMN `deleted` bigint(20) NULL COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间' AFTER `description`;

-- 系统角色新增类型字段
ALTER TABLE `ballcat`.`sys_role`
ADD COLUMN `type` tinyint(1) NULL DEFAULT 2 COMMENT '角色类型，1：系统角色 2：业务角色' AFTER `code`;