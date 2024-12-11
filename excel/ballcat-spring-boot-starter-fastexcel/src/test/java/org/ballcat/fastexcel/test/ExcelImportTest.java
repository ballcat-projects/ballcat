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
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.idev.excel.FastExcel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.ballcat.fastexcel.application.DemoData;
import org.ballcat.fastexcel.application.ExcelTestApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Excel 导入测试类
 *
 * @author Hccake
 */
@Slf4j
@SpringBootTest(classes = ExcelTestApplication.class)
@AutoConfigureMockMvc
class ExcelImportTest {

	@Autowired
	MockMvc mockMvc;

	ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void simpleTest() throws Exception {
		// 写入一个 excel 文件到 multipartFile
		List<DemoData> dataList = new ArrayList<>();
		for (int i = 0; i < 2; i++) {
			DemoData data = new DemoData();
			data.setUsername("username" + i);
			data.setPassword("password" + i);
			dataList.add(data);
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		FastExcel.write(bos, DemoData.class).sheet().doWrite(dataList);
		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		MockMultipartFile multipartFile = new MockMultipartFile("file", bis);

		MvcResult mvcResult = this.mockMvc
			.perform(MockMvcRequestBuilders.multipart("/import/simple")
				.file(multipartFile)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		String contentAsString = mvcResult.getResponse().getContentAsString();
		List<DemoData> demoDataList = this.objectMapper.readValue(contentAsString, new TypeReference<List<DemoData>>() {
		});

		Assertions.assertEquals(2, demoDataList.size());
		Assertions.assertEquals("username0", demoDataList.get(0).getUsername());
		Assertions.assertEquals("password1", demoDataList.get(1).getPassword());
	}

	@Test
	void ignoreEmptyRowTest() throws Exception {

		List<DemoData> dataList = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			if (i == 1) {
				dataList.add(null);
				continue;
			}
			DemoData data = new DemoData();
			data.setUsername("username" + i);
			data.setPassword("password" + i);
			dataList.add(data);
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		FastExcel.write(bos, DemoData.class).sheet().doWrite(dataList);
		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		MockMultipartFile multipartFile = new MockMultipartFile("file", bis);
		MvcResult mvcResult = this.mockMvc
			.perform(MockMvcRequestBuilders.multipart("/import/ignore-empty-row-disabled")
				.file(multipartFile)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		String contentAsString = mvcResult.getResponse().getContentAsString();
		List<DemoData> demoDataList = this.objectMapper.readValue(contentAsString, new TypeReference<List<DemoData>>() {
		});

		Assertions.assertEquals(3, demoDataList.size());
		Assertions.assertEquals("username0", demoDataList.get(0).getUsername());
		Assertions.assertEquals("password2", demoDataList.get(2).getPassword());
		Assertions.assertNull(demoDataList.get(1).getUsername());
		Assertions.assertNull(demoDataList.get(1).getPassword());

		// 忽略空行
		mvcResult = this.mockMvc
			.perform(MockMvcRequestBuilders.multipart("/import/ignore-empty-row-enabled")
				.file(multipartFile)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();
		contentAsString = mvcResult.getResponse().getContentAsString();
		demoDataList = this.objectMapper.readValue(contentAsString, new TypeReference<List<DemoData>>() {
		});

		Assertions.assertEquals(2, demoDataList.size());
		Assertions.assertEquals("username0", demoDataList.get(0).getUsername());
		Assertions.assertEquals("password2", demoDataList.get(1).getPassword());

	}

}
