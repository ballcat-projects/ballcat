package com.hccake.ballcat.codegen.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import com.hccake.ballcat.codegen.config.GenConfig;
import com.hccake.ballcat.codegen.entity.ColumnEntity;
import com.hccake.ballcat.codegen.entity.TableEntity;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.File;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成器   工具类
 *
 * @author hccake
 * @date 2018-07-30
 */
@Slf4j
@UtilityClass
public class GenUtils {

	/**
	 * 前端工程名
	 */
	private final String FRONT_PROJECT_NAME = "ballcat-ui-vue";
	/**
	 * 后端工程名
	 */
	private final String BACK_PROJECT_NAME = "ballcat";


	private final String QO_JAVA_VM = "QO.java.vm";
	private final String VO_JAVA_VM = "VO.java.vm";
	private final String ENTITY_JAVA_VM = "Entity.java.vm";
	private final String MAPPER_JAVA_VM = "Mapper.java.vm";
	private final String SERVICE_JAVA_VM = "Service.java.vm";
	private final String SERVICE_IMPL_JAVA_VM = "ServiceImpl.java.vm";
	private final String CONTROLLER_JAVA_VM = "Controller.java.vm";
	private final String MAPPER_XML_VM = "Mapper.xml.vm";
	private final String MENU_SQL_VM = "permission.sql.vm";

	private final String API_JS_VM = "api.js.vm";
	private final String PAGE_VUE_VM = "Page.vue.vm";
	private final String FORM_VUE_VM = "Form.vue.vm";


	private List<String> getTemplates() {
		List<String> templates = new ArrayList<>();
		templates.add("template/QO.java.vm");
		templates.add("template/VO.java.vm");
		templates.add("template/Entity.java.vm");
		templates.add("template/Mapper.java.vm");
		templates.add("template/Mapper.xml.vm");
		templates.add("template/Service.java.vm");
		templates.add("template/ServiceImpl.java.vm");
		templates.add("template/Controller.java.vm");
		templates.add("template/permission.sql.vm");

		templates.add("template/api.js.vm");
		templates.add("template/Page.vue.vm");
		templates.add("template/Form.vue.vm");

		return templates;
	}

	/**
	 * 生成代码
	 */
	@SneakyThrows
	public void generatorCode(GenConfig genConfig, Map<String, String> table,
							  List<Map<String, String>> columns, ZipOutputStream zip) {

		boolean hasBigDecimal = false;
		//表信息
		TableEntity tableEntity = new TableEntity();
		tableEntity.setTableName(table.get("tableName"));
		tableEntity.setComments(table.get("tableComment"));


		String tablePrefix = genConfig.getTablePrefix();;
		//表名转换成Java类名
		String className = tableToJava(tableEntity.getTableName(), tablePrefix);
		tableEntity.setCaseClassName(className);
		tableEntity.setLowerClassName(StringUtils.uncapitalize(className));
		//获取需要在swagger文档中隐藏的属性字段
		Set<String> hiddenColumns = genConfig.getHiddenColumns();
		//列信息
		List<ColumnEntity> columnList = new ArrayList<>();
		for (Map<String, String> column : columns) {
			ColumnEntity columnEntity = new ColumnEntity();
			columnEntity.setColumnName(column.get("columnName"));
			columnEntity.setDataType(column.get("dataType"));
			columnEntity.setComments(column.get("columnComment"));
			columnEntity.setExtra(column.get("extra"));
			columnEntity.setNullable("NO".equals(column.get("isNullable")));
			columnEntity.setColumnType(column.get("columnType"));
			//隐藏不需要的在接口文档中展示的字段
			if(CollUtil.isNotEmpty(hiddenColumns)){
				columnEntity.setHidden(hiddenColumns.contains(column.get("columnName")));
			}

			//列名转换成Java属性名
			String attrName = columnToJava(columnEntity.getColumnName());
			columnEntity.setCaseAttrName(attrName);
			columnEntity.setLowerAttrName(StringUtils.uncapitalize(attrName));

			//列的数据类型，转换成Java类型
			Map<String, String> typeMapping = genConfig.getTypeMapping();
			String attrType = typeMapping.getOrDefault(columnEntity.getDataType(), "unknowType");
			columnEntity.setAttrType(attrType);
			if (!hasBigDecimal && "BigDecimal".equals(attrType)) {
				hasBigDecimal = true;
			}
			//是否主键
			if ("PRI".equalsIgnoreCase(column.get("columnKey")) && tableEntity.getPk() == null) {
				tableEntity.setPk(columnEntity);
			}

			columnList.add(columnEntity);
		}
		tableEntity.setColumns(columnList);

		//没主键，则第一个字段为主键
		if (tableEntity.getPk() == null) {
			tableEntity.setPk(tableEntity.getColumns().get(0));
		}

		//设置velocity资源加载器
		Properties prop = new Properties();
		prop.put("file.resource.loader.class", ClasspathResourceLoader.class.getName());
		Velocity.init(prop);
		//封装模板数据
		Map<String, Object> map = new HashMap<>(16);
		map.put("tableName", tableEntity.getTableName());
		map.put("pk", tableEntity.getPk());
		map.put("className", tableEntity.getCaseClassName());
		map.put("classname", tableEntity.getLowerClassName());
		map.put("tableAlias", prodAlias(tableEntity.getCaseClassName()));
		map.put("pathName", tableEntity.getLowerClassName().toLowerCase());
		map.put("columns", tableEntity.getColumns());
		map.put("hasBigDecimal", hasBigDecimal);
		map.put("datetime", DateUtil.now());
		map.put("comments", tableEntity.getComments());
		map.put("author", genConfig.getAuthor());
		map.put("moduleName", genConfig.getModuleName());
		map.put("package", genConfig.getPackageName());

		VelocityContext context = new VelocityContext(map);

		//获取模板列表
		List<String> templates = getTemplates();
		for (String template : templates) {
			//渲染模板
			StringWriter sw = new StringWriter();
			Template tpl = Velocity.getTemplate(template, CharsetUtil.UTF_8);
			tpl.merge(context, sw);

			//添加到zip
			zip.putNextEntry(new ZipEntry(Objects
					.requireNonNull(getFileName(template, tableEntity.getCaseClassName()
							, map.get("package").toString(), map.get("moduleName").toString()))));
			IoUtil.write(zip, StandardCharsets.UTF_8, false, sw.toString());
			IoUtil.close(sw);
			zip.closeEntry();
		}
	}

	/**
	 * 根据类名生成表别名
	 * @param className 类名
	 * @return 表别名
	 */
	private String prodAlias(String className) {
		StringBuilder sb = new StringBuilder();
		for (char c : className.toCharArray()) {
			if(c >= 'A' && c <= 'Z'){
				sb.append(Character.toLowerCase(c));
			}
		}
		return sb.toString();
	}


	/**
	 * 列名转换成Java属性名
	 */
	public String columnToJava(String columnName) {
		return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
	}

	/**
	 * 表名转换成Java类名
	 */
	private String tableToJava(String tableName, String tablePrefix) {
		if (StringUtils.isNotBlank(tablePrefix)) {
			tableName = tableName.replaceFirst(tablePrefix, "");
		}
		return columnToJava(tableName);
	}

	/**
	 * 获取文件名
	 */
	private String getFileName(String template, String className, String packageName, String moduleName) {
		String packagePath = BACK_PROJECT_NAME + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator;
		if (StringUtils.isNotBlank(packageName)) {
			packagePath += packageName.replace(".", File.separator) + File.separator + moduleName + File.separator;
		}

		if (template.contains(VO_JAVA_VM)) {
			return packagePath + "model" + File.separator +  "vo" + File.separator + className + "VO.java";
		}

		if (template.contains(QO_JAVA_VM)) {
			return packagePath + "model" + File.separator +  "qo" + File.separator + className + "QO.java";
		}

		if (template.contains(ENTITY_JAVA_VM)) {
			return packagePath + "model" + File.separator +  "entity" + File.separator + className + ".java";
		}

		if (template.contains(MAPPER_JAVA_VM)) {
			return packagePath + "mapper" + File.separator + className + "Mapper.java";
		}

		if (template.contains(SERVICE_JAVA_VM)) {
			return packagePath + "service" + File.separator + className + "Service.java";
		}

		if (template.contains(SERVICE_IMPL_JAVA_VM)) {
			return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
		}

		if (template.contains(CONTROLLER_JAVA_VM)) {
			return packagePath + "controller" + File.separator + className + "Controller.java";
		}

		if (template.contains(MAPPER_XML_VM)) {
			return BACK_PROJECT_NAME + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "mapper"
				+ File.separator + moduleName + File.separator + className + "Mapper.xml";
		}

		if (template.contains(MENU_SQL_VM)) {
			return className.toLowerCase() + "_permission.sql";
		}



		if (template.contains(API_JS_VM)) {
			return FRONT_PROJECT_NAME + File.separator + "src" + File.separator + "api" +
					File.separator + moduleName + File.separator +className.toLowerCase() + ".js";
		}

		if (template.contains(PAGE_VUE_VM)) {
			return FRONT_PROJECT_NAME + File.separator + "src" + File.separator + "views" +
					File.separator + moduleName + File.separator + className.toLowerCase() +
					File.separator + className + "Page.vue";
		}

		if (template.contains(FORM_VUE_VM)) {
			return FRONT_PROJECT_NAME + File.separator + "src" + File.separator + "views" +
					File.separator + moduleName + File.separator + className.toLowerCase() +
					File.separator + className + "Form.vue";
		}


		return null;
	}
}
