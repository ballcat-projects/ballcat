package com.hccake.starter.file;

import com.hccake.ballcat.common.util.FileUtils;
import com.hccake.ballcat.common.util.StreamUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.util.StringUtils;

/**
 * @author lingting 2021/10/17 20:11
 */
public class FileClient {

	public static final String SLASH = File.separator;

	private final File parentDir;

	private final String parentDirPath;

	public FileClient(FileProperties properties) throws FileException {
		final File dir = StringUtils.hasText(properties.getPath()) ? new File(properties.getPath())
				: FileUtils.getSystemTempDir();

		// 不存在且创建失败
		if (!dir.exists() && !dir.mkdirs()) {
			throw new FileException(String.format("路径: %s; 不存在且创建失败! 请检查是否拥有对该路径的操作权限!", dir.getPath()));
		}

		parentDir = dir;
		parentDirPath = dir.getPath();
	}

	/**
	 * 获取操作的根路径
	 * @return java.lang.String
	 * @author lingting 2021-10-18 11:24
	 */
	public String getRoot() {
		return parentDirPath;
	}

	/**
	 * 获取完整路径
	 * @param relativePath 文件相对 getRoot() 的路径@return java.lang.String
	 * @author lingting 2021-10-18 16:40
	 */
	public String getWholePath(String relativePath) {
		if (relativePath.startsWith(SLASH)) {
			return getRoot() + relativePath.substring(1);
		}
		return getRoot() + relativePath;
	}

	/**
	 *
	 * 文件上传
	 * @param relativePath 文件相对 getRoot() 的路径
	 * @param stream 文件输入流
	 * @return 文件绝对路径
	 * @author lingting 2021-10-19 22:32
	 */
	public String upload(InputStream stream, String relativePath) throws IOException {
		// 目标文件
		final File file = new File(parentDir, relativePath);

		if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
			throw new FileException("文件上传失败! 创建父级文件夹失败! 父级路径: " + file.getParentFile().getPath());
		}

		if (!file.exists() && !file.createNewFile()) {
			throw new FileException("文件上传失败! 创建文件失败! 文件路径: " + file.getPath());
		}

		try (FileOutputStream outputStream = new FileOutputStream(file)) {
			StreamUtils.inputToOutput(stream, outputStream);
		}

		return file.getPath();
	}

	/**
	 * 下载文件
	 * @param relativePath 文件相对 getRoot() 的路径
	 * @return java.io.FileOutputStream 文件流
	 * @author lingting 2021-10-18 16:48
	 */
	public File download(String relativePath) {
		return new File(parentDir, relativePath);
	}

	/**
	 * 删除文件
	 * @param relativePath 文件相对 getRoot() 的路径
	 * @return boolean
	 * @author lingting 2021-10-18 17:14
	 */
	public boolean delete(String relativePath) throws IOException {
		final File file = new File(parentDir, relativePath);
		return file.exists() && file.delete();
	}

}
