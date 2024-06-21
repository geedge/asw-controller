/**
 * 1、新增 sys_user 表
 * 2、添加默认用户 admin/admin
 */
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` varchar(64) NOT NULL,
  `name` varchar(255) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  `pwd` varchar(255) NOT NULL,
  `create_timestamp` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_name` (`user_name`) USING BTREE,
  KEY `idx_name` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `sys_user`(`id`, `name`, `user_name`, `pwd`, `create_timestamp`) VALUES ('admin', 'admin', 'admin', 'ad9d757e620d5d9cd8e32c3dbcf37525', UNIX_TIMESTAMP(NOW())*1000);

/**
 * 1、新增 sys_role 表
 * 2、新增 sys_menu 表
 * 3、新增 sys_user_role 表
 * 4、新增 sys_role_menu 表
 * 5、添加初始化数据
 */
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` varchar(64) NOT NULL,
  `name` varchar(255) NOT NULL,
  `i18n` varchar(255) NOT NULL,
  `remark` varchar(255) NOT NULL,
  `build_in` int(10) NOT NULL DEFAULT 0,
  `create_timestamp` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` varchar(64) NOT NULL,
  `name` varchar(255) NOT NULL,
  `i18n` varchar(255) NOT NULL,
  `pid` varchar(64) NOT NULL DEFAULT '',
  `type` varchar(64) NOT NULL,
  `perms` varchar(255) NOT NULL DEFAULT '',
  `route` varchar(255) NOT NULL DEFAULT '',
  `icon` varchar(255) NOT NULL DEFAULT '',
  `order` int(10) NOT NULL DEFAULT 1,
  `create_timestamp` bigint(20) NOT NULL,
  `state` int(10) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` varchar(64) NOT NULL,
  `role_id` varchar(64) NOT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `menu_id` varchar(64) NOT NULL,
  `role_id` varchar(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 添加内置角色
INSERT INTO `sys_role` (`id`, `name`, `i18n`, `remark`, `build_in`, `create_timestamp`) VALUES ('admin', 'admin', 'admin', 'admin', 1, UNIX_TIMESTAMP(NOW())*1000);

/**
 * 1、新增 sys_config 表
 */
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
  `param_key` varchar(50) NOT NULL COMMENT 'key',
  `param_value` text DEFAULT NULL,
  `status` tinyint(4) DEFAULT 1 COMMENT '状态   0：隐藏   1：显示',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`param_key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

/**
  初始化 magic api 时区配置 ，默认 Asia/Shanghai
 */

INSERT INTO `sys_config` (`param_key`, `param_value`, `status`, `remark`) VALUES ( 'timezone', 'Asia/Shanghai', 1, '时区配置');

/**
 * 新增 magic api file 表
 */
DROP TABLE IF EXISTS `magic_api_file`;

CREATE TABLE `magic_api_file` (
  `file_path` varchar(512) NOT NULL,
  `file_content` mediumtext,
  PRIMARY KEY (`file_path`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `magic_api_backup`;

CREATE TABLE `magic_api_backup` (
  `id` varchar(32) NOT NULL COMMENT '原对象ID',
  `create_date` bigint(13) NOT NULL COMMENT '备份时间',
  `tag` varchar(32) DEFAULT NULL COMMENT '标签',
  `type` varchar(32) DEFAULT NULL COMMENT '类型',
  `name` varchar(64) DEFAULT NULL COMMENT '原名称',
  `content` blob COMMENT '备份内容',
  `create_by` varchar(64) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`,`create_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/**
 * 新增 sys_file  sys_file_content 表
 */
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file` (
  `id` varchar(64) NOT NULL COMMENT '文件md5值',
  `name` varchar(255) NOT NULL,
  `suffix` varchar(255) NOT NULL DEFAULT '' COMMENT '文件名后缀',
  `content_type` varchar(255) DEFAULT NULL,
  `size` bigint(20) NOT NULL,
  `path` varchar(512) NOT NULL COMMENT 'file:相对路径 或 db:md5',
  `create_timestamp` bigint(20) NOT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `sys_file_content`;
CREATE TABLE `sys_file_content` (
  `id` varchar(64) NOT NULL,
  `content` longblob NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;