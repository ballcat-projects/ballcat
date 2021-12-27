package com.hccake.ballcat.admin;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.hccake.ballcat.admin.config.prop.DbProperties;
import com.hccake.ballcat.auth.annotation.EnableOauth2AuthorizationServer;
import com.hccake.ballcat.common.security.annotation.EnableOauth2ResourceServer;
import com.hccake.ballcat.common.security.properties.SecurityProperties;
import com.hccake.ballcat.system.authentication.CustomTokenEnhancer;
import com.hccake.ballcat.system.authentication.DefaultUserInfoCoordinatorImpl;
import com.hccake.ballcat.system.authentication.SysUserDetailsServiceImpl;
import com.hccake.ballcat.system.authentication.UserInfoCoordinator;
import com.hccake.ballcat.system.properties.UpmsProperties;
import com.hccake.ballcat.system.service.SysUserService;
import com.hccake.extend.mybatis.plus.methods.InsertBatchSomeColumnByCollection;
import lombok.AllArgsConstructor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/5/25 21:01
 */
@EnableAsync
@MapperScan("com.hccake.ballcat.**.mapper")
@ComponentScan({ "com.hccake.ballcat.admin", "com.hccake.ballcat.auth", "com.hccake.ballcat.system",
		"com.hccake.ballcat.log", "com.hccake.ballcat.file", "com.hccake.ballcat.notify" })
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({ UpmsProperties.class, SecurityProperties.class, DbProperties.class })
@EnableOauth2AuthorizationServer
@EnableOauth2ResourceServer
public class UpmsAutoConfiguration {

	/**
	 * token 增强，追加一些自定义信息
	 * @return TokenEnhancer Token增强处理器
	 */
	@Bean
	@ConditionalOnMissingBean
	public TokenEnhancer tokenEnhancer() {
		return new CustomTokenEnhancer();
	}

	/**
	 * 用户详情处理类
	 *
	 * @author hccake
	 */
	@Configuration(proxyBeanMethods = false)
	@ConditionalOnClass(SysUserService.class)
	@ConditionalOnMissingBean(UserDetailsService.class)
	static class UserDetailsServiceConfiguration {

		/**
		 * 用户详情处理类
		 * @return SysUserDetailsServiceImpl
		 */
		@Bean
		@ConditionalOnMissingBean
		public UserDetailsService userDetailsService(SysUserService sysUserService,
				UserInfoCoordinator userInfoCoordinator) {
			return new SysUserDetailsServiceImpl(sysUserService, userInfoCoordinator);
		}

		/**
		 * 用户信息协调者
		 * @return UserInfoCoordinator
		 */
		@Bean
		@ConditionalOnMissingBean
		public UserInfoCoordinator userInfoCoordinator() {
			return new DefaultUserInfoCoordinatorImpl();
		}

	}

	@Configuration
	@AllArgsConstructor
	static class MybatisPlusBeanConfiguration {

		private final DbProperties dbProperties;

		/**
		 * 分页插件
		 * @return InnerInterceptor
		 */
		@Bean
		@Order(Ordered.HIGHEST_PRECEDENCE + 100)
		@ConditionalOnMissingBean(PaginationInnerInterceptor.class)
		public InnerInterceptor paginationInnerInterceptor() {
			PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
			// 单页分页条数限制
			paginationInterceptor.setMaxLimit(dbProperties.getMaxLimit());
			// 数据库类型
			paginationInterceptor.setDbType(DbType.MYSQL);
			// 生成 countSql 优化掉 join 现在只支持 left join
			paginationInterceptor.setOptimizeJoin(dbProperties.isOptimizeJoin());
			return paginationInterceptor;
		}

		/**
		 * 自定义批量插入方法注入
		 * @return AbstractMethod
		 */
		@Bean
		public AbstractMethod insertBatchSomeColumnByCollection() {
			// 对于只在更新时进行填充的字段不做插入处理
			return new InsertBatchSomeColumnByCollection(t -> t.getFieldFill() != FieldFill.UPDATE);
		}

	}

}
