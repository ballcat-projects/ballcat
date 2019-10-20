package com.hccake.ballcat.admin.modules.log.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hccake.ballcat.common.core.result.R;
import com.hccake.ballcat.api.modules.api.model.entity.ApiAccessLog;
import com.hccake.ballcat.api.modules.log.service.ApiAccessLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 访问日志
 *
 * @author hccake
 * @date 2019-10-16 16:09:25
 */
@RestController
@AllArgsConstructor
@RequestMapping("/log/apiaccesslog")
@Api(value = "/log/apiaccesslog", tags = "访问日志管理")
public class ApiAccessLogController {

    private final ApiAccessLogService apiAccessLogService;

    /**
     * 分页查询
     *
     * @param page         分页对象
     * @param apiAccessLog 访问日志
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R<IPage<ApiAccessLog>> getAccessLogApiPage(
            Page<ApiAccessLog> page, ApiAccessLog apiAccessLog) {
        return R.ok(apiAccessLogService.page(page, Wrappers.query(apiAccessLog)));
    }


    /**
     * 通过id查询访问日志
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    public R<ApiAccessLog> getById(@PathVariable("id") Long id) {
        return R.ok(apiAccessLogService.getById(id));
    }


}
