import request from "@/utils/request";

// 查询角色列表（支持分页、搜索）
export function apiGetRoleList(params) {
  return request({
    url: "/role/list",
    method: "get",
    params
  });
}

// 查询单个角色信息（用于编辑回显）
export function apiGetRoleById(id) {
  return request({
    url: `/role/${id}`,
    method: "get",
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

// 更新角色
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
    method: "delete",
  });
}
