package com.hccake.ballcat.commom.storage;

import static com.hccake.ballcat.commom.storage.constant.StorageConstants.PATH_FLAG;

import com.hccake.ballcat.commom.storage.domain.StreamTemp;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.util.Assert;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/1/7 16:28
 */
public interface FileStorageClient {

	/**
	 * 文件上传, 本方法会读一遍流, 计算流大小, 推荐使用 upload(stream, filePath, size) 方法
	 * @param filePath 存储对象名称
	 * @param stream 文件输入流
	 * @return 文件相对路径
	 * @throws IOException 流操作时异常
	 */
	default String upload(InputStream stream, String filePath) throws IOException {
		final StreamTemp temp = getSize(stream);
		return upload(temp.getStream(), filePath, temp.getSize());
	}

	/**
	 * 上传文件
	 * @param stream 文件流
	 * @param filePath 文件路径
	 * @param size 输入流大小(仅在使用亚马逊云时有效)
	 * @return 文件路径
	 * @author lingting 2021-04-16 15:20
	 */
	String upload(InputStream stream, String filePath, Long size);

	/**
	 * 删除路径
	 * @param path 路径
	 * @author lingting 2021-04-16 15:26
	 */
	void delete(String path);

	/**
	 * 复制文件
	 * @param source 原对象名
	 * @param target 目标对象名
	 */
	void copy(String source, String target);

	/**
	 * 获取下载路径
	 * @param filePath 文件路径
	 * @return java.lang.String
	 * @author lingting 2021-04-16 15:33
	 */
	String getDownloadUrl(String filePath);

	/**
	 * 获取文件操作根路径
	 * @return java.lang.String
	 * @author lingting 2021-05-10 15:57
	 */
	String getRoot();

	/**
	 * 方便有其他更好的计算大小实现时可以先替换
	 * @param stream 要计算大小的流
	 * @return StreamTemp 返回大小和一个完全相同的新流
	 * @author lingting 2021-05-10 15:29
	 * @throws IOException 流操作时异常
	 */
	default StreamTemp getSize(InputStream stream) throws IOException {
		return StreamTemp.of(stream);
	}

	/**
	 * 获取真实文件路径
	 * @param path 相对 root 的文件路径
	 * @return java.lang.String
	 * @author lingting 2021-05-10 15:58
	 */
	default String getPath(String path) {
		Assert.hasText(path, "path must not be empty");

		// 路径以 root 开头, 表示已经是真实路径了
		if (path.startsWith(getRoot())) {
			return path;
		}

		if (path.startsWith(PATH_FLAG)) {
			path = path.substring(1);
		}

		return getRoot() + path;
	}

}
