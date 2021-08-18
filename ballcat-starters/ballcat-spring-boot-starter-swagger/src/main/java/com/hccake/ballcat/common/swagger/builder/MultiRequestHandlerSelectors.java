package com.hccake.ballcat.common.swagger.builder;

import org.springframework.util.ClassUtils;
import springfox.documentation.RequestHandler;

import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 多路径选择
 *
 * @author hccake
 */
public class MultiRequestHandlerSelectors {

	private MultiRequestHandlerSelectors() {
		throw new UnsupportedOperationException();
	}

	private static final String PACKAGE_SEPARATOR = ";";

	/**
	 * Any RequestHandler satisfies this condition
	 * @return predicate that is always true
	 */
	public static Predicate<RequestHandler> any() {
		return each -> true;
	}

	/**
	 * No RequestHandler satisfies this condition
	 * @return predicate that is always false
	 */
	public static Predicate<RequestHandler> none() {
		return each -> false;
	}

	/**
	 * Predicate that matches RequestHandler with handlers methods annotated with given
	 * annotation
	 * @param annotation - annotation to check
	 * @return this
	 */
	public static Predicate<RequestHandler> withMethodAnnotation(final Class<? extends Annotation> annotation) {
		return input -> input.isAnnotatedWith(annotation);
	}

	/**
	 * Predicate that matches RequestHandler with given annotation on the declaring class
	 * of the handler method
	 * @param annotation - annotation to check
	 * @return this
	 */
	public static Predicate<RequestHandler> withClassAnnotation(final Class<? extends Annotation> annotation) {
		return input -> declaringClass(input).map(annotationPresent(annotation)).orElse(false);
	}

	private static Function<Class<?>, Boolean> annotationPresent(final Class<? extends Annotation> annotation) {
		return input -> input.isAnnotationPresent(annotation);
	}

	private static Function<Class<?>, Boolean> handlerPackage(final String basePackage) {
		return input -> {
			String inputPackageName = ClassUtils.getPackageName(input);
			String[] basePackages = basePackage.split(PACKAGE_SEPARATOR);
			for (String packageName : basePackages) {
				if (inputPackageName.startsWith(packageName)) {
					return true;
				}
			}
			return false;
		};
	}

	/**
	 * Predicate that matches RequestHandler with given base package name for the class of
	 * the handler method. This predicate includes all request handlers matching the
	 * provided basePackage
	 * @param basePackage - base package of the classes
	 * @return this
	 */
	public static Predicate<RequestHandler> basePackage(String basePackage) {
		return input -> declaringClass(input).map(handlerPackage(basePackage)).orElse(true);
	}

	private static Optional<Class<?>> declaringClass(RequestHandler input) {
		return Optional.ofNullable(input.declaringClass());
	}

}
