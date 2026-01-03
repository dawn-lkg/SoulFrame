-- 系统通知表
CREATE TABLE IF NOT EXISTS `sys_notification`
(
    `id`
    bigint
(
    20
) NOT NULL AUTO_INCREMENT COMMENT '通知ID',
    `title` varchar
(
    100
) NOT NULL COMMENT '通知标题',
    `content` varchar
(
    500
) NOT NULL COMMENT '通知内容',
    `type` tinyint
(
    4
) NOT NULL DEFAULT '1' COMMENT '通知类型（1：系统通知，2：安全提醒，3：活动通知）',
    `user_id` bigint
(
    20
) NOT NULL COMMENT '接收用户ID',
    `read` tinyint
(
    1
) NOT NULL DEFAULT '0' COMMENT '是否已读（0：未读，1：已读）',
    `color` varchar
(
    20
) DEFAULT NULL COMMENT '通知颜色（用于前端显示）',
    `create_by` varchar
(
    64
) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar
(
    64
) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `remark` varchar
(
    500
) DEFAULT NULL COMMENT '备注',
    `del_flag` tinyint
(
    1
) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    PRIMARY KEY
(
    `id`
),
    KEY `idx_user_id`
(
    `user_id`
),
    KEY `idx_read`
(
    `read`
),
    KEY `idx_create_time`
(
    `create_time`
)
    ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='系统通知表';

-- 个人消息表
CREATE TABLE IF NOT EXISTS `sys_message`
(
    `id`
    bigint
(
    20
) NOT NULL AUTO_INCREMENT COMMENT '消息ID',
    `sender_id` bigint
(
    20
) NOT NULL COMMENT '发送者用户ID',
    `sender` varchar
(
    100
) NOT NULL COMMENT '发送者名称',
    `receiver_id` bigint
(
    20
) NOT NULL COMMENT '接收者用户ID',
    `content` varchar
(
    500
) NOT NULL COMMENT '消息内容',
    `type` tinyint
(
    4
) NOT NULL DEFAULT '1' COMMENT '消息类型（1：文本，2：图片，3：文件）',
    `read` tinyint
(
    1
) NOT NULL DEFAULT '0' COMMENT '是否已读（0：未读，1：已读）',
    `avatar` varchar
(
    255
) DEFAULT NULL COMMENT '发送者头像',
    `create_by` varchar
(
    64
) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar
(
    64
) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `remark` varchar
(
    500
) DEFAULT NULL COMMENT '备注',
    `del_flag` tinyint
(
    1
) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    PRIMARY KEY
(
    `id`
),
    KEY `idx_sender_id`
(
    `sender_id`
),
    KEY `idx_receiver_id`
(
    `receiver_id`
),
    KEY `idx_read`
(
    `read`
),
    KEY `idx_create_time`
(
    `create_time`
)
    ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='个人消息表';

-- 待办事项表
CREATE TABLE IF NOT EXISTS `sys_todo`
(
    `id`
    bigint
(
    20
) NOT NULL AUTO_INCREMENT COMMENT '待办ID',
    `user_id` bigint
(
    20
) NOT NULL COMMENT '用户ID',
    `title` varchar
(
    100
) NOT NULL COMMENT '待办标题',
    `content` varchar
(
    500
) NOT NULL COMMENT '待办内容',
    `deadline` datetime DEFAULT NULL COMMENT '截止时间',
    `status` varchar
(
    20
) NOT NULL DEFAULT 'pending' COMMENT '状态（pending：待办，completed：已完成，overdue：已逾期）',
    `priority` varchar
(
    20
) NOT NULL DEFAULT 'medium' COMMENT '优先级（high：高，medium：中，low：低）',
    `create_by` varchar
(
    64
) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar
(
    64
) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `remark` varchar
(
    500
) DEFAULT NULL COMMENT '备注',
    `del_flag` tinyint
(
    1
) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    PRIMARY KEY
(
    `id`
),
    KEY `idx_user_id`
(
    `user_id`
),
    KEY `idx_status`
(
    `status`
),
    KEY `idx_deadline`
(
    `deadline`
),
    KEY `idx_create_time`
(
    `create_time`
)
    ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='待办事项表';
