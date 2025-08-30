package com.a2a.server2;

import org.noear.snack.core.utils.StringUtil;
import org.noear.solon.ai.annotation.ToolMapping;
import org.noear.solon.annotation.Param;

/**
 * @author haiTao.Wang on 2025/8/21.
 */
public class Server2Tools {

    @ToolMapping(description = "根据天气推荐旅游景点", returnDirect = true)
    public String recommendTourist(@Param(description = "天气") String weather) {
        System.err.println("recommendTourist:" + weather);
        if (StringUtil.isEmpty(weather)) {
            return "请输入天气";
        }

        if (weather.contains("晴")) {
            return "公园、爬山等室外运动";
        }
        return "海洋馆、科技馆等室内运动";
    }

}