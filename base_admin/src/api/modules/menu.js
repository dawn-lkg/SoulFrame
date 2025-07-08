import request from '@/utils/request'

// 获取菜单列表(树形结构)
export const getMenuTreeList = () => {
  return request.get('/system/menu/tree')
}

// 获取菜单列表（扁平结构）
export function getList(params) {
    return request.get('/system/menu/list', { params })
}

// 新增菜单
export function addMenu(data) {
    return request.post('/system/menu', data)
}

// 更新菜单
export function updateMenu(data) {
    return request.put(`/system/menu`, data)
}

// 删除菜单
export function removeMenu(id) {
    return request.delete(`/system/menu/${id}`)
}

// 批量删除菜单
export function batchDeleteMenu(ids) {
    return request.batchDel(`/system/menu/batch`, ids)
}

//根据 角色id获取菜单列表
export function getRoleMenuTreeSelect(roleId) {
    return request.get(`/system/menu/role-menu-tree-select/${roleId}`)
}

// 获取菜单树选择项
export function getMenuTreeSelect() {
    return request.get('/system/menu/menu-tree-select')
}







