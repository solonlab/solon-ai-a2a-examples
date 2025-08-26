package io.a2a.spec;

/**
 * Defines the base structure for any JSON-RPC 2.0 request, response, or notification.
 */
public  interface JSONRPCMessage {
    String JSONRPC_VERSION = "2.0";

    String getJsonrpc();
    Object getId();

}
