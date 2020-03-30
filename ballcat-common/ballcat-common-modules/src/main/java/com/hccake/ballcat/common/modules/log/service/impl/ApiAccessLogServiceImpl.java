package com.hccake.ballcat.common.modules.log.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hccake.ballcat.common.modules.log.mapper.ApiAccessLogMapper;
import com.hccake.ballcat.common.modules.log.model.entity.ApiAccessLog;
import com.hccake.ballcat.common.modules.log.service.ApiAccessLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 访问日志
 *
 * @author hccake
 * @date 2019-10-16 16:09:25
 */
@Slf4j
@Service
public class ApiAccessLogServiceImpl extends ServiceImpl<ApiAccessLogMapper, ApiAccessLog> implements ApiAccessLogService {

}
