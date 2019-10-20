package com.hccake.ballcat.admin.modules.log.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hccake.ballcat.admin.modules.log.model.entity.AdminOperationLog;
import com.hccake.ballcat.commom.log.operation.service.OperationLogSaveService;

/**
 * 操作日志
 *
 * @author hccake
 * @date 2019-10-15 20:42:32
 */
public interface OperationLogAdminService extends IService<AdminOperationLog>, OperationLogSaveService {

}
