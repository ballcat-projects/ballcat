package com.hccake.ballcat.codegen.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hccake.ballcat.codegen.model.entity.TemplateProperty;
import com.hccake.ballcat.codegen.model.vo.TemplatePropertyVO;
import com.hccake.ballcat.codegen.model.qo.TemplatePropertyQO;
import com.hccake.ballcat.codegen.mapper.TemplatePropertyMapper;
import com.hccake.ballcat.codegen.service.TemplatePropertyService;
import org.springframework.stereotype.Service;

/**
 * 模板属性配置
 *
 * @author hccake
 * @date 2020-06-22 15:46:39
 */
@Service
public class TemplatePropertyServiceImpl extends ServiceImpl<TemplatePropertyMapper, TemplateProperty> implements TemplatePropertyService {
    private final static String TABLE_ALIAS_PREFIX = "tp.";

    /**
    *  根据QueryObeject查询分页数据
    * @param page 分页参数
    * @param qo 查询参数对象
    * @return  分页数据
    */
    @Override
    public IPage<TemplatePropertyVO> selectPageVo(IPage<?> page, TemplatePropertyQO qo) {
        QueryWrapper<TemplateProperty> wrapper = Wrappers.<TemplateProperty>query()
				.eq(TABLE_ALIAS_PREFIX + "group_id", qo.getGroupId())
                .eq(ObjectUtil.isNotNull(qo.getId()), TABLE_ALIAS_PREFIX + "id", qo.getId());
        return baseMapper.selectPageVo(page, wrapper);
    }

}
