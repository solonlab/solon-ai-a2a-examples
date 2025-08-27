package com.a2a.server1.controller;

import io.a2a_preview.server.A2AServer;
import io.a2a_preview.server.A2AServerEndpoint;
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
