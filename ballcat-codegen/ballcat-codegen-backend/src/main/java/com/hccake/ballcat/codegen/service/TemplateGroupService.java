package com.hccake.ballcat.codegen.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hccake.ballcat.codegen.model.entity.TemplateGroup;
import com.hccake.ballcat.codegen.model.vo.TemplateGroupVO;
import com.hccake.ballcat.codegen.model.qo.TemplateGroupQO;

/**
 * 模板组
 *
 * @author hccake
 * @date 2020-06-19 19:11:41
 */
public interface TemplateGroupService extends IService<TemplateGroup> {

    /**
    *  根据QueryObeject查询分页数据
    * @param page 分页参数
    * @param qo 查询参数对象
    * @return  分页数据
    */
    IPage<TemplateGroupVO> selectPageVo(IPage<?> page, TemplateGroupQO qo);

}