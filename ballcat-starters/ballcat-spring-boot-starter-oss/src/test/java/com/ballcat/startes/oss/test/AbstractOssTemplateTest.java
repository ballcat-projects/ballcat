package com.ballcat.startes.oss.test;

import com.hccake.ballcat.common.oss.OssAutoConfiguration;
import com.hccake.ballcat.common.oss.OssTemplate;
import com.hccake.ballcat.common.oss.prefix.ObjectKeyPrefixConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 抽象OSS操作测试模板
 *
 * @author lishangbu
 * @date 2022/10/27
 */
@SpringBootTest(classes = OssAutoConfiguration.class)
public abstract class AbstractOssTemplateTest {

	@Autowired
	protected OssTemplate ossTemplate;

	@Autowired
	protected ObjectKeyPrefixConverter objectKeyPrefixConverter;

	protected void createBucket(String bucket) {
		ossTemplate.createBucket(bucket);
	}

	protected void deleteBucket(String bucket) {
		ossTemplate.deleteBucket(bucket);
	}

}
