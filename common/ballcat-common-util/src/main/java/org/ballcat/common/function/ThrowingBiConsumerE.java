package org.ballcat.common.function;

/**
 * @author lingting 2023-12-22 11:49
 */
@FunctionalInterface
public interface ThrowingBiConsumerE<T, D, E extends Exception> {

	void accept(T t, D d) throws E;

}
