package com.hccake.ballcat.common.model.result;

import com.hccake.common.i18n.annotation.I18nField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 响应信息主体
 *
 * @param <T>
 * @author Hccake
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "返回体结构")
public class R<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "返回状态码")
	private int code;

	@ApiModelProperty(value = "返回信息")
	private String message;

	@ApiModelProperty(value = "数据")
	private T data;

	public static <T> R<T> ok() {
		return ok(null);
	}

	public static <T> R<T> ok(T data) {
		return new R<T>().setCode(SystemResultCode.SUCCESS.getCode()).setData(data)
				.setMessage(SystemResultCode.SUCCESS.getMessage());
	}

	public static <T> R<T> ok(T data, String message) {
		return new R<T>().setCode(SystemResultCode.SUCCESS.getCode()).setData(data).setMessage(message);
	}

	public static <T> R<T> failed(int code, String message) {
		return new R<T>().setCode(code).setMessage(message);
	}

	public static <T> R<T> failed(ResultCode failMsg) {
		return new R<T>().setCode(failMsg.getCode()).setMessage(failMsg.getMessage());
	}

	public static <T> R<T> failed(ResultCode failMsg, String message) {
		return new R<T>().setCode(failMsg.getCode()).setMessage(message);
	}

}
