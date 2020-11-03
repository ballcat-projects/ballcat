# 新酷卡所属表
CREATE TABLE IF NOT EXISTS `port_info`
(
    `Id`      int(11)          NOT NULL AUTO_INCREMENT,
    `PortNum` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '端口号',
    `IMSI`    varchar(255)     NOT NULL DEFAULT '' COMMENT '用户识别码(IMSI)',
    `ICCID`   varchar(255)              DEFAULT '' COMMENT '卡识别码(ICCID)',
    `PhoNum`  varchar(255)              DEFAULT NULL COMMENT '手机号',
    PRIMARY KEY (`Id`)
) ENGINE = INNODB
  DEFAULT CHARSET = gbk;

CREATE TABLE IF NOT EXISTS `sms_recv`
(
    `Id`         int(11)      NOT NULL AUTO_INCREMENT,
    `PortNum`    int(11) unsigned      DEFAULT '0' COMMENT '接收短信的端口号',
    `PhoNum`     varchar(255)          DEFAULT '' COMMENT '手机号',
    `IMSI`       varchar(255)          DEFAULT NULL COMMENT '用户识别码(IMSI)',
    `ICCID`      varchar(255)          DEFAULT NULL COMMENT '卡识别码(ICCID)',
    `smsDate`    varchar(255) NOT NULL DEFAULT '' COMMENT '短信日期，注意是smsDate而不是smsData',
    `smsNumber`  varchar(255) NOT NULL DEFAULT '' COMMENT '短信号码',
    `smsContent` varchar(255) NOT NULL DEFAULT '' COMMENT '短信内容',
    PRIMARY KEY (`Id`)
) ENGINE = INNODB
  DEFAULT CHARSET = gbk;

CREATE TABLE IF NOT EXISTS `sms_send`
(
    `Id`         int(11)          NOT NULL AUTO_INCREMENT,
    `PortNum`    int(11)                   DEFAULT '-1' COMMENT '大于0表示指定端口号发送',
    `smsNumber`  varchar(255)     NOT NULL DEFAULT '' COMMENT '接收号码',
    `smsSubject` varchar(255)              DEFAULT '' COMMENT '彩信标题，如果发送彩信不能为空',
    `smsContent` varchar(255)     NOT NULL DEFAULT '' COMMENT '发送内容',
    `smsType`    int(11) unsigned          DEFAULT '0' COMMENT '0:短信 1:彩信',
    `PhoNum`     varchar(255)              DEFAULT NULL COMMENT '手机号',
    `smsState`   int(11) unsigned NOT NULL DEFAULT '0' COMMENT '0:未发送 1:已在发送队列 2:发送成功 3:发送失败',
    PRIMARY KEY (`Id`)
) ENGINE = INNODB
  DEFAULT CHARSET = gbk
  ROW_FORMAT = DYNAMIC;
