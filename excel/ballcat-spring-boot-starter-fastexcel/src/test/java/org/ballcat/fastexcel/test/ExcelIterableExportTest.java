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

package org.ballcat.fastexcel.test;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import cn.idev.excel.ExcelReader;
import cn.idev.excel.FastExcel;
import cn.idev.excel.read.metadata.ReadSheet;
import lombok.extern.slf4j.Slf4j;
import org.ballcat.fastexcel.ResponseExcelAutoConfiguration;
import org.ballcat.fastexcel.application.DemoData;
import org.ballcat.fastexcel.application.ExcelIterableExportTestController;
import org.ballcat.fastexcel.application.ExcelTestApplication;
import org.ballcat.fastexcel.handler.DefaultAnalysisEventListener;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Excel 导出测试类
 *
 * @author Hccake
 */
@Slf4j
@ContextConfiguration(classes = { ExcelTestApplication.class, ResponseExcelAutoConfiguration.class })
@WebMvcTest(ExcelIterableExportTestController.class)
class ExcelIterableExportTest {

	@Autowired
	private MockMvc mockMvc;

	/**
	 * 循环导出测试，测试大数据量分sheet导出.
	 */
	@Test
	void iterableExport() throws Exception {
		// 测试数据：总共2500条数据，每批次500条，每个sheet最多1000条
		// 预期结果：3个sheet，前两个sheet各1000条，最后一个sheet 500条
		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/export/iterable"))
			.andExpect(status().isOk())
			.andReturn();

		ByteArrayInputStream inputStream = new ByteArrayInputStream(mvcResult.getResponse().getContentAsByteArray());

		List<DefaultAnalysisEventListener> listeners = new ArrayList<>();

		// 创建三个 ReadSheet，分别用于读取三个sheet
		ReadSheet[] readSheets = new ReadSheet[3];
		for (int i = 0; i < 3; i++) {
			DefaultAnalysisEventListener listener = new DefaultAnalysisEventListener();
			listeners.add(listener);
			readSheets[i] = FastExcel.readSheet(i).head(DemoData.class).registerReadListener(listener).build();
		}

		try (ExcelReader excelReader = FastExcel.read(inputStream).build()) {
			excelReader.read(readSheets);
		}

		Assertions.assertEquals(1000, listeners.get(0).getList().size());
		Assertions.assertEquals(1000, listeners.get(1).getList().size());
		Assertions.assertEquals(500, listeners.get(2).getList().size());
	}

	@Test
	void pageExport() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/export/iterable/page"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();

		ByteArrayInputStream inputStream = new ByteArrayInputStream(mvcResult.getResponse().getContentAsByteArray());

		List<DefaultAnalysisEventListener> listeners = new ArrayList<>();
		ReadSheet[] readSheets = new ReadSheet[3];
		for (int i = 0; i < 3; i++) {
			DefaultAnalysisEventListener listener = new DefaultAnalysisEventListener();
			listeners.add(listener);
			readSheets[i] = FastExcel.readSheet(i).head(DemoData.class).registerReadListener(listener).build();
		}

		try (ExcelReader excelReader = FastExcel.read(inputStream).build()) {
			excelReader.read(readSheets);
		}

		Assertions.assertEquals(1000, listeners.get(0).getList().size());
		Assertions.assertEquals(1000, listeners.get(1).getList().size());
		Assertions.assertEquals(500, listeners.get(2).getList().size());
	}

	@Test
	void offsetExport() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/export/iterable/offset"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();

		ByteArrayInputStream inputStream = new ByteArrayInputStream(mvcResult.getResponse().getContentAsByteArray());

		List<DefaultAnalysisEventListener> listeners = new ArrayList<>();
		ReadSheet[] readSheets = new ReadSheet[3];
		for (int i = 0; i < 3; i++) {
			DefaultAnalysisEventListener listener = new DefaultAnalysisEventListener();
			listeners.add(listener);
			readSheets[i] = FastExcel.readSheet(i).head(DemoData.class).registerReadListener(listener).build();
		}

		try (ExcelReader excelReader = FastExcel.read(inputStream).build()) {
			excelReader.read(readSheets);
		}

		Assertions.assertEquals(1000, listeners.get(0).getList().size());
		Assertions.assertEquals(1000, listeners.get(1).getList().size());
		Assertions.assertEquals(500, listeners.get(2).getList().size());
	}

}
