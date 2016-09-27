# 关掉当前会话的外键约束
SET SESSION FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS sys_area;# 区域表
DROP TABLE IF EXISTS sys_dict;# 字典表
DROP TABLE IF EXISTS sys_mdict;# 多级字典表
DROP TABLE IF EXISTS sys_role;# 角色表
DROP TABLE IF EXISTS sys_office;# 部门表
DROP TABLE IF EXISTS sys_role_office;# 角色-机构表
DROP TABLE IF EXISTS sys_user;# 用户表
DROP TABLE IF EXISTS sys_user_role;# 用户-角色
DROP TABLE IF EXISTS sys_menu;# 菜单表
DROP TABLE IF EXISTS sys_role_menu;# 角色菜单表
DROP TABLE IF EXISTS sys_log;# 日志表

CREATE TABLE sys_area(
  id VARCHAR(64) NOT NULL COMMENT '编号',
  parent_id VARCHAR(64) NOT NULL COMMENT '父级编号',
  parent_ids VARCHAR(2000) NOT NULL COMMENT '所有父级编号',
  name VARCHAR(100) NOT NULL COMMENT '区域名称',
  sort DECIMAL(10,0) NOT NULL COMMENT '排序',
  code VARCHAR(100) COMMENT '区域编码',
  type CHAR(1) COMMENT '区域类型', #1：国家；2：省份、直辖市；3：地市；4：区县
  create_by VARCHAR(64) NOT NULL COMMENT '创建者',
  create_date DATETIME NOT NULL COMMENT '创建时间',
  update_by VARCHAR(64) NOT NULL COMMENT '更新者',
  update_date DATETIME NOT NULL COMMENT '更新时间',
  remarks VARCHAR(255) COMMENT '备注消息',
  del_flag CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
  PRIMARY KEY (id)
)COMMENT = '区域表' CHARSET=utf8 ENGINE= InnoDB;

CREATE TABLE sys_dict(
  id VARCHAR(64) NOT NULL COMMENT '编号',
  value VARCHAR(100) NOT NULL COMMENT '数据值',
  lable VARCHAR(100) NOT NULL COMMENT '标签名',
  type VARCHAR(100) NOT NULL COMMENT '类型',
  description VARCHAR(100) NOT NULL COMMENT '描述',
  sort DECIMAL(10,0) NOT NULL COMMENT '排序（升序）',
  parent_id VARCHAR(64) DEFAULT '0' COMMENT '父级编号',
  create_by VARCHAR(64) NOT NULL COMMENT '创建者',
  create_date DATETIME NOT NULL COMMENT '创建时间',
  update_by VARCHAR(64) NOT NULL COMMENT '更新者',
  update_date DATETIME NOT NULL COMMENT '更新时间',
  remarks VARCHAR(255) COMMENT '备注消息',
  del_flag CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
  PRIMARY KEY (id)
)COMMENT = '字典表' CHARSET=utf8 ENGINE= InnoDB;

CREATE TABLE sys_mdict(
  id VARCHAR(64) NOT NULL COMMENT '编号',
  parent_id VARCHAR(64) NOT NULL COMMENT '父级编号',
  parent_ids VARCHAR(2000) NOT NULL COMMENT '所有父级编号',
  name VARCHAR(100) NOT NULL COMMENT '名称',
  sort DECIMAL(10,0) NOT NULL COMMENT '排序',
  description VARCHAR(100) NOT NULL COMMENT '描述',
  create_by VARCHAR(64) NOT NULL COMMENT '创建者',
  create_date DATETIME NOT NULL COMMENT '创建时间',
  update_by VARCHAR(64) NOT NULL COMMENT '更新者',
  update_date DATETIME NOT NULL COMMENT '更新时间',
  remarks VARCHAR(255) COMMENT '备注消息',
  del_flag CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
  PRIMARY KEY (id)
)COMMENT = '多级字典表' CHARSET=utf8 ENGINE= InnoDB;

CREATE TABLE sys_role(
  id VARCHAR(64) NOT NULL COMMENT '编号',
  office_id VARCHAR(64) COMMENT '归属机构',
  name VARCHAR(100) NOT NULL COMMENT '角色名称',
  enname VARCHAR(255) COMMENT '英文名称',
  role_type VARCHAR(255) COMMENT '角色类型',
  data_scope CHAR(1) COMMENT '数据范围', #0：所有数据；1：所在公司及以下数据；2：所在公司数据；3：所在部门及以下数据；4：所在部门数据；8：仅本人数据；9：按明细设置
  is_sys VARCHAR(64) COMMENT '是否系统数据',
  useable VARCHAR(64) COMMENT '是否可用',
  create_by VARCHAR(64) NOT NULL COMMENT '创建者',
  create_date DATETIME NOT NULL COMMENT '创建时间',
  update_by VARCHAR(64) NOT NULL COMMENT '更新者',
  update_date DATETIME NOT NULL COMMENT '更新时间',
  remarks VARCHAR(255) COMMENT '备注消息',
  del_flag CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记', #0：正常；1：删除
  PRIMARY KEY (id)
) COMMENT = '角色表' CHARSET=utf8 ENGINE= InnoDB;

CREATE TABLE sys_office(
  id VARCHAR(64) NOT NULL COMMENT '编号',
  parent_id VARCHAR(64) NOT NULL COMMENT '父级编号',
  parent_ids VARCHAR(2000) NOT NULL COMMENT '所有父级编号',
  name VARCHAR(100) NOT NULL COMMENT '名称',
  sort DECIMAL(10,0) NOT NULL COMMENT '排序',
  area_id varchar(64) NOT NULL COMMENT '归属区域',
  code varchar(100) COMMENT '区域编码',
  type char(1) NOT NULL COMMENT '机构类型', #1：公司；2：部门；3：小组
  grade char(1) NOT NULL COMMENT '机构等级', #1：一级；2：二级；3：三级；4：四级
  address varchar(255) COMMENT '联系地址',
  zip_code varchar(100) COMMENT '邮政编码',
  master varchar(100) COMMENT '负责人',
  phone varchar(200) COMMENT '电话',
  fax varchar(200) COMMENT '传真',
  email varchar(200) COMMENT '邮箱',
  USEABLE varchar(64) COMMENT '是否启用',
  PRIMARY_PERSON varchar(64) COMMENT '主负责人',
  DEPUTY_PERSON varchar(64) COMMENT '副负责人',
  create_by VARCHAR(64) NOT NULL COMMENT '创建者',
  create_date DATETIME NOT NULL COMMENT '创建时间',
  update_by VARCHAR(64) NOT NULL COMMENT '更新者',
  update_date DATETIME NOT NULL COMMENT '更新时间',
  remarks VARCHAR(255) COMMENT '备注消息',
  del_flag CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
  PRIMARY KEY (id)
)COMMENT = '机构表' CHARSET=utf8 ENGINE= InnoDB;

CREATE TABLE sys_role_office(
  role_id VARCHAR(64) NOT NULL COMMENT '角色编号',
  office_id VARCHAR(64) NOT NULL COMMENT '机构编号',
  PRIMARY KEY (role_id, office_id)
)COMMENT = '角色-机构' CHARSET=utf8 ENGINE= InnoDB;

CREATE TABLE sys_user(
  id VARCHAR(64) NOT NULL COMMENT '编号',
  company_id VARCHAR(64) NOT NULL COMMENT '归属公司',
  department_id VARCHAR(64) NOT NULL COMMENT '归属部门',
  login_name VARCHAR(100) NOT NULL COMMENT '登录名',
  password VARCHAR(100) NOT NULL COMMENT '密码',
  no VARCHAR(100) COMMENT '工号',
  name VARCHAR(100) NOT NULL COMMENT '姓名',
  email VARCHAR(200) COMMENT '邮箱',
  phone VARCHAR(200) COMMENT '电话',
  mobile VARCHAR(200) COMMENT '手机',
  user_type CHAR(1) COMMENT '用户类型',
  photo VARCHAR(1000) COMMENT '用户头像',
  login_ip VARCHAR(100) COMMENT '最后登陆IP',
  login_flag VARCHAR(64) COMMENT '是否可登陆',
  create_by VARCHAR(64) NOT NULL COMMENT '创建者',
  create_date DATETIME NOT NULL COMMENT '创建时间',
  update_by VARCHAR(64) NOT NULL COMMENT '更新者',
  update_date DATETIME NOT NULL COMMENT '更新时间',
  remarks VARCHAR(255) COMMENT '备注消息',
  del_flag CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
  PRIMARY KEY (id)
)COMMENT = '用户表' CHARSET=utf8 ENGINE= InnoDB;

CREATE TABLE sys_user_role(
  user_id VARCHAR(64) NOT NULL COMMENT '用户编号',
  role_id VARCHAR(64) NOT NULL COMMENT '角色编号',
  PRIMARY KEY (user_id, role_id)
)COMMENT = '用户-角色' CHARSET=utf8 ENGINE= InnoDB;

CREATE TABLE sys_menu(
  id VARCHAR(64) NOT NULL COMMENT '编号',
  parent_id VARCHAR(64) NOT NULL COMMENT '父级编号',
  parent_ids VARCHAR(2000) NOT NULL COMMENT '所有父级编号',
  name VARCHAR(100) NOT NULL COMMENT '名称',
  sort DECIMAL(10,0) NOT NULL COMMENT '排序',
  href VARCHAR(2000) COMMENT '链接',
  target VARCHAR(20) COMMENT '目标', #mainFrame、 _blank、_self、_parent、_top
  icon VARCHAR(100) COMMENT '图标',
  is_show CHAR(1) NOT NULL COMMENT '是否在菜单显示', #1：显示；0：不显示
  permission VARCHAR(200) COMMENT '权限标识',
  create_by VARCHAR(64) NOT NULL COMMENT '创建者',
  create_date DATETIME NOT NULL COMMENT '创建时间',
  update_by VARCHAR(64) NOT NULL COMMENT '更新者',
  update_date DATETIME NOT NULL COMMENT '更新时间',
  remarks VARCHAR(255) COMMENT '备注消息',
  del_flag CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
  PRIMARY KEY (id)
)COMMENT = '菜单表' CHARSET=utf8 ENGINE= InnoDB;

CREATE TABLE sys_role_menu(
  role_id VARCHAR(64) NOT NULL COMMENT '角色编号',
  menu_id VARCHAR(64) NOT NULL COMMENT '菜单编号',
  PRIMARY KEY (role_id, menu_id)
)COMMENT = '角色-菜单' CHARSET=utf8 ENGINE= InnoDB;

CREATE TABLE sys_log(
  id VARCHAR(64) NOT NULL COMMENT '编号',
  type CHAR(1) DEFAULT '1' COMMENT '日志类型', #1：接入日志；2：异常日志
  title VARCHAR(255) DEFAULT '' COMMENT '日志标题',
  create_by VARCHAR(64) NOT NULL COMMENT '创建者',
  create_date DATETIME NOT NULL COMMENT '创建时间',
  remote_addr VARCHAR(255) COMMENT '操作ip地址',
  user_agent VARCHAR(255) COMMENT '用户代理',
  request_uri VARCHAR(255) COMMENT '请求URI',
  method VARCHAR(5) COMMENT '操作方式',
  params text COMMENT '操作提交数据',
  exception text COMMENT '异常消息',
  PRIMARY KEY (id)
)COMMENT = '日志表' CHARSET=utf8 ENGINE= InnoDB;

# Create Indexes

CREATE INDEX sys_area_parent_id ON sys_area (parent_id ASC);
CREATE INDEX sys_area_del_flag ON sys_area(del_flag ASC);
CREATE INDEX sys_dict_value ON sys_dict(value ASC);
CREATE INDEX sys_dict_label ON sys_dict(lable ASC);
CREATE INDEX sys_mdict_parent_id ON sys_mdict(parent_id ASC);
CREATE INDEX sys_mdict_del_flag ON sys_mdict(del_flag ASC);
CREATE INDEX sys_dict_del_flag ON sys_dict(del_flag ASC);
CREATE INDEX sys_log_create_by ON sys_log(create_by ASC);
CREATE INDEX sys_log_create_date ON sys_log(create_date ASC);
CREATE INDEX sys_log_request_uri ON sys_log(request_uri ASC);
CREATE INDEX sys_log_type ON sys_log(type ASC);
CREATE INDEX sys_menu_parent_id ON sys_menu(parent_id ASC);
CREATE INDEX sys_menu_del_flag ON sys_menu(del_flag ASC);
CREATE INDEX sys_office_parent_id ON sys_office(parent_id ASC);
CREATE INDEX sys_office_del_flag ON sys_office(del_flag ASC);
CREATE INDEX sys_office_type ON sys_office(type ASC);
CREATE INDEX sys_role_enname ON sys_role(enname ASC);
CREATE INDEX sys_role_del_flag ON sys_role(del_flag ASC);
CREATE INDEX sys_user_department_id ON sys_user(department_id ASC);
CREATE INDEX sys_user_company_id ON sys_user(company_id ASC);
CREATE INDEX sys_user_login_name ON sys_user(login_name ASC);
CREATE INDEX sys_user_update_date ON sys_user(update_date ASC);
CREATE INDEX sys_user_del_flag ON sys_user(del_flag ASC);

