import request from "@/utils/axios";

export const login = (data) => {
    return request.post("/auth/login", data);
};

export const logout = () => {
    return request.post("/auth/logout");
};
