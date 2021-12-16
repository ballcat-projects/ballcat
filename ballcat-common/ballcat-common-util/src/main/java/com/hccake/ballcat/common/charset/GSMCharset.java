/**
 * Copyright (C) 2011 Twitter, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.hccake.ballcat.common.charset;

/**
 * This class encodes and decodes Java Strings to and from the SMS default alphabet. It
 * also supports the default extension table. The default alphabet and it's extension
 * table is defined in GSM 03.38.
 *
 * @author joelauer
 * @author hccake
 */
public final class GSMCharset {

	private GSMCharset() {
	}

	/**
	 * The extension character uses this character as a precharacter
	 */
	public static final int EXTENDED_ESCAPE = 0x1b;

	/**
	 * Page break (extended table).
	 */
	public static final int PAGE_BREAK = 0x0a;

	public static final char[] CHAR_TABLE = { '@', '\u00a3', '$', '\u00a5', '\u00e8', '\u00e9', '\u00f9', '\u00ec',
			'\u00f2', '\u00c7', '\n', '\u00d8', '\u00f8', '\r', '\u00c5', '\u00e5', '\u0394', '_', '\u03a6', '\u0393',
			'\u039b', '\u03a9', '\u03a0', '\u03a8', '\u03a3', '\u0398', '\u039e', ' ', '\u00c6', '\u00e6', '\u00df',
			'\u00c9', ' ', '!', '"', '#', '\u00a4', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', '0', '1',
			'2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '\u00a1', 'A', 'B', 'C', 'D', 'E',
			'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
			'\u00c4', '\u00d6', '\u00d1', '\u00dc', '\u00a7', '\u00bf', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
			'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '\u00e4', '\u00f6',
			'\u00f1', '\u00fc', '\u00e0', };

	/**
	 * Extended character table. Characters in this table are accessed by the 'escape'
	 * character in the base table. It is important that none of the 'inactive' characters
	 * ever be matchable with a valid base-table character as this breaks the encoding
	 * loop.
	 *
	 * @see #EXTENDED_ESCAPE
	 */
	public static final char[] EXT_CHAR_TABLE = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, '^', 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, '{', '}', 0, 0, 0, 0, 0, '\\', 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, '[', '~', ']', 0, '|', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, '\u20ac', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, };

	/**
	 * Verifies that this charset can represent every character in the Java String (char
	 * sequence).
	 * @param str0 The String to verfiy
	 * @return True if the charset can represent every character in the Java String,
	 * otherwise false.
	 */
	public static boolean canRepresent(CharSequence str0) {
		return need7bitsNum(str0) >= 0;
	}

	/**
	 * Gets the number of 7bits of the string required under GSM-7 encoding
	 * @return Returns -1 if the string cannot be encoded in GSM-7, otherwise returns the
	 * number of 7bits of the string required under GSM-7 encoding
	 */
	public static int need7bitsNum(CharSequence str0) {
		if (str0 == null) {
			return 0;
		}

		int need7bitsNum = 0;
		int len = str0.length();
		for (int i = 0; i < len; i++) {
			// get the char in this string
			char c = str0.charAt(i);
			// a very easy check a-z, A-Z, and 0-9 are always valid
			if (c >= 'A' && c <= 'z') {
				need7bitsNum++;
				continue;
			}
			if (c >= '0' && c <= '9') {
				need7bitsNum++;
				continue;
			}
			// gsm-7 maximum codepoint supported is €
			if (c > '€') {
				return -1;
			}

			// search both charmaps (if char is in either, we're good!)
			boolean found = false;
			for (int j = 0; j < CHAR_TABLE.length; j++) {
				if (c == CHAR_TABLE[j]) {
					need7bitsNum++;
					found = true;
					break;
				}
				else if (c == EXT_CHAR_TABLE[j]) {
					need7bitsNum = need7bitsNum + 2;
					found = true;
					break;
				}
			}
			// if we searched both charmaps and didn't find it, then its bad
			if (!found) {
				return -1;
			}

		}
		return need7bitsNum;
	}

}
