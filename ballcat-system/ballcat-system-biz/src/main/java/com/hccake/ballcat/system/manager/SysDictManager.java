package com.hccake.ballcat.system.manager;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import com.hccake.ballcat.common.core.exception.BusinessException;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.common.model.result.BaseResultCode;
import com.hccake.ballcat.system.converter.SysDictItemConverter;
import com.hccake.ballcat.system.event.DictChangeEvent;
import com.hccake.ballcat.system.model.entity.SysDict;
import com.hccake.ballcat.system.model.entity.SysDictItem;
import com.hccake.ballcat.system.model.qo.SysDictQO;
import com.hccake.ballcat.system.model.vo.DictDataVO;
import com.hccake.ballcat.system.model.vo.DictItemVO;
import com.hccake.ballcat.system.model.vo.SysDictItemPageVO;
import com.hccake.ballcat.system.model.vo.SysDictPageVO;
import com.hccake.ballcat.system.service.SysDictItemService;
import com.hccake.ballcat.system.service.SysDictService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

	private final ApplicationEventPublisher eventPublisher;

	/**
	 * 字典表分页
	 * @param pageParam 分页参数
	 * @param sysDictQO 查询参数
	 * @return 字典表分页数据
	 */
	public PageResult<SysDictPageVO> dictPage(PageParam pageParam, SysDictQO sysDictQO) {
		return sysDictService.queryPage(pageParam, sysDictQO);
	}

	/**
	 * 保存字典
	 * @param sysDict 字典对象
	 * @return 执行是否成功
	 */
	public boolean dictSave(SysDict sysDict) {
		sysDict.setHashCode(IdUtil.fastSimpleUUID());
		return sysDictService.save(sysDict);
	}

	/**
	 * 更新字典
	 * @param sysDict 字典对象
	 * @return 执行是否成功
	 */
	public boolean updateDictById(SysDict sysDict) {
		// 查询现有数据
		SysDict dict = sysDictService.getById(sysDict.getId());
		sysDict.setHashCode(IdUtil.fastSimpleUUID());
		boolean result = sysDictService.updateById(sysDict);
		if (result) {
			eventPublisher.publishEvent(new DictChangeEvent(dict.getCode()));
		}
		return result;
	}

	/**
	 * 删除字典
	 * @param id 字典id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void removeDictById(Integer id) {
		// 查询现有数据
		SysDict dict = sysDictService.getById(id);
		// 字典标识
		String dictCode = dict.getCode();

		// 有关联字典项则不允许删除
		Assert.isFalse(sysDictItemService.exist(dictCode),
				() -> new BusinessException(BaseResultCode.LOGIC_CHECK_ERROR.getCode(), "该字典下存在字典项，不允许删除！"));

		// 删除字典
		Assert.isTrue(sysDictService.removeById(id),
				() -> new BusinessException(BaseResultCode.UPDATE_DATABASE_ERROR.getCode(), "字典删除异常"));
	}

	/**
	 * 更新字典项状态
	 * @param itemId 字典项id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateDictItemStatusById(Integer itemId, Integer status) {
		// 获取字典项
		SysDictItem dictItem = sysDictItemService.getById(itemId);
		Assert.notNull(dictItem,
				() -> new BusinessException(BaseResultCode.LOGIC_CHECK_ERROR.getCode(), "错误的字典项 id：" + itemId));

		// 更新字典项状态
		SysDictItem entity = new SysDictItem();
		entity.setId(itemId);
		entity.setStatus(status);
		Assert.isTrue(sysDictItemService.updateById(entity),
				() -> new BusinessException(BaseResultCode.UPDATE_DATABASE_ERROR.getCode(), "字典项状态更新异常"));

		// 更新字典 hash
		String dictCode = dictItem.getDictCode();
		Assert.isTrue(sysDictService.updateHashCode(dictCode),
				() -> new BusinessException(BaseResultCode.UPDATE_DATABASE_ERROR.getCode(), "字典 Hash 更新异常"));

		// 发布字典更新事件
		eventPublisher.publishEvent(new DictChangeEvent(dictCode));
	}

	/**
	 * 字典项分页
	 * @param pageParam 分页属性
	 * @param dictCode 字典标识
	 * @return 字典项分页数据
	 */
	public PageResult<SysDictItemPageVO> dictItemPage(PageParam pageParam, String dictCode) {
		return sysDictItemService.queryPage(pageParam, dictCode);
	}

	/**
	 * 新增字典项
	 * @param sysDictItem 字典项
	 * @return 执行是否成功
	 */
	@Transactional(rollbackFor = Exception.class)
	public boolean saveDictItem(SysDictItem sysDictItem) {
		// 更新字典项Hash值
		String dictCode = sysDictItem.getDictCode();
		if (!sysDictService.updateHashCode(dictCode)) {
			return false;
		}

		boolean result = sysDictItemService.save(sysDictItem);
		if (result) {
			eventPublisher.publishEvent(new DictChangeEvent(dictCode));
		}
		return result;
	}

	/**
	 * 更新字典项
	 * @param sysDictItem 字典项
	 * @return 执行是否成功
	 */
	@Transactional(rollbackFor = Exception.class)
	public boolean updateDictItemById(SysDictItem sysDictItem) {
		// 根据ID查询字典
		String dictCode = sysDictItem.getDictCode();
		// 更新字典项Hash值
		if (!sysDictService.updateHashCode(dictCode)) {
			return false;
		}
		boolean result = sysDictItemService.updateById(sysDictItem);
		if (result) {
			eventPublisher.publishEvent(new DictChangeEvent(dictCode));
		}
		return result;
	}

	/**
	 * 删除字典项
	 * @param id 字典项
	 * @return 执行是否成功
	 */
	@Transactional(rollbackFor = Exception.class)
	public boolean removeDictItemById(Integer id) {
		// 根据ID查询字典
		SysDictItem dictItem = sysDictItemService.getById(id);
		String dictCode = dictItem.getDictCode();
		// 更新字典项Hash值
		if (!sysDictService.updateHashCode(dictCode)) {
			return false;
		}
		boolean result = sysDictItemService.removeById(id);
		if (result) {
			eventPublisher.publishEvent(new DictChangeEvent(dictCode));
		}
		return true;
	}

	/**
	 * 查询字典数据
	 * @param dictCodes 字典标识
	 * @return DictDataAndHashVO
	 */
	public List<DictDataVO> queryDictDataAndHashVO(String[] dictCodes) {
		List<DictDataVO> list = new ArrayList<>();
		// 查询对应hash值，以及字典项数据
		List<SysDict> sysDictList = sysDictService.listByCodes(dictCodes);
		if (CollectionUtil.isNotEmpty(sysDictList)) {
			for (SysDict sysDict : sysDictList) {
				List<SysDictItem> dictItems = sysDictItemService.listByDictCode(sysDict.getCode());
				// 排序并转换为VO
				List<DictItemVO> setDictItems = dictItems.stream().sorted(Comparator.comparingInt(SysDictItem::getSort))
						.map(SysDictItemConverter.INSTANCE::poToItemVo).collect(Collectors.toList());
				// 组装DataVO
				DictDataVO dictDataVO = new DictDataVO();
				dictDataVO.setValueType(sysDict.getValueType());
				dictDataVO.setDictCode(sysDict.getCode());
				dictDataVO.setHashCode(sysDict.getHashCode());
				dictDataVO.setDictItems(setDictItems);

				list.add(dictDataVO);
			}
		}
		return list;
	}

	/**
	 * 返回失效的Hash
	 * @param dictHashCode 校验的hashCodeMap
	 * @return List<String> 失效的字典标识集合
	 */
	public List<String> invalidDictHash(Map<String, String> dictHashCode) {
		List<SysDict> byCode = sysDictService.listByCodes(dictHashCode.keySet().toArray(new String[] {}));
		// 过滤相等Hash值的字典项，并返回需要修改的字典项的Code
		return byCode.stream().filter(x -> !dictHashCode.get(x.getCode()).equals(x.getHashCode())).map(SysDict::getCode)
				.collect(Collectors.toList());
	}

}
