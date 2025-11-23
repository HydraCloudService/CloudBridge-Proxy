package de.hydracloud.cloudbridge.network.packet.impl.response;

import de.hydracloud.cloudbridge.network.packet.ResponsePacket;
import de.hydracloud.cloudbridge.network.packet.data.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CheckPlayerMaintenanceResponsePacket extends ResponsePacket {

    private boolean value;

    public CheckPlayerMaintenanceResponsePacket(boolean value) {
        this.value = value;
    }

    @Override
    protected void encodePayload(PacketData packetData) {
        super.encodePayload(packetData);
        packetData.write(value);
    }

    @Override
    protected void decodePayload(PacketData packetData) {
        super.decodePayload(packetData);
        value = packetData.readBool();
    }

    @Override
    public void handle() {}

}
