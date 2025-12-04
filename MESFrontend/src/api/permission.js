import request from "@/utils/request";

// 查询权限列表
export function getPermissionList(query) {
  return request({
    url: "/permission/list",
    method: "get",
    params: query
  });
}

// 查询单个权限
export function getPermission(id) {
  return request({
    url: `/permission/${id}`,
    method: "get"
  });
}

// 新增权限
export function addPermission(data) {
  return request({
    url: "/permission",
    method: "post",
    data
  });
}

// 更新权限
export function updatePermission(data) {
  return request({
    url: "/permission",
    method: "put",
    data
  });
}

// 删除权限
export function deletePermission(id) {
  return request({
    url: `/permission/${id}`,
    method: "delete"
  });
}
