import request from '@/utils/request'

// 获取服务器信息
export const getServerInfo = () => {
  return request.get('/monitor/server')
}

// 获取cpu监控信息
export const getCpuMonitorInfo = () => {
  return request.get('/monitor/server/cpu')
}

// 获取内存监控信息
export const getMemoryMonitorInfo = () => {
  return request.get('/monitor/server/memory')
}
