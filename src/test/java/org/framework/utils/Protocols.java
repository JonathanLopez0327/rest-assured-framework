package org.framework.utils;

public enum Protocols {
    SSL("SSL"),
    TLS("TLS");

    private final String protocolName;

    Protocols(String protocolName) {
        this.protocolName = protocolName;
    }

    public String getProtocolName() {
        return this.protocolName;
    }
}
