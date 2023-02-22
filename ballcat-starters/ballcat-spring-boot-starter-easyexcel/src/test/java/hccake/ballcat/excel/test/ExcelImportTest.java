package hccake.ballcat.excel.test;

import com.alibaba.excel.EasyExcel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hccake.ballcat.excel.application.DemoData;
import hccake.ballcat.excel.application.ExcelTestApplication;
import lombok.extern.slf4j.Slf4j;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

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
		EasyExcel.write(bos, DemoData.class).sheet().doWrite(dataList);
		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		MockMultipartFile multipartFile = new MockMultipartFile("file", bis);

		MvcResult mvcResult = mockMvc
			.perform(MockMvcRequestBuilders.multipart("/import/simple")
				.file(multipartFile)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		String contentAsString = mvcResult.getResponse().getContentAsString();
		List<DemoData> demoDataList = objectMapper.readValue(contentAsString, new TypeReference<List<DemoData>>() {
		});

		Assertions.assertEquals(2, demoDataList.size());
		Assertions.assertEquals("username0", demoDataList.get(0).getUsername());
		Assertions.assertEquals("password1", demoDataList.get(1).getPassword());
	}

}
