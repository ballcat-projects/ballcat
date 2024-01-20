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

package org.ballcat.common.core.strategy;

import lombok.extern.slf4j.Slf4j;

/**
 * 苹果
 *
 * @author kevin
 */
@Slf4j(topic = "STRATEGY_APPLE_FRUIT")
@StrategyComponent("APPLE")
public class AppleFruit implements Fruit {

	@Override
	public void show() {
		log.info("THIS IS APPLE...");
	}

}
