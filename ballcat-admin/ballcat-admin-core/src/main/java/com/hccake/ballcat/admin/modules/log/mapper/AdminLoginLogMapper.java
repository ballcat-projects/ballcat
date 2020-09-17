package com.hccake.ballcat.admin.modules.log.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hccake.ballcat.admin.modules.log.model.entity.AdminLoginLog;
import com.hccake.ballcat.admin.modules.log.model.vo.AdminLoginLogVO;
import org.apache.ibatis.annotations.Param;

/**
 * 登陆日志
 *
 * @author hccake 2020-09-16 20:21:10
 */
public interface AdminLoginLogMapper extends BaseMapper<AdminLoginLog> {

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param wrapper 查询wrapper
	 * @return IPage<LoginLogVO> VO分页数据
	 */
	IPage<AdminLoginLogVO> selectPageVo(IPage<?> page, @Param(Constants.WRAPPER) Wrapper<AdminLoginLog> wrapper);

}