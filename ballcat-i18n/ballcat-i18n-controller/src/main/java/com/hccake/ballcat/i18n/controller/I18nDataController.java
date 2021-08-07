package com.hccake.ballcat.i18n.controller;

import com.hccake.ballcat.common.log.operation.annotation.CreateOperationLogging;
import com.hccake.ballcat.common.log.operation.annotation.DeleteOperationLogging;
import com.hccake.ballcat.common.log.operation.annotation.UpdateOperationLogging;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.common.model.result.BaseResultCode;
import com.hccake.ballcat.common.model.result.R;
import com.hccake.ballcat.i18n.model.entity.I18nData;
import com.hccake.ballcat.i18n.model.qo.I18nDataQO;
import com.hccake.ballcat.i18n.model.vo.I18nDataPageVO;
import com.hccake.ballcat.i18n.service.I18nDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 国际化信息
 *
 * @author hccake 2021-08-06 10:48:25
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/i18n/i18n-data")
@Api(value = "i18n-data", tags = "国际化信息管理")
public class I18nDataController {

	private final I18nDataService i18nDataService;

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param i18nDataQO 国际化信息查询对象
	 * @return R 通用返回体
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@per.hasPermission('i18n:i18n-data:read')")
	public R<PageResult<I18nDataPageVO>> getI18nDataPage(PageParam pageParam, I18nDataQO i18nDataQO) {
		return R.ok(i18nDataService.queryPage(pageParam, i18nDataQO));
	}

	/**
	 * 新增国际化信息
	 * @param i18nData 国际化信息
	 * @return R 通用返回体
	 */
	@ApiOperation(value = "新增国际化信息", notes = "新增国际化信息")
	@CreateOperationLogging(msg = "新增国际化信息")
	@PostMapping
	@PreAuthorize("@per.hasPermission('i18n:i18n-data:add')")
	public R save(@RequestBody I18nData i18nData) {
		return i18nDataService.save(i18nData) ? R.ok() : R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "新增国际化信息失败");
	}

	/**
	 * 修改国际化信息
	 * @param i18nData 国际化信息
	 * @return R 通用返回体
	 */
	@ApiOperation(value = "修改国际化信息", notes = "修改国际化信息")
	@UpdateOperationLogging(msg = "修改国际化信息")
	@PutMapping
	@PreAuthorize("@per.hasPermission('i18n:i18n-data:edit')")
	public R updateById(@RequestBody I18nData i18nData) {
		return i18nDataService.updateById(i18nData) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "修改国际化信息失败");
	}

	/**
	 * 通过id删除国际化信息
	 * @param code code 唯一标识
	 * @param languageTag 语言标签
	 * @return R 通用返回体
	 */
	@ApiOperation(value = "通过id删除国际化信息", notes = "通过id删除国际化信息")
	@DeleteOperationLogging(msg = "通过id删除国际化信息")
	@DeleteMapping
	@PreAuthorize("@per.hasPermission('i18n:i18n-data:del')")
	public R removeById(@RequestParam("code") String code, @RequestParam("languageTag") String languageTag) {
		return i18nDataService.removeByCodeAndLanguageTag(code, languageTag) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "通过id删除国际化信息失败");
	}

}