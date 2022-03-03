package com.hccake.ballcat.common.datascope.test.datapermission;

import com.hccake.ballcat.common.datascope.annotation.DataPermission;
import com.hccake.ballcat.common.datascope.handler.DataPermissionRule;
import com.hccake.ballcat.common.datascope.holder.DataPermissionRuleHolder;
import org.springframework.stereotype.Component;

/**
 * @author hccake
 */
@Component
@DataPermission(excludeResources = { "class" })
public class TestServiceImpl implements TestService {

	@Override
	@DataPermission(excludeResources = { "order" })
	public DataPermissionRule methodA() {
		return DataPermissionRuleHolder.peek();
	}

	@Override
	public DataPermissionRule methodB() {
		return DataPermissionRuleHolder.peek();
	}

	@Override
	public DataPermissionRule methodC() {
		return DataPermissionRuleHolder.peek();
	}

}
