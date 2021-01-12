# Changelog

更新日志

## [Unreleased]

- 全局数据校验支持

- OSS starter 修改使用 AWS S3



## [0.0.7]

### Added

- feat: 多页签导出支持每个页签不同头类型
- feat: 新增创建人和更新人的自动填充支持
- feat: 提供ExcelWriterBuilder，使用者可以复写此接口方法，来对excel导出做自定义处理
- feat: 数据权限注解 @DataPermission 提供方法级别的忽略支持
- add: 新增 HtmlUtil，方便快捷提取 html 中的纯文本，且保留换行结构
- feat: 代码编辑器的模板编辑框提供全屏功能
- feat:  新增用户成的发布事件
- feat:  新增登陆时的图形验证码校验，提升安全性
- feat:  新增虚拟货币的支付stater支持
- feat:  新增基于 websocket-starter，方便系统集成 websocket 使用
- feat: 新增系统公告，支持多种方式指定接收人，以及多种公告推送方式
- feat:  新增 admin-websocket 插件包，引入此依赖，可获得实时的站内公告推送以及字典项更新推送能力，默认使用redis发布订阅进行集群支持，用户可通过自定义 MessageDistributor 来更换消息分发模式，比如使用专业的消息队列，也提供了 LocalMessageDistributor，在单节点时使用此分发器，更高效稳定

### Changed

- refactor:  代码格式化强制换行符使用 LF，保证跨系统协同开发的统一性
- refactor:  mail-stater 的结构微调，修改了部分类名
- refactor：调整了系统的依赖结构，将 spring 相关依赖版本管理由父工程移动到 ballcat-dependencies 中

### Bug

- fix: 修复由于SpringMvc5.3版本后的跨域通配符使用方式导致 swagger 跨域配置 * 号无法生效的问题
- fix: 修复字典项删除时未更新hashcode 导致的前台缓存问题
- fix: 添加依赖，修复高版本 lombok 和 mapstruct 的冲突问题
- fix: 修复代码生成器模板生成失败以及无法平移文件的问题
- fix: 修复操作日志在记录入参时，若参数中含有 request 或 response 导致的堆栈溢出问题

### Dependency

- Bump spring-boot from 2.4.0 to 2.4.1
- Bump mapstruct from 1.3.1.final to 1.4.1.final
- Bump spring-javaformat-maven-plugin 0.0.25 to 0.0.26



## [0.0.6]

### Warning

- 更新了 UserDetail，缓存反序列化将会出现问题，更新版本前需要清除对应缓存
- 返回体结构修改，属性 msg 修改为 message，可能影响前端信息展示，接入系统的第三方的响应数据接收，升级前需提前沟通

### Added

- feat: 新增组织机构(部门)，用户与组织机构为一对一的关系
- feat: 数据权限，利用 mybatis 拦截器对 sql 进行拦截约束，约束规则支持自定义，适用于大部分数据权限控制。
- feat: 角色新增 scopeType，暂时支持全部，本人，本部门，本人及子部门等几种范围类型
- feat: 新增短信发送 stater
- feat: excel导出支持自定义头信息
- feat: 新增 JacksonUtils，方便全局统一 objectMapper 配置

### Changed

- fix: 修复没有提供默认 profile，导致用户不指定 profile 时，全局异常处理无法正常初始化的问题
- refactor:  Lov 模块调整
- refactor: lovBody 和 lovSearch 关联属性由 lovId 更改为  keyword
- refactor: UserDetails 属性重构，抽象出用户资源(userResources)和用户属性(userAttributes)，默认将用户的角色和权限作为资源存入userResources. 可以重写UserInfoCoordinator类，来根据业务调整用户资源和用户属性
- refactor: kafka 消费者配置提供
- refactor: 返回体结构修改，属性 msg 修改为 message
- refactor: xss 和 monitor auth 过滤器提供开关，并调整了配置前缀
- refactor: 用户角色，角色权限的关联，由 role_id 修改为使用 role_code
- fix: 系统用户查询时组织机构ID为空不为null时导致的异常
- refactor: 字典附加属性，value值修改为object类型
- fix: 修复用户在容器初始化前使用缓存注解时，CacheLock未初始化导致的异常问题
- fix: 移除 hutool json 的使用（该工具类部分情况下可能导致栈溢出）
- refactor:  支持用户select数据查询接口使用 userTypes 进行多类型筛选，删除原先的地址栏占位符查询方式

### Dependency

- Bump spring-boot from 2.3.4 to 2.4.0



## [0.0.5]

### Added

- Lov 模块
- 字典相关
  - feat: DictItemVO 新增 id 属性
  - feat: 字典项新增 attributes 属性，用于定制额外的非必须属性，如颜色等供前端使用

### Changed

- refactor: ApplicationContextUtil 更名为 SpringUtil

- refactor: LogUtil#isMultipart 去除只判断 POST 请求的限制

- 全局异常&异常通知

  - fix: 修复异常通知 message 为 null 时导致的异常

  - fix: 修复类校验失败时，无法正常返回错误信息的
  - feat: 异常通知添加 hostname 和 ip 信息
  - fix: 捕获空指针异常时，会导致异常通知空指针的问题
  - fix: 异常通知 cpu 占用过高问题
  - feat: 添加忽略指定异常类的配置
  - refactor: 优化钉钉通知的http请求方式
  - style: 通知信息中的英文冒号转为中文冒号
  - refactor: 除未知异常外，取消全局异常捕获时的异常打印，如需详细堆栈可以在异常处理类中进行处理

- feat: AbstractQueueThread#preProcessor 修改为 public，便于子类重写

- fix: 修复包装 RequestBody 导致，表单数据无法正常读取的bug

- fix: 修复在前台页面新建权限时无法指定主键 Id 的异常

- feat: extend-mybatis-plus 中批量插入方法，将生产的主键回填到实体中

- refactor: 登陆日志和操作日志分离

- fix: 修复用户登陆后将密文密码返回前台的安全隐患问题

- style: 代码生成器样式微调

### Dependency

- Bump spring-boot from 2.3.1 to 2.3.4
- Bump mybatis-plus from 3.3.2 to 3.4.0
- Bump hutool from 5.3.10 to 5.4.1
- Bump  spring-java-format from 0.0.22 to 0.0.25



## [0.0.4]

### Added

- 新增 kafka stater 模块
- 新增 mybatis-extends 扩展，添加批量插入方法
- accesslog 提供 responseWrapper，方便记录响应数据
- swagger stater 新增 additionalModelPackage 属性，用于扫描一些额外的 swaggerModel
- 异常捕获新增对 MethodArgumentTypeMismatchException 的处理
- 新增 Security 是否开启禁止 iframe 嵌入的配置控制

### Changed

- AbstractQueueThread 提高默认的批处理大小
- 代码生成器移除加载动态数据源时指定的 driverClassName
- 移除 admin-core 默认引入的 swagger 依赖，现在用户可以在自己的项目中选择引入
- 移除 JacksonConfig#ObjectMapper 的 @Primary 注解，便于用户自定义
- 数据字典优化，支持批量请求，接口地址调整
- 调整前后端传输密码使用的 AES Padding mode 为 PKCS5Padding
- 修复因 TokenStore 与 cachePropertiesHolder 加载顺序导致的启动异常
- 修复 codegen 无法选择 master 之外的数据源进行代码生成的bug
- 当使用 DingTalk 异常通知时，会 @所有人
- 字典添加值类型字段，便于前端回显，以及后续校验控制

### Dependency

- swagger up to 1.5.21
- dynamic-datasource up to 3.2.0
- spring-boot-admin up to 2.2.4
- easyexcel up to 2.2.6



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