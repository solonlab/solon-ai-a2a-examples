package com.a2a.server2.controller;

import org.noear.solon.ai.a2a.server.A2AServer;
import org.noear.solon.ai.a2a.server.A2AServerEndpoint;
import org.noear.solon.annotation.*;

/**
 * A2A REST controller for handling JSON-RPC requests
 */
@Controller
public class A2AController extends A2AServerEndpoint {
    public A2AController(A2AServer server) {
        super(server);
    }
}