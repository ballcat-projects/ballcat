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

import java.util.Collection;

import cn.idev.excel.enums.WriteDirectionEnum;
import cn.idev.excel.write.metadata.fill.FillConfig;
import cn.idev.excel.write.metadata.fill.FillWrapper;
import lombok.Getter;

/**
 * Represents a unit of data and configuration for Excel template filling.
 * <p>
 * Encapsulates:
 * <ul>
 * <li>An optional prefix ({@code {prefix.}}) for list filling</li>
 * <li>The actual data (collection, map, or JavaBean)</li>
 * <li>Fill behavior configuration (direction, new row, etc.)</li>
 * </ul>
 * </p>
 * <p>
 * When a prefix is provided and the data is a {@link Collection}, it is automatically
 * wrapped in a {@link FillWrapper} for list filling. Otherwise, the raw data is used for
 * object filling ({@code {field}}).
 * </p>
 * <p>
 * Example: <pre>{@code
 * // List fill with prefix
 * FillEntry listFill = FillEntry.of("orders", orderList);
 *
 * // Vertical object fill
 * FillEntry objectFill = FillEntry.vertical(supplierInfo);
 *
 * // Custom configuration
 * FillEntry custom = FillEntry.of(userMap)
 *     .withConfig(FillConfig.builder().forceNewRow(true).build());
 * }</pre>
 * </p>
 *
 * @author xm.z
 */
@Getter
public final class FillEntry {

	/**
	 * Optional prefix for list fill (e.g., "items" for {@code {items.}} in template). If
	 * present and data is a collection, it will be wrapped in a {@link FillWrapper}.
	 */
	private final String prefix;

	/**
	 * The data to be filled. Can be:
	 * <ul>
	 * <li>A {@link Collection} (wrapped in {@link FillWrapper} if prefix is set)</li>
	 * <li>A {@link java.util.Map} or JavaBean (for object fill)</li>
	 * </ul>
	 */
	private final Object data;

	/**
	 * Optional fill configuration (direction, style, force new row, etc.). Uses default
	 * settings if {@code null}.
	 */
	private final FillConfig fillConfig;

	private FillEntry(String prefix, Object data, FillConfig fillConfig) {
		this.prefix = prefix;
		this.data = data;
		this.fillConfig = fillConfig;
	}

	// ================== Factory Methods ==================

	/**
	 * Creates a fill entry for object filling (no prefix).
	 * @param data the data (Map, JavaBean, etc.)
	 * @return a new immutable fill entry
	 */
	public static FillEntry of(Object data) {
		return new FillEntry(null, data, null);
	}

	/**
	 * Creates a list fill entry with the given prefix. The data is automatically wrapped
	 * in a {@link FillWrapper}.
	 * @param prefix the prefix (e.g., "orders") for template binding
	 * @param data the collection to fill
	 * @return a new fill entry
	 * @throws IllegalArgumentException if data is null
	 */
	public static FillEntry of(String prefix, Collection<?> data) {
		if (data == null) {
			throw new IllegalArgumentException("Data cannot be null");
		}
		FillWrapper fillWrapper = new FillWrapper(prefix, data);
		return new FillEntry(prefix, fillWrapper, null);
	}

	/**
	 * Creates a fill entry with custom configuration (no prefix).
	 * @param data the data to fill
	 * @param fillConfig fill configuration
	 * @return a new fill entry
	 */
	public static FillEntry of(Object data, FillConfig fillConfig) {
		return new FillEntry(null, data, fillConfig);
	}

	/**
	 * Creates a list fill entry with prefix and configuration.
	 * @param prefix the prefix for template binding
	 * @param data the collection to fill
	 * @param fillConfig fill configuration
	 * @return a new fill entry
	 * @throws IllegalArgumentException if data is null
	 */
	public static FillEntry of(String prefix, Collection<?> data, FillConfig fillConfig) {
		if (data == null) {
			throw new IllegalArgumentException("Data cannot be null");
		}
		Object wrappedData = new FillWrapper(prefix, data);
		return new FillEntry(prefix, wrappedData, fillConfig);
	}

	// ================== Fluent API ==================

	/**
	 * Returns a new entry with the specified configuration.
	 * @param fillConfig the new fill configuration
	 * @return a new fill entry with updated config
	 */
	public FillEntry withConfig(FillConfig fillConfig) {
		return new FillEntry(this.prefix, this.data, fillConfig);
	}

	/**
	 * Returns a new entry with the specified data.
	 * <p>
	 * <b>Warning:</b> If this entry has a prefix, ensure the new data is properly wrapped
	 * (e.g., via {@link FillWrapper}) if it's a collection.
	 * </p>
	 * @param newData the new data
	 * @return a new fill entry with updated data
	 */
	public FillEntry withData(Object newData) {
		return new FillEntry(this.prefix, newData, this.fillConfig);
	}

	// ================== Convenience Builders ==================

	/**
	 * Creates a horizontal list fill entry.
	 * @param prefix the prefix (e.g., "items")
	 * @param data the collection data
	 * @return a new fill entry with horizontal direction
	 */
	public static FillEntry horizontal(String prefix, Collection<?> data) {
		FillConfig config = FillConfig.builder().direction(WriteDirectionEnum.HORIZONTAL).build();
		return of(prefix, data, config);
	}

	/**
	 * Creates a vertical list fill entry.
	 * @param prefix the prefix (e.g., "items")
	 * @param data the collection data
	 * @return a new fill entry with vertical direction
	 */
	public static FillEntry vertical(String prefix, Collection<?> data) {
		FillConfig config = FillConfig.builder().direction(WriteDirectionEnum.VERTICAL).build();
		return of(prefix, data, config);
	}

	/**
	 * Creates a horizontally oriented object fill entry.
	 * @param data the object data (Map, JavaBean)
	 * @return a new fill entry with horizontal direction
	 */
	public static FillEntry horizontal(Object data) {
		FillConfig config = FillConfig.builder().direction(WriteDirectionEnum.HORIZONTAL).build();
		return of(data, config);
	}

	/**
	 * Creates a vertically oriented object fill entry.
	 * @param data the object data (Map, JavaBean)
	 * @return a new fill entry with vertical direction
	 */
	public static FillEntry vertical(Object data) {
		FillConfig config = FillConfig.builder().direction(WriteDirectionEnum.VERTICAL).build();
		return of(data, config);
	}

	/**
	 * Creates a fill entry that forces a new row after filling.
	 * @param data the data to fill
	 * @return a new fill entry with {@code forceNewRow = true}
	 */
	public static FillEntry forceNewRow(Object data) {
		FillConfig config = FillConfig.builder().forceNewRow(true).build();
		return of(data, config);
	}

}
