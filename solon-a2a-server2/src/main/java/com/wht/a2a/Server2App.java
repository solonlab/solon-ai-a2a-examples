package com.wht.a2a;

import org.noear.solon.Solon;
import org.noear.solon.annotation.SolonMain;

@SolonMain
public class Server2App {
    public static void main(String[] args) {
        Solon.start(Server2App.class, args);
    }
}