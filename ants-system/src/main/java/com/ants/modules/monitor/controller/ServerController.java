package com.ants.modules.monitor.controller;

import com.ants.common.system.result.Result;
import com.ants.modules.monitor.service.Server;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO 服务器监控
 * Author Chen
 * Date   2021/3/25 10:25
 */
@RestController
@RequestMapping("/monitor")
public class ServerController {
    /**
     * 获取服务器监控信息
     * @param
     * @return com.company.api.base.ResultT<com.company.api.server.Server>
     * @author chen
     * @since 2020/7/14
     */
    @GetMapping("/server")
    public Result<?> getInfo() throws Exception {
        Server server = new Server();
        server.copyTo();
        return Result.ok(server);
    }
}
