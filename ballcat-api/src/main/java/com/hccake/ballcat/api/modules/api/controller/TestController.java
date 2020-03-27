package com.hccake.ballcat.api.modules.api.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hccake.ballcat.api.modules.api.model.entity.ApiAccessLog;
import com.hccake.ballcat.api.modules.log.service.ApiAccessLogService;
import com.hccake.ballcat.common.core.exception.BusinessException;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/16 11:46
 */
@RequestMapping
@RestController
@RequiredArgsConstructor
public class TestController {
    private final ApiAccessLogService apiAccessLogService;


    @ApiOperation("测试地址")
    @PostMapping("/test")
    public String test(){
        return "Hello word!";
    }


    @GetMapping("/test/{test}")
    public String test(@PathVariable String test){
        return "Hello " + test;
    }


    @GetMapping("/test/page")
    public String page(){

        apiAccessLogService.page(new Page<>(), Wrappers.<ApiAccessLog>query().eq("id", 1));

        apiAccessLogService.page(new Page<>(), Wrappers.<ApiAccessLog>lambdaQuery().eq(ApiAccessLog::getId, 1));

        return "Hello word!";
    }


    @PostMapping("/formdata")
    public String test1(@RequestParam("formdata")String formdata){
        return formdata;
    }


    @PostMapping("/xwww")
    public String test2(@RequestParam("xwww")String xwww){
        return xwww;
    }


    @PostMapping("/raw")
    public TestObj test3(@RequestBody TestObj testObj){

        System.out.println(testObj);

        TestObj test = new TestObj();
        test.setLocalDateTime(LocalDateTime.now());
        test.setLocalDate(LocalDate.now());
        test.setStr("test") ;

        return test;
    }


    @PostMapping("/errortest")
    public String error(){
        throw new BusinessException(9999, "Error Test！");
    }



}
