package com.clm.web.controller;

import com.clm.common.core.controller.BaseController;
import com.clm.common.core.domain.Result;
import com.clm.common.enums.BusinessType;
import com.clm.framework.annotation.Log;
import com.clm.system.domain.entity.Server;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 陈黎明
 * @Date 2025/7/9 23:29
 * @since 2025-03-08
 */
@Tag(name = "服务器管理")
@RestController
@RequestMapping("/monitor/server")
public class MonitorServerController extends BaseController {

    @Log(businessType = BusinessType.QUERY)
    @Operation(summary = "获取服务器信息")
    @GetMapping
    public Result<Server> getServerInfo() throws Exception {
        Server server = new Server();
        server.copyTo();
        return success(server);
    }
}
