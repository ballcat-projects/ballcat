package hccake.ballcat.excel.application;

import com.hccake.common.excel.annotation.ResponseExcel;
import com.hccake.common.excel.annotation.Sheet;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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

}
