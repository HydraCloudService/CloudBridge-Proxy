package de.hydracloud.cloudbridge.event;

import de.hydracloud.cloudbridge.network.packet.CloudPacket;
import dev.waterdog.waterdogpe.event.CancellableEvent;
import dev.waterdog.waterdogpe.event.Event;

public class NetworkPacketReceiveEvent extends Event implements CancellableEvent {

    private final CloudPacket packet;

    public NetworkPacketReceiveEvent(CloudPacket packet) {
        this.packet = packet;
    }

    public CloudPacket getPacket() {
        return packet;
    }
}
