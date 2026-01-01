package de.hydracloud.cloudbridge.network.packet.impl.normal;

import de.hydracloud.cloudbridge.network.packet.CloudPacket;
import de.hydracloud.cloudbridge.network.packet.data.PacketData;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.network.serverinfo.ServerInfo;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PlayerTransferPacket extends CloudPacket {

    private String player;
    private String server;

    public PlayerTransferPacket(String player, String server) {
        this.player = player;
        this.server = server;
    }

    @Override
    protected void encodePayload(PacketData packetData) {
        packetData.write(player);
        packetData.write(server);
    }

    @Override
    protected void decodePayload(PacketData packetData) {
        player = packetData.readString();
        server = packetData.readString();
    }

    @Override
    public void handle() {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(this.player);
        if (player != null) {
            ServerInfo info = ProxyServer.getInstance().getServerInfo(this.server);
            if (info != null) {
                player.connect(info);
            }
        }
    }

}
