package com.a2a.demo;

import io.a2a.solon.integration.A2ASolonPlugin;
import org.noear.solon.Solon;

/**
 *
 * @author noear 2025/8/30 created
 *
 */
public class ClientApp {
    public static void main(String[] args) {
        Solon.start(ClientApp.class, args, app -> {
            app.pluginExclude(A2ASolonPlugin.class);
        });
    }
}
