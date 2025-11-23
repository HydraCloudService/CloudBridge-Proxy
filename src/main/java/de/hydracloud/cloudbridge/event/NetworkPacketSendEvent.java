package de.hydracloud.cloudbridge.event;

import de.hydracloud.cloudbridge.network.packet.CloudPacket;
import dev.waterdog.waterdogpe.event.CancellableEvent;
import dev.waterdog.waterdogpe.event.Event;
import lombok.Getter;

@Getter
public class NetworkPacketSendEvent extends Event implements CancellableEvent {

    private final CloudPacket packet;

    public NetworkPacketSendEvent(CloudPacket packet) {
        this.packet = packet;
    }

}