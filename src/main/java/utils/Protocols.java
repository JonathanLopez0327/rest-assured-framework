package utils;

import lombok.Getter;

@Getter
public enum Protocols {
    SSL("SSL"),
    TLS("TLS");

    private final String protocolName;

    Protocols(String protocolName) {
        this.protocolName = protocolName;
    }

}
