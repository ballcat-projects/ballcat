package com.hccake.ballcat.common.code;

import com.hccake.ballcat.common.model.result.R;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ValidateCodeController
 *
 * @author xm.z
 */
@RestController
@RequiredArgsConstructor
public class ValidateCodeController {

	private final ValidateCodeProcessorHolder validateCodeProcessorHolder;

	/**
	 * Create verification code, call different {@link ValidateCodeProcessor} interface
	 * implementations according to different verification code types
	 * @param request the request
	 * @param response the response
	 * @param type the verification code type
	 * @return {@link R}
	 */
	@SneakyThrows
	@GetMapping("/code/{type}")
	public R<Object> createCode(HttpServletRequest request, HttpServletResponse response, @PathVariable String type) {
		validateCodeProcessorHolder.findValidateCodeProcessor(type).create(new ServletWebRequest(request, response));
		return R.ok();
	}

}