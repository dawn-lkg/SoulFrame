-- 创建文件表
CREATE TABLE file (
    -- 主键和基本信息
    file_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '文件唯一标识',
    file_name VARCHAR(255) NOT NULL COMMENT '文件名称',
    file_extension VARCHAR(20) COMMENT '文件扩展名',
    original_name VARCHAR(255) NOT NULL COMMENT '原始文件名',
    
    -- 文件属性
    file_size BIGINT NOT NULL COMMENT '文件大小(字节)',
    file_hash VARCHAR(64) NOT NULL COMMENT '文件哈希值(SHA256)',
    mime_type VARCHAR(100) COMMENT 'MIME类型',
    encoding VARCHAR(50) COMMENT '文件编码',
    
    -- 存储信息
    storage_path VARCHAR(500) NOT NULL COMMENT '存储路径',
    storage_type ENUM('local', 'oss', 's3', 'cos') DEFAULT 'local' COMMENT '存储类型',
    bucket_name VARCHAR(100) COMMENT '存储桶名称',
    
    owner_id BIGINT NOT NULL COMMENT '文件所有者ID',
    
    -- 文件状态
    status ENUM('uploading', 'active', 'archived', 'deleted') DEFAULT 'active' COMMENT '文件状态',
    is_public BOOLEAN DEFAULT FALSE COMMENT '是否公开',
    is_encrypted BOOLEAN DEFAULT FALSE COMMENT '是否加密',
    
    -- 文件内容信息
    width INT COMMENT '图片宽度',
    height INT COMMENT '图片高度',
    duration INT COMMENT '视频/音频时长(秒)',
    
    -- 版本信息
    version_number INT DEFAULT 1 COMMENT '版本号',
    is_latest_version BOOLEAN DEFAULT TRUE COMMENT '是否最新版本',
    
    -- 上传信息
    upload_ip VARCHAR(45) COMMENT '上传IP地址',
    upload_user_agent TEXT COMMENT '上传用户代理',
    upload_source VARCHAR(50) COMMENT '上传来源',
    
    -- 访问统计
    download_count INT DEFAULT 0 COMMENT '下载次数',
    view_count INT DEFAULT 0 COMMENT '查看次数',
    last_accessed_at TIMESTAMP NULL COMMENT '最后访问时间',
    
    -- 元数据
    metadata JSON COMMENT '扩展元数据',
    tags JSON COMMENT '文件标签',
    description TEXT COMMENT '文件描述',
    
    -- 时间戳
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(64) COMMENT '创建人',
    update_by VARCHAR(64) COMMENT '更新人',
    del_flag CHAR(1) DEFAULT '0' COMMENT '逻辑删除'
) COMMENT='文件表';

-- 创建索引
CREATE INDEX idx_file_owner_id ON file(owner_id);
CREATE INDEX idx_file_file_hash ON file(file_hash);
CREATE INDEX idx_file_status ON file(status);
CREATE INDEX idx_file_created_time ON file(created_time); 