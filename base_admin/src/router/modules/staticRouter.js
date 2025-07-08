import Layout from '@/layout/index.vue'
import NotFound from '@/components/error/404.vue'
import NoPermission from '@/components/error/403.vue'
import ServerError from '@/components/error/500.vue'
import IFrame from '@/layout/iframe/index.vue'


// 静态路由
export const staticRouter = [
  {
        path: '/',
        redirect: '/home',
  },
  {
    path: '/login',
    component: () => import('@/views/login/index.vue'),
  },
  // {
  //   name: 'layout',
  //   component: () => import('@/layout/silderLayout/index.vue'),
  //   children: [
  //     {
  //       path: '/home',
  //       name: 'home',
  //       component: () => import('@/views/dashboard/index.vue'),
  //     },
  //   ],
  // },
]

// 错误路由
export const errorRoute = [
  {
    path: '/404',
    name: '404',
    component: NotFound,
  },
  {
    path: '/403',
    name: '403',
    component: NoPermission,
  },
  {
    path: '/500',
    name: '500',
    component: ServerError,
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'error',
    component: NotFound,
  },
]

//根路由
export const rootRoute = {
  path: '/',
  name: 'root',
  component: Layout,
  children: []
}

// 内链路由
export const iframeRoute = {
  path: '/iframe',
  name: 'iframe',
  component: IFrame,
}
