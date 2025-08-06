package de.hydracloud.cloudbridge.network.packet.impl.normal;

import de.hydracloud.cloudbridge.api.server.status.ServerStatus;
import de.hydracloud.cloudbridge.network.packet.CloudPacket;
import de.hydracloud.cloudbridge.network.packet.data.PacketData;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CloudServerStatusChangePacket extends CloudPacket {

    private ServerStatus newStatus;

    public CloudServerStatusChangePacket(ServerStatus newStatus) {
        this.newStatus = newStatus;
    }

    @Override
    protected void encodePayload(PacketData packetData) {
        super.encodePayload(packetData);
        packetData.writeServerStatus(newStatus);
    }

    @Override
    protected void decodePayload(PacketData packetData) {
        super.decodePayload(packetData);
        newStatus = packetData.readServerStatus();
    }

    @Override
    public void handle() {}

    public ServerStatus getNewStatus() {
        return newStatus;
    }
}
