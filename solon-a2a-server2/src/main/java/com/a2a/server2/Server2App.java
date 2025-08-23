package com.a2a.server2;

import org.noear.solon.Solon;
import org.noear.solon.annotation.SolonMain;

@SolonMain
public class Server2App {
    public static void main(String[] args) {
        Solon.start(Server2App.class, args);
    }
}