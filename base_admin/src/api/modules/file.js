import request from '@/utils/request'

// 上传文件
export async function uploadFile(data) {
  return request.post('/system/file/upload', data)
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
