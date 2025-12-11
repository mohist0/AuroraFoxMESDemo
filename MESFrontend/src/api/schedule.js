import request from "@/utils/request";

export function getSchedulePlan() {
  return request({
    url: "/schedule/getAll",
    method: "get"
  });
}
