package com.hccake.ballcat.common.websocket.annotations;

import com.hccake.ballcat.common.websocket.config.WebSocketAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import java.lang.annotation.*;

/**
 * 启用websocket
 * @author Yakir
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@EnableWebSocket
@Import(WebSocketAutoConfiguration.class)
public @interface EnableWebSocket2 {

}
