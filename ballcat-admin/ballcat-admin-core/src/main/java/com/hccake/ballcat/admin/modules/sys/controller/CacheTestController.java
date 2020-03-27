package com.your.packages.admin.modules;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/3/25 23:45
 */
@RestController
@RequestMapping("/public/test")
public class CacheTestController {


    @GetMapping("1")
   // @Cached(key = "testKey1")
    public String test1(){
        return "ceshi1";
    }

    @GetMapping("2")
    //@CachePut(key = "testKey1")
    public String test2(){
        return "ceshi2";
    }


    @GetMapping("3")
   // @CacheDel(key = "testKey1")
    public void test3(){
        System.out.println("test3");
    }

}
