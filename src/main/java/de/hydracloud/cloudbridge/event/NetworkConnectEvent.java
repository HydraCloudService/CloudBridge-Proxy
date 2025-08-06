package de.hydracloud.cloudbridge.event;

import dev.waterdog.waterdogpe.event.Event;

import java.net.InetSocketAddress;

public class NetworkConnectEvent extends Event {

    private final InetSocketAddress address;

    public NetworkConnectEvent(InetSocketAddress address) {
        this.address = address;
    }

    public InetSocketAddress getAddress() {
        return address;
    }
}
