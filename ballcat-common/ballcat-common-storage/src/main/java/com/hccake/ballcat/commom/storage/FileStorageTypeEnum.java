package com.hccake.ballcat.commom.storage;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/5/21 15:13
 * 文件存储类型枚举
 * 暂时只写了阿里云的实现
 */
@Getter
@AllArgsConstructor
public enum FileStorageTypeEnum {

    /**
     * 阿里云
     */
    ALIYUN
}
