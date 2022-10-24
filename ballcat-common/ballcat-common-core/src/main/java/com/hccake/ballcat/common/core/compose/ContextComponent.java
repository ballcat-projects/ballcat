package com.hccake.ballcat.common.core.compose;

/**
 * 上下文组件, 在接入对应的上下文时(如: spring 的 bean) 便于在 开始和结束时执行对应的方法
 * <p>
 * 默认自动接入spring
 * </p>
 * <p>
 * 一般用于线程类实例达成接入到对应的上下文环境时自动开启和结束线程
 * </p>
 *
 * @author lingting 2022/10/15 17:55
 */
public interface ContextComponent {

	/**
	 * 上下文准备好之后调用, 内部做一些线程的初始化以及线程启动
	 */
	void onApplicationStart();

	/**
	 * 在上下文销毁前调用, 内部做线程停止和数据缓存相关
	 */
	void onApplicationStop();

}
