import axios from 'axios';
import {useAuthStore} from '@/stores/auth'
import router from '@/router'
import {LOGIN_PATH} from '@/config'


// 默认配置
const DEFAULT_CONFIG = {
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
};

const isPrintLog = false;

// 创建 axios 实例
const instance = axios.create(DEFAULT_CONFIG);

// 获取token
const getToken = () => useAuthStore().token;

// 清除token
const clearToken = () => useAuthStore().clearAuth();

//获取token名称
const getTokenName = () => useAuthStore().tokenName

const generateRequestId = () => 
  Math.random().toString(36).substring(2) + Date.now().toString(36);

const handleUnauthorized = () => {
  clearToken();
  console.log('用户未授权，请重新登录');
  router.push(LOGIN_PATH);
};

// 错误处理映射
const ERROR_MESSAGES = {
  400: '请求参数错误',
  401: '未授权访问',
  403: '权限不足',
  404: '请求的资源不存在',
  429: '请求过于频繁，请稍后重试',
  500: '服务器内部错误',
  502: '网关错误',
  503: '服务暂不可用',
};

// 处理响应错误
const handleResponseError = (error) => {
  let errorMessage = '网络错误，请稍后重试';

  if (error.response) {
    const { status, data } = error.response;
    errorMessage = ERROR_MESSAGES[status] || data?.message || `请求失败 (${status})`;
    
    if (status === 401) {
      handleUnauthorized();
    }
  } else if (error.request) {
    errorMessage = error.code === 'ECONNABORTED' 
      ? '请求超时，请检查网络连接' 
      : '网络连接失败，请检查网络设置';
  }

  return Promise.reject(new Error(errorMessage));
};

// 请求拦截器
instance.interceptors.request.use(
  (config) => {
    // 添加认证token
    const token = getToken();
    if (token) {
      config.headers[getTokenName()] =`Bearer ${token}`;
    }

    // 添加请求ID
    config.headers['X-Request-ID'] = generateRequestId();

    // GET请求添加时间戳防缓存
    if (config.method === 'get') {
      config.params = { ...config.params, _t: Date.now() };
    }

    if (isPrintLog) {
      console.log(`🚀 请求发送: ${config.method?.toUpperCase()} ${config.url}`, config);
    }

    return config;
  },
  (error) => {
    console.error('❌ 请求错误:', error);
    return Promise.reject(error);
  }
);

// 响应拦截器
instance.interceptors.response.use(
  (response) => {
    if (isPrintLog) {
      console.log(`✅ 响应接收: ${response.config.url}`, response.data);
    }

    if (response.config.responseType === 'blob') {
			if (response.status === 200) {
				return response
			} else {
				message.warning('文件下载失败或此文件不存在')
				return
			}
		}
    
    const { code, msg, data } = response.data;
    
    if (code === 200 || code === 0) {
      return data;
    } else if (code === 401) {
      handleUnauthorized();
      return Promise.reject(new Error(msg || '未授权访问'));
    } else {
      return Promise.reject(new Error(msg || '请求失败'));
    }
  },
  handleResponseError
);

// 基础请求方法
export const get = (url, config = {}) => instance.get(url, config);

export const post = (url, data = {}, config = {}) => instance.post(url, data, config);

export const put = (url, data = {}, config = {}) => instance.put(url, data, config);

export const del = (url, config = {}) => instance.delete(url, config);

export const batchDel = (url, data = {}, config = {}) => instance.delete(url, { data, ...config });

export const patch = (url, data = {}, config = {}) => instance.patch(url, data, config);

// 文件上传
export const upload = (url, file, config = {}, onProgress) => {
  const formData = file instanceof FormData ? file : new FormData();
  if (file instanceof File) {
    formData.append('file', file);
  }

  return instance.post(url, formData, {
    ...config,
    headers: {
      'Content-Type': 'multipart/form-data',
      ...config.headers,
    },
    onUploadProgress: onProgress,
  });
};

// 文件下载
export const download = (url, filename, config = {},params = {}) => {
  return instance.get(url, {
    ...config,
    params,
    responseType: 'blob',
  }).then((response) => {
    const blob = new Blob([response.data]);
    const downloadUrl = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = downloadUrl;
    link.download = filename || 'download';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    window.URL.revokeObjectURL(downloadUrl);
  });
};

// 批量请求
export const all = (requests) => axios.all(requests);

// 请求重试
export const retry = (fn, retries = 3, delay = 1000) => {
  return new Promise((resolve, reject) => {
    const attempt = (count) => {
      fn()
        .then(resolve)
        .catch((error) => {
          if (count > 0) {
            console.log(`请求失败，剩余重试次数: ${count}`);
            setTimeout(() => attempt(count - 1), delay);
          } else {
            reject(error);
          }
        });
    };
    attempt(retries);
  });
};

// 配置相关
export const setAuthToken = (token, storage = 'localStorage') => {
  if (storage === 'localStorage') {
    localStorage.setItem('access_token', token);
  } else {
    sessionStorage.setItem('access_token', token);
  }
};

export const setDefaultConfig = (config) => {
  Object.assign(instance.defaults, config);
};

export const getInstance = () => instance;

// 创建新实例
export const createInstance = (config = {}) => {
  return axios.create({ ...DEFAULT_CONFIG, ...config });
};

// 默认导出
const request = {
  get,
  post,
  put,
  delete: del,
  batchDel,
  patch,
  upload,
  download,
  all,
  retry,
  setAuthToken,
  setDefaultConfig,
  getInstance,
  createInstance,
};

export default request;