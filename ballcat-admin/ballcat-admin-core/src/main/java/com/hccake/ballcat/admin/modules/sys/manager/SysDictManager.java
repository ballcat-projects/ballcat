package com.hccake.ballcat.admin.modules.sys.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysDict;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysDictItem;
import com.hccake.ballcat.admin.modules.sys.model.qo.SysDictQO;
import com.hccake.ballcat.admin.modules.sys.service.SysDictItemService;
import com.hccake.ballcat.admin.modules.sys.service.SysDictService;
import com.hccake.ballcat.common.core.vo.SelectData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/3/27 19:50
 */
@Service
@RequiredArgsConstructor
public class SysDictManager {
    private final SysDictService sysDictService;
    private final SysDictItemService sysDictItemService;

    /**
     * 字典Select数据
     * @param dictCode 字典标识
     * @return 字典项集合组成的SelectData
     */
    public List<SelectData> querySelectDataByDictCode(String dictCode) {
        return sysDictItemService.querySelectDataByDictCode(dictCode);
    }

    /**
     * 字典表分页
     * @param page 分页参数
     * @param sysDictQO  查询参数
     * @return 字典表分页数据
     */
    public IPage<SysDict> dictPage(Page<SysDict> page, SysDictQO sysDictQO) {
        return sysDictService.page(page, sysDictQO);
    }

    /**
     * 保存字典
     * @param sysDict 字典对象
     * @return 执行是否成功
     */
    public boolean dictSave(SysDict sysDict) {
        return sysDictService.save(sysDict);
    }

    /**
     * 更新字典
     * @param sysDict 字典对象
     * @return 执行是否成功
     */
    public boolean updateDictById(SysDict sysDict) {
        return sysDictService.updateById(sysDict);
    }

    /**
     * 删除字典
     * @param id 字典id
     * @return 执行是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean removeDictById(Integer id) {
        SysDict oldData = sysDictService.getById(id);
        if(sysDictService.removeById(id)){
            // 级联删除对应的字典项
            sysDictItemService.remove(Wrappers.<SysDictItem>lambdaUpdate()
                    .eq(SysDictItem::getDictCode, oldData.getCode())
            );
            return true;
        }
        return false;
    }

    /**
     * 字典项分页
     * @param page  分页属性
     * @param dictCode 字典标识
     * @return 字典项分页数据
     */
    public IPage<SysDictItem> dictItemPage(Page<SysDictItem> page, String dictCode) {
        return sysDictItemService.page(page, dictCode);
    }

    /**
     * 新增字典项
     * @param sysDictItem 字典项
     * @return 执行是否成功
     */
    public boolean saveDictItem(SysDictItem sysDictItem) {
        return sysDictItemService.save(sysDictItem);
    }


    /**
     * 更新字典项
     * @param sysDictItem 字典项
     * @return 执行是否成功
     */
    public boolean updateDictItemById(SysDictItem sysDictItem) {
        return sysDictItemService.updateById(sysDictItem);
    }


    /**
     * 删除字典项
     * @param id 字典项
     * @return 执行是否成功
     */
    public boolean removeDictItemById(Integer id) {
        return sysDictItemService.removeById(id);
    }

}
