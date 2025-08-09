import router from '@/router'
import {useAuthStore} from '@/stores/auth'
import {exampleRoute, profileRoute, rootRoute} from '@/router/modules/staticRouter.js'

export const dynamicRouter = async () => {
    const authStore = useAuthStore();
    await authStore.initRoutes();
    const routes = authStore.routes;
    routes.push(profileRoute);
    routes.push(exampleRoute);
    rootRoute.children = routes;
    router.addRoute(rootRoute);
    console.log("动态路由添加完成")
}


