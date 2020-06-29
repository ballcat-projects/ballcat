import Vue from 'vue'
import VueRouter from 'vue-router'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

// 捕获异常，否则重复push控制台有异常
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location, onResolve, onReject) {
  if (onResolve || onReject) return originalPush.call(this, location, onResolve, onReject)
  return originalPush.call(this, location).catch(err => err)
}

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    hiddenInMenu: true,
    component: () => import('../layouts/BasicLayout'),
    redirect: '/gen',
    children: [
      {
        path: '/gen',
        redirect: '/gen/codegen',
        meta: { icon: 'setting', title: '代码生成器' },
        component: { render: h => h('router-view') },
        children: [
          {
            path: '/gen/codegen',
            name: 'CodeGen',
            meta: { title: '代码生成器' },
            component: () => import(/* webpackChunkName: "gen" */ '../views/gen/codegen/GeneratePage')
          },
          {
            path: '/gen/template/group',
            name: 'TemplateGroup',
            meta: { title: '模板组管理' },
            component: () => import(/* webpackChunkName: "gen" */ '../views/gen/templategroup/TemplateGroupPage')
          },
          {
            path: '/gen/datasouce',
            name: 'DataSource',
            meta: { title: '数据源配置' },
            component: () => import(/* webpackChunkName: "gen" */ '../views/gen/datasourceconfig/DataSourceConfigPage')
          }
        ]
      },
      {
        path: '404',
        name: 'notFound',
        hiddenInMenu: true,
        component: () => import('../views/404')
      }
    ]
  },
  {
    path: '*',
    name: '404',
    hiddenInMenu: true,
    redirect: '/404'
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {
  NProgress.start()
  next()
})

router.afterEach(() => {
  NProgress.done()
})

export default router
