# 更新日志



## [0.6.0] 2021-01-20

### :warning: Warning

- Swagger2 相关注解迁移到 OpenAPI3，由于使用了 springdoc-openapi，且该项目当前版本的一些问题，如果没有在 服务中引入
  **springdoc-openapi-ui** 的依赖，或者配置中添加 `springdoc.api-docs.enabled=false` 的配置，则会导致启动报错
- 删除了 knife4j-ui 的版本管理，对于 OpenAPI3，请使用 knife4j 的 3.x 版本
- springfox 组件未适配 springboot 2.6.2 版本，如需继续使用 springfox，请添加 `spring.mvc.pathmatch.matching-strategy=ant-path-matcher` 配置，以及注册 `SpringfoxHandlerProviderBeanPostProcessor` 到 spring 容器中
- springboot 2.6.x 默认禁止循环依赖，如有循环依赖启动将会报错，请注意修改代码，或者添加配置 `spring.main.allow-circular-references = true ` (不建议)
- `IPageArgumentResolver` 移除，如果直接使用 mybatisPlus 的 IPage 做为查询入参会有 SQL 注入风险，请注意修改！！！
- `IPageArgumentResolver` 移除，如果直接使用 mybatisPlus 的 IPage 做为查询入参会有 SQL 注入风险，请注意修改！！！
- `IPageArgumentResolver` 移除，如果直接使用 mybatisPlus 的 IPage 做为查询入参会有 SQL 注入风险，请注意修改！！！

### ⭐ New Features

- 【修改】修改 jackson 脱敏支持的模块添加方式，使用为注册 `JsonDesensitizeModule` 的形式，以便复用 spring-boot 默认的 module 注册。
- 【修改】调整 `CustomJavaTimeModule` 的注册方式，防止被 JSR310 的 `JavaTimeModule` 覆盖
- 【删除】移除过时已久的 `IPageArgumentResolver`，让 starter-web 和 mybatis-plus 模块解耦。
- 【删除】移除过时的 Lov 相关代码。
- 【修改】Swagger2 相关注解迁移到 OpenAPI3
- 【修改】文档底层支持从 springfox 迁移到 springdoc-openapi
- 【添加】对于 GET 请求的入参封装类，如 xxQO，添加 `@ParameterObject` 注解，以便在文档上正确展示查询入参
- 【修改】由于 springfox 长久不更新，弃用基于该框架的 **ballcat-spring-boot-starter-swagger** 组件
- 【新增】添加 **ballcat-extend-openapi**，模块，基于 springdoc-openapi 做了部分扩展，参看[文档](http://www.ballcat.cn/guide/feature/openapi.html)
- 【删除】删除 knife4j-ui 的版本管理
- 【修改】代码优化，显示指定部分参数或返回值的泛型
- 【修改】Sonarlint 部分代码警告处理
- 【删除】移除 dependencies pom 中无用的 pluginManagement 部分
- 【修改】hutool 依赖管理改为使用 hutool 官方提供的 bom
- 【新增】添加 **ballcat-extend-tesseract** 扩展模块，用于 OCR 文字识别工具的调用封装
- 【修改】字典相关逻辑调整
  - 去除字典只读/可写的属性控制
  - 字典项增加启用/禁用的状态属性
  - 字典现在在有字典项的情况下不允许删除（之前会自动级联删除）
- 【修改】同步 mybtais-plus 升级 3.5.x 后，AbstractMethod 的方法名获取做的调整
- 【修改】**ballcat-spring-boot-starter-oss** 更新 oss 相关方法与变量. 由 path 变为 key. 符合 oss 规范，原 rootPath 属性标记为过期，修改为 objectKeyPrefix
- 【添加】**ballcat-spring-boot-starter-oss** 新增根据 `File`  直接上传的方法
- 【修改】`StreamUtils` 克隆流方法优化. 使用 FileOutStream 保证不会因为文件过大而内存溢出
- 【修改】`OssDisabledException` 父类由 `Exception` 修改为 `RuntimeException`
- 【修改】**ballcat-common-idempotent** 幂等组件微调
  - `RedisIdempotentKeyStore` 的 stringRedisTemplate 属性，改为构造器注入
  - 取消 `IdempotentAspect` 切面的 @Component 注解，防止误注册



### 🐞 Bug Fixes

- 【修复】修复删除字典项时没有将变动通知到前端的问题
- 【修复】修复 `FileUtils#updateTmpDir` 方法中文件夹创建异常的问题



### 🔨 Dependency Upgrades

- 【升级】spring-boot from  2.5.6 to 2.6.2
- 【升级】lombok from 1.18.20 to 1.18.22
- 【升级】spring-javaformat from 0.0.28 to 0.0.29
- 【升级】hutool from 5.7.12 to 5.7.19
- 【升级】dynamic-datasource from 3.4.1 to 3.5.0
- 【升级】jasypt from 3.0.3 to 3.0.4
- 【升级】jsoup from 1.14.2 to 1.14.3
- 【升级】mybatis-plus from 3.4.3.4 to 3.5.0
- 【升级】mybatis from 3.5.7 to 3.5.9
- 【升级】jsqlparse from 4.2 to 4.3
- 【升级】fastjson from 1.2.76 to 1.2.79
- 【升级】spring-boot-admin from 2.5.4 to 2.6.0



## [0.5.0] 2021-12-03

### :warning: Warning

- 由于业务实体类的统一修改，其对应的表结构发生了变化
- 批量方法从 `saveBatchSomeColumn` 切换到 `saveBatch` 后，注意项目中的 jdbcUrl 配置，需要添加 rewriteBatchedStatements=true 条件，否则插入效率降低


### ⭐ New Features

- 【修改】 业务实体类添加父类 `LogicDeletedBaseEntity`，统一支持逻辑删除
- 【修改】 业务实体类统一修改描述、备注等属性名为 remarks
- 【修改】 业务代码批量插入部分方法从 `saveBatchSomeColumn` 切换到 `saveBatch`， 经实测，开启批处理事务以及 jdbcUrl 连接添加`rewriteBatchedStatements=true` 后,  循环 insert into 批量提交比 insert into values 语法速度更快。
- 【新增】 **ballcat-spring-boot-starter-file** 组件，支持 local 本地 和 ftp 文件上传操作
- 【添加】 `TreeUtils#treeToList()` 方法，支持将树平铺为列表
- 【添加】 `ImageUtils#mixResolveClone()` 方法，先使用快速解析，若失败回退到正常解析方法
- 【新增】 `FileUtils` 工具类
- 【新增】 `BaseEntity` 和 `LogicDeletedBaseEntity` 实体类基类
- 【新增】 支持定制 Redis Key 前缀的生成规则
- 【新增】 `DistributeLock` , 更加方便的进行分布式锁的使用
- 【新增】 `AbstractMessageEventListener` 类，提供默认的消息序列化处理
- 【添加】 `ExtendService#saveBatch()` 方法
- 【新增】 多线程对同一 websocket session 进行发送操作的支持
- 【修改】 默认提供的 MybatisPlusConfig 配置类中的自动填充处理类的条件注解修改，方便用户替换为自己的 `MetaObjectHandler`
- 【新增】 线程池配置 `@Async` 异步线程日志支持 traceId 输出
- 【添加】 `TokenGrantBuilder#getAuthenticationManager()` 方法，方便子类继承时获取 AuthenticationManager (#133)
- 【修改】 `FileService` ，OssClient 不再为必须依赖，当没有配置 Oss 时，默认回退使用 FileClient，根据配置走本地存储或者FTP
- 【修改】 `MappedStatementIdsWithoutDataScope` 的 `WITHOUT_MAPPED_STATEMENT_ID_MAP` 属性类型为 `ConcurrentHashMap`
- 【修改】 `TraceIdFilter` 默认在响应头中返回 TraceId 参数，方便排查问题
- 【修改】 `UserInfoCoordinator` 从类调整为接口，并提供默认实现 `DefaultUserInfoCoordinatorImpl`



### 🐞 Bug Fixes

- 【修复】 数据权限使用 JDK动态代理或者桥接方法时无法正确找到 `@DataPermission` 注解的问题
- 【修复】 数据权限在 SQL 右连接，内连接失效的问题
- 【修复】 数据权限对于使用括号包裹的 sql 解析失效的问题
- 【修复】 在仅使用  `ballcat.swagger.enabled=false` 的情况下，swagger 没有正常关闭的问题
- 【修复】 由于跨域问题，导致 swagger 无法在聚合者 Aggregator 中对 文档提供者 Provider 进行调试的问题
- 【修复】 WebSocket 在接收普通文本属性时的异常问题，现在会回退使用 `PlanTextMessageHandler` 进行处理
- 【修复】 查询指定名称的组织时构建树失败的问题




### 🔨 Dependency Upgrades

- 【升级】 spring-boot from  2.5.4 to 2.5.6
- 【升级】 spring-boot-admin from 2.5.1 to 2.5.4


## [0.4.0] 2021-10-15

### Warning

- mybatis-plus 升级，其对应一些 count 方法，返回值修改为了 Long 类型，项目中有使用的地方需要对应修改
- 默认登录时返回的 token 属性有所变更，原 roles 修改为 roleCodes，前端注意对应升级
- websocket 默认使用 local 进行分发，这将导致集群状态下的数据推送异常，如需集群部署，请修改对应配置
- websocket 相关接口 MessageSender 移除，该接口并入 MessageDistributor ，注意修改对应依赖引入类型

### Added

- feat：**ballcat-auth** 授权服务器定制增强：
  - 允许用户自定义 `AccessTokenConverter`，修改自省端点 `/check_token` 的返回值
  - 允许用户定制授权处理器或者新增授权处理器，用户可以通过覆盖 `TokenGrantBuilder` 实现
  - 允许用户添加自己的 `AuthenticationProvider` 方便处理自定义的 grant_type
  - 添加 OAuth2ClientConfigurer 抽象接口，方便用户替换 ClientDetailsService 的配置方式
  - 和 **ballcat-system** 模块解耦，方便复用 **ballcat-auth** 快速搭建一个授权服务器，例如 C 端用户 和 后台用户分离登陆系统，各搭建一套基于 OAuth2 的登录。
  - 根据 OAuth2 规范，调整 check_token 端点响应，在 token 不正确时响应 200，响应体为 `{ active: false }`，而不是返回 400
- feat：数据权限对于 jsqlparse 4.2 后，连表使用尾缀多个 OnExpression 方式的 SQL 解析支持
- feat：角色添加 scopeResource 属性，以便支持自定义数据权限设置一些信息
- feat：默认的 jackson 时间序列化添加了 `Instant` 类型支持，防止在使用时出现异常 InvalidDefinitionException: Java 8 date/time type `java.time.Instant` not supported by default



### Changed

- refactor：资源服务器对于客户端凭证生产的token 解析支持，对应的 userdetails 为 `ClientPrincipal`

- refactor：授权服务器自省端点的 scope 属性响应调整，根据 OAuth2 自省端点协议，scope 应返回字符串，用空格间隔

- refactor：数据权限调整

  - 问题修复： fix 数据权限在表名使用 `` 转义字符时失效的问题
  - 性能优化：对于无需数据权限控制的 sql 在解析一次后进行记录，后续不再进行解析处理
  - 结构调整：防止误用以及避免歧义，DataScopeHolder 修改为 DataScopeSqlProcessor 的私有内部类

- refactor：SelectData 试图对象中的 value 修改为 Object 类型，selected 和 disabled 修改为 Boolean 类型

- refactor：系统用户相关的 service 和 mapper 层，修改使用 Collection 接收参数，方便使用

- refactor：TokenAttributeNameConstants 常量类拆分

- refactor：UserInfoDTO 属性调整，新增了 menus 用于存储用户拥有的菜单对象集合，修改 roles 属性用于存储用户拥有的角色对象集合，原 roles 属性修改为 roleCodes 存储角色标识集合

- refactor：为避免歧义，登录和自省端点返回信息中的属性名称 roles 修改为 roleCodes

- bug：修复使用 **ballcat-spring-boot-starter-web** 时，若没有引入 security 依赖则启动异常的问题

- refactor： system 相关事件优化调整
  1. 用户组织变动时发布 UserOrganizationChangeEvent 事件
  2. 用户新建的事件由 UserChangeEvent 修改为 UserCreatedEvent
  3. system 的 event 类从 biz 迁移到 model 模块中

- refactor：**ballcat-common-websocket** 移除 MessageSender 接口，将其并入消息分发器 MessageDistributor

- refactor：**ballcat-spring-boot-starter-websocket** 与 redis 解耦，将默认注册的消息分发器由 redis 改为 local，基于内存分发。可通过 ballcat.websocket.message-distributor 属性修改为 redis 或者 custom，值为 custom 表示，用户自己定制 MessageDistributor（如修改为使用 mq，可用性更高）

  ```yaml
  ballcat:
  	websocket:
  		# 默认为 local 仅支持单节点使用，redis 基于 PUB/SUB 消息订阅支持了集群下的消息推送问题
  		message-distributor: redis # local | redis | custom
  ```

- refactor：**ballcat-spring-boot-starter-redis** 调整 AddMessageEventListenerToContainer 的注册方式，防止用户配置包扫描导致的加载顺序异常

- refactor：有用户绑定组织时，不允许删除组织



### Dependency

- Bump spring-boot from 2.4.8 to 2.5.5
- Bump lombok from 1.18.16 to 1.18.20
- Bump mybatis-plus 3.4.4 to 3.4.3.4
- Bump mybatis  3.5.6 to 3.5.7
- Bump jsqlparser 4.0 to 4.2
- Bump flatten-maven-plugin from 1.2.5 to 1.2.7
- Bump spring-javaformat from 0.0.27 to 0.0.28
- Bump hutool from 5.7.3 to 5.7.12
- Bump spring-boot-admin from 2.4.2 to 2.5.1
- Bump dynamic-datasource-spring-boot-starter from 3.3.2 to 3.4.1




## [0.3.0] 2021-09-09

### Warning

- 多个模块包名调整，注意重新 import 对应路径
- 国际化重构，改动较大，注意对应代码调整。国际化使用文档参看：http://www.ballcat.cn/guide/feature/i18n.html
- 由于 **ballcat-common-conf** 的删除，非 admin 服务中的 mybatis-plus 的相关配置，如分页插件，批量插入方法的注入，需要按需添加。
- 操作日志优化，修改了 `OperationLogHandler` 的相关方法，如果有自定义 OperationLogHandler ，需要注意同步更新
- 现在资源服务器默认关闭了表单登录功能，可通过配置开启表单登录并指定登录页地址



### Added

- feat: 国际化功能的默认支持，新增 **ballcat-i18n** 相关模块，以便提供默认的业务国际化实现方式

- feat: 登录用户名密码错误时的错误消息国际化处理

- feat: **ballcat-common-redis** 针对 PUB/SUB 新增 `MessageEventListener` 接口，**ballcat-spring-boot-starter-redis**  中会自动注册所有实现 `MessageEventListener` 接口的监听器

- feat:  **ballcat-common-redis** 中的 `@CacheDel` 注解，新增 multiDel 属性，方便批量删除缓存

- feat: 新增 **ballcat-common-idempotent** 幂等模块

- feat: 针对 hibernate-validation 校验的提示消息，支持使用 {}，占位替代 defaultMessage

- feat:  **ballcat-common-core** 中默认新增了 `CreateGroup` 和 `UpdateGroup` 接口，方便分组校验使用

- feat:  新增 **ballcat-spring-boot-starter-web** 模块，该模块基于 `spring-boot-starter-web`, 并使用 undertow 作为默认的嵌入式容器，且将 **ballcat-common-conf** 中对 web 应用的配置增强，如全局异常管理，以及 Sql 防注入处理，jackson 的默认配置等配置移动到此项目中

- feat: **ballcat-extend-mybatis-plus** 模块中，为了支持连表查询的条件构建，新增 `OtherTableColumnAliasFunction` ，方便使用  `LambdaAliasQueryWrapperX` 进行关联表查询条件的构建

- feat: **ballcat-spring-boot-starter-easyexcel** 支持导出时进行 Excel 头信息的国际化处理，使用 `{}` 进行占位表示，使用示例可参看 I18nData 的导出使用

- feat: **ballcat-spring-boot-starter-swagger** 配置的扫描路径 `basePackage` ，支持使用 `,`  进行多包名的分割扫描

- feat: **ballcat-spring-boot-starter-datascope** 中的数据权限控制注解 @DataPermission 扩展支持在 Mapper 之外使用，且支持方法嵌套调用时使用不同的 @DataPermission 环境

- feat: **ballcat-common-security** 中资源服务器配置不再默认开启表单登录，新增两个配置属性用于开启并指定登录页地址：

  ```yaml
  ballcat:
  	security:
  		oauth2:
  			resourceserver:
  				# 是否开启表单登录，默认 false
  				enable-form-login: true
  				# 登录页地址，开启表单登录时生效，不配置则默认为 /login
  				form-login-page: http://login-domin
  ```





### Changed

- refactor: **ballcat-common-conf** 内原先对于 mybati-plus 的自动填充、分页插件、以及批量插入方法注入的配置移动到 **ballcat-admin-core** 中

- refactor:  `SpELUtils` 改名为 `SpelUtils`，并移动到 **ballcat-common-util** 模块中

- refactor:  `ApplicationContextHolder` 改名为 `SpringUtils`，并移动到 **ballcat-common-util** 模块中

- refactor: **ballcat-spring-boot-starter-log** 中拆分出 **ballcat-common-log** 模块，解决在 log-biz 模块中需要引入 starter 的问题，部分代码的包名有变更

- refactor: **ballcat-spring-boot-starter-redis** 中拆分出 **ballcat-common-redis** 模块

- refactor: 重构原先的国际化 i18n 功能，新增 **ballcat-common-i18n** 模块，移除原先的 **ballcat-extend-i18n** 模块

- pref: 取消 **ballcat-spring-boot-starter-web** 中 **spring-security-core** 的传递依赖

- fix: 修复当查询一个不存在的系统配置后，由于缓存空值，导致添加配置后依然查询不到的问题

- pref: 菜单查询的返回类型修改为 SysMenuPageVO

- fix: 修复 excel 导出的 content-type 和实际文件类型不匹配的问题

- fix: 提高缓存切面的 Order，使其在事务提交后执行更新或删除操作，防止并发导致缓存数据错误

- pref: 菜单支持删除 icon

- fix: 修复当菜单 id 修改时，未级联修改其子菜单的父级 id 的问题

- pref: 优化操作日志，改为在方法执行前获取方法参数信息，防止用户在执行方法时将方法入参修改了

- pref: **ballcat-admin-core** 中默认扩展 springboot 默认的 TaskExecutor 配置，将拒绝策略从抛出异常修改为使用当前线程执行

- refactor:  移动 TreeNode 模型到 common-util 包中，以便减少 common-util 包的依赖

- refactor: **ballcat-spring-boot-starter-xss** 抽象出 XssCleaner 角色，用于控制 Xss 文本的清除行为，方便用户自定义

- pref: 用户登陆时的错误信息返回原始的细节信息，而不是全部返回用户名密码错误

- fix:  **ballcat-system-biz** websocket 包名拼写错误修复




### Removed

- 移除 **ballcat-common-conf**，相关代码拆分入 **ballcat-spring-boot-starter-web** 和 **ballcat-admin-core**



### Dependency

- Bump jsoup from 1.13.1 to 1.14.2



## [0.2.0] 2021-08-11

### Added

- feat: 新增 ballcat-extend-redis-module 模块，提供对布隆过滤器的操作
- feat: 新建用户时可以直接绑定用户角色，而不必分两次操作了
- feat: 支持修改菜单ID，方便转移菜单位置时，保持菜单 ID 规则
- feat: **新增 ballcat-common-security 模块**
  - 新增 CustomRedisTokenStore 用于在序列化异常时，直接清除缓存。避免每次修改 UserDetails 时都需要用户手动去删除所有缓存信息
  - 迁移 PasswordUtils 从 common-util 到 common-security，且 PasswordEncoder 使用 DelegatingPasswordEncoder, 方便未来切换密码加密算法
  - 迁移 OAuth 相关的异常处理，从 ballcat-oauth-controller 到 common-secutiy
  - 新增 ResourceServer 相关配置以及基础组件，基于 SpringSecurity 5.X
  -  SysUserDetails rename to User, sysUser 中的相关属性，现在直接写在 User 类中，同时删除了 userResource 和 userAttributes 属性，新增了 attributes 属性。



### Changed

- refactor: 数据权限 dataScopes 通过 ThreadLocal 进行方法间传递
- refactor: 拆分 admin-websocket 模块，方便用户剔除不需要的组件.
- refactor: ballcat-spring-boot-starter-websocket 模块中 websocket 相关的封装代码抽取到 **ballcat-commo-websocket** 模块，starter 仅保留自动配置相关代码
- pref: 菜单的逻辑删除属性使用 mybatis-plus 的自动填充功能，且当菜单 ID 已使用时提示详情
- pref: 精简了一些 common 模块中不需要的依赖
- refactor: OAuth2 ResourceServer 底层从 spring-security-oauth2 依赖迁移至 SpringSecurity 5.x
- pref: common-conf 中现在默认注册 jackson 的脱敏序列化器了，用户可以通过注册 name 为 ”desensitizeCustomizer“ 的 Jackson2ObjectMapperBuilderCustomizer bean，覆盖默认配置
- refactor: ballcat-spring-boot-starter-log 和业务解耦，操作日志的生产和存储全部交由业务项目自己处理，ballcat-log-biz 模块中提供了默认的操作日志实体类，以及 OperationLogHandler 的默认实现
- refactor: 由于 common-security 的抽取， ballcat-oauth 模块只剩下了授权相关，故更名为 ballcat-auth，同时做了一些结构上的调整，方便后续独立部署授权服务器。
  - 配置 `ballcat.upms.loginCaptchaEnabled` 现在调整为 `ballcat.security.oauth2.authorizationserver.loginCaptchaEnabled` ，用以控制登录验证码的开关
  - 配置 `ballcat.security.ignoreUrls` 现在调整为 `ballcat.security.oauth2.resourceserver.ignoreUrls` 用以控制资源服务器对部分 url 的鉴权忽略
  - 配置 `ballcat.security.iframeDeny` 现在调整为  `ballcat.security.oauth2.resourceserver.iframeDeny` 用于开启资源服务器的嵌入 iframe 允许
  - 新增 `@EnableOauth2AuthorizationServer` 注解，用以开启授权服务器 (ballcat-admin-core 模块中默认开启）


### Removed

- 移除 ballcat-oauth-model，相关代码迁入 ballcat-common-security


### Dependency

- Bump spring-boot from 2.4.3 to 2.4.8
- knife4j from 2.0.8 to 2.0.9
- hutool from 5.5.8 to 5.7.3
- fastjson from 1.2.75 to 1.2.76
- dynamic-datasource from 3.3.1 to 3.3.2
- spring-boot-admin from 2.4.1 to 2.4.2
- anji-captcha from 1.2.8 to 1.2.9



## [0.1.0] 2021-06-28

### Warning

- 此版本重构了前端路由部分，服务端权限表 sys_permission 改为 sys_menu，改动较大，迁移时建议先备份原始数据，执行增量 sql 后若出现问题，再进行比对处理
- 调整了模块名，sys => system，后续包名也都尽量不再使用缩写，注意修改对应类的引用包路径
- 项目部分配置添加 ballcat. 前缀
  - 文件存储现在修改为了对象存储（OssProperties.java），配置前缀为 ballcat.oss
  - 登录验证码开关和超级管理员指定的配置（UpmsProperties.java），前缀为 ballcat.upms
  - 登录密码的 AES 加解密密钥，忽略鉴权的 url 列表，iframe 嵌入配置开关等安全相关的配置 （SecurityProperties.java），前缀为 ballcat.security
- 模块拆分重构，原 `admin.modules` 下的 `log`、`system`、`notify` 相关代码，全部独立模块。目前拆分为 `model`，`biz`，`controller` 三层，方便按需引入。`ballcat-admin-core` 依然默认集成所有模块
  - log 模块涉及的表名以及类名修改，原 AdminXXXLog 类，全部去除 Admin 开头。表名前缀由 `admin_` 修改为 `log_`
  - log 中的登录日志也不再默认开启，需要登录日志，可手动注册 `LoginLogHandler` 类，代码示例可参考 `ballcat-sample-admin` 项目中的  `LogHandlerConfig`。
  - 同样访问日志和操作日志也需对应注册 handler，且在启动类上添加 @EnableXXXLog 注解
  - mapper.xml 文件移动，由于模块拆分，目前各模块的 mapper.xml 直接放置在了 mapper 文件夹下，对应的文件扫描配置 `mybatis-plus.mapper-locations` 需要修改为 `classpath*:/mapper/**/*Mapper.xml`
    ```yml
    mybatis-plus:
      mapper-locations: classpath*:/mapper/**/*Mapper.xml
    ```



### Added

- feat: 新增了国际化插件 i18n extend 和 i18n starter
- feat: BusinessException 的错误消息支持占位符了
- feat: PageParam 分页查询参数对象，支持用户自定义其子类以便做额外的功能处理
- feat: TreeUtils 现在构建树时，支持传入 Comprator，进行自定义排序
- feat: 新增 SmsUtils，以及 GSMCharst 类，用于短信长度计算
- feat: 新增了一个根据用户id查询 UserInfo 的接口
- feat: ballcat-extend-mybatis-plus 模块中添加普通枚举类型处理. 根据 name() 返回值判断枚举值
- feat: ballcat-spring-boot-starter-easyexcel 新增 @RequestExcel 注解，方便导入 excel 直接解析为实体对象
- feat: 新增了一个接口用于组织机构树形层级和深度的错误数据校正



### Changed

- refactor: SysPermission 移除，新增 SysMenu 类，相关关联类同步修改，减少了大部分的配置属性，转交由前端处理
- refactor: Lov 实体修改为 SysLov
- refactor: 移除 AdminRuleProperties.java，adminRule 相关配置与登陆验证码开关控制一并合入 UpmsProperties, 密码加密密钥配置并入 SecurityProperties，并将其配置前缀统一添加 ballcat.
- refactor: SysUserDetailsServiceImpl.getUserDetailsByUserInfo 方法调整为 public 级别, 便于以api方式登录的请求注入用户信息
- refactor: 重构了 excel 自定义头生成器的使用方式
- refactor: 修改 AbstractRedisThread.getObjType 默认实现, 使其更符合大多数情况(获取失败的情况下子类重写此方法)
- refactor: 文件存储 starter-storage 重构，修改为对象存储，使用 S3 协议和云端交互，所有支持 S3 协议的云存储都可以使用，如亚马逊、阿里云、腾讯云、七牛云
- refactor: 移除 userInfoDTO 中的 roleIds 属性
- refactor: 系统配置添加缓存注解，提升查询效率，更新和删除修改为使用 confKey, 而不是 ID
- pref: 根据 mapstruct 官方文档，调整了 lombok 和 mapstruct 的依赖引入方式
- pref: 所有 @RequestParam 和 @PathVariable 注解，指定 value 值，避免因环境问题，编译未保存参数名称，导致的参数绑定异常
- pref: 简化微信原生支付方法
- pref: 前后端交互密码解密异常时的错误日志以及响应信息优化
- fix: 禁止删除有子节点的组织，以及不能修改父组织为自己的子组织
- fix: 修复由于 mapstruct 的引入方式修改，导致 yml 中配置信息不提示的问题
- fix: 修复 easyexcel 添加 password 后会导致 excel 文件打开异常的问题
- pref: starter-log 和 starter-oss 模块包名拼写错误修复，commom -> common
- refactor: AccessLogSaveThread 不再默认启动，AbstractQueueThread 中对非活动状态的线程进行启动，避免重复启动异常
- fix: 修复组织架构移动到子层级时，导致其他节点的层级和深度被错误修改的问题（之前的错误数据可用此版本新增的校正功能修复）
- fix: 修复操作日志添加参数忽略类型的异常问题

### Dependency

- Bump  spring-boot-admin from 2.3.1 to 2.4.1
- Bump virtual-currency  from 0.4.1  to  0.4.2



## [0.0.9] 2021-04-28

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



## [0.0.8] 2021-03-04

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



## [0.0.7] 2021-01-19

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



## [0.0.6] 2020-12-03

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



## [0.0.5] 2020-09-18

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



## [0.0.4] 2020-08-14

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



## [0.0.3] 2020-07-06

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





## [0.0.2] 2020-06-04

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
