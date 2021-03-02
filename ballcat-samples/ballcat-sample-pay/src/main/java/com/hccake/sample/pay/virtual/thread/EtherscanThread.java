package com.hccake.sample.pay.virtual.thread;

import com.hccake.ballcat.common.util.JsonUtils;
import com.hccake.sample.pay.virtual.entity.Order;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import live.lingting.virtual.currency.Transaction;
import live.lingting.virtual.currency.service.impl.InfuraServiceImpl;

/**
 * @author lingting 2021/1/5 15:22
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EtherscanThread extends AbstractThread {

	private final InfuraServiceImpl service;

	@Override
	public void init() {
		log.debug("Etherscan 订单校验");
	}

	@Override
	public Optional<Transaction> getTransaction(Order obj) {
		try {
			return service.getTransactionByHash(obj.getHash());
		}
		catch (Throwable e) {
			log.error("查询订单出错, 订单: " + JsonUtils.toJson(obj), e);
			// 查询出错, 返回 empty
			return Optional.empty();
		}
	}

}
