package de.hydracloud.cloudbridge.network.packet.impl.normal;

import de.hydracloud.cloudbridge.network.packet.CloudPacket;
import de.hydracloud.cloudbridge.network.packet.data.PacketData;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PlayerKickPacket extends CloudPacket {

    private String player;
    private String reason;

    public PlayerKickPacket(String player, String reason) {
        this.player = player;
        this.reason = reason;
    }

    @Override
    protected void encodePayload(PacketData packetData) {
        super.encodePayload(packetData);
        packetData.write(player);
        packetData.write(reason);
    }

    @Override
    protected void decodePayload(PacketData packetData) {
        super.decodePayload(packetData);
        player = packetData.readString();
        reason = packetData.readString();
    }

    @Override
    public void handle() {
        ProxiedPlayer player;
        if ((player = ProxyServer.getInstance().getPlayer(this.player)) != null) player.disconnect((CharSequence) reason);
    }

}
