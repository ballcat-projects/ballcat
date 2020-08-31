package com.hccake.extend.mybatis.plus.mysql.methods;

/**
 * @author lingting 2020/5/27 11:47
 */
public class InsertByBatch extends BaseInsertBatch {

	@Override
	public boolean backFillKey() {
		return true;
	}

	@Override
	protected String getSql() {
		return "<script>insert into %s %s values %s</script>";
	}

	@Override
	protected String getId() {
		return "insertByBatch";
	}

}
