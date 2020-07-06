# Changelog

更新日志

## [Unreleased]

- Lov 支持


## [0.0.3] - 2020-07-06

 ### Added

- 重构代码生成器
  - 前端使用 ant-design-vue 重构，支持单体应用以及前后端分离两种部署方式
  - 多数据源支持，动态添加删除，生成时选择对应数据源进行代码生成
  - 代码生成结构调整，支持自定义代码生成结构
  - 支持在线模板编辑
  - 支持自定义模板属性，定制模板

- 提供 dingTalk-starter，简化 dingTalk 接入

- 提供 kafka 扩展，以及kafka流式处理扩展包

- 记录登陆登出日志

- 自动填充（逻辑删除标识），新建时增加对字段名为 `deleted` 且 类型为`Long`的属性填充，填充值为 0

- 超级管理员配置，可根据userid 或 username 指定当前项目的超级管理员（即最多可配置两个超级管理员），超级管理员默认拥有所有权限，不可删除，仅自己可以修改自己的信息
  如下配置：指定了 userId 为 1 或者 username 为 admin 的用户为超级管理员

    ```yaml
    ballcat:
        admin:
            rule: 
                userId: 1  
                username: admin
    ```

### Changed

- 访问日志忽略路径修改为可配置

- 角色新增类型属性，对于系统类型角色，不允许删除

- 更新逻辑删除不能使用 unique key 的问题，逻辑删除使用时间戳，未删除为0，删除则为删除的时间戳，实体类字段同一使用Long，数据库使用bigint。  

  - 配置文件添加如下配置：

    ```yaml
    mybatis-plus:
      global-config:
        db-config:
          logic-delete-value: "NOW()" # 逻辑已删除值(使用当前时间标识)
          logic-not-delete-value: 0 # 逻辑未删除值(默认为 0)
    ```

### Dependency

- mybatis-plus 版本升级至 3.3.2
- spring-boot 版本升级至 2.3.1.RELEASE
- spring-security-oauth2 升级至 2.3.8.RELEASE  

### SQL

```sql
# 逻辑删除字段修改为bigint
ALTER TABLE `ballcat`.`sys_user` 
MODIFY COLUMN `deleted` bigint(20) NULL DEFAULT NULL COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间' AFTER `type`;
ALTER TABLE `ballcat`.`sys_role` 
MODIFY COLUMN `deleted` bigint(20) NULL DEFAULT NULL COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间' AFTER `note`;
ALTER TABLE `ballcat`.`sys_permission` 
MODIFY COLUMN `deleted` bigint(20) NULL DEFAULT NULL COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间' AFTER `type`;
ALTER TABLE `ballcat`.`sys_dict` 
MODIFY COLUMN `deleted` bigint(20) NULL DEFAULT NULL COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间' AFTER `hash_code`;
ALTER TABLE `ballcat`.`sys_dict_item` 
MODIFY COLUMN `deleted` bigint(20) NULL DEFAULT NULL COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间' AFTER `remarks`;
ALTER TABLE `ballcat`.`sys_config` 
ADD COLUMN `deleted` bigint(20) NULL COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间' AFTER `description`;

# 系统角色新增类型字段
ALTER TABLE `ballcat`.`sys_role` 
ADD COLUMN `type` tinyint(1) NULL DEFAULT 2 COMMENT '角色类型，1：系统角色 2：业务角色' AFTER `code`;
```



 

## [0.0.2] 

### Added

- 更新系统配置表，重命名为 sys_config, 并调整包位置，并入sys模块下。

- 模块重构，调整部分common下的模块，修改放入starter

- 合并 simple-cache，后续直接引入`ballcat-spring-boot-starter-redis`模块，即可开启全局key前缀，以及缓存注解功能

- traceId跟踪，引入`ballcat-spring-boot-starter-log`，会自动为每个请求的日志上下文中注入TraceId

- operation_log、admin_access_log表新增字段 trace_id，类型char(24).

- logback-spring.xml 彩色日志模板中，加入`%clr([%X{traceId}]){faint}`，文件日志模板加入`[%X{traceId}]`，用于打印traceId

- 移除api_access_log表，以及相关代码

- 更新日志，追加追踪ID，操作类型

- core项目更新为自动装配，可以删除项目Application中的
  `@ServletComponentScan("com.hccake.ballcat.admin.oauth.filter")` 注解。

  `@MapperScan("com.hccake.ballcat.**.mapper")`
  `@ComponentScan("com.hccake.ballcat.admin")`
  注解上的这两个包扫描也可去掉。

### SQL

```sql
ALTER TABLE `admin_operation_log` 
ADD COLUMN `trace_id` char(24) NULL COMMENT '追踪ID' AFTER `id`,
MODIFY COLUMN `method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方式' AFTER `uri`,
ADD COLUMN `type` tinyint(1) NULL COMMENT '操作类型' AFTER `status`;

ALTER TABLE `admin_access_log` 
ADD COLUMN `trace_id` char(24) NULL COMMENT '追踪ID' AFTER `id`
```