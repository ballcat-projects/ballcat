package com.hccake.ballcat.extend.tesseract;

import com.hccake.ballcat.extend.tesseract.enums.TesseractLang;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.function.Consumer;

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
		tesseractPath = path;
	}

	/**
	 * tesseract 命令执行, 方便使用者重写. 自定义自己的命令.
	 * @param builderConsumer a {@link java.util.function.Consumer} object.
	 * @return a {@link java.util.List} object
	 */
	public List<String> run(Consumer<TesseractCommand.TesseractCommandBuilder> builderConsumer) {
		final TesseractCommand.TesseractCommandBuilder builder = TesseractCommand.builder().tesseract(tesseractPath);
		builderConsumer.accept(builder);
		return builder.build().run();
	}

	/**
	 * <p>
	 * toString.
	 * </p>
	 * @param image a {@link com.hccake.ballcat.extend.tesseract.TesseractImage} object.
	 * @return a {@link java.util.List} object.
	 */
	public List<String> toString(TesseractImage image) {
		return toString(image, getLang());
	}

	/**
	 * <p>
	 * toString.
	 * </p>
	 * @param image a {@link com.hccake.ballcat.extend.tesseract.TesseractImage} object.
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
	 * @param image a {@link com.hccake.ballcat.extend.tesseract.TesseractImage} object.
	 * @return a {@link java.util.List} object.
	 */
	public List<TesseractBox> toBoxes(TesseractImage image) {
		return toBoxes(image, getLang());
	}

	/**
	 * <p>
	 * toBoxes.
	 * </p>
	 * @param image a {@link com.hccake.ballcat.extend.tesseract.TesseractImage} object.
	 * @param lang a {@link java.lang.String} object.
	 * @return a {@link java.util.List} object.
	 */
	public List<TesseractBox> toBoxes(TesseractImage image, String lang) {
		return TesseractBox.of(run(builder -> builder.lang(lang).image(image).boxes(true)), image);
	}

}
