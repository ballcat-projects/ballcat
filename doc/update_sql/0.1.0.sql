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
        CASE
            `type`
            WHEN 0 THEN
                LOWER(`router_name`)
            WHEN 1 THEN
                IF(LEFT(`path`, 4) = 'http', LOWER(`router_name`),
                IF(LOCATE('/', `path`) > 0, SUBSTRING_INDEX( `path`, '/', - 1 ), `path`)) ELSE NULL
            END AS `path`,
        CASE
            `target`
            WHEN '_blank' THEN
                3 ELSE 1
            END AS `target_type`,
        CASE
            `type`
            WHEN 1 THEN
                IF(LEFT ( `PATH`, 4 ) = 'http', `PATH`, `component` ) ELSE NULL
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
);

-- 删除无用的权限数据
DELETE FROM `sys_menu` WHERE `id` = 10029;
DELETE FROM `sys_menu` WHERE `id` = 10033;


-- 更新权限标识
update sys_menu set permission = REPLACE(permission,'sys:sys','system:');
update sys_menu set permission = REPLACE(permission,'sys:','system:');

-- 修改菜单URI
UPDATE sys_menu
SET uri = REPLACE ( uri, 'sys/sys', 'sys/' )
WHERE
  type = 1
  AND target_type = 1
  AND LEFT ( uri, 3 ) = 'sys';

UPDATE sys_menu
SET uri = CONCAT(
        REVERSE( SUBSTR( REVERSE( uri ), INSTR( REVERSE( uri ), '/' ) + 1 ) ),
        '/Sys',
        REVERSE( LEFT ( REVERSE( uri ), LOCATE( '/', REVERSE( uri ) ) - 1 ) )
    )
WHERE
  type = 1
  AND target_type = 1
  AND LEFT ( REVERSE( LEFT ( REVERSE( uri ), LOCATE( '/', REVERSE( uri ) ) - 1 ) ), 3 ) != 'Sys'
  AND LEFT ( uri, 3 ) = 'sys';

UPDATE sys_menu
SET uri =  CONCAT('system', SUBSTRING(uri, 4, LENGTH(uri)) )
WHERE
  type = 1
  AND target_type = 1
  AND LEFT ( uri, 3 ) = 'sys';

-- 更新菜单 Path
update sys_menu set path = 'system' where id = 100000;
update sys_menu set path = 'user' where id = 100100;

-- 修改菜单名称
UPDATE `sys_menu` SET `title` = '弹出选择器', uri = 'system/lov/SysLovPage' WHERE `id` = 100600;

-- 删除原来的权限菜单
DELETE FROM `sys_menu` WHERE id in (100300, 100301, 100302, 100303, 100304);
-- 插入为现在的菜单管理
INSERT INTO `sys_menu`(`id`, `parent_id`, `title`, `icon`, `permission`, `path`, `target_type`, `uri`, `sort`, `keep_alive`, `hidden`, `type`, `remarks`, `deleted`, `create_time`, `update_time`) VALUES (100300, 100000, '菜单权限', NULL, NULL, 'menu', 1, 'system/menu/SysMenuPage', 3, 0, 0, 1, NULL, 0, now(), NULL);
INSERT INTO `sys_menu`(`id`, `parent_id`, `title`, `icon`, `permission`, `path`, `target_type`, `uri`, `sort`, `keep_alive`, `hidden`, `type`, `remarks`, `deleted`, `create_time`, `update_time`) VALUES (100301, 100800, '菜单权限查询', NULL, 'system:menu:read', NULL, 1, NULL, 0, 0, 0, 2, NULL, 0, now(), NULL);
INSERT INTO `sys_menu`(`id`, `parent_id`, `title`, `icon`, `permission`, `path`, `target_type`, `uri`, `sort`, `keep_alive`, `hidden`, `type`, `remarks`, `deleted`, `create_time`, `update_time`) VALUES (100302, 100800, '菜单权限新增', NULL, 'system:menu:add', NULL, 1, NULL, 1, 0, 0, 2, NULL, 0, now(), NULL);
INSERT INTO `sys_menu`(`id`, `parent_id`, `title`, `icon`, `permission`, `path`, `target_type`, `uri`, `sort`, `keep_alive`, `hidden`, `type`, `remarks`, `deleted`, `create_time`, `update_time`) VALUES (100303, 100800, '菜单权限修改', NULL, 'system:menu:edit', NULL, 1, NULL, 2, 0, 0, 2, NULL, 0, now(), NULL);
INSERT INTO `sys_menu`(`id`, `parent_id`, `title`, `icon`, `permission`, `path`, `target_type`, `uri`, `sort`, `keep_alive`, `hidden`, `type`, `remarks`, `deleted`, `create_time`, `update_time`) VALUES (100304, 100800, '菜单权限删除', NULL, 'system:menu:del', NULL, 1, NULL, 3, 0, 0, 2, NULL, 0, now(), NULL);


-- 角色权限关联表修改
RENAME TABLE `sys_role_permission` TO `sys_role_menu`;
ALTER TABLE `sys_role_menu`
    CHANGE COLUMN `permission_id` `menu_id` int(11) NOT NULL COMMENT '菜单ID' AFTER `role_code`;


-- 删除权限表
DROP TABLE `sys_permission`;



-- 新增字典
INSERT INTO `sys_dict`(`code`, `title`, `remarks`, `editable`, `value_type`, `hash_code`, `deleted`, `create_time`, `update_time`) VALUES ('menu_type', '菜单类型', '系统菜单的类型', 0, 1, '977ab81907404dcb8b2392b98738f95f', 0, '2021-04-06 21:39:45', NULL);
INSERT INTO `sys_dict_item`(`dict_code`, `value`, `name`, `attributes`, `sort`, `remarks`, `deleted`, `create_time`, `update_time`) VALUES ('menu_type', '0', '目录', '{}', 1, NULL, 0, '2021-04-06 21:41:34', NULL);
INSERT INTO `sys_dict_item`(`dict_code`, `value`, `name`, `attributes`, `sort`, `remarks`, `deleted`, `create_time`, `update_time`) VALUES ('menu_type', '1', '菜单', '{}', 2, NULL, 0, '2021-04-06 21:41:45', NULL);
INSERT INTO `sys_dict_item`(`dict_code`, `value`, `name`, `attributes`, `sort`, `remarks`, `deleted`, `create_time`, `update_time`) VALUES ('menu_type', '2', '按钮', '{}', 3, NULL, 0, '2021-04-06 21:41:56', NULL);

-- lov 修改用户请求地址
UPDATE `sys_lov` SET `url` = '/system/user/page', update_time = now() WHERE `keyword` = 'lov_user'