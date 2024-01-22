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

package org.ballcat.dingtalk;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.ballcat.dingtalk.message.DingTalkActionCardMessage;

/**
 * @author lingting 2020/6/12 19:35
 */
@Getter
@Setter
@Accessors(chain = true)
public class DingTalkParams {

	@JsonProperty("msgtype")
	private String type;

	private At at;

	private ActionCard actionCard;

	private Link link;

	private Markdown markdown;

	private Text text;

	@Override
	@SneakyThrows(JsonProcessingException.class)
	public String toString() {
		return new ObjectMapper().writeValueAsString(this);
	}

	@Data
	@Accessors(chain = true)
	public static class Text {

		private String content;

	}

	@Data
	@Accessors(chain = true)
	public static class Markdown {

		private String title;

		private String text;

	}

	@Data
	@Accessors(chain = true)
	public static class Link {

		private String text;

		private String title;

		private String picUrl;

		private String messageUrl;

	}

	@Data
	@Accessors(chain = true)
	public static class ActionCard {

		private String title;

		private String text;

		private String btnOrientation;

		private String singleTitle;

		@JsonProperty("singleURL")
		private String singleUrl;

		@JsonProperty("btns")
		private List<DingTalkActionCardMessage.Button> buttons;

	}

	@Data
	@Accessors(chain = true)
	public static class At {

		@JsonProperty("isAtAll")
		private boolean atAll;

		private Set<String> atMobiles;

	}

}
