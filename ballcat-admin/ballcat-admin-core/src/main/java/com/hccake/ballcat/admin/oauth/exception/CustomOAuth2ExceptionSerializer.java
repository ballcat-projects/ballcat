package com.hccake.ballcat.admin.oauth.exception;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.hccake.ballcat.common.core.result.R;
import com.hccake.ballcat.common.core.result.ResultStatus;

import java.io.IOException;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/29 10:33
 */
public class CustomOAuth2ExceptionSerializer extends StdSerializer<CustomOAuth2Exception> {

    public CustomOAuth2ExceptionSerializer() {
        super(CustomOAuth2Exception.class);
    }

    @Override
    public void serialize(CustomOAuth2Exception e, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeObject(R.failed(ResultStatus.UNAUTHORIZED, e.getMessage()));
    }
}
