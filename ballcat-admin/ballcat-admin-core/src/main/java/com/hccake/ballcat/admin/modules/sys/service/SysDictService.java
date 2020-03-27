package com.hccake.ballcat.admin.modules.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysDict;
import com.hccake.ballcat.admin.modules.sys.model.qo.SysDictQO;

/**
 * 字典表
 *
 * @author hccake
 * @date 2020-03-26 18:40:20
 */
public interface SysDictService extends IService<SysDict> {

    /**
    *  根据QueryObeject查询分页数据
    * @param page 分页参数
    * @param qo 查询参数对象
    * @return  分页数据
    */
    IPage<SysDict> page(IPage<SysDict> page, SysDictQO qo);

}
