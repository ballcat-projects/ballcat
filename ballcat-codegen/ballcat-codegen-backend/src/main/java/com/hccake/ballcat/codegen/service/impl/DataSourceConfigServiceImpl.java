package com.hccake.ballcat.codegen.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.hccake.ballcat.codegen.mapper.DataSourceConfigMapper;
import com.hccake.ballcat.codegen.model.converter.DataSourceConfigConverter;
import com.hccake.ballcat.codegen.model.dto.DataSourceConfigDTO;
import com.hccake.ballcat.codegen.model.entity.DataSourceConfig;
import com.hccake.ballcat.codegen.model.qo.DataSourceConfigQO;
import com.hccake.ballcat.codegen.model.vo.DataSourceConfigVO;
import com.hccake.ballcat.codegen.service.DataSourceConfigService;
import com.hccake.ballcat.common.core.domain.PageParam;
import com.hccake.ballcat.common.core.domain.PageResult;
import com.hccake.ballcat.common.core.domain.SelectData;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

/**
 * 数据源
 *
 * @author hccake
 * @date 2020 -06-17 10:24:47
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DataSourceConfigServiceImpl extends ExtendServiceImpl<DataSourceConfigMapper, DataSourceConfig>
		implements DataSourceConfigService {

	private final StringEncryptor stringEncryptor;

	private final DataSourceCreator dataSourceCreator;

	@Resource(type = DataSource.class)
	private DynamicRoutingDataSource dynamicRoutingDataSource;

	/**
	 * 根据QueryObject查询分页数据
	 * @param pageParam 分页参数
	 * @param qo 查询参数对象
	 * @return 分页数据
	 */
	@Override
	public PageResult<DataSourceConfigVO> queryPage(PageParam pageParam, DataSourceConfigQO qo) {
		return baseMapper.queryPage(pageParam, qo);
	}

	/**
	 * 获取 SelectData 集合
	 * @return List<SelectData < ?>> SelectData 集合
	 */
	@Override
	public List<SelectData<?>> listSelectData() {
		return baseMapper.listSelectData();
	}

	@Override
	public boolean save(DataSourceConfigDTO dto) {
		// 新的数据源配置信息
		DataSourceProperty dataSourceProperty = getDataSourceProperty(dto.getName(), dto.getUrl(), dto.getUsername(),
				dto.getPass());
		// 校验数据源配置
		if (isErrorDataSourceProperty(dataSourceProperty)) {
			return false;
		}

		// 转换为实体，并将密码加密
		DataSourceConfig dataSourceConfig = DataSourceConfigConverter.INSTANCE.dtoToPo(dto);
		String pass = dto.getPass();
		dataSourceConfig.setPassword(stringEncryptor.encrypt(pass));

		// 落库存储
		int flag = baseMapper.insert(dataSourceConfig);
		if (!SqlHelper.retBool(flag)) {
			return false;
		}

		// 动态添加数据源
		addDynamicDataSource(dataSourceProperty);
		return true;
	}

	/**
	 * 更新数据源配置
	 * @param dto 数据源配置信息
	 * @return boolean
	 */
	@Override
	public boolean update(DataSourceConfigDTO dto) {
		// 获取现有数据源配置
		DataSourceConfig oldConfig = baseMapper.selectById(dto.getId());
		Assert.notNull(oldConfig, "Update data source config that does not exist");

		// 转换为实体
		DataSourceConfig dataSourceConfig = DataSourceConfigConverter.INSTANCE.dtoToPo(dto);

		// 若没有修改密码，则使用现有的密码，否则加密存储
		String pass = dto.getPass();
		if (StrUtil.isBlank(pass)) {
			pass = stringEncryptor.decrypt(oldConfig.getPassword());
		}
		else {
			dataSourceConfig.setPassword(stringEncryptor.encrypt(pass));
		}

		// 新的数据源配置信息
		DataSourceProperty dataSourceProperty = getDataSourceProperty(dto.getName(), dto.getUrl(), dto.getUsername(),
				pass);
		// 校验数据源配置
		if (isErrorDataSourceProperty(dataSourceProperty)) {
			return false;
		}

		// 落库存储
		int flag = baseMapper.updateById(dataSourceConfig);
		if (!SqlHelper.retBool(flag)) {
			return false;
		}

		// 先删除现有数据源
		dynamicRoutingDataSource.removeDataSource(oldConfig.getName());
		// 再添加数据源
		addDynamicDataSource(dataSourceProperty);

		return true;
	}

	@Override
	public boolean removeById(Serializable id) {
		// 获取现有数据源配置
		DataSourceConfig oldConfig = baseMapper.selectById(id);
		Assert.notNull(oldConfig, "Delete data source config that does not exist");

		if (SqlHelper.retBool(baseMapper.deleteById(id))) {
			// 删除现有数据源
			dynamicRoutingDataSource.removeDataSource(oldConfig.getName());
			return true;
		}
		return false;
	}

	/**
	 * 添加动态数据源
	 * @param dataSourceProperty 数据源配置
	 */
	private void addDynamicDataSource(DataSourceProperty dataSourceProperty) {
		DataSource dataSource = dataSourceCreator.createDataSource(dataSourceProperty);
		dynamicRoutingDataSource.addDataSource(dataSourceProperty.getPoolName(), dataSource);
	}

	/**
	 * 获得数据源配置实体
	 * @param dsName 数据源名称
	 * @param url 数据库连接
	 * @param username 数据库用户名
	 * @param password 数据库密码
	 * @return 数据源配置
	 */
	private DataSourceProperty getDataSourceProperty(String dsName, String url, String username, String password) {
		DataSourceProperty dataSourceProperty = new DataSourceProperty();
		dataSourceProperty.setPoolName(dsName);
		dataSourceProperty.setUrl(url);
		dataSourceProperty.setUsername(username);
		dataSourceProperty.setPassword(password);
		return dataSourceProperty;
	}

	/**
	 * 校验数据源是配置否可用
	 * @param dataSourceProperty 数据源配置信息
	 * @return boolean
	 */
	private boolean isErrorDataSourceProperty(DataSourceProperty dataSourceProperty) {
		try (Connection ignored = DriverManager.getConnection(dataSourceProperty.getUrl(),
				dataSourceProperty.getUsername(), dataSourceProperty.getPassword())) {
			if (log.isDebugEnabled()) {
				log.debug("check connection success, dataSourceProperty: {}", dataSourceProperty);
			}
		}
		catch (Exception e) {
			log.error("get connection error, dataSourceProperty: {}", dataSourceProperty, e);
			return true;
		}
		return false;
	}

}
