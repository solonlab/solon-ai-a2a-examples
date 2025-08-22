package com.wht.a2a;

import org.noear.solon.Solon;
import org.noear.solon.annotation.SolonMain;

@SolonMain
public class ClientApp {
    public static void main(String[] args) {
        Solon.start(ClientApp.class, args);
    }
}