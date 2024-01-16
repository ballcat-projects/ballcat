package org.ballcat.common.function;

/**
 * @author lingting 2023/2/2 17:36
 */
@FunctionalInterface
@SuppressWarnings("java:S112")
public interface ThrowingBiFunction<T, D, R> {

	R apply(T t, D d) throws Exception;

}
