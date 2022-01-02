package com.hccake.ballcat.extend.tesseract;

import com.hccake.ballcat.extend.tesseract.enums.TesseractLang;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
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
	 * @param path 例: C:\Program Files\Tesseract-OCR\tesseract.exe
	 */
	public void setTesseractPath(String path) {
		tesseractPath = path;
	}

	public List<String> toString(TesseractImage image) {
		return toString(image, getLang());
	}

	public List<String> toString(TesseractImage image, String lang) {
		final TesseractCommand command = TesseractCommand.builder().boxes(false).tesseract(tesseractPath).lang(lang)
				.image(image).build();
		return command.run();
	}

	public List<TesseractBoxes> toBoxes(TesseractImage image) {
		return toBoxes(image, getLang());
	}

	public List<TesseractBoxes> toBoxes(TesseractImage image, String lang) {
		final TesseractCommand command = TesseractCommand.builder().boxes(true).tesseract(tesseractPath).lang(lang)
				.image(image).build();
		return TesseractBoxes.of(command.run(), image);
	}

}
