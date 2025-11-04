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

package org.ballcat.oss.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.apache.tika.detect.DefaultDetector;

/**
 * 通用 MIME 类型检测工具（仅使用 Apache Tika + JDK NIO）
 * <p>
 * - 优先使用 Tika（基于文件头检测） - 失败则回退到 Files.probeContentType() - 线程安全：Tika 单例可安全复用
 * <p>
 * 注意： - 传入的 InputStream 由调用方负责关闭；本方法不会关闭该流（Tika.detect 可能会读取流的一部分/全部）。 - 有些 Tika 版本没有
 * Metadata.RESOURCE_NAME_KEY，因此这里使用字面键 "resourceName" 作为文件名提示。
 *
 * @author Hccake
 */
@Slf4j
public final class MimeTypeDetector {

	private static final String DEFAULT_MIME_TYPE = "application/octet-stream";

	/** 线程安全单例 */
	private static final Tika TIKA = new Tika(new DefaultDetector());

	private MimeTypeDetector() {
	}

	/**
	 * 检测文件的 MIME 类型。
	 * @param file 待检测的文件（不能为 null）
	 * @return 检测到的 MIME 类型，若无法识别返回 application/octet-stream
	 */
	public static String detect(File file) {
		if (file == null) {
			log.warn("detect(File) called with null file, returning default MIME type.");
			return DEFAULT_MIME_TYPE;
		}

		// 1) Apache Tika
		try {
			String type = TIKA.detect(file);
			if (isValid(type)) {
				return type;
			}
			else {
				log.debug("Tika returned invalid type for file {}: {}", file, type);
			}
		}
		catch (Throwable t) {
			log.warn("Tika failed to detect MIME type for file {}. Will try Files.probeContentType().", file, t);
		}

		// 2) JDK NIO fallback
		try {
			Path path = file.toPath();
			String type = Files.probeContentType(path);
			if (isValid(type)) {
				return type;
			}
			else {
				log.debug("Files.probeContentType returned invalid type for file {}: {}", file, type);
			}
		}
		catch (Throwable t) {
			log.warn("Files.probeContentType failed for file {}.", file, t);
		}

		log.info("Unable to detect MIME type for file {} — returning default {}", file, DEFAULT_MIME_TYPE);
		return DEFAULT_MIME_TYPE;
	}

	private static boolean isValid(String type) {
		return type != null && !type.trim().isEmpty() && !DEFAULT_MIME_TYPE.equals(type);
	}

}
