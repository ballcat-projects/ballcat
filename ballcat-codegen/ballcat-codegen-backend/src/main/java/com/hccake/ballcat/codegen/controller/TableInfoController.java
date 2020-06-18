package com.hccake.ballcat.codegen.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hccake.ballcat.codegen.model.qo.TableInfoQO;
import com.hccake.ballcat.codegen.model.vo.TableInfo;
import com.hccake.ballcat.codegen.service.TableInfoService;
import com.hccake.ballcat.common.core.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 表信息前端控制器
 *
 * @author hccake
 * @date 2020-06-17 10:24:47
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/tableinfo" )
@Api(value = "tableinfo", tags = "数据源管理")
public class TableInfoController {
    private final TableInfoService tableInfoService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param tableInfoQO 表信息查询对象
     * @return R
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R<IPage<TableInfo>> getDataSourceConfigPage(
            Page<?> page, TableInfoQO tableInfoQO) {
        return R.ok(tableInfoService.selectPageVo(page, tableInfoQO));
    }
}
