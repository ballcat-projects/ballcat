package com.hccake.ballcat.admin.test;

import com.hccake.ballcat.admin.modules.sys.model.converter.SysPermissionConverter;
import com.hccake.ballcat.admin.modules.sys.model.vo.PermissionVO;
import com.hccake.ballcat.admin.modules.sys.model.vo.Router;
import org.junit.Test;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/19 11:42
 */
public class ConvertTest {

    @Test
    public void test(){

        PermissionVO sysPermission = new PermissionVO();
        sysPermission.setIcon("icon");
        sysPermission.setCode("code");
        sysPermission.setParentId(-1);

        Router router = SysPermissionConverter.INSTANCE.toRouter(sysPermission);
        System.out.println(router);

    }
}
