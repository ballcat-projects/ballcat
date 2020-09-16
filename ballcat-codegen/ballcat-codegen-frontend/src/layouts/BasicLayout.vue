<template>
  <a-layout id="components-layout-side" style="min-height: 100vh">
    <a-layout-sider :trigger="null" v-model="collapsed" collapsible width="208px" collapsedWidth="48px">
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

  .project-name {
    display: inline-block;
    margin-left: 8px;
    font-weight: 600;
    font-size: 18px;
    line-height: 32px;
    vertical-align: middle;
    animation: fade-in;
    animation-duration: 0.2s;
  }
}

#components-layout-side .trigger {
  font-size: 18px;
  line-height: 48px;
  padding: 0 24px;
  cursor: pointer;
  transition: color 0.3s;
}

#components-layout-side .ant-layout-header {
  height: 48px;
  line-height: 48px;
}

#components-layout-side .trigger:hover {
  color: #1890ff;
}

.ant-layout-footer {
  text-align: center;
  padding: 5px 50px 12px;
}

.ant-menu-inline-collapsed {
  width: 48px;
}
.ant-menu-inline-collapsed > .ant-menu-submenu > .ant-menu-submenu-title {
  padding: 0 16px !important;
}
</style>
