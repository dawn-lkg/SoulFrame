import request from '@/utils/request'


// 分页获取文件列表
export async function getFilePage(params) {
  return request.get('/system/file/page', { params })
}

// 上传文件
export async function uploadFile(data) {
  return request.upload('/system/file/upload', data)
}

// 获取文件url
export async function getFileUrl(id) {
  return request.get(`/system/file/url/${id}`)
}

// 批量上传文件
export async function batchUploadFile(data) {
  return request.post('/system/file/batchUpload', data)
}

// 删除文件
export async function deleteFile(id) {
  return request.delete(`/system/file/${id}`)
}

// 批量删除文件
export async function batchDeleteFile(ids) {
  return request.batchDel('/system/file/batch', ids)
}
