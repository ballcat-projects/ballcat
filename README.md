# BallCat

BallCat 组织旨在为项目快速开发提供一系列的基础能力，方便用户根据项目需求快速进行功能拓展。

## 前言

以前在使用其他后台管理脚手架进行开发时，经常会遇到因为项目业务原因需要进行二开的情况，在长期的开发后，一旦源项目进行迭代升级，很难进行同步更新。

所以 BallCat 推荐开发项目时，以依赖的方式引入 BallCat 中提供的所有功能，这样后续跟随 BallCat 版本升级时只需要修改对应的依赖版本号即可完成同步。

BallCat 已将所有 JAR 包都推送至中央仓库，也会为每个版本升级提供 SQL 改动文件。

> 如果在使用中遇到了必须通过二开修改源码才能解决的问题或功能时，欢迎提 issues，如果功能具有通用性，我们会为 BallCat 添加此能力，也欢迎直接 PR 你的改动。


## 相关仓库

| 项目            | 简介                                 | github 地址                                         |
| --------------- | ------------------------------------ | --------------------------------------------------- |
| ballcat         | 核心项目组件                         | https://github.com/ballcat-projects/ballcat         |
| ballcat-ui-vue  | 管理后台前端                         | https://github.com/ballcat-projects/ballcat-ui-vue  |
| ballcat-codegen | 代码生成器                           | https://github.com/ballcat-projects/ballcat-codegen |
| ballcat-samples | 使用示例                             | https://github.com/ballcat-projects/ballcat-samples |



## 地址链接

**管理后台预览**：http://preview.ballcat.cn

**代码生成器预览**：http://codegen.ballcat.cn/

**文档地址**：http://www.ballcat.cn/ （目前文档只有少量内容，会陆续填坑）



# ballcat

本仓库存放了 BallCat 提供的所有的基础 Jar 包。

如 `ballcat-admin-core` 依赖，用户引入此依赖并配合核心 sql，即可获得用户管理，OAuth2，权限控制，字典等等后台管理相关的基础功能。

`ballcat-admin-websocket` 依赖，用户引入后，后台管理即可获得 websocket 能力，对公告、字典、弹出选择框的修改可以及时通知到前端

`ballcat-spring-boot-starter-datascope` 依赖，用户引入后，根据自己的业务进行定制，即可获得数据权限控制的能力

······ 等等


## 项目结构

```
.
|-- ballcat-admin
|   |-- ballcat-admin-core         			 -- 后台管理核心模块（权限控制，字典，Oauth2等）
|   |-- ballcat-admin-i18n         			 -- 国际化使用方案
|   `-- ballcat-admin-websocket     		 -- 后台管理 websocket 支持插件（公告和字典等同步）
|-- ballcat-common			   
|   |-- ballcat-common-conf		        	 -- web公用配置
|   |-- ballcat-common-core            		 -- 核心组件
|   |-- ballcat-common-model            	 -- 公用的一些模型
|   |-- ballcat-common-util             	 -- 公用的工具类
|   `-- ballcat-common-desensitize			 -- 脱敏工具类
|-- ballcat-dependencies       -- ballcat项目本身各子模块的依赖管理，以及第三方模块的依赖管理
|-- ballcat-extends					
|   |-- ballcat-extends-dingtalk		       -- 钉钉的一些操作封装
|   |-- ballcat-extends-i18n   				   -- 国际化的一些使用方案
|   |-- ballcat-extends-kafka  				   -- kafka 的一些操作扩展
|   |-- ballcat-extends-kafka-stream  	   	   -- kafka 流处理的一些操作扩展
|   |-- ballcat-extends-mybatis-plus  	   	   -- 基于 mybatis-plus 相关的一些扩展
|   |-- ballcat-extends-pay-ali  	   		   -- 针对支付宝支付的一些操作封装
|   |-- ballcat-extends-pay-virtual  		   -- 针对虚拟货币支付的一些操作封装
|   `-- ballcat-extends-pay-wx  			   -- 针对微信支付的一些操作封装
|-- ballcat-starters
|   |-- ballcat-spring-boot-starter-datascope  -- 数据权限控制
|   |-- ballcat-spring-boot-starter-dingtalk   -- 钉钉集成工具
|   |-- ballcat-spring-boot-starter-easyexcel  -- 通过注解快速导出excle（easyexcel）
|   |-- ballcat-spring-boot-starter-i18n  	   -- 国际化方案
|   |-- ballcat-spring-boot-starter-job        -- 定时任务集成（目前仅xxl-job）
|   |-- ballcat-spring-boot-starter-kafka      -- 消息队列 kafka 集成
|   |-- ballcat-spring-boot-starter-log		   -- 访问日志，操作日志，TraceId注入
|   |-- ballcat-spring-boot-starter-mail	   -- 邮件发送
|   |-- ballcat-spring-boot-starter-pay	       -- 支付相关
|   |-- ballcat-spring-boot-starter-redis      -- 提供注解使用redis, 分布式锁，防击穿，全局key前缀等功能
|   |-- ballcat-spring-boot-starter-sms        -- 短信接入 starter
|   |-- ballcat-spring-boot-starter-oss        -- 对象存储（所有支持 AWS S3 协议的云存储，如阿里云，七牛云，腾讯云）
|   |-- ballcat-spring-boot-starter-swagger    -- swagger文档配置（提供无注册中心的文档聚合方案）
|   |-- ballcat-spring-boot-starter-websocket  -- websocket 集成
|   `-- ballcat-spring-boot-starter-xss  	   -- xss 防注入相关
`-- doc        -- 初始化数据库脚本
```

## 核心依赖

| 依赖                   | 版本          | 官网                                             |
| ---------------------- | ------------- | ------------------------------------------------ |
| Spring Boot            | 2.4.3         | https://spring.io/projects/spring-boot#learn     |
| Spring Security OAuth2 | 2.3.8.RELEASE | https://spring.io/projects/spring-security-oauth |
| Mybatis Plus           | 3.4.3         | https://mp.baomidou.com/                         |
| XXL-JOB                | 2.3.0         | http://www.xuxueli.com/xxl-job                   |
| Hutool                 | 5.5.8         | https://www.hutool.cn/                           |


## 快速搭建后台管理

参看官方文档 http://www.ballcat.cn/guide/quick-start.html


## 交流群

<img src="https://hccake-img.oss-cn-shanghai.aliyuncs.com/ballcat/ballcat-wechat-group.png" alt="微信" width="35%"/>