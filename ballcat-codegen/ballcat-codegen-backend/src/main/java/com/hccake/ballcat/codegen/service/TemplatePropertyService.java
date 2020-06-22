package com.hccake.ballcat.codegen.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hccake.ballcat.codegen.model.entity.TemplateProperty;
import com.hccake.ballcat.codegen.model.vo.TemplatePropertyVO;
import com.hccake.ballcat.codegen.model.qo.TemplatePropertyQO;

/**
 * 模板属性配置
 *
 * @author hccake
 * @date 2020-06-22 15:46:39
 */
public interface TemplatePropertyService extends IService<TemplateProperty> {

    /**
    *  根据QueryObeject查询分页数据
    * @param page 分页参数
    * @param qo 查询参数对象
    * @return  分页数据
    */
    IPage<TemplatePropertyVO> selectPageVo(IPage<?> page, TemplatePropertyQO qo);

}