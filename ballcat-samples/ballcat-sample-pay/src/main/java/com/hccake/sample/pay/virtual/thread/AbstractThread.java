package com.hccake.sample.pay.virtual.thread;

import com.hccake.ballcat.common.util.JsonUtils;
import com.hccake.sample.pay.virtual.domain.Result;
import com.hccake.sample.pay.virtual.entity.Order;
import com.hccake.sample.pay.virtual.enums.Contract;
import com.hccake.extend.pay.viratual.AbstractVerifyThread;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import live.lingting.virtual.currency.core.model.TransactionInfo;
import live.lingting.virtual.currency.core.enums.TransactionStatus;

/**
 * 配置基本校验
 *
 * @author lingting 2021/1/5 14:25
 */
@Slf4j
public abstract class AbstractThread extends AbstractVerifyThread<Order, Result> {

	@Override
	public int getBatchSize() {
		return 10;
	}

	/**
	 * 如果默认程序无法正确解析你的数据. 可自定义类型. 或者重写 {@link this#convertToObj(String)} 方法 自定义转换
	 *
	 * 可重写 {@link this#convertToString(Object)} 方法. 自定义缓存数据
	 * @author lingting 2021-03-03 10:29
	 */
	@Override
	protected Type getObjType() {
		return super.getObjType();
	}

	@Override
	public void error(Throwable e, List<Order> list) {
		// 读取缓存 和 接收数据时出现异常执行此方法
		log.error("读取缓存 和 接收数据时出现异常执行此方法, 数据: " + JsonUtils.toJson(list), e);
	}

	@Override
	public void handler(Order obj, Optional<TransactionInfo> optional) {
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
				put(obj);
			}
			return;
		}

		TransactionInfo transaction = optional.get();

		if (transaction.getStatus() == TransactionStatus.WAIT) {
			// 交易需要等待继续查询
			put(obj);
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
		else if (obj.getContract() != Contract.USDT) {
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
		log.error("校验订单 和 获取交易数据时出现异常执行此方法, 出错订单: " + JsonUtils.toJson(obj), e);

		// 可根据业务需求在此进行其他处理.

		/*
		 * 例如 放入缓存等待下次处理.
		 */
		put(obj);
		/*
		 * 例如 直接按照充值失败进行结算
		 */
		failed(obj, Optional.empty(), new Result("出现异常, 具体请查询日志! 简易信息: " + e.getMessage()));
	}

	@Override
	public void success(Order obj, Optional<TransactionInfo> optional, Result verifyResult) {
		log.info("交易成功, 订单数据: {}, 交易信息: {}, 结果: {}", JsonUtils.toJson(obj),
				!optional.isPresent() ? "null" : JsonUtils.toJson(optional.get()), JsonUtils.toJson(verifyResult));
	}

	@Override
	public void failed(Order obj, Optional<TransactionInfo> optional, Result verifyResult) {
		log.info("交易失败, 订单数据: {}, 交易信息: {}, 结果: {}", JsonUtils.toJson(obj),
				!optional.isPresent() ? "null" : JsonUtils.toJson(optional.get()), JsonUtils.toJson(verifyResult));
	}

}
