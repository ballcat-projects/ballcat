package org.ballcat.common.function;

/**
 * @author lingting 2023/1/21 22:56
 */
@FunctionalInterface
@SuppressWarnings("java:S112")
public interface ThrowingBiConsumer<T, D> {

	void accept(T t, D d) throws Exception;

}
