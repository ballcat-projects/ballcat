package com.hccake.ballcat.auth.filter;

import javax.servlet.Filter;

/**
 * @author hccake
 */
@Deprecated
public class FilterWrapper {

	private final Filter filter;

	public FilterWrapper(Filter filter) {
		this.filter = filter;
	}

	public Filter getFilter() {
		return filter;
	}

}
