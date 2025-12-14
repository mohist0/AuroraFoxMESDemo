import request from "@/api/request.js";

// 获取全部权限
export function getPermissionList() {
  return request({
    url: "/permission/getAllPermissions",
    method: "get"
  });
}

// 获取单个权限
export function getPermission(id) {
  return request({
    url: `/permission/getPermissionById/${id}`,
    method: "get"
  });
}

// 新增权限
export function addPermission(data) {
  return request({
    url: "/permission/addPermission",
    method: "post",
    data
  });
}

// 更新权限
export function updatePermission(id, data) {
  return request({
    url: `/permission/updatePermission/${id}`,
    method: "put",
    data
  });
}

// 删除权限
export function deletePermission(id) {
  return request({
    url: `/permission/deletePermission/${id}`,
    method: "delete"
  });
}
