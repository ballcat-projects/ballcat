package com.hccake.ballcat.common.util.json;

import java.lang.reflect.Type;

/**
 * json 相关 util类需实现本类
 * 
 * @author lingting 2021/2/25 20:43
 */
public interface JsonTool {

	/**
	 * obj -> jsonStr
	 * @param obj obj
	 * @return java.lang.String
	 * @author lingting 2021-02-25 21:00
	 */
	String toJson(Object obj);

	/**
	 * jsonStr -> obj
	 * @param json json str
	 * @param r obj.class
	 * @return T
	 * @author lingting 2021-02-25 21:02
	 */
	<T> T toObj(String json, Class<T> r);

	/**
	 * jsonStr -> obj
	 * @param json json str
	 * @param t (obj.class)type
	 * @return T
	 * @author lingting 2021-02-25 21:02
	 */
	<T> T toObj(String json, Type t);

	/**
	 *
	 * jsonStr -> obj
	 * @param json json str
	 * @param t TypeReference
	 * @return T
	 * @author lingting 2021-02-25 21:49
	 */
	<T> T toObj(String json, TypeReference<T> t);

}
