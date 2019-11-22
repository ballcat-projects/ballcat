
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
) ENGINE = InnoDB AUTO_INCREMENT = 703 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '访问日志' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '操作日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for api_access_log
-- ----------------------------
DROP TABLE IF EXISTS `api_access_log`;
CREATE TABLE `api_access_log`  (
  `id` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint(16) NULL DEFAULT NULL COMMENT '用户ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问IP地址',
  `user_agent` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户代理',
  `uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求URI',
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
) ENGINE = InnoDB AUTO_INCREMENT = 222 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '访问日志' ROW_FORMAT = Dynamic;

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
INSERT INTO `oauth_client_details` VALUES ('test', NULL, '$2a$10$8DrIu79gvgx8.nQXuWGPR.tG/SHh547krcxhMeDrikJjUWlLua4.K', 'server', 'password,refresh_token', NULL, NULL, NULL, NULL, NULL, 'true');

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `title` varchar(32) DEFAULT NULL COMMENT '菜单标题',
  `code` varchar(32) DEFAULT NULL COMMENT '菜单权限标识',
  `path` varchar(128) DEFAULT NULL COMMENT '路由URL',
  `router_name` varchar(32) DEFAULT NULL COMMENT '路由名称',
  `component` varchar(128) DEFAULT NULL COMMENT 'component地址',
  `redirect` varchar(255) DEFAULT NULL COMMENT '重定向地址',
  `target` varchar(20) DEFAULT NULL COMMENT '链接跳转目标',
  `parent_id` int(11) DEFAULT NULL COMMENT '父菜单ID',
  `icon` varchar(32) DEFAULT NULL COMMENT '图标',
  `sort` int(11) DEFAULT '1' COMMENT '排序值',
  `keep_alive` tinyint(1) DEFAULT '0' COMMENT '0-开启，1- 关闭',
  `hidden` tinyint(1) DEFAULT '0' COMMENT '是否隐藏路由: 0否,1是',
  `type` tinyint(1) DEFAULT '0' COMMENT '菜单类型 （0菜单 1按钮）',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT '逻辑删除标记(0--正常 1--删除)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=110301 DEFAULT CHARSET=utf8mb4 COMMENT='菜单权限';
-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES (10028, '个人页', NULL, NULL, 'account', 'layouts/RouteView', '/account/center', 0, 'user', 2, 0, 1, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES (10029, '个人中心', NULL, NULL, 'center', 'account/center/Index', NULL, 10028, NULL, 1, 0, 1, 1, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES (10030, '个人设置', NULL, NULL, 'settings', 'account/settings/Index', '/account/settings/base', 10028, NULL, 1, 0, 1, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES (10031, '基本设置', NULL, '/account/settings/base', 'BaseSettings', 'account/settings/BaseSetting', NULL, 10030, NULL, 1, 0, 0, 1, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES (10032, '安全设置', NULL, '/account/settings/security', 'SecuritySettings', 'account/settings/Security', NULL, 10030, NULL, 1, 0, 0, 1, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES (10033, '个性化设置', NULL, '/account/settings/custom', 'CustomSettings', 'account/settings/Custom', NULL, 10030, NULL, 1, 0, 0, 1, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES (10034, '账户绑定', NULL, '/account/settings/binding', 'BindingSettings', 'account/settings/Binding', NULL, 10030, NULL, 1, 0, 0, 1, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES (10035, '新消息通知', NULL, '/account/settings/notification', 'NotificationSettings', 'account/settings/Notification', NULL, 10030, NULL, 1, 0, 0, 1, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES (100000, '系统管理', NULL, '', 'sys', 'layouts/RouteView', '/sys/sysuser', 0, 'setting', 9, 0, 0, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES (100100, '系统用户', NULL, '/sys/sysuser', 'sysuser', 'sys/sysuser/SysUserPage', NULL, 100000, NULL, 1, 0, 0, 1, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES (100101, '系统用户新增', 'sys_sysuser_add', NULL, NULL, NULL, NULL, 100100, NULL, 0, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100102, '系统用户修改', 'sys_sysuser_edit', NULL, NULL, NULL, NULL, 100100, NULL, 1, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100103, '系统用户删除', 'sys_sysuser_del', NULL, NULL, NULL, NULL, 100100, NULL, 2, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100104, '系统用户授权', 'sys_sysuser_grant', NULL, NULL, NULL, NULL, 100100, NULL, 3, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100105, '系统用户改密', 'sys_sysuser_pass', NULL, NULL, NULL, NULL, 100100, NULL, 4, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100200, '角色管理', NULL, '/sys/role', 'role', 'sys/role/RolePage', NULL, 100000, NULL, 1, 0, 0, 1, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES (100201, '系统角色新增', 'sys_sysrole_add', NULL, NULL, NULL, NULL, 100200, NULL, 0, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100202, '系统角色修改', 'sys_sysrole_edit', NULL, NULL, NULL, NULL, 100200, NULL, 1, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100203, '系统角色删除', 'sys_sysrole_del', NULL, NULL, NULL, NULL, 100200, NULL, 2, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100204, '系统角色授权', 'sys_sysrole_grant', NULL, NULL, NULL, NULL, 100200, NULL, 3, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100300, '权限管理', NULL, '/sys/permission', 'permission', 'sys/permission/PermissionPage', NULL, 100000, NULL, 1, 0, 0, 1, 0, NULL, '2019-10-13 22:00:24');
INSERT INTO `sys_permission` VALUES (100301, '权限新增', 'sys_syspermission_add', NULL, NULL, NULL, NULL, 100300, NULL, 0, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100302, '权限修改', 'sys_syspermission_edit', NULL, NULL, NULL, NULL, 100300, NULL, 1, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100303, '权限删除', 'sys_syspermission_del', NULL, NULL, NULL, NULL, 100300, NULL, 2, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100400, '配置信息', NULL, '/config/baseconfig', 'baseconfig', 'config/baseconfig/BaseConfigPage', NULL, 100000, NULL, 4, 0, 0, 1, 0, NULL, '2019-10-15 14:13:49');
INSERT INTO `sys_permission` VALUES (100401, '配置新增', 'config_baseconfig_add', NULL, NULL, NULL, NULL, 100400, NULL, 0, 0, 0, 2, 0, '2019-10-13 22:00:24', '2019-10-15 14:14:03');
INSERT INTO `sys_permission` VALUES (100402, '配置修改', 'config_baseconfig_edit', NULL, NULL, NULL, NULL, 100400, NULL, 1, 0, 0, 2, 0, '2019-10-13 22:00:24', '2019-10-15 14:14:10');
INSERT INTO `sys_permission` VALUES (100403, '配置删除', 'config_baseconfig_del', NULL, NULL, NULL, NULL, 100400, NULL, 2, 0, 0, 2, 0, '2019-10-13 22:00:24', '2019-10-15 14:14:29');
INSERT INTO `sys_permission` VALUES (110000, '日志管理', NULL, '', 'log', 'layouts/RouteView', '/log/adminoperationlog', 0, 'file-search', 9, 0, 0, 0, 0, NULL, '2019-10-16 18:30:07');
INSERT INTO `sys_permission` VALUES (110100, '操作日志', NULL, '/log/adminoperationlog', 'adminOperationLog', 'log/adminoperationlog/AdminOperationLogPage', NULL, 110000, NULL, 1, 0, 0, 1, 0, NULL, '2019-10-13 22:00:24');
INSERT INTO `sys_permission` VALUES (110200, '访问日志(接口)', NULL, '/log/apiaccesslog', 'apiAccessLog', 'log/apiaccesslog/ApiAccessLogPage', NULL, 110000, NULL, 1, 0, 0, 1, 0, NULL, '2019-10-13 22:00:24');
INSERT INTO `sys_permission` VALUES (110300, '访问日志(后台)', NULL, '/log/adminaccesslog', 'adminAccessLog', 'log/adminaccesslog/AdminAccessLogPage', NULL, 110000, NULL, 1, 0, 0, 1, 0, NULL, '2019-10-13 22:00:24');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `del_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标识（0-正常,1-删除）',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `update_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `role_idx1_role_code`(`code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '管理员', 'ROLE_ADMIN', '管理员', 0, '2017-10-29 15:45:51', '2019-10-15 14:13:16');
INSERT INTO `sys_role` VALUES (2, '测试工程师', 'ROLE_TEST', '测试1', 0, '2019-09-02 11:34:36', '2019-10-15 14:35:29');
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
INSERT INTO `sys_role` VALUES (13, '角色测试121321', 'ROLE_test', '角色测试1', 0, '2019-09-29 21:33:53', '2019-09-29 21:38:09');

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
INSERT INTO `sys_role_permission` VALUES (1, 100200);
INSERT INTO `sys_role_permission` VALUES (1, 100201);
INSERT INTO `sys_role_permission` VALUES (1, 100202);
INSERT INTO `sys_role_permission` VALUES (1, 100203);
INSERT INTO `sys_role_permission` VALUES (1, 100204);
INSERT INTO `sys_role_permission` VALUES (1, 100300);
INSERT INTO `sys_role_permission` VALUES (1, 100301);
INSERT INTO `sys_role_permission` VALUES (1, 100302);
INSERT INTO `sys_role_permission` VALUES (1, 100303);
INSERT INTO `sys_role_permission` VALUES (1, 100400);
INSERT INTO `sys_role_permission` VALUES (1, 100401);
INSERT INTO `sys_role_permission` VALUES (1, 100402);
INSERT INTO `sys_role_permission` VALUES (1, 100403);
INSERT INTO `sys_role_permission` VALUES (1, 100500);
INSERT INTO `sys_role_permission` VALUES (1, 100501);
INSERT INTO `sys_role_permission` VALUES (1, 100502);
INSERT INTO `sys_role_permission` VALUES (1, 100503);
INSERT INTO `sys_role_permission` VALUES (1, 100700);
INSERT INTO `sys_role_permission` VALUES (1, 100701);
INSERT INTO `sys_role_permission` VALUES (1, 100702);
INSERT INTO `sys_role_permission` VALUES (1, 100703);
INSERT INTO `sys_role_permission` VALUES (1, 100800);
INSERT INTO `sys_role_permission` VALUES (1, 100801);
INSERT INTO `sys_role_permission` VALUES (1, 100802);
INSERT INTO `sys_role_permission` VALUES (1, 100803);
INSERT INTO `sys_role_permission` VALUES (1, 100900);
INSERT INTO `sys_role_permission` VALUES (1, 100901);
INSERT INTO `sys_role_permission` VALUES (1, 100902);
INSERT INTO `sys_role_permission` VALUES (1, 100903);
INSERT INTO `sys_role_permission` VALUES (1, 101000);
INSERT INTO `sys_role_permission` VALUES (1, 101001);
INSERT INTO `sys_role_permission` VALUES (1, 101002);
INSERT INTO `sys_role_permission` VALUES (1, 101003);
INSERT INTO `sys_role_permission` VALUES (1, 101100);
INSERT INTO `sys_role_permission` VALUES (1, 101101);
INSERT INTO `sys_role_permission` VALUES (1, 101200);
INSERT INTO `sys_role_permission` VALUES (1, 101201);
INSERT INTO `sys_role_permission` VALUES (1, 101202);
INSERT INTO `sys_role_permission` VALUES (1, 101203);
INSERT INTO `sys_role_permission` VALUES (1, 110000);
INSERT INTO `sys_role_permission` VALUES (1, 110100);
INSERT INTO `sys_role_permission` VALUES (1, 110200);
INSERT INTO `sys_role_permission` VALUES (1, 110300);
INSERT INTO `sys_role_permission` VALUES (2, 100000);
INSERT INTO `sys_role_permission` VALUES (2, 100100);
INSERT INTO `sys_role_permission` VALUES (2, 100200);
INSERT INTO `sys_role_permission` VALUES (2, 100300);
INSERT INTO `sys_role_permission` VALUES (2, 100400);
INSERT INTO `sys_role_permission` VALUES (2, 104001);
INSERT INTO `sys_role_permission` VALUES (2, 104003);

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
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态(1-正常,2-冻结)',
  `del_flag` tinyint(1) NULL DEFAULT NULL COMMENT '删除状态(0-正常,1-已删除)',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `index_user_name`(`username`) USING BTREE,
  INDEX `index_user_status`(`status`) USING BTREE,
  INDEX `index_user_del_flag`(`del_flag`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '超管牛逼', '$2a$10$YJDXeAsk7FjQQVTdutIat.rPR3p3uUPWmZyhtnRDOrIjPujOAUrla', NULL, 'http://ivirallink-test.oss-cn-shanghai.aliyuncs.com/811c5dc958384dcd8cbb155c643c81e3.jpg', 1, 'chengbohua@foxmail.com', '15800000000', 1, 0, '2999-09-20 17:13:24', '2019-09-20 17:13:27');
INSERT INTO `sys_user` VALUES (10, 'test', '测试用户', '$2a$10$YJDXeAsk7FjQQVTdutIat.rPR3p3uUPWmZyhtnRDOrIjPujOAUrla', NULL, '', 0, '123456@qq.com', '12345678520', 1, 0, NULL, NULL);
INSERT INTO `sys_user` VALUES (12, 'test1', 'test1', '$2a$10$EotCw/oHyg1MgJMDFgEeeOO0/jVHZgIFn0jX9kq9SP9sIAXF2m0Yi', NULL, NULL, 1, 'test1@qq.com', '12356322365', 1, 0, '2019-10-18 20:40:57', NULL);
INSERT INTO `sys_user` VALUES (17, 'test2', 'test2', 'encode123456', NULL, NULL, 1, 'test2@qq.com', '123456789', 1, 0, NULL, NULL);

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

-- ----------------------------
-- Table structure for tbl_base_config
-- ----------------------------
DROP TABLE IF EXISTS `tbl_base_config`;
CREATE TABLE `tbl_base_config`  (
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
-- Records of tbl_base_config
-- ----------------------------
INSERT INTO `tbl_base_config` VALUES (3, '测试配置信息1', 'key', '123456', '123', '123', '2019-10-18 18:43:36', NULL);
INSERT INTO `tbl_base_config` VALUES (4, '测试配置2', '213', '1231', '123123', '1231123', NULL, '2019-10-15 16:45:55');

SET FOREIGN_KEY_CHECKS = 1;
