package com.hccake.ballcat.admin.modules.sys.model.converter;

import com.hccake.ballcat.admin.modules.log.model.entity.AdminOperationLog;
import com.hccake.ballcat.commom.log.operation.model.OperationLogDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/15 21:03
 */
@Mapper
public interface OperationLogConverter {

    OperationLogConverter INSTANCE = Mappers.getMapper(OperationLogConverter.class);

    /**
     * 转换OperationLog 为 OperationLogAdmin
     * @param operationLogDTO
     * @return
     */
    AdminOperationLog dtoToPo(OperationLogDTO operationLogDTO);

}
