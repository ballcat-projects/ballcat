package com.hccake.ballcat.common.security.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

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

	/**
	 * 创建一个密码加密的代理，方便后续切换密码的加密算法
	 * @see PasswordEncoderFactories#createDelegatingPasswordEncoder()
	 * @return DelegatingPasswordEncoder
	 */
	@SuppressWarnings("deprecation")
	private static PasswordEncoder createDelegatingPasswordEncoder() {
		String encodingId = "bcrypt";
		Map<String, PasswordEncoder> encoders = new HashMap<>(10);
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		encoders.put(encodingId, bCryptPasswordEncoder);
		encoders.put("ldap", new org.springframework.security.crypto.password.LdapShaPasswordEncoder());
		encoders.put("MD4", new org.springframework.security.crypto.password.Md4PasswordEncoder());
		encoders.put("MD5", new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("MD5"));
		encoders.put("noop", org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance());
		encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
		encoders.put("scrypt", new SCryptPasswordEncoder());
		encoders.put("SHA-1", new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("SHA-1"));
		encoders.put("SHA-256",
				new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("SHA-256"));
		encoders.put("sha256", new org.springframework.security.crypto.password.StandardPasswordEncoder());
		encoders.put("argon2", new Argon2PasswordEncoder());
		DelegatingPasswordEncoder delegatingPasswordEncoder = new DelegatingPasswordEncoder(encodingId, encoders);

		// 设置默认的密码解析器，以便兼容历史版本的密码
		delegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(bCryptPasswordEncoder);
		return delegatingPasswordEncoder;
	}

	public static final PasswordEncoder ENCODER = PasswordUtils.createDelegatingPasswordEncoder();

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
	 * 加密密码
	 * @param rawPassword 明文密码
	 * @return 密文密码
	 */
	public static String encode(CharSequence rawPassword) {
		return ENCODER.encode(rawPassword);
	}

	/**
	 * 判断明文密码和密文密码是否匹配
	 * @param rawPassword 明文密码
	 * @param encodedPassword 密文密码
	 * @return 匹配返回 true
	 */
	public static boolean matches(CharSequence rawPassword, String encodedPassword) {
		return ENCODER.matches(rawPassword, encodedPassword);
	}

	/**
	 * 判断是否需要升级加密算法
	 * @param encodedPassword 密文密码
	 * @return 需要返回 true
	 */
	public static boolean upgradeEncoding(String encodedPassword) {
		return ENCODER.upgradeEncoding(encodedPassword);
	}
}
