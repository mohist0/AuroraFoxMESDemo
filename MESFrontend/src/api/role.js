import request from "../utils/request";

/* ===========================
   角色（Role）相关接口
   =========================== */

// 查询角色列表（含分页、搜索）
export function apiGetRoleList(params) {
  return request({
    url: "/role",
    method: "get",
    params
  });
}

// 根据 ID 查询角色
export function apiGetRoleById(id) {
  return request({
    url: `/role/${id}`,
    method: "get"
  });
}

// 新增角色
export function apiAddRole(data) {
  return request({
    url: "/role",
    method: "post",
    data
  });
}

// 修改角色（名称/描述）
export function apiUpdateRole(data) {
  return request({
    url: "/role",
    method: "put",
    data
  });
}

// 删除角色
export function apiDeleteRole(id) {
  return request({
    url: `/role/${id}`,
    method: "delete"
  });
}


/* ===========================
   权限（Permission）相关接口
   =========================== */

// 获取所有权限（创建/修改角色时需要）
export function apiGetPermissionList() {
  return request({
    url: "/permission",
    method: "get"
  });
}

// 根据角色 ID 查询该角色拥有的权限
export function apiGetRolePermissions(roleId) {
  return request({
    url: `/role/${roleId}/permissions`,
    method: "get"
  });
}


/* ===========================
   角色权限（Role-Permission）相关接口
   =========================== */

// 给角色分配权限（批量）
export function apiAssignPermissionsToRole(roleId, permissionIds) {
  return request({
    url: `/role/${roleId}/permissions`,
    method: "post",
    data: permissionIds
  });
}

// 更新角色权限（前端变动实时保存）
export function apiUpdateRolePermissions(roleId, permissionIds) {
  return request({
    url: `/role/${roleId}/permissions`,
    method: "put",
    data: permissionIds
  });
}

// 删除角色的全部权限（可选）
export function apiClearRolePermissions(roleId) {
  return request({
    url: `/role/${roleId}/permissions`,
    method: "delete"
  });
}
