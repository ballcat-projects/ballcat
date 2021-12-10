package com.hccake.ballcat.i18n.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.hccake.ballcat.common.core.constant.enums.ImportModeEnum;
import com.hccake.ballcat.common.log.operation.annotation.CreateOperationLogging;
import com.hccake.ballcat.common.log.operation.annotation.DeleteOperationLogging;
import com.hccake.ballcat.common.log.operation.annotation.UpdateOperationLogging;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.common.model.result.BaseResultCode;
import com.hccake.ballcat.common.model.result.R;
import com.hccake.ballcat.i18n.converter.I18nDataConverter;
import com.hccake.ballcat.i18n.model.dto.I18nDataCreateDTO;
import com.hccake.ballcat.i18n.model.dto.I18nDataDTO;
import com.hccake.ballcat.i18n.model.entity.I18nData;
import com.hccake.ballcat.i18n.model.qo.I18nDataQO;
import com.hccake.ballcat.i18n.model.vo.I18nDataExcelVO;
import com.hccake.ballcat.i18n.model.vo.I18nDataPageVO;
import com.hccake.ballcat.i18n.service.I18nDataService;
import com.hccake.common.excel.annotation.RequestExcel;
import com.hccake.common.excel.annotation.ResponseExcel;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 国际化信息
 *
 * @author hccake 2021-08-06 10:48:25
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/i18n/i18n-data")
@Tag(name = "国际化信息管理")
public class I18nDataController {

	private final I18nDataService i18nDataService;

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param i18nDataQO 国际化信息查询对象
	 * @return R 通用返回体
	 */
	@GetMapping("/page")
	@PreAuthorize("@per.hasPermission('i18n:i18n-data:read')")
	@Operation(summary = "分页查询", description = "分页查询")
	public R<PageResult<I18nDataPageVO>> getI18nDataPage(PageParam pageParam, I18nDataQO i18nDataQO) {
		return R.ok(i18nDataService.queryPage(pageParam, i18nDataQO));
	}

	/**
	 * 查询指定国际化标识的所有数据
	 * @param code 国际化标识
	 * @return R 通用返回体
	 */
	@GetMapping("/list")
	@PreAuthorize("@per.hasPermission('i18n:i18n-data:read')")
	@Operation(summary = "查询指定国际化标识的所有数据", description = "查询指定国际化标识的所有数据")
	public R<List<I18nData>> listByCode(@RequestParam("code") String code) {
		return R.ok(i18nDataService.listByCode(code));
	}

	/**
	 * 新增国际化信息
	 * @param i18nDataCreateDTO 国际化信息
	 * @return R 通用返回体
	 */
	@CreateOperationLogging(msg = "新增国际化信息")
	@PostMapping
	@PreAuthorize("@per.hasPermission('i18n:i18n-data:add')")
	@Operation(summary = "新增国际化信息", description = "新增国际化信息")
	public R save(@Valid @RequestBody I18nDataCreateDTO i18nDataCreateDTO) {
		// 转换为实体类列表
		List<I18nData> list = new ArrayList<>();
		List<I18nDataCreateDTO.LanguageText> languageTexts = i18nDataCreateDTO.getLanguageTexts();
		for (I18nDataCreateDTO.LanguageText languageText : languageTexts) {
			I18nData i18nData = new I18nData();
			i18nData.setCode(i18nDataCreateDTO.getCode());
			i18nData.setRemarks(i18nDataCreateDTO.getRemarks());
			i18nData.setLanguageTag(languageText.getLanguageTag());
			i18nData.setMessage(languageText.getMessage());
			list.add(i18nData);
		}
		return i18nDataService.saveBatch(list) ? R.ok() : R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "新增国际化信息失败");
	}

	/**
	 * 修改国际化信息
	 * @param i18nDataDTO 国际化信息
	 * @return R 通用返回体
	 */
	@UpdateOperationLogging(msg = "修改国际化信息")
	@PutMapping
	@PreAuthorize("@per.hasPermission('i18n:i18n-data:edit')")
	@Operation(summary = "修改国际化信息", description = "修改国际化信息")
	public R updateById(@RequestBody I18nDataDTO i18nDataDTO) {
		return i18nDataService.updateByCodeAndLanguageTag(i18nDataDTO) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "修改国际化信息失败");
	}

	/**
	 * 通过id删除国际化信息
	 * @param code code 唯一标识
	 * @param languageTag 语言标签
	 * @return R 通用返回体
	 */
	@DeleteOperationLogging(msg = "通过id删除国际化信息")
	@DeleteMapping
	@PreAuthorize("@per.hasPermission('i18n:i18n-data:del')")
	@Operation(summary = "通过id删除国际化信息", description = "通过id删除国际化信息")
	public R removeById(@RequestParam("code") String code, @RequestParam("languageTag") String languageTag) {
		return i18nDataService.removeByCodeAndLanguageTag(code, languageTag) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "通过id删除国际化信息失败");
	}

	/**
	 * 导入国际化信息
	 * @return R 通用返回体
	 */
	@PostMapping("/import")
	@PreAuthorize("@per.hasPermission('i18n:i18n-data:import')")
	@Operation(summary = "导入国际化信息", description = "导入国际化信息")
	public R<?> importI18nData(@RequestExcel List<I18nDataExcelVO> excelVos,
			@RequestParam("importMode") ImportModeEnum importModeEnum) {

		if (CollectionUtil.isEmpty(excelVos)) {
			return R.ok();
		}

		// 转换结构
		List<I18nData> list = excelVos.stream().map(I18nDataConverter.INSTANCE::excelVoToPo)
				.collect(Collectors.toList());

		// 跳过已有数据，返回已有数据列表
		if (importModeEnum == ImportModeEnum.SKIP_EXISTING) {
			List<I18nData> existsList = i18nDataService.saveWhenNotExist(list);
			return R.ok(existsList);
		}

		// 覆盖已有数据
		if (importModeEnum == ImportModeEnum.OVERWRITE_EXISTING) {
			i18nDataService.saveOrUpdate(list);
		}

		return R.ok();
	}

	/**
	 * 导出国际化信息
	 * @param i18nDataQO 国际化信息查询对象
	 * @return List<I18nDataExcelVO>
	 */
	@ResponseExcel(name = "国际化信息", i18nHeader = true)
	@GetMapping("/export")
	@PreAuthorize("@per.hasPermission('i18n:i18n-data:export')")
	@Operation(summary = "导出国际化信息", description = "导出国际化信息")
	public List<I18nDataExcelVO> exportI18nData(I18nDataQO i18nDataQO) {
		List<I18nData> list = i18nDataService.queryList(i18nDataQO);
		if (CollectionUtil.isEmpty(list)) {
			return new ArrayList<>();
		}
		// 转换为 excel vo 对象
		return list.stream().map(I18nDataConverter.INSTANCE::poToExcelVo).collect(Collectors.toList());
	}

	/**
	 * 国际化 excel 模板
	 * @return List<I18nDataExcelVO>
	 */
	@ResponseExcel(name = "国际化信息模板", i18nHeader = true)
	@GetMapping("/excel-template")
	@PreAuthorize("@per.hasPermission('i18n:i18n-data:import')")
	@Operation(summary = "国际化信息 Excel 模板", description = "国际化信息 Excel 模板")
	public List<I18nDataExcelVO> excelTemplate() {
		List<I18nDataExcelVO> list = new ArrayList<>();
		list.add(new I18nDataExcelVO());
		return list;
	}

}