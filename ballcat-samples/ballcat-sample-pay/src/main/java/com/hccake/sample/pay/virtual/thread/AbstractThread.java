package com.hccake.sample.pay.virtual.thread;

import com.hccake.ballcat.common.core.util.JacksonUtils;
import com.hccake.sample.pay.virtual.domain.Result;
import com.hccake.sample.pay.virtual.entity.Order;
import com.hccake.starter.pay.viratual.AbstractVerifyThread;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import live.lingting.virtual.currency.Transaction;
import live.lingting.virtual.currency.enums.TransactionStatus;

/**
 * 配置基本校验
 *
 * @author lingting 2021/1/5 14:25
 */
@Slf4j
public abstract class AbstractThread extends AbstractVerifyThread<Order, Result> {

	public final List<Order> CACHE = new ArrayList<>();

	@Override
	public long getBatchSize() {
		return 1;
	}

	@Override
	public void errorLog(Throwable e, List<Order> list) {
		// 读取缓存 和 接收数据时出现异常执行此方法
		log.error("读取缓存 和 接收数据时出现异常执行此方法, 数据: " + JacksonUtils.toJson(list), e);
	}

	@Override
	public void handler(Order obj, Optional<Transaction> optional) {
		/*
		 * 不管哪个平台的充值订单, 验证逻辑都是一样的
		 *
		 * 如果对某平台有特殊要求, 可以在子类中实现本方法,
		 */

		// 没有获取到交易数据
		if (!optional.isPresent()) {
			// 订单已创建超过6小时
			if (Duration.between(obj.getCreateTime(), LocalDateTime.now()).toHours() > 6) {
				failed(obj, optional, new Result("超时"));
			}
			// 没有超过限时, 缓存
			else {
				cache(obj);
			}
			return;
		}

		Transaction transaction = optional.get();

		if (transaction.getStatus() == TransactionStatus.WAIT) {
			// 交易需要等待继续查询
			cache(obj);
		}
		// 交易失败
		else if (transaction.getStatus() == TransactionStatus.FAIL) {
			failed(obj, optional, new Result("交易失败"));
		}
		// 交易生成时间超过订单创建时间 12小时范围, 拒绝
		else if (Duration.between(transaction.getTime(), obj.getCreateTime()).toHours() > 12) {
			failed(obj, optional, new Result("交易生成时间超过订单创建时间 12小时范围, 拒绝"));
		}
		// 收款地址验证
		else if (!transaction.getTo().equals(obj.getAddress())) {
			failed(obj, optional, new Result("收款地址异常"));
		}
		// 收款货币类型验证
		else if (obj.getContract() != transaction.getContract()) {
			failed(obj, optional, new Result("收款货币类型异常"));
		}

		/*
		 * .... 其他业务判断逻辑
		 */

		// 交易成功
		else {
			success(obj, optional, new Result("成功"));
		}
	}

	@Override
	public void error(Order obj, Throwable e) {
		// 校验订单 和 获取交易数据时出现异常执行此方法
		log.error("校验订单 和 获取交易数据时出现异常执行此方法, 出错订单: " + JacksonUtils.toJson(obj), e);

		// 可根据业务需求在此进行其他处理.

		/*
		 * 例如 放入缓存等待下次处理.
		 */
		cache(obj);
		/*
		 * 例如 直接按照充值失败进行结算
		 */
		failed(obj, Optional.empty(), new Result("出现异常, 具体请查询日志! 简易信息: " + e.getMessage()));
	}

	@Override
	public void success(Order obj, Optional<Transaction> optional, Result verifyResult) {
		log.info("交易成功, 订单数据: {}, 交易信息: {}, 结果: {}", JacksonUtils.toJson(obj),
				!optional.isPresent() ? "null" : JacksonUtils.toJson(optional.get()),
				JacksonUtils.toJson(verifyResult));
	}

	@Override
	public void failed(Order obj, Optional<Transaction> optional, Result verifyResult) {
		log.info("交易失败, 订单数据: {}, 交易信息: {}, 结果: {}", JacksonUtils.toJson(obj),
				!optional.isPresent() ? "null" : JacksonUtils.toJson(optional.get()),
				JacksonUtils.toJson(verifyResult));
	}

	@Override
	public void cache(Order obj) {
		CACHE.add(obj);
	}

	@Override
	public List<Order> readCache() {
		/*
		 * 这里使用 list 缓存是因为只是样例, 生产环境不建议
		 */
		List<Order> list = new ArrayList<>(CACHE);
		CACHE.clear();
		return list;
	}

}
