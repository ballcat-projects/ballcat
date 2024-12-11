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

package org.ballcat.fastexcel.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ballcat.fastexcel.annotation.ResponseExcel;
import org.ballcat.fastexcel.fill.FillDataSupplier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Hccake
 */
@RestController
@RequestMapping("fill")
public class ExcelFillTestController {

	@ResponseExcel(name = "simple-fill", fill = true, template = "fill-template.xlsx")
	@GetMapping("/simple")
	public List<DemoData> simple() {
		List<DemoData> dataList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			DemoData data = new DemoData();
			data.setUsername("username" + i);
			data.setPassword("password" + i);
			dataList.add(data);
		}
		return dataList;
	}

	@ResponseExcel(name = "complex-fill", fill = true, template = "fill-complex.xlsx",
			fillDataSupplier = DemoFillDataSupplier.class)
	@GetMapping("/complex")
	public List<DemoData> complex() {
		List<DemoData> dataList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			DemoData data = new DemoData();
			data.setUsername("username" + i);
			data.setPassword("password" + i);
			dataList.add(data);
		}
		return dataList;
	}

	public static class DemoFillDataSupplier implements FillDataSupplier {

		@Override
		public Object getFillData() {
			Map<String, String> map = new HashMap<>(2);
			map.put("date", "2019年10月9日13:28:28");
			return map;
		}

	}

}
