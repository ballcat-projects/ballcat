-- lov 模块
DROP TABLE IF EXISTS `sys_lov`;
CREATE TABLE `sys_lov`
(
    `id`                  int(11) NOT NULL AUTO_INCREMENT,
    `keyword`             varchar(50)  DEFAULT NULL COMMENT '关键字，唯一，加载lov数据时通过关键字加载',
    `url`                 varchar(255) DEFAULT NULL COMMENT '获取数据时请求路径',
    `method`              varchar(10)  DEFAULT NULL COMMENT 'http请求方式',
    `position`            varchar(10)  DEFAULT NULL COMMENT 'http请求参数设置位置',
    `key`                 varchar(20)  DEFAULT NULL COMMENT '数据的key',
    `fixed_params`        varchar(255) DEFAULT '{}' COMMENT '固定请求参数，请设置 jsonString, 默认值 {}',
    `multiple`            bit(1)       DEFAULT NULL COMMENT '是否需要多选',
    `search`              bit(1)       DEFAULT NULL COMMENT '是否需要搜索框',
    `ret`                 bit(1)       DEFAULT NULL COMMENT '是否需要返回数据, false则不会有确定按钮',
    `ret_field`           varchar(50)  DEFAULT NULL COMMENT '返回数据的字段',
    `ret_field_data_type` tinyint(1)   DEFAULT NULL COMMENT '返回字段数据类型 1 String 2 Number',
    `create_time`         datetime     DEFAULT NULL COMMENT '创建时间',
    `update_time`         datetime     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `keyword` (`keyword`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC COMMENT ='lov';

DROP TABLE IF EXISTS `sys_lov_body`;
CREATE TABLE `sys_lov_body`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT,
    `keyword`     varchar(50)  DEFAULT NULL COMMENT '关键字，唯一，通过关键字关联lov',
    `title`       varchar(100) DEFAULT NULL COMMENT '标题',
    `field`       varchar(50)  DEFAULT NULL COMMENT '字段, 同一lov下，field不可重复`',
    `index`       int(255)     DEFAULT NULL COMMENT '索引，字段排序',
    `property`    varchar(255) DEFAULT '{}' COMMENT '自定义属性，请设置 jsonString, 默认值 {}',
    `custom`      bit(1)       DEFAULT NULL COMMENT '是否自定义html',
    `html`        text COMMENT '如果 custom=true 则当前值不能为空',
    `create_time` datetime     DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY (`keyword`, `field`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC COMMENT ='lov body';

DROP TABLE IF EXISTS `sys_lov_search`;
CREATE TABLE `sys_lov_search`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT,
    `keyword`     varchar(50)  DEFAULT NULL COMMENT '关键字，唯一，通过关键字关联lov',
    `label`       varchar(100) DEFAULT NULL COMMENT '标签文字',
    `field`       varchar(50)  DEFAULT NULL COMMENT '字段',
    `placeholder` varchar(255) DEFAULT NULL COMMENT 'placeholder',
    `tag`         varchar(50)  DEFAULT NULL COMMENT 'html 标签',
    `options`     text COMMENT 'tag=SELECT时的选项',
    `min`         int(1)       DEFAULT NULL COMMENT 'tag=INPUT_NUMBER时的选项，设置数字最小值',
    `max`         int(1)       DEFAULT NULL COMMENT 'tag=INPUT_NUMBER时的选项，设置数字最大值',
    `dict_code`   varchar(50)  DEFAULT NULL COMMENT 'tag=DICT_SELECT时的选项，设置dict-code',
    `custom`      bit(1)       DEFAULT NULL COMMENT '是否自定义html',
    `html`        text COMMENT '如果 custom=true 则当前值不能为空',
    `create_time` datetime     DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY (`keyword`, `field`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC COMMENT ='lov search';

-- 角色数据权限字段
ALTER TABLE `sys_role`
    ADD COLUMN `scope_type` tinyint(1) NULL COMMENT '数据权限：1全部，2本人，3本人及子部门，4本部门' AFTER `update_time`;
-- 更新索引为唯一索引
ALTER TABLE `sys_role`
    DROP INDEX `uk_code_deleted`,
    ADD UNIQUE INDEX `uk_code_deleted` (`code`, `deleted`) USING BTREE;

-- 用户角色关联表更新
alter table sys_user_role drop primary key ;
alter table sys_user_role add column `id` bigint(20) NOT NULL  primary key  AUTO_INCREMENT FIRST;
alter table sys_user_role add column `role_code` varchar(64) comment 'role code';
alter table sys_user_role add unique (`role_code`,`user_id`);
update sys_user_role set `role_code`= (select `code` from sys_role where `id`=`role_id`) where role_code is null;
alter table sys_user_role modify column `role_code` varchar(64) not null comment 'role code';
alter table sys_user_role drop `role_id`;

-- 角色权限关联表更新
alter table sys_role_permission drop primary key ;
alter table sys_role_permission add column `id` bigint(20) NOT NULL  primary key  AUTO_INCREMENT FIRST;
alter table sys_role_permission add column `role_code` varchar(64) comment 'role code';
alter table sys_role_permission add unique (`role_code`,`permission_id`);
update sys_role_permission set `role_code`= (select `code` from sys_role where `id`=`role_id`) where role_code is null;
alter table sys_role_permission modify column `role_code` varchar(64) not null comment 'role code';
alter table sys_role_permission drop `role_id`;


-- 组织架构表
CREATE TABLE `sys_organization` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(50) DEFAULT NULL COMMENT '组织名称',
  `parent_id` int(11) DEFAULT '0' COMMENT '父级ID',
  `hierarchy` varchar(512) DEFAULT NULL COMMENT '层级信息，从根节点到当前节点的最短路径，使用-分割节点ID',
  `depth` int(1) DEFAULT NULL COMMENT '当前节点深度',
  `description` varchar(512) DEFAULT NULL COMMENT '描述信息',
  `sort` int(1) DEFAULT '1' COMMENT '排序字段，由小到大',
  `create_by` varchar(100) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(100) DEFAULT NULL COMMENT '修改者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='组织架构';

-- 系统用户表添加组织架构关联id
ALTER TABLE `sys_user`
ADD COLUMN `organization_id` int(11) NULL DEFAULT 0 COMMENT '所属组织ID' AFTER `type`;