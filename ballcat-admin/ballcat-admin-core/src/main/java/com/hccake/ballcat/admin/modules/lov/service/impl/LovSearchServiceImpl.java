package com.hccake.ballcat.admin.modules.lov.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hccake.ballcat.admin.modules.lov.mapper.LovSearchMapper;
import com.hccake.ballcat.admin.modules.lov.model.entity.LovSearch;
import com.hccake.ballcat.admin.modules.lov.service.LovSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author lingting 2020-08-10 17:21
 */
@Service
@RequiredArgsConstructor
public class LovSearchServiceImpl extends ServiceImpl<LovSearchMapper, LovSearch> implements LovSearchService {

}
