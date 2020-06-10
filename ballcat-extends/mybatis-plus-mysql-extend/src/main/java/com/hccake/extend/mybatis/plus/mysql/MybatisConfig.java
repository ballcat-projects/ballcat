package com.hccake.extend.mybatis.plus.mysql;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.hccake.extend.mybatis.plus.config.MybatisConfigurer;
import com.hccake.extend.mybatis.plus.mysql.methods.InsertIgnoreByBatch;
import com.hccake.extend.mybatis.plus.mysql.methods.InsertOrUpdateByBatch;
import com.hccake.extend.mybatis.plus.mysql.methods.InsertOrUpdateFieldByBatch;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Set;

/**
 * @author lingting  2020/5/27 11:43
 */
@Configuration
public class MybatisConfig implements MybatisConfigurer {

	@Override
	public void addIgnoreFields(Set<String> set) {
		set.add("createTime");
	}

	@Override
	public void addGlobalMethods(List<AbstractMethod> list) {
		list.add(new InsertIgnoreByBatch());
		list.add(new InsertOrUpdateByBatch());
		list.add(new InsertOrUpdateFieldByBatch());
	}
}
