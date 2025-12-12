SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for QRTZ_BLOB_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_BLOB_TRIGGERS`;
CREATE TABLE `QRTZ_BLOB_TRIGGERS`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `blob_data` blob NULL COMMENT '存放持久化Trigger对象',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `QRTZ_BLOB_TRIGGERS_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `QRTZ_TRIGGERS` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = 'Blob类型的触发器表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of QRTZ_BLOB_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_CALENDARS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_CALENDARS`;
CREATE TABLE `QRTZ_CALENDARS`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '调度名称',
  `calendar_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '日历名称',
  `calendar` blob NOT NULL COMMENT '存放持久化calendar对象',
  PRIMARY KEY (`sched_name`, `calendar_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '日历信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of QRTZ_CALENDARS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_CRON_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_CRON_TRIGGERS`;
CREATE TABLE `QRTZ_CRON_TRIGGERS`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `cron_expression` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'cron表达式',
  `time_zone_id` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '时区',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `QRTZ_CRON_TRIGGERS_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `QRTZ_TRIGGERS` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = 'Cron类型的触发器表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of QRTZ_CRON_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_FIRED_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_FIRED_TRIGGERS`;
CREATE TABLE `QRTZ_FIRED_TRIGGERS`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '调度名称',
  `entry_id` varchar(95) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '调度器实例id',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `instance_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '调度器实例名',
  `fired_time` bigint(13) NOT NULL COMMENT '触发的时间',
  `sched_time` bigint(13) NOT NULL COMMENT '定时器制定的时间',
  `priority` int(11) NOT NULL COMMENT '优先级',
  `state` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '状态',
  `job_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '任务名称',
  `job_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '任务组名',
  `is_nonconcurrent` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '是否并发',
  `requests_recovery` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '是否接受恢复执行',
  PRIMARY KEY (`sched_name`, `entry_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '已触发的触发器表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of QRTZ_FIRED_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_JOB_DETAILS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_JOB_DETAILS`;
CREATE TABLE `QRTZ_JOB_DETAILS`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '调度名称',
  `job_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '任务名称',
  `job_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '任务组名',
  `description` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '相关介绍',
  `job_class_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '执行任务类名称',
  `is_durable` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '是否持久化',
  `is_nonconcurrent` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '是否并发',
  `is_update_data` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '是否更新数据',
  `requests_recovery` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '是否接受恢复执行',
  `job_data` blob NULL COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`, `job_name`, `job_group`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '任务详细信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of QRTZ_JOB_DETAILS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_LOCKS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_LOCKS`;
CREATE TABLE `QRTZ_LOCKS`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '调度名称',
  `lock_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '悲观锁名称',
  PRIMARY KEY (`sched_name`, `lock_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '存储的悲观锁信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of QRTZ_LOCKS
-- ----------------------------
INSERT INTO `QRTZ_LOCKS` VALUES ('AdminScheduler', 'STATE_ACCESS');
INSERT INTO `QRTZ_LOCKS` VALUES ('AdminScheduler', 'TRIGGER_ACCESS');

-- ----------------------------
-- Table structure for QRTZ_PAUSED_TRIGGER_GRPS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_PAUSED_TRIGGER_GRPS`;
CREATE TABLE `QRTZ_PAUSED_TRIGGER_GRPS`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '调度名称',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'trigger组名',
  PRIMARY KEY (`sched_name`, `trigger_group`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '暂停的触发器表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of QRTZ_PAUSED_TRIGGER_GRPS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_SCHEDULER_STATE
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SCHEDULER_STATE`;
CREATE TABLE `QRTZ_SCHEDULER_STATE`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '调度名称',
  `instance_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '实例名称',
  `last_checkin_time` bigint(13) NOT NULL COMMENT '上次检查时间',
  `checkin_interval` bigint(13) NOT NULL COMMENT '检查间隔时间',
  PRIMARY KEY (`sched_name`, `instance_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '调度器状态表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of QRTZ_SCHEDULER_STATE
-- ----------------------------
INSERT INTO `QRTZ_SCHEDULER_STATE` VALUES ('AdminScheduler', 'DESKTOP-6CHNMD31758636199600', 1758640778420, 15000);
INSERT INTO `QRTZ_SCHEDULER_STATE` VALUES ('AdminScheduler', 'iZbp18afp9a01ex4fsodntZ1757930656176', 1758640781654, 15000);

-- ----------------------------
-- Table structure for QRTZ_SIMPLE_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SIMPLE_TRIGGERS`;
CREATE TABLE `QRTZ_SIMPLE_TRIGGERS`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `repeat_count` bigint(7) NOT NULL COMMENT '重复的次数统计',
  `repeat_interval` bigint(12) NOT NULL COMMENT '重复的间隔时间',
  `times_triggered` bigint(10) NOT NULL COMMENT '已经触发的次数',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `QRTZ_SIMPLE_TRIGGERS_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `QRTZ_TRIGGERS` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '简单触发器的信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of QRTZ_SIMPLE_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_SIMPROP_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SIMPROP_TRIGGERS`;
CREATE TABLE `QRTZ_SIMPROP_TRIGGERS`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `str_prop_1` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'String类型的trigger的第一个参数',
  `str_prop_2` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'String类型的trigger的第二个参数',
  `str_prop_3` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'String类型的trigger的第三个参数',
  `int_prop_1` int(11) NULL DEFAULT NULL COMMENT 'int类型的trigger的第一个参数',
  `int_prop_2` int(11) NULL DEFAULT NULL COMMENT 'int类型的trigger的第二个参数',
  `long_prop_1` bigint(20) NULL DEFAULT NULL COMMENT 'long类型的trigger的第一个参数',
  `long_prop_2` bigint(20) NULL DEFAULT NULL COMMENT 'long类型的trigger的第二个参数',
  `dec_prop_1` decimal(13, 4) NULL DEFAULT NULL COMMENT 'decimal类型的trigger的第一个参数',
  `dec_prop_2` decimal(13, 4) NULL DEFAULT NULL COMMENT 'decimal类型的trigger的第二个参数',
  `bool_prop_1` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'Boolean类型的trigger的第一个参数',
  `bool_prop_2` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'Boolean类型的trigger的第二个参数',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `QRTZ_SIMPROP_TRIGGERS_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `QRTZ_TRIGGERS` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '简单触发器的扩展信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of QRTZ_SIMPROP_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_TRIGGERS`;
CREATE TABLE `QRTZ_TRIGGERS`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '触发器的名字',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '触发器所属组的名字',
  `job_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'qrtz_job_details表job_name的外键',
  `job_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'qrtz_job_details表job_group的外键',
  `description` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '相关介绍',
  `next_fire_time` bigint(13) NULL DEFAULT NULL COMMENT '上一次触发时间（毫秒）',
  `prev_fire_time` bigint(13) NULL DEFAULT NULL COMMENT '下一次触发时间（默认为-1表示不触发）',
  `priority` int(11) NULL DEFAULT NULL COMMENT '优先级',
  `trigger_state` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '触发器状态',
  `trigger_type` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '触发器的类型',
  `start_time` bigint(13) NOT NULL COMMENT '开始时间',
  `end_time` bigint(13) NULL DEFAULT NULL COMMENT '结束时间',
  `calendar_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '日程表名称',
  `misfire_instr` smallint(2) NULL DEFAULT NULL COMMENT '补偿执行的策略',
  `job_data` blob NULL COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  INDEX `sched_name`(`sched_name`, `job_name`, `job_group`) USING BTREE,
  CONSTRAINT `QRTZ_TRIGGERS_ibfk_1` FOREIGN KEY (`sched_name`, `job_name`, `job_group`) REFERENCES `QRTZ_JOB_DETAILS` (`sched_name`, `job_name`, `job_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '触发器详细信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of QRTZ_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for performance_metrics
-- ----------------------------
DROP TABLE IF EXISTS `performance_metrics`;
CREATE TABLE `performance_metrics`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `metric_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `metric_value` decimal(5, 2) NULL DEFAULT NULL,
  `collect_time` timestamp NULL DEFAULT NULL,
  `server_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_metric_time`(`metric_type`, `collect_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 587756 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '性能监控' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `config_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置项名称',
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置项键名',
  `config_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '配置项值',
  `config_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'string' COMMENT '配置项类型(string:字符串 number:数字 boolean:布尔值 json:JSON对象)',
  `config_group` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'default' COMMENT '配置项分组',
  `config_desc` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配置项描述',
  `is_enabled` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用(0:禁用 1:启用)',
  `is_system` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否系统配置(0:否 1:是)',
  `sort_order` int(11) NULL DEFAULT 0 COMMENT '排序序号',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '逻辑删除 0正常 1删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_config_key`(`config_key`) USING BTREE,
  INDEX `idx_config_group`(`config_group`) USING BTREE,
  INDEX `idx_is_enabled`(`is_enabled`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, '系统名称', 'sys.name', 'BaseAdmin', 'string', 'system', '系统名称配置', 1, 1, 1, '2025-07-12 20:06:00', '2025-07-13 21:46:55', '', NULL, '1', '0');
INSERT INTO `sys_config` VALUES (2, '系统版本', 'sys.version', '1.0.0', 'string', 'system', '系统版本号', 1, 1, 2, '2025-07-12 20:06:00', '2025-07-12 20:06:00', NULL, NULL, NULL, '0');
INSERT INTO `sys_config` VALUES (3, '系统Logo', 'sys.logo', '/logo.png', 'string', 'system', '系统Logo图片路径', 1, 1, 3, '2025-07-12 20:06:00', '2025-07-12 20:06:00', NULL, NULL, NULL, '0');
INSERT INTO `sys_config` VALUES (4, '系统首页', 'sys.indexHome', '/home', 'string', 'system', '系统默认首页', 1, 1, 4, '2025-07-12 20:06:00', '2025-07-13 16:52:58', '', NULL, '1', '0');
INSERT INTO `sys_config` VALUES (5, '系统底部信息', 'sys.footer', '版权所有 © 2023 [陈黎明] 保留所有权利', 'string', 'system', '系统底部版权信息', 1, 1, 5, '2025-07-12 20:06:00', '2025-07-13 17:07:46', '', NULL, '1', '0');
INSERT INTO `sys_config` VALUES (6, '用户默认密码', 'sys.user.defaultPassword', '123456', 'string', 'system', '用户默认密码', 1, 1, 6, '2025-07-12 20:06:00', '2025-07-12 20:06:00', NULL, NULL, NULL, '0');
INSERT INTO `sys_config` VALUES (7, '用户头像上传路径', 'sys.user.avatarPath', '/upload/avatar', 'string', 'upload', '用户头像上传路径', 1, 1, 7, '2025-07-12 20:06:00', '2025-07-13 00:59:55', NULL, NULL, '1', '0');
INSERT INTO `sys_config` VALUES (8, '文件上传路径', 'storage.upload.path', '/upload/file', 'string', 'storage', '文件上传根路径', 1, 1, 8, '2025-07-12 20:06:00', '2025-07-14 11:03:37', '', NULL, '1', '0');
INSERT INTO `sys_config` VALUES (9, '允许上传文件类型', 'sys.upload.allowedFileTypes', 'jpg,jpeg,png,gif,doc,docx,xls,xlsx,ppt,pptx,pdf,zip,rar,txt', 'string', 'upload', '允许上传的文件类型', 1, 1, 9, '2025-07-12 20:06:00', '2025-07-13 01:02:38', NULL, NULL, '1', '1');
INSERT INTO `sys_config` VALUES (10, '最大上传文件大小', 'sys.upload.maxFileSize', '10', 'number', 'upload', '最大上传文件大小(MB)', 1, 1, 10, '2025-07-12 20:06:00', '2025-07-13 01:02:38', NULL, NULL, '1', '1');
INSERT INTO `sys_config` VALUES (11, '123', '123', '12312312', 'string', 'default', NULL, 1, 0, 0, '2025-07-13 00:56:22', '2025-07-13 01:02:45', NULL, '1', '1', '1');
INSERT INTO `sys_config` VALUES (12, '默认上传仓库', 'storage.storageType', 'minio', 'string', 'storage', '', 1, 1, 0, '2025-07-14 10:54:42', '2025-07-15 15:47:20', '', '1', '1', '0');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `dept_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父部门id',
  `ancestors` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '部门名称',
  `order_num` int(4) NULL DEFAULT 0 COMMENT '显示顺序',
  `leader` bigint(20) NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '邮箱',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '部门表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (1, 0, '0', '总公司', 0, 1, '18827574649', '2360187899@qq.com', '0', '0', '1', '2025-09-08 00:35:54', '1', '2025-09-09 00:49:16', '');
INSERT INTO `sys_dept` VALUES (2, 1, '0,1', '研发部门', 0, NULL, '', '', '0', '0', '1', '2025-09-08 15:53:29', '1', '2025-09-08 15:53:29', '');
INSERT INTO `sys_dept` VALUES (3, 1, '0,1', '市场部门', 0, NULL, '', '', '0', '0', '1', '2025-09-08 16:42:30', '1', '2025-09-08 16:42:30', '');
INSERT INTO `sys_dept` VALUES (4, 1, '0,1', '财务部门', 0, NULL, '', '', '0', '0', '1', '2025-09-08 16:42:49', '1', '2025-09-08 16:42:58', '');
INSERT INTO `sys_dept` VALUES (5, 1, '0,1', '人力资源', 0, NULL, '', '', '0', '0', '1', '2025-09-08 16:43:20', '1', '2025-09-08 16:48:13', '');
INSERT INTO `sys_dept` VALUES (6, 1, '0,1', '行政部门', 0, NULL, '', '', '0', '0', '1', '2025-09-08 16:56:20', '1', '2025-09-08 16:56:20', '');
INSERT INTO `sys_dept` VALUES (7, 2, '0,1,2', '前端开发组', 0, NULL, '', '', '0', '0', '1', '2025-09-08 17:15:02', '1', '2025-09-08 17:15:02', NULL);
INSERT INTO `sys_dept` VALUES (8, 2, '0,1,2', '后端开发组', 0, NULL, '', '', '0', '0', '1', '2025-09-08 17:15:08', '1', '2025-09-08 17:15:08', NULL);
INSERT INTO `sys_dept` VALUES (9, 2, '0,1,2', '测试组', 0, NULL, '', '', '0', '0', '1', '2025-09-08 17:15:14', '1', '2025-09-08 17:15:14', NULL);
INSERT INTO `sys_dept` VALUES (10, 2, '0,1,2', '运维组', 0, NULL, '', '', '0', '0', '1', '2025-09-08 17:15:21', '1', '2025-09-08 17:15:21', NULL);
INSERT INTO `sys_dept` VALUES (11, 3, '0,1,3', '国内市场组', 0, NULL, '', '', '0', '0', '1', '2025-09-08 17:15:33', '1', '2025-09-08 17:15:33', NULL);
INSERT INTO `sys_dept` VALUES (12, 3, '0,1,3', '海外市场组', 0, NULL, '', '', '0', '0', '1', '2025-09-08 17:15:39', '1', '2025-09-08 17:15:39', NULL);
INSERT INTO `sys_dept` VALUES (13, 3, '0,1,3', '市场策划组', 0, NULL, '', '', '0', '0', '1', '2025-09-08 17:15:44', '1', '2025-09-08 17:15:44', NULL);
INSERT INTO `sys_dept` VALUES (14, 4, '0,1,4', '会计组', 0, NULL, '', '', '0', '0', '1', '2025-09-08 17:15:56', '1', '2025-09-08 17:15:56', NULL);
INSERT INTO `sys_dept` VALUES (15, 4, '0,1,4', '出纳组', 0, NULL, '', '', '0', '0', '1', '2025-09-08 17:16:09', '1', '2025-09-08 17:16:09', NULL);
INSERT INTO `sys_dept` VALUES (16, 7, '0,1,2,7', 'PC端组', 0, NULL, '', '', '0', '0', '1', '2025-09-08 17:16:35', '1', '2025-09-08 17:16:35', NULL);
INSERT INTO `sys_dept` VALUES (17, 7, '0,1,2,7', '移动端组', 0, NULL, '', '', '0', '0', '1', '2025-09-08 17:16:44', '1', '2025-09-08 17:16:44', NULL);
INSERT INTO `sys_dept` VALUES (18, 8, '0,1,2,8', 'Java组', 0, NULL, '', '', '0', '0', '1', '2025-09-08 17:17:00', '1', '2025-09-08 17:17:00', NULL);
INSERT INTO `sys_dept` VALUES (19, 8, '0,1,2,8', 'Python组', 0, NULL, '', '', '0', '0', '1', '2025-09-08 17:17:08', '1', '2025-09-08 17:17:08', NULL);
INSERT INTO `sys_dept` VALUES (20, 8, '0,1,2,8', 'Go组', 0, NULL, '', '', '0', '0', '1', '2025-09-08 17:17:13', '1', '2025-09-08 17:17:13', NULL);

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `dict_code` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int(4) NULL DEFAULT 0 COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 136 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '字典数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (100, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', '2025-05-11 12:10:28', '', NULL, '性别男');
INSERT INTO `sys_dict_data` VALUES (101, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '2025-05-11 12:10:28', '', NULL, '性别女');
INSERT INTO `sys_dict_data` VALUES (102, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', 'admin', '2025-05-11 12:10:28', '', NULL, '性别未知');
INSERT INTO `sys_dict_data` VALUES (103, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '2025-05-11 12:10:28', '', NULL, '显示菜单');
INSERT INTO `sys_dict_data` VALUES (104, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '2025-05-11 12:10:28', '', NULL, '隐藏菜单');
INSERT INTO `sys_dict_data` VALUES (105, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '2025-05-11 12:10:28', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (106, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '2025-05-11 12:10:28', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (107, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', '2025-05-11 12:10:28', '', NULL, '系统默认是');
INSERT INTO `sys_dict_data` VALUES (108, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', '2025-05-11 12:10:28', '', NULL, '系统默认否');
INSERT INTO `sys_dict_data` VALUES (109, 0, '正常', '0', 'sys_user_status', '', 'default', 'N', '0', '1', '2025-07-17 22:59:33', '1', '2025-07-17 23:01:13', '');
INSERT INTO `sys_dict_data` VALUES (110, 0, '停用', '1', 'sys_user_status', '', '', 'N', '0', '1', '2025-07-17 22:59:52', '1', '2025-07-17 23:01:17', '');
INSERT INTO `sys_dict_data` VALUES (116, 0, '正常', '0', 'sys_menu_status', '', 'success', 'N', '0', '1', '2025-07-18 12:51:36', '1', '2025-07-18 12:51:36', '');
INSERT INTO `sys_dict_data` VALUES (117, 1, '停用', '1', 'sys_menu_status', '', 'danger', 'N', '0', '1', '2025-07-18 12:54:04', '1', '2025-07-18 12:55:25', '');
INSERT INTO `sys_dict_data` VALUES (118, 0, '正常', '0', 'sys_role_status', '', 'success', 'N', '0', '1', '2025-07-18 13:26:24', '1', '2025-07-18 13:26:24', '');
INSERT INTO `sys_dict_data` VALUES (119, 0, '停用', '1', 'sys_role_status', '', 'danger', 'N', '0', '1', '2025-07-18 13:26:34', '1', '2025-07-18 13:29:19', '');
INSERT INTO `sys_dict_data` VALUES (120, 0, '查询', '0', 'sys_operate_type', '', 'warning', 'N', '0', '1', '2025-07-18 14:04:25', '1', '2025-07-18 14:08:21', '');
INSERT INTO `sys_dict_data` VALUES (121, 0, '新增', '1', 'sys_operate_type', '', 'success', 'N', '0', '1', '2025-07-18 14:04:36', '1', '2025-07-18 14:07:47', '');
INSERT INTO `sys_dict_data` VALUES (122, 0, '修改', '2', 'sys_operate_type', '', 'info', 'N', '0', '1', '2025-07-18 14:04:45', '1', '2025-07-18 14:08:12', '');
INSERT INTO `sys_dict_data` VALUES (123, 0, '删除', '3', 'sys_operate_type', '', 'danger', 'N', '0', '1', '2025-07-18 14:05:45', '1', '2025-07-18 14:08:26', '');
INSERT INTO `sys_dict_data` VALUES (124, 0, '导出', '4', 'sys_operate_type', '', 'warning', 'N', '0', '1', '2025-07-18 14:06:08', '1', '2025-07-18 14:09:20', '');
INSERT INTO `sys_dict_data` VALUES (125, 0, '导入', '5', 'sys_operate_type', '', 'warning', 'N', '0', '1', '2025-07-18 14:06:18', '1', '2025-07-18 14:09:13', '');
INSERT INTO `sys_dict_data` VALUES (126, 0, '清空数据', '6', 'sys_operate_type', '', 'warning', 'N', '0', '1', '2025-07-18 14:06:34', '1', '2025-07-18 14:08:49', '');
INSERT INTO `sys_dict_data` VALUES (127, 0, '其他', '99', 'sys_operate_type', '', 'default', 'N', '0', '1', '2025-07-18 14:06:42', '1', '2025-07-18 14:08:57', '');
INSERT INTO `sys_dict_data` VALUES (128, 0, '成功', '0', 'sys_operate_status', '', 'success', 'N', '0', '1', '2025-07-18 14:18:07', '1', '2025-07-18 14:18:07', '');
INSERT INTO `sys_dict_data` VALUES (129, 0, '失败', '1', 'sys_operate_status', '', 'danger', 'N', '0', '1', '2025-07-18 14:18:17', '1', '2025-07-18 14:18:17', '');
INSERT INTO `sys_dict_data` VALUES (130, 0, '成功', '0', 'sys_loginLog_status', '', 'success', 'N', '0', '1', '2025-07-18 14:40:57', '1', '2025-07-18 14:40:57', '');
INSERT INTO `sys_dict_data` VALUES (131, 0, '失败', '1', 'sys_loginLog_status', '', 'danger', 'N', '0', '1', '2025-07-18 14:41:17', '1', '2025-07-18 14:41:17', '');
INSERT INTO `sys_dict_data` VALUES (132, 1, '默认', 'DEFAULT', 'sys_job_group', '', 'default', 'N', '0', '2', '2025-07-31 18:51:04', '2', '2025-07-31 20:04:48', '');
INSERT INTO `sys_dict_data` VALUES (133, 0, '系统', 'SYSTEM', 'sys_job_group', '', 'warning', 'N', '0', '2', '2025-07-31 18:51:24', '2', '2025-07-31 18:51:24', '');
INSERT INTO `sys_dict_data` VALUES (134, 0, '成功', '0', 'job_execution_status', '', 'success', 'N', '0', '1', '2025-07-18 14:18:07', '1', '2025-08-22 12:02:06', '');
INSERT INTO `sys_dict_data` VALUES (135, 0, '失败', '1', 'job_execution_status', '', 'danger', 'N', '0', '1', '2025-07-18 14:18:07', '1', '2025-08-22 12:08:52', '');

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `dict_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '字典类型',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE INDEX `dict_type`(`dict_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 111 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '字典类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_sex', '0', 'admin', '2025-05-11 12:10:28', '', NULL, '用户性别列表');
INSERT INTO `sys_dict_type` VALUES (2, '菜单状态', 'sys_show_hide', '0', 'admin', '2025-05-11 12:10:28', '', NULL, '菜单状态列表');
INSERT INTO `sys_dict_type` VALUES (3, '系统开关', 'sys_normal_disable', '0', 'admin', '2025-05-11 12:10:28', '', NULL, '系统开关列表');
INSERT INTO `sys_dict_type` VALUES (5, '任务分组', 'sys_job_group', '0', 'admin', '2025-05-11 12:10:28', '', NULL, '任务分组列表');
INSERT INTO `sys_dict_type` VALUES (6, '系统是否', 'sys_yes_no', '0', 'admin', '2025-05-11 12:10:28', '', NULL, '系统是否列表');
INSERT INTO `sys_dict_type` VALUES (100, '用户状态', 'sys_user_status', '0', '1', '2025-07-17 22:56:26', '1', '2025-07-17 22:56:26', '用户状态列表');
INSERT INTO `sys_dict_type` VALUES (101, '菜单状态', 'sys_menu_status', '0', '1', '2025-07-17 23:28:30', '1', '2025-07-17 23:28:30', '菜单状态列表');
INSERT INTO `sys_dict_type` VALUES (105, '角色状态', 'sys_role_status', '0', '1', '2025-07-18 13:25:59', '1', '2025-07-18 13:25:59', '角色状态列表');
INSERT INTO `sys_dict_type` VALUES (106, '数据日志操作状态', 'sys_operate_status', '0', '1', '2025-07-18 14:03:36', '1', '2025-07-18 14:03:36', '数据日志操作状态');
INSERT INTO `sys_dict_type` VALUES (107, '数据日志操作类型', 'sys_operate_type', '0', '1', '2025-07-18 14:03:36', '1', '2025-07-18 14:03:36', '数据日志操作类型');
INSERT INTO `sys_dict_type` VALUES (108, '登录日志状态', 'sys_loginLog_status', '0', '1', '2025-07-18 14:38:56', '1', '2025-07-18 14:38:56', '登录日志状态');
INSERT INTO `sys_dict_type` VALUES (110, '调度日志执行状态', 'job_execution_status', '0', '1', '2025-08-22 12:00:57', '1', '2025-08-22 12:00:57', '调度日志执行状态');

-- ----------------------------
-- Table structure for sys_file
-- ----------------------------
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file`  (
  `file_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '文件唯一标识',
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '文件名称',
  `file_extension` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '文件扩展名',
  `original_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '原始文件名',
  `file_size` bigint(20) NOT NULL COMMENT '文件大小(字节)',
  `file_hash` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '文件哈希值(SHA256)',
  `file_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '文件url',
  `mime_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'MIME类型',
  `encoding` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '文件编码',
  `storage_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '存储路径',
  `storage_type` enum('local','oss','s3','cos','minio') CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT 'local' COMMENT '存储类型',
  `bucket_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '存储桶名称',
  `owner_id` bigint(20) NOT NULL COMMENT '文件所有者ID',
  `status` enum('uploading','active','archived','deleted') CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT 'active' COMMENT '文件状态',
  `is_public` tinyint(1) NULL DEFAULT 0 COMMENT '是否公开',
  `is_encrypted` tinyint(1) NULL DEFAULT 0 COMMENT '是否加密',
  `width` int(11) NULL DEFAULT NULL COMMENT '图片宽度',
  `height` int(11) NULL DEFAULT NULL COMMENT '图片高度',
  `duration` int(11) NULL DEFAULT NULL COMMENT '视频/音频时长(秒)',
  `version_number` int(11) NULL DEFAULT 1 COMMENT '版本号',
  `is_latest_version` tinyint(1) NULL DEFAULT 1 COMMENT '是否最新版本',
  `upload_ip` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '上传IP地址',
  `upload_user_agent` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '上传用户代理',
  `upload_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '上传来源',
  `download_count` int(11) NULL DEFAULT 0 COMMENT '下载次数',
  `view_count` int(11) NULL DEFAULT 0 COMMENT '查看次数',
  `last_accessed_at` timestamp NULL DEFAULT NULL COMMENT '最后访问时间',
  `metadata` json NULL COMMENT '扩展元数据',
  `tags` json NULL COMMENT '文件标签',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '文件描述',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '更新人',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '逻辑删除',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`file_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 94 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job`  (
  `job_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
  `status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '状态（0正常 1暂停）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '备注信息',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`job_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '定时任务调度表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_job
-- ----------------------------
INSERT INTO `sys_job` VALUES (4, 'cpu使用率监控', 'DEFAULT', 'monitorTask.realTimeCpuUsage()', '0/5 * * * * ?', '3', '1', '0', '', '2025-08-19 13:45:23', '2025-08-19 13:45:23');
INSERT INTO `sys_job` VALUES (5, '内存使用率监控', 'SYSTEM', 'monitorTask.realTimeMemUsage()', '0/5 * * * * ?', '3', '1', '0', '', '2025-08-20 15:36:12', '2025-08-20 15:36:12');
INSERT INTO `sys_job` VALUES (6, '刷题待办', 'SYSTEM', 'todoTask.createTodo()', '0 0 * * * ?', '3', '1', '1', '', '2025-09-13 23:19:17', '2025-09-13 23:19:17');

-- ----------------------------
-- Table structure for sys_job_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_job_log`;
CREATE TABLE `sys_job_log`  (
  `job_log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '调用目标字符串',
  `job_message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '日志信息',
  `job_id` bigint(20) NULL DEFAULT NULL COMMENT '任务id',
  `status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
  `exception_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '异常信息',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `stop_time` datetime NULL DEFAULT NULL COMMENT '停止时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 620903 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '定时任务调度日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log`  (
  `info_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '用户账号',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `ipaddr` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '操作系统',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '提示消息',
  `login_time` datetime NULL DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`info_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 775 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '系统访问记录' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '菜单名称',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int(4) NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '路由参数',
  `is_frame` int(1) NULL DEFAULT 1 COMMENT '是否为外链（0组件 1外联 2内链）',
  `is_cache` int(1) NULL DEFAULT 0 COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '逻辑删除',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`) USING BTREE,
  INDEX `index_parent_order`(`parent_id`, `order_num`) USING BTREE,
  INDEX `index_flag`(`del_flag`) USING BTREE,
  INDEX `index_parant_order_delFlag`(`parent_id`, `order_num`, `del_flag`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '菜单权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 0, '/system', '', '', 0, 0, 'M', '0', '0', '', 'SettingOutlined', '', '2025-05-01 16:01:13', '1', '2025-05-04 10:49:27', '0', '');
INSERT INTO `sys_menu` VALUES (2, '用户管理', 1, 1, '/system/user', '/system/user', '', 0, 1, 'C', '0', '0', '', 'UserOutlined', '', '2025-05-01 16:01:16', '2', '2025-08-03 00:48:33', '0', '');
INSERT INTO `sys_menu` VALUES (3, '角色管理', 1, 3, '/system/role', '/system/role', '', 0, 0, 'C', '0', '0', '', 'SolutionOutlined', '', '2025-05-01 16:01:19', '1', '2025-05-10 01:27:36', '0', '');
INSERT INTO `sys_menu` VALUES (4, '菜单管理', 1, 2, '/system/menu', '/system/menu', '', 0, 0, 'C', '0', '0', '', 'MenuOutlined', '', '2025-05-01 16:01:28', '1', '2025-05-10 01:27:25', '0', '');
INSERT INTO `sys_menu` VALUES (5, '百度', 0, 3, 'https://www.baidu.com/', '', '{\"id\":1}', 2, 1, 'C', '0', '0', '', 'GlobalOutlined', '1', '2025-05-02 16:51:57', '1', '2025-08-15 22:54:13', '1', '');
INSERT INTO `sys_menu` VALUES (9, '查询', 2, 0, '', '', '', 0, 1, 'F', '0', '0', 'system:user:query', '', '1', '2025-05-05 01:08:13', '1', '2025-05-05 01:08:13', '0', '');
INSERT INTO `sys_menu` VALUES (10, '数据日志', 1, 7, '/system/dataLog', '/system/dataLog', '', 0, 1, 'C', '0', '0', '', 'DatabaseOutlined', '1', '2025-05-05 23:32:50', '1', '2025-09-08 00:35:20', '0', '');
INSERT INTO `sys_menu` VALUES (11, '登录日志', 1, 6, '/system/loginLog', '/system/loginLog', '', 0, 1, 'C', '0', '0', '', 'HddOutlined', '2', '2025-05-10 01:04:39', '1', '2025-07-12 20:11:33', '0', '');
INSERT INTO `sys_menu` VALUES (12, '系统监控', 0, 1, '/monitor', '', '', 0, 1, 'M', '0', '0', '', 'MonitorOutlined', '1', '2025-05-10 01:21:51', '1', '2025-05-10 01:21:51', '0', '');
INSERT INTO `sys_menu` VALUES (13, '在线用户', 12, 4, '/monitor/online', '/monitor/online', '', 0, 1, 'C', '0', '0', '', 'TeamOutlined', '1', '2025-05-10 11:19:43', '1', '2025-05-10 11:31:42', '0', '');
INSERT INTO `sys_menu` VALUES (14, '首页', 16, -1, '/home', '/dashboard', '', 0, 1, 'C', '0', '0', '', 'HomeOutlined', '1', '2025-05-21 23:53:30', '1', '2025-07-08 13:26:27', '0', '');
INSERT INTO `sys_menu` VALUES (15, '菜单', 14, 0, '/system/dataLog', '/system/dataLog', '', 0, 1, 'C', '0', '0', '', '', '1', '2025-05-21 23:53:53', '1', '2025-05-22 00:10:43', '1', '');
INSERT INTO `sys_menu` VALUES (16, '仪表板', 0, -1, '/DashboardOutlined', '', '', 0, 1, 'M', '0', '0', '', 'DashboardOutlined', '1', '2025-07-08 13:24:27', '1', '2025-07-08 13:26:11', '0', '');
INSERT INTO `sys_menu` VALUES (17, '服务监控', 12, 0, '/monitor/server', '/monitor/server', '', 0, 1, 'C', '0', '0', '', 'PieChartOutlined', '1', '2025-07-09 22:24:49', '1', '2025-07-09 22:25:31', '0', '');
INSERT INTO `sys_menu` VALUES (18, '文件管理', 1, 6, '/system/file', '/system/file', '', 0, 1, 'C', '0', '0', '', 'FileSearchOutlined', '1', '2025-07-10 12:20:48', '1', '2025-09-08 00:35:11', '0', '');
INSERT INTO `sys_menu` VALUES (19, '系统配置', 1, 8, '/system/config', '/system/config', '', 0, 1, 'C', '0', '0', '', 'SettingOutlined', '1', '2025-07-12 20:10:49', '1', '2025-09-08 00:35:28', '0', '');
INSERT INTO `sys_menu` VALUES (20, '接口文档', 1, 9, 'http://120.27.215.0:5555/api/doc.html', '', '', 2, 1, 'C', '0', '0', '', 'AlignCenterOutlined', '1', '2025-07-13 22:35:24', '1', '2025-09-08 00:35:33', '0', '');
INSERT INTO `sys_menu` VALUES (21, 'demo', 0, 999, '/demo', '/demo', '', 0, 1, 'C', '0', '0', '', 'BarsOutlined', '1', '2025-07-13 22:41:52', '1', '2025-07-18 15:09:23', '1', '');
INSERT INTO `sys_menu` VALUES (22, '文件分享', 0, 0, '/fileShare', '/fileShare', '', 0, 1, 'C', '0', '0', '', '', '1', '2025-07-16 18:38:57', '1', '2025-07-16 18:40:09', '1', '');
INSERT INTO `sys_menu` VALUES (23, '字典管理', 1, 5, '/system/dict', '/system/dict', '', 0, 1, 'C', '0', '0', '', 'ContainerOutlined', '1', '2025-07-17 19:27:51', '1', '2025-09-08 00:35:07', '0', '');
INSERT INTO `sys_menu` VALUES (24, '任务调度', 12, 1, '/monitor/job', '/monitor/job', '', 0, 1, 'C', '0', '0', '', 'FieldTimeOutlined', '1', '2025-07-25 00:56:15', '1', '2025-07-25 00:56:15', '0', '');
INSERT INTO `sys_menu` VALUES (25, '分析页', 16, 0, '/analysis', '/analysis', '', 0, 1, 'C', '0', '0', '', 'FundProjectionScreenOutlined', '2', '2025-08-02 23:25:40', '2', '2025-08-02 23:25:40', '0', '');
INSERT INTO `sys_menu` VALUES (26, 'demo', 0, 111, '/demo', '/demo', '', 0, 1, 'C', '0', '0', '', 'UnorderedListOutlined', '2', '2025-08-10 12:54:39', '1', '2025-08-22 16:47:25', '1', '');
INSERT INTO `sys_menu` VALUES (27, '个人中心', 0, 2, '/personal ', '', '', 0, 1, 'M', '0', '0', '', 'UsergroupDeleteOutlined', '1', '2025-08-15 14:17:13', '1', '2025-08-15 16:16:03', '0', '');
INSERT INTO `sys_menu` VALUES (28, '个人资料', 27, 1, '/profile', '/profile', '', 0, 1, 'C', '0', '0', '', 'UserOutlined', '1', '2025-08-15 14:21:07', '1', '2025-08-15 14:21:07', '0', '');
INSERT INTO `sys_menu` VALUES (29, 'demo', 0, 9999, '/demo', '/demo', '', 0, 1, 'C', '0', '0', '', 'TrademarkCircleOutlined', '1', '2025-09-01 15:15:26', '1', '2025-09-01 15:15:40', '0', '');
INSERT INTO `sys_menu` VALUES (30, '部门管理', 1, 4, '/system/dept', '/system/dept', '', 0, 1, 'C', '0', '0', '', 'DeploymentUnitOutlined', '1', '2025-09-07 23:06:52', '1', '2025-09-07 23:17:09', '0', '');
INSERT INTO `sys_menu` VALUES (31, '消息', 0, 9999, '/system/message', '/system/message', '', 0, 1, 'C', '1', '0', '', 'MessageOutlined', '1', '2025-09-10 13:32:39', '1', '2025-09-12 12:40:32', '0', '');
INSERT INTO `sys_menu` VALUES (32, '待办', 0, 9999, '/system/todo', '/system/todo', '', 0, 1, 'C', '1', '0', '', 'PicLeftOutlined', '1', '2025-09-10 13:38:39', '1', '2025-09-12 12:40:22', '0', '');

-- ----------------------------
-- Table structure for sys_message
-- ----------------------------
DROP TABLE IF EXISTS `sys_message`;
CREATE TABLE `sys_message`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `sender_id` bigint(20) NOT NULL COMMENT '发送者用户ID',
  `sender` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发送者名称',
  `receiver_id` bigint(20) NOT NULL COMMENT '接收者用户ID',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息内容',
  `type` tinyint(4) NOT NULL DEFAULT 1 COMMENT '消息类型（1：文本，2：图片，3：文件）',
  `read` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已读（0：未读，1：已读）',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发送者头像',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `del_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标志（0代表存在 1代表删除）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sender_id`(`sender_id`) USING BTREE,
  INDEX `idx_receiver_id`(`receiver_id`) USING BTREE,
  INDEX `idx_read`(`read`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '个人消息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_notification
-- ----------------------------
DROP TABLE IF EXISTS `sys_notification`;
CREATE TABLE `sys_notification`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '通知ID',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '通知标题',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '通知内容',
  `type` tinyint(4) NOT NULL DEFAULT 1 COMMENT '通知类型（1：系统通知，2：安全提醒，3：活动通知）',
  `user_id` bigint(20) NOT NULL COMMENT '接收用户ID',
  `read` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已读（0：未读，1：已读）',
  `color` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '通知颜色（用于前端显示）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `del_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标志（0代表存在 1代表删除）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_read`(`read`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统通知表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_notification
-- ----------------------------
INSERT INTO `sys_notification` VALUES (1, '1', '1', 1, 1, 1, '#1890ff', '1', '2025-09-13 00:50:17', '1', '2025-09-13 01:04:16', NULL, 1);
INSERT INTO `sys_notification` VALUES (2, '删除', 'asd', 1, 1, 1, '#1890ff', '1', '2025-09-13 00:55:25', '1', '2025-09-13 01:04:16', NULL, 1);
INSERT INTO `sys_notification` VALUES (3, '冲冲冲', '请问请问', 1, 1, 1, '#1890ff', '1', '2025-09-13 13:10:41', '1', '2025-09-14 23:59:28', NULL, 0);
INSERT INTO `sys_notification` VALUES (4, '111', '123', 1, 1, 0, '#1890ff', '2', '2025-09-19 20:52:57', '2', '2025-09-19 20:52:57', NULL, 0);
INSERT INTO `sys_notification` VALUES (5, '测试', '精通 MySQL（事务、索引、SQL 优化），熟悉 Redis 数据结构与持久化机制，具备性能调优经验。', 1, 1, 1, '#1890ff', '2', '2025-09-19 23:20:50', '2', '2025-09-19 23:21:08', NULL, 0);

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`  (
  `oper_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '模块标题',
  `business_type` int(2) NULL DEFAULT 0 COMMENT '业务类型（0查询 1新增 2修改 3删除 4其他）',
  `method` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '请求方式',
  `operator_type` int(1) NULL DEFAULT 0 COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '返回参数',
  `status` int(1) NULL DEFAULT 0 COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime NULL DEFAULT NULL COMMENT '操作时间',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '操作系统',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '浏览器',
  `cost_time` bigint(10) NULL DEFAULT NULL COMMENT '耗费时间',
  `user_agent` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户代理',
  PRIMARY KEY (`oper_id`) USING BTREE,
  INDEX `idx_ip_user_agent`(`oper_ip`, `user_agent`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28421 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '操作日志记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '角色权限字符串',
  `role_sort` int(4) NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '部门树选择项是否关联显示',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '角色信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'SuperAdmin', 0, '1', 1, 1, '0', '0', '', NULL, '1', '2025-07-08 13:47:08', NULL);
INSERT INTO `sys_role` VALUES (2, '管理员', 'admin', 2, '1', 1, 1, '0', '0', '', NULL, '1', '2025-07-07 21:28:06', NULL);
INSERT INTO `sys_role` VALUES (3, '普通用户', 'user', 3, '1', 1, 1, '1', '0', '1', '2025-05-04 18:09:03', '1', '2025-07-18 13:27:46', NULL);
INSERT INTO `sys_role` VALUES (4, '游客', 'aaa', 0, '1', 1, 1, '0', '1', '1', '2025-07-07 21:23:03', '1', '2025-07-07 21:27:48', NULL);
INSERT INTO `sys_role` VALUES (5, 'test1', 'test1', 5, '1', 1, 1, '0', '1', '1', '2025-07-07 21:24:07', '1', '2025-07-07 21:27:18', NULL);

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '角色和部门关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE,
  INDEX `index_role`(`role_id`) USING BTREE,
  INDEX `index_menu`(`menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '角色和菜单关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1, 1);
INSERT INTO `sys_role_menu` VALUES (1, 2);
INSERT INTO `sys_role_menu` VALUES (1, 3);
INSERT INTO `sys_role_menu` VALUES (1, 4);
INSERT INTO `sys_role_menu` VALUES (1, 9);
INSERT INTO `sys_role_menu` VALUES (1, 10);
INSERT INTO `sys_role_menu` VALUES (1, 11);
INSERT INTO `sys_role_menu` VALUES (1, 12);
INSERT INTO `sys_role_menu` VALUES (1, 13);
INSERT INTO `sys_role_menu` VALUES (1, 14);
INSERT INTO `sys_role_menu` VALUES (1, 16);
INSERT INTO `sys_role_menu` VALUES (1, 17);
INSERT INTO `sys_role_menu` VALUES (1, 18);
INSERT INTO `sys_role_menu` VALUES (1, 19);
INSERT INTO `sys_role_menu` VALUES (1, 20);
INSERT INTO `sys_role_menu` VALUES (1, 23);
INSERT INTO `sys_role_menu` VALUES (1, 24);
INSERT INTO `sys_role_menu` VALUES (1, 25);
INSERT INTO `sys_role_menu` VALUES (1, 27);
INSERT INTO `sys_role_menu` VALUES (1, 28);
INSERT INTO `sys_role_menu` VALUES (1, 30);
INSERT INTO `sys_role_menu` VALUES (1, 31);
INSERT INTO `sys_role_menu` VALUES (1, 32);
INSERT INTO `sys_role_menu` VALUES (2, 1);
INSERT INTO `sys_role_menu` VALUES (2, 2);
INSERT INTO `sys_role_menu` VALUES (2, 9);
INSERT INTO `sys_role_menu` VALUES (2, 14);
INSERT INTO `sys_role_menu` VALUES (2, 16);
INSERT INTO `sys_role_menu` VALUES (2, 18);
INSERT INTO `sys_role_menu` VALUES (2, 27);
INSERT INTO `sys_role_menu` VALUES (2, 28);
INSERT INTO `sys_role_menu` VALUES (2, 30);
INSERT INTO `sys_role_menu` VALUES (2, 31);
INSERT INTO `sys_role_menu` VALUES (2, 32);
INSERT INTO `sys_role_menu` VALUES (3, 14);
INSERT INTO `sys_role_menu` VALUES (3, 16);
INSERT INTO `sys_role_menu` VALUES (3, 25);
INSERT INTO `sys_role_menu` VALUES (3, 27);
INSERT INTO `sys_role_menu` VALUES (3, 28);

-- ----------------------------
-- Table structure for sys_todo
-- ----------------------------
DROP TABLE IF EXISTS `sys_todo`;
CREATE TABLE `sys_todo`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '待办ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '待办标题',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '待办内容',
  `deadline` datetime NULL DEFAULT NULL COMMENT '截止时间',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'pending' COMMENT '状态（pending：待办，completed：已完成，overdue：已逾期）',
  `priority` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'medium' COMMENT '优先级（high：高，medium：中，low：低）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `del_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标志（0代表存在 1代表删除）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_deadline`(`deadline`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '待办事项表' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '用户邮箱',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '手机号码',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '密码',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  `login_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`) USING BTREE,
  INDEX `create_time_Index`(`create_time`) USING BTREE,
  INDEX `user_name_Index`(`user_name`) USING BTREE,
  INDEX `dept_id_index`(`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 1, 'admin', '黎明', '00', '2360187899@qq.com', '18827574649', '0', 'http://120.27.215.0:8081/files/970c267a-ada5-4096-a709-4489a16f885c.gif', '$2a$10$ibwEJ94.7HxN9qUVjznLhObPxlegQp8zK/c5IYcwi4hssSn/suqGW', '0', '0', '127.0.0.1', '2025-09-23 22:03:24', '', '2025-04-25 23:52:07', '1', '2025-07-17 19:29:06', NULL);
INSERT INTO `sys_user` VALUES (2, 2, 'test1', '金明6156', '00', '123@qq.com', '18827574648', '0', '', '$2a$10$dXYPhRb/73MOJbaNwc9.M.tHDfJ7O2XovVz0t0hoMJBZd6/3eGEUe', '0', '0', '171.83.67.157', '2025-09-19 21:07:00', '1', '2025-04-28 16:07:10', '2', '2025-09-19 21:06:57', NULL);
INSERT INTO `sys_user` VALUES (3, 100, 'admin001', '快乐熊猫1123', '00', 'admin001@example.com', '13800138001', '0', '', '$2a$10$BpeJMmYNr0bVEkgXjde3MeVZeBDWiMYNgR65wQJ8GnVELi4bfzzWy', '0', '0', '127.0.0.1', '2025-08-18 19:56:58', 'admin', '2025-04-29 13:51:19', '3', '2025-08-17 23:41:54', '管理部门-主管');
INSERT INTO `sys_user` VALUES (4, 6, 'admin002', '聪明狐狸', '00', 'admin002@example.com', '13800138002', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '', NULL, 'admin', '2025-04-29 13:51:19', '1', '2025-09-09 15:58:14', '管理部门-文员');
INSERT INTO `sys_user` VALUES (5, 18, 'tech001', '机智猫头鹰', '00', 'tech001@example.com', '13800138003', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '', NULL, 'admin', '2025-04-29 13:51:19', '1', '2025-09-09 17:25:40', '技术部门-经理');
INSERT INTO `sys_user` VALUES (6, 101, 'tech002', '勇敢小狮子', '00', 'tech002@example.com', '13800138004', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '', NULL, 'admin', '2025-04-29 13:51:19', '', NULL, '技术部门-开发工程师');
INSERT INTO `sys_user` VALUES (7, 101, 'tech003', '温柔蝴蝶', '00', 'tech003@example.com', '13800138005', '1', '', '$2a$10$KNxlUOTOWjpjeguxxebJneDKFvoKAUk0fjPSGJYaiAuZ5RR2AUiWq', '0', '0', '', NULL, 'admin', '2025-04-29 13:51:19', '', NULL, '技术部门-测试工程师');
INSERT INTO `sys_user` VALUES (8, 6, 'market001', '活泼小兔', '00', 'market001@example.com', '13800138006', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '', NULL, 'admin', '2025-04-29 13:51:19', '1', '2025-09-09 17:34:13', '市场部门-经理');
INSERT INTO `sys_user` VALUES (9, 5, 'market002', '阳光长颈鹿', '00', 'market002@example.com', '13800138007', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '', NULL, 'admin', '2025-04-29 13:51:19', '1', '2025-09-09 17:34:22', '市场部门-销售主管');
INSERT INTO `sys_user` VALUES (10, 10, 'market003', '可爱小熊', '00', 'market003@example.com', '13800138008', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '', NULL, 'admin', '2025-04-29 13:51:19', '1', '2025-09-09 17:34:31', '市场部门-销售专员');
INSERT INTO `sys_user` VALUES (11, 15, 'finance001', '睿智海豚', '00', 'finance001@example.com', '13800138009', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '', NULL, 'admin', '2025-04-29 13:51:19', '1', '2025-09-09 17:34:46', '财务部门-经理');
INSERT INTO `sys_user` VALUES (12, 103, 'finance002', '文静小鹿', '00', 'finance002@example.com', '13800138010', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '1', '', NULL, 'admin', '2025-04-29 13:51:19', '1', '2025-07-06 01:03:19', '财务部门-会计');
INSERT INTO `sys_user` VALUES (13, 11, 'test001', '红色凤凰', '00', 'test001@example.com', '13800138011', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '1', '0', '', NULL, 'admin', '2025-04-29 13:51:19', '1', '2025-09-09 17:34:55', '测试账号-已停用');
INSERT INTO `sys_user` VALUES (14, 100, 'test002', '蓝色海豹', '00', 'test002@example.com', '13800138012', '2', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '1', '', NULL, 'admin', '2025-04-29 13:51:19', '1', '2025-07-06 01:03:19', '测试账号-已删除');
INSERT INTO `sys_user` VALUES (15, 102, 'test1111', 'sda', '00', '123@qq.com', '18827574648', '0', '', '$2a$10$HvJG3LntwHv9I2B5pV./oOsIOzIZvE9ZtudHqT1vRmbfjymkzmkgm', '0', '1', '127.0.0.1', '2025-07-06 00:54:56', '1', '2025-07-06 00:54:35', '1', '2025-07-06 00:56:03', NULL);
INSERT INTO `sys_user` VALUES (16, NULL, 'test1234', 'sda', '00', '123@qq.com', '18827574649', '2', '', '$2a$10$cEeYGttU5FI0fCbOYalSuOnhxMIvjPyubY.5l5lL6PXck9/PqbDF2', '0', '0', '', NULL, '1', '2025-07-17 23:42:59', '1', '2025-07-17 23:43:15', NULL);
INSERT INTO `sys_user` VALUES (17, 1, 'test11111111', 'sda', '00', '123@qq.com', '18827574649', '0', '', '$2a$10$DumOboz3nf1z0I3NlbX8OO4bW.WybYQyqa6rGvaxeewdadk9fNYWa', '0', '1', '', NULL, '1', '2025-08-15 01:47:09', '1', '2025-08-15 01:47:16', NULL);
INSERT INTO `sys_user` VALUES (18, 1, 'admin2', '黎明', '00', '123@qq.com', '18827574648', '0', '', '$2a$10$71HTbnB4O9lPByx8ZDQnwuKvFUrTgUPnFHy0jQzGaLnQPBaMJrZli', '0', '1', '', NULL, '1', '2025-08-15 02:37:19', '1', '2025-08-15 02:37:49', NULL);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE,
  INDEX `index_user`(`user_id`) USING BTREE,
  INDEX `index_role`(`role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户和角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);
INSERT INTO `sys_user_role` VALUES (1, 2);
INSERT INTO `sys_user_role` VALUES (2, 2);
INSERT INTO `sys_user_role` VALUES (3, 3);
INSERT INTO `sys_user_role` VALUES (4, 3);
INSERT INTO `sys_user_role` VALUES (5, 3);
INSERT INTO `sys_user_role` VALUES (8, 3);
INSERT INTO `sys_user_role` VALUES (9, 3);
INSERT INTO `sys_user_role` VALUES (10, 3);
INSERT INTO `sys_user_role` VALUES (11, 3);
INSERT INTO `sys_user_role` VALUES (13, 3);
INSERT INTO `sys_user_role` VALUES (16, 3);
INSERT INTO `sys_user_role` VALUES (17, 2);
INSERT INTO `sys_user_role` VALUES (17, 3);

SET FOREIGN_KEY_CHECKS = 1;
