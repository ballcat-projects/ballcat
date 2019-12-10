package com.hccake.ballcat.admin.test;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysRole;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/17 9:58
 */
@SpringBootTest
public class TestUpdate {




    @Test
    public void test() {

        Wrapper<SysRole> wrapper = Wrappers.query();
        System.out.println(wrapper.getSqlSelect());
        System.out.println(wrapper.getCustomSqlSegment());
        System.out.println(wrapper.getExpression().getSqlSegment());
    }

}
