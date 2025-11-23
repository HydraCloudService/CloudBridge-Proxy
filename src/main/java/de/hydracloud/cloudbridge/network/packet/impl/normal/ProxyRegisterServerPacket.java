package de.hydracloud.cloudbridge.network.packet.impl.normal;

import de.hydracloud.cloudbridge.network.packet.CloudPacket;
import de.hydracloud.cloudbridge.network.packet.data.PacketData;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.network.serverinfo.BedrockServerInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.InetSocketAddress;

@Getter
@NoArgsConstructor
public class ProxyRegisterServerPacket extends CloudPacket {

    private String serverName;
    private int port;

    public ProxyRegisterServerPacket(String serverName, int port) {
        this.serverName = serverName;
        this.port = port;
    }

    @Override
    protected void encodePayload(PacketData packetData) {
        super.encodePayload(packetData);
        packetData.write(serverName);
        packetData.write(port);
    }

    @Override
    protected void decodePayload(PacketData packetData) {
        super.decodePayload(packetData);
        serverName = packetData.readString();
        port = ((Number) packetData.readDouble()).intValue();
    }

    @Override
    public void handle() {
        ProxyServer.getInstance().registerServerInfo(new BedrockServerInfo(serverName, new InetSocketAddress(port), null));
    }

}
