package com.hccake.ballcat.common.idempotent.test;

import com.hccake.ballcat.common.idempotent.annotation.Idempotent;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hccake
 */
@RestController
public class IdempotentController {

	@RequestMapping("/")
	@Idempotent(uniqueExpression = "#request.getHeader('formId')", duration = 1)
	public String greeting() {
		return "hello word";
	}

}
