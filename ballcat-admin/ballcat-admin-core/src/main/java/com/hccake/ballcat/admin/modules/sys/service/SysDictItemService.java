package com.hccake.ballcat.admin.modules.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysDictItem;

import java.util.List;

/**
 * 字典项
 *
 * @author hccake
 * @date 2020-03-26 18:40:20
 */
public interface SysDictItemService extends IService<SysDictItem> {

    /**
    *  根据QueryObeject查询分页数据
    * @param page 分页参数
    * @param dictCode 查询参数对象
    * @return  分页数据
    */
    IPage<SysDictItem> page(IPage<SysDictItem> page, String dictCode);


    /**
     * 根据Code查询对应字典项数据
     * @param dictCode
     * @return
     */
    List<SysDictItem> getByDictCode(String dictCode);
}
