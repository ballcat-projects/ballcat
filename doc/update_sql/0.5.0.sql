INSERT INTO `sys_dict` (`code`, `title`, `remarks`, `editable`, `value_type`, `hash_code`, `deleted`, `create_time`, `update_time`)
VALUES ('user_status', '用户状态', NULL, 0, 1, '9527', 0, NOW(), NOW());

INSERT INTO `sys_dict_item` (`dict_code`, `value`, `name`, `attributes`, `sort`, `remarks`, `deleted`, `create_time`, `update_time`)
VALUES ('user_status', '0', '锁定', '{
    \"tagColor\": \"#d9d9d9\",
    \"languages\": {
        \"en-US\": \"Locked\",
        \"zh-CN\": \"锁定\"
    },
    \"textColor\": \"#d9d9d9\",
    \"badgeStatus\": \"default\"
}', 2, NULL, 0, NOW(), NULL)
     , ('user_status', '1', '正常', '{
    \"tagColor\": \"blue\",
    \"languages\": {
        \"en-US\": \"Normal\",
        \"zh-CN\": \"正常\"
    },
    \"textColor\": \"#5b8ff9\",
    \"badgeStatus\": \"processing\"
}', 1, NULL, 0, NOW(), NULL);


-- 业务表追加审计字段：创建人、修改人、创建时间、更新时间、逻辑删除标识
ALTER TABLE `sys_user`
    MODIFY COLUMN `deleted` bigint NULL DEFAULT 0 COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间' AFTER `organization_id`,
    ADD COLUMN `create_by` int NULL COMMENT '创建人' AFTER `deleted`,
    ADD COLUMN `update_by` int NULL COMMENT '修改人' AFTER `create_by`,
    MODIFY COLUMN `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' AFTER `update_by`,
    MODIFY COLUMN `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间' AFTER `create_time`;

ALTER TABLE `sys_role`
    MODIFY COLUMN `deleted` bigint(20) NULL DEFAULT 0 COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间' AFTER `note`,
    ADD COLUMN `create_by` int NULL COMMENT '创建人' AFTER `deleted`,
    ADD COLUMN `update_by` int NULL COMMENT '修改人' AFTER `create_by`,
    MODIFY COLUMN `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' AFTER `update_by`,
    MODIFY COLUMN `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间' AFTER `create_time`;

ALTER TABLE `sys_menu`
    MODIFY COLUMN `deleted` bigint(20) NULL DEFAULT 0 COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间' AFTER `remarks`,
    ADD COLUMN `create_by` int NULL COMMENT '创建人' AFTER `deleted`,
    ADD COLUMN `update_by` int NULL COMMENT '修改人' AFTER `create_by`,
    MODIFY COLUMN `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' AFTER `update_by`,
    MODIFY COLUMN `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间' AFTER `create_time`;

ALTER TABLE `sys_organization`
    ADD COLUMN `deleted` bigint NULL DEFAULT 0 COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间' AFTER `sort`,
MODIFY COLUMN `create_by` int NULL DEFAULT NULL COMMENT '创建者' AFTER `deleted`,
MODIFY COLUMN `update_by` int NULL DEFAULT NULL COMMENT '修改者' AFTER `create_by`,
MODIFY COLUMN `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' AFTER `update_by`,
MODIFY COLUMN `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间' AFTER `create_time`;

ALTER TABLE `sys_dict`
    MODIFY COLUMN `deleted` bigint(20) NULL DEFAULT 0 COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间' AFTER `hash_code`,
    ADD COLUMN `create_by` int NULL COMMENT '创建人' AFTER `deleted`,
    ADD COLUMN `update_by` int NULL COMMENT '修改人' AFTER `create_by`,
    MODIFY COLUMN `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' AFTER `update_by`,
    MODIFY COLUMN `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间' AFTER `create_time`;

ALTER TABLE `sys_dict_item`
    MODIFY COLUMN `deleted` bigint(20) NULL DEFAULT 0 COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间' AFTER `remarks`,
    ADD COLUMN `create_by` int NULL COMMENT '创建人' AFTER `deleted`,
    ADD COLUMN `update_by` int NULL COMMENT '修改人' AFTER `create_by`,
    MODIFY COLUMN `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' AFTER `update_by`,
    MODIFY COLUMN `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间' AFTER `create_time`;

ALTER TABLE `sys_config`
    MODIFY COLUMN `deleted` bigint(20) NULL DEFAULT 0 COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间' AFTER `description`,
    ADD COLUMN `create_by` int NULL COMMENT '创建人' AFTER `deleted`,
    ADD COLUMN `update_by` int NULL COMMENT '修改人' AFTER `create_by`,
    MODIFY COLUMN `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' AFTER `update_by`,
    MODIFY COLUMN `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间' AFTER `create_time`;

ALTER TABLE `notify_announcement`
    ADD COLUMN `deleted` bigint NULL DEFAULT 0 COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间' AFTER `deadline`,
MODIFY COLUMN `create_by` int NULL COMMENT '创建人' AFTER `deleted`,
ADD COLUMN `update_by` int NULL COMMENT '修改人' AFTER `create_by`,
MODIFY COLUMN `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' AFTER `update_by`,
MODIFY COLUMN `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间' AFTER `create_time`;


-- 统一备注属性的名称为 remarks
ALTER TABLE `sys_role`
    CHANGE COLUMN `note` `remarks` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注' AFTER `scope_resources`;

ALTER TABLE `sys_menu`
    MODIFY COLUMN `remarks` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注信息' AFTER `type`;

ALTER TABLE `sys_organization`
    CHANGE COLUMN `description` `remarks` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注' AFTER `sort`;

ALTER TABLE `sys_dict`
    MODIFY COLUMN `remarks` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注' AFTER `hash_code`;

ALTER TABLE `sys_dict_item`
    MODIFY COLUMN `remarks` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注' AFTER `sort`;

ALTER TABLE `sys_config`
    CHANGE COLUMN `description` `remarks` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注' AFTER `category`;


-- 国际化部分
ALTER TABLE `i18n_data`
    ADD COLUMN `deleted` bigint NULL DEFAULT 0 COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间' AFTER `remark`,
ADD COLUMN `create_by` int NULL COMMENT '创建人' AFTER `deleted`,
ADD COLUMN `update_by` int NULL COMMENT '修改人' AFTER `create_by`,
MODIFY COLUMN `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' AFTER `update_by`,
MODIFY COLUMN `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间' AFTER `create_time`;

ALTER TABLE `i18n_data`
DROP INDEX `udx_laguage_tag_code`,
ADD UNIQUE INDEX `udx_laguage_tag_code`(`language_tag`, `code`, `deleted`) USING BTREE;

ALTER TABLE `i18n_data`
    CHANGE COLUMN `remark` `remarks` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注' AFTER `message`;