<template>
  <a-layout id="components-layout-side" style="min-height: 100vh">
    <a-layout-sider :trigger="null" v-model="collapsed" collapsible width="256px">
      <div class="logo">
        <!--<LogSvg alt="logo" />-->
        <img src="~@/assets/logo.svg" class="logo" alt="logo" style="width:32px;height:auto" />
        <span v-show="!collapsed" class="project-name">Code Generator</span>
      </div>
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
      <a-layout-footer>
        <Footer />
      </a-layout-footer>
    </a-layout>
  </a-layout>
</template>
<script>
import Header from './Header'
import Footer from './Footer'
import SiderMenu from './SiderMenu'
import LogSvg from '@/assets/logo.svg?inline'

export default {
  name: 'BasicLayout',
  components: { Header, Footer, SiderMenu, LogSvg },
  data() {
    return {
      collapsed: false
    }
  }
}
</script>

<style lang="less">
@import url('../less/global.less');

#components-layout-side .logo {
  color: #eeeeee;
  line-height: 64px;
  height: 64px;
  text-align: center;
  overflow: hidden;
  vertical-align: middle;

  .project-name {
    margin-left: 8px;
    vertical-align: middle;
    font-size: 20px;
    font-family: 'Josefin Sans', 'Helvetica', 'Arial', 'Microsoft YaHei', '黑体', '宋体', 'sans-serif';
    font-weight: 600;
  }
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

.ant-layout-footer {
  text-align: center;
  padding: 5px 50px 12px;
}
</style>
