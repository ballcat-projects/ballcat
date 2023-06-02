


## 简介

![](https://hccake-img.oss-cn-shanghai.aliyuncs.com/ballcat/ballcat-doc.png)

[![LICENSE](https://img.shields.io/badge/license-MIT-green)](https://github.com/ballcat-projects/ballcat/blob/master/LICENSE)
[![GitHub stars](https://img.shields.io/github/stars/ballcat-projects/ballcat.svg?style=social&label=Stars)](https://github.com/ballcat-projects/ballcat/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/ballcat-projects/ballcat.svg?style=social&label=Fork)](https://github.com/ballcat-projects/ballcat/network/members)



**BallCat** 组织旨在为项目快速开发提供一系列的基础能力，方便使用者根据项目需求快速进行功能拓展。

在以前使用其他后台管理脚手架进行开发时，经常会遇到因为项目业务原因需要进行二开的问题，在长期的开发后，一旦源项目进行迭代升级，很难进行同步更新。

为了解决这一问题，**BallCat** 将自身所有的业务和功能都设计为可插拔的依赖，方便用户自由组装和卸载。

开发时，用户以依赖的方式引入 **BallCat** 所提供的模块，当 **BallCat** 项目升级时，用户只需同步更新版本号，即可获得功能更新。



**Ballcat 已将所有 JAR 包都推送至中央仓库，也会为每个版本的升级改动列出详细的更新日志，以及增量 SQL。**

> 如果在使用中遇到了必须通过二开修改源码才能解决的问题或功能时，欢迎提 issues，如果功能具有通用性，我们会为 BallCat 添加此能力，也欢迎直接 PR 你的改动。



- Github 地址 ：https://github.com/ballcat-projects/ballcat
- Gitee 地址：https://gitee.com/ballcat-projects/ballcat （如果 Github 访问速度比较慢的话，可以访问 Gitee ）
- 文档地址：http://www.ballcat.cn/ (域名备案中：暂时先使用 http://49.234.30.188/ 访问文档)





## 技术栈

  Spring Boot、Spring Security、Spring Authorization Server、Mybatis Plus、Hutool


## 相关仓库

| 项目             | 简介              | gitee 地址                                          | github 地址                                          |
| ---------------- |-----------------| --------------------------------------------------- | ---------------------------------------------------- |
| ballcat          | 核心项目组件          | https://gitee.com/ballcat-projects/ballcat          | https://github.com/ballcat-projects/ballcat          |
| ballcat-ui-vue   | 管理后台前端 vue2 版本  | https://gitee.com/ballcat-projects/ballcat-ui-vue   | https://github.com/ballcat-projects/ballcat-ui-vue   |
| ballcat-ui-react | 管理后台前端 react 版本 | https://gitee.com/ballcat-projects/ballcat-ui-react | https://github.com/ballcat-projects/ballcat-ui-react |
| ballcat-admin-ui-vue3 | 管理后台前端 vue3 版本  | https://gitee.com/ballcat-projects/ballcat-admin-ui-vue3 | https://github.com/ballcat-projects/ballcat-admin-ui-vue3 |
| ballcat-codegen  | 代码生成器           | https://gitee.com/ballcat-projects/ballcat-codegen  | https://github.com/ballcat-projects/ballcat-codegen  |
| ballcat-samples  | 使用示例            | https://gitee.com/ballcat-projects/ballcat-samples  | https://github.com/ballcat-projects/ballcat-samples  |
| ballcat-boot     | 单体应用模板项目        | https://gitee.com/ballcat-projects/ballcat-boot     | https://github.com/ballcat-projects/ballcat-boot     |

> 注意 ballcat 是核心组件仓库，如果需要启动后端服务，请使用 ballcat-boot



## 项目演示

### 后台管理

**演示地址** （账户：admin ；密码：a123456） ：

http://preview.ballcat.cn/

**演示效果**：

![](https://hccake-img.oss-cn-shanghai.aliyuncs.com/ballcat/doc/ballcat-preview.png)

![图片](https://hccake-img.oss-cn-shanghai.aliyuncs.com/ballcat/ballcat-preview0.png)

![](https://hccake-img.oss-cn-shanghai.aliyuncs.com/ballcat/ballcat-preview1.png)

![](https://hccake-img.oss-cn-shanghai.aliyuncs.com/ballcat/ballcat-preview2.png)

### 代码生成器

代码生成器提供了在线编辑模板的功能，以及多数据源的支持。

只需启动一个代码生成器服务放在测试服，所有项目需要生成代码时都可以复用此生成器，减少了频繁切换项目启动生成器的繁琐。

**演示地址**：

http://codegen.ballcat.cn/

**演示效果**：

![](https://hccake-img.oss-cn-shanghai.aliyuncs.com/ballcat/doc/ballcat-codegen-preview.png)



## 业务模块

**BallCat** 为后台管理的一些基本需求提供了以下五个业务模块，用户可以按需引入：

- **ballcat-auth（授权模块）** ：用于支撑 OAuth2 的授权服务器，集成了登录图像验证码，登录AES密码解密过滤器等相关功能。目前使用 Spring-Security-OAuth2 作为基础，后续将迁移到  **[spring-authorization-server](https://github.com/spring-projects/spring-authorization-server)** 项目。
- **ballcat-system（系统模块）** ：提供了用户管理、角色管理、菜单管理、组织架构、字典管理、系统配置等这些后台管理系统中不可或缺的核心功能。
- **ballcat-log（日志模块）** ：提供了登录日志、操作日志、访问日志等日志记录功能，提供了 TraceId，可串联一次请求中的所有日志信息。日志默认存储位置在 mysql 中，用户可以按需定制日志处理逻辑。
- **ballcat-i18n（国际化模块）** ：提供了基于数据库的国际化信息配置存储方案，提供 local + redis 双重缓存处理，提升国际化处理效率。
- **ballcat-notify（通知模块）** ：目前提供了系统公告的能力，下个版本将会新增通知相关的功能。


## 功能模块

功能模块和业务无关，非 ballcat 项目也可以引入这些模块获得功能增强，用户按照实际业务需求选择模块进行集成。

**包括但不限于以下这些功能** ：

- **数据权限控制**，在 orm 层实现，基于 Jsqparse 解析 Sql，进行权限范围的 sql 注入
- **国际化功能**，不仅支持 spring 原生的文件配置形式，还可以自定义动态加载国际化配置
- **注解使用 redis** 缓存、分布式锁，防击穿，全局key前缀等功能
- **注解快速实现 excel 导入导出**功能
- **支付功能**：包括支付宝、微信、USDT 虚拟货币等
- **基于 S3 协议的对象存储**封装，方便一套代码兼容大部分云平台，如阿里云，七牛云，腾讯云

**目前提供的功能模块列表**

```
|-- ballcat-common                 -- 基础公用组件
|   |-- ballcat-common-core             -- 核心组件
|   |-- ballcat-common-desensitize      -- 脱敏基础组件
|   |-- ballcat-common-i18n	            -- 国际化基础组件
|   |-- ballcat-common-idempoten        -- 幂等基础组件
|   |-- ballcat-common-log              -- 日志基础组件
|   |-- ballcat-common-model            -- 公用的一些模型
|   |-- ballcat-common-redis            -- redis基础组件
|   |-- ballcat-common-security         -- 安全相关，以及资源服务器配置
|   |-- ballcat-common-util             -- 公用的工具
|   `-- ballcat-common-websocket        -- 对于 spring websocket 的一些抽象封装
|-- ballcat-dependencies           -- ballcat项目本身各子模块的依赖管理，以及第三方模块的依赖管理
|-- ballcat-extends	               -- 扩展模块，大多是对于一些第三方组件的扩展处理
|   |-- ballcat-extend-dingtalk	        -- 钉钉的一些操作封装
|   |-- ballcat-extend-kafka            -- kafka 的一些操作扩展
|   |-- ballcat-extend-kafka-stream	    -- kafka 流处理的一些操作扩展
|   |-- ballcat-extend-mybatis-plus	    -- 基于 mybatis-plus 相关的一些扩展
|   |-- ballcat-extend-openapi          -- 对 springdoc-openapi 的一点封装扩展
|   |-- ballcat-extend-pay-ali          -- 针对支付宝支付的一些操作封装
|   |-- ballcat-extend-pay-virtual      -- 针对虚拟货币支付的一些操作封装
|   |-- ballcat-extend-pay-wx           -- 针对微信支付的一些操作封装
|   |-- ballcat-extend-redis-module     -- redis module 的扩展功能（暂时只有布隆过滤器）
|   `-- ballcat-extend-tesseract        -- 对 OCR 文字识别工具的一个操作封装
|-- ballcat-starters                -- Spring Boot快速启动器      
|   |-- ballcat-spring-boot-starter-datascope   -- 数据权限控制
|   |-- ballcat-spring-boot-starter-dingtalk    -- 钉钉集成工具
|   |-- ballcat-spring-boot-starter-easyexcel   -- 通过注解快速导入导出excle（easyexcel）
|   |-- ballcat-spring-boot-starter-file        -- 文件上传 FTP or Local
|   |-- ballcat-spring-boot-starter-i18n        -- 国际化方案
|   |-- ballcat-spring-boot-starter-idempotent  -- 幂等处理方案
|   |-- ballcat-spring-boot-starter-ip2region   -- 离线IP查询方案
|   |-- ballcat-spring-boot-starter-job         -- 定时任务集成（目前仅xxl-job）
|   |-- ballcat-spring-boot-starter-kafka       -- 消息队列 kafka 集成
|   |-- ballcat-spring-boot-starter-log         -- 访问日志，操作日志，TraceId注入
|   |-- ballcat-spring-boot-starter-mail        -- 邮件发送
|   |-- ballcat-spring-boot-starter-oss	        -- 对象存储（所有支持 AWS S3 协议的云存储，如阿里云，七牛云，腾讯云）
|   |-- ballcat-spring-boot-starter-pay	        -- 支付相关
|   |-- ballcat-spring-boot-starter-redis       -- 提供注解使用 redis, 分布式锁，防击穿，全局key前缀等功能
|   |-- ballcat-spring-boot-starter-sms	        -- 短信接入 starter
|   |-- ballcat-spring-boot-starter-swagger     -- swagger文档配置（提供无注册中心的文档聚合方案）
|   |-- ballcat-spring-boot-starter-web         -- Web工程封装
|   |-- ballcat-spring-boot-starter-websocket	-- 基于 common-websocket 的自动配置
|   `-- ballcat-spring-boot-starter-xss	        -- xss 防注入相关
```

## 后台管理搭建

参看官方文档，快速搭建一章： http://www.ballcat.cn/guide/quick-start.html


## 交流群

由于微信限制群人数超 200 无法通过扫码进群，可以扫我的个人微信二维码，或者添加我的微信号 `Hccake_`，备注 `BallCat 进群`，我再邀请你入群

<img src="https://hccake-img.oss-cn-shanghai.aliyuncs.com/ballcat/wechat-hccake.jpg" alt="微信" width="35%"/>
