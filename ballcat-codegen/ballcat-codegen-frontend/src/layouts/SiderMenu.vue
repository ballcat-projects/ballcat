<template>
  <div style="width: 208px">
    <a-menu :selected-keys="selectedKeys" :open-keys.sync="openKeys" mode="inline" theme="dark">
      <template v-for="item in menuData">
        <a-menu-item v-if="!item.children" :key="item.path" @click="$router.push({ path: item.path })">
          <a-icon v-if="item.meta.icon" :type="item.meta.icon" />
          <span>{{ item.meta.title }}</span>
        </a-menu-item>
        <sub-menu v-else :key="item.path" :menu-info="item" />
      </template>
    </a-menu>
  </div>
</template>

<script>
import SubMenu from './SubMenu'

export default {
  name: 'SiderMenu',
  components: {
    'sub-menu': SubMenu
  },
  watch: {
    '$route.path': function(val) {
      this.selectedKeys = this.selectedKeysMap[val]
      this.openKeys = this.openKeysMap[val]
    }
  },
  data() {
    this.selectedKeysMap = {}
    this.openKeysMap = {}
    const menuData = this.getMenuData(this.$router.options.routes[0].children)
    return {
      selectedKeys: this.selectedKeysMap[this.$route.path],
      openKeys: this.openKeysMap[this.$route.path],
      menuData
    }
  },
  methods: {
    getMenuData(routes = [], parentKeys = [], selectedKey) {
      const menuData = []
      routes.forEach(item => {
        if (!item.hiddenInMenu) {
          const newItem = { ...item }
          delete newItem.children
          this.openKeysMap[item.path] = parentKeys
          this.selectedKeysMap[item.path] = [selectedKey || item.path]
          if (item.children && !item.hideChildrenInMenu) {
            newItem.children = this.getMenuData(item.children, [...parentKeys, item.path])
          }
          menuData.push(newItem)
        }
      })
      return menuData
    }
  }
}
</script>
