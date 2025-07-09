import request from "@/utils/request"

// 获取分页用户列表
export async function getUserPage(params){
    return request.get('/system/user/page', { params })
}


// 获取用户列表
export async function getUserList(params){
    return request.get('/system/user/list', { params })
}


// 获取用户详情
export async function getUserDetail(userId){
    return request.get(`/system/user/${userId}`)
}


// 新增用户
export async function addUser(data){
    return request.post('/system/user', data)
}


// 修改用户
export async function updateUser(data){
    return request.put('/system/user', data)
}


// 删除用户
export async function deleteUser(userId){
    return request.delete(`/system/user/${userId}`)
}

// 批量删除用户
export async function batchDeleteUser(userIds){
    return request.batchDel(`/system/user/batch`, userIds)
}

// 重置密码
export async function resetPassword(userId){
    return request.put(`/system/user/resetPassword/${userId}`)
}

// 检查用户名是否存在
export async function checkUsername(username){
    return request.get(`/system/user/checkUsername/${username}`)
}





