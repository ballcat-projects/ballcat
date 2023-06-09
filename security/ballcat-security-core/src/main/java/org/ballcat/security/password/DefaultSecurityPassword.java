package org.ballcat.security.password;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author lingting 2023-03-30 15:10
 */
public class DefaultSecurityPassword implements SecurityPassword {

	private final String securityKey;

	private final BCryptPasswordEncoder encode = new BCryptPasswordEncoder();

	public DefaultSecurityPassword(String securityKey) {
		this.securityKey = securityKey;
	}

	public boolean valid(String plaintext) {
		return StringUtils.hasText(plaintext);
	}

	private BytesEncryptor frontEncryptor() {
		return Encryptors.standard(securityKey, securityKey);
	}

	/**
	 * 依据前端加密方式, 明文转密文
	 */
	public String encodeFront(String plaintext) {
		BytesEncryptor encryptor = frontEncryptor();
		Base64.Encoder encoder = Base64.getEncoder();

		byte[] bytes = encryptor.encrypt(plaintext.getBytes(StandardCharsets.UTF_8));
		return encoder.encodeToString(bytes);
	}

	/**
	 * 解析收到的前端密文
	 */
	public String decodeFront(String ciphertext) {
		BytesEncryptor encryptor = frontEncryptor();
		Base64.Decoder decoder = Base64.getDecoder();

		byte[] bytes = decoder.decode(ciphertext.getBytes(StandardCharsets.UTF_8));
		byte[] result = encryptor.decrypt(bytes);
		return new String(result, StandardCharsets.UTF_8);
	}

	/**
	 * 密码明文转数据库存储的密文
	 */
	public String encode(String plaintext) {
		return encode.encode(plaintext);
	}

	/**
	 * 明文和密文是否匹配
	 */
	public boolean match(String plaintext, String ciphertext) {
		return encode.matches(plaintext, ciphertext);
	}

}
