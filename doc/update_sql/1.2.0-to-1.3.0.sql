ALTER TABLE `sys_user`
    CHANGE COLUMN `sex` `gender` tinyint(1) NULL DEFAULT 0 COMMENT '性别(0-默认未知,1-男,2-女)' AFTER `avatar`,
    CHANGE COLUMN `phone` `phone_number` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号' AFTER `email`,
    MODIFY COLUMN `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id' FIRST,
    MODIFY COLUMN `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录账号' AFTER `user_id`,
    MODIFY COLUMN `nickname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '显示名称' AFTER `username`,
    MODIFY COLUMN `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码' AFTER `nickname`,
    MODIFY COLUMN `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态(1-正常,0-冻结)' AFTER `phone_number`,
    MODIFY COLUMN `type` tinyint(1) NOT NULL DEFAULT 1 COMMENT '账户类型' AFTER `status`,
    MODIFY COLUMN `deleted` bigint(20) NOT NULL DEFAULT 0 COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间' AFTER `organization_id`,
    MODIFY COLUMN `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建人' AFTER `deleted`,
    MODIFY COLUMN `update_by` bigint(20) NULL DEFAULT NULL COMMENT '修改人' AFTER `create_by`,
    MODIFY COLUMN `update_time` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间' AFTER `create_time`;


ALTER TABLE `sys_user_role`
    MODIFY COLUMN `user_id` bigint(20) NOT NULL COMMENT '用户ID' AFTER `id`;

ALTER TABLE `sys_role_menu`
    MODIFY COLUMN `menu_id` bigint(20) NOT NULL COMMENT '菜单ID' AFTER `role_code`;


ALTER TABLE `sys_role`
    MODIFY COLUMN `id` bigint(20) NOT NULL AUTO_INCREMENT FIRST,
    MODIFY COLUMN `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL AFTER `id`,
    MODIFY COLUMN `code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL AFTER `name`,
    MODIFY COLUMN `type` tinyint(1) NOT NULL DEFAULT 2 COMMENT '角色类型，1：系统角色 2：业务角色' AFTER `code`,
    MODIFY COLUMN `deleted` bigint(20) NOT NULL DEFAULT 0 COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间' AFTER `remarks`,
    MODIFY COLUMN `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建人' AFTER `deleted`,
    MODIFY COLUMN `update_by` bigint(20) NULL DEFAULT NULL COMMENT '修改人' AFTER `create_by`;


ALTER TABLE `sys_organization`
    MODIFY COLUMN `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID' FIRST,
    MODIFY COLUMN `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '组织名称' AFTER `id`,
    MODIFY COLUMN `parent_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '父级ID' AFTER `name`,
    MODIFY COLUMN `hierarchy` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '层级信息，从根节点到当前节点的最短路径，使用-分割节点ID' AFTER `parent_id`,
    MODIFY COLUMN `depth` int(1) NOT NULL COMMENT '当前节点深度' AFTER `hierarchy`,
    MODIFY COLUMN `sort` int(1) NOT NULL DEFAULT 1 COMMENT '排序字段，由小到大' AFTER `depth`,
    MODIFY COLUMN `deleted` bigint(20) NOT NULL DEFAULT 0 COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间' AFTER `remarks`,
    MODIFY COLUMN `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建者' AFTER `deleted`,
    MODIFY COLUMN `update_by` bigint(20) NULL DEFAULT NULL COMMENT '修改者' AFTER `create_by`;


ALTER TABLE `sys_menu`
    MODIFY COLUMN `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID' FIRST,
    MODIFY COLUMN `parent_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '父级ID' AFTER `id`,
    MODIFY COLUMN `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称' AFTER `parent_id`,
    MODIFY COLUMN `type` tinyint(1) NOT NULL DEFAULT 0 COMMENT '菜单类型 （0目录，1菜单，2按钮）' AFTER `hidden`,
    MODIFY COLUMN `deleted` bigint(20) NOT NULL DEFAULT 0 COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间' AFTER `remarks`,
    MODIFY COLUMN `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建人' AFTER `deleted`,
    MODIFY COLUMN `update_by` bigint(20) NULL DEFAULT NULL COMMENT '修改人' AFTER `create_by`;

ALTER TABLE `sys_dict_item`
    MODIFY COLUMN `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID' FIRST,
    MODIFY COLUMN `deleted` bigint(20) NOT NULL DEFAULT 0 COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间' AFTER `remarks`,
    MODIFY COLUMN `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建人' AFTER `deleted`,
    MODIFY COLUMN `update_by` bigint(20) NULL DEFAULT NULL COMMENT '修改人' AFTER `create_by`;


ALTER TABLE `sys_dict`
    MODIFY COLUMN `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号' FIRST,
    MODIFY COLUMN `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标识' AFTER `id`,
    MODIFY COLUMN `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称' AFTER `code`,
    MODIFY COLUMN `value_type` tinyint(1) NOT NULL DEFAULT 0 COMMENT '值类型,1:Number 2:String 3:Boolean' AFTER `title`,
    MODIFY COLUMN `deleted` bigint(20) NOT NULL DEFAULT 0 COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间' AFTER `remarks`,
    MODIFY COLUMN `create_by` bigint(11) NULL DEFAULT NULL COMMENT '创建人' AFTER `deleted`,
    MODIFY COLUMN `update_by` bigint(11) NULL DEFAULT NULL COMMENT '修改人' AFTER `create_by`;


ALTER TABLE `sys_config`
    MODIFY COLUMN `id` bigint(20) NOT NULL AUTO_INCREMENT FIRST,
    MODIFY COLUMN `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '配置名称' AFTER `id`,
    MODIFY COLUMN `conf_key` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '配置键' AFTER `name`,
    MODIFY COLUMN `conf_value` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '配置值' AFTER `conf_key`,
    MODIFY COLUMN `category` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '分类' AFTER `conf_value`,
    MODIFY COLUMN `deleted` bigint(20) NOT NULL DEFAULT 0 COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间' AFTER `remarks`,
    MODIFY COLUMN `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建人' AFTER `deleted`,
    MODIFY COLUMN `update_by` bigint(20) NULL DEFAULT NULL COMMENT '修改人' AFTER `create_by`;


ALTER TABLE `i18n_data`
    MODIFY COLUMN `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID' FIRST,
    MODIFY COLUMN `deleted` bigint NOT NULL DEFAULT 0 COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间' AFTER `remarks`;