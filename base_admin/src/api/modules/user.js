import request from "@/utils/request"

// 获取分页用户列表
export async function getUserPage(params){
    try {
        return await request.get('/system/user/page', { params })
    } catch (error) {
        console.log(error)
        throw error
    }
}


// 获取用户列表
export async function getUserList(params){
    try {
        return await request.get('/system/user/list', { params })
    } catch (error) {
        console.log(error)
        throw error
    }
}


// 获取用户详情
export async function getUserDetail(userId){
    try {
        return await request.get(`/system/user/${userId}`)
    } catch (error) {
        console.log(error)
        throw error
    }
}


// 新增用户
export async function addUser(data){
    try {
        return await request.post('/system/user', data)
    } catch (error) {
        console.log(error)
        throw error
    }
}


// 修改用户
export async function updateUser(data){
    try {
        return await request.put('/system/user', data)
    } catch (error) {   
        console.log(error)
        throw error
    }
}


// 删除用户
export async function deleteUser(userId){
    try {
        return await request.delete(`/system/user/${userId}`)
    } catch (error) {
        console.log(error)
        throw error
    }
}

// 批量删除用户
export async function batchDeleteUser(userIds){
    try {
        return await request.batchDel(`/system/user/batch`, userIds)
    } catch (error) {
        console.log(error)
        throw error
    }
}

// 重置密码
export async function resetPassword(userId){
    try {
        return await request.put(`/system/user/resetPassword/${userId}`)
    } catch (error) {
        console.log(error)
        throw error
    }
}

// 检查用户名是否存在
export async function checkUsername(username){
    try {
        return await request.get(`/system/user/checkUsername/${username}`)
    } catch (error) {
        console.log(error)
        throw error
    }
}





