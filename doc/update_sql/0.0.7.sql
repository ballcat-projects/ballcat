-- 公告表
CREATE TABLE `notify_announcement` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
   `title` varchar(255) DEFAULT NULL COMMENT '标题',
   `content` text COMMENT '内容',
   `recipient_filter_type` int(1) DEFAULT NULL COMMENT '接收人筛选方式，1：全部 2：用户角色 3：组织机构 4：用户类型 5：自定义用户',
   `recipient_filter_condition` json DEFAULT NULL COMMENT '对应接收人筛选方式的条件信息，多个用逗号分割。如角色标识，组织ID，用户类型，用户ID等',
   `receive_mode` json DEFAULT NULL COMMENT '接收方式',
   `status` tinyint(1) DEFAULT NULL COMMENT '状态，0：已关闭 1：发布中 2：待发布',
   `immortal` tinyint(1) DEFAULT NULL COMMENT '永久有效的',
   `deadline` datetime(3) DEFAULT NULL COMMENT '截止日期',
   `create_by` int(1) DEFAULT NULL COMMENT '创建人',
   `create_time` datetime(3) DEFAULT NULL COMMENT '创建时间',
   `update_time` datetime(3) DEFAULT NULL COMMENT '更新时间',
   PRIMARY KEY (`id`),
   KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='公告信息';
-- 用户公告关联表
CREATE TABLE `notify_user_announcement` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `announcement_id` bigint(20) DEFAULT NULL COMMENT '公告id',
    `user_id` int(11) DEFAULT NULL COMMENT '用户ID',
    `state` tinyint(1) DEFAULT NULL COMMENT '状态，已读(1)|未读(0)',
    `read_time` datetime DEFAULT NULL COMMENT '阅读时间',
    `create_time` datetime DEFAULT NULL COMMENT '拉取时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id_anno_id` (`user_id`,`announcement_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='用户公告表';

-- 权限
INSERT INTO `sys_permission`(`id`, `title`, `code`, `path`, `router_name`, `component`, `redirect`, `target`, `parent_id`, `icon`, `sort`, `keep_alive`, `hidden`, `type`, `deleted`, `create_time`, `update_time`) VALUES (120000, '消息通知', NULL, NULL, 'notify', 'layouts/RouteView', NULL, NULL, 0, 'message', 3, 0, 0, 0, 0, '2020-12-15 16:47:53', NULL);
INSERT INTO `sys_permission`(`id`, `title`, `code`, `path`, `router_name`, `component`, `redirect`, `target`, `parent_id`, `icon`, `sort`, `keep_alive`, `hidden`, `type`, `deleted`, `create_time`, `update_time`) VALUES (120100, '公告信息', NULL, '/notify/announcement', 'announcement', 'notify/announcement/AnnouncementPage', NULL, NULL, 120000, NULL, 1, 0, 0, 1, 0, NULL, '2019-10-13 22:00:24');
INSERT INTO `sys_permission`(`id`, `title`, `code`, `path`, `router_name`, `component`, `redirect`, `target`, `parent_id`, `icon`, `sort`, `keep_alive`, `hidden`, `type`, `deleted`, `create_time`, `update_time`) VALUES (120101, '公告信息查询', 'notify:announcement:read', NULL, NULL, NULL, NULL, NULL, 120100, NULL, 0, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission`(`id`, `title`, `code`, `path`, `router_name`, `component`, `redirect`, `target`, `parent_id`, `icon`, `sort`, `keep_alive`, `hidden`, `type`, `deleted`, `create_time`, `update_time`) VALUES (120102, '公告信息新增', 'notify:announcement:add', NULL, NULL, NULL, NULL, NULL, 120100, NULL, 1, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission`(`id`, `title`, `code`, `path`, `router_name`, `component`, `redirect`, `target`, `parent_id`, `icon`, `sort`, `keep_alive`, `hidden`, `type`, `deleted`, `create_time`, `update_time`) VALUES (120103, '公告信息修改', 'notify:announcement:edit', NULL, NULL, NULL, NULL, NULL, 120100, NULL, 2, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission`(`id`, `title`, `code`, `path`, `router_name`, `component`, `redirect`, `target`, `parent_id`, `icon`, `sort`, `keep_alive`, `hidden`, `type`, `deleted`, `create_time`, `update_time`) VALUES (120104, '公告信息删除', 'notify:announcement:del', NULL, NULL, NULL, NULL, NULL, 120100, NULL, 3, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission`(`id`, `title`, `code`, `path`, `router_name`, `component`, `redirect`, `target`, `parent_id`, `icon`, `sort`, `keep_alive`, `hidden`, `type`, `deleted`, `create_time`, `update_time`) VALUES (120200, '用户公告', NULL, '/notify/userannouncement', 'userAnnouncement', 'notify/userannouncement/UserAnnouncementPage', NULL, NULL, 120000, NULL, 1, 0, 1, 1, 0, NULL, '2020-12-26 19:00:35');
INSERT INTO `sys_permission`(`id`, `title`, `code`, `path`, `router_name`, `component`, `redirect`, `target`, `parent_id`, `icon`, `sort`, `keep_alive`, `hidden`, `type`, `deleted`, `create_time`, `update_time`) VALUES (120201, '用户公告表查询', 'notify:userannouncement:read', NULL, NULL, NULL, NULL, NULL, 120200, NULL, 0, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);

-- 用户lov
INSERT INTO `sys_lov`(`keyword`, `url`, `method`, `position`, `key`, `fixed_params`, `multiple`, `ret`, `ret_field`, `create_time`, `update_time`, `title`) VALUES ('lov_user', '/sysuser/page', 'GET', 'PARAMS', 'userId', '{}', b'1', b'1', 'userId', '2020-12-16 14:45:40', '2020-12-16 14:47:59', '用户');
INSERT INTO `sys_lov_body`(`keyword`, `title`, `field`, `index`, `property`, `custom`, `html`, `create_time`) VALUES ('lov_user', '用户名', 'username', 1, '{\n\n}', b'0', NULL, '2020-12-16 14:45:40');
INSERT INTO `sys_lov_body`( `keyword`, `title`, `field`, `index`, `property`, `custom`, `html`, `create_time`) VALUES ('lov_user', '昵称', 'nickname', 2, '{\n\n}', b'0', NULL, '2020-12-16 14:45:40');
INSERT INTO `sys_lov_body`(`keyword`, `title`, `field`, `index`, `property`, `custom`, `html`, `create_time`) VALUES ('lov_user', '组织', 'organizationName', 3, '{\n\n}', b'0', NULL, '2020-12-16 14:45:40');


-- 用户类型字典
INSERT INTO `sys_dict`(`code`, `title`, `remarks`, `editable`, `value_type`, `hash_code`, `deleted`, `create_time`, `update_time`) VALUES ('user_type', '用户类型', '用户类型，1：系统用户', 1, 1, 'd7feef85cbee4da7a089eabccd6064bd', 0, '2020-12-16 13:44:37', '2020-12-16 13:54:10');
INSERT INTO `sys_dict_item`(`dict_code`, `value`, `name`, `attributes`, `sort`, `remarks`, `deleted`, `create_time`, `update_time`) VALUES ('user_type', '1', '系统用户', '{}', 1, NULL, 0, '2020-12-16 13:45:19', NULL);

-- 消息接收人筛选方式字典项
INSERT INTO `sys_dict`(`code`, `title`, `remarks`, `editable`, `value_type`, `hash_code`, `deleted`, `create_time`, `update_time`) VALUES ('recipient_filter_type', '消息接收人筛选方式', '接收人筛选方式，1：全部 2：用户角色 3：组织机构 4：用户类型 5：自定义用户', 1, 1, 'd76c2327edd74a18990aebaece8e1ea1', 0, '2020-12-15 17:36:24', NULL);
INSERT INTO `sys_dict_item`(`dict_code`, `value`, `name`, `attributes`, `sort`, `remarks`, `deleted`, `create_time`, `update_time`) VALUES ('recipient_filter_type', '1', '全部', '{}', 1, '不筛选，对全部用户发送', 0, '2020-12-15 17:37:30', NULL);
INSERT INTO `sys_dict_item`(`dict_code`, `value`, `name`, `attributes`, `sort`, `remarks`, `deleted`, `create_time`, `update_time`) VALUES ('recipient_filter_type', '2', '指定角色', '{}', 2, '筛选拥有指定角色的用户', 0, '2020-12-15 17:38:54', '2020-12-16 13:35:03');
INSERT INTO `sys_dict_item`(`dict_code`, `value`, `name`, `attributes`, `sort`, `remarks`, `deleted`, `create_time`, `update_time`) VALUES ('recipient_filter_type', '3', '指定组织', '{}', 3, '筛选指定组织的用户', 0, '2020-12-15 17:39:19', '2020-12-16 13:35:09');
INSERT INTO `sys_dict_item`(`dict_code`, `value`, `name`, `attributes`, `sort`, `remarks`, `deleted`, `create_time`, `update_time`) VALUES ('recipient_filter_type', '4', '指定类型', '{}', 4, '筛选指定用户类型的用户', 0, '2020-12-15 17:39:50', '2020-12-16 13:35:16');
INSERT INTO `sys_dict_item`(`dict_code`, `value`, `name`, `attributes`, `sort`, `remarks`, `deleted`, `create_time`, `update_time`) VALUES ('recipient_filter_type', '5', '指定用户', '{}', 5, '指定用户发送', 0, '2020-12-15 17:40:06', '2020-12-21 21:52:43');

-- 通知渠道字典项
INSERT INTO `sys_dict`(`code`, `title`, `remarks`, `editable`, `value_type`, `hash_code`, `deleted`, `create_time`, `update_time`) VALUES ('notify_channel', '通知渠道', '通知渠道', 1, 1, 'e23bf1c205a44a60995a166e819347f9', 0, '2020-12-16 15:37:36', '2021-01-07 15:32:02');
INSERT INTO `sys_dict_item`(`dict_code`, `value`, `name`, `attributes`, `sort`, `remarks`, `deleted`, `create_time`, `update_time`) VALUES ('notify_channel', '1', '站内', '{}', 1, NULL, 0, '2020-12-16 15:37:53', '2021-01-05 21:42:52');
INSERT INTO `sys_dict_item`(`dict_code`, `value`, `name`, `attributes`, `sort`, `remarks`, `deleted`, `create_time`, `update_time`) VALUES ('notify_channel', '2', '短信', '{}', 2, NULL, 0, '2020-12-16 15:38:08', NULL);
INSERT INTO `sys_dict_item`(`dict_code`, `value`, `name`, `attributes`, `sort`, `remarks`, `deleted`, `create_time`, `update_time`) VALUES ('notify_channel', '3', '邮箱', '{}', 3, NULL, 0, '2020-12-16 15:38:20', NULL);
INSERT INTO `sys_dict_item`(`dict_code`, `value`, `name`, `attributes`, `sort`, `remarks`, `deleted`, `create_time`, `update_time`) VALUES ('notify_channel', '4', '钉钉', '{}', 4, NULL, 20201221155643, '2020-12-16 15:38:28', NULL);
