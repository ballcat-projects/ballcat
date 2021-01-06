package com.hccake.sample.pay.virtual.controller;

import com.hccake.sample.pay.virtual.entity.Order;
import com.hccake.sample.pay.virtual.enums.Status;
import com.hccake.sample.pay.virtual.thread.EtherscanThread;
import com.hccake.sample.pay.virtual.thread.OmniThread;
import com.hccake.sample.pay.virtual.thread.TronscanThread;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import live.lingting.virtual.currency.contract.EtherscanContract;
import live.lingting.virtual.currency.contract.OmniContract;
import live.lingting.virtual.currency.contract.TronscanContract;

/**
 * @author lingting 2021/1/5 16:17
 */
@RestController
@RequiredArgsConstructor
public class VirtualController {

	private final EtherscanThread etherscanThread;

	private final TronscanThread tronscanThread;

	private final OmniThread omniThread;

	@GetMapping("eth")
	public Object eth() {
		etherscanThread.putObject(new Order()
				// infura 平台由于需要 project id, 可以自己填上自己的projectId 进行验证
				.setHash("0x2ab87cc91f48fa940aab53e602a57786ad3d4a263875b6ffb779713bf5a60645")
				.setCreateTime(LocalDateTime.now()).setContract(EtherscanContract.USDT));
		return "";
	}

	@GetMapping("trc")
	public Object trc() {
		tronscanThread.putObject(new Order().setHash("b22dce34a2c60661989ee710cf71d80d3ff50c1613e7fde6f9e34146ef7bdd2e")
				.setContract(TronscanContract.USDT).setSn(2312314L).setCreateTime(LocalDateTime.now())
				.setStatus(Status.WAIT).setAddress("TFm55T9n2Qs3gfehoXt4YFBJzRJKrHWH3V"));
		return "";
	}

	@GetMapping("btc")
	public Object btc() {
		omniThread.putObject(new Order().setHash("f583049c257da84d17874aef32425c8d192c12f9718db152fc72370b9d6bd01f")
				.setContract(OmniContract.USDT).setSn(2312314L).setCreateTime(LocalDateTime.now())
				.setStatus(Status.WAIT).setAddress("34Bs4AJigJUcbXXJk5kVznRNDiAGj57qCm"));
		return "";
	}

}
