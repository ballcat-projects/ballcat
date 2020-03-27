package com.your.packages.admin.modules;

import com.hccake.simpleredis.core.annotation.CacheDel;
import com.hccake.simpleredis.core.annotation.CachePut;
import com.hccake.simpleredis.core.annotation.Cached;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/3/25 23:45
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/public/cache-test")
public class CacheTestController {
    private final StringRedisTemplate redisTemplate;


    @GetMapping("/cachedTestKey1")
    @Cached(key = "testKey1")
    public String cachedTestKey1(){
        return "testKey1 add:" + System.currentTimeMillis();
    }

    @GetMapping("/putTestKey1")
    @CachePut(key = "testKey1")
    public String putTestKey1(){
        return "testKey1 update:" + System.currentTimeMillis();
    }


    @GetMapping("/delTestKey1")
    @CacheDel(key = "testKey1")
    public void delTestKey1(){
        System.out.println("testKey1 del");
    }


    @GetMapping("/cachedTestKey2")
    public String cachedTestKey2(){
        redisTemplate.opsForValue().set("testKey2", "1");
        return "testKey2 add success";
    }

}
