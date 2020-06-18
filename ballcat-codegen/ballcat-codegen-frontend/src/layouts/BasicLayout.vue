<template>
  <a-layout id="components-layout-side" style="min-height: 100vh">
    <a-layout-sider :trigger="null" v-model="collapsed" collapsible width="256px">
      <div class="logo">BallCat Code Generator</div>
      <SiderMenu />
    </a-layout-sider>
    <a-layout>
      <a-layout-header style="background: #fff; padding: 0">
        <a-icon class="trigger" :type="collapsed ? 'menu-unfold' : 'menu-fold'" @click="collapsed = !collapsed" />
        <Header />
      </a-layout-header>
      <a-layout-content style="margin: 16px">
        <router-view />
      </a-layout-content>
      <a-layout-footer style="text-align: center">
        <Footer />
      </a-layout-footer>
    </a-layout>
  </a-layout>
</template>
<script>
import Header from './Header'
import Footer from './Footer'
import SiderMenu from './SiderMenu'

export default {
  name: 'BasicLayout',
  components: { Header, Footer, SiderMenu },
  data() {
    return {
      collapsed: false,
    }
  },
}
</script>

<style lang="less">
@import url('../less/global.less');

#components-layout-side .logo {
  color: #eeeeee;
  line-height: 64px;
  text-align: center;
  overflow: hidden;
}

#components-layout-side .trigger {
  font-size: 18px;
  line-height: 64px;
  padding: 0 24px;
  cursor: pointer;
  transition: color 0.3s;
}

#components-layout-side .trigger:hover {
  color: #1890ff;
}
</style>
