package com.clm.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clm.system.domain.OperLog;
import com.clm.system.domain.param.OperLogQueryParam;
import com.clm.system.domain.vo.OperLogVO;
import org.apache.ibatis.annotations.Param;

/**
 * 操作日志记录(OperLog)表数据库访问层
 *
 * @author 陈黎明
 * @since 2025-03-08
 */
public interface OperLogMapper extends BaseMapper<OperLog> {
    
    /**
     * 分页查询操作日志
     *
     * @param page  分页参数
     * @param param 查询参数
     * @return 分页结果
     */
    IPage<OperLogVO> selectOperLogPage(Page<OperLogVO> page, @Param("param") OperLogQueryParam param);
    
    /**
     * 清空操作日志
     */
    void cleanOperLog();
} 