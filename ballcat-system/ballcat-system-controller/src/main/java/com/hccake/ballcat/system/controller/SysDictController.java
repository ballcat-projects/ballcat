package com.hccake.ballcat.system.controller;

import com.hccake.ballcat.common.log.operation.annotation.CreateOperationLogging;
import com.hccake.ballcat.common.log.operation.annotation.DeleteOperationLogging;
import com.hccake.ballcat.common.log.operation.annotation.UpdateOperationLogging;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.common.model.result.BaseResultCode;
import com.hccake.ballcat.common.model.result.R;
import com.hccake.ballcat.system.manager.SysDictManager;
import com.hccake.ballcat.system.model.entity.SysDict;
import com.hccake.ballcat.system.model.entity.SysDictItem;
import com.hccake.ballcat.system.model.qo.SysDictQO;
import com.hccake.ballcat.system.model.vo.DictDataVO;
import com.hccake.ballcat.system.model.vo.SysDictItemPageVO;
import com.hccake.ballcat.system.model.vo.SysDictPageVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 字典表
 *
 * @author hccake
 * @date 2020-03-26 18:40:20
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/dict")
@Tag(name = "字典表管理")
public class SysDictController {

	private final SysDictManager sysDictManager;

	/**
	 * 通过字典标识查找对应字典项
	 * @param dictCodes 字典标识列表
	 * @return 同类型字典
	 */
	@GetMapping("/data")
	public R<List<DictDataVO>> getDictData(@RequestParam("dictCodes") String[] dictCodes) {
		return R.ok(sysDictManager.queryDictDataAndHashVO(dictCodes));
	}

	/**
	 * 通过字典标识查找对应字典项
	 * @param dictHashCode 字典标识
	 * @return 同类型字典
	 */
	@PostMapping("/invalid-hash")
	public R<List<String>> invalidDictHash(@RequestBody Map<String, String> dictHashCode) {
		return R.ok(sysDictManager.invalidDictHash(dictHashCode));
	}

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param sysDictQO 字典查询参数
	 * @return R<PageResult<SysDictVO>>
	 */
	@GetMapping("/page")
	@PreAuthorize("@per.hasPermission('system:dict:read')")
	@Operation(summary = "分页查询", description = "分页查询")
	public R<PageResult<SysDictPageVO>> getSysDictPage(PageParam pageParam, SysDictQO sysDictQO) {
		return R.ok(sysDictManager.dictPage(pageParam, sysDictQO));
	}

	/**
	 * 新增字典表
	 * @param sysDict 字典表
	 * @return R
	 */
	@CreateOperationLogging(msg = "新增字典表")
	@PostMapping
	@PreAuthorize("@per.hasPermission('system:dict:add')")
	@Operation(summary = "新增字典表", description = "新增字典表")
	public R<Void> save(@RequestBody SysDict sysDict) {
		return sysDictManager.dictSave(sysDict) ? R.ok() : R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "新增字典表失败");
	}

	/**
	 * 修改字典表
	 * @param sysDict 字典表
	 * @return R
	 */
	@UpdateOperationLogging(msg = "修改字典表")
	@PutMapping
	@PreAuthorize("@per.hasPermission('system:dict:edit')")
	@Operation(summary = "修改字典表", description = "修改字典表")
	public R<Void> updateById(@RequestBody SysDict sysDict) {
		return sysDictManager.updateDictById(sysDict) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "修改字典表失败");
	}

	/**
	 * 通过id删除字典表
	 * @param id id
	 * @return R
	 */
	@DeleteOperationLogging(msg = "通过id删除字典表")
	@DeleteMapping("/{id}")
	@PreAuthorize("@per.hasPermission('system:dict:del')")
	@Operation(summary = "通过id删除字典表", description = "通过id删除字典表")
	public R<Void> removeById(@PathVariable("id") Integer id) {
		sysDictManager.removeDictById(id);
		return R.ok();
	}

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param dictCode 字典标识
	 * @return R
	 */
	@GetMapping("/item/page")
	@PreAuthorize("@per.hasPermission('system:dict:read')")
	@Operation(summary = "分页查询", description = "分页查询")
	public R<PageResult<SysDictItemPageVO>> getSysDictItemPage(PageParam pageParam,
			@RequestParam("dictCode") String dictCode) {
		return R.ok(sysDictManager.dictItemPage(pageParam, dictCode));
	}

	/**
	 * 新增字典项
	 * @param sysDictItem 字典项
	 * @return R
	 */
	@CreateOperationLogging(msg = "新增字典项")
	@PostMapping("item")
	@PreAuthorize("@per.hasPermission('system:dict:add')")
	@Operation(summary = "新增字典项", description = "新增字典项")
	public R<Void> saveItem(@RequestBody SysDictItem sysDictItem) {
		return sysDictManager.saveDictItem(sysDictItem) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "新增字典项失败");
	}

	/**
	 * 修改字典项
	 * @param sysDictItem 字典项
	 * @return R
	 */
	@UpdateOperationLogging(msg = "修改字典项")
	@PutMapping("item")
	@PreAuthorize("@per.hasPermission('system:dict:edit')")
	@Operation(summary = "修改字典项", description = "修改字典项")
	public R<Void> updateItemById(@RequestBody SysDictItem sysDictItem) {
		return sysDictManager.updateDictItemById(sysDictItem) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "修改字典项失败");
	}

	/**
	 * 通过id删除字典项
	 * @param id id
	 * @return R
	 */
	@DeleteOperationLogging(msg = "通过id删除字典项")
	@DeleteMapping("/item/{id}")
	@PreAuthorize("@per.hasPermission('system:dict:del')")
	@Operation(summary = "通过id删除字典项", description = "通过id删除字典项")
	public R<Void> removeItemById(@PathVariable("id") Integer id) {
		return sysDictManager.removeDictItemById(id) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "通过id删除字典项失败");
	}

	/**
	 * 通过id修改字典项状态
	 * @param id id
	 * @return R
	 */
	@UpdateOperationLogging(msg = "通过id修改字典项状态")
	@PatchMapping("/item/{id}")
	@PreAuthorize("@per.hasPermission('system:dict:edit')")
	@Operation(summary = "通过id修改字典项状态", description = "通过id修改字典项状态")
	public R<Void> updateDictItemStatusById(@PathVariable("id") Integer id, @RequestParam("status") Integer status) {
		sysDictManager.updateDictItemStatusById(id, status);
		return R.ok();
	}

}
