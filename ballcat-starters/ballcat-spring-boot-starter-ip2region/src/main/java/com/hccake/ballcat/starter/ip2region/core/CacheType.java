package com.hccake.ballcat.starter.ip2region.core;

/**
 * 搜索服务实现
 *
 * @author lishangbu
 * @date 2022/10/16
 */
public enum CacheType {

	/**
	 * 不缓存
	 * <p>
	 * 完全基于文件的查询
	 * </p>
	 */
	NONE,
	/**
	 * 缓存 VectorIndex 索引
	 * <p>
	 * 提前从 xdb 文件中加载出来 VectorIndex 数据，然后全局缓存，每次创建 Searcher 对象的时候使用全局的 VectorIndex
	 * 缓存可以减少一次固定的 IO 操作，从而加速查询，减少 IO 压力
	 * </p>
	 */
	VECTOR_INDEX,
	/**
	 * 缓存整个 xdb 数据
	 * <p>
	 * 预先加载整个 ip2region.xdb 的数据到内存，然后基于这个数据创建查询对象来实现完全基于文件的查询，类似之前的 memory search
	 * </p>
	 */
	XDB

}
