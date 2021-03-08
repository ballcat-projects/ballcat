package com.hccake.ballcat.common.datascope.handler;

import com.hccake.ballcat.common.datascope.DataScope;
import com.hccake.ballcat.common.datascope.annotation.DataPermission;
import com.hccake.ballcat.common.datascope.util.AnnotationUtil;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author Hccake 2021/1/27
 * @version 1.0
 */
@DataPermission
@RequiredArgsConstructor
public abstract class AbstractDataPermissionHandler implements DataPermissionHandler {

	private final List<DataScope> dataScopes;

	private static final Map<String, DataPermission> DATA_PERMISSION_CACHE = new ConcurrentHashMap<>();

	/**
	 * 提供一个默认的空值注解，用于缓存空值占位使用
	 */
	private static final DataPermission EMPTY_DATA_PERMISSION = AbstractDataPermissionHandler.class
			.getAnnotation(DataPermission.class);

	/**
	 * 系统配置的所有的数据范围
	 * @return 数据范围集合
	 */
	@Override
	public List<DataScope> dataScopes() {
		return dataScopes;
	}

	/**
	 * 系统配置的所有的数据范围
	 * @param mappedStatementId Mapper方法ID
	 * @return 数据范围集合
	 */
	@Override
	public List<DataScope> filterDataScopes(String mappedStatementId) {
		if (this.dataScopes == null || this.dataScopes.isEmpty()) {
			return new ArrayList<>();
		}
		// 获取当前方法对应的权限注解，根据注解进行数据范围控制的过滤
		DataPermission dataPermission = getDataPermissionCache(mappedStatementId);
		if (dataPermission == null) {
			return dataScopes;
		}

		if (dataPermission.ignore()) {
			return new ArrayList<>();
		}

		// 当指定了只包含的资源时，只对该资源的DataScope
		if (dataPermission.includeResources().length > 0) {
			Set<String> a = new HashSet<>(Arrays.asList(dataPermission.includeResources()));
			return dataScopes.stream().filter(x -> a.contains(x.getResource())).collect(Collectors.toList());
		}

		// 当未指定只包含的资源，且指定了排除的资源时，则排除此部分资源的 DataScope
		if (dataPermission.excludeResources().length > 0) {
			Set<String> a = new HashSet<>(Arrays.asList(dataPermission.excludeResources()));
			return dataScopes.stream().filter(x -> !a.contains(x.getResource())).collect(Collectors.toList());
		}

		return dataScopes;
	}

	/**
	 * 从缓存中获取数据权限注解 优先获取方法上的注解，再获取类上的注解
	 * @param mappedStatementId 类名.方法名
	 * @return 当前方法有效的数据权限注解
	 */
	private DataPermission getDataPermissionCache(String mappedStatementId) {
		DataPermission dataPermission;
		if (DATA_PERMISSION_CACHE.containsKey(mappedStatementId)) {
			dataPermission = DATA_PERMISSION_CACHE.get(mappedStatementId);
			return EMPTY_DATA_PERMISSION.equals(dataPermission) ? null : dataPermission;
		}
		else {
			dataPermission = AnnotationUtil.findAnnotationByMappedStatementId(mappedStatementId, DataPermission.class);
			DATA_PERMISSION_CACHE.put(mappedStatementId,
					dataPermission == null ? EMPTY_DATA_PERMISSION : dataPermission);
			return dataPermission;
		}
	}

}
