package hccake.ballcat.excel.application;

import com.hccake.common.excel.annotation.RequestExcel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Hccake
 */
@RestController
@RequestMapping("/import")
public class ExcelImportTestController {

	@PostMapping(value = "/simple")
	public List<DemoData> simple(@RequestExcel List<DemoData> list) {
		return list;
	}

}
