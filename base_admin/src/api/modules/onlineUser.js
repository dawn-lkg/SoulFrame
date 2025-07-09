import request from '@/utils/request'

// 获取在线用户列表
export async function getOnlineUserList(params) {
  return request.get('/system/online/list', { params })
}

// 强退用户
export async function forceLogout(tokenId) {
  return request.delete(`/system/online/${tokenId}`)
}

// 批量强退
export async function batchForceLogout(tokenIds) {
  return request.batchDel('/system/online/batch', tokenIds)
}


