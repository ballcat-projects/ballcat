<template>
  <div>
    <a-descriptions title="使用说明">
      <a-descriptions-item label="文件名占位" :span="3">
        使用 {} 占位，使用时会替换为实际属性
      </a-descriptions-item>
      <a-descriptions-item label="文件夹合并" :span="3">
        多级文件夹可以合并为一个目录项, 使用 / 或者. 作为分隔符，例如 com.test
      </a-descriptions-item>
      <a-descriptions-item label="模板引擎" :span="3">
        目前只支持 Velocity，具体语法请参看其官网
      </a-descriptions-item>
    </a-descriptions>
    <a-descriptions title="系统提供属性">
      <a-descriptions-item label="currentTime">
        当前系统时间
      </a-descriptions-item>
      <a-descriptions-item label="tableName">
        当前表名
      </a-descriptions-item>
      <a-descriptions-item label="comments">
        当前表备注
      </a-descriptions-item>
      <a-descriptions-item label="classname">
        类名，小驼峰形式，首字母小写
      </a-descriptions-item>
      <a-descriptions-item label="className">
        类名，大驼峰形式，首字母大写
      </a-descriptions-item>
      <a-descriptions-item label="pathName">
        类名，全字母小写
      </a-descriptions-item>
      <a-descriptions-item label="tableAlias">
        表别名，类名各单词首字母小写组合
      </a-descriptions-item>
      <a-descriptions-item label="pk">
        主键的列属性
      </a-descriptions-item>
      <a-descriptions-item label="columns">
        列属性的 List 集合
      </a-descriptions-item>
    </a-descriptions>
    <a-descriptions title="列属性详情">
      <a-descriptions-item label="columnName">
        列名
      </a-descriptions-item>
      <a-descriptions-item label="comments">
        列备注
      </a-descriptions-item>
      <a-descriptions-item label="dataType">
        列数据类型
      </a-descriptions-item>
      <a-descriptions-item label="columnType">
        列类型(数据类型+长度等信息)
      </a-descriptions-item>
      <a-descriptions-item label="attrName">
        列对应属性名，首字母小写
      </a-descriptions-item>
      <a-descriptions-item label="capitalizedAttrName">
        列对应属性名，首字母大写
      </a-descriptions-item>
      <a-descriptions-item label="attrType">
        列对应Java类型
      </a-descriptions-item>
      <a-descriptions-item label="extra">
        列的额外属性，如自增
      </a-descriptions-item>
    </a-descriptions>
    <a-descriptions title="用户填写属性">
      <a-descriptions-item label="tablePrefix">
        表前缀，代码生成时截取此前缀后再生成类名
      </a-descriptions-item>
    </a-descriptions>
    <a-descriptions title="用户自定义属性">
      <template v-for="item in this.properties">
        <a-descriptions-item :label="item.propKey">
          {{ item.remarks ? item.title + '，' + item.remarks : item.title }}
        </a-descriptions-item>
      </template>
    </a-descriptions>
  </div>
</template>

<script>
import { getProperties } from '@/api/gen/templateproperty'

export default {
  name: 'CodeGenTips',
  props: {
    templateGroupId: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      properties: []
    }
  },
  mounted() {
    getProperties(this.templateGroupId).then(res => {
      this.properties = res.data
    })
  }
}
</script>

<style scoped></style>
