-- 创建数据库并设置字符集与排序规则
CREATE DATABASE IF NOT EXISTS mes_class
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;

-- 切换到目标数据库
USE mes_class;

-- 临时关闭外键检查以便删除或创建表
SET FOREIGN_KEY_CHECKS = 0;

-- 如果存在旧的users表则删除
DROP TABLE IF EXISTS users;

-- 创建users表，存储用户基本信息
CREATE TABLE users
(
    -- 用户编号，主键
    user_id         VARCHAR(50) PRIMARY KEY COMMENT '用户编号',

    -- 用户名
    user_name       VARCHAR(100) NOT NULL COMMENT '用户名',

    -- 用户密码
    user_password   VARCHAR(100) NOT NULL COMMENT '用户密码',

    -- 盐值
    salt_value      VARCHAR(100) NOT NULL COMMENT '盐值',

    -- 最近登录时间
    last_login_time DATETIME COMMENT '最近登录时间',

    -- 创建时间
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) COMMENT ='用户信息表';

-- 如果存在旧的roles表则删除
DROP TABLE IF EXISTS roles;

-- 创建roles表，存储角色信息
CREATE TABLE roles
(
    -- 角色编号，主键
    role_id     VARCHAR(50) PRIMARY KEY COMMENT '角色编号',

    -- 角色名称
    role_name   VARCHAR(100) NOT NULL COMMENT '角色名称',

    -- 角色说明
    role_desc   TEXT COMMENT '角色说明',

    -- 创建时间
    create_time DATETIME COMMENT '创建时间'
) COMMENT ='角色信息表';

-- 如果存在旧的permissions表则删除
DROP TABLE IF EXISTS permissions;

-- 创建permissions表，存储权限信息
CREATE TABLE permissions
(
    -- 权限编号，主键
    permission_id   VARCHAR(50) PRIMARY KEY COMMENT '权限编号',

    -- 权限名称
    permission_name VARCHAR(100) NOT NULL COMMENT '权限名称',

    -- 权限类型
    permission_type VARCHAR(50) COMMENT '权限类型',

    -- 权限说明
    permission_desc TEXT COMMENT '权限说明',

    -- 创建时间
    create_time     DATETIME COMMENT '创建时间'
) COMMENT ='权限信息表';

-- 如果存在旧的user_role表则删除
DROP TABLE IF EXISTS user_role;

-- 创建user_role表，存储用户与角色的映射关系
CREATE TABLE user_role
(
    -- 数据编号，主键
    ur_id       VARCHAR(50) PRIMARY KEY COMMENT '数据编号',

    -- 用户编号，外键关联 users.user_id
    user_id     VARCHAR(50) NOT NULL COMMENT '用户编号',

    -- 角色编号，外键关联 roles.role_id
    role_id     VARCHAR(50) NOT NULL COMMENT '角色编号',

    -- 创建时间
    create_time DATETIME COMMENT '创建时间',

    -- 外键约束：用户编号关联 users 表
    CONSTRAINT fk_userrole_user FOREIGN KEY (user_id) REFERENCES users (user_id),

    -- 外键约束：角色编号关联 roles 表
    CONSTRAINT fk_userrole_role FOREIGN KEY (role_id) REFERENCES roles (role_id)
) COMMENT ='用户角色关系表';

-- 如果存在旧的role_permission表则删除
DROP TABLE IF EXISTS role_permission;

-- 创建role_permission表，存储角色与权限的映射关系
CREATE TABLE role_permission
(
    -- 数据编号，主键
    rp_id         VARCHAR(50) PRIMARY KEY COMMENT '数据编号',

    -- 角色编号，外键关联 roles.role_id
    role_id       VARCHAR(50) NOT NULL COMMENT '角色编号',

    -- 权限编号，外键关联 permissions.permission_id
    permission_id VARCHAR(50) NOT NULL COMMENT '权限编号',

    -- 创建时间
    create_time   DATETIME COMMENT '创建时间',

    -- 外键约束：角色编号关联 roles 表
    CONSTRAINT fk_roleperm_role FOREIGN KEY (role_id) REFERENCES roles (role_id),

    -- 外键约束：权限编号关联 permissions 表
    CONSTRAINT fk_roleperm_perm FOREIGN KEY (permission_id) REFERENCES permissions (permission_id)
) COMMENT ='角色权限关系表';

-- 如果存在旧的product表则删除
DROP TABLE IF EXISTS product;

-- 创建product表，存储产品信息
CREATE TABLE product
(
    -- 产品编号，主键
    product_id   VARCHAR(50) PRIMARY KEY COMMENT '产品编号',

    -- 产品名称
    product_name VARCHAR(100) NOT NULL COMMENT '产品名称',

    -- 产品说明
    product_desc TEXT COMMENT '产品说明',

    -- 创建时间
    create_time  DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    -- 更新时间
    update_time  DATETIME COMMENT '更新时间'
) COMMENT ='产品信息表';

-- 如果存在旧的production_order表则删除
DROP TABLE IF EXISTS production_order;

-- 创建production_order表，存储生产订单信息
CREATE TABLE production_order
(
    -- 生产订单编号，主键
    order_id           VARCHAR(50) PRIMARY KEY COMMENT '生产订单编号',

    -- 产品编号，外键关联 product.product_id
    product_id         VARCHAR(50)  NOT NULL COMMENT '产品编号',

    -- 产品名称
    product_name       VARCHAR(100) NOT NULL COMMENT '产品名称',

    -- 订单数量
    order_quantity     INT          NOT NULL COMMENT '订单数量',

    -- 已排产数量
    scheduled_quantity INT COMMENT '已排产数量',

    -- 已完成数量
    completed_quantity INT COMMENT '已完成数量',

    -- 订单状态
    order_status       VARCHAR(50)  NOT NULL COMMENT '订单状态',

    -- 交期
    delivery_date      DATE COMMENT '交期',

    -- 计划开工日期
    planned_start_date DATE COMMENT '计划开工日期',

    -- 计划完工日期
    planned_end_date   DATE COMMENT '计划完工日期',

    -- 备注
    remark             TEXT COMMENT '备注',

    -- 创建时间
    create_time        DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    -- 更新时间
    update_time        DATETIME COMMENT '更新时间',

    -- 外键约束：产品编号关联 product 表
    CONSTRAINT fk_order_product FOREIGN KEY (product_id) REFERENCES product (product_id)
) COMMENT ='生产订单表';
-- 如果存在旧的production_line表则删除
DROP TABLE IF EXISTS production_line;

-- 创建production_line表，存储生产线信息
CREATE TABLE production_line
(
    -- 生产线编号，主键
    line_id     VARCHAR(50) PRIMARY KEY COMMENT '生产线编号',

    -- 生产线名称
    line_name   VARCHAR(100) NOT NULL COMMENT '生产线名称',

    -- 生产线说明
    line_desc   TEXT COMMENT '生产线说明',

    -- 创建时间
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    -- 更新时间
    update_time DATETIME COMMENT '更新时间'
) COMMENT ='生产线信息表';

-- 如果存在旧的work_order表则删除
DROP TABLE IF EXISTS work_order;

-- 创建work_order表，存储工单信息
CREATE TABLE work_order
(
    -- 工单编号，主键
    work_order_id      VARCHAR(50) PRIMARY KEY COMMENT '工单编号',

    -- 所属生产订单编号，外键关联 production_order.order_id
    order_id           VARCHAR(50) NOT NULL COMMENT '生产订单编号',

    -- 产品编号，外键关联 product.product_id
    product_id         VARCHAR(50) NOT NULL COMMENT '产品编号',

    -- 数量
    quantity           INT         NOT NULL COMMENT '数量',

    -- 已排产数量
    scheduled_quantity INT COMMENT '已排产数量',

    -- 完成数量
    completed_quantity INT COMMENT '完成数量',

    -- 工单状态
    work_order_status  VARCHAR(50) NOT NULL COMMENT '工单状态',

    -- 计划开工时间
    planned_start_time DATETIME COMMENT '计划开工时间',

    -- 计划完工时间
    planned_end_time   DATETIME COMMENT '计划完工时间',

    -- 实际开工时间
    actual_start_time  DATETIME COMMENT '实际开工时间',

    -- 实际完工时间
    actual_end_time    DATETIME COMMENT '实际完工时间',

    -- 生产线编号，外键关联 production_line.line_id
    line_id            VARCHAR(50) NOT NULL COMMENT '生产线编号',

    -- 备注
    remark             TEXT COMMENT '备注',

    -- 创建时间
    create_time        DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    -- 更新时间
    update_time        DATETIME COMMENT '更新时间',

    -- 外键约束：生产订单编号关联 production_order 表
    CONSTRAINT fk_workorder_order FOREIGN KEY (order_id) REFERENCES production_order (order_id),

    -- 外键约束：产品编号关联 product 表
    CONSTRAINT fk_workorder_product FOREIGN KEY (product_id) REFERENCES product (product_id),

    -- 外键约束：生产线编号关联 production_line 表
    CONSTRAINT fk_workorder_line FOREIGN KEY (line_id) REFERENCES production_line (line_id)
) COMMENT ='工单信息表';

-- 如果存在旧的material表则删除
DROP TABLE IF EXISTS material;

-- 创建material表，存储物料信息
CREATE TABLE material
(
    -- 物料编号，主键
    material_id   VARCHAR(50) PRIMARY KEY COMMENT '物料编号',

    -- 物料名称
    material_name VARCHAR(100) NOT NULL COMMENT '物料名称',

    -- 物料说明
    material_desc TEXT COMMENT '物料说明',

    -- 计量单位
    unit          VARCHAR(20) COMMENT '计量单位',

    -- 创建时间
    create_time   DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    -- 更新时间
    update_time   DATETIME COMMENT '更新时间'
) COMMENT ='物料信息表';

-- 如果存在旧的production_schedule表则删除
DROP TABLE IF EXISTS production_schedule;

-- 创建production_schedule表，存储生产排程信息
CREATE TABLE production_schedule
(
    -- 排程编号，主键
    schedule_id        VARCHAR(50) PRIMARY KEY COMMENT '排程编号',

    -- 工单编号，外键关联 work_order.work_order_id
    work_order_id      VARCHAR(50) NOT NULL COMMENT '工单编号',

    -- 生产订单编号，外键关联 production_order.order_id
    order_id           VARCHAR(50) NOT NULL COMMENT '生产订单编号',

    -- 产品编号，外键关联 product.product_id
    product_id         VARCHAR(50) NOT NULL COMMENT '产品编号',

    -- 生产线编号，外键关联 production_line.line_id
    line_id            VARCHAR(50) NOT NULL COMMENT '生产线编号',

    -- 物料编号，外键关联 material.material_id
    material_id        VARCHAR(50) NOT NULL COMMENT '物料编号',

    -- 计划开工时间
    planned_start_time DATETIME COMMENT '计划开工时间',

    -- 计划完工时间
    planned_end_time   DATETIME COMMENT '计划完工时间',

    -- 实际开工时间
    actual_start_time  DATETIME COMMENT '实际开工时间',

    -- 实际完工时间
    actual_end_time    DATETIME COMMENT '实际完工时间',

    -- 备注
    remark             TEXT COMMENT '备注',

    -- 创建时间
    create_time        DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    -- 更新时间
    update_time        DATETIME COMMENT '更新时间',

    -- 外键约束：工单编号关联 work_order 表
    CONSTRAINT fk_schedule_workorder FOREIGN KEY (work_order_id) REFERENCES work_order (work_order_id),

    -- 外键约束：生产订单编号关联 production_order 表
    CONSTRAINT fk_schedule_order FOREIGN KEY (order_id) REFERENCES production_order (order_id),

    -- 外键约束：产品编号关联 product 表
    CONSTRAINT fk_schedule_product FOREIGN KEY (product_id) REFERENCES product (product_id),

    -- 外键约束：生产线编号关联 production_line 表
    CONSTRAINT fk_schedule_line FOREIGN KEY (line_id) REFERENCES production_line (line_id),

    -- 外键约束：物料编号关联 material 表
    CONSTRAINT fk_schedule_material FOREIGN KEY (material_id) REFERENCES material (material_id)
) COMMENT ='生产排程信息表';

-- 插入角色数据
INSERT INTO roles (role_id, role_name, role_desc, create_time)
VALUES ('R001', '管理层', '负责战略决策与数据分析', NOW()),
       ('R002', '系统管理员', '负责系统用户与权限管理', NOW()),
       ('R003', '设备管理员', '负责设备维护与管理', NOW()),
       ('R004', '生产计划员', '负责生产计划与排程', NOW()),
       ('R005', '车间操作员', '负责生产执行', NOW()),
       ('R006', '质检人员', '负责质量检测与管理', NOW());

-- 插入权限数据
INSERT INTO permissions (permission_id, permission_name, permission_type, permission_desc, create_time)
VALUES ('P001', '数据分析', NULL, '管理层使用的数据分析权限', NOW()),
       ('P002', '用户管理', NULL, '系统管理员的用户管理权限', NOW()),
       ('P003', '权限管理', NULL, '系统管理员的权限管理权限', NOW()),
       ('P004', '生产计划管理', NULL, '生产计划员的生产计划管理权限', NOW()),
       ('P005', '设备管理', NULL, '设备管理员的设备管理权限', NOW()),
       ('P006', '质量管理', NULL, '质检人员的质量管理权限', NOW()),
       ('P007', '生产执行管理', NULL, '车间操作员的生产执行权限', NOW());

-- 插入角色权限映射数据
INSERT INTO role_permission (rp_id, role_id, permission_id, create_time)
VALUES ('RP001', 'R001', 'P001', NOW()),
       ('RP002', 'R002', 'P002', NOW()),
       ('RP003', 'R002', 'P003', NOW()),
       ('RP004', 'R004', 'P004', NOW()),
       ('RP005', 'R003', 'P005', NOW()),
       ('RP006', 'R006', 'P006', NOW()),
       ('RP007', 'R005', 'P007', NOW());

-- 插入产品数据
INSERT INTO product (product_id, product_name, product_desc, create_time)
VALUES ('PR001', '高性能芯片A', '用于服务器的高性能芯片', NOW()),
       ('PR002', '低功耗芯片B', '用于移动设备的低功耗芯片', NOW());

-- 插入物料数据
INSERT INTO material (material_id, material_name, material_desc, unit, create_time)
VALUES ('M001', '硅晶圆', '芯片制造的基础材料', '片', NOW()),
       ('M002', '光刻胶', '用于芯片光刻工艺', '升', NOW()),
       ('M003', '金属导线', '用于芯片内部互连', '米', NOW());

-- 插入生产线数据
INSERT INTO production_line (line_id, line_name, line_desc, create_time)
VALUES ('L001', '生产线A', '用于高性能芯片生产', NOW()),
       ('L002', '生产线B', '用于低功耗芯片生产', NOW()),
       ('L003', '生产线C', '备用生产线', NOW());

-- 插入生产订单数据
INSERT INTO production_order (order_id, product_id, product_name, order_quantity, scheduled_quantity,
                              completed_quantity, order_status, delivery_date, planned_start_date, planned_end_date,
                              remark, create_time)
VALUES ('O001', 'PR001', '高性能芯片A', 1000, 500, 200, '生产中', '2025-12-31', '2025-12-01', '2025-12-30',
        '首批高性能芯片订单', NOW());

-- 插入工单数据
INSERT INTO work_order (work_order_id, order_id, product_id, quantity, scheduled_quantity, completed_quantity,
                        work_order_status, planned_start_time, planned_end_time, actual_start_time, actual_end_time,
                        line_id, remark, create_time)
VALUES ('W001', 'O001', 'PR001', 500, 300, 100, '生产中', '2025-12-01 08:00:00', '2025-12-15 18:00:00',
        '2025-12-01 09:00:00', NULL, 'L001', '首批工单，分配到生产线A', NOW());

-- 插入生产排程数据
INSERT INTO production_schedule (schedule_id, work_order_id, order_id, product_id, line_id, material_id,
                                 planned_start_time, planned_end_time, actual_start_time, actual_end_time, remark,
                                 create_time)
VALUES ('S001', 'W001', 'O001', 'PR001', 'L001', 'M001', '2025-12-01 08:00:00', '2025-12-15 18:00:00',
        '2025-12-01 09:00:00', NULL, '排程使用硅晶圆作为主要物料', NOW());

-- 插入初始管理员用户 AuroraFox
INSERT INTO users (user_id, user_name, user_password, salt_value, last_login_time, create_time)
VALUES ('U001', 'AuroraFox',
        '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', -- SHA256("123456" + salt)
        'a1b2c3d4e5f67890abcdef1234567890', -- 盐值
        NULL, NOW());

-- 绑定 AuroraFox 用户到 R001 管理层
INSERT INTO user_role (ur_id, user_id, role_id, create_time)
VALUES (UUID(), 'U001', 'R001', NOW());

-- 绑定 AuroraFox 用户到 R002 系统管理员
INSERT INTO user_role (ur_id, user_id, role_id, create_time)
VALUES (UUID(), 'U001', 'R002', NOW());

-- 绑定 AuroraFox 用户到 R004 生产计划员
INSERT INTO user_role (ur_id, user_id, role_id, create_time)
VALUES (UUID(), 'U001', 'R004', NOW());

-- 所有表定义完成后重新开启外键检查
SET FOREIGN_KEY_CHECKS = 1;