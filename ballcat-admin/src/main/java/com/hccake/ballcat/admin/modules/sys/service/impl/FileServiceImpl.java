package com.hccake.ballcat.admin.modules.sys.service.impl;

import com.hccake.ballcat.admin.modules.sys.service.FileService;
import com.hccake.ballcat.commom.storage.FileStorageClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/1/8 11:16
 */
@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileStorageClient fileStorageClient;

    /**
     * 文件上传
     *
     * @param file 待上传文件
     * @param objectName 文件对象名
     *
     */
    @Override
    public void uploadFile(MultipartFile file, String objectName) throws IOException {
        fileStorageClient.putObject(objectName, file.getInputStream());
    }
}
