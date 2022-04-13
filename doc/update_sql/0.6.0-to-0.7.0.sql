ALTER TABLE `log_operation_log`
ADD COLUMN `result` text COMMENT '操作结果' AFTER `params`;