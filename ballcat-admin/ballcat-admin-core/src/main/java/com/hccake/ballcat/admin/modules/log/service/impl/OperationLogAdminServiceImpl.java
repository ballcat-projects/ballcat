package com.hccake.ballcat.admin.modules.log.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.hccake.ballcat.admin.modules.log.mapper.AdminOperationLogMapper;
import com.hccake.ballcat.admin.modules.log.model.entity.AdminOperationLog;
import com.hccake.ballcat.admin.modules.log.service.OperationLogAdminService;
import com.hccake.ballcat.admin.modules.sys.model.converter.OperationLogConverter;
import com.hccake.ballcat.commom.log.operation.model.OperationLogDTO;
import org.springframework.stereotype.Service;

/**
 * 操作日志
 *
 * @author hccake
 * @date 2019-10-15 20:42:32
 */
@Service
public class OperationLogAdminServiceImpl extends ServiceImpl<AdminOperationLogMapper, AdminOperationLog> implements OperationLogAdminService {

    /**
     * 保存操作日志
     *
     * @param operationLogDTO
     * @return true/false
     */
    @Override
    public boolean saveLog(OperationLogDTO operationLogDTO) {
        AdminOperationLog adminOperationLog = OperationLogConverter.INSTANCE.dtoToPo(operationLogDTO);
        return SqlHelper.retBool(baseMapper.insert(adminOperationLog));
    }
}
