/*
 * Copyright 2023-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ballcat.springsecyrity;

import org.ballcat.security.annotation.Authorize;

public class BookServiceImpl implements BookService {

	@Override
	@Authorize("hasPermission('book:read')")
	public String readBook(String bookName) {
		return bookName + " read";
	}

	@Override
	@Authorize("#bookName == '格林童话'")
	public String readSpecified(String bookName) {
		return bookName + " read";
	}

	@Override
	@Authorize("hasPermission('book:edit') AND hasRole('ROLE_admin')")
	public String editBook(String bookName) {
		return bookName + " edited";
	}

	@Override
	@Authorize("hasPermission('book:remove')")
	public String removeBook(String bookName) {
		return bookName + " removed";
	}

}
