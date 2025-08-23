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

package org.ballcat.fastexcel.fill;

import java.util.List;

/**
 * Provides fill entries for Excel template rendering.
 * <p>
 * Implementations supply dynamic data regions via {@link FillEntry}.
 * </p>
 *
 * @author Hccake
 * @since 2.0.0
 * @see FillEntry
 */
public interface FillDataSupplier {

	/**
	 * Provides the fill entries to be applied to the Excel template.
	 * <p>
	 * This method is invoked during the fill phase of export. Each returned
	 * {@link FillEntry} corresponds to a data region in the template.
	 * </p>
	 * @return a list of fill entries; never {@code null} — return empty list if no data
	 */
	List<FillEntry> fillEntries();

}