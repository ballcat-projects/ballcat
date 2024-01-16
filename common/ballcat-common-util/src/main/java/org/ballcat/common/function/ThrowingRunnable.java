package org.ballcat.common.function;

/**
 * @author lingting 2023/1/16 17:46
 */
@FunctionalInterface
@SuppressWarnings("java:S112")
public interface ThrowingRunnable {

	void run() throws Exception;

}
