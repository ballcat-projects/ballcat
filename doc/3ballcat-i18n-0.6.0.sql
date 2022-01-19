-- 国际化信息表
CREATE TABLE `i18n_data` (
                             `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                             `language_tag` varchar(10) NOT NULL COMMENT '语言标签',
                             `code` varchar(60) NOT NULL COMMENT '唯一标识 = 业务:关键词',
                             `message` varchar(255) NOT NULL COMMENT '文本值，可以使用 { } 加角标，作为占位符',
                             `remarks` varchar(256) DEFAULT NULL COMMENT '备注',
                             `deleted` bigint(20) DEFAULT '0' COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间',
                             `create_by` int(11) DEFAULT NULL COMMENT '创建人',
                             `update_by` int(11) DEFAULT NULL COMMENT '修改人',
                             `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                             PRIMARY KEY (`id`),
                             UNIQUE KEY `udx_laguage_tag_code` (`language_tag`,`code`,`deleted`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=93 DEFAULT CHARSET=utf8mb4 COLLATE = utf8mb4_general_ci  COMMENT='国际化信息';

-- 插入菜单的国际化信息配置
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (1, 'zh-CN', 'menu.account', '个人页', '', '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (2, 'en-US', 'menu.account', 'Account', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (3, 'zh-CN', 'menu.account.settings', '个人设置', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (4, 'en-US', 'menu.account.settings', 'Account Settings\r', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (5, 'zh-CN', 'menu.account.settings.base', '基本设置', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (6, 'en-US', 'menu.account.settings.base', 'Base', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (7, 'zh-CN', 'menu.account.settings.security', '安全设置', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (8, 'en-US', 'menu.account.settings.security', 'Security', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (9, 'zh-CN', 'menu.account.settings.binding', '账户绑定', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (10, 'en-US', 'menu.account.settings.binding', 'Binding', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (11, 'zh-CN', 'menu.account.settings.notification', '新消息通知', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (12, 'en-US', 'menu.account.settings.notification', 'Notification', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (13, 'zh-CN', 'menu.system', '系统管理', NULL, '2021-08-06 11:46:52', '2021-08-14 20:00:45');
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (14, 'en-US', 'menu.system', 'System', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (15, 'zh-CN', 'menu.system.user', '系统用户', NULL, '2021-08-06 11:46:52', '2021-08-14 20:00:56');
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (16, 'en-US', 'menu.system.user', 'User', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (17, 'zh-CN', 'menu.system.role', '角色管理', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (18, 'en-US', 'menu.system.role', 'Role', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (19, 'zh-CN', 'menu.system.config', '配置信息', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (20, 'en-US', 'menu.system.config', 'Config', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (21, 'zh-CN', 'menu.system.dict', '字典管理', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (22, 'en-US', 'menu.system.dict', 'Dict', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (23, 'zh-CN', 'menu.system.lov', '弹窗选择', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (24, 'en-US', 'menu.system.lov', 'PopUp Selctor', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (25, 'zh-CN', 'menu.system.organization', '组织架构', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (26, 'en-US', 'menu.system.organization', 'Organization', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (27, 'zh-CN', 'menu.system.menu', '菜单权限', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (28, 'en-US', 'menu.system.menu', 'Menu', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (29, 'zh-CN', 'menu.log', '日志管理', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (30, 'en-US', 'menu.log', 'Log', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (31, 'zh-CN', 'menu.log.operationLog', '操作日志', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (32, 'en-US', 'menu.log.operationLog', 'Operation Log', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (33, 'zh-CN', 'menu.log.loginLog', '登陆日志', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (34, 'en-US', 'menu.log.loginLog', 'Login Log', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (35, 'zh-CN', 'menu.log.accessLog', '访问日志', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (36, 'en-US', 'menu.log.accessLog', 'Access Log', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (37, 'zh-CN', 'menu.notice', '消息通知', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (38, 'en-US', 'menu.notice', 'Notice', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (39, 'zh-CN', 'menu.notice.announcement', '公告信息', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (40, 'en-US', 'menu.notice.announcement', 'Announcement', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (41, 'zh-CN', 'menu.notice.userAnnouncement', '用户公告', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (42, 'en-US', 'menu.notice.userAnnouncement', 'User Announcement', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (43, 'zh-CN', 'menu.i18n', '国际化', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (44, 'en-US', 'menu.i18n', 'I18N', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (45, 'zh-CN', 'menu.i18n.i18nData', '国际化信息', NULL, '2021-08-06 11:46:52', '2021-08-08 17:01:09');
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (46, 'en-US', 'menu.i18n.i18nData', 'I18N Data', NULL, '2021-08-06 11:46:52', '2021-08-07 18:42:35');
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (47, 'zh-CN', 'menu.develop', '开发平台', NULL, '2021-08-06 11:46:52', '2021-08-07 19:10:23');
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (48, 'en-US', 'menu.develop', 'Develop', NULL, '2021-08-06 11:46:52', '2021-08-07 19:11:49');
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (49, 'zh-CN', 'menu.develop.swagger', '接口文档', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (50, 'en-US', 'menu.develop.swagger', 'Swagger', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (51, 'zh-CN', 'menu.develop.knife4j', '文档增强', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (52, 'en-US', 'menu.develop.knife4j', 'Api Doc', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (53, 'zh-CN', 'menu.develop.job', '调度中心', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (54, 'en-US', 'menu.develop.job', 'Job Center', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (55, 'zh-CN', 'menu.develop.monitor', '服务监控', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (56, 'en-US', 'menu.develop.monitor', 'Monitor', NULL, '2021-08-06 11:46:52', NULL);
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (57, 'zh-CN', 'menu.develop.codegen', '代码生成', NULL, '2021-08-06 11:46:52', '2021-08-08 17:03:22');
INSERT INTO `i18n_data` (`id`, `language_tag`, `code`, `message`, `remarks`, `create_time`, `update_time`) VALUES (58, 'en-US', 'menu.develop.codegen', 'Code Generate', NULL, '2021-08-06 11:46:52', '2021-08-08 17:03:33');


-- 插入国际化菜单
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `icon`, `permission`, `path`, `target_type`, `uri`, `sort`, `keep_alive`, `hidden`, `type`, `remarks`, `deleted`, `create_time`, `update_time`) VALUES (130000, 0, 'menu.i18n', 'global', NULL, 'i18n', 1, '', 4, 0, 0, 0, '国际化', 0, '2021-08-05 17:04:07', '2021-08-05 17:04:57');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `icon`, `permission`, `path`, `target_type`, `uri`, `sort`, `keep_alive`, `hidden`, `type`, `remarks`, `deleted`, `create_time`, `update_time`) VALUES (130100, 130000, 'menu.i18n.i18nData', NULL, NULL, 'i18n-data', 1, 'i18n/i18n-data/I18nDataPage', 1, 1, 0, 1, '国际化信息', 0, '2021-08-05 17:00:54', '2021-08-05 17:29:34');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `icon`, `permission`, `path`, `target_type`, `uri`, `sort`, `keep_alive`, `hidden`, `type`, `remarks`, `deleted`, `create_time`, `update_time`) VALUES (130101, 130100, '国际化信息查询', NULL, 'i18n:i18n-data:read', '', 1, NULL, 1, 0, 0, 2, NULL, 0, '2021-08-05 17:00:54', '2021-08-05 17:00:54');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `icon`, `permission`, `path`, `target_type`, `uri`, `sort`, `keep_alive`, `hidden`, `type`, `remarks`, `deleted`, `create_time`, `update_time`) VALUES (130102, 130100, '国际化信息新增', NULL, 'i18n:i18n-data:add', '', 1, NULL, 2, 0, 0, 2, NULL, 0, '2021-08-05 17:00:54', '2021-08-05 17:00:54');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `icon`, `permission`, `path`, `target_type`, `uri`, `sort`, `keep_alive`, `hidden`, `type`, `remarks`, `deleted`, `create_time`, `update_time`) VALUES (130103, 130100, '国际化信息修改', NULL, 'i18n:i18n-data:edit', '', 1, NULL, 3, 0, 0, 2, NULL, 0, '2021-08-05 17:00:54', '2021-08-05 17:00:54');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `icon`, `permission`, `path`, `target_type`, `uri`, `sort`, `keep_alive`, `hidden`, `type`, `remarks`, `deleted`, `create_time`, `update_time`) VALUES (130104, 130100, '国际化信息删除', NULL, 'i18n:i18n-data:del', '', 1, NULL, 4, 0, 0, 2, NULL, 0, '2021-08-05 17:00:54', '2021-08-05 17:00:54');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `icon`, `permission`, `path`, `target_type`, `uri`, `sort`, `keep_alive`, `hidden`, `type`, `remarks`, `deleted`, `create_time`, `update_time`) VALUES (130105, 130100, '国际化信息导出', NULL, 'i18n:i18n-data:export', '', 1, NULL, 5, 0, 0, 2, NULL, 0, '2021-08-05 17:00:54', '2021-08-05 17:00:54');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `icon`, `permission`, `path`, `target_type`, `uri`, `sort`, `keep_alive`, `hidden`, `type`, `remarks`, `deleted`, `create_time`, `update_time`) VALUES (130106, 130100, '国际化信息导出', NULL, 'i18n:i18n-data:import', '', 1, NULL, 6, 0, 0, 2, NULL, 0, '2021-08-05 17:00:54', '2021-08-05 17:00:54');


-- 更新菜单的国际化 title
update sys_menu set title = 'menu.account' where id = 10028;
update sys_menu set title = 'menu.account.settings' where id = 10030;
update sys_menu set title = 'menu.account.settings.base' where id = 10031;
update sys_menu set title = 'menu.account.settings.security' where id = 10032;
update sys_menu set title = 'menu.account.settings.binding' where id = 10034;
update sys_menu set title = 'menu.account.settings.notification' where id = 10035;
update sys_menu set title = 'menu.system' where id = 100000;
update sys_menu set title = 'menu.system.user' where id = 100100;
update sys_menu set title = 'menu.system.role' where id = 100200;
update sys_menu set title = 'menu.system.config' where id = 100400;
update sys_menu set title = 'menu.system.dict' where id = 100500;
update sys_menu set title = 'menu.system.lov' where id = 100600;
update sys_menu set title = 'menu.system.organization' where id = 100700;
update sys_menu set title = 'menu.system.menu' where id = 100800;
update sys_menu set title = 'menu.log' where id = 110000;
update sys_menu set title = 'menu.log.operationLog' where id = 110100;
update sys_menu set title = 'menu.log.loginLog' where id = 110200;
update sys_menu set title = 'menu.log.accessLog' where id = 110300;
update sys_menu set title = 'menu.notice' where id = 120000;
update sys_menu set title = 'menu.notice.announcement' where id = 120100;
update sys_menu set title = 'menu.notice.userAnnouncement' where id = 120200;
update sys_menu set title = 'menu.i18n' where id = 130000;
update sys_menu set title = 'menu.i18n.i18nData' where id = 130100;
update sys_menu set title = 'menu.develop' where id = 990000;
update sys_menu set title = 'menu.develop.swagger' where id = 990100;
update sys_menu set title = 'menu.develop.knife4j' where id = 990200;
update sys_menu set title = 'menu.develop.job' where id = 990300;
update sys_menu set title = 'menu.develop.monitor' where id = 990400;
update sys_menu set title = 'menu.develop.codegen' where id = 990500;


-- 字典国际化配置
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.languages", '{"en-US": "Normal ", "zh-CN": "正常"}') where dict_code = 'log_status' and value = '1';
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.languages", '{"en-US": "Error", "zh-CN": "异常"}') where dict_code = 'log_status' and value = '0';
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.languages", '{"en-US": "Male", "zh-CN": "男"}') where dict_code = 'gender' and value = '1';
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.languages", '{"en-US": "Female", "zh-CN": "女"}') where dict_code = 'gender' and value = '2';
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.languages", '{"en-US": "Unknown", "zh-CN": "未知"}') where dict_code = 'gender' and value = '3';
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.languages", '{"en-US": "Login", "zh-CN": "登陆"}') where dict_code = 'login_event_type' and value = '1';
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.languages", '{"en-US": "Logout", "zh-CN": "登出"}') where dict_code = 'login_event_type' and value = '2';
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.languages", '{"en-US": "Read", "zh-CN": "查看"}') where dict_code = 'operation_type' and value = '3';
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.languages", '{"en-US": "Create", "zh-CN": "新建"}') where dict_code = 'operation_type' and value = '4';
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.languages", '{"en-US": "Update", "zh-CN": "修改"}') where dict_code = 'operation_type' and value = '5';
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.languages", '{"en-US": "Delete", "zh-CN": "删除"}') where dict_code = 'operation_type' and value = '6';
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.languages", '{"en-US": "System", "zh-CN": "系统"}') where dict_code = 'role_type' and value = '1';
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.languages", '{"en-US": "Custom", "zh-CN": "自定义"}') where dict_code = 'role_type' and value = '2';
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.languages", '{"en-US": "Yes", "zh-CN": "是"}') where dict_code = 'yes_or_no' and value = '1';
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.languages", '{"en-US": "No", "zh-CN": "否"}') where dict_code = 'yes_or_no' and value = '0';
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.languages", '{"en-US": "System User", "zh-CN": "系统用户"}') where dict_code = 'user_type' and value = '1';
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.languages", '{"en-US": "All", "zh-CN": "全部"}') where dict_code = 'recipient_filter_type' and value = '1';
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.languages", '{"en-US": "Specify the role", "zh-CN": "指定角色"}') where dict_code = 'recipient_filter_type' and value = '2';
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.languages", '{"en-US": "Specify the organization", "zh-CN": "指定组织"}') where dict_code = 'recipient_filter_type' and value = '3';
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.languages", '{"en-US": "Specify the type", "zh-CN": "指定类型"}') where dict_code = 'recipient_filter_type' and value = '4';
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.languages", '{"en-US": "Specify the user", "zh-CN": "指定用户"}') where dict_code = 'recipient_filter_type' and value = '5';
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.languages", '{"en-US": "Station", "zh-CN": "站内"}') where dict_code = 'notify_channel' and value = '1';
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.languages", '{"en-US": "SMS", "zh-CN": "短信"}') where dict_code = 'notify_channel' and value = '2';
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.languages", '{"en-US": "Email", "zh-CN": "邮箱"}') where dict_code = 'notify_channel' and value = '3';
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.languages", '{"en-US": "Catalog", "zh-CN": "目录"}') where dict_code = 'menu_type' and value = '0';
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.languages", '{"en-US": "Menu", "zh-CN": "菜单"}') where dict_code = 'menu_type' and value = '1';
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.languages", '{"en-US": "Button", "zh-CN": "按钮"}') where dict_code = 'menu_type' and value = '2';
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.languages", '{"en-US": "Other", "zh-CN": "其他"}') where dict_code = 'operation_type' and value = '0';
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.languages", '{"en-US": "Import", "zh-CN": "导入"}') where dict_code = 'operation_type' and value = '1';
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.languages", '{"en-US": "Export", "zh-CN": "导出"}') where dict_code = 'operation_type' and value = '2';
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.languages", ' {"en-US": "Locked", "zh-CN": "锁定"}') where dict_code = 'user_status' and value = '0';
update sys_dict_item set `attributes` = JSON_SET(`attributes`, "$.languages", '{"en-US": "Normal", "zh-CN": "正常"}') where dict_code = 'user_status' and value = '1';
