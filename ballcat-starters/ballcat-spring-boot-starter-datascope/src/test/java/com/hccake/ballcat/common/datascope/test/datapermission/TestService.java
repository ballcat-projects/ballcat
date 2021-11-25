package com.hccake.ballcat.common.datascope.test.datapermission;

import com.hccake.ballcat.common.datascope.annotation.DataPermission;

/**
 * @author hccake
 */
public interface TestService {

	DataPermission methodA();

	DataPermission methodB();

}
