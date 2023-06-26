package org.ballcat.autoconfigure.web.undertow;

import lombok.RequiredArgsConstructor;
import org.ballcat.common.core.thread.AbstractTimer;

import java.io.File;

@RequiredArgsConstructor
public class BallcatUndertowTimer extends AbstractTimer {

	protected final File dir;

	@Override
	protected void process() {
		try {
			if (dir == null || dir.exists()) {
				return;
			}
			dir.mkdirs();
		}
		catch (Exception e) {
			//
		}
	}

}