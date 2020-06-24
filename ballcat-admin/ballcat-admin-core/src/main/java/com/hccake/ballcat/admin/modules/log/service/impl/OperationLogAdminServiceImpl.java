package com.hccake.ballcat.admin.modules.log.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hccake.ballcat.admin.modules.log.mapper.AdminOperationLogMapper;
import com.hccake.ballcat.admin.modules.log.model.entity.AdminOperationLog;
import com.hccake.ballcat.admin.modules.log.service.OperationLogAdminService;
import org.springframework.stereotype.Service;

/**
 * 操作日志
 *
 * @author hccake
 * @date 2019-10-15 20:42:32
 */
@Service
public class OperationLogAdminServiceImpl extends ServiceImpl<AdminOperationLogMapper, AdminOperationLog>
		implements OperationLogAdminService {

}
