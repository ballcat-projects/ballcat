package com.hccake.ballcat.admin.modules.system.controller;

import com.hccake.ballcat.admin.modules.system.converter.SysLovConverter;
import com.hccake.ballcat.admin.modules.system.model.dto.SysLovDTO;
import com.hccake.ballcat.admin.modules.system.model.entity.SysLov;
import com.hccake.ballcat.admin.modules.system.model.qo.SysLovQO;
import com.hccake.ballcat.admin.modules.system.model.vo.LovInfoVO;
import com.hccake.ballcat.admin.modules.system.model.vo.SysLovPageVO;
import com.hccake.ballcat.admin.modules.system.service.SysLovBodyService;
import com.hccake.ballcat.admin.modules.system.service.SysLovSearchService;
import com.hccake.ballcat.admin.modules.system.service.SysLovService;
import com.hccake.ballcat.commom.log.operation.annotation.CreateOperationLogging;
import com.hccake.ballcat.commom.log.operation.annotation.DeleteOperationLogging;
import com.hccake.ballcat.commom.log.operation.annotation.UpdateOperationLogging;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.common.model.result.BaseResultCode;
import com.hccake.ballcat.common.model.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lingting 2020-08-10 16:40
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/lov")
@Api(value = "system-lov", tags = "弹窗选择器")
public class SysLovController {

	private final SysLovService sysLovService;

	private final SysLovBodyService bodyService;

	private final SysLovSearchService searchService;

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param qo 查询对象
	 * @return R 通用返回体
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@per.hasPermission('system:lov:read')")
	public R<PageResult<SysLovPageVO>> getLovPage(PageParam pageParam, SysLovQO qo) {
		return R.ok(sysLovService.queryPage(pageParam, qo));
	}

	@ApiOperation("根据keyword获取lov数据")
	@GetMapping("/data/{keyword}")
	public R<LovInfoVO> getDataByKeyword(@PathVariable("keyword") String keyword) {

		SysLov sysLov = sysLovService.getByKeyword(keyword);
		if (sysLov == null) {
			return R.failed(BaseResultCode.UNKNOWN_ERROR, "获取失败!");
		}
		// 封装VO
		LovInfoVO lovInfoVO = SysLovConverter.INSTANCE.poToInfoVO(sysLov);
		lovInfoVO.setBodyList(bodyService.listByKeyword(keyword));
		lovInfoVO.setSearchList(searchService.listByKeyword(keyword));

		return R.ok(lovInfoVO);
	}

	@PostMapping("/check")
	@ApiOperation("检查本地缓存中指定keyword的lov是否已过期")
	public R<List<String>> check(@RequestBody Map<String, LocalDateTime> map) {
		List<SysLov> list = new ArrayList<>(map.size());
		map.forEach((keyword, updateTime) -> list.add(new SysLov().setKeyword(keyword).setUpdateTime(updateTime)));
		return R.ok(sysLovService.check(list));
	}

	/**
	 * 新增lov
	 * @param dto lov
	 * @return R 通用返回体
	 */
	@ApiOperation(value = "新增lov", notes = "新增lov")
	@CreateOperationLogging(msg = "新增lov")
	@PutMapping
	@PreAuthorize("@per.hasPermission('system:lov:add')")
	public R<?> save(@RequestBody SysLovDTO dto) {
		return sysLovService.save(dto.toLov(), dto.getBodyList(), dto.getSearchList()) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "新增lov失败!");
	}

	/**
	 * 修改lov
	 * @param dto lov
	 * @return R 通用返回体
	 */
	@ApiOperation(value = "修改lov", notes = "修改lov")
	@UpdateOperationLogging(msg = "修改lov")
	@PostMapping
	@PreAuthorize("@per.hasPermission('system:lov:edit')")
	public R<?> updateById(@RequestBody SysLovDTO dto) {
		return sysLovService.update(dto.toLov(), dto.getBodyList(), dto.getSearchList()) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "修改lov失败");
	}

	/**
	 * 通过id删除lov
	 * @param id id
	 * @return R 通用返回体
	 */
	@ApiOperation(value = "通过id删除lov", notes = "通过id删除lov")
	@DeleteOperationLogging(msg = "通过id删除lov")
	@DeleteMapping("/{id}")
	@PreAuthorize("@per.hasPermission('system:lov:del')")
	public R<?> removeById(@PathVariable Integer id) {
		return sysLovService.remove(id) ? R.ok() : R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "通过id删除lov失败");
	}

}
