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

package org.ballcat.tesseract;

import java.util.List;
import java.util.function.Consumer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.ballcat.tesseract.enums.TesseractLang;

/**
 * <p>
 * Tesseract class.
 * </p>
 *
 * @author lingting
 */
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class Tesseract {

	/**
	 * 默认认为已经把 tesseract 加入到了环境变量
	 */
	private String tesseractPath = "tesseract";

	private final String lang = TesseractLang.EN.getVal();

	/**
	 * 更新tesseract命令, 当你没有加入到环境变量时. 替换为全路径
	 * @param path 例: <code>C:\Program Files\Tesseract-OCR\tesseract.exe</code>
	 */
	public void setTesseractPath(String path) {
		this.tesseractPath = path;
	}

	/**
	 * tesseract 命令执行, 方便使用者重写. 自定义自己的命令.
	 * @param builderConsumer a {@link java.util.function.Consumer} object.
	 * @return a {@link java.util.List} object
	 */
	public List<String> run(Consumer<TesseractCommand.TesseractCommandBuilder> builderConsumer) {
		final TesseractCommand.TesseractCommandBuilder builder = TesseractCommand.builder()
			.tesseract(this.tesseractPath);
		builderConsumer.accept(builder);
		return builder.build().run();
	}

	/**
	 * <p>
	 * toString.
	 * </p>
	 * @param image a {@link TesseractImage} object.
	 * @return a {@link java.util.List} object.
	 */
	public List<String> toString(TesseractImage image) {
		return toString(image, getLang());
	}

	/**
	 * <p>
	 * toString.
	 * </p>
	 * @param image a {@link TesseractImage} object.
	 * @param lang a {@link java.lang.String} object.
	 * @return a {@link java.util.List} object.
	 */
	public List<String> toString(TesseractImage image, String lang) {
		return run(builder -> builder.lang(lang).image(image));
	}

	/**
	 * <p>
	 * toBoxes.
	 * </p>
	 * @param image a {@link TesseractImage} object.
	 * @return a {@link java.util.List} object.
	 */
	public List<TesseractBox> toBoxes(TesseractImage image) {
		return toBoxes(image, getLang());
	}

	/**
	 * <p>
	 * toBoxes.
	 * </p>
	 * @param image a {@link TesseractImage} object.
	 * @param lang a {@link java.lang.String} object.
	 * @return a {@link java.util.List} object.
	 */
	public List<TesseractBox> toBoxes(TesseractImage image, String lang) {
		return TesseractBox.of(run(builder -> builder.lang(lang).image(image).boxes(true)), image);
	}

}
