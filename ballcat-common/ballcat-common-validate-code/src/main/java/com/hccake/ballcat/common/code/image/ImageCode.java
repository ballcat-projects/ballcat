package com.hccake.ballcat.common.code.image;

import com.hccake.ballcat.common.code.ValidateCode;
import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * ImageCode
 *
 * @author xm.z
 */
public class ImageCode extends ValidateCode implements Serializable {

	private static final long serialVersionUID = -6020470039852318468L;

	@Getter
	@Setter
	private BufferedImage image;

	public ImageCode(BufferedImage image, String code, int expireIn) {
		super(code, expireIn);
		this.image = image;
	}

	public ImageCode(BufferedImage image, String code, LocalDateTime expireTime) {
		super(code, expireTime);
		this.image = image;
	}

}