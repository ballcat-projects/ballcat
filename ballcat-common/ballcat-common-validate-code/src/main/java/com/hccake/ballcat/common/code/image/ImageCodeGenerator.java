package com.hccake.ballcat.common.code.image;

import cn.hutool.captcha.CircleCaptcha;
import com.hccake.ballcat.common.code.ValidateCodeGenerator;
import com.hccake.ballcat.common.code.ValidateCodeProperties;
import lombok.Setter;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * ImageCodeGenerator
 *
 * @author xm.z
 */
public class ImageCodeGenerator implements ValidateCodeGenerator {

	@Setter
	private ValidateCodeProperties.ImageCodeProperties imageCodeProperties;

	@Override
	public ImageCode generate(ServletWebRequest request) {
		int width = ServletRequestUtils.getIntParameter(request.getRequest(), "width", imageCodeProperties.getWidth());
		int height = ServletRequestUtils.getIntParameter(request.getRequest(), "height",
				imageCodeProperties.getWidth());
		CircleCaptcha captcha = new CircleCaptcha(width, height, imageCodeProperties.getLength());
		return new ImageCode(captcha.getImage(), captcha.getCode(), imageCodeProperties.getExpireIn());
	}

}