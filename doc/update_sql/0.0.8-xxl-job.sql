-- xxl-job 升级到 2.3.0
ALTER TABLE `xxl_job_log_report`
    ADD COLUMN `update_time` datetime(0) NULL DEFAULT NULL AFTER `fail_count`;

ALTER TABLE `xxl_job_group`
    MODIFY COLUMN `address_list` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '执行器地址列表，多地址逗号分隔' AFTER `address_type`,
    ADD COLUMN `update_time` datetime(0) NULL DEFAULT NULL AFTER `address_list`;

-- 注意按顺序更新以下 sql
ALTER TABLE `xxl_job_info`
    ADD COLUMN `schedule_type` varchar(50) NULL DEFAULT 'NONE' COMMENT '调度类型' AFTER `alarm_email`,
ADD COLUMN `schedule_conf` varchar(128) NULL DEFAULT NULL COMMENT '调度配置，值含义取决于调度类型' AFTER `schedule_type`,
ADD COLUMN `misfire_strategy` varchar(50) NULL DEFAULT 'DO_NOTHING' COMMENT '调度过期策略' AFTER `schedule_conf`;

UPDATE `xxl_job_info`
SET `schedule_type` = 'CRON',
    `schedule_conf` = job_cron;

ALTER TABLE `xxl_job_info` DROP COLUMN `job_cron`;