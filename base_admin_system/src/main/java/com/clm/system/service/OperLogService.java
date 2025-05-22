package com.clm.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.clm.system.domain.OperLog;
import com.clm.system.domain.param.OperLogQueryParam;
import com.clm.system.domain.vo.OperLogVO;

import java.util.List;

/**
 * 操作日志记录(OperLog)表服务接口
 *
 * @author 陈黎明
 * @since 2025-03-08
 */
public interface OperLogService extends IService<OperLog> {
    
    /**
     * 分页查询操作日志
     *
     * @param param 查询参数
     * @return 分页结果
     */
    IPage<OperLogVO> pageOperLog(OperLogQueryParam param);
    
    /**
     * 新增操作日志
     *
     * @param operLog 操作日志信息
     * @return 结果
     */
    boolean insertOperLog(OperLog operLog);

    /**
     * 新增操作日志（异步）
     */
    void insertOperLogAsync(OperLog operLog);

    /**
     * 批量删除操作日志
     *
     * @param operIds 需要删除的操作日志ID
     * @return 结果
     */
    boolean deleteOperLogByIds(List<Long> operIds);
    
    /**
     * 查询操作日志详细
     *
     * @param operId 操作ID
     * @return 操作日志对象
     */
    OperLogVO getOperLogById(Long operId);
    
    /**
     * 清空操作日志
     *
     * @return 结果
     */
    boolean cleanOperLog();
    
    /**
     * 导出操作日志
     *
     * @param param 查询条件
     * @return 操作日志列表
     */
    List<OperLogVO> exportOperLog(OperLogQueryParam param);
} 