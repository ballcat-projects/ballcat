package com.hccake.starter.sms.impl.xinkuka.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author lingting 2020/5/6 17:51
 */
@Data
@Accessors(chain = true)
@TableName("sms_send")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "短信发送表")
public class XinKuKaSend extends Model<XinKuKaSend> {

	@TableId
	private Long id;

	@ApiModelProperty("大于0表示指定端口号发送")
	@TableField("PortNum")
	private Integer portNum;

	@ApiModelProperty("接收号码")
	@TableField("smsNumber")
	private String smsNumber;

	@ApiModelProperty("彩信标题，如果发送彩信不能为空")
	@TableField("smsSubject")
	private String smsSubject;

	@ApiModelProperty("发送内容")
	@TableField("smsContent")
	private String smsContent;

	@ApiModelProperty("0:短信 1:彩信")
	@TableField("smsType")
	private Integer smsType;

	@ApiModelProperty("手机号")
	@TableField("PhoNum")
	private String phoNum;

	@ApiModelProperty("状态")
	@TableField("smsState")
	private Integer smsState;

}
