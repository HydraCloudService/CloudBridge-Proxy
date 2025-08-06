package de.hydracloud.cloudbridge.network.packet.impl.request;

import de.hydracloud.cloudbridge.network.packet.RequestPacket;
import de.hydracloud.cloudbridge.network.packet.data.PacketData;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ServerHandshakeRequestPacket extends RequestPacket {

    private String serverName;
    private int processId;
    private int maxPlayerCount;

    public ServerHandshakeRequestPacket(String serverName, int processId, int maxPlayerCount) {
        this.serverName = serverName;
        this.processId = processId;
        this.maxPlayerCount = maxPlayerCount;
    }

    @Override
    protected void encodePayload(PacketData packetData) {
        super.encodePayload(packetData);
        packetData.write(serverName);
        packetData.write(processId);
        packetData.write(maxPlayerCount);
    }

    @Override
    protected void decodePayload(PacketData packetData) {
        super.decodePayload(packetData);
        serverName = packetData.readString();
        processId = packetData.readInt();
        maxPlayerCount = packetData.readInt();
    }

    public String getServerName() {
        return serverName;
    }
}
