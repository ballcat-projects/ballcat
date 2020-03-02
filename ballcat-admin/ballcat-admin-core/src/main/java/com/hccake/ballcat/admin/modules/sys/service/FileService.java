package com.hccake.ballcat.admin.modules.sys.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/1/8 11:15
 */
public interface FileService {
    /**
     * 文件上传
     * @param file 待上传文件
     * @param objectName 文件对象名
     *
     */
    void uploadFile(MultipartFile file, String objectName) throws IOException;
}
