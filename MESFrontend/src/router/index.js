import { createRouter, createWebHistory } from "vue-router";

import RoleManage from "@/views/RoleManage.vue";
import PermissionManage from "../views/PermissionManage.vue";

const routes = [
  {
    path: "/",
    redirect: "/role"
  },
  {
    path: "/role",
    name: "RoleManage",
    component: RoleManage
  },
  {
    path: "/permission",
    name: "PermissionManage",
   component: PermissionManage
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
