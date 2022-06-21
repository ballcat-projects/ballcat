package com.ballcat.extend.mybatis.plus;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hccake.extend.mybatis.plus.alias.TableAlias;
import lombok.Data;

/**
 * @author hccake
 */
@Data
@TableAlias("d")
@TableName("tbl_demo")
public class Demo {

	private String name;

	private String age;

}
