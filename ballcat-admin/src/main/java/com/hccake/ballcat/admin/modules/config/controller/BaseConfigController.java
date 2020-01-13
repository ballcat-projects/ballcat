package com.hccake.ballcat.admin.modules.config.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hccake.ballcat.api.modules.config.model.entity.BaseConfig;
import com.hccake.ballcat.api.modules.config.service.BaseConfigService;
import com.hccake.ballcat.commom.log.operation.annotation.OperationLogging;
import com.hccake.ballcat.common.core.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * 系统配置表
 *
 * @author ballcat code generator
 * @date 2019-10-14 17:42:23
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("config/baseconfig")
@Api(value = "config/baseconfig", tags = "系统配置")
public class BaseConfigController {
    private final BaseConfigService baseConfigService;

    /**
     * 分页查询
     *
     * @param page       分页对象
     * @param baseConfig 系统配置表
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R<IPage<BaseConfig>> getSysConfigPage(
            Page<BaseConfig> page, BaseConfig baseConfig) {
        return R.ok(baseConfigService.page(page, Wrappers.query(baseConfig)));
    }

    /**
     * 通过id查询系统配置表
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    public R<BaseConfig> getById(@PathVariable("id") Integer id) {
        return R.ok(baseConfigService.getById(id));
    }

    /**
     * 新增系统配置表
     *
     * @param baseConfig 系统配置表
     * @return R
     */
    @ApiOperation(value = "新增系统配置表", notes = "新增系统配置表")
    @OperationLogging("新增系统配置表")
    @PostMapping
    @PreAuthorize("@per.hasPermission('config_baseconfig_add')")
    public R save(@RequestBody BaseConfig baseConfig) {
        return R.ok(baseConfigService.save(baseConfig));
    }

    /**
     * 修改系统配置表
     *
     * @param baseConfig 系统配置表
     * @return R
     */
    @ApiOperation(value = "修改系统配置表", notes = "修改系统配置表")
    @OperationLogging("修改系统配置表")
    @PutMapping
    @PreAuthorize("@per.hasPermission('config_baseconfig_edit')")
    public R updateById(@RequestBody BaseConfig baseConfig) {
        return R.ok(baseConfigService.updateById(baseConfig));
    }

    /**
     * 通过id删除系统配置表
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除系统配置表", notes = "通过id删除系统配置表")
    @OperationLogging("通过id删除系统配置表")
    @DeleteMapping("/{id}")
    @PreAuthorize("@per.hasPermission('config_baseconfig_del')")
    public R removeById(@PathVariable Integer id) {
        return R.ok(baseConfigService.removeById(id));
    }

}
