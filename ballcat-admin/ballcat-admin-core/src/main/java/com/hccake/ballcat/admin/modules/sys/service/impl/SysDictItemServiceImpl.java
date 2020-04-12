package com.hccake.ballcat.admin.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hccake.ballcat.admin.modules.sys.mapper.SysDictItemMapper;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysDictItem;
import com.hccake.ballcat.admin.modules.sys.service.SysDictItemService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典项
 *
 * @author hccake
 * @date 2020-03-26 18:40:20
 */
@Service
public class SysDictItemServiceImpl extends ServiceImpl<SysDictItemMapper, SysDictItem> implements SysDictItemService {

    /**
    *  根据QueryObeject查询分页数据
    * @param page 分页参数
    * @param dictCode 查询参数对象
    * @return  分页数据
    */
    @Override
    public IPage<SysDictItem> page(IPage<SysDictItem> page, String dictCode) {
        LambdaQueryWrapper<SysDictItem> wrapper = Wrappers.<SysDictItem>lambdaQuery()
                .eq(SysDictItem::getDictCode, dictCode);
        return baseMapper.selectPage(page, wrapper);
    }


    /**
     * 根据Code查询对应字典项数据
     *
     * @param dictCode
     * @return
     */
    @Override
    public List<SysDictItem> getByDictCode(String dictCode) {
        return baseMapper.selectList(Wrappers.<SysDictItem>lambdaQuery()
                .eq(SysDictItem::getDictCode, dictCode));
    }

}
