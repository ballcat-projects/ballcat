package com.hccake.ballcat.common.datascope.handler;

import com.hccake.ballcat.common.datascope.DataScope;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hccake 2020/9/28
 * @version 1.0
 */
@Getter
@Setter
public class DataPermissionHandler {

	List<DataScope> dataScopes = new ArrayList<>();

}
