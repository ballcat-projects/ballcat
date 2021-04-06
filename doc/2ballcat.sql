
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin_access_log
-- ----------------------------
DROP TABLE IF EXISTS `admin_access_log`;
CREATE TABLE `admin_access_log`  (
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
                                     `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
                                     PRIMARY KEY (`id`) USING BTREE,
                                     INDEX `user_id`(`user_id`) USING BTREE,
                                     INDEX `user_name`(`username`) USING BTREE,
                                     INDEX `uri`(`uri`) USING BTREE,
                                     INDEX `httpStatus`(`http_status`) USING BTREE,
                                     INDEX `create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '访问日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_login_log
-- ----------------------------
DROP TABLE IF EXISTS `admin_login_log`;
CREATE TABLE `admin_login_log`  (
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
                                    `login_time` datetime(0) NULL DEFAULT NULL COMMENT '登录/登出时间',
                                    `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
                                    PRIMARY KEY (`id`) USING BTREE,
                                    INDEX `username`(`username`) USING BTREE,
                                    INDEX `status`(`status`) USING BTREE,
                                    INDEX `create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '登陆日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `admin_operation_log`;
CREATE TABLE `admin_operation_log`  (
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
                                        `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
                                        PRIMARY KEY (`id`) USING BTREE,
                                        INDEX `operator`(`operator`) USING BTREE,
                                        INDEX `uri`(`uri`) USING BTREE,
                                        INDEX `status`(`status`) USING BTREE,
                                        INDEX `create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '操作日志' ROW_FORMAT = Dynamic;

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
                                        `create_by` int(1) NULL DEFAULT NULL COMMENT '创建人',
                                        `create_time` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
                                        `update_time` datetime(3) NULL DEFAULT NULL COMMENT '更新时间',
                                        PRIMARY KEY (`id`) USING BTREE,
                                        INDEX `idx_status`(`status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '公告信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for notify_user_announcement
-- ----------------------------
DROP TABLE IF EXISTS `notify_user_announcement`;
CREATE TABLE `notify_user_announcement`  (
                                             `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                             `announcement_id` bigint(20) NULL DEFAULT NULL COMMENT '公告id',
                                             `user_id` int(11) NULL DEFAULT NULL COMMENT '用户ID',
                                             `state` tinyint(1) NULL DEFAULT NULL COMMENT '状态，已读(1)|未读(0)',
                                             `read_time` datetime(0) NULL DEFAULT NULL COMMENT '阅读时间',
                                             `create_time` datetime(0) NULL DEFAULT NULL COMMENT '拉取时间',
                                             PRIMARY KEY (`id`) USING BTREE,
                                             UNIQUE INDEX `uk_user_id_anno_id`(`user_id`, `announcement_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户公告表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'OAuth客户端配置' ROW_FORMAT = Dynamic;

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
                               `description` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
                               `deleted` bigint(20) NULL DEFAULT NULL COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间',
                               `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
                               `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
                               PRIMARY KEY (`id`) USING BTREE,
                               UNIQUE INDEX `uk_conf_key_deleted`(`conf_key`, `deleted`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '基础配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, '网站弹窗开关', 'site_popup', '0', 'group', '宣传网站是否弹出框的控制开关。\n1：开启 0：关闭', 0, '2020-07-03 15:24:44', '2019-10-15 16:45:55');

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
                             `value_type` tinyint(1) NULL DEFAULT 0 COMMENT '值类型,1:Number 2:String 3:Boolean',
                             `hash_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'hash值，当字典项被修改时变更',
                             `deleted` bigint(20) NULL DEFAULT NULL COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间',
                             `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                             `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                             PRIMARY KEY (`id`) USING BTREE,
                             UNIQUE INDEX `uk_code_deleted`(`code`, `deleted`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES (1, 'dict_property', '字典属性', '只读、可写', 1, 1, '0226999fa7a64f8c9c36309ab68889bd', 0, '2020-03-27 01:05:29', '2020-07-03 14:30:03');
INSERT INTO `sys_dict` VALUES (2, 'log_status', '日志状态', '正常、异常', 1, 1, 'd63783be5ae845a9905fc5c69e796837', 0, '2020-03-27 01:05:29', '2020-03-27 11:06:44');
INSERT INTO `sys_dict` VALUES (3, 'gender', '性别', '用户性别', 1, 1, 'aca1caf123123e4872be29c8cc448', 0, '2020-03-27 01:05:29', '2020-03-27 00:55:28');
INSERT INTO `sys_dict` VALUES (4, 'grant_types', '授权类型', 'OAuth授权类型', 1, 1, 'e5316daadb490e9ca7e1ac5c5607a4', 0, '2020-03-27 01:05:29', '2020-03-27 00:30:16');
INSERT INTO `sys_dict` VALUES (5, 'operation_type', '操作类型', '操作日志的操作类型', 0, 1, '360bb77640dd4b109d58c094163c60b8', 0, '2020-07-14 20:28:54', NULL);
INSERT INTO `sys_dict` VALUES (6, 'role_type', '角色类型', '系统角色、业务角色', 0, 1, '53f3fb8c715149fe8793be4c25127ce9', 0, '2020-07-14 21:16:45', NULL);
INSERT INTO `sys_dict` VALUES (7, 'dict_value_type', '字典数据类型', 'Number、String、Boolean', 1, 1, '886c8965bdaa4c1e91ffcd5fb20ea84f', 0, '2020-08-14 17:16:47', NULL);
INSERT INTO `sys_dict` VALUES (8, 'login_event_type', '登陆事件类型', '1：登陆  2：登出', 0, 1, '6fe465274208421eb0619a516875e270', 0, '2020-09-17 14:44:00', NULL);
INSERT INTO `sys_dict` VALUES (9, 'tf', '是否', NULL, 1, 1, 'b409e633384f495c81ed5f926cbaacfc', 0, '2020-12-16 14:36:28', NULL);
INSERT INTO `sys_dict` VALUES (10, 'lov_http_method', 'lov模块请求方式', NULL, 1, 2, '794c8a10b8ee4cff9254ad7bfd02bc29', 0, '2020-12-16 14:36:28', '2021-01-07 15:09:57');
INSERT INTO `sys_dict` VALUES (11, 'lov_http_params_position', 'lov模块请求参数位置', NULL, 1, 2, '639a7dbc115b4a81829cefc0d26f6dbf', 0, '2020-12-16 14:36:28', '2021-01-07 14:53:25');
INSERT INTO `sys_dict` VALUES (12, 'lov_search_tag', 'lov模块搜索组件标签', NULL, 1, 2, 'ce839c05eafb4790a63883f98181ec1c', 0, '2020-12-16 14:36:28', NULL);
INSERT INTO `sys_dict` VALUES (13, 'user_type', '用户类型', '用户类型，1：系统用户', 1, 1, 'd7feef85cbee4da7a089eabccd6064bd', 0, '2020-12-16 13:44:37', '2020-12-16 13:54:10');
INSERT INTO `sys_dict` VALUES (14, 'recipient_filter_type', '消息接收人筛选方式', '接收人筛选方式，1：全部 2：用户角色 3：组织机构 4：用户类型 5：自定义用户', 1, 1, 'd76c2327edd74a18990aebaece8e1ea1', 0, '2020-12-15 17:36:24', NULL);
INSERT INTO `sys_dict` VALUES (15, 'notify_channel', '通知渠道', '通知渠道', 1, 1, 'a2463171291b4a949d2b9d2d3dfff4bc', 0, '2020-12-16 15:37:36', '2021-01-07 23:16:25');

-- ----------------------------
-- Table structure for sys_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_item`;
CREATE TABLE `sys_dict_item`  (
                                  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                  `dict_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典Code',
                                  `value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据值',
                                  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签',
                                  `attributes` json NULL COMMENT '附加属性',
                                  `sort` int(10) NOT NULL DEFAULT 0 COMMENT '排序（升序）',
                                  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                                  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间',
                                  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  INDEX `idx_dict_code`(`dict_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典项' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_item
-- ----------------------------
INSERT INTO `sys_dict_item` VALUES (1, 'dict_property', '0', '只读', '{\"tagColor\": \"orange\"}', 0, '只读字典，不可编辑', 0, '2020-03-27 01:05:52', '2020-09-16 15:31:24');
INSERT INTO `sys_dict_item` VALUES (2, 'dict_property', '1', '可写', '{\"tagColor\": \"green\"}', 1, '该字典可以编辑', 0, '2020-03-27 01:05:52', '2020-09-16 15:31:51');
INSERT INTO `sys_dict_item` VALUES (3, 'log_status', '1', '正常', '{\"textColor\": \"#34890A\"}', 0, '', 0, '2020-03-27 01:05:52', '2020-09-17 14:41:13');
INSERT INTO `sys_dict_item` VALUES (4, 'log_status', '0', '异常', '{\"textColor\": \"red\"}', 1, '', 0, '2020-03-27 01:05:52', '2019-03-25 12:49:13');
INSERT INTO `sys_dict_item` VALUES (5, 'gender', '1', '男', '{}', 0, '', 0, '2020-03-27 01:05:52', '2019-03-27 13:45:13');
INSERT INTO `sys_dict_item` VALUES (6, 'gender', '2', '女', '{}', 1, '', 0, '2020-03-27 01:05:52', '2019-03-27 13:45:34');
INSERT INTO `sys_dict_item` VALUES (7, 'gender', '3', '未知', NULL, 2, '', 0, '2020-03-27 01:05:52', '2019-03-27 13:45:57');
INSERT INTO `sys_dict_item` VALUES (8, 'grant_types', 'password', '密码模式', NULL, 0, NULL, 0, '2020-03-27 01:05:52', '2019-08-13 07:35:28');
INSERT INTO `sys_dict_item` VALUES (9, 'grant_types', 'authorization_code', '授权码模式', NULL, 1, NULL, 0, '2020-03-27 01:05:52', '2019-08-13 07:36:07');
INSERT INTO `sys_dict_item` VALUES (10, 'grant_types', 'client_credentials', '客户端模式', NULL, 2, NULL, 0, '2020-03-27 01:05:52', '2019-08-13 07:36:30');
INSERT INTO `sys_dict_item` VALUES (11, 'grant_types', 'refresh_token', '刷新模式', NULL, 3, NULL, 0, '2020-03-27 01:05:52', '2019-08-13 07:36:54');
INSERT INTO `sys_dict_item` VALUES (12, 'grant_types', 'implicit', '简化模式', NULL, 4, NULL, 0, '2020-03-27 01:05:52', '2019-08-13 07:39:32');
INSERT INTO `sys_dict_item` VALUES (13, 'login_event_type', '1', '登陆', '{\"tagColor\": \"cyan\"}', 0, '', 0, '2020-03-27 01:05:52', '2019-03-25 12:49:18');
INSERT INTO `sys_dict_item` VALUES (14, 'login_event_type', '2', '登出', '{\"tagColor\": \"pink\"}', 1, '', 0, '2020-03-27 01:05:52', '2019-03-25 12:49:13');
INSERT INTO `sys_dict_item` VALUES (15, 'operation_type', '3', '查看', '{\"tagColor\": \"purple\"}', 2, '', 0, '2020-03-27 01:05:52', '2019-03-25 12:49:13');
INSERT INTO `sys_dict_item` VALUES (16, 'operation_type', '4', '新建', '{\"tagColor\": \"cyan\"}', 3, '', 0, '2020-03-27 01:05:52', '2019-03-25 12:49:13');
INSERT INTO `sys_dict_item` VALUES (17, 'operation_type', '5', '修改', '{\"tagColor\": \"orange\"}', 4, '', 0, '2020-03-27 01:05:52', '2019-03-25 12:49:13');
INSERT INTO `sys_dict_item` VALUES (18, 'operation_type', '6', '删除', '{\"tagColor\": \"pink\"}', 5, '', 0, '2020-03-27 01:05:52', '2019-03-25 12:49:13');
INSERT INTO `sys_dict_item` VALUES (19, 'role_type', '1', '系统', '{\"tagColor\": \"orange\"}', 1, '系统角色不能删除', 0, '2020-07-14 21:17:07', NULL);
INSERT INTO `sys_dict_item` VALUES (20, 'role_type', '2', '业务', '{\"tagColor\": \"green\"}', 2, '业务角色可读可写', 0, '2020-07-14 21:17:24', NULL);
INSERT INTO `sys_dict_item` VALUES (21, 'dict_type', '1', 'Number', NULL, 1, NULL, 0, '2020-08-12 16:10:22', '2020-08-12 16:12:33');
INSERT INTO `sys_dict_item` VALUES (22, 'dict_type', '2', 'String', NULL, 1, NULL, 0, '2020-08-12 16:10:31', '2020-08-12 16:12:27');
INSERT INTO `sys_dict_item` VALUES (23, 'dict_type', '3', 'Boolean', NULL, 1, NULL, 0, '2020-08-12 16:10:38', '2020-08-12 16:12:23');
INSERT INTO `sys_dict_item` VALUES (24, 'dict_value_type', '1', 'Number', NULL, 1, NULL, 0, '2020-08-12 16:10:22', '2020-08-12 16:12:33');
INSERT INTO `sys_dict_item` VALUES (25, 'dict_value_type', '2', 'String', NULL, 1, NULL, 0, '2020-08-12 16:10:31', '2020-08-12 16:12:27');
INSERT INTO `sys_dict_item` VALUES (26, 'dict_value_type', '3', 'Boolean', '{}', 1, NULL, 0, '2020-08-12 16:10:38', '2020-10-20 15:05:53');
INSERT INTO `sys_dict_item` VALUES (27, 'tf', '1', '是', NULL, 1, NULL, 0, '2020-07-22 20:03:57', '2020-07-22 20:10:09');
INSERT INTO `sys_dict_item` VALUES (28, 'tf', '0', '否', NULL, 1, NULL, 0, '2020-07-22 20:04:16', '2020-07-22 20:10:06');
INSERT INTO `sys_dict_item` VALUES (29, 'lov_http_method', 'GET', 'GET', NULL, 1, NULL, 0, '2020-12-16 14:36:28', NULL);
INSERT INTO `sys_dict_item` VALUES (30, 'lov_http_method', 'HEAD', 'HEAD', NULL, 1, NULL, 1, '2020-12-16 14:36:28', NULL);
INSERT INTO `sys_dict_item` VALUES (31, 'lov_http_method', 'POST', 'POST', NULL, 1, NULL, 0, '2020-12-16 14:36:28', NULL);
INSERT INTO `sys_dict_item` VALUES (32, 'lov_http_method', 'PUT', 'PUT', NULL, 1, NULL, 1, '2020-12-16 14:36:28', NULL);
INSERT INTO `sys_dict_item` VALUES (33, 'lov_http_method', 'PATCH', 'PATCH', NULL, 1, NULL, 1, '2020-12-16 14:36:28', NULL);
INSERT INTO `sys_dict_item` VALUES (34, 'lov_http_method', 'DELETE', 'DELETE', NULL, 1, NULL, 1, '2020-12-16 14:36:28', NULL);
INSERT INTO `sys_dict_item` VALUES (35, 'lov_http_method', 'OPTIONS', 'OPTIONS', NULL, 1, NULL, 1, '2020-12-16 14:36:28', NULL);
INSERT INTO `sys_dict_item` VALUES (36, 'lov_http_method', 'TRACE', 'TRACE', NULL, 1, NULL, 1, '2020-12-16 14:36:28', NULL);
INSERT INTO `sys_dict_item` VALUES (37, 'lov_http_method', 'HEAD', 'HEAD', NULL, 1, NULL, 0, '2020-12-16 14:36:28', NULL);
INSERT INTO `sys_dict_item` VALUES (38, 'lov_http_method', 'PUT', 'PUT', NULL, 1, NULL, 0, '2020-12-16 14:36:28', NULL);
INSERT INTO `sys_dict_item` VALUES (39, 'lov_http_method', 'PATCH', 'PATCH', NULL, 1, NULL, 0, '2020-12-16 14:36:28', NULL);
INSERT INTO `sys_dict_item` VALUES (40, 'lov_http_method', 'DELETE', 'DELETE', NULL, 1, NULL, 0, '2020-12-16 14:36:28', NULL);
INSERT INTO `sys_dict_item` VALUES (41, 'lov_http_method', 'OPTIONS', 'OPTIONS', NULL, 1, NULL, 0, '2020-12-16 14:36:28', NULL);
INSERT INTO `sys_dict_item` VALUES (42, 'lov_http_method', 'TRACE', 'TRACE', NULL, 1, NULL, 0, '2020-12-16 14:36:28', NULL);
INSERT INTO `sys_dict_item` VALUES (43, 'lov_http_params_position', 'DATA', 'DATA', NULL, 1, NULL, 0, '2020-12-16 14:36:28', NULL);
INSERT INTO `sys_dict_item` VALUES (44, 'lov_http_params_position', 'PARAMS', 'PARAMS', NULL, 1, NULL, 0, '2020-12-16 14:36:28', NULL);
INSERT INTO `sys_dict_item` VALUES (45, 'lov_search_tag', 'INPUT_TEXT', 'INPUT_TEXT', NULL, 1, NULL, 0, '2020-12-16 14:36:28', NULL);
INSERT INTO `sys_dict_item` VALUES (46, 'lov_search_tag', 'INPUT_NUMBER', 'INPUT_NUMBER', NULL, 1, NULL, 0, '2020-12-16 14:36:28', NULL);
INSERT INTO `sys_dict_item` VALUES (47, 'lov_search_tag', 'SELECT', 'SELECT', NULL, 1, NULL, 0, '2020-12-16 14:36:28', NULL);
INSERT INTO `sys_dict_item` VALUES (48, 'lov_search_tag', 'DICT_SELECT', 'DICT_SELECT', NULL, 1, NULL, 0, '2020-12-16 14:36:28', NULL);
INSERT INTO `sys_dict_item` VALUES (49, 'user_type', '1', '系统用户', '{}', 1, NULL, 0, '2020-12-16 13:45:19', NULL);
INSERT INTO `sys_dict_item` VALUES (50, 'recipient_filter_type', '1', '全部', '{}', 1, '不筛选，对全部用户发送', 0, '2020-12-15 17:37:30', NULL);
INSERT INTO `sys_dict_item` VALUES (51, 'recipient_filter_type', '2', '指定角色', '{}', 2, '筛选拥有指定角色的用户', 0, '2020-12-15 17:38:54', '2020-12-16 13:35:03');
INSERT INTO `sys_dict_item` VALUES (52, 'recipient_filter_type', '3', '指定组织', '{}', 3, '筛选指定组织的用户', 0, '2020-12-15 17:39:19', '2020-12-16 13:35:09');
INSERT INTO `sys_dict_item` VALUES (53, 'recipient_filter_type', '4', '指定类型', '{}', 4, '筛选指定用户类型的用户', 0, '2020-12-15 17:39:50', '2020-12-16 13:35:16');
INSERT INTO `sys_dict_item` VALUES (54, 'recipient_filter_type', '5', '指定用户', '{}', 5, '指定用户发送', 0, '2020-12-15 17:40:06', '2020-12-21 21:52:43');
INSERT INTO `sys_dict_item` VALUES (55, 'notify_channel', '1', '站内', '{}', 1, NULL, 0, '2020-12-16 15:37:53', '2021-01-05 21:42:52');
INSERT INTO `sys_dict_item` VALUES (56, 'notify_channel', '2', '短信', '{}', 2, NULL, 0, '2020-12-16 15:38:08', NULL);
INSERT INTO `sys_dict_item` VALUES (57, 'notify_channel', '3', '邮箱', '{}', 3, NULL, 0, '2020-12-16 15:38:20', NULL);
INSERT INTO `sys_dict_item` VALUES (58, 'notify_channel', '4', '钉钉', '{}', 4, NULL, 20201221155643, '2020-12-16 15:38:28', NULL);

-- ----------------------------
-- Table structure for sys_lov
-- ----------------------------
DROP TABLE IF EXISTS `sys_lov`;
CREATE TABLE `sys_lov`  (
                            `id` int(11) NOT NULL AUTO_INCREMENT,
                            `keyword` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关键字，唯一，加载lov数据时通过关键字加载',
                            `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '标题',
                            `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '获取数据时请求路径',
                            `method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'http请求方式',
                            `position` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'http请求参数设置位置',
                            `key` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据的key',
                            `fixed_params` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '{}' COMMENT '固定请求参数，请设置 jsonString, 默认值 {}',
                            `multiple` bit(1) NULL DEFAULT NULL COMMENT '是否需要多选',
                            `ret` bit(1) NULL DEFAULT NULL COMMENT '是否需要返回数据, false则不会有确定按钮',
                            `ret_field` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '返回数据的字段',
                            `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                            `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',

                            PRIMARY KEY (`id`) USING BTREE,
                            UNIQUE INDEX `keyword`(`keyword`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'lov' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_lov
-- ----------------------------
INSERT INTO `sys_lov` VALUES (1, 'lov_user', '用户', '/sysuser/page', 'GET', 'PARAMS', 'userId', '{}', b'1', b'1', 'userId', '2020-12-16 14:45:40', '2020-12-16 14:47:59');

-- ----------------------------
-- Table structure for sys_lov_body
-- ----------------------------
DROP TABLE IF EXISTS `sys_lov_body`;
CREATE TABLE `sys_lov_body`  (
                                 `id` int(11) NOT NULL AUTO_INCREMENT,
                                 `keyword` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关键字，唯一，通过关键字关联lov',
                                 `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
                                 `field` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字段, 同一lov下，field不可重复`',
                                 `index` int(255) NULL DEFAULT NULL COMMENT '索引，字段排序',
                                 `property` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '{}' COMMENT '自定义属性，请设置 jsonString, 默认值 {}',
                                 `custom` bit(1) NULL DEFAULT NULL COMMENT '是否自定义html',
                                 `html` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '如果 custom=true 则当前值不能为空',
                                 `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                 PRIMARY KEY (`id`) USING BTREE,
                                 UNIQUE INDEX `keyword`(`keyword`, `field`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'lov body' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_lov_body
-- ----------------------------
INSERT INTO `sys_lov_body` VALUES (1, 'lov_user', '用户名', 'username', 1, '{\n\n}', b'0', NULL, '2020-12-16 14:45:40');
INSERT INTO `sys_lov_body` VALUES (2, 'lov_user', '昵称', 'nickname', 2, '{\n\n}', b'0', NULL, '2020-12-16 14:45:40');
INSERT INTO `sys_lov_body` VALUES (3, 'lov_user', '组织', 'organizationName', 3, '{\n\n}', b'0', NULL, '2020-12-16 14:45:40');

-- ----------------------------
-- Table structure for sys_lov_search
-- ----------------------------
DROP TABLE IF EXISTS `sys_lov_search`;
CREATE TABLE `sys_lov_search`  (
                                   `id` int(11) NOT NULL AUTO_INCREMENT,
                                   `keyword` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关键字，唯一，通过关键字关联lov',
                                   `label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签文字',
                                   `field` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字段',
                                   `placeholder` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'placeholder',
                                   `tag` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'html 标签',
                                   `options` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'tag=SELECT时的选项',
                                   `min` int(1) NULL DEFAULT NULL COMMENT 'tag=INPUT_NUMBER时的选项，设置数字最小值',
                                   `max` int(1) NULL DEFAULT NULL COMMENT 'tag=INPUT_NUMBER时的选项，设置数字最大值',
                                   `dict_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'tag=DICT_SELECT时的选项，设置dict-code',
                                   `custom` bit(1) NULL DEFAULT NULL COMMENT '是否自定义html',
                                   `html` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '如果 custom=true 则当前值不能为空',
                                   `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                   PRIMARY KEY (`id`) USING BTREE,
                                   UNIQUE INDEX `keyword`(`keyword`, `field`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'lov search' ROW_FORMAT = Dynamic;

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
                                     `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述信息',
                                     `sort` int(1) NULL DEFAULT 1 COMMENT '排序字段，由小到大',
                                     `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
                                     `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改者',
                                     `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                     `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                                     PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '组织架构' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_organization
-- ----------------------------
INSERT INTO `sys_organization` VALUES (6, '高大上公司', 0, '0', 4, '一个神秘的组织', 1, NULL, NULL, '2020-09-24 00:11:14', '2020-11-19 10:08:29');
INSERT INTO `sys_organization` VALUES (7, '产品研发部', 6, '0-6', 5, '一个🐂皮的部门', 1, NULL, NULL, '2020-09-24 00:48:07', '2020-09-24 15:54:03');
INSERT INTO `sys_organization` VALUES (8, 'java开发一组', 7, '0-7', 2, NULL, 1, NULL, NULL, '2020-09-24 00:50:34', NULL);
INSERT INTO `sys_organization` VALUES (9, 'Java开发二组', 7, '0-7', 2, NULL, 2, NULL, NULL, '2020-09-24 00:50:57', NULL);
INSERT INTO `sys_organization` VALUES (10, '谷歌', 0, '0', 1, NULL, 1, NULL, NULL, '2020-09-24 00:51:55', '2020-11-19 10:08:42');
INSERT INTO `sys_organization` VALUES (11, '不会Ollie', 10, '0-10', 0, NULL, 1, NULL, NULL, '2020-09-24 14:30:11', NULL);
INSERT INTO `sys_organization` VALUES (12, 'treflip高手', 10, '0-10', 2, NULL, 2, NULL, NULL, '2020-09-24 18:11:27', NULL);
INSERT INTO `sys_organization` VALUES (13, 'impossible', 10, '0-10', 2, NULL, 2, NULL, NULL, '2020-09-24 18:11:53', NULL);

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
                                   `deleted` bigint(20) NULL DEFAULT NULL COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间',
                                   `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                   `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                                   PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 990501 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单权限' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES (10028, '个人页', NULL, NULL, 'account', 'layouts/RouteView', '/account/center', NULL, 0, 'user', 0, 0, 1, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES (10029, '个人中心', NULL, NULL, 'center', 'account/center/Index', NULL, NULL, 10028, NULL, 1, 0, 1, 1, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES (10030, '个人设置', NULL, NULL, 'settings', 'account/settings/Index', '/account/settings/base', NULL, 10028, NULL, 1, 0, 1, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES (10031, '基本设置', NULL, '/account/settings/base', 'BaseSettings', 'account/settings/BaseSetting', NULL, NULL, 10030, NULL, 1, 0, 0, 1, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES (10032, '安全设置', NULL, '/account/settings/security', 'SecuritySettings', 'account/settings/Security', NULL, NULL, 10030, NULL, 1, 0, 0, 1, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES (10033, '个性化设置', NULL, '/account/settings/custom', 'CustomSettings', 'account/settings/Custom', NULL, NULL, 10030, NULL, 1, 0, 0, 1, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES (10034, '账户绑定', NULL, '/account/settings/binding', 'BindingSettings', 'account/settings/Binding', NULL, NULL, 10030, NULL, 1, 0, 0, 1, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES (10035, '新消息通知', NULL, '/account/settings/notification', 'NotificationSettings', 'account/settings/Notification', NULL, NULL, 10030, NULL, 1, 0, 0, 1, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES (100000, '系统管理', NULL, '', 'sys', 'layouts/RouteView', '/sys/sysuser', NULL, 0, 'setting', 1, 0, 0, 0, 0, NULL, '2020-12-15 16:50:32');
INSERT INTO `sys_permission` VALUES (100100, '系统用户', NULL, '/sys/sysuser', 'sysuser', 'sys/sysuser/SysUserPage', NULL, NULL, 100000, NULL, 1, 0, 0, 1, 0, NULL, '2020-12-15 16:51:42');
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
INSERT INTO `sys_permission` VALUES (100400, '配置信息', NULL, '/sys/config', 'sysConfig', 'sys/config/SysConfigPage', NULL, NULL, 100000, NULL, 5, 0, 0, 1, 0, NULL, '2020-06-04 00:45:27');
INSERT INTO `sys_permission` VALUES (100401, '配置查询', 'sys:config:read', NULL, NULL, NULL, NULL, NULL, 100400, NULL, 0, 0, 0, 2, 0, '2019-10-13 22:00:24', '2020-06-04 00:46:03');
INSERT INTO `sys_permission` VALUES (100402, '配置新增', 'sys:config:add', NULL, NULL, NULL, NULL, NULL, 100400, NULL, 1, 0, 0, 2, 0, '2019-10-13 22:00:24', '2020-06-04 00:46:13');
INSERT INTO `sys_permission` VALUES (100403, '配置修改', 'sys:config:edit', NULL, NULL, NULL, NULL, NULL, 100400, NULL, 2, 0, 0, 2, 0, '2019-10-13 22:00:24', '2020-06-04 00:46:22');
INSERT INTO `sys_permission` VALUES (100404, '配置删除', 'sys:config:del', NULL, NULL, NULL, NULL, NULL, 100400, NULL, 3, 0, 0, 2, 0, '2019-10-13 22:00:24', '2020-06-04 00:46:31');
INSERT INTO `sys_permission` VALUES (100500, '字典管理', NULL, '/sys/dict', 'sysDict', 'sys/dict/SysDictPage', NULL, NULL, 100000, NULL, 4, 0, 0, 1, 0, NULL, '2019-10-13 22:00:24');
INSERT INTO `sys_permission` VALUES (100501, '字典查询', 'sys:dict:read', NULL, NULL, NULL, NULL, NULL, 100500, NULL, 0, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100502, '字典新增', 'sys:dict:add', NULL, NULL, NULL, NULL, NULL, 100500, NULL, 1, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100503, '字典修改', 'sys:dict:edit', NULL, NULL, NULL, NULL, NULL, 100500, NULL, 2, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100504, '字典删除', 'sys:dict:del', NULL, NULL, NULL, NULL, NULL, 100500, NULL, 3, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100600, 'lov', NULL, '/sys/lov', 'Lov', 'sys/lov/Lov', NULL, NULL, 100000, NULL, 6, 0, 0, 1, 0, NULL, '2020-08-27 21:36:18');
INSERT INTO `sys_permission` VALUES (100601, 'lov查询', 'sys:lov:read', NULL, NULL, NULL, NULL, NULL, 100600, NULL, 0, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100602, 'lov新增', 'sys:lov:add', NULL, NULL, NULL, NULL, NULL, 100600, NULL, 1, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100603, 'lov修改', 'sys:lov:edit', NULL, NULL, NULL, NULL, NULL, 100600, NULL, 2, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100604, 'lov删除', 'sys:lov:del', NULL, NULL, NULL, NULL, NULL, 100600, NULL, 3, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100700, '组织架构', NULL, '/sys/organization', 'organization', 'sys/organization/OrganizationPage', NULL, NULL, 100000, NULL, 1, 0, 0, 1, 0, NULL, '2019-10-13 22:00:24');
INSERT INTO `sys_permission` VALUES (100701, '组织架构查询', 'sys:organization:read', NULL, NULL, NULL, NULL, NULL, 100700, NULL, 0, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100702, '组织架构新增', 'sys:organization:add', NULL, NULL, NULL, NULL, NULL, 100700, NULL, 1, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100703, '组织架构修改', 'sys:organization:edit', NULL, NULL, NULL, NULL, NULL, 100700, NULL, 2, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (100704, '组织架构删除', 'sys:organization:del', NULL, NULL, NULL, NULL, NULL, 100700, NULL, 3, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (110000, '日志管理', NULL, '', 'log', 'layouts/RouteView', '/log/adminoperationlog', NULL, 0, 'file-search', 2, 0, 0, 0, 0, NULL, '2020-12-15 16:50:16');
INSERT INTO `sys_permission` VALUES (110100, '操作日志', NULL, '/log/adminoperationlog', 'adminOperationLog', 'log/adminoperationlog/AdminOperationLogPage', NULL, NULL, 110000, NULL, 2, 0, 0, 1, 0, NULL, '2020-09-17 01:50:47');
INSERT INTO `sys_permission` VALUES (110101, '操作日志查询', 'log:adminoperationlog:read', NULL, NULL, NULL, NULL, NULL, 110100, NULL, 0, 0, 0, 2, 0, '2019-10-13 22:00:24', '2019-10-15 14:14:03');
INSERT INTO `sys_permission` VALUES (110200, '登陆日志', NULL, '/log/adminloginlog', 'adminLoginLog', 'log/adminloginlog/AdminLoginLogPage', NULL, NULL, 110000, NULL, 1, 0, 0, 1, 0, NULL, '2019-10-13 22:00:24');
INSERT INTO `sys_permission` VALUES (110201, '登陆日志查询', 'log:adminloginlog:read', NULL, NULL, NULL, NULL, NULL, 110200, NULL, 0, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (110300, '访问日志(后台)', NULL, '/log/adminaccesslog', 'adminAccessLog', 'log/adminaccesslog/AdminAccessLogPage', NULL, NULL, 110000, NULL, 3, 0, 0, 1, 0, NULL, '2020-09-17 01:50:38');
INSERT INTO `sys_permission` VALUES (110301, '访问日志(后台)查询', 'log:adminaccesslog:read', NULL, '', NULL, NULL, NULL, 110300, NULL, 0, 0, 0, 2, 0, '2019-10-13 22:00:24', '2019-10-15 14:14:03');
INSERT INTO `sys_permission` VALUES (120000, '消息通知', NULL, NULL, 'notify', 'layouts/RouteView', NULL, NULL, 0, 'message', 3, 0, 0, 0, 0, '2020-12-15 16:47:53', NULL);
INSERT INTO `sys_permission` VALUES (120100, '公告信息', NULL, '/notify/announcement', 'announcement', 'notify/announcement/AnnouncementPage', NULL, NULL, 120000, NULL, 1, 0, 0, 1, 0, NULL, '2019-10-13 22:00:24');
INSERT INTO `sys_permission` VALUES (120101, '公告信息查询', 'notify:announcement:read', NULL, NULL, NULL, NULL, NULL, 120100, NULL, 0, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (120102, '公告信息新增', 'notify:announcement:add', NULL, NULL, NULL, NULL, NULL, 120100, NULL, 1, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (120103, '公告信息修改', 'notify:announcement:edit', NULL, NULL, NULL, NULL, NULL, 120100, NULL, 2, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (120104, '公告信息删除', 'notify:announcement:del', NULL, NULL, NULL, NULL, NULL, 120100, NULL, 3, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (120200, '用户公告', NULL, '/notify/userannouncement', 'userAnnouncement', 'notify/userannouncement/UserAnnouncementPage', NULL, NULL, 120000, NULL, 1, 0, 1, 1, 0, NULL, '2020-12-26 19:00:35');
INSERT INTO `sys_permission` VALUES (120201, '用户公告表查询', 'notify:userannouncement:read', NULL, NULL, NULL, NULL, NULL, 120200, NULL, 0, 0, 0, 2, 0, '2019-10-13 22:00:24', NULL);
INSERT INTO `sys_permission` VALUES (990000, '开发平台', '', '', 'develop', 'layouts/RouteView', '', NULL, 0, 'desktop', 99, 0, 0, 0, 0, NULL, '2019-11-22 16:49:56');
INSERT INTO `sys_permission` VALUES (990100, '接口文档', '', '/develop/swagger', 'swagger', 'layouts/IframeView', '', '', 990000, 'file', 1, 0, 0, 1, 0, NULL, '2019-11-22 16:48:42');
INSERT INTO `sys_permission` VALUES (990200, '文档增强', '', '/develop/doc', 'doc', 'layouts/IframeView', '', '', 990000, 'file-text', 2, 0, 0, 1, 0, NULL, '2019-11-22 16:48:50');
INSERT INTO `sys_permission` VALUES (990300, '调度中心', '', 'http://ballcat-job:8888/xxl-job-admin', 'job', '', '', '_blank', 990000, 'rocket', 3, 0, 0, 1, 0, NULL, '2019-11-22 16:49:14');
INSERT INTO `sys_permission` VALUES (990400, '服务监控', '', 'http://ballcat-monitor:9999', 'monitor', '', '', '_blank', 990000, 'alert', 4, 0, 0, 1, 0, NULL, '2019-11-22 16:49:22');
INSERT INTO `sys_permission` VALUES (990500, '代码生成', '', 'http://localhost:7777', 'codegen', '', '', '_blank', 990000, 'printer', 5, 0, 0, 1, 0, NULL, '2019-11-22 16:49:35');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
                             `id` int(11) NOT NULL AUTO_INCREMENT,
                             `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                             `code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                             `type` tinyint(1) NULL DEFAULT 2 COMMENT '角色类型，1：系统角色 2：业务角色',
                             `scope_type` tinyint(1) NULL DEFAULT NULL COMMENT '数据权限：1全部，2本人，3本人及子部门，4本部门',
                             `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                             `deleted` bigint(20) NULL DEFAULT NULL COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间',
                             `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
                             `update_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
                             PRIMARY KEY (`id`) USING BTREE,
                             UNIQUE INDEX `uk_code_deleted`(`code`, `deleted`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '管理员', 'ROLE_ADMIN', 1, NULL, '管理员', 0, '2017-10-29 15:45:51', '2020-07-14 21:23:06');
INSERT INTO `sys_role` VALUES (2, '测试工程师', 'ROLE_TEST', 2, NULL, '测试工程师', 0, '2019-09-02 11:34:36', '2020-07-06 12:47:15');
INSERT INTO `sys_role` VALUES (14, '销售主管', 'ROLE_SALES_EXECUTIVE', 2, NULL, '销售主管', 0, '2020-02-27 15:10:36', '2020-07-06 12:47:14');
INSERT INTO `sys_role` VALUES (15, '销售专员', 'ROLE_SALESMAN', 2, NULL, '销售专员', 0, '2020-02-27 15:12:18', '2020-07-06 12:47:13');

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`  (
                                        `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                        `permission_id` int(11) NOT NULL COMMENT '菜单ID',
                                        `role_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'role code',
                                        PRIMARY KEY (`id`) USING BTREE,
                                        UNIQUE INDEX `role_code`(`role_code`, `permission_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 279 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES (212, 10028, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (211, 10029, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (214, 10030, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (213, 10031, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (215, 10032, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (216, 10033, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (217, 10034, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (218, 10035, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (221, 100000, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (220, 100100, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (219, 100101, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (222, 100102, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (223, 100103, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (224, 100104, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (225, 100105, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (226, 100106, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (233, 100200, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (232, 100201, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (234, 100202, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (235, 100203, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (236, 100204, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (237, 100205, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (239, 100300, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (238, 100301, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (240, 100302, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (241, 100303, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (242, 100304, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (249, 100400, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (248, 100401, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (250, 100402, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (251, 100403, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (252, 100404, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (244, 100500, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (243, 100501, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (245, 100502, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (246, 100503, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (247, 100504, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (254, 100600, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (253, 100601, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (255, 100602, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (256, 100603, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (257, 100604, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (228, 100700, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (227, 100701, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (229, 100702, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (230, 100703, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (231, 100704, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (260, 110000, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (262, 110100, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (261, 110101, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (259, 110200, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (258, 110201, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (264, 110300, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (263, 110301, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (278, 120000, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (266, 120100, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (265, 120101, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (267, 120102, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (268, 120103, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (269, 120104, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (277, 120200, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (276, 120201, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (271, 990000, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (270, 990100, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (272, 990200, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (273, 990300, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (274, 990400, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (275, 990500, 'ROLE_ADMIN');
INSERT INTO `sys_role_permission` VALUES (137, 10028, 'ROLE_SALES_EXECUTIVE');
INSERT INTO `sys_role_permission` VALUES (138, 10029, 'ROLE_SALES_EXECUTIVE');
INSERT INTO `sys_role_permission` VALUES (139, 10030, 'ROLE_SALES_EXECUTIVE');
INSERT INTO `sys_role_permission` VALUES (140, 10031, 'ROLE_SALES_EXECUTIVE');
INSERT INTO `sys_role_permission` VALUES (141, 10032, 'ROLE_SALES_EXECUTIVE');
INSERT INTO `sys_role_permission` VALUES (142, 10033, 'ROLE_SALES_EXECUTIVE');
INSERT INTO `sys_role_permission` VALUES (143, 10034, 'ROLE_SALES_EXECUTIVE');
INSERT INTO `sys_role_permission` VALUES (144, 10035, 'ROLE_SALES_EXECUTIVE');
INSERT INTO `sys_role_permission` VALUES (61, 10028, 'ROLE_TEST');
INSERT INTO `sys_role_permission` VALUES (62, 10029, 'ROLE_TEST');
INSERT INTO `sys_role_permission` VALUES (63, 10030, 'ROLE_TEST');
INSERT INTO `sys_role_permission` VALUES (64, 10031, 'ROLE_TEST');
INSERT INTO `sys_role_permission` VALUES (65, 10032, 'ROLE_TEST');
INSERT INTO `sys_role_permission` VALUES (66, 10033, 'ROLE_TEST');

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
                             `organization_id` int(11) NULL DEFAULT 0 COMMENT '所属组织ID',
                             `deleted` bigint(20) NULL DEFAULT NULL COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间',
                             `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                             `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                             PRIMARY KEY (`user_id`) USING BTREE,
                             UNIQUE INDEX `uk_username_deleted`(`username`, `deleted`) USING BTREE,
                             INDEX `idx_status`(`status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '超管牛逼', '$2a$10$YJDXeAsk7FjQQVTdutIat.rPR3p3uUPWmZyhtnRDOrIjPujOAUrla', NULL, 'sysuser/1/avatar/20200226/ab6bd5221afe4238ae4987f278758113.jpg', 1, 'chengbohua@foxmail.com', '15800000000', 1, 1, 6, 0, '2999-09-20 17:13:24', '2020-10-17 17:40:00');
INSERT INTO `sys_user` VALUES (10, 'test4', '测试用户213', '$2a$10$RpZQ8i7ke9ikT1AE8cQwfe3t0NoRmkL5pr1U9YNXn2O9YiToZjMTG', NULL, 'sysuser/10/avatar/20201204/002875d468db41239ee02ad99ab14490.jpg', 2, NULL, '12345678520', 0, 1, 6, 0, NULL, '2021-01-07 21:04:03');
INSERT INTO `sys_user` VALUES (12, 'test1', 'test1', '$2a$10$EotCw/oHyg1MgJMDFgEeeOO0/jVHZgIFn0jX9kq9SP9sIAXF2m0Yi', NULL, 'sysuser/12/avatar/20200109/05e189b252b44598b6d150ce3597d293.jpg', 1, 'test1@qq.com', '12356322365', 1, 1, 12, 20200609182117, '2019-10-18 20:40:57', NULL);
INSERT INTO `sys_user` VALUES (17, 'test2', 'test2', '$2a$10$YJDXeAsk7FjQQVTdutIat.rPR3p3uUPWmZyhtnRDOrIjPujOAUrla', NULL, NULL, 1, 'test2@qq.com', '123456789', 1, 1, 8, 0, NULL, '2020-07-06 12:09:08');

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, 'ROLE_ADMIN');
INSERT INTO `sys_user_role` VALUES (5, 17, 'ROLE_SALESMAN');
INSERT INTO `sys_user_role` VALUES (6, 10, 'ROLE_SALES_EXECUTIVE');
INSERT INTO `sys_user_role` VALUES (2, 18, 'ROLE_SALES_EXECUTIVE');
INSERT INTO `sys_user_role` VALUES (3, 19, 'ROLE_SALES_EXECUTIVE');
INSERT INTO `sys_user_role` VALUES (4, 1, 'ROLE_TEST');

SET FOREIGN_KEY_CHECKS = 1;
