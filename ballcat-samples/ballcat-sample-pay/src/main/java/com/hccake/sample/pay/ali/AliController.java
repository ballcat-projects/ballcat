package com.hccake.sample.pay.ali;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.hccake.starte.pay.ali.AliPay;
import com.hccake.starte.pay.ali.domain.AliPayCallback;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @author lingting 2021/1/25 15:18
 */
@RestController
@RequestMapping("ali")
@RequiredArgsConstructor
public class AliController {

	private final AliPay aliPay;

	BigDecimal amount = new BigDecimal("100");

	BigDecimal zero = new BigDecimal("0.01");

	private static final Snowflake snowflake = IdUtil.createSnowflake(1, 1);

	/**
	 * 支付宝支付回调
	 * @param callback 回调参数
	 * @return java.lang.String
	 * @author lingting 2021-01-26 15:18
	 */
	@PostMapping
	public String notice(HttpServletRequest request, @RequestParam Map<String, String> callback) {
		System.out.println("notice");
		AliPayCallback of = AliPayCallback.of(callback);
		return "success";
	}

	@GetMapping
	public String debug() {
		// System.out.printf(sn);
		// aliPay.codePay(sn, amount, "280528061260052112", "测试");
		return "success";
	}

}
