package com.hccake.sample.pay.ali;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.hccake.starte.pay.ali.AliPay;
import java.math.BigDecimal;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lingting 2021/1/25 15:18
 */
@RestController
@RequestMapping("ali")
@RequiredArgsConstructor
public class Controller {

	private final AliPay aliPay;

	private static final Snowflake snowflake = IdUtil.createSnowflake(1, 1);

	@PostMapping
	public String notice(Map<String, Object> params) {

		return "success";
	}

	@GetMapping
	public String debug() {
		String sn = snowflake.nextIdStr();
		BigDecimal amount = new BigDecimal("100");
		// System.out.printf(sn);
		// aliPay.codePay(sn, amount, "280528061260052112", "测试");
		return "success";
	}

}
