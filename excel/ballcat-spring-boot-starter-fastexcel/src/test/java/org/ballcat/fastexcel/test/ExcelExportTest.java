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
import java.util.List;

import cn.idev.excel.ExcelReader;
import cn.idev.excel.FastExcel;
import cn.idev.excel.read.metadata.ReadSheet;
import lombok.extern.slf4j.Slf4j;
import org.ballcat.fastexcel.ResponseExcelAutoConfiguration;
import org.ballcat.fastexcel.application.DemoData;
import org.ballcat.fastexcel.application.ExcelExportTestController;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Excel 导出测试类
 *
 * @author Hccake
 */
@Slf4j
@ContextConfiguration(classes = { ExcelTestApplication.class, ResponseExcelAutoConfiguration.class })
@WebMvcTest(ExcelExportTestController.class)
class ExcelExportTest {

	@Autowired
	private MockMvc mockMvc;

	/**
	 * 简单的导出测试.
	 */
	@Test
	void simpleExport() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/export/simple"))
			.andExpect(status().isOk())
			.andReturn();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(mvcResult.getResponse().getContentAsByteArray());
		DefaultAnalysisEventListener readListener = new DefaultAnalysisEventListener();
		FastExcel.read(inputStream, DemoData.class, readListener).sheet().doRead();

		List<Object> list = readListener.getList();
		Assertions.assertEquals(10, list.size());

		Object o = list.get(9);
		Assertions.assertInstanceOf(DemoData.class, o);

		DemoData demoData = (DemoData) o;
		Assertions.assertEquals("username9", demoData.getUsername());
		Assertions.assertEquals("password9", demoData.getPassword());

	}

	/**
	 * 导出脱敏测试.
	 */
	@Test
	void desensitizationExport() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/export/desensitization"))
			.andExpect(status().isOk())
			.andReturn();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(mvcResult.getResponse().getContentAsByteArray());
		DefaultAnalysisEventListener readListener = new DefaultAnalysisEventListener();
		FastExcel.read(inputStream, DemoData.class, readListener).sheet().doRead();

		List<Object> list = readListener.getList();
		Assertions.assertEquals(10, list.size());

		Object o = list.get(9);
		Assertions.assertInstanceOf(DemoData.class, o);

		DemoData demoData = (DemoData) o;
		Assertions.assertEquals("username9", demoData.getUsername());
		Assertions.assertEquals("pas****d9", demoData.getPassword());
	}

	/**
	 * 模板导出，写入数据时不写入头信息.
	 */
	@Test
	void templateExportIgnoreHeader() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/export/templateExportIgnoreHeader"))
			.andExpect(status().isOk())
			.andReturn();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(mvcResult.getResponse().getContentAsByteArray());
		DefaultAnalysisEventListener readListener = new DefaultAnalysisEventListener();
		FastExcel.read(inputStream, DemoData.class, readListener).sheet().doRead();

		List<Object> list = readListener.getList();
		Assertions.assertEquals(10, list.size());

		Object o = list.get(9);
		Assertions.assertInstanceOf(DemoData.class, o);

		DemoData demoData = (DemoData) o;
		Assertions.assertEquals("username9", demoData.getUsername());
		Assertions.assertEquals("password9", demoData.getPassword());

	}

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

		// 创建三个 ReadSheet，分别用于读取三个sheet
		DefaultAnalysisEventListener firstSheetListener = new DefaultAnalysisEventListener();
		DefaultAnalysisEventListener secondSheetListener = new DefaultAnalysisEventListener();
		DefaultAnalysisEventListener thirdSheetListener = new DefaultAnalysisEventListener();

		ReadSheet readSheet1 = FastExcel.readSheet(0)
			.head(DemoData.class)
			.registerReadListener(firstSheetListener)
			.build();
		ReadSheet readSheet2 = FastExcel.readSheet(1)
			.head(DemoData.class)
			.registerReadListener(secondSheetListener)
			.build();
		ReadSheet readSheet3 = FastExcel.readSheet(2)
			.head(DemoData.class)
			.registerReadListener(thirdSheetListener)
			.build();

		// 使用 ExcelReader 一次性读取所有sheet
		try (ExcelReader excelReader = FastExcel.read(inputStream).build()) {
			excelReader.read(readSheet1, readSheet2, readSheet3);
		}

		// 验证第一个sheet
		List<Object> firstSheet = firstSheetListener.getList();
		Assertions.assertEquals(1000, firstSheet.size());

		// 验证第二个sheet
		List<Object> secondSheet = secondSheetListener.getList();
		Assertions.assertEquals(1000, secondSheet.size());

		// 验证第三个sheet
		List<Object> thirdSheet = thirdSheetListener.getList();
		Assertions.assertEquals(500, thirdSheet.size());

		// 验证数据正确性
		DemoData firstData = (DemoData) firstSheet.get(0);
		Assertions.assertEquals("username2499", firstData.getUsername());
		Assertions.assertEquals("password2499", firstData.getPassword());

		DemoData lastData = (DemoData) thirdSheet.get(499);
		Assertions.assertEquals("username0", lastData.getUsername());
		Assertions.assertEquals("password0", lastData.getPassword());
	}

}
