package de.hydracloud.cloudbridge.network.packet;

import de.hydracloud.cloudbridge.network.packet.data.PacketData;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class CloudPacket {

    private boolean encoded = false;

    public void encode(PacketData packetData) {
        if (!encoded) {
            encoded = true;
            packetData.write(getClass().getSimpleName());
            encodePayload(packetData);
        }
    }

    public void decode(PacketData packetData) {
        packetData.readString();
        decodePayload(packetData);
    }

    protected void encodePayload(PacketData packetData) {}

    protected void decodePayload(PacketData packetData) {}

    abstract public void handle();

    public boolean isEncoded() {
        return encoded;
    }
}
