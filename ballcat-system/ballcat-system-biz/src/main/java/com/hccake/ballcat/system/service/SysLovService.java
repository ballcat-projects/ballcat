package com.hccake.ballcat.system.service;

import com.hccake.ballcat.system.model.entity.SysLov;
import com.hccake.ballcat.system.model.entity.SysLovBody;
import com.hccake.ballcat.system.model.entity.SysLovSearch;
import com.hccake.ballcat.system.model.qo.SysLovQO;
import com.hccake.ballcat.system.model.vo.SysLovPageVO;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.ExtendService;
import java.util.List;

/**
 * @author lingting 2020-08-10 17:20
 */
public interface SysLovService extends ExtendService<SysLov> {

	/**
	 * 根据QueryObject查询分页数据
	 * @param pageParam 分页参数
	 * @param qo 查询参数对象
	 * @return PageResult<LoginLogVO> 分页数据
	 */
	PageResult<SysLovPageVO> queryPage(PageParam pageParam, SysLovQO qo);

	/**
	 * 更新实体类
	 * @param entity 实体类
	 * @param bodyList body
	 * @param searchList search
	 * @return boolean
	 * @author lingting 2020-07-21 10:47:24
	 */
	boolean update(SysLov entity, List<SysLovBody> bodyList, List<SysLovSearch> searchList);

	/**
	 * 移除实体类
	 * @param id id
	 * @return boolean
	 * @author lingting 2020-07-21 10:49:35
	 */
	boolean remove(Integer id);

	/**
	 * 更新实体类
	 * @param sysLov 实体类
	 * @param bodyList body内容
	 * @param searchList search 内容
	 * @return boolean
	 * @author lingting 2020-07-21 10:47:24
	 */
	boolean save(SysLov sysLov, List<SysLovBody> bodyList, List<SysLovSearch> searchList);

	/**
	 * 根据keyword获取lov数据
	 * @param keyword keyword
	 * @return Lov
	 */
	SysLov getByKeyword(String keyword);

	/**
	 * 检查指定的lov是否已过期
	 * @param list 数据集
	 * @return java.util.List<java.lang.String>
	 * @author lingting 2021-03-26 10:02
	 */
	List<String> check(List<SysLov> list);

}
