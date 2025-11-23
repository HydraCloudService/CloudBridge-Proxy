package de.hydracloud.cloudbridge.event;

import dev.waterdog.waterdogpe.event.Event;
import lombok.Getter;

import java.net.InetSocketAddress;

@Getter
public class NetworkConnectEvent extends Event {

    private final InetSocketAddress address;

    public NetworkConnectEvent(InetSocketAddress address) {
        this.address = address;
    }

}
