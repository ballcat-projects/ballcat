package com.hccake.extend.mybatis.plus.methods;

/**
 * @author lingting 2020/5/27 11:47
 */
public class InsertIgnoreByBatch extends BaseInsertBatch {

	protected InsertIgnoreByBatch() {
		super("insertIgnoreByBatch");
	}

	protected InsertIgnoreByBatch(String methodName) {
		super(methodName);
	}

	@Override
	protected String getSql() {
		return "<script>insert ignore into %s %s values %s</script>";
	}

}
