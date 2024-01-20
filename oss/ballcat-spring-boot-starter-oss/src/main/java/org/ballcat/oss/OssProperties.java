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

package org.ballcat.oss;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import software.amazon.awssdk.regions.Region;

/**
 * @author Hccake 2019/7/16 15:34
 */

@Data
@ConfigurationProperties(prefix = OssProperties.PREFIX)
public class OssProperties {

	public static final String PREFIX = "ballcat.oss";

	/**
	 * 是否开启OSS
	 */
	private Boolean enabled = true;

	/**
	 *
	 * <p>
	 * endpoint 节点地址, 例如:
	 * </p>
	 * <ul>
	 * <li>阿里云节点: oss-cn-qingdao.aliyuncs.com</li>
	 * <li>亚马逊s3节点: s3.ap-southeast-1.amazonaws.com</li>
	 * <li>亚马逊节点: ap-southeast-1.amazonaws.com</li>
	 * </ul>
	 * <p>
	 * 只需要完整 正确的节点地址即可
	 * </p>
	 */
	private String endpoint;

	/**
	 * <p>
	 * 区域
	 * </p>
	 *
	 * <p>
	 * 如果配置了自定义域名(domain), 必须配置本值
	 * </p>
	 * <p>
	 * 如果没有配置自定义域名(domain), 配置了节点, 那么当前值可以不配置
	 * </p>
	 */
	private String region = Region.CN_NORTH_1.id();

	/**
	 * 自定义域名，配置此参数时，返回url优先使用
	 * <p>
	 * 在复杂的企业网络环境中，可能存在内部维护oss实例通过内部程序代码上传，然后通过前端网关或者nginx暴露oss端点的情况,
	 * 即内部上传地址与外部访问地址不一致，此时可以通过配置该属性隐藏内部实例细节并提供给外部便捷访问
	 * </p>
	 * <p>
	 * 如将<code>endpoint</code>设置为http://192.168.1.3:9000，<code>domain</code>设置为<code>http://example.com</code>,就可以在内部通过http://192.168.1.3:9000上传删除等文件操作，外部通过http://example.com访问，
	 * </p>
	 * <p>
	 * ⚠️该配置只会处理API渲染返回的情况，如果需要文件操作，一定要配置<code>endpoint</code>属性,这是和老版逻辑不一致的地方
	 * </p>
	 */
	private String domain;

	/**
	 * 密钥key
	 */
	private String accessKey;

	/**
	 * 密钥Secret
	 */
	private String accessSecret;

	/**
	 *
	 * 默认存储桶
	 *
	 */
	private String bucket;

	/**
	 * 所有 oss 对象 key 的前缀
	 */
	private String objectKeyPrefix;

	/**
	 * <p>
	 * S3支持virtual-hosted style URL和path style URL两种访问bucket的方式
	 * </p>
	 * <li>
	 * <ol>
	 * Path style URL:在path style URL中，bucket的名子紧跟在domain之后，成为URL path的一部分
	 * 形如:http://s3endpoint/BUCKET
	 * <p>
	 * 例如，如果你要把photo.jpg存放在region为us-west-2，bucket为images的bucket中，你可用以下的方式来访问这个文件：
	 * </p>
	 * <p>
	 * http://s3-us-west-2.amazonaws.com/images/photo.jpg
	 * </p>
	 *
	 * <p>
	 * http://s3.us-west-2.amazonaws.com/images/photo.jpg
	 * </p>
	 * <p>
	 * 如果，这个bucket在us-east-1这个region中，你可以使用如下方式：
	 * </p>
	 * <p>
	 * http://s3.amazonaws.com/images/photo.jpg
	 *
	 * </p>
	 * <p>
	 * http://s3-external-1.amazonaws.com/images/photo.jpg
	 * </p>
	 * <p>
	 * 此值为true时,默认开启此风格。可用于nginx 反向代理和S3默认支持
	 * </p>
	 * </ol>
	 * <ol>
	 * Virtual-hosted style URL:在virtual-hosted style
	 * URL中，bucket的名称成为了subdomain:http://BUCKET.s3endpoint
	 * <p>
	 * 例如，如果你要把photo.jpg存放在region为us-west-2，bucket为images的bucket中，你可用以下的方式来访问这个文件：
	 * </p>
	 * <p>
	 * http://images.s3-us-west-2.amazonaws.com/photo.jpg
	 * </p>
	 * <p>
	 * http://images.s3.us-west-2.amazonaws.com/photo.jpg
	 * </p>
	 * <p>
	 * 如果，这个bucket在us-east-1这个region中，你可以使用如下方式：
	 * </p>
	 * <p>
	 * http://images.s3.amazonaws.com/photo.jpg
	 * </p>
	 * <p>
	 * http://images.s3-external-1.amazonaws.com/photo.jpg
	 * </p>
	 * <p>
	 * 此值为false时,默认开启此风格。可用于阿里云等
	 * </p>
	 * </ol>
	 * </li>
	 *
	 */
	private Boolean pathStyleAccess = true;

	/**
	 * 是否将数据进行分块传输 aliyun 不支持分块传输
	 */
	private Boolean chunkedEncoding = true;

}
