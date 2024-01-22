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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * 本类坐标系. 原点为 图片左上角
 *
 * @author lingting
 */
@Getter
@ToString
@EqualsAndHashCode
public class TesseractBox implements Serializable {

	private static final long serialVersionUID = 1L;

	private String text;

	/**
	 * 左上角 x 坐标
	 */
	private int x;

	/**
	 * 左上角 y 坐标
	 */
	private int y;

	/**
	 * 文本宽度
	 */
	private int width;

	/**
	 * 文本高度
	 */
	private int height;

	/**
	 * 第几张图片. 从 0 开始
	 */
	private int index;

	/**
	 * <p>
	 * of.
	 * </p>
	 * @param lines a {@link java.util.List} object.
	 * @param image a {@link TesseractImage} object.
	 * @return a {@link java.util.List} object.
	 */
	public static List<TesseractBox> of(List<String> lines, TesseractImage image) {
		final int height = image.getHeight();
		List<TesseractBox> list = new ArrayList<>();

		for (String line : lines) {
			/*
			 * abc 14 1246 21 1262 0
			 *
			 * abc - 文本
			 *
			 * 14 - 左下角 x 坐标
			 *
			 * 1246 - 左下角 y 坐标 到 图片底部的距离
			 *
			 * 21 - 右上角 x 坐标
			 *
			 * 1262 - 右上角 y 坐标 到图片底部的距离
			 *
			 * 0 - 第几张图片. 从 0 开始
			 */
			final String[] split = line.split(" ");

			final TesseractBox box = new TesseractBox();
			box.text = split[0];

			box.x = Integer.parseInt(split[1]);
			box.y = height - Integer.parseInt(split[4]);

			box.width = Integer.parseInt(split[3]) - box.x;
			box.height = Integer.parseInt(split[4]) - Integer.parseInt(split[2]);

			box.index = Integer.parseInt(split[5]);
			list.add(box);
		}

		return list;
	}

}
