ALTER TABLE `admin_operation_log`
ADD COLUMN `trace_id` char(24) NULL COMMENT '追踪ID' AFTER `id`,
MODIFY COLUMN `method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方式' AFTER `uri`,
ADD COLUMN `type` tinyint(1) NULL COMMENT '操作类型' AFTER `status`;

ALTER TABLE `admin_access_log`
ADD COLUMN `trace_id` char(24) NULL COMMENT '追踪ID' AFTER `id`