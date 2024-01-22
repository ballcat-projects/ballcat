/*
 * Copyright 2023-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ballcat.dingtalk.message;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.ballcat.common.markdown.MarkdownBuilder;
import org.ballcat.dingtalk.DingTalkParams;
import org.ballcat.dingtalk.enums.MessageTypeEnum;

/**
 * @author lingting 2020/6/10 22:13
 */
@Getter
@Setter
@Accessors(chain = true)
public class DingTalkMarkDownMessage extends AbstractDingTalkMessage {

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 内容
	 */
	private MarkdownBuilder text;

	@Override
	public MessageTypeEnum getType() {
		return MessageTypeEnum.MARKDOWN;
	}

	@Override
	public DingTalkParams put(DingTalkParams params) {
		return params.setMarkdown(new DingTalkParams.Markdown().setTitle(this.title).setText(this.text.build()));
	}

}
