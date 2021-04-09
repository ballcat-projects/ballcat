-- 新建菜单表
CREATE TABLE `sys_menu` (
        `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
        `parent_id` int(11) NOT NULL DEFAULT '0' COMMENT '父级ID',
        `title` varchar(32) DEFAULT NULL COMMENT '菜单名称',
        `icon` varchar(32) DEFAULT NULL COMMENT '菜单图标',
        `permission` varchar(32) DEFAULT NULL COMMENT '授权标识',
        `path` varchar(128) DEFAULT NULL COMMENT '路由地址',
        `target_type` tinyint(1) DEFAULT '1' COMMENT '打开方式 (1组件 2内链 3外链)',
        `uri` varchar(128) DEFAULT '' COMMENT '定位标识 (打开方式为组件时其值为组件相对路径，其他为URL地址)',
        `sort` int(11) DEFAULT '1' COMMENT '显示排序',
        `keep_alive` tinyint(1) DEFAULT '0' COMMENT '组件缓存：0-开启，1-关闭',
        `hidden` tinyint(1) DEFAULT '0' COMMENT '隐藏菜单:  0-否，1-是',
        `type` tinyint(1) DEFAULT '0' COMMENT '菜单类型 （0目录，1菜单，2按钮）',
        `remarks` varchar(50) DEFAULT NULL COMMENT '备注信息',
        `deleted` bigint(20) DEFAULT NULL COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间',
        `create_time` datetime DEFAULT NULL COMMENT '创建时间',
        `update_time` datetime DEFAULT NULL COMMENT '更新时间',
        PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='菜单权限';

-- 权限表数据到菜单表数据的转换
INSERT INTO `sys_menu` (
    `id`,
    `parent_id`,
    `title`,
    `icon`,
    `permission`,
    `path`,
    `target_type`,
    `uri`,
    `sort`,
    `keep_alive`,
    `hidden`,
    `type`,
    `deleted`,
    `create_time`,
    `update_time`
) (
    SELECT
        `id`,
        `parent_id`,
        `title`,
        `icon`,
        `code` AS `permission`,
        SUBSTRING_INDEX( `path`, '/', - 1 ),
        CASE
            `target`
            WHEN '_blank' THEN
                2 ELSE 1
            END AS `target_type`,
        CASE
            `type`
            WHEN 1 THEN
                `component` ELSE NULL
            END AS `uri`,
        `sort`,
        `keep_alive`,
        `hidden`,
        `type`,
        `deleted`,
        `create_time`,
        `update_time`
    FROM
        sys_permission
)

-- 角色权限关联表修改
RENAME TABLE `sys_role_permission` TO `sys_role_menu`;
ALTER TABLE `sys_role_menu`
    CHANGE COLUMN `permission_id` `menu_id` int(11) NOT NULL COMMENT '菜单ID' AFTER `role_code`;

-- 删除权限表
DROP TABLE `sys_permission`;
