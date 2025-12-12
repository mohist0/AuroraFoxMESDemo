// src/api/user.js
//用户管理api
import request from "@/utils/axios";

// 获取全部用户（GET）
export const getAllUsers = () => request.get("/user/getAllUsers");

// 添加用户（POST，参数格式严格匹配接口）
export const addUser = (data) => request.post("/user/addUser", data);

// 更新用户（PUT，参数格式严格匹配接口）
export const updateUser = (userId, data) => request.put(`/user/updateUser/${userId}`, data);

// 删除用户（DELETE）
export const deleteUser = (userId) => request.delete(`/user/deleteUser/${userId}`);

// 根据ID获取用户（GET）
export const getUserById = (userId) => request.get(`/user/getUserById/${userId}`);

// 获取用户角色（GET）
export const getUserRoles = (userId) => request.get(`/user/${userId}/roles`);