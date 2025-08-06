package de.hydracloud.cloudbridge.network.packet.impl.request;

import de.hydracloud.cloudbridge.network.packet.RequestPacket;
import de.hydracloud.cloudbridge.network.packet.data.PacketData;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CloudServerStartRequestPacket extends RequestPacket {

    private String template;
    private int count;

    public CloudServerStartRequestPacket(String template, int count) {
        this.template = template;
        this.count = count;
    }

    @Override
    protected void encodePayload(PacketData packetData) {
        super.encodePayload(packetData);
        packetData.write(template);
        packetData.write(count);
    }

    @Override
    protected void decodePayload(PacketData packetData) {
        super.decodePayload(packetData);
        template = packetData.readString();
        count = packetData.readInt();
    }

    public String getTemplate() {
        return template;
    }

    public int getCount() {
        return count;
    }
}
