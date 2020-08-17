package com.hccake.ballcat.admin.modules.lov.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hccake.ballcat.admin.modules.lov.mapper.LovBodyMapper;
import com.hccake.ballcat.admin.modules.lov.model.entity.LovBody;
import com.hccake.ballcat.admin.modules.lov.service.LovBodyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author lingting 2020-08-10 17:21
 */
@Service
@RequiredArgsConstructor
public class LovBodyServiceImpl extends ServiceImpl<LovBodyMapper, LovBody> implements LovBodyService {

}
