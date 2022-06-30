package com.hccake.ballcat.extend.openapi.pageable;

import com.fasterxml.jackson.databind.JavaType;
import com.hccake.ballcat.common.model.domain.PageParam;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.Schema;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.providers.ObjectMapperProvider;

import java.util.Iterator;

/**
 * The PageParam Type models converter.
 *
 * @author hccake
 */
public class PageParamOpenAPIConverter implements ModelConverter {

	/**
	 * The constant PAGEABLE_TO_REPLACE.
	 */
	private static final String PAGE_PARAM_TO_REPLACE = "com.hccake.ballcat.common.model.domain.PageParam";

	/**
	 * The constant PAGE_PARAM.
	 */
	private static final AnnotatedType PAGE_PARAM = new AnnotatedType(PageParam.class).resolveAsRef(true);

	/**
	 * The Spring doc object mapper.
	 */
	private final ObjectMapperProvider springDocObjectMapper;

	/**
	 * Instantiates a new Pageable open api converter.
	 * @param springDocObjectMapper the spring doc object mapper
	 */
	public PageParamOpenAPIConverter(ObjectMapperProvider springDocObjectMapper) {
		this.springDocObjectMapper = springDocObjectMapper;
	}

	/**
	 * Resolve schema.
	 * @param type the type
	 * @param context the context
	 * @param chain the chain
	 * @return the schema
	 */
	@Override
	public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
		JavaType javaType = springDocObjectMapper.jsonMapper().constructType(type.getType());
		if (javaType != null) {
			Class<?> cls = javaType.getRawClass();
			if (PAGE_PARAM_TO_REPLACE.equals(cls.getCanonicalName())) {
				if (!type.isSchemaProperty()) {
					type = PAGE_PARAM;
				}
				else {
					type.name(cls.getSimpleName() + StringUtils.capitalize(type.getParent().getType()));
				}
			}
		}
		return (chain.hasNext()) ? chain.next().resolve(type, context, chain) : null;
	}

}