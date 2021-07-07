package com.hccake.ballcat.common.security.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;

/**
 * 前后端交互中密码使用 AES 加密，模式: CBC，padding: PKCS5，偏移量暂不定制和密钥相同。 <br/>
 * 服务端OAuth2中，密码使用BCrypt方式加密
 *
 * @author Hccake
 * @version 1.0
 * @date 2019/9/25 15:14
 */
public final class PasswordUtils {

	private PasswordUtils() {
	}

	public static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

	/**
	 * 将前端传递过来的密文解密为明文
	 * @param aesPass AES加密后的密文
	 * @param secretKey 密钥
	 * @return 明文密码
	 */
	public static String decodeAES(String aesPass, String secretKey) {
		byte[] secretKeyBytes = secretKey.getBytes();
		AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, secretKeyBytes, secretKeyBytes);
		byte[] result = aes.decrypt(Base64.decode(aesPass.getBytes(StandardCharsets.UTF_8)));
		// 删除byte数组中补位产生的\u0000, 否则密码校验时会有问题
		return new String(result, StandardCharsets.UTF_8).replaceAll("[\u0000]", "");
	}

	/**
	 * 将明文密码加密为密文
	 * @param password 明文密码
	 * @param secretKey 密钥
	 * @return AES加密后的密文
	 */
	public static String encodeAESBase64(String password, String secretKey) {
		byte[] secretKeyBytes = secretKey.getBytes();
		AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, secretKeyBytes, secretKeyBytes);
		return aes.encryptBase64(password, StandardCharsets.UTF_8);
	}

	/**
	 * 使用BCrypt加密密码
	 * @param password 明文密码
	 * @return BCrypt加密后的密码
	 */
	public static String encodeBCrypt(String password) {
		return ENCODER.encode(password);
	}

}
