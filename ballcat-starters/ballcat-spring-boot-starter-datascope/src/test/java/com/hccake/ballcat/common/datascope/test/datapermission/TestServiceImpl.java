package com.hccake.ballcat.common.datascope.test.datapermission;

import com.hccake.ballcat.common.datascope.annotation.DataPermission;
import com.hccake.ballcat.common.datascope.holder.DataPermissionAnnotationHolder;
import org.springframework.stereotype.Component;

/**
 * @author hccake
 */
@Component
@DataPermission(excludeResources = { "class" })
public class TestServiceImpl implements TestService {

	@Override
	@DataPermission(excludeResources = { "order" })
	public DataPermission methodA() {
		return DataPermissionAnnotationHolder.peek();
	}

	@Override
	public DataPermission methodB() {
		return DataPermissionAnnotationHolder.peek();
	}

}
