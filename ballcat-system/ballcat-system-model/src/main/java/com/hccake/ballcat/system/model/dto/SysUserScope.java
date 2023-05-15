package com.hccake.ballcat.system.model.dto;

import lombok.Data;

import java.util.List;

/**
 *
 * 用户权限信息，基础只有roleIds 后续业务相关的授权 按需扩展
 *
 * @author Hccake 2019/9/24 10:13
 */
@Data
public class SysUserScope {

	private List<String> roleCodes;

}
