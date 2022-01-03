package com.hccake.ballcat.extend.tesseract;

import com.hccake.ballcat.extend.tesseract.enums.TesseractLang;
import java.util.List;
import java.util.function.Consumer;
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

	/**
	 * tesseract 命令执行. 方便使用者重写. 自定义自己的命令.
	 */
	public List<String> run(Consumer<TesseractCommand.TesseractCommandBuilder> builderConsumer) {
		final TesseractCommand.TesseractCommandBuilder builder = TesseractCommand.builder().tesseract(tesseractPath);
		builderConsumer.accept(builder);
		return builder.build().run();
	}

	public List<String> toString(TesseractImage image) {
		return toString(image, getLang());
	}

	public List<String> toString(TesseractImage image, String lang) {
		return run(builder -> builder.lang(lang).image(image));
	}

	public List<TesseractBox> toBoxes(TesseractImage image) {
		return toBoxes(image, getLang());
	}

	public List<TesseractBox> toBoxes(TesseractImage image, String lang) {
		return TesseractBox.of(run(builder -> builder.lang(lang).image(image).boxes(true)), image);
	}

}
