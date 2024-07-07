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

package org.ballcat.openapi.pageable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Optional;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.ballcat.common.model.domain.PageParam;
import org.ballcat.web.pageable.Pageable;
import org.springdoc.core.DelegatingMethodParameter;
import org.springdoc.core.customizers.DelegatingMethodParameterCustomizer;
import org.springframework.core.MethodParameter;

/**
 * 针对自定义 #{@link Pageable} 注解控制的 PageParam 入参的参数，进行 OpenAPI 的相关属性覆盖处理.
 *
 * @author Hccake
 * @since 2.0,0
 * @see org.springdoc.core.customizers.DataRestDelegatingMethodParameterCustomizer
 */
@Slf4j
public class PageableDelegatingMethodParameterCustomizer implements DelegatingMethodParameterCustomizer {

	@Override
	public void customize(MethodParameter originalParameter, MethodParameter methodParameter) {
		Class<?> parameterType = originalParameter.getParameterType();
		if (!parameterType.isAssignableFrom(PageParam.class)) {
			return;
		}

		Pageable pageable = originalParameter.getParameterAnnotation(Pageable.class);
		if (pageable == null) {
			return;
		}

		Field field = FieldUtils.getDeclaredField(DelegatingMethodParameter.class, "additionalParameterAnnotations",
				true);
		try {
			Annotation[] parameterAnnotations = (Annotation[]) field.get(methodParameter);
			if (ArrayUtils.isEmpty(parameterAnnotations)) {
				return;
			}
			for (int i = 0; i < parameterAnnotations.length; i++) {
				if (Parameter.class.equals(parameterAnnotations[i].annotationType())) {
					Optional<Annotation> annotationForField = getNewParameterAnnotationForField(methodParameter,
							pageable);
					if (annotationForField.isPresent()) {
						parameterAnnotations[i] = annotationForField.get();
					}
				}
			}
		}
		catch (IllegalAccessException e) {
			log.warn(e.getMessage());
		}
	}

	/**
	 * Gets new parameter annotation for field.
	 * @param methodParameter the method parameter
	 * @param pageable the pageable annotation to customer request param
	 * @return the new parameter annotation for field
	 */
	private Optional<Annotation> getNewParameterAnnotationForField(MethodParameter methodParameter, Pageable pageable) {
		String parameterName = methodParameter.getParameterName();
		Field field;
		Parameter parameterNew;
		try {
			field = methodParameter.getContainingClass().getDeclaredField(parameterName);
			Parameter parameter = field.getAnnotation(Parameter.class);
			parameterNew = new Parameter() {
				@Override
				public Class<? extends Annotation> annotationType() {
					return parameter.annotationType();
				}

				@Override
				public String name() {
					return getName(parameter.name(), pageable);
				}

				@Override
				public ParameterIn in() {
					return parameter.in();
				}

				@Override
				public String description() {
					return getDescription(parameterName, parameter.description(), pageable);
				}

				@Override
				public boolean required() {
					return parameter.required();
				}

				@Override
				public boolean deprecated() {
					return parameter.deprecated();
				}

				@Override
				public boolean allowEmptyValue() {
					return parameter.allowEmptyValue();
				}

				@Override
				public ParameterStyle style() {
					return parameter.style();
				}

				@Override
				public Explode explode() {
					return parameter.explode();
				}

				@Override
				public boolean allowReserved() {
					return parameter.allowReserved();
				}

				@Override
				public Schema schema() {
					return new Schema() {

						@Override
						public Class<? extends Annotation> annotationType() {
							return parameter.schema().annotationType();
						}

						@Override
						public Class<?> implementation() {
							return parameter.schema().implementation();
						}

						@Override
						public Class<?> not() {
							return parameter.schema().not();
						}

						@Override
						public Class<?>[] oneOf() {
							return parameter.schema().oneOf();
						}

						@Override
						public Class<?>[] anyOf() {
							return parameter.schema().anyOf();
						}

						@Override
						public Class<?>[] allOf() {
							return parameter.schema().allOf();
						}

						@Override
						public String name() {
							return parameter.schema().name();
						}

						@Override
						public String title() {
							return parameter.schema().title();
						}

						@Override
						public double multipleOf() {
							return parameter.schema().multipleOf();
						}

						@Override
						public String maximum() {
							return parameter.schema().maximum();
						}

						@Override
						public boolean exclusiveMaximum() {
							return parameter.schema().exclusiveMaximum();
						}

						@Override
						public String minimum() {
							return parameter.schema().minimum();
						}

						@Override
						public boolean exclusiveMinimum() {
							return parameter.schema().exclusiveMaximum();
						}

						@Override
						public int maxLength() {
							return parameter.schema().maxLength();
						}

						@Override
						public int minLength() {
							return parameter.schema().minLength();
						}

						@Override
						public String pattern() {
							return parameter.schema().pattern();
						}

						@Override
						public int maxProperties() {
							return parameter.schema().maxProperties();
						}

						@Override
						public int minProperties() {
							return parameter.schema().minProperties();
						}

						@Override
						public String[] requiredProperties() {
							return parameter.schema().requiredProperties();
						}

						@Override
						public boolean required() {
							return parameter.schema().required();
						}

						@Override
						public RequiredMode requiredMode() {
							return parameter.schema().requiredMode();
						}

						@Override
						public String description() {
							return parameter.schema().description();
						}

						@Override
						public String format() {
							return parameter.schema().format();
						}

						@Override
						public String ref() {
							return parameter.schema().ref();
						}

						@Override
						public boolean nullable() {
							return parameter.schema().nullable();
						}

						@Override
						public boolean readOnly() {
							return AccessMode.READ_ONLY.equals(parameter.schema().accessMode());
						}

						@Override
						public boolean writeOnly() {
							return AccessMode.WRITE_ONLY.equals(parameter.schema().accessMode());
						}

						@Override
						public AccessMode accessMode() {
							return parameter.schema().accessMode();
						}

						@Override
						public String example() {
							return parameter.schema().example();
						}

						@Override
						public ExternalDocumentation externalDocs() {
							return parameter.schema().externalDocs();
						}

						@Override
						public boolean deprecated() {
							return parameter.schema().deprecated();
						}

						@Override
						public String type() {
							return parameter.schema().type();
						}

						@Override
						public String[] allowableValues() {
							return parameter.schema().allowableValues();
						}

						@Override
						public String defaultValue() {
							return parameter.schema().defaultValue();
						}

						@Override
						public String discriminatorProperty() {
							return parameter.schema().discriminatorProperty();
						}

						@Override
						public DiscriminatorMapping[] discriminatorMapping() {
							return parameter.schema().discriminatorMapping();
						}

						@Override
						public boolean hidden() {
							return parameter.schema().hidden();
						}

						@Override
						public boolean enumAsRef() {
							return parameter.schema().enumAsRef();
						}

						@Override
						public Class<?>[] subTypes() {
							return parameter.schema().subTypes();
						}

						@Override
						public Extension[] extensions() {
							return parameter.schema().extensions();
						}

						@Override
						public AdditionalPropertiesValue additionalProperties() {
							return parameter.schema().additionalProperties();
						}
					};
				}

				@Override
				public ArraySchema array() {
					ArraySchema arraySchema = parameter.array();
					return new ArraySchema() {
						@Override
						public Class<? extends Annotation> annotationType() {
							return arraySchema.annotationType();
						}

						@Override
						public Schema schema() {
							return arraySchema.schema();
						}

						@Override
						public Schema arraySchema() {
							Schema schema = arraySchema.arraySchema();
							return new Schema() {

								@Override
								public Class<? extends Annotation> annotationType() {
									return schema.annotationType();
								}

								@Override
								public Class<?> implementation() {
									return schema.implementation();
								}

								@Override
								public Class<?> not() {
									return schema.not();
								}

								@Override
								public Class<?>[] oneOf() {
									return schema.oneOf();
								}

								@Override
								public Class<?>[] anyOf() {
									return schema.anyOf();
								}

								@Override
								public Class<?>[] allOf() {
									return schema.allOf();
								}

								@Override
								public String name() {
									return schema.name();
								}

								@Override
								public String title() {
									return schema.title();
								}

								@Override
								public double multipleOf() {
									return schema.multipleOf();
								}

								@Override
								public String maximum() {
									return schema.maximum();
								}

								@Override
								public boolean exclusiveMaximum() {
									return schema.exclusiveMaximum();
								}

								@Override
								public String minimum() {
									return schema.minimum();
								}

								@Override
								public boolean exclusiveMinimum() {
									return schema.exclusiveMinimum();
								}

								@Override
								public int maxLength() {
									return schema.maxLength();
								}

								@Override
								public int minLength() {
									return schema.minLength();
								}

								@Override
								public String pattern() {
									return schema.pattern();
								}

								@Override
								public int maxProperties() {
									return schema.maxProperties();
								}

								@Override
								public int minProperties() {
									return schema.minProperties();
								}

								@Override
								public String[] requiredProperties() {
									return schema.requiredProperties();
								}

								@Override
								public boolean required() {
									return schema.required();
								}

								@Override
								public RequiredMode requiredMode() {
									return schema.requiredMode();
								}

								@Override
								public String description() {
									return schema.description();
								}

								@Override
								public String format() {
									return schema.format();
								}

								@Override
								public String ref() {
									return schema.ref();
								}

								@Override
								public boolean nullable() {
									return schema.nullable();
								}

								@Override
								public boolean readOnly() {
									return AccessMode.READ_ONLY.equals(schema.accessMode());
								}

								@Override
								public boolean writeOnly() {
									return AccessMode.WRITE_ONLY.equals(schema.accessMode());
								}

								@Override
								public AccessMode accessMode() {
									return schema.accessMode();
								}

								@Override
								public String example() {
									return schema.example();
								}

								@Override
								public ExternalDocumentation externalDocs() {
									return schema.externalDocs();
								}

								@Override
								public boolean deprecated() {
									return schema.deprecated();
								}

								@Override
								public String type() {
									return schema.type();
								}

								@Override
								public String[] allowableValues() {
									return schema.allowableValues();
								}

								@Override
								public String defaultValue() {
									return schema.defaultValue();
								}

								@Override
								public String discriminatorProperty() {
									return schema.discriminatorProperty();
								}

								@Override
								public DiscriminatorMapping[] discriminatorMapping() {
									return schema.discriminatorMapping();
								}

								@Override
								public boolean hidden() {
									return schema.hidden();
								}

								@Override
								public boolean enumAsRef() {
									return schema.enumAsRef();
								}

								@Override
								public Class<?>[] subTypes() {
									return schema.subTypes();
								}

								@Override
								public Extension[] extensions() {
									return schema.extensions();
								}

								@Override
								public AdditionalPropertiesValue additionalProperties() {
									return schema.additionalProperties();
								}
							};
						}

						@Override
						public int maxItems() {
							return arraySchema.maxItems();
						}

						@Override
						public int minItems() {
							return arraySchema.minItems();
						}

						@Override
						public boolean uniqueItems() {
							return arraySchema.uniqueItems();
						}

						@Override
						public Extension[] extensions() {
							return arraySchema.extensions();
						}
					};
				}

				@Override
				public Content[] content() {
					return parameter.content();
				}

				@Override
				public boolean hidden() {
					return parameter.hidden();
				}

				@Override
				public ExampleObject[] examples() {
					return parameter.examples();
				}

				@Override
				public String example() {
					return parameter.example();
				}

				@Override
				public Extension[] extensions() {
					return parameter.extensions();
				}

				@Override
				public String ref() {
					return parameter.ref();
				}
			};
			return Optional.of(parameterNew);
		}
		catch (NoSuchFieldException e) {
			log.warn(e.getMessage());
			return Optional.empty();
		}
	}

	/**
	 * Gets name.
	 * @param parameterName the parameter name
	 * @param pageable the pageable annotation to customer request param
	 * @return the name
	 */
	private String getName(String parameterName, Pageable pageable) {
		String name = null;
		switch (parameterName) {
			case "size":
				name = pageable.sizeParameterName();
				break;
			case "sort":
				name = pageable.sortParameterName();
				break;
			case "page":
				name = pageable.pageParameterName();
				break;
			default:
				// Do nothing here
				break;
		}
		return name;
	}

	/**
	 * Gets description.
	 * @param parameterName the parameter name
	 * @param originalDescription the original description
	 * @param pageable the pageable annotation to customer request param
	 * @return the description
	 */
	private String getDescription(String parameterName, String originalDescription, Pageable pageable) {
		if ("size".equals(parameterName)) {
			return originalDescription.replace("100", String.valueOf(pageable.maxPageSize()));
		}
		return originalDescription;
	}

}
