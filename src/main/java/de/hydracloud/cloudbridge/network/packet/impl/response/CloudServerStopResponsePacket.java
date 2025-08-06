package de.hydracloud.cloudbridge.network.packet.impl.response;

import de.hydracloud.cloudbridge.network.packet.ResponsePacket;
import de.hydracloud.cloudbridge.network.packet.data.PacketData;
import de.hydracloud.cloudbridge.network.packet.impl.types.ErrorReason;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CloudServerStopResponsePacket extends ResponsePacket {

    private ErrorReason errorReason;

    public CloudServerStopResponsePacket(ErrorReason errorReason) {
        this.errorReason = errorReason;
    }

    @Override
    protected void encodePayload(PacketData packetData) {
        super.encodePayload(packetData);
        packetData.writeErrorReason(errorReason);
    }

    @Override
    protected void decodePayload(PacketData packetData) {
        super.decodePayload(packetData);
        errorReason = packetData.readErrorReason();
    }

    @Override
    public void handle() {}

    public ErrorReason getErrorReason() {
        return errorReason;
    }
}
