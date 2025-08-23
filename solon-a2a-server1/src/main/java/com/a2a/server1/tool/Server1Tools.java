package com.a2a.server1.tool;

import org.noear.solon.ai.annotation.ToolMapping;
import org.noear.solon.annotation.Param;

/**
 * @author haiTao.Wang on 2025/8/21.
 */
public class Server1Tools {

    @ToolMapping(description = "查询天气预报", returnDirect = true)
    public String getWeather(@Param(description = "城市位置") String location) {
        System.err.println("查询天气预报：" + location);
        return location + "天气晴";
    }

    @ToolMapping(description = "查询温度")
    public String getTemperature(@Param(description = "城市位置") String location) {
        System.err.println("查询温度：" + location);
        return location + "温度14度";
    }
}