import request from '@/utils/request'

// 获取服务器信息
export const getServerInfo = () => {
  return request.get('/monitor/server')
}



