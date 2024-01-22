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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = { StrategyBeanProcessor.class, AppleFruit.class, OrangeFruit.class })
class StrategyTest {

	@Test
	void test() {
		Fruit appleStrategy = StrategyFactory.getNonNullStrategy(Fruit.class, FruitType.APPLE.name());
		Assertions.assertNotNull(appleStrategy);
		appleStrategy.show();

		Fruit orangeStrategy = StrategyFactory.getNonNullStrategy(Fruit.class, FruitType.ORANGE.name());
		Assertions.assertNotNull(orangeStrategy);
		orangeStrategy.show();
	}

	@Test
	void testNoSuchStrategyException() {
		Assertions.assertThrows(NoSuchStrategyException.class,
				() -> StrategyFactory.getNonNullStrategy(Fruit.class, "Banana"));
	}

}
