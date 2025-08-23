package com.a2a.server1;

import org.noear.solon.Solon;
import org.noear.solon.annotation.SolonMain;

@SolonMain
public class Server1App {
    public static void main(String[] args) {
        Solon.start(Server1App.class, args);
    }
}