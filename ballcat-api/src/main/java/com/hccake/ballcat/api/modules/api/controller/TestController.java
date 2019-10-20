package com.hccake.ballcat.api.modules.api.controller;

import com.hccake.ballcat.common.core.exception.BallCatException;
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
public class TestController {


    @PostMapping("/test")
    public String test(){
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
        throw new BallCatException(9999, "Error Test！");
    }



}
