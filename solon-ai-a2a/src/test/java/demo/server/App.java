package demo.server;

import org.noear.solon.Solon;

/**
 *
 * @author noear 2025/8/30 created
 *
 */
public class App {
    public static void main(String[] args) {
        Solon.start(App.class, new String[]{"--server.port=9999"});
    }
}
