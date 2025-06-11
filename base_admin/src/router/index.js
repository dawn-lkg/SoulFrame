import { createRouter, createWebHistory } from 'vue-router'
// import { getToken, removeToken } from '@/utils/auth';

const routes = [
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/login/index.vue'),
  },
  {
    path: '/',
    name: 'layout',
    component: () => import('../layout/silderLayout/index.vue'),
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})


// 配置
const WHITE_LIST = ['/login', '/register', '/404', '/403'];

// 工具函数
const startProgress = () => window.NProgress?.start();
const finishProgress = () => window.NProgress?.done();

const setPageTitle = (to) => {
  const title = to.meta?.title;
  if (title) {
    document.title = `${title} - 趣问系统`;
  }
};

const logRouteAccess = (to, from) => {
  const logData = {
    timestamp: new Date().toISOString(),
    fromPath: from.path,
    toPath: to.path,
    userAgent: navigator.userAgent
  };
  console.log('路由访问日志:', logData);
};

const isRateLimited = (to) => {
  const now = Date.now();
  const key = `route_access_${to.path}`;
  const lastAccess = sessionStorage.getItem(key);
  
  if (lastAccess && now - parseInt(lastAccess) < 1000) {
    return true;
  }
  
  sessionStorage.setItem(key, now.toString());
  return false;
};

const hasPermission = (userRoles, routeRoles) => {
  if (!routeRoles || routeRoles.length === 0) return true;
  if (!userRoles || userRoles.length === 0) return false;
  return routeRoles.some(role => userRoles.includes(role));
};

// 主要守卫函数
export const beforeEachGuard = (store) => async (to, from, next) => {
  startProgress();

  try {
    const token = getToken();
    
    if (token) {
      await handleAuthenticatedUser(to, from, next, store);
    } else {
      handleUnauthenticatedUser(to, from, next);
    }
  } catch (error) {
    console.error('路由拦截器错误:', error);
    handleError(error, next, store);
  }
};

export const afterEachGuard = (to, from) => {
  finishProgress();
  setPageTitle(to);
  logRouteAccess(to, from);
};

export const beforeResolveGuard = (to, from, next) => {
  if (to.meta?.requiresAuth && !getToken()) {
    next('/login');
    return;
  }

  if (isRateLimited(to)) {
    console.warn('访问过于频繁，请稍后再试');
    next(false);
    return;
  }

  next();
};

// 处理已认证用户
const handleAuthenticatedUser = async (to, from, next, store) => {
  if (to.path === '/login') {
    next({ path: '/' });
    return;
  }

  const hasUserInfo = store.getters.userName;
  
  if (hasUserInfo) {
    checkPermission(to, from, next, store);
  } else {
    try {
      await store.dispatch('user/getUserInfo');
      const accessRoutes = await store.dispatch('permission/generateRoutes', store.getters.roles);
      
      // Vue Router 4.x 使用 addRoute
      if (router.addRoute) {
        accessRoutes.forEach(route => router.addRoute(route));
      } else {
        // Vue Router 3.x
        router.addRoutes(accessRoutes);
      }
      
      next({ ...to, replace: true });
    } catch (error) {
      console.error('获取用户信息失败:', error);
      await store.dispatch('user/resetToken');
      next(`/login?redirect=${to.path}`);
    }
  }
};

// 处理未认证用户
const handleUnauthenticatedUser = (to, from, next) => {
  if (WHITE_LIST.includes(to.path)) {
    next();
  } else {
    next(`/login?redirect=${to.path}`);
  }
};

// 检查权限
const checkPermission = (to, from, next, store) => {
  const userRoles = store.getters.roles;
  const routeRoles = to.meta?.roles;

  if (hasPermission(userRoles, routeRoles)) {
    next();
  } else {
    next({ path: '/403', replace: true });
  }
};

// 错误处理
const handleError = (error, next, store) => {
  removeToken();
  store.dispatch('user/resetToken');
  next('/login');
};

// React 版本的路由守卫 Hook
export const useRouteGuard = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { userInfo, roles } = useSelector(state => state.user);
  
  const [loading, setLoading] = useState(true);

  const checkAuth = useCallback(async () => {
    setLoading(true);
    
    try {
      const token = getToken();
      
      if (token) {
        if (location.pathname === '/login') {
          navigate('/', { replace: true });
          return;
        }

        if (!userInfo) {
          await dispatch(getUserInfo()).unwrap();
        }

        if (!checkRoutePermission(location.pathname, roles)) {
          navigate('/403', { replace: true });
          return;
        }
      } else if (!WHITE_LIST.includes(location.pathname)) {
        navigate(`/login?redirect=${location.pathname}`, { replace: true });
      }
    } catch (error) {
      console.error('认证检查失败:', error);
      handleAuthError(navigate, dispatch);
    } finally {
      setLoading(false);
    }
  }, [location.pathname, userInfo, roles, navigate, dispatch]);

  useEffect(() => {
    checkAuth();
  }, [checkAuth]);

  useEffect(() => {
    if (!loading) {
      setDocumentTitle(location.pathname);
      logAccess(location);
    }
  }, [location, loading]);

  return { loading };
};

// React 辅助函数
const checkRoutePermission = (pathname, userRoles) => {
  const routePermissions = {
    '/admin': ['admin'],
    '/user/management': ['admin', 'manager'],
    '/reports': ['admin', 'manager', 'viewer'],
  };

  const requiredRoles = routePermissions[pathname];
  return hasPermission(userRoles, requiredRoles);
};

const handleAuthError = (navigate, dispatch) => {
  removeToken();
  dispatch(resetToken());
  navigate('/login', { replace: true });
};

const setDocumentTitle = (pathname) => {
  const titles = {
    '/': '首页',
    '/dashboard': '仪表板',
    '/admin': '管理后台',
    '/login': '登录',
  };
  
  document.title = `${titles[pathname] || '趣问系统'} - 趣问系统`;
};

const logAccess = (location) => {
  console.log('页面访问:', {
    path: location.pathname,
    timestamp: new Date().toISOString(),
  });
};

// 简化的守卫设置函数
export const setupRouterGuards = (router, store) => {
  router.beforeEach(beforeEachGuard(store));
  router.afterEach(afterEachGuard);
  router.beforeResolve(beforeResolveGuard);
};

// 白名单管理
export const addToWhiteList = (routes) => {
  if (Array.isArray(routes)) {
    WHITE_LIST.push(...routes);
  } else {
    WHITE_LIST.push(routes);
  }
};

export const removeFromWhiteList = (route) => {
  const index = WHITE_LIST.indexOf(route);
  if (index > -1) {
    WHITE_LIST.splice(index, 1);
  }
};

export default router

