SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for log_access_log
-- ----------------------------
DROP TABLE IF EXISTS `log_access_log`;
CREATE TABLE `log_access_log`  (
  `id` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `trace_id` char(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '追踪ID',
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
  `result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '响应信息',
  `error_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '错误消息',
  `time` bigint(64) NULL DEFAULT NULL COMMENT '执行时长',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `user_name`(`username`) USING BTREE,
  INDEX `uri`(`uri`) USING BTREE,
  INDEX `httpStatus`(`http_status`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '访问日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for log_login_log
-- ----------------------------
DROP TABLE IF EXISTS `log_login_log`;
CREATE TABLE `log_login_log`  (
  `id` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `trace_id` char(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '追踪ID',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登陆IP',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作系统',
  `status` tinyint(1) NOT NULL COMMENT '状态',
  `event_type` tinyint(1) NULL DEFAULT NULL COMMENT '事件类型，1：登录 2：登出',
  `msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作信息',
  `location` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登陆地点',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '浏览器',
  `login_time` datetime NULL DEFAULT NULL COMMENT '登录/登出时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `username`(`username`) USING BTREE,
  INDEX `status`(`status`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3291 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '登陆日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for log_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `log_operation_log`;
CREATE TABLE `log_operation_log`  (
  `id` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `trace_id` char(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '追踪ID',
  `msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '日志消息',
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问IP地址',
  `user_agent` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户代理',
  `uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求URI',
  `method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方式',
  `params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '操作提交的数据',
  `status` tinyint(1) NOT NULL COMMENT '操作状态',
  `type` tinyint(1) NULL DEFAULT NULL COMMENT '操作类型',
  `time` bigint(64) NULL DEFAULT NULL COMMENT '执行时长',
  `operator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `operator`(`operator`) USING BTREE,
  INDEX `uri`(`uri`) USING BTREE,
  INDEX `status`(`status`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '操作日志' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for notify_announcement
-- ----------------------------
DROP TABLE IF EXISTS `notify_announcement`;
CREATE TABLE `notify_announcement`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `recipient_filter_type` int(1) NULL DEFAULT NULL COMMENT '接收人筛选方式，1：全部 2：用户角色 3：组织机构 4：用户类型 5：自定义用户',
  `recipient_filter_condition` json NULL COMMENT '对应接收人筛选方式的条件信息，多个用逗号分割。如角色标识，组织ID，用户类型，用户ID等',
  `receive_mode` json NULL COMMENT '接收方式',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态，0：已关闭 1：发布中 2：待发布',
  `immortal` tinyint(1) NULL DEFAULT NULL COMMENT '永久有效的',
  `deadline` datetime(3) NULL DEFAULT NULL COMMENT '截止日期',
  `deleted` bigint(20) NULL DEFAULT 0 COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间',
  `create_by` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_by` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '公告信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of notify_announcement
-- ----------------------------

-- ----------------------------
-- Table structure for notify_user_announcement
-- ----------------------------
DROP TABLE IF EXISTS `notify_user_announcement`;
CREATE TABLE `notify_user_announcement`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `announcement_id` bigint(20) NULL DEFAULT NULL COMMENT '公告id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户ID',
  `state` tinyint(1) NULL DEFAULT NULL COMMENT '状态，已读(1)|未读(0)',
  `read_time` datetime NULL DEFAULT NULL COMMENT '阅读时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '拉取时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_id_anno_id`(`user_id`, `announcement_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户公告表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of notify_user_announcement
-- ----------------------------

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
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'OAuth客户端配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of oauth_client_details
-- ----------------------------
INSERT INTO `oauth_client_details` VALUES ('actuator', NULL, '$2a$10$frNrwNcb5rUeNBd7EhME6uN7zTOe0qKlDVsT2SgZZDitJXuJJ7wDO', 'server', 'password,refresh_token', NULL, NULL, NULL, NULL, NULL, 'true');
INSERT INTO `oauth_client_details` VALUES ('test', NULL, '$2a$10$8DrIu79gvgx8.nQXuWGPR.tG/SHh547krcxhMeDrikJjUWlLua4.K', 'server', 'password,client_credentials,refresh_token,mobile', NULL, NULL, NULL, NULL, NULL, 'true');
INSERT INTO `oauth_client_details` VALUES ('ui', NULL, '$2a$10$8UbJyUN9kdE16RlqSx9Sc.YLIjKLDWFS2Nvev.uN/P2OrrCtVkuMO', 'server', 'password,refresh_token', NULL, NULL, NULL, NULL, NULL, 'true');

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '配置名称',
  `conf_key` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '配置键',
  `conf_value` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '配置值',
  `category` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分类',
  `remarks` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `deleted` bigint(20) NULL DEFAULT 0 COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间',
  `create_by` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_by` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_conf_key_deleted`(`conf_key`, `deleted`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '基础配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, '网站弹窗开关', 'site_popup', '1233', 'group', '宣传网站是否弹出框的控制开关。 1：开启 0：关闭', 0, NULL, NULL, '2019-10-15 16:45:55', '2021-04-28 19:47:34');

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` int(64) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标识',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `value_type` tinyint(1) NULL DEFAULT 0 COMMENT '值类型,1:Number 2:String 3:Boolean',
  `hash_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'hash值，当字典项被修改时变更',
  `remarks` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `deleted` bigint(20) NULL DEFAULT 0 COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间',
  `create_by` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_by` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_code_deleted`(`code`, `deleted`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES (2, 'log_status', '日志状态', 1, 'b3b0d5919e4a46ffa6dd17d9d7799c58', '正常、异常', 0, NULL, NULL, '2020-03-27 01:05:29', '2020-03-27 11:06:44');
INSERT INTO `sys_dict` VALUES (3, 'gender', '性别', 1, 'ae18a6a3e6744f10bd35dc71867edf8d', '用户性别', 0, NULL, NULL, '2020-03-27 01:05:29', '2020-03-27 00:55:28');
INSERT INTO `sys_dict` VALUES (4, 'grant_types', '授权类型', 1, 'e5316daadb490e9ca7e1ac5c5607a4', 'OAuth授权类型', 0, NULL, NULL, '2020-03-27 01:05:29', '2020-03-27 00:30:16');
INSERT INTO `sys_dict` VALUES (5, 'operation_type', '操作类型', 1, '6b375bba88f649caa38e95d8e5c5c5c9', '操作日志的操作类型', 0, NULL, NULL, '2020-07-14 20:28:54', NULL);
INSERT INTO `sys_dict` VALUES (6, 'role_type', '角色类型', 1, '90875dc38a154b9d810e8556f8972d9b', '角色类型，系统保留角色不允许删除', 0, NULL, NULL, '2020-07-14 21:16:45', '2021-08-09 00:26:30');
INSERT INTO `sys_dict` VALUES (7, 'dict_value_type', '字典数据类型', 1, '886c8965bdaa4c1e91ffcd5fb20ea84f', 'Number、String、Boolean', 0, NULL, NULL, '2020-08-14 17:16:47', NULL);
INSERT INTO `sys_dict` VALUES (8, 'login_event_type', '登陆事件类型', 1, '56f191fa64ad42b5948e9dcb331cded9', '1：登陆  2：登出', 0, NULL, NULL, '2020-09-17 14:44:00', NULL);
INSERT INTO `sys_dict` VALUES (9, 'yes_or_no', '是否', 1, 'aa22893ca4d243cb8eb198e0f7d66824', NULL, 0, NULL, NULL, '2021-08-16 16:16:43', NULL);
INSERT INTO `sys_dict` VALUES (13, 'user_type', '用户类型', 1, 'a2730f33f24045578ebe7786282e1955', '用户类型', 0, NULL, NULL, '2020-12-16 13:44:37', '2021-04-12 10:54:01');
INSERT INTO `sys_dict` VALUES (14, 'recipient_filter_type', '消息接收人筛选方式', 1, '54f95affca9e4c53aa679bca2855351f', '接收人筛选方式', 0, NULL, NULL, '2020-12-15 17:36:24', '2021-07-01 16:27:05');
INSERT INTO `sys_dict` VALUES (15, 'notify_channel', '通知渠道', 1, 'b1d33b2d410b4214920f67c01af80f2f', '通知渠道', 0, NULL, NULL, '2020-12-16 15:37:36', '2021-07-01 17:17:16');
INSERT INTO `sys_dict` VALUES (16, 'menu_type', '菜单类型', 1, '88b77280a299482a8e58fbc5fcc065a3', '系统菜单的类型', 0, NULL, NULL, '2021-04-06 21:39:45', NULL);
INSERT INTO `sys_dict` VALUES (17, 'user_status', '用户状态', 1, '9527', NULL, 0, NULL, NULL, '2021-12-03 11:54:39', '2021-12-03 11:54:39');

-- ----------------------------
-- Table structure for sys_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_item`;
CREATE TABLE `sys_dict_item`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `dict_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典Code',
  `value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据值',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态 1：启用 0：禁用',
  `attributes` json NULL COMMENT '附加属性',
  `sort` int(10) NOT NULL DEFAULT 0 COMMENT '排序（升序）',
  `remarks` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `deleted` bigint(20) NULL DEFAULT 0 COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间',
  `create_by` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_by` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uqx_value_dict_code`(`value`, `dict_code`, `deleted`) USING BTREE,
  INDEX `idx_dict_code`(`dict_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典项' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_item
-- ----------------------------
INSERT INTO `sys_dict_item` VALUES (3, 'log_status', '1', '正常', 1, '{\"tagColor\": \"\", \"languages\": \"{\\\"en-US\\\": \\\"Normal \\\", \\\"zh-CN\\\": \\\"正常\\\"}\", \"textColor\": \"#34890A\"}', 0, '', 0, NULL, NULL, '2020-03-27 01:05:52', '2021-12-03 11:57:31');
INSERT INTO `sys_dict_item` VALUES (4, 'log_status', '0', '异常', 1, '{\"tagColor\": \"\", \"languages\": \"{\\\"en-US\\\": \\\"Error\\\", \\\"zh-CN\\\": \\\"异常\\\"}\", \"textColor\": \"#FF0000\"}', 1, '', 0, NULL, NULL, '2020-03-27 01:05:52', '2021-12-03 11:57:31');
INSERT INTO `sys_dict_item` VALUES (5, 'gender', '1', '男', 1, '{\"tagColor\": \"\", \"languages\": \"{\\\"en-US\\\": \\\"Male\\\", \\\"zh-CN\\\": \\\"男\\\"}\", \"textColor\": \"\"}', 0, '', 0, NULL, NULL, '2020-03-27 01:05:52', '2021-12-03 11:57:31');
INSERT INTO `sys_dict_item` VALUES (6, 'gender', '2', '女', 1, '{\"tagColor\": \"\", \"languages\": \"{\\\"en-US\\\": \\\"Female\\\", \\\"zh-CN\\\": \\\"女\\\"}\", \"textColor\": \"\"}', 1, '', 0, NULL, NULL, '2020-03-27 01:05:52', '2021-12-03 11:57:31');
INSERT INTO `sys_dict_item` VALUES (7, 'gender', '3', '未知', 1, '{\"tagColor\": \"\", \"languages\": \"{\\\"en-US\\\": \\\"Unknown\\\", \\\"zh-CN\\\": \\\"未知\\\"}\", \"textColor\": \"\"}', 2, '', 0, NULL, NULL, '2020-03-27 01:05:52', '2021-12-03 11:57:31');
INSERT INTO `sys_dict_item` VALUES (8, 'grant_types', 'password', '密码模式', 1, '{}', 0, NULL, 0, NULL, NULL, '2020-03-27 01:05:52', '2019-08-13 07:35:28');
INSERT INTO `sys_dict_item` VALUES (9, 'grant_types', 'authorization_code', '授权码模式', 1, '{}', 1, NULL, 0, NULL, NULL, '2020-03-27 01:05:52', '2019-08-13 07:36:07');
INSERT INTO `sys_dict_item` VALUES (10, 'grant_types', 'client_credentials', '客户端模式', 1, '{}', 2, NULL, 0, NULL, NULL, '2020-03-27 01:05:52', '2019-08-13 07:36:30');
INSERT INTO `sys_dict_item` VALUES (11, 'grant_types', 'refresh_token', '刷新模式', 1, '{}', 3, NULL, 0, NULL, NULL, '2020-03-27 01:05:52', '2019-08-13 07:36:54');
INSERT INTO `sys_dict_item` VALUES (12, 'grant_types', 'implicit', '简化模式', 1, '{}', 4, NULL, 0, NULL, NULL, '2020-03-27 01:05:52', '2019-08-13 07:39:32');
INSERT INTO `sys_dict_item` VALUES (13, 'login_event_type', '1', '登陆', 1, '{\"tagColor\": \"cyan\", \"languages\": \"{\\\"en-US\\\": \\\"Login\\\", \\\"zh-CN\\\": \\\"登陆\\\"}\", \"textColor\": \"\"}', 0, '', 0, NULL, NULL, '2020-03-27 01:05:52', '2021-12-03 11:57:31');
INSERT INTO `sys_dict_item` VALUES (14, 'login_event_type', '2', '登出', 1, '{\"tagColor\": \"pink\", \"languages\": \"{\\\"en-US\\\": \\\"Logout\\\", \\\"zh-CN\\\": \\\"登出\\\"}\", \"textColor\": \"\"}', 1, '', 0, NULL, NULL, '2020-03-27 01:05:52', '2021-12-03 11:57:31');
INSERT INTO `sys_dict_item` VALUES (15, 'operation_type', '3', '查看', 1, '{\"tagColor\": \"purple\", \"languages\": \"{\\\"en-US\\\": \\\"Read\\\", \\\"zh-CN\\\": \\\"查看\\\"}\", \"textColor\": \"\"}', 3, '', 0, NULL, NULL, '2020-03-27 01:05:52', '2021-12-03 11:57:31');
INSERT INTO `sys_dict_item` VALUES (16, 'operation_type', '4', '新建', 1, '{\"tagColor\": \"cyan\", \"languages\": \"{\\\"en-US\\\": \\\"Create\\\", \\\"zh-CN\\\": \\\"新建\\\"}\", \"textColor\": \"\"}', 4, '', 0, NULL, NULL, '2020-03-27 01:05:52', '2021-12-03 11:57:31');
INSERT INTO `sys_dict_item` VALUES (17, 'operation_type', '5', '修改', 1, '{\"tagColor\": \"orange\", \"languages\": \"{\\\"en-US\\\": \\\"Update\\\", \\\"zh-CN\\\": \\\"修改\\\"}\", \"textColor\": \"\"}', 5, '', 0, NULL, NULL, '2020-03-27 01:05:52', '2021-12-03 11:57:31');
INSERT INTO `sys_dict_item` VALUES (18, 'operation_type', '6', '删除', 1, '{\"tagColor\": \"pink\", \"languages\": \"{\\\"en-US\\\": \\\"Delete\\\", \\\"zh-CN\\\": \\\"删除\\\"}\", \"textColor\": \"\"}', 6, '', 0, NULL, NULL, '2020-03-27 01:05:52', '2021-12-03 11:57:31');
INSERT INTO `sys_dict_item` VALUES (19, 'role_type', '1', '系统', 1, '{\"tagColor\": \"orange\", \"languages\": \"{\\\"en-US\\\": \\\"System\\\", \\\"zh-CN\\\": \\\"系统\\\"}\", \"textColor\": \"\"}', 1, '系统角色不能删除', 0, NULL, NULL, '2020-07-14 21:17:07', '2021-12-03 11:57:31');
INSERT INTO `sys_dict_item` VALUES (20, 'role_type', '2', '自定义', 1, '{\"tagColor\": \"green\", \"languages\": \"{\\\"en-US\\\": \\\"Custom\\\", \\\"zh-CN\\\": \\\"自定义\\\"}\", \"textColor\": \"\"}', 2, '自定义角色可以删除', 0, NULL, NULL, '2020-07-14 21:17:24', '2021-12-03 11:57:31');
INSERT INTO `sys_dict_item` VALUES (21, 'dict_type', '1', 'Number', 1, '{}', 1, NULL, 0, NULL, NULL, '2020-08-12 16:10:22', '2020-08-12 16:12:33');
INSERT INTO `sys_dict_item` VALUES (22, 'dict_type', '2', 'String', 1, '{}', 1, NULL, 0, NULL, NULL, '2020-08-12 16:10:31', '2020-08-12 16:12:27');
INSERT INTO `sys_dict_item` VALUES (23, 'dict_type', '3', 'Boolean', 1, '{}', 1, NULL, 0, NULL, NULL, '2020-08-12 16:10:38', '2020-08-12 16:12:23');
INSERT INTO `sys_dict_item` VALUES (24, 'dict_value_type', '1', 'Number', 1, '{}', 1, NULL, 0, NULL, NULL, '2020-08-12 16:10:22', '2020-08-12 16:12:33');
INSERT INTO `sys_dict_item` VALUES (25, 'dict_value_type', '2', 'String', 1, '{}', 1, NULL, 0, NULL, NULL, '2020-08-12 16:10:31', '2020-08-12 16:12:27');
INSERT INTO `sys_dict_item` VALUES (26, 'dict_value_type', '3', 'Boolean', 1, '{}', 1, NULL, 0, NULL, NULL, '2020-08-12 16:10:38', '2020-10-20 15:05:53');
INSERT INTO `sys_dict_item` VALUES (27, 'yes_or_no', '1', '是', 1, '{\"tagColor\": \"\", \"languages\": \"{\\\"en-US\\\": \\\"Yes\\\", \\\"zh-CN\\\": \\\"是\\\"}\", \"textColor\": \"\"}', 1, NULL, 0, NULL, NULL, '2021-08-16 16:17:08', '2021-12-03 11:57:31');
INSERT INTO `sys_dict_item` VALUES (28, 'yes_or_no', '0', '否', 1, '{\"tagColor\": \"\", \"languages\": \"{\\\"en-US\\\": \\\"No\\\", \\\"zh-CN\\\": \\\"否\\\"}\", \"textColor\": \"\"}', 2, NULL, 0, NULL, NULL, '2021-08-16 16:17:34', '2021-12-03 11:57:31');
INSERT INTO `sys_dict_item` VALUES (49, 'user_type', '1', '系统用户', 1, '{\"tagColor\": \"\", \"languages\": \"{\\\"en-US\\\": \\\"System User\\\", \\\"zh-CN\\\": \\\"系统用户\\\"}\", \"textColor\": \"\"}', 1, NULL, 0, NULL, NULL, '2020-12-16 13:45:19', '2021-12-03 11:57:31');
INSERT INTO `sys_dict_item` VALUES (50, 'recipient_filter_type', '1', '全部', 1, '{\"tagColor\": \"\", \"languages\": \"{\\\"en-US\\\": \\\"All\\\", \\\"zh-CN\\\": \\\"全部\\\"}\", \"textColor\": \"\"}', 1, '不筛选，对全部用户发送', 0, NULL, NULL, '2020-12-15 17:37:30', '2021-12-03 11:57:31');
INSERT INTO `sys_dict_item` VALUES (51, 'recipient_filter_type', '2', '指定角色', 1, '{\"tagColor\": \"\", \"languages\": \"{\\\"en-US\\\": \\\"Specify the role\\\", \\\"zh-CN\\\": \\\"指定角色\\\"}\", \"textColor\": \"\"}', 2, '筛选拥有指定角色的用户', 0, NULL, NULL, '2020-12-15 17:38:54', '2021-12-03 11:57:31');
INSERT INTO `sys_dict_item` VALUES (52, 'recipient_filter_type', '3', '指定组织', 1, '{\"tagColor\": \"\", \"languages\": \"{\\\"en-US\\\": \\\"Specify the organization\\\", \\\"zh-CN\\\": \\\"指定组织\\\"}\", \"textColor\": \"\"}', 3, '筛选指定组织的用户', 0, NULL, NULL, '2020-12-15 17:39:19', '2021-12-03 11:57:31');
INSERT INTO `sys_dict_item` VALUES (53, 'recipient_filter_type', '4', '指定类型', 1, '{\"tagColor\": \"\", \"languages\": \"{\\\"en-US\\\": \\\"Specify the type\\\", \\\"zh-CN\\\": \\\"指定类型\\\"}\", \"textColor\": \"\"}', 4, '筛选指定用户类型的用户', 0, NULL, NULL, '2020-12-15 17:39:50', '2021-12-03 11:57:31');
INSERT INTO `sys_dict_item` VALUES (54, 'recipient_filter_type', '5', '指定用户', 1, '{\"tagColor\": \"\", \"languages\": \"{\\\"en-US\\\": \\\"Specify the user\\\", \\\"zh-CN\\\": \\\"指定用户\\\"}\", \"textColor\": \"\"}', 5, '指定用户发送', 0, NULL, NULL, '2020-12-15 17:40:06', '2021-12-03 11:57:31');
INSERT INTO `sys_dict_item` VALUES (55, 'notify_channel', '1', '站内', 1, '{\"tagColor\": \"\", \"languages\": \"{\\\"en-US\\\": \\\"Station\\\", \\\"zh-CN\\\": \\\"站内\\\"}\", \"textColor\": \"\"}', 1, NULL, 0, NULL, NULL, '2020-12-16 15:37:53', '2021-12-03 11:57:31');
INSERT INTO `sys_dict_item` VALUES (56, 'notify_channel', '2', '短信', 1, '{\"tagColor\": \"\", \"languages\": \"{\\\"en-US\\\": \\\"SMS\\\", \\\"zh-CN\\\": \\\"短信\\\"}\", \"textColor\": \"\"}', 2, NULL, 0, NULL, NULL, '2020-12-16 15:38:08', '2021-12-03 11:57:31');
INSERT INTO `sys_dict_item` VALUES (57, 'notify_channel', '3', '邮箱', 1, '{\"tagColor\": \"\", \"languages\": \"{\\\"en-US\\\": \\\"Email\\\", \\\"zh-CN\\\": \\\"邮箱\\\"}\", \"textColor\": \"\"}', 3, NULL, 0, NULL, NULL, '2020-12-16 15:38:20', '2021-12-03 11:57:31');
INSERT INTO `sys_dict_item` VALUES (59, 'menu_type', '0', '目录', 1, '{\"tagColor\": \"\", \"languages\": \"{\\\"en-US\\\": \\\"Catalog\\\", \\\"zh-CN\\\": \\\"目录\\\"}\", \"textColor\": \"\"}', 1, NULL, 0, NULL, NULL, '2021-04-06 21:41:34', '2021-12-03 11:57:31');
INSERT INTO `sys_dict_item` VALUES (60, 'menu_type', '1', '菜单', 1, '{\"tagColor\": \"\", \"languages\": \"{\\\"en-US\\\": \\\"Menu\\\", \\\"zh-CN\\\": \\\"菜单\\\"}\", \"textColor\": \"\"}', 2, NULL, 0, NULL, NULL, '2021-04-06 21:41:45', '2021-12-03 11:57:31');
INSERT INTO `sys_dict_item` VALUES (61, 'menu_type', '2', '按钮', 1, '{\"tagColor\": \"\", \"languages\": \"{\\\"en-US\\\": \\\"Button\\\", \\\"zh-CN\\\": \\\"按钮\\\"}\", \"textColor\": \"\"}', 3, NULL, 0, NULL, NULL, '2021-04-06 21:41:56', '2021-12-03 11:57:31');
INSERT INTO `sys_dict_item` VALUES (62, 'operation_type', '0', '其他', 1, '{\"tagColor\": \"\", \"languages\": \"{\\\"en-US\\\": \\\"Other\\\", \\\"zh-CN\\\": \\\"其他\\\"}\", \"textColor\": \"\"}', 0, NULL, 0, NULL, NULL, '2021-08-16 16:59:28', '2021-12-03 11:57:31');
INSERT INTO `sys_dict_item` VALUES (63, 'operation_type', '1', '导入', 1, '{\"tagColor\": \"green\", \"languages\": \"{\\\"en-US\\\": \\\"Import\\\", \\\"zh-CN\\\": \\\"导入\\\"}\", \"textColor\": \"\"}', 1, NULL, 0, NULL, NULL, '2021-08-16 16:59:52', '2021-12-03 11:57:31');
INSERT INTO `sys_dict_item` VALUES (64, 'operation_type', '2', '导出', 1, '{\"tagColor\": \"blue\", \"languages\": \"{\\\"en-US\\\": \\\"Export\\\", \\\"zh-CN\\\": \\\"导出\\\"}\", \"textColor\": \"\"}', 2, NULL, 0, NULL, NULL, '2021-08-16 17:02:07', '2021-12-03 11:57:31');
INSERT INTO `sys_dict_item` VALUES (65, 'user_status', '0', '锁定', 1, '{\"tagColor\": \"#d9d9d9\", \"languages\": {\"en-US\": \"Locked\", \"zh-CN\": \"锁定\"}, \"textColor\": \"#d9d9d9\", \"badgeStatus\": \"default\"}', 2, NULL, 0, NULL, NULL, '2021-12-03 11:54:39', NULL);
INSERT INTO `sys_dict_item` VALUES (66, 'user_status', '1', '正常', 1, '{\"tagColor\": \"blue\", \"languages\": {\"en-US\": \"Normal\", \"zh-CN\": \"正常\"}, \"textColor\": \"#5b8ff9\", \"badgeStatus\": \"processing\"}', 1, NULL, 0, NULL, NULL, '2021-12-03 11:54:39', NULL);

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `parent_id` int(11) NOT NULL DEFAULT 0 COMMENT '父级ID',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `icon` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `permission` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '授权标识',
  `path` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由地址',
  `target_type` tinyint(1) NULL DEFAULT 1 COMMENT '打开方式 (1组件 2内链 3外链)',
  `uri` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '定位标识 (打开方式为组件时其值为组件相对路径，其他为URL地址)',
  `sort` int(11) NULL DEFAULT 1 COMMENT '显示排序',
  `keep_alive` tinyint(1) NULL DEFAULT 0 COMMENT '组件缓存：0-开启，1-关闭',
  `hidden` tinyint(1) NULL DEFAULT 0 COMMENT '隐藏菜单:  0-否，1-是',
  `type` tinyint(1) NULL DEFAULT 0 COMMENT '菜单类型 （0目录，1菜单，2按钮）',
  `remarks` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  `deleted` bigint(20) NULL DEFAULT 0 COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间',
  `create_by` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_by` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 990501 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单权限' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (10028, 0, '个人页', 'user', NULL, 'account', 1, 'account/settings/Index', 0, 0, 1, 1, NULL, 0, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (10030, 10028, '个人设置', NULL, NULL, 'settings', 1, NULL, 1, 0, 1, 0, NULL, 0, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (10031, 10030, '基本设置', NULL, NULL, 'base', 1, 'account/settings/BaseSetting', 1, 0, 0, 1, NULL, 0, NULL, NULL, NULL, '2021-12-03 12:33:43');
INSERT INTO `sys_menu` VALUES (10032, 10030, '安全设置', NULL, NULL, 'security', 1, 'account/settings/Security', 2, 0, 0, 1, NULL, 0, NULL, NULL, NULL, '2021-12-03 12:33:43');
INSERT INTO `sys_menu` VALUES (10034, 10030, '账户绑定', NULL, NULL, 'binding', 1, 'account/settings/Binding', 4, 0, 0, 1, NULL, 0, NULL, NULL, NULL, '2021-12-03 12:33:43');
INSERT INTO `sys_menu` VALUES (10035, 10030, '新消息通知', NULL, NULL, 'notification', 1, 'account/settings/Notification', 5, 0, 0, 1, NULL, 0, NULL, NULL, NULL, '2021-12-03 12:33:43');
INSERT INTO `sys_menu` VALUES (100000, 0, '系统管理', 'setting', NULL, 'system', 1, NULL, 1, 0, 0, 0, NULL, 0, NULL, NULL, NULL, '2021-12-03 11:57:30');
INSERT INTO `sys_menu` VALUES (100100, 100000, '系统用户', NULL, NULL, 'user', 1, 'system/user/SysUserPage', 1, 0, 0, 1, NULL, 0, NULL, NULL, NULL, '2021-12-03 11:57:30');
INSERT INTO `sys_menu` VALUES (100101, 100100, '系统用户查询', NULL, 'system:user:read', NULL, 1, NULL, 1, 0, 0, 2, NULL, 0, NULL, NULL, '2020-03-05 16:56:57', NULL);
INSERT INTO `sys_menu` VALUES (100102, 100100, '系统用户新增', NULL, 'system:user:add', NULL, 1, NULL, 1, 0, 0, 2, NULL, 0, NULL, NULL, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_menu` VALUES (100103, 100100, '系统用户修改', NULL, 'system:user:edit', NULL, 1, NULL, 2, 0, 0, 2, NULL, 0, NULL, NULL, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_menu` VALUES (100104, 100100, '系统用户删除', NULL, 'system:user:del', NULL, 1, NULL, 3, 0, 0, 2, NULL, 0, NULL, NULL, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_menu` VALUES (100105, 100100, '系统用户授权', NULL, 'system:user:grant', NULL, 1, NULL, 3, 0, 0, 2, NULL, 0, NULL, NULL, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_menu` VALUES (100106, 100100, '系统用户改密', NULL, 'system:user:pass', NULL, 1, NULL, 4, 0, 0, 2, NULL, 0, NULL, NULL, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_menu` VALUES (100200, 100000, '角色管理', NULL, NULL, 'role', 1, 'system/role/SysRolePage', 2, 0, 0, 1, NULL, 0, NULL, NULL, NULL, '2021-12-03 11:57:30');
INSERT INTO `sys_menu` VALUES (100201, 100200, '系统角色查询', NULL, 'system:role:read', NULL, 1, NULL, 0, 0, 0, 2, NULL, 0, NULL, NULL, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_menu` VALUES (100202, 100200, '系统角色新增', NULL, 'system:role:add', NULL, 1, NULL, 1, 0, 0, 2, NULL, 0, NULL, NULL, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_menu` VALUES (100203, 100200, '系统角色修改', NULL, 'system:role:edit', NULL, 1, NULL, 2, 0, 0, 2, NULL, 0, NULL, NULL, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_menu` VALUES (100204, 100200, '系统角色删除', NULL, 'system:role:del', NULL, 1, NULL, 3, 0, 0, 2, NULL, 0, NULL, NULL, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_menu` VALUES (100205, 100200, '系统角色授权', NULL, 'system:role:grant', NULL, 1, NULL, 3, 0, 0, 2, NULL, 0, NULL, NULL, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_menu` VALUES (100400, 100000, '配置信息', NULL, NULL, 'config', 1, 'system/config/SysConfigPage', 6, 0, 0, 1, NULL, 0, NULL, NULL, NULL, '2021-12-03 11:57:30');
INSERT INTO `sys_menu` VALUES (100401, 100400, '配置查询', NULL, 'system:config:read', NULL, 1, NULL, 0, 0, 0, 2, NULL, 0, NULL, NULL, '2019-10-13 22:00:24', '2020-06-04 00:46:03');
INSERT INTO `sys_menu` VALUES (100402, 100400, '配置新增', NULL, 'system:config:add', NULL, 1, NULL, 1, 0, 0, 2, NULL, 0, NULL, NULL, '2019-10-13 22:00:24', '2020-06-04 00:46:13');
INSERT INTO `sys_menu` VALUES (100403, 100400, '配置修改', NULL, 'system:config:edit', NULL, 1, NULL, 2, 0, 0, 2, NULL, 0, NULL, NULL, '2019-10-13 22:00:24', '2020-06-04 00:46:22');
INSERT INTO `sys_menu` VALUES (100404, 100400, '配置删除', NULL, 'system:config:del', NULL, 1, NULL, 3, 0, 0, 2, NULL, 0, NULL, NULL, '2019-10-13 22:00:24', '2020-06-04 00:46:31');
INSERT INTO `sys_menu` VALUES (100500, 100000, '字典管理', NULL, NULL, 'dict', 1, 'system/dict/SysDictPage', 5, 0, 0, 1, NULL, 0, NULL, NULL, NULL, '2021-12-03 11:57:30');
INSERT INTO `sys_menu` VALUES (100501, 100500, '字典查询', NULL, 'system:dict:read', NULL, 1, NULL, 0, 0, 0, 2, NULL, 0, NULL, NULL, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_menu` VALUES (100502, 100500, '字典新增', NULL, 'system:dict:add', NULL, 1, NULL, 1, 0, 0, 2, NULL, 0, NULL, NULL, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_menu` VALUES (100503, 100500, '字典修改', NULL, 'system:dict:edit', NULL, 1, NULL, 2, 0, 0, 2, NULL, 0, NULL, NULL, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_menu` VALUES (100504, 100500, '字典删除', NULL, 'system:dict:del', NULL, 1, NULL, 3, 0, 0, 2, NULL, 0, NULL, NULL, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_menu` VALUES (100700, 100000, '组织架构', NULL, NULL, 'organization', 1, 'system/organization/SysOrganizationPage', 4, 0, 0, 1, NULL, 0, NULL, NULL, NULL, '2021-12-03 11:57:30');
INSERT INTO `sys_menu` VALUES (100701, 100700, '组织架构查询', NULL, 'system:organization:read', NULL, 1, NULL, 0, 0, 0, 2, NULL, 0, NULL, NULL, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_menu` VALUES (100702, 100700, '组织架构新增', NULL, 'system:organization:add', NULL, 1, NULL, 1, 0, 0, 2, NULL, 0, NULL, NULL, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_menu` VALUES (100703, 100700, '组织架构修改', NULL, 'system:organization:edit', NULL, 1, NULL, 2, 0, 0, 2, NULL, 0, NULL, NULL, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_menu` VALUES (100704, 100700, '组织架构删除', NULL, 'system:organization:del', NULL, 1, NULL, 3, 0, 0, 2, NULL, 0, NULL, NULL, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_menu` VALUES (100705, 100700, '组织机构校正', NULL, 'system:organization:revised', NULL, 1, '', 5, 0, 0, 2, '校正组织机构层级和深度', 0, NULL, NULL, '2021-06-22 21:54:19', NULL);
INSERT INTO `sys_menu` VALUES (100800, 100000, '菜单权限', NULL, NULL, 'menu', 1, 'system/menu/SysMenuPage', 3, 0, 0, 1, NULL, 0, NULL, NULL, NULL, '2021-12-03 11:57:30');
INSERT INTO `sys_menu` VALUES (100801, 100800, '菜单权限查询', NULL, 'system:menu:read', NULL, 1, NULL, 0, 0, 0, 2, NULL, 0, NULL, NULL, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_menu` VALUES (100802, 100800, '菜单权限新增', NULL, 'system:menu:add', NULL, 1, NULL, 1, 0, 0, 2, NULL, 0, NULL, NULL, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_menu` VALUES (100803, 100800, '菜单权限修改', NULL, 'system:menu:edit', NULL, 1, NULL, 2, 0, 0, 2, NULL, 0, NULL, NULL, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_menu` VALUES (100804, 100800, '菜单权限删除', NULL, 'system:menu:del', NULL, 1, NULL, 3, 0, 0, 2, NULL, 0, NULL, NULL, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_menu` VALUES (110000, 0, '日志管理', 'file-search', NULL, 'log', 1, NULL, 2, 0, 0, 0, NULL, 0, NULL, NULL, NULL, '2021-12-03 11:57:30');
INSERT INTO `sys_menu` VALUES (110100, 110000, '操作日志', NULL, NULL, 'operation-log', 1, 'log/operation-log/OperationLogPage', 2, 0, 0, 1, NULL, 0, NULL, NULL, NULL, '2021-12-03 11:57:30');
INSERT INTO `sys_menu` VALUES (110101, 110100, '操作日志查询', NULL, 'log:operation-log:read', NULL, 1, NULL, 0, 0, 0, 2, NULL, 0, NULL, NULL, '2019-10-13 22:00:24', '2019-10-15 14:14:03');
INSERT INTO `sys_menu` VALUES (110200, 110000, '登陆日志', NULL, NULL, 'login-log', 1, 'log/login-log/LoginLogPage', 1, 0, 0, 1, NULL, 0, NULL, NULL, NULL, '2021-12-03 11:57:31');
INSERT INTO `sys_menu` VALUES (110201, 110200, '登陆日志查询', NULL, 'log:login-log:read', NULL, 1, NULL, 0, 0, 0, 2, NULL, 0, NULL, NULL, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_menu` VALUES (110300, 110000, '访问日志', NULL, NULL, 'access-log', 1, 'log/access-log/AccessLogPage', 3, 0, 0, 1, NULL, 0, NULL, NULL, NULL, '2021-12-03 11:57:31');
INSERT INTO `sys_menu` VALUES (110301, 110300, '访问日志查询', NULL, 'log:access-log:read', NULL, 1, NULL, 0, 0, 0, 2, NULL, 0, NULL, NULL, '2019-10-13 22:00:24', '2019-10-15 14:14:03');
INSERT INTO `sys_menu` VALUES (120000, 0, '消息通知', 'message', NULL, 'notify', 1, NULL, 3, 0, 0, 0, NULL, 0, NULL, NULL, '2020-12-15 16:47:53', '2021-12-03 11:57:31');
INSERT INTO `sys_menu` VALUES (120100, 120000, '公告信息', NULL, NULL, 'announcement', 1, 'notify/announcement/AnnouncementPage', 1, 0, 0, 1, NULL, 0, NULL, NULL, NULL, '2021-12-03 11:57:31');
INSERT INTO `sys_menu` VALUES (120101, 120100, '公告信息查询', NULL, 'notify:announcement:read', NULL, 1, NULL, 0, 0, 0, 2, NULL, 0, NULL, NULL, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_menu` VALUES (120102, 120100, '公告信息新增', NULL, 'notify:announcement:add', NULL, 1, NULL, 1, 0, 0, 2, NULL, 0, NULL, NULL, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_menu` VALUES (120103, 120100, '公告信息修改', NULL, 'notify:announcement:edit', NULL, 1, NULL, 2, 0, 0, 2, NULL, 0, NULL, NULL, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_menu` VALUES (120104, 120100, '公告信息删除', NULL, 'notify:announcement:del', NULL, 1, NULL, 3, 0, 0, 2, NULL, 0, NULL, NULL, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_menu` VALUES (120200, 120000, '用户公告', NULL, NULL, 'userannouncement', 1, 'notify/userannouncement/UserAnnouncementPage', 1, 0, 1, 1, NULL, 0, NULL, NULL, NULL, '2021-12-03 11:57:31');
INSERT INTO `sys_menu` VALUES (120201, 120200, '用户公告表查询', NULL, 'notify:userannouncement:read', NULL, 1, NULL, 0, 0, 0, 2, NULL, 0, NULL, NULL, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_menu` VALUES (660000, 0, 'Ballcat 官网', 'crown', NULL, 'ballcat', 2, 'http://www.ballcat.cn', 0, 1, 0, 1, NULL, 0, 1, 1, '2022-01-19 21:35:22', '2022-01-19 21:36:56');
INSERT INTO `sys_menu` VALUES (990000, 0, '开发平台', 'desktop', '', 'develop', 1, NULL, 99, 0, 0, 0, NULL, 0, NULL, NULL, NULL, '2021-12-03 11:57:31');
INSERT INTO `sys_menu` VALUES (990100, 990000, '接口文档', 'file', '', 'swagger', 3, 'http://ballcat-admin:8080/swagger-ui/index.html', 1, 0, 0, 1, NULL, 0, NULL, 1, NULL, '2022-01-19 21:32:19');
INSERT INTO `sys_menu` VALUES (990200, 990000, '文档增强', 'file-text', '', 'doc', 3, 'http://ballcat-admin:8080/doc.html', 2, 0, 0, 1, NULL, 0, NULL, 1, NULL, '2022-01-19 21:33:30');
INSERT INTO `sys_menu` VALUES (990300, 990000, '调度中心', 'rocket', '', 'xxl-job', 3, 'http://ballcat-job:8888/xxl-job-admin', 3, 0, 0, 1, NULL, 0, NULL, 1, NULL, '2022-01-19 21:33:37');
INSERT INTO `sys_menu` VALUES (990400, 990000, '服务监控', 'alert', '', 'monitor', 3, 'http://ballcat-monitor:9999', 4, 0, 0, 1, NULL, 0, NULL, 1, NULL, '2022-01-19 21:33:42');
INSERT INTO `sys_menu` VALUES (990500, 990000, '代码生成', 'printer', '', 'codegen', 3, 'http://localhost:7777', 5, 0, 0, 1, NULL, 0, NULL, 1, NULL, '2022-01-19 21:33:46');

-- ----------------------------
-- Table structure for sys_organization
-- ----------------------------
DROP TABLE IF EXISTS `sys_organization`;
CREATE TABLE `sys_organization`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组织名称',
  `parent_id` int(11) NULL DEFAULT 0 COMMENT '父级ID',
  `hierarchy` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '层级信息，从根节点到当前节点的最短路径，使用-分割节点ID',
  `depth` int(1) NULL DEFAULT NULL COMMENT '当前节点深度',
  `sort` int(1) NULL DEFAULT 1 COMMENT '排序字段，由小到大',
  `remarks` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `deleted` bigint(20) NULL DEFAULT 0 COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间',
  `create_by` int(11) NULL DEFAULT NULL COMMENT '创建者',
  `update_by` int(11) NULL DEFAULT NULL COMMENT '修改者',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '组织架构' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_organization
-- ----------------------------
INSERT INTO `sys_organization` VALUES (6, '高大上公司', 0, '0', 1, 1, '一个神秘的组织', 0, NULL, NULL, '2020-09-24 00:11:14', '2020-11-19 10:08:29');
INSERT INTO `sys_organization` VALUES (7, '产品研发部', 0, '0', 1, 1, '一个🐂皮的部门', 0, NULL, NULL, '2020-09-24 00:48:07', '2021-06-22 21:55:25');
INSERT INTO `sys_organization` VALUES (8, 'java开发一组', 7, '0-7', 2, 1, NULL, 0, NULL, NULL, '2020-09-24 00:50:34', NULL);
INSERT INTO `sys_organization` VALUES (9, 'Java开发二组', 7, '0-7', 2, 2, NULL, 0, NULL, NULL, '2020-09-24 00:50:57', NULL);
INSERT INTO `sys_organization` VALUES (10, '谷歌', 6, '0-6', 2, 1, NULL, 0, NULL, NULL, '2020-09-24 00:51:55', '2021-06-22 20:59:42');
INSERT INTO `sys_organization` VALUES (11, '不会Ollie', 10, '0-6-10', 3, 1, NULL, 0, NULL, NULL, '2020-09-24 14:30:11', NULL);
INSERT INTO `sys_organization` VALUES (12, 'treflip高手', 10, '0-6-10', 3, 2, NULL, 0, NULL, NULL, '2020-09-24 18:11:27', NULL);
INSERT INTO `sys_organization` VALUES (13, 'impossible', 10, '0-6-10', 3, 2, NULL, 0, NULL, NULL, '2020-09-24 18:11:53', NULL);
INSERT INTO `sys_organization` VALUES (14, '测试', 12, '0-6-10-12', 4, 1, NULL, 0, NULL, NULL, '2021-06-22 18:32:02', '2021-06-22 22:19:06');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `type` tinyint(1) NULL DEFAULT 2 COMMENT '角色类型，1：系统角色 2：业务角色',
  `scope_type` tinyint(1) NULL DEFAULT 1 COMMENT '数据范围类型',
  `scope_resources` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据范围资源，当数据范围类型为自定义时使用',
  `remarks` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `deleted` bigint(20) NULL DEFAULT 0 COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间',
  `create_by` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_by` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_code_deleted`(`code`, `deleted`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统角色' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '管理员', 'ROLE_ADMIN', 1, 0, NULL, '管理员', 0, NULL, NULL, '2017-10-29 15:45:51', '2021-09-23 15:19:09');
INSERT INTO `sys_role` VALUES (2, '测试工程师', 'ROLE_TEST', 2, 1, NULL, '测试工程师', 0, NULL, NULL, '2019-09-02 11:34:36', '2021-09-22 15:58:44');
INSERT INTO `sys_role` VALUES (14, '销售主管', 'ROLE_SALES_EXECUTIVE', 2, 1, NULL, '销售主管', 0, NULL, NULL, '2020-02-27 15:10:36', '2021-09-23 15:20:58');
INSERT INTO `sys_role` VALUES (15, '销售专员', 'ROLE_SALESMAN', 2, 1, NULL, '销售专员', 0, NULL, NULL, '2020-02-27 15:12:18', '2021-09-22 23:34:04');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'role code',
  `menu_id` int(11) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `role_code`(`role_code`, `menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (572, 'ROLE_ADMIN', 10028);
INSERT INTO `sys_role_menu` VALUES (571, 'ROLE_ADMIN', 10030);
INSERT INTO `sys_role_menu` VALUES (570, 'ROLE_ADMIN', 10031);
INSERT INTO `sys_role_menu` VALUES (573, 'ROLE_ADMIN', 10032);
INSERT INTO `sys_role_menu` VALUES (574, 'ROLE_ADMIN', 10034);
INSERT INTO `sys_role_menu` VALUES (575, 'ROLE_ADMIN', 10035);
INSERT INTO `sys_role_menu` VALUES (578, 'ROLE_ADMIN', 100000);
INSERT INTO `sys_role_menu` VALUES (577, 'ROLE_ADMIN', 100100);
INSERT INTO `sys_role_menu` VALUES (576, 'ROLE_ADMIN', 100101);
INSERT INTO `sys_role_menu` VALUES (579, 'ROLE_ADMIN', 100102);
INSERT INTO `sys_role_menu` VALUES (580, 'ROLE_ADMIN', 100103);
INSERT INTO `sys_role_menu` VALUES (581, 'ROLE_ADMIN', 100104);
INSERT INTO `sys_role_menu` VALUES (582, 'ROLE_ADMIN', 100105);
INSERT INTO `sys_role_menu` VALUES (583, 'ROLE_ADMIN', 100106);
INSERT INTO `sys_role_menu` VALUES (585, 'ROLE_ADMIN', 100200);
INSERT INTO `sys_role_menu` VALUES (584, 'ROLE_ADMIN', 100201);
INSERT INTO `sys_role_menu` VALUES (586, 'ROLE_ADMIN', 100202);
INSERT INTO `sys_role_menu` VALUES (587, 'ROLE_ADMIN', 100203);
INSERT INTO `sys_role_menu` VALUES (588, 'ROLE_ADMIN', 100204);
INSERT INTO `sys_role_menu` VALUES (589, 'ROLE_ADMIN', 100205);
INSERT INTO `sys_role_menu` VALUES (591, 'ROLE_ADMIN', 100400);
INSERT INTO `sys_role_menu` VALUES (590, 'ROLE_ADMIN', 100401);
INSERT INTO `sys_role_menu` VALUES (592, 'ROLE_ADMIN', 100402);
INSERT INTO `sys_role_menu` VALUES (593, 'ROLE_ADMIN', 100403);
INSERT INTO `sys_role_menu` VALUES (594, 'ROLE_ADMIN', 100404);
INSERT INTO `sys_role_menu` VALUES (596, 'ROLE_ADMIN', 100500);
INSERT INTO `sys_role_menu` VALUES (595, 'ROLE_ADMIN', 100501);
INSERT INTO `sys_role_menu` VALUES (597, 'ROLE_ADMIN', 100502);
INSERT INTO `sys_role_menu` VALUES (598, 'ROLE_ADMIN', 100503);
INSERT INTO `sys_role_menu` VALUES (599, 'ROLE_ADMIN', 100504);
INSERT INTO `sys_role_menu` VALUES (601, 'ROLE_ADMIN', 100700);
INSERT INTO `sys_role_menu` VALUES (600, 'ROLE_ADMIN', 100701);
INSERT INTO `sys_role_menu` VALUES (602, 'ROLE_ADMIN', 100702);
INSERT INTO `sys_role_menu` VALUES (603, 'ROLE_ADMIN', 100703);
INSERT INTO `sys_role_menu` VALUES (604, 'ROLE_ADMIN', 100704);
INSERT INTO `sys_role_menu` VALUES (605, 'ROLE_ADMIN', 100705);
INSERT INTO `sys_role_menu` VALUES (607, 'ROLE_ADMIN', 100800);
INSERT INTO `sys_role_menu` VALUES (606, 'ROLE_ADMIN', 100801);
INSERT INTO `sys_role_menu` VALUES (608, 'ROLE_ADMIN', 100802);
INSERT INTO `sys_role_menu` VALUES (609, 'ROLE_ADMIN', 100803);
INSERT INTO `sys_role_menu` VALUES (610, 'ROLE_ADMIN', 100804);
INSERT INTO `sys_role_menu` VALUES (613, 'ROLE_ADMIN', 110000);
INSERT INTO `sys_role_menu` VALUES (612, 'ROLE_ADMIN', 110100);
INSERT INTO `sys_role_menu` VALUES (611, 'ROLE_ADMIN', 110101);
INSERT INTO `sys_role_menu` VALUES (615, 'ROLE_ADMIN', 110200);
INSERT INTO `sys_role_menu` VALUES (614, 'ROLE_ADMIN', 110201);
INSERT INTO `sys_role_menu` VALUES (617, 'ROLE_ADMIN', 110300);
INSERT INTO `sys_role_menu` VALUES (616, 'ROLE_ADMIN', 110301);
INSERT INTO `sys_role_menu` VALUES (620, 'ROLE_ADMIN', 120000);
INSERT INTO `sys_role_menu` VALUES (619, 'ROLE_ADMIN', 120100);
INSERT INTO `sys_role_menu` VALUES (618, 'ROLE_ADMIN', 120101);
INSERT INTO `sys_role_menu` VALUES (621, 'ROLE_ADMIN', 120102);
INSERT INTO `sys_role_menu` VALUES (622, 'ROLE_ADMIN', 120103);
INSERT INTO `sys_role_menu` VALUES (623, 'ROLE_ADMIN', 120104);
INSERT INTO `sys_role_menu` VALUES (625, 'ROLE_ADMIN', 120200);
INSERT INTO `sys_role_menu` VALUES (624, 'ROLE_ADMIN', 120201);
INSERT INTO `sys_role_menu` VALUES (632, 'ROLE_ADMIN', 660000);
INSERT INTO `sys_role_menu` VALUES (627, 'ROLE_ADMIN', 990000);
INSERT INTO `sys_role_menu` VALUES (626, 'ROLE_ADMIN', 990100);
INSERT INTO `sys_role_menu` VALUES (628, 'ROLE_ADMIN', 990200);
INSERT INTO `sys_role_menu` VALUES (629, 'ROLE_ADMIN', 990300);
INSERT INTO `sys_role_menu` VALUES (630, 'ROLE_ADMIN', 990400);
INSERT INTO `sys_role_menu` VALUES (631, 'ROLE_ADMIN', 990500);
INSERT INTO `sys_role_menu` VALUES (279, 'ROLE_SALES_EXECUTIVE', 10029);
INSERT INTO `sys_role_menu` VALUES (280, 'ROLE_SALES_EXECUTIVE', 10031);
INSERT INTO `sys_role_menu` VALUES (281, 'ROLE_SALES_EXECUTIVE', 10032);
INSERT INTO `sys_role_menu` VALUES (282, 'ROLE_SALES_EXECUTIVE', 10033);
INSERT INTO `sys_role_menu` VALUES (283, 'ROLE_SALES_EXECUTIVE', 10034);
INSERT INTO `sys_role_menu` VALUES (284, 'ROLE_SALES_EXECUTIVE', 10035);
INSERT INTO `sys_role_menu` VALUES (285, 'ROLE_SALES_EXECUTIVE', 780000);
INSERT INTO `sys_role_menu` VALUES (286, 'ROLE_SALES_EXECUTIVE', 790000);
INSERT INTO `sys_role_menu` VALUES (635, 'ROLE_TEST', 10028);
INSERT INTO `sys_role_menu` VALUES (634, 'ROLE_TEST', 10030);
INSERT INTO `sys_role_menu` VALUES (633, 'ROLE_TEST', 10031);
INSERT INTO `sys_role_menu` VALUES (636, 'ROLE_TEST', 10032);
INSERT INTO `sys_role_menu` VALUES (673, 'ROLE_TEST', 10034);
INSERT INTO `sys_role_menu` VALUES (674, 'ROLE_TEST', 10035);
INSERT INTO `sys_role_menu` VALUES (638, 'ROLE_TEST', 100000);
INSERT INTO `sys_role_menu` VALUES (639, 'ROLE_TEST', 100100);
INSERT INTO `sys_role_menu` VALUES (640, 'ROLE_TEST', 100101);
INSERT INTO `sys_role_menu` VALUES (641, 'ROLE_TEST', 100102);
INSERT INTO `sys_role_menu` VALUES (642, 'ROLE_TEST', 100103);
INSERT INTO `sys_role_menu` VALUES (643, 'ROLE_TEST', 100104);
INSERT INTO `sys_role_menu` VALUES (644, 'ROLE_TEST', 100105);
INSERT INTO `sys_role_menu` VALUES (645, 'ROLE_TEST', 100106);
INSERT INTO `sys_role_menu` VALUES (646, 'ROLE_TEST', 100200);
INSERT INTO `sys_role_menu` VALUES (647, 'ROLE_TEST', 100201);
INSERT INTO `sys_role_menu` VALUES (648, 'ROLE_TEST', 100202);
INSERT INTO `sys_role_menu` VALUES (649, 'ROLE_TEST', 100203);
INSERT INTO `sys_role_menu` VALUES (650, 'ROLE_TEST', 100204);
INSERT INTO `sys_role_menu` VALUES (651, 'ROLE_TEST', 100205);
INSERT INTO `sys_role_menu` VALUES (652, 'ROLE_TEST', 100400);
INSERT INTO `sys_role_menu` VALUES (653, 'ROLE_TEST', 100401);
INSERT INTO `sys_role_menu` VALUES (654, 'ROLE_TEST', 100402);
INSERT INTO `sys_role_menu` VALUES (655, 'ROLE_TEST', 100403);
INSERT INTO `sys_role_menu` VALUES (656, 'ROLE_TEST', 100404);
INSERT INTO `sys_role_menu` VALUES (657, 'ROLE_TEST', 100500);
INSERT INTO `sys_role_menu` VALUES (658, 'ROLE_TEST', 100501);
INSERT INTO `sys_role_menu` VALUES (659, 'ROLE_TEST', 100502);
INSERT INTO `sys_role_menu` VALUES (660, 'ROLE_TEST', 100503);
INSERT INTO `sys_role_menu` VALUES (661, 'ROLE_TEST', 100504);
INSERT INTO `sys_role_menu` VALUES (662, 'ROLE_TEST', 100700);
INSERT INTO `sys_role_menu` VALUES (663, 'ROLE_TEST', 100701);
INSERT INTO `sys_role_menu` VALUES (664, 'ROLE_TEST', 100702);
INSERT INTO `sys_role_menu` VALUES (665, 'ROLE_TEST', 100703);
INSERT INTO `sys_role_menu` VALUES (666, 'ROLE_TEST', 100704);
INSERT INTO `sys_role_menu` VALUES (667, 'ROLE_TEST', 100705);
INSERT INTO `sys_role_menu` VALUES (668, 'ROLE_TEST', 100800);
INSERT INTO `sys_role_menu` VALUES (669, 'ROLE_TEST', 100801);
INSERT INTO `sys_role_menu` VALUES (670, 'ROLE_TEST', 100802);
INSERT INTO `sys_role_menu` VALUES (671, 'ROLE_TEST', 100803);
INSERT INTO `sys_role_menu` VALUES (672, 'ROLE_TEST', 100804);
INSERT INTO `sys_role_menu` VALUES (637, 'ROLE_TEST', 660000);
INSERT INTO `sys_role_menu` VALUES (675, 'ROLE_TEST', 990000);
INSERT INTO `sys_role_menu` VALUES (676, 'ROLE_TEST', 990100);
INSERT INTO `sys_role_menu` VALUES (677, 'ROLE_TEST', 990200);
INSERT INTO `sys_role_menu` VALUES (678, 'ROLE_TEST', 990300);
INSERT INTO `sys_role_menu` VALUES (679, 'ROLE_TEST', 990400);
INSERT INTO `sys_role_menu` VALUES (680, 'ROLE_TEST', 990500);

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
  `organization_id` int(11) NULL DEFAULT NULL COMMENT '所属组织ID',
  `deleted` bigint(20) NULL DEFAULT 0 COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间',
  `create_by` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_by` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `uk_username_deleted`(`username`, `deleted`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_organizaiton_id`(`organization_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '超管牛逼', '$2a$10$YJDXeAsk7FjQQVTdutIat.rPR3p3uUPWmZyhtnRDOrIjPujOAUrla', NULL, 'sysuser/1/avatar/20200226/ab6bd5221afe4238ae4987f278758113.jpg', 1, 'chengbohua@foxmail.com', '15800000000', 1, 1, 6, 0, NULL, NULL, '2999-09-20 17:13:24', '2020-10-17 17:40:00');
INSERT INTO `sys_user` VALUES (10, 'test4', '测试用户213', '$2a$10$B8aJ9tTJnTz0UR6OFi7v2uJlByvMCIzHS9/w1zKs1hiaraajhzhhq', NULL, 'sysuser/10/avatar/20201204/002875d468db41239ee02ad99ab14490.jpg', 2, 'magic.xiaohua@gmail.com', '12345678520', 0, 1, 6, 0, NULL, NULL, NULL, '2021-05-26 15:58:10');
INSERT INTO `sys_user` VALUES (12, 'test1', 'test1', '$2a$10$EotCw/oHyg1MgJMDFgEeeOO0/jVHZgIFn0jX9kq9SP9sIAXF2m0Yi', NULL, 'sysuser/12/avatar/20200109/05e189b252b44598b6d150ce3597d293.jpg', 1, 'test1@qq.com', '12356322365', 1, 1, 12, 20200609182117, NULL, NULL, '2019-10-18 20:40:57', NULL);
INSERT INTO `sys_user` VALUES (17, 'test2', 'test2', '$2a$10$YJDXeAsk7FjQQVTdutIat.rPR3p3uUPWmZyhtnRDOrIjPujOAUrla', NULL, NULL, 1, 'test2@qq.com', '123456789', 1, 1, 8, 0, NULL, NULL, NULL, '2020-07-06 12:09:08');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `role_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'role code',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `role_code`(`role_code`, `user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, 'ROLE_ADMIN');
INSERT INTO `sys_user_role` VALUES (6, 10, 'ROLE_SALES_EXECUTIVE');
INSERT INTO `sys_user_role` VALUES (4, 1, 'ROLE_TEST');

SET FOREIGN_KEY_CHECKS = 1;
