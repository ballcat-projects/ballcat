CREATE TABLE `sys_datasource` (
                                  `id` bigint(64) NOT NULL,
                                  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '数据源名称',
                                  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '地址',
                                  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
                                  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
                                  `type` int(1) NOT NULL COMMENT '类型',
                                  `max_holds` tinyint(2) DEFAULT NULL,
                                  `extra` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '额外信息',
                                  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '描述',
                                  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='数据源表';

-- ----------------------------
-- Table structure for sys_tenant
-- ----------------------------
CREATE TABLE `sys_tenant` (
                              `tenant_id` bigint(64) NOT NULL COMMENT '主键',
                              `tenant_code` varchar(16) COLLATE utf8mb4_general_ci NOT NULL COMMENT '租户code',
                              `tenant_name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '租户名称',
                              `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '域名地址',
                              `duty` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '联系人',
                              `phone` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '联系电话',
                              `address` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '联系地址',
                              `logo` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'logo地址',
                              `status` tinyint(1) DEFAULT NULL COMMENT '状态(1-正常,0-冻结)',
                              `expire_time` date DEFAULT NULL COMMENT '创建时间',
                              `deleted` bigint(20) DEFAULT NULL COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间',
                              `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                              `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                              PRIMARY KEY (`tenant_id`) USING BTREE,
                              UNIQUE KEY `idx_tenant_code` (`tenant_code`),
                              UNIQUE KEY `idx_tenant_name` (`tenant_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='租户表';

-- ----------------------------
-- Table structure for sys_tenant_datasource
-- ----------------------------
CREATE TABLE `sys_tenant_datasource` (
                                         `id` bigint(64) NOT NULL,
                                         `tenant_id` bigint(64) NOT NULL,
                                         `datasource_id` bigint(64) NOT NULL,
                                         `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                         `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                         PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='租户数据源映射表';
