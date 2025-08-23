package com.a2a.demo;

import org.noear.solon.Solon;
import org.noear.solon.annotation.SolonMain;

@SolonMain
public class ClientApp {
    public static void main(String[] args) {
        Solon.start(ClientApp.class, args);
    }
}