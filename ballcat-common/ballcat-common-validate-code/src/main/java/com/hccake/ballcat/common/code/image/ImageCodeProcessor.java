package com.hccake.ballcat.common.code.image;

import cn.hutool.core.img.ImgUtil;
import com.hccake.ballcat.common.code.impl.AbstractValidateCodeProcessor;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * ImageCodeProcessor
 *
 * @author xm.z
 */
public class ImageCodeProcessor extends AbstractValidateCodeProcessor<ImageCode> {

	/**
	 * Send the graphic verification code and return it to the user as a picture stream
	 */
	@Override
	protected void send(ServletWebRequest request, ImageCode imageCode) throws Exception {
		ImgUtil.writePng(imageCode.getImage(), request.getResponse().getOutputStream());
	}

}