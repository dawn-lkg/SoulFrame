import request from '@/utils/request'


// 分页获取系统配置列表
export function getConfigPage(params) {
  return request.get('/system/config/page', params)
}

// 获取系统配置列表
export function getConfigList(params) {
  return request.get('/system/config/list', params)
}

// 获取系统配置详情
export function getConfigDetail(id) {
  return request.get(`/system/config/${id}`)
}

// 新增系统配置
export function addConfig(data) {
  return request.post('/system/config', data)
}

// 修改系统配置
export function updateConfig(data) {
  return request.put('/system/config', data)
}

// 删除系统配置
export function deleteConfig(ids) {
  return request.delete(`/system/config/${ids}`)
}

// 根据键名获取系统配置
export function getConfigByKey(configKey) {
  return request.get(`/system/config/key/${configKey}`)
}
