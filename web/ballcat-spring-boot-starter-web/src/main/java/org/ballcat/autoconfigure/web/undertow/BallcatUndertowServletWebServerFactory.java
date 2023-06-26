package org.ballcat.autoconfigure.web.undertow;

import io.undertow.server.DefaultByteBufferPool;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;

/**
 * @author lingting 2023-06-26 19:22
 */
public class BallcatUndertowServletWebServerFactory
		implements WebServerFactoryCustomizer<UndertowServletWebServerFactory> {

	@Override
	public void customize(UndertowServletWebServerFactory factory) {
		DefaultByteBufferPool buffers = new DefaultByteBufferPool(false, 2048, -1, 24, 0);

		WebSocketDeploymentInfo webSocketDeploymentInfo = new WebSocketDeploymentInfo();
		webSocketDeploymentInfo.setBuffers(buffers);

		factory.addDeploymentInfoCustomizers(deploymentInfo -> deploymentInfo
			.addServletContextAttribute("io.undertow.websockets.jsr.WebSocketDeploymentInfo", webSocketDeploymentInfo));
	}

}
