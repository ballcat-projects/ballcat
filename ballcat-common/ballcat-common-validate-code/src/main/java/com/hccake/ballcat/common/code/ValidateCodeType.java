package com.hccake.ballcat.common.code;

import com.hccake.ballcat.common.code.constant.CodeConstants;

/**
 * ValidateCodeType
 *
 * @author xm.z
 */
public enum ValidateCodeType {

	SMS {
		@Override
		public String getParamNameOnValidate() {
			return CodeConstants.DEFAULT_PARAMETER_NAME_CODE_SMS;
		}
	},

	IMAGE {
		@Override
		public String getParamNameOnValidate() {
			return CodeConstants.DEFAULT_PARAMETER_NAME_CODE_IMAGE;
		}
	};

	/**
	 * The name of the parameter obtained from the request during verification
	 * @return parameter name
	 */
	public abstract String getParamNameOnValidate();

}