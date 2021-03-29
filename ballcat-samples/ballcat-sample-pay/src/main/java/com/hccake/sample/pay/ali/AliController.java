package com.hccake.sample.pay.ali;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.hccake.extend.pay.ali.AliPay;
import com.hccake.extend.pay.ali.domain.AliPayCallback;
import java.math.BigDecimal;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lingting 2021/1/25 15:18
 */
@RestController
@RequestMapping("ali")
@RequiredArgsConstructor
public class AliController {

	private final AliPay aliPay;

	BigDecimal zero = new BigDecimal("0.01");

	private static final Snowflake snowflake = IdUtil.createSnowflake(1, 1);

	/**
	 * 支付宝支付回调
	 * @param callback 回调参数
	 * @return java.lang.String
	 * @author lingting 2021-01-26 15:18
	 */
	@SneakyThrows
	@PostMapping
	public String notice(HttpServletRequest request, @RequestParam Map<String, String> callback) {
		System.out.println("notice");
		AliPayCallback of = AliPayCallback.of(callback);
		System.out.println(of.checkSign(aliPay));
		aliPay.checkSignV1(of.getRaw());
		return "success";
	}

	@GetMapping
	public String debug() {
		// System.out.printf(sn);
		// aliPay.codePay(sn, amount, "280528061260052112", "测试");
		return "success";
	}

}
