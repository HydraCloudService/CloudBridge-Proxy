package de.hydracloud.cloudbridge.network.packet.impl.response;

import de.hydracloud.cloudbridge.network.packet.ResponsePacket;
import de.hydracloud.cloudbridge.network.packet.data.PacketData;
import de.hydracloud.cloudbridge.network.packet.impl.types.VerifyStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ServerHandshakeResponsePacket extends ResponsePacket {

    private VerifyStatus verifyStatus;

    public ServerHandshakeResponsePacket(VerifyStatus verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    @Override
    protected void encodePayload(PacketData packetData) {
        super.encodePayload(packetData);
        packetData.writeVerifyStatus(verifyStatus);
    }

    @Override
    protected void decodePayload(PacketData packetData) {
        super.decodePayload(packetData);
        verifyStatus = packetData.readVerifyStatus();
    }

    @Override
    public void handle() {}

}
