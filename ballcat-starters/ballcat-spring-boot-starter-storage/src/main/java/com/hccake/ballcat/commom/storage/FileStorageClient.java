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
	 * 文件上传, 本方法会读一遍流, 计算流大小, 推荐使用 upload(stream, absolutePath, size) 方法
	 * @param absolutePath 文件相对 getRoot() 的路径
	 * @param stream 文件输入流
	 * @return 文件绝对路径
	 * @throws IOException 流操作时异常
	 */
	default String upload(InputStream stream, String absolutePath) throws IOException {
		final StreamTemp temp = getSize(stream);
		return upload(temp.getStream(), absolutePath, temp.getSize());
	}

	/**
	 * 上传文件
	 * @param stream 文件流
	 * @param absolutePath 文件相对 getRoot() 的路径
	 * @param size 输入流大小(仅在使用亚马逊云时有效)
	 * @return 文件绝对路径
	 * @author lingting 2021-04-16 15:20
	 */
	String upload(InputStream stream, String absolutePath, Long size);

	/**
	 * 删除路径
	 * @param path 路径
	 * @author lingting 2021-04-16 15:26
	 */
	void delete(String path);

	/**
	 * 复制文件
	 * @param absoluteSource 相对 getRoot() 的 源文件 路径
	 * @param absoluteTarget 相对 getRoot() 的 目标文件 路径
	 */
	void copy(String absoluteSource, String absoluteTarget);

	/**
	 * 获取下载路径
	 * @param absolutePath 文件相对 getRoot() 的路径
	 * @return java.lang.String
	 * @author lingting 2021-04-16 15:33
	 */
	String getDownloadUrl(String absolutePath);

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
	 * @param absolutePath 文件相对 getRoot() 的路径
	 * @return 文件绝对路径
	 * @author lingting 2021-05-10 15:58
	 */
	default String getPath(String absolutePath) {
		Assert.hasText(absolutePath, "path must not be empty");

		if (absolutePath.startsWith(PATH_FLAG)) {
			absolutePath = absolutePath.substring(1);
		}

		return getRoot() + absolutePath;
	}

}
