package com.hccake.ballcat.codegen.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hccake.ballcat.codegen.model.entity.TemplateGroup;
import com.hccake.ballcat.codegen.model.vo.TemplateGroupVO;
import com.hccake.ballcat.codegen.model.qo.TemplateGroupQO;
import com.hccake.ballcat.codegen.mapper.TemplateGroupMapper;
import com.hccake.ballcat.codegen.service.TemplateGroupService;
import org.springframework.stereotype.Service;

/**
 * 模板组
 *
 * @author hccake
 * @date 2020-06-19 19:11:41
 */
@Service
public class TemplateGroupServiceImpl extends ServiceImpl<TemplateGroupMapper, TemplateGroup> implements TemplateGroupService {
    private final static String TABLE_ALIAS_PREFIX = "tg.";

    /**
    *  根据QueryObeject查询分页数据
    * @param page 分页参数
    * @param qo 查询参数对象
    * @return  分页数据
    */
    @Override
    public IPage<TemplateGroupVO> selectPageVo(IPage<?> page, TemplateGroupQO qo) {
        QueryWrapper<TemplateGroup> wrapper = Wrappers.<TemplateGroup>query()
                .eq(ObjectUtil.isNotNull(qo.getId()), TABLE_ALIAS_PREFIX + "Id", qo.getId());
        return baseMapper.selectPageVo(page, wrapper);
    }

}