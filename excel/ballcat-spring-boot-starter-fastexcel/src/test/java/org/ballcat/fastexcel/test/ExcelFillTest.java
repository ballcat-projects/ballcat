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

import cn.idev.excel.FastExcel;
import lombok.extern.slf4j.Slf4j;
import org.ballcat.fastexcel.ResponseExcelAutoConfiguration;
import org.ballcat.fastexcel.application.DemoData;
import org.ballcat.fastexcel.application.ExcelFillTestController;
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

/**
 * @author Hccake
 */
@Slf4j
@ContextConfiguration(classes = { ExcelTestApplication.class, ResponseExcelAutoConfiguration.class })
@WebMvcTest(ExcelFillTestController.class)
class ExcelFillTest {

	@Autowired
	private MockMvc mockMvc;

	/**
	 * 简单的导出测试
	 */
	@Test
	void simpleFill() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/fill/simple"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(mvcResult.getResponse().getContentAsByteArray());
		DefaultAnalysisEventListener readListener = new DefaultAnalysisEventListener();
		FastExcel.read(inputStream, DemoData.class, readListener).sheet().doRead();

		List<Object> list = readListener.getList();
		Assertions.assertEquals(10, list.size());

		Object o = list.get(9);
		Assertions.assertInstanceOf(DemoData.class, o);

		DemoData demoData = (DemoData) o;
		Assertions.assertEquals("username9你好", demoData.getUsername());
		Assertions.assertEquals("password9Hello", demoData.getPassword());

	}

	/**
	 * 简单的导出测试
	 */
	@Test
	void complexFill() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/fill/complex"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();
		byte[] contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(contentAsByteArray);

		// ByteArrayInputStream fileInputStream = new
		// ByteArrayInputStream(contentAsByteArray);
		// FileUtils.writeToFile(new File("complex-export.xlsx"), fileInputStream);

		DefaultAnalysisEventListener readListener = new DefaultAnalysisEventListener();
		FastExcel.read(inputStream, DemoData.class, readListener).sheet().headRowNumber(3).doRead();

		List<Object> list = readListener.getList();
		Assertions.assertEquals(10, list.size());

		Object o = list.get(9);
		Assertions.assertInstanceOf(DemoData.class, o);

		DemoData demoData = (DemoData) o;
		Assertions.assertEquals("username9", demoData.getUsername());
		Assertions.assertEquals("password9", demoData.getPassword());
	}

}
