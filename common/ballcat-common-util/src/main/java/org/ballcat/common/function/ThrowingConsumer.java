package org.ballcat.common.function;

/**
 * @author lingting 2023/1/21 22:56
 */
@FunctionalInterface
@SuppressWarnings("java:S112")
public interface ThrowingConsumer<T> {

	void accept(T t) throws Exception;

}
