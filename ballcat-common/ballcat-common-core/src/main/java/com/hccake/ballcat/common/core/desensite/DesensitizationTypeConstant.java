package com.hccake.ballcat.common.core.desensite;

/**
 * 默认的一些脱敏方式
 *
 * @author Hccake 2021/1/22
 * @version 1.0
 */
public final class DesensitizationTypeConstant {

	private DesensitizationTypeConstant() {
	}

	/**
	 * 全脱敏
	 * @see com.hccake.ballcat.common.core.desensite.handler.AllMaskDesensitizationHandler
	 */
	public static final String ALL_MASK = "ALL_MASK";

	/**
	 * 银行卡脱敏
	 * @see com.hccake.ballcat.common.core.desensite.handler.BankCardNODesensitizationHandler
	 */
	public static final String BANK_CARD_NO = "BANK_CARD_NO";

	/**
	 * 邮箱脱敏
	 * @see com.hccake.ballcat.common.core.desensite.handler.EmailDesensitizationHandler
	 */
	public static final String EMAIL = "EMAIL";

	/**
	 * 加密后的密码脱敏
	 * @see com.hccake.ballcat.common.core.desensite.handler.EncryptedPasswordDesensitizationHandler
	 */
	public static final String ENCRYPTED_PASSWORD = "ENCRYPTED_PASSWORD";

	/**
	 * 定长脱敏，总是返回定长的数据值
	 * @see com.hccake.ballcat.common.core.desensite.handler.FixedLengthDesensitizationHandler
	 */
	public static final String FIXED_LENGTH = "FIXED_LENGTH";

	/**
	 * 身份证号脱敏
	 * @see com.hccake.ballcat.common.core.desensite.handler.IDCardNODesensitizationHandler
	 */
	public static final String ID_CARD_NO = "ID_CARD_NO";

	/**
	 * 手机号脱敏
	 * @see com.hccake.ballcat.common.core.desensite.handler.PhoneNumberDesensitizationHandler
	 */
	public static final String PHONE_NUMBER = "PHONE_NUMBER";

}
