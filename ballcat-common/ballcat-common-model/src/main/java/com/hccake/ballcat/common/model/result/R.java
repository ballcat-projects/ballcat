package com.hccake.ballcat.common.model.result;

import com.hccake.ballcat.common.i18n.I18nClass;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 响应信息主体
 *
 * @param <T>
 * @author Hccake
 */
@I18nClass
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(title = "返回体结构")
public class R<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(title = "返回状态码")
	private int code;

	@Schema(title = "返回信息")
	private String message;

	@Schema(title = "数据")
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
