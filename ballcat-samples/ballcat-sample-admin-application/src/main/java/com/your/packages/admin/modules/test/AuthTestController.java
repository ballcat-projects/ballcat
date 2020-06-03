package com.your.packages.admin.modules.test;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/27 22:29
 */
@RestController
@RequestMapping("auth-test")
public class AuthTestController {


    @PreAuthorize("hasRole('2')")
    @RequestMapping("role1")
    public String testRole1(){
        return "role1 request success!";
    }


    @PreAuthorize("hasRole('ROLE_1')")
    @RequestMapping("role2")
    public String testRole2(){
        return "role2 request success!";
    }


    @PreAuthorize("hasRole('ROLE_3')")
    @RequestMapping("role3")
    public String testRole3(){
        return "role3 request success!";
    }



    @PreAuthorize("hasPermission('edit')")
    @RequestMapping("permission2")
    public String testPermission2(){
        return "permission1 request success!";
    }

    @PreAuthorize("hasPermission(targetObject, 'edit')")
    @RequestMapping("permission1")
    public String testPermission1(){
        return "permission1 request success!";
    }


    @RequestMapping("permission3")
    public String testPermission3(){
        return "permission3 request success!";
    }



}
