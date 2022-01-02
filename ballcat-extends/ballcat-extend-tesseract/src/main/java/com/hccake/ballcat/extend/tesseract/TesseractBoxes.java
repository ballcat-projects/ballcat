package com.hccake.ballcat.extend.tesseract;

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
public class TesseractBoxes implements Serializable {

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

	public static List<TesseractBoxes> of(List<String> lines, TesseractImage image) {
		final int height = image.getHeight();
		List<TesseractBoxes> list = new ArrayList<>();

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

			final TesseractBoxes boxes = new TesseractBoxes();
			boxes.text = split[0];

			boxes.x = Integer.parseInt(split[1]);
			boxes.y = height - Integer.parseInt(split[4]);

			boxes.width = Integer.parseInt(split[3]) - boxes.x;
			boxes.height = Integer.parseInt(split[4]) - Integer.parseInt(split[2]);

			boxes.index = Integer.parseInt(split[5]);
			list.add(boxes);
		}

		return list;
	}

}
