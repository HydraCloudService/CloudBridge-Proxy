package de.hydracloud.cloudbridge.network.packet.impl.types;

public enum ErrorReason {

    NO_ERROR,
    TEMPLATE_EXISTENCE,
    MAX_SERVERS,
    SERVER_EXISTENCE;

    public static ErrorReason getReasonByName(String name) {
        return valueOf(name);
    }
}
