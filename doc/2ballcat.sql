SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin_access_log
-- ----------------------------
DROP TABLE IF EXISTS `admin_access_log`;
CREATE TABLE `admin_access_log`  (
  `id` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint(16) NULL DEFAULT NULL COMMENT '用户ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问IP地址',
  `user_agent` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户代理',
  `uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求URI',
  `matching_pattern` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求映射路径',
  `method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作方式',
  `req_params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '请求参数',
  `req_body` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '请求body',
  `http_status` int(5) NULL DEFAULT NULL COMMENT '响应状态码',
  `error_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '错误消息',
  `time` bigint(64) NULL DEFAULT NULL COMMENT '执行时长',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `user_name`(`username`) USING BTREE,
  INDEX `uri`(`uri`) USING BTREE,
  INDEX `httpStatus`(`http_status`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28047 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '访问日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `admin_operation_log`;
CREATE TABLE `admin_operation_log`  (
  `id` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '日志消息',
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问IP地址',
  `user_agent` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户代理',
  `uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求URI',
  `method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作方式',
  `params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '操作提交的数据',
  `status` tinyint(1) NOT NULL COMMENT '操作状态',
  `time` bigint(64) NULL DEFAULT NULL COMMENT '执行时长',
  `operator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `operator`(`operator`) USING BTREE,
  INDEX `uri`(`uri`) USING BTREE,
  INDEX `status`(`status`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3332 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '操作日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details`  (
  `client_id` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `resource_ids` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `client_secret` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `scope` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `authorized_grant_types` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `web_server_redirect_uri` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `authorities` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `access_token_validity` int(11) NULL DEFAULT NULL,
  `refresh_token_validity` int(11) NULL DEFAULT NULL,
  `additional_information` varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `autoapprove` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`client_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oauth_client_details
-- ----------------------------
INSERT INTO `oauth_client_details` VALUES ('actuator', NULL, '$2a$10$frNrwNcb5rUeNBd7EhME6uN7zTOe0qKlDVsT2SgZZDitJXuJJ7wDO', 'server', 'password,refresh_token', NULL, NULL, NULL, NULL, NULL, 'true');
INSERT INTO `oauth_client_details` VALUES ('test', NULL, '$2a$10$8DrIu79gvgx8.nQXuWGPR.tG/SHh547krcxhMeDrikJjUWlLua4.K', 'server', 'password,client_credentials,refresh_token,mobile', NULL, NULL, NULL, NULL, NULL, 'true');
INSERT INTO `oauth_client_details` VALUES ('ui', NULL, '$2a$10$8UbJyUN9kdE16RlqSx9Sc.YLIjKLDWFS2Nvev.uN/P2OrrCtVkuMO', 'server', 'password,refresh_token', NULL, NULL, NULL, NULL, NULL, 'true');

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` int(64) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标识',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `editable` tinyint(1) NULL DEFAULT 0 COMMENT '可编辑 1：是 0：否',
  `hash_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'hash值，当字典项被修改时变更',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除标识',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_code`(`code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES (1, 'dict_property', '字典属性', '只读、可写', 1, 'e5316dab741b490e9ca7e1ac5c5607a4', 0, '2020-03-27 01:05:29', '2020-03-27 14:18:16');
INSERT INTO `sys_dict` VALUES (2, 'log_type', '日志类型', '异常、正常', 1, 'f8af5ef4568735abf7e6cc00afe938b', 0, '2020-03-27 01:05:29', '2020-03-27 11:06:44');
INSERT INTO `sys_dict` VALUES (3, 'gender', '性别', '用户性别', 1, 'aca1caf123123e4872be29c8cc448', 0, '2020-03-27 01:05:29', '2020-03-27 00:55:28');
INSERT INTO `sys_dict` VALUES (4, 'grant_types', '授权类型', 'OAuth授权类型', 1, 'e5316daadb490e9ca7e1ac5c5607a4', 0, '2020-03-27 01:05:29', '2020-03-27 00:30:16');

-- ----------------------------
-- Table structure for sys_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_item`;
CREATE TABLE `sys_dict_item`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `dict_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典Code',
  `value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据值',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签',
  `sort` int(10) NOT NULL DEFAULT 0 COMMENT '排序（升序）',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除标志',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `sys_dict_value`(`value`) USING BTREE,
  INDEX `sys_dict_label`(`name`) USING BTREE,
  INDEX `sys_dict_del_flag`(`deleted`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典项' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_item
-- ----------------------------
INSERT INTO `sys_dict_item` VALUES (1, 'dict_property', '0', '只读', 0, '只读字典，不可编辑', 0, '2020-03-27 01:05:52', '2020-04-11 22:34:21');
INSERT INTO `sys_dict_item` VALUES (2, 'dict_property', '1', '可写', 1, '该字典可以编辑', 0, '2020-03-27 01:05:52', '2020-04-11 22:34:14');
INSERT INTO `sys_dict_item` VALUES (3, 'log_type', '1', '正常', 0, '', 0, '2020-03-27 01:05:52', '2019-03-25 12:49:18');
INSERT INTO `sys_dict_item` VALUES (4, 'log_type', '0', '异常', 1, '', 0, '2020-03-27 01:05:52', '2019-03-25 12:49:13');
INSERT INTO `sys_dict_item` VALUES (5, 'gender', '1', '男', 0, '', 0, '2020-03-27 01:05:52', '2019-03-27 13:45:13');
INSERT INTO `sys_dict_item` VALUES (6, 'gender', '2', '女', 1, '', 0, '2020-03-27 01:05:52', '2019-03-27 13:45:34');
INSERT INTO `sys_dict_item` VALUES (7, 'gender', '3', '未知', 2, '', 0, '2020-03-27 01:05:52', '2019-03-27 13:45:57');
INSERT INTO `sys_dict_item` VALUES (8, 'grant_types', 'password', '密码模式', 0, NULL, 0, '2020-03-27 01:05:52', '2019-08-13 07:35:28');
INSERT INTO `sys_dict_item` VALUES (9, 'grant_types', 'authorization_code', '授权码模式', 1, NULL, 0, '2020-03-27 01:05:52', '2019-08-13 07:36:07');
INSERT INTO `sys_dict_item` VALUES (10, 'grant_types', 'client_credentials', '客户端模式', 2, NULL, 0, '2020-03-27 01:05:52', '2019-08-13 07:36:30');
INSERT INTO `sys_dict_item` VALUES (11, 'grant_types', 'refresh_token', '刷新模式', 3, NULL, 0, '2020-03-27 01:05:52', '2019-08-13 07:36:54');
INSERT INTO `sys_dict_item` VALUES (12, 'grant_types', 'implicit', '简化模式', 4, NULL, 0, '2020-03-27 01:05:52', '2019-08-13 07:39:32');

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `title` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单标题',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单权限标识',
  `path` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由URL',
  `router_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由名称',
  `component` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'component地址',
  `redirect` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '重定向地址',
  `target` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '链接跳转目标',
  `parent_id` int(11) NULL DEFAULT NULL COMMENT '父菜单ID',
  `icon` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图标',
  `sort` int(11) NULL DEFAULT 1 COMMENT '排序值',
  `keep_alive` tinyint(1) NULL DEFAULT 0 COMMENT '0-开启，1- 关闭',
  `hidden` tinyint(1) NULL DEFAULT 0 COMMENT '是否隐藏路由: 0否,1是',
  `type` tinyint(1) NULL DEFAULT 0 COMMENT '菜单类型 （0菜单 1按钮）',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除标记(0--正常 1--删除)',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 990510 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单权限' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES (10028, '个人页', NULL, NULL, 'account', 'layouts/RouteView', '/account/center', NULL, 0, 'user', 2, 0, 1, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES (10029, '个人中心', NULL, NULL, 'center', 'account/center/Index', NULL, NULL, 10028, NULL, 1, 0, 1, 1, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES (10030, '个人设置', NULL, NULL, 'settings', 'account/settings/Index', '/account/settings/base', NULL, 10028, NULL, 1, 0, 1, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES (10031, '基本设置', NULL, '/account/settings/base', 'BaseSettings', 'account/settings/BaseSetting', NULL, NULL, 10030, NULL, 1, 0, 0, 1, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES (10032, '安全设置', NULL, '/account/settings/security', 'SecuritySettings', 'account/settings/Security', NULL, NULL, 10030, NULL, 1, 0, 0, 1, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES (10033, '个性化设置', NULL, '/account/settings/custom', 'CustomSettings', 'account/settings/Custom', NULL, NULL, 10030, NULL, 1, 0, 0, 1, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES (10034, '账户绑定', NULL, '/account/settings/binding', 'BindingSettings', 'account/settings/Binding', NULL, NULL, 10030, NULL, 1, 0, 0, 1, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES (10035, '新消息通知', NULL, '/account/settings/notification', 'NotificationSettings', 'account/settings/Notification', NULL, NULL, 10030, NULL, 1, 0, 0, 1, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES (100000, '系统管理', NULL, '', 'sys', 'layouts/RouteView', '/sys/sysuser', NULL, 0, 'setting', 9, 0, 0, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES (100100, '系统用户', NULL, '/sys/sysuser', 'sysuser', 'sys/sysuser/SysUserPage', NULL, NULL, 100000, NULL, 1, 0, 0, 1, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES (100101, '系统用户查询', 'sys:sysuser:read', NULL, NULL, NULL, NULL, NULL, 100100, NULL, 1, 0, 0, 2, 0, '2020-03-05 16:56:57', NULL);
INSERT INTO `sys_permission` VALUES (100102, '系统用户新增', 'sys:sysuser:add', NULL, NULL, NULL, NULL, NULL, 100100, NULL, 1, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100103, '系统用户修改', 'sys:sysuser:edit', NULL, NULL, NULL, NULL, NULL, 100100, NULL, 2, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100104, '系统用户删除', 'sys:sysuser:del', NULL, NULL, NULL, NULL, NULL, 100100, NULL, 3, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100105, '系统用户授权', 'sys:sysuser:grant', NULL, NULL, NULL, NULL, NULL, 100100, NULL, 3, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100106, '系统用户改密', 'sys:sysuser:pass', NULL, NULL, NULL, NULL, NULL, 100100, NULL, 4, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100200, '角色管理', NULL, '/sys/role', 'role', 'sys/role/RolePage', NULL, NULL, 100000, NULL, 1, 0, 0, 1, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES (100201, '系统角色查询', 'sys:sysrole:read', NULL, NULL, NULL, NULL, NULL, 100200, NULL, 0, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100202, '系统角色新增', 'sys:sysrole:add', NULL, NULL, NULL, NULL, NULL, 100200, NULL, 1, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100203, '系统角色修改', 'sys:sysrole:edit', NULL, NULL, NULL, NULL, NULL, 100200, NULL, 2, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100204, '系统角色删除', 'sys:sysrole:del', NULL, NULL, NULL, NULL, NULL, 100200, NULL, 3, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100205, '系统角色授权', 'sys:sysrole:grant', NULL, NULL, NULL, NULL, NULL, 100200, NULL, 3, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100300, '权限管理', NULL, '/sys/permission', 'permission', 'sys/permission/PermissionPage', NULL, NULL, 100000, NULL, 1, 0, 0, 1, 0, NULL, '2019-10-13 22:00:24');
INSERT INTO `sys_permission` VALUES (100301, '权限查询', 'sys:syspermission:read', NULL, NULL, NULL, NULL, NULL, 100300, NULL, 0, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100302, '权限新增', 'sys:syspermission:add', NULL, NULL, NULL, NULL, NULL, 100300, NULL, 1, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100303, '权限修改', 'sys:syspermission:edit', NULL, NULL, NULL, NULL, NULL, 100300, NULL, 2, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100304, '权限删除', 'sys:syspermission:del', NULL, NULL, NULL, NULL, NULL, 100300, NULL, 3, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100400, '配置信息', NULL, '/sys/config', 'config', 'sys/config/SysConfigPage', NULL, NULL, 100000, NULL, 5, 0, 0, 1, 0, NULL, '2019-10-15 14:13:49');
INSERT INTO `sys_permission` VALUES (100401, '配置查询', 'sys:config:read', NULL, NULL, NULL, NULL, NULL, 100400, NULL, 0, 0, 0, 2, 0, '2019-10-13 22:00:24', '2019-10-15 14:14:03');
INSERT INTO `sys_permission` VALUES (100402, '配置新增', 'sys:config:add', NULL, NULL, NULL, NULL, NULL, 100400, NULL, 1, 0, 0, 2, 0, '2019-10-13 22:00:24', '2019-10-15 14:14:03');
INSERT INTO `sys_permission` VALUES (100403, '配置修改', 'sys:config:edit', NULL, NULL, NULL, NULL, NULL, 100400, NULL, 2, 0, 0, 2, 0, '2019-10-13 22:00:24', '2019-10-15 14:14:10');
INSERT INTO `sys_permission` VALUES (100404, '配置删除', 'sys:config:del', NULL, NULL, NULL, NULL, NULL, 100400, NULL, 3, 0, 0, 2, 0, '2019-10-13 22:00:24', '2019-10-15 14:14:29');
INSERT INTO `sys_permission` VALUES (100500, '字典管理', NULL, '/sys/dict', 'sysDict', 'sys/dict/SysDictPage', NULL, NULL, 100000, NULL, 4, 0, 0, 1, 0, NULL, '2019-10-13 22:00:24');
INSERT INTO `sys_permission` VALUES (100501, '字典查询', 'sys:dict:read', NULL, NULL, NULL, NULL, NULL, 100500, NULL, 0, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100502, '字典新增', 'sys:dict:add', NULL, NULL, NULL, NULL, NULL, 100500, NULL, 1, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100503, '字典修改', 'sys:dict:edit', NULL, NULL, NULL, NULL, NULL, 100500, NULL, 2, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100504, '字典删除', 'sys:dict:del', NULL, NULL, NULL, NULL, NULL, 100500, NULL, 3, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (110000, '日志管理', NULL, '', 'log', 'layouts/RouteView', '/log/adminoperationlog', NULL, 0, 'file-search', 9, 0, 0, 0, 0, NULL, '2019-10-16 18:30:07');
INSERT INTO `sys_permission` VALUES (110100, '操作日志', NULL, '/log/adminoperationlog', 'adminOperationLog', 'log/adminoperationlog/AdminOperationLogPage', NULL, NULL, 110000, NULL, 1, 0, 0, 1, 0, NULL, '2019-10-13 22:00:24');
INSERT INTO `sys_permission` VALUES (110101, '操作日志查询', 'log:adminoperationlog:read', NULL, NULL, NULL, NULL, NULL, 110100, NULL, 0, 0, 0, 2, 0, '2019-10-13 22:00:24', '2019-10-15 14:14:03');
INSERT INTO `sys_permission` VALUES (110300, '访问日志(后台)', NULL, '/log/adminaccesslog', 'adminAccessLog', 'log/adminaccesslog/AdminAccessLogPage', NULL, NULL, 110000, NULL, 1, 0, 0, 1, 0, NULL, '2019-10-13 22:00:24');
INSERT INTO `sys_permission` VALUES (110301, '访问日志(后台)查询', 'log:adminaccesslog:read', NULL, NULL, NULL, NULL, NULL, 110300, NULL, 0, 0, 0, 2, 0, '2019-10-13 22:00:24', '2019-10-15 14:14:03');
INSERT INTO `sys_permission` VALUES (990000, '开发平台', '', '', 'develop', 'layouts/RouteView', '', NULL, 0, 'desktop', 99, 0, 0, 0, 0, NULL, '2019-11-22 16:49:56');
INSERT INTO `sys_permission` VALUES (990100, '接口文档', '', 'http://ballcat-admin:8080/swagger-ui.html', 'swagger', '', '', '_blank', 990000, 'file', 1, 0, 0, 1, 0, NULL, '2019-11-22 16:48:42');
INSERT INTO `sys_permission` VALUES (990200, '文档增强', '', 'http://ballcat-admin:8080/doc.html', 'doc', '', '', '_blank', 990000, 'file-text', 2, 0, 0, 1, 0, NULL, '2019-11-22 16:48:50');
INSERT INTO `sys_permission` VALUES (990300, '调度中心', '', 'http://ballcat-job:8888/xxl-job-admin', 'job', '', '', '_blank', 990000, 'rocket', 3, 0, 0, 1, 0, NULL, '2019-11-22 16:49:14');
INSERT INTO `sys_permission` VALUES (990400, '服务监控', '', 'http://ballcat-monitor:9999', 'monitor', '', '', '_blank', 990000, 'alert', 4, 0, 0, 1, 0, NULL, '2019-11-22 16:49:22');
INSERT INTO `sys_permission` VALUES (990500, '代码生成', '', 'http://localhost:7777', 'codegen', '', '', '_blank', 990000, 'printer', 5, 0, 0, 1, 0, NULL, '2019-11-22 16:49:35');
INSERT INTO `sys_permission` VALUES (990509, '极速建群', NULL, '/aty/groupcreate', 'groupCreate', 'aty/groupcreate/groupCreatePage', NULL, NULL, 500000, NULL, 1, 0, 0, 1, 0, '2020-03-16 10:59:46', NULL);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '删除标识（0-正常,1-删除）',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `update_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `role_idx1_role_code`(`code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '管理员', 'ROLE_ADMIN', '管理员', 0, '2017-10-29 15:45:51', '2019-10-15 14:13:16');
INSERT INTO `sys_role` VALUES (2, '测试工程师', 'ROLE_TEST', '测试工程师', 0, '2019-09-02 11:34:36', '2020-02-27 15:08:49');
INSERT INTO `sys_role` VALUES (3, 'asdsad', 'asdad', NULL, 1, '2019-09-29 21:01:58', '2019-09-29 21:03:45');
INSERT INTO `sys_role` VALUES (4, '这个角色3', 'codde ', '测试角色3', 1, '2019-09-29 21:04:37', '2019-09-29 21:09:24');
INSERT INTO `sys_role` VALUES (5, 'ada', 'ROLE_1231', NULL, 1, '2019-09-29 21:09:41', '2019-09-29 21:32:58');
INSERT INTO `sys_role` VALUES (6, '测试橘色', 'ROLE_1313', NULL, 1, '2019-09-29 21:11:05', '2019-09-29 21:33:00');
INSERT INTO `sys_role` VALUES (7, 'asda', 'ROLE_123', 'ASDAD', 1, '2019-09-29 21:12:07', '2019-09-29 21:33:01');
INSERT INTO `sys_role` VALUES (8, 'ada', 'ROLE_7879', NULL, 1, '2019-09-29 21:15:46', '2019-09-29 21:33:03');
INSERT INTO `sys_role` VALUES (9, 'asdad', 'ROLE_13131313', 'de ada ', 1, '2019-09-29 21:17:49', '2019-09-29 21:33:04');
INSERT INTO `sys_role` VALUES (10, 'hjkjhkkl', 'ROLE_jhkjlk', NULL, 1, '2019-09-29 21:20:41', '2019-09-29 21:33:06');
INSERT INTO `sys_role` VALUES (11, '45456564564564', 'ROLE_ytghh', NULL, 1, '2019-09-29 21:23:02', '2019-09-29 21:33:08');
INSERT INTO `sys_role` VALUES (12, 'adsad', 'ROLE_gfas', 'adadadadasdf', 1, '2019-09-29 21:32:43', '2019-09-29 21:33:10');
INSERT INTO `sys_role` VALUES (13, '角色测试121321', 'ROLE_test', '角色测试1', 1, '2019-09-29 21:33:53', '2020-02-27 15:04:43');
INSERT INTO `sys_role` VALUES (14, '销售主管', 'ROLE_SALES_EXECUTIVE', '销售主管', 0, '2020-02-27 15:10:36', '2020-03-02 19:47:27');
INSERT INTO `sys_role` VALUES (15, '销售专员', 'ROLE_SALESMAN', '销售专员', 0, '2020-02-27 15:12:18', '2020-02-27 15:12:25');

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`  (
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `permission_id` int(11) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `permission_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES (1, 10028);
INSERT INTO `sys_role_permission` VALUES (1, 10029);
INSERT INTO `sys_role_permission` VALUES (1, 10030);
INSERT INTO `sys_role_permission` VALUES (1, 10031);
INSERT INTO `sys_role_permission` VALUES (1, 10032);
INSERT INTO `sys_role_permission` VALUES (1, 10033);
INSERT INTO `sys_role_permission` VALUES (1, 10034);
INSERT INTO `sys_role_permission` VALUES (1, 10035);
INSERT INTO `sys_role_permission` VALUES (1, 100000);
INSERT INTO `sys_role_permission` VALUES (1, 100100);
INSERT INTO `sys_role_permission` VALUES (1, 100101);
INSERT INTO `sys_role_permission` VALUES (1, 100102);
INSERT INTO `sys_role_permission` VALUES (1, 100103);
INSERT INTO `sys_role_permission` VALUES (1, 100104);
INSERT INTO `sys_role_permission` VALUES (1, 100105);
INSERT INTO `sys_role_permission` VALUES (1, 100106);
INSERT INTO `sys_role_permission` VALUES (1, 100200);
INSERT INTO `sys_role_permission` VALUES (1, 100201);
INSERT INTO `sys_role_permission` VALUES (1, 100202);
INSERT INTO `sys_role_permission` VALUES (1, 100203);
INSERT INTO `sys_role_permission` VALUES (1, 100204);
INSERT INTO `sys_role_permission` VALUES (1, 100205);
INSERT INTO `sys_role_permission` VALUES (1, 100300);
INSERT INTO `sys_role_permission` VALUES (1, 100301);
INSERT INTO `sys_role_permission` VALUES (1, 100302);
INSERT INTO `sys_role_permission` VALUES (1, 100303);
INSERT INTO `sys_role_permission` VALUES (1, 100304);
INSERT INTO `sys_role_permission` VALUES (1, 100400);
INSERT INTO `sys_role_permission` VALUES (1, 100401);
INSERT INTO `sys_role_permission` VALUES (1, 100402);
INSERT INTO `sys_role_permission` VALUES (1, 100403);
INSERT INTO `sys_role_permission` VALUES (1, 100404);
INSERT INTO `sys_role_permission` VALUES (1, 100500);
INSERT INTO `sys_role_permission` VALUES (1, 100501);
INSERT INTO `sys_role_permission` VALUES (1, 100502);
INSERT INTO `sys_role_permission` VALUES (1, 100503);
INSERT INTO `sys_role_permission` VALUES (1, 100504);
INSERT INTO `sys_role_permission` VALUES (1, 110000);
INSERT INTO `sys_role_permission` VALUES (1, 110100);
INSERT INTO `sys_role_permission` VALUES (1, 110101);
INSERT INTO `sys_role_permission` VALUES (1, 110200);
INSERT INTO `sys_role_permission` VALUES (1, 110201);
INSERT INTO `sys_role_permission` VALUES (1, 110300);
INSERT INTO `sys_role_permission` VALUES (1, 110301);
INSERT INTO `sys_role_permission` VALUES (1, 200000);
INSERT INTO `sys_role_permission` VALUES (1, 200100);
INSERT INTO `sys_role_permission` VALUES (1, 200101);
INSERT INTO `sys_role_permission` VALUES (1, 200102);
INSERT INTO `sys_role_permission` VALUES (1, 200103);
INSERT INTO `sys_role_permission` VALUES (1, 200104);
INSERT INTO `sys_role_permission` VALUES (1, 200200);
INSERT INTO `sys_role_permission` VALUES (1, 200202);
INSERT INTO `sys_role_permission` VALUES (1, 200203);
INSERT INTO `sys_role_permission` VALUES (1, 200204);
INSERT INTO `sys_role_permission` VALUES (1, 300000);
INSERT INTO `sys_role_permission` VALUES (1, 300100);
INSERT INTO `sys_role_permission` VALUES (1, 300101);
INSERT INTO `sys_role_permission` VALUES (1, 300102);
INSERT INTO `sys_role_permission` VALUES (1, 300103);
INSERT INTO `sys_role_permission` VALUES (1, 300104);
INSERT INTO `sys_role_permission` VALUES (1, 300200);
INSERT INTO `sys_role_permission` VALUES (1, 300201);
INSERT INTO `sys_role_permission` VALUES (1, 300202);
INSERT INTO `sys_role_permission` VALUES (1, 300203);
INSERT INTO `sys_role_permission` VALUES (1, 300204);
INSERT INTO `sys_role_permission` VALUES (1, 400000);
INSERT INTO `sys_role_permission` VALUES (1, 400100);
INSERT INTO `sys_role_permission` VALUES (1, 400101);
INSERT INTO `sys_role_permission` VALUES (1, 400102);
INSERT INTO `sys_role_permission` VALUES (1, 400103);
INSERT INTO `sys_role_permission` VALUES (1, 400104);
INSERT INTO `sys_role_permission` VALUES (1, 400200);
INSERT INTO `sys_role_permission` VALUES (1, 400201);
INSERT INTO `sys_role_permission` VALUES (1, 400202);
INSERT INTO `sys_role_permission` VALUES (1, 400203);
INSERT INTO `sys_role_permission` VALUES (1, 400204);
INSERT INTO `sys_role_permission` VALUES (1, 500000);
INSERT INTO `sys_role_permission` VALUES (1, 500100);
INSERT INTO `sys_role_permission` VALUES (1, 500101);
INSERT INTO `sys_role_permission` VALUES (1, 500102);
INSERT INTO `sys_role_permission` VALUES (1, 500103);
INSERT INTO `sys_role_permission` VALUES (1, 500104);
INSERT INTO `sys_role_permission` VALUES (1, 500200);
INSERT INTO `sys_role_permission` VALUES (1, 500201);
INSERT INTO `sys_role_permission` VALUES (1, 500202);
INSERT INTO `sys_role_permission` VALUES (1, 500203);
INSERT INTO `sys_role_permission` VALUES (1, 500204);
INSERT INTO `sys_role_permission` VALUES (1, 500300);
INSERT INTO `sys_role_permission` VALUES (1, 500301);
INSERT INTO `sys_role_permission` VALUES (1, 500302);
INSERT INTO `sys_role_permission` VALUES (1, 500303);
INSERT INTO `sys_role_permission` VALUES (1, 500304);
INSERT INTO `sys_role_permission` VALUES (1, 500400);
INSERT INTO `sys_role_permission` VALUES (1, 500401);
INSERT INTO `sys_role_permission` VALUES (1, 500402);
INSERT INTO `sys_role_permission` VALUES (1, 500403);
INSERT INTO `sys_role_permission` VALUES (1, 500404);
INSERT INTO `sys_role_permission` VALUES (1, 990000);
INSERT INTO `sys_role_permission` VALUES (1, 990100);
INSERT INTO `sys_role_permission` VALUES (1, 990200);
INSERT INTO `sys_role_permission` VALUES (1, 990300);
INSERT INTO `sys_role_permission` VALUES (1, 990400);
INSERT INTO `sys_role_permission` VALUES (1, 990500);
INSERT INTO `sys_role_permission` VALUES (1, 990509);
INSERT INTO `sys_role_permission` VALUES (2, 10028);
INSERT INTO `sys_role_permission` VALUES (2, 10029);
INSERT INTO `sys_role_permission` VALUES (2, 10030);
INSERT INTO `sys_role_permission` VALUES (2, 10031);
INSERT INTO `sys_role_permission` VALUES (2, 10032);
INSERT INTO `sys_role_permission` VALUES (2, 10033);
INSERT INTO `sys_role_permission` VALUES (14, 200000);
INSERT INTO `sys_role_permission` VALUES (14, 200100);
INSERT INTO `sys_role_permission` VALUES (14, 200101);
INSERT INTO `sys_role_permission` VALUES (14, 200102);
INSERT INTO `sys_role_permission` VALUES (14, 200103);
INSERT INTO `sys_role_permission` VALUES (14, 200104);
INSERT INTO `sys_role_permission` VALUES (14, 200200);
INSERT INTO `sys_role_permission` VALUES (14, 200202);
INSERT INTO `sys_role_permission` VALUES (14, 200203);
INSERT INTO `sys_role_permission` VALUES (14, 200204);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` int(2) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录账号',
  `nickname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '显示名称',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `salt` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'md5密码盐',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `sex` tinyint(1) NULL DEFAULT 0 COMMENT '性别(0-默认未知,1-男,2-女)',
  `email` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电子邮件',
  `phone` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电话',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态(1-正常,0-冻结)',
  `type` tinyint(1) NULL DEFAULT NULL COMMENT '账户类型',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '删除状态(0-正常,1-已删除)',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `index_user_name`(`username`) USING BTREE,
  INDEX `index_user_status`(`status`) USING BTREE,
  INDEX `index_user_del_flag`(`deleted`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '超管牛逼', '$2a$10$YJDXeAsk7FjQQVTdutIat.rPR3p3uUPWmZyhtnRDOrIjPujOAUrla', NULL, 'sysuser/1/avatar/20200226/ab6bd5221afe4238ae4987f278758113.jpg', 1, 'chengbohua@foxmail.com', '15800000000', 1, NULL, 0, '2999-09-20 17:13:24', '2019-09-20 17:13:27');
INSERT INTO `sys_user` VALUES (10, 'test4', '测试用户213', '$2a$10$YJDXeAsk7FjQQVTdutIat.rPR3p3uUPWmZyhtnRDOrIjPujOAUrla', NULL, '', 0, '1234567@qq.com', '12345678520', 1, NULL, 1, NULL, NULL);
INSERT INTO `sys_user` VALUES (12, 'test1', 'test1', '$2a$10$EotCw/oHyg1MgJMDFgEeeOO0/jVHZgIFn0jX9kq9SP9sIAXF2m0Yi', NULL, 'sysuser/12/avatar/20200109/05e189b252b44598b6d150ce3597d293.jpg', 1, 'test1@qq.com', '12356322365', 1, NULL, 1, '2019-10-18 20:40:57', NULL);
INSERT INTO `sys_user` VALUES (17, 'test2', 'test2', 'encode123456', NULL, NULL, 1, 'test2@qq.com', '123456789', 1, NULL, 1, NULL, NULL);
INSERT INTO `sys_user` VALUES (18, 'terry', 'terry', '$2a$10$yRGTJb.Taqne3weLlRZT2uxcMYBqBoeKH7FxXcPu2bGuXl0FAJhKG', NULL, 'sysuser/18/avatar/20200227/40d16fbadc25493bbc617aabbf60ae99.jpg', 1, 'chengbohua@foxmail.com', '1588815454', 1, NULL, 0, NULL, NULL);
INSERT INTO `sys_user` VALUES (19, 'river', 'river', '$2a$10$sOtzpy9Ns5jbpE0lRCnRiez0mmD3ocCjXfkFAp.dGoWBZHlvXWLjC', NULL, NULL, 1, 'river.liu@moppomobi.com', '15888888888', 1, NULL, 0, NULL, NULL);
INSERT INTO `sys_user` VALUES (20, 'GDS', 'GDS', '$2a$10$IyL22DM13sk/slF1NL3EjOBEmyn.40H5wN2PUYjasSuRwBFVZdwWy', NULL, NULL, 1, 'gaodongshuai3@163.com', '17521723813', 1, NULL, 0, '2020-03-11 14:36:55', NULL);
INSERT INTO `sys_user` VALUES (21, 'qweq', '12321', '$2a$10$APxG/nT3jG1qSz2Izo9Wh.1evHfXclnjVFFKBDyhVXGCUVi6ASU1S', NULL, NULL, 1, '1231', '1231', 1, 1, 0, '2020-04-15 11:28:35', NULL);
INSERT INTO `sys_user` VALUES (22, '123123', '1312', '$2a$10$cYfuSvuq706CdmT0teHuVetyt1BH6YTbOP2a8lpEXI661UWsyDNHm', NULL, NULL, 1, '12312', '13221', 1, 1, 0, '2020-04-15 11:29:01', NULL);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);
INSERT INTO `sys_user_role` VALUES (1, 2);
INSERT INTO `sys_user_role` VALUES (18, 14);
INSERT INTO `sys_user_role` VALUES (19, 14);

-- ----------------------------
-- Table structure for tbl_sys_config
-- ----------------------------
DROP TABLE IF EXISTS `tbl_sys_config`;
CREATE TABLE `tbl_sys_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '配置名称',
  `conf_key` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '配置在缓存中的key名',
  `conf_value` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '配置值',
  `groups` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组',
  `description` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `config_cache_key`(`conf_key`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '基础配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tbl_sys_config
-- ----------------------------
INSERT INTO `tbl_sys_config` VALUES (4, '网站弹窗开关', 'site_popup', '1', 'group', '宣传网站是否弹出框的控制开关。\n1：开启 0：关闭', '2020-02-27 15:06:28', '2019-10-15 16:45:55');

SET FOREIGN_KEY_CHECKS = 1;
