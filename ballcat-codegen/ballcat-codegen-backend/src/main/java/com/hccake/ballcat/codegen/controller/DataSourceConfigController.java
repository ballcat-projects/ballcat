package com.hccake.ballcat.codegen.controller;

import com.hccake.ballcat.codegen.model.dto.DataSourceConfigDTO;
import com.hccake.ballcat.codegen.model.entity.DataSourceConfig;
import com.hccake.ballcat.codegen.model.qo.DataSourceConfigQO;
import com.hccake.ballcat.codegen.model.vo.DataSourceConfigVO;
import com.hccake.ballcat.codegen.service.DataSourceConfigService;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.common.model.result.BaseResultCode;
import com.hccake.ballcat.common.model.result.R;
import com.hccake.ballcat.common.model.domain.SelectData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据源
 *
 * @author hccake
 * @date 2020-06-17 10:24:47
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/gen/datasource-config")
@Api(value = "datasource-config", tags = "数据源管理")
public class DataSourceConfigController {

	private final DataSourceConfigService dataSourceConfigService;

	/**
	 * 分页查询
	 * @param pageParam 分页对象
	 * @param dataSourceConfigQO 数据源
	 * @return R
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	// @PreAuthorize("@per.hasPermission('gen:datasourceconfig:read')" )
	public R<PageResult<DataSourceConfigVO>> getDataSourceConfigPage(PageParam pageParam,
			DataSourceConfigQO dataSourceConfigQO) {
		return R.ok(dataSourceConfigService.queryPage(pageParam, dataSourceConfigQO));
	}

	/**
	 * 通过id查询数据源
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id查询", notes = "通过id查询")
	@GetMapping("/{id}")
	// @PreAuthorize("@per.hasPermission('gen:datasourceconfig:read')" )
	public R<DataSourceConfig> getById(@PathVariable("id") Integer id) {
		return R.ok(dataSourceConfigService.getById(id));
	}

	/**
	 * 新增数据源
	 * @param dataSourceConfigDTO 数据源
	 * @return R
	 */
	@ApiOperation(value = "新增数据源", notes = "新增数据源")
	// @CreateOperationLogging(msg = "新增数据源" )
	@PostMapping
	// @PreAuthorize("@per.hasPermission('gen:datasourcecofig:add')" )
	public R save(@RequestBody DataSourceConfigDTO dataSourceConfigDTO) {
		return dataSourceConfigService.save(dataSourceConfigDTO) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "新增数据源失败");
	}

	/**
	 * 修改数据源
	 * @param dataSourceConfigDTO 数据源
	 * @return R
	 */
	@ApiOperation(value = "修改数据源", notes = "修改数据源")
	// @UpdateOperationLogging(msg = "修改数据源" )
	@PutMapping
	// @PreAuthorize("@per.hasPermission('gen:datasourceconfig:edit')" )
	public R updateById(@RequestBody DataSourceConfigDTO dataSourceConfigDTO) {
		return dataSourceConfigService.update(dataSourceConfigDTO) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "修改数据源失败");
	}

	/**
	 * 通过id删除数据源
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id删除数据源", notes = "通过id删除数据源")
	// @DeleteOperationLogging(msg = "通过id删除数据源" )
	@DeleteMapping("/{id}")
	// @PreAuthorize("@per.hasPermission('gen:datasourceconfig:del')" )
	public R removeById(@PathVariable Integer id) {
		return dataSourceConfigService.removeById(id) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "通过id删除数据源失败");
	}

	/**
	 * 获取selectData数据
	 * @return R
	 */
	@ApiOperation(value = "获取selectData数据", notes = "获取selectData数据")
	@GetMapping("/select")
	public R<List<SelectData<?>>> listSelectData() {
		List<SelectData<?>> selectDataList = dataSourceConfigService.listSelectData();
		return R.ok(selectDataList);
	}

}
