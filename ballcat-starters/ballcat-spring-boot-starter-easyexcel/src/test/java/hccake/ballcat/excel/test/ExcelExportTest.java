package hccake.ballcat.excel.test;

import com.alibaba.excel.EasyExcel;
import com.hccake.common.excel.ResponseExcelAutoConfiguration;
import com.hccake.common.excel.handler.DefaultAnalysisEventListener;
import hccake.ballcat.excel.application.DemoData;
import hccake.ballcat.excel.application.ExcelExportTestController;
import hccake.ballcat.excel.application.ExcelTestApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.ByteArrayInputStream;
import java.util.List;

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
	 * 简单的导出测试
	 */
	@Test
	void simpleExport() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/export/simple"))
			.andExpect(status().isOk())
			.andReturn();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(mvcResult.getResponse().getContentAsByteArray());
		DefaultAnalysisEventListener readListener = new DefaultAnalysisEventListener();
		EasyExcel.read(inputStream, DemoData.class, readListener).sheet().doRead();

		List<Object> list = readListener.getList();
		Assertions.assertEquals(10, list.size());

		Object o = list.get(9);
		Assertions.assertInstanceOf(DemoData.class, o);

		DemoData demoData = (DemoData) o;
		Assertions.assertEquals("username9", demoData.getUsername());
		Assertions.assertEquals("password9", demoData.getPassword());

	}

	/**
	 * 模板导出，写入数据时不写入头信息
	 */
	@Test
	void templateExportIgnoreHeader() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/export/templateExportIgnoreHeader"))
			.andExpect(status().isOk())
			.andReturn();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(mvcResult.getResponse().getContentAsByteArray());
		DefaultAnalysisEventListener readListener = new DefaultAnalysisEventListener();
		EasyExcel.read(inputStream, DemoData.class, readListener).sheet().doRead();

		List<Object> list = readListener.getList();
		Assertions.assertEquals(10, list.size());

		Object o = list.get(9);
		Assertions.assertInstanceOf(DemoData.class, o);

		DemoData demoData = (DemoData) o;
		Assertions.assertEquals("username9", demoData.getUsername());
		Assertions.assertEquals("password9", demoData.getPassword());

	}

}
