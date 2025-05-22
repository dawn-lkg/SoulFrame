package com.clm.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clm.system.domain.SysFile;
import com.clm.system.domain.vo.FileChunkResultVO;
import com.clm.system.domain.vo.FileChunkVO;
import com.clm.system.domain.vo.FileInfoVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件服务接口
 *
 * @author 陈黎明
 */
public interface SysFileService extends IService<SysFile> {

    /**
     * 上传文件
     *
     * @param file       文件
     * @param bucketName 桶名称（可选）
     * @return 文件信息
     */
    FileInfoVO uploadFile(MultipartFile file, String bucketName);

    /**
     * 批量上传文件
     *
     * @param files      文件列表
     * @param bucketName 桶名称（可选）
     * @return 文件信息列表
     */
    List<FileInfoVO> uploadFiles(List<MultipartFile> files, String bucketName);

    /**
     * 获取文件
     *
     * @param fileId 文件ID
     * @return 文件信息
     */
    FileInfoVO getFile(Long fileId);

    /**
     * 获取文件URL
     *
     * @param fileId 文件ID
     * @param expiry 过期时间（分钟）
     * @return 文件URL
     */
    String getFileUrl(Long fileId, Integer expiry);

    /**
     * 删除文件
     *
     * @param fileId 文件ID
     * @return 是否成功
     */
    boolean deleteFile(Long fileId);

    /**
     * 批量删除文件
     *
     * @param fileIds 文件ID列表
     * @return 是否成功
     */
    boolean deleteFiles(List<Long> fileIds);

    /**
     * 根据条件查询文件列表
     *
     * @param sysFile 查询条件
     * @return 文件列表
     */
    List<SysFile> selectFileList(SysFile sysFile);
    
    /**
     * 检查文件分片
     *
     * @param identifier 文件唯一标识
     * @param filename   文件名
     * @param totalChunks 总分片数
     * @param totalSize   文件总大小
     * @return 分片检查结果
     */
    FileChunkResultVO checkChunk(String identifier, String filename, Integer totalChunks, Long totalSize);
    
    /**
     * 上传文件分片
     *
     * @param chunk 分片信息
     * @return 分片上传结果
     */
    FileChunkResultVO uploadChunk(FileChunkVO chunk);
    
    /**
     * 合并文件分片
     *
     * @param identifier 文件唯一标识
     * @param filename   文件名
     * @param totalChunks 总分片数
     * @param totalSize   文件总大小
     * @param contentType 文件类型
     * @param bucketName  存储桶名称（可选）
     * @return 文件信息
     */
    FileInfoVO mergeChunks(String identifier, String filename, Integer totalChunks, Long totalSize, String contentType, String bucketName);
} 