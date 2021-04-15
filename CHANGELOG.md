# Changelog

更新日志

## [Unreleased]

- 全局数据校验支持

- OSS starter 修改使用 AWS S3

## [0.0.9]

### Warning

- 由于用户属性和用户资源类的抽象，更新版本后，需要删除原来缓存的用户数据，否则会造成反序列化移除
- ExtendService#selectByPage 方法移除，原本使用此方法的分页查询，需要更改为使用 baseMapper#selectPage
- 部分类路径有修改，注意迁移
- 代码生成器独立到新的仓库：https://github.com/ballcat-projects/ballcat-codegen
- 示例使用迁移到新的仓库：https://github.com/ballcat-projects/ballcat-samples

### Added

- feat: RedisHelper 工具类新增 list 的 rightPush 和 leftPop 方法
- feat: 新增了一个基于 Redis 的线程队列
- feat: 新增解绑用户角色关联关系的功能
- feat: `ExtendService#saveBatchSomeColumn` 现在支持分批批量插入了
- feat: admin-websocket 新增了 Lov 弹窗选择器修改时的 websocket 推送

### Changed

- refactor: 用户属性和用户资源抽象出接口，不再使用 Map 存储，具体使用类交由使用方进行构造，类似于` UserDetails`
- refactor: common-desensitize 优化，支持自定义注解脱敏

- refactor: 抽象 `AbstractThread` 类. 让下级自定义 poll 和 put 方法.
- refactor: `AbstractQueueThread` 添加程序关闭时的处理方法，防止停机时的数据丢失问题
- refactor: 简化了支付宝和微信的回调类，并添加了验签方法
- refactor: 使用 `Jackson2ObjectMapperBuilder` 构造 `ObjectMapper`，保留使用配置文件配置 jackson 属性的能力，以及方便用户增加自定义配置
- refactor: xss 防注入重构，抽取成一个 starter，限制基于 jsoup 的白名单过滤，可自定义排除路径和请求类型的配置，admin-core 包现在默认集成此 starter
- refactor: 工具类添加 finnal 关键字和私有构造
- refactor:  修改 extends 下的三个支付模块的类路径, 把 starter 修改为 extend
- refactor:  优化 JsonUtils 的类型转换
- fix: 修复 `LambdaQueryWrapperX#inIfPresent` 参数错误处理成流，导致的 sql 拼接异常
- fix: 修复当没有字典项时，无法正常删除字典的 bug
- fix: 修复几次版本更新导致的代码生成器的各种 bug，如目录项拖动，以及zip 文件流末端损坏等
- fix: 操作日志记录时，参数为 null 导致的空指针问题

### Removed

- 移除新酷卡短信组件
- 移除 mybatis-plus-extend 中的 selectByPage 方法，因为其无法真正修改返回类型，现在使用 `page.convert` 进行 数据转换

### Dependency

- Bump virtual-currency  from 0.3.2  to  0.4.1
- Bump  spring-boot-admin from 2.4.0 to 2.3.1



## [0.0.8]

### Warning

- 更新了 Service 层的父类，现在无法直接使用 service 对象，进行 Wrapper 条件构造
- 更新了分页查询的排序参数，前端需要对应升级
- commom-model和common-util的抽离，导致部分工具类和部分通用实体包名修改, 请注意替换

### Added

- feat: Swagger3 支持，文档地址更新为 /swagger-ui/index.html
- feat: 剥离全局异常捕获中请求方法和请求媒体类型不支持的异常，方便生产环境排查问题
- feat: 新增 common-desensitize 脱敏模块，默认提供了部分常用脱敏类型，且支持SPI形式追加用户自定义脱敏处理器
- feat: 新增 pay-ali 模块，用于支持支付宝支付
- feat: 新增图形验证码开关配置，默认开启
- feat: 分页查询出入参封装，提供 PageParam 作为入参，PageResult 作为出参，不再用 Page 贯穿
- feat: 密码在日志中的存储脱敏
- feat: 数据权限注解提供对于指定 Mapper类，或指定方法的数据权限关闭功能
- feat: 添加 JsonUtils 根据依赖执行对应的json处理方法
- feat: 添加 RedisHelper 提供对redis的常用方法支持
- test: 对 client test 跳过登陆验证和密码解密，便于测试，注意生产环境不要开启 test client

### Changed

- refactor: 取消了项目文件格式化指定换行符使用 LF 的限制
- refactor: 分页查询的排序参数属性修改，用于支持多列排序
- refactor: mybatis-plus-extend 扩展包调整
  - 新增 LambdaQueryWrapperX ，提供 ifPresent 方法，用于简化条件判断
  - 新增 LambdaAliasQueryWrapperX 用于构造带别名的条件语句和查询sql
  - ExtendMapper 新增 selectByPage 方法，扩展自 selectPage 方法，支持查询数据直接转换为 VO 的映射
  - ExtendMapper 新增 insertBatchSomeColumn，使用 insert into 方式进行插入，提升批量插入效率
  - ExtendService 扩展自 IService，但是取消所有对外暴露 Wrapper 参数的方法，便于规范代码分层
- refactor: 所有 Service 改为继承 ExtendService，所有 Mapper 改为继承 ExtendMapper，且所有查询 条件构造下沉入DAO 层
- refactor:  部分方法名修改，查询方法返回结果为集合时，方法名使用 list 开头
- refactor: 钉钉消息通知优化，每次通知使用新的 request 实例
- refactor: 访问日志默认忽略验证码获取请求，操作日志忽略 MutipartFile 类型的参数记录
- refactor: 用户密码在 service 使用明文密码交互，AES 加解密在 controller 或者过滤器中完成
- refactor: 密码加解密密钥的配置添加 ballcat 前缀：ballcat.password.secret-key
- refactor: hutool 改为按需引入
- refactor: 取消代码文件换行符强制使用 LF 的限制
- refactor: 取消 jackson 配置中，全局 Null 值转 '' 的处理，但是以下对 类型 Null 值特殊处理
  - String Null 转 ''
  - Array 和 Collection Null 转 []
  - Map Null 转 {}
- refactor: 从 common-core 中剥离出 common-util 和 common-model

### Removed

- 移除 mybatis-plus-extend-mysql 扩展包，相关方法移入 mybatis-plus-extend 扩展中
- 移除 model 的 AR 模式支持
- 移除 hibernate-validator 的版本指定，改为跟随 spring-boot 的依赖版本
- 移除 JacksonUtils

### Dependency

- Bump spring-boot from 2.4.1 to 2.4.3
- Bump mapstruct from 1.4.1.final to 1.4.2.final
- Bump spring-javaformat-maven-plugin 0.0.26 to 0.0.27
- Bump hutool from 5.5.7 from 5.5.8
- Bump mybatis-plus from 3.4.1 to 3.4.2
- Bump dynamic-datasource from 3.2.0 to 3.3.1
- Bump spring-boot-admin from 2.3.1 to 2.4.0
- Bump oss.aliyun from 3.8.0 to 3.11.3
- Bump anji-captcha 1.2.5 to 1.2.8



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