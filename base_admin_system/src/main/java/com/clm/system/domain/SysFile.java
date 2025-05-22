package com.clm.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文件信息表实体类
 *
 * @author 陈黎明
 */
@Data
@TableName("sys_file")
public class SysFile implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 文件ID
     */
    @TableId(type = IdType.AUTO)
    private Long fileId;

    /**
     * 文件原始名称
     */
    private String fileName;

    /**
     * 文件存储名称
     */
    private String fileKey;

    /**
     * 存储桶名称
     */
    private String bucketName;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;

    /**
     * 文件类型/扩展名
     */
    private String fileType;

    /**
     * 文件MIME类型
     */
    private String mimeType;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 文件来源（上传者类型）：1-用户上传 2-系统生成
     */
    private Integer fileSource;

    /**
     * 文件状态：0-暂存 1-正常 2-禁用
     */
    private Integer status;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String remark;
} 