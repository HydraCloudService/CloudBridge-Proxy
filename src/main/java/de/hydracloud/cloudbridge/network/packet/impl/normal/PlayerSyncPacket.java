package de.hydracloud.cloudbridge.network.packet.impl.normal;

import de.hydracloud.cloudbridge.api.CloudAPI;
import de.hydracloud.cloudbridge.api.player.CloudPlayer;
import de.hydracloud.cloudbridge.api.registry.Registry;
import de.hydracloud.cloudbridge.network.packet.CloudPacket;
import de.hydracloud.cloudbridge.network.packet.data.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PlayerSyncPacket extends CloudPacket {

    private CloudPlayer player;
    private boolean removal;

    public PlayerSyncPacket(CloudPlayer player, boolean removal) {
        this.player = player;
        this.removal = removal;
    }

    @Override
    protected void encodePayload(PacketData packetData) {
        super.encodePayload(packetData);
        packetData.writePlayer(player);
        packetData.write(removal);
    }

    @Override
    protected void decodePayload(PacketData packetData) {
        super.decodePayload(packetData);
        player = packetData.readPlayer();
        removal = packetData.readBool();
    }

    @Override
    public void handle() {
        if (CloudAPI.getInstance().getPlayerByName(player.getName()) == null) {
            if (!removal) Registry.registerPlayer(player);
        } else {
            if (removal) {
                Registry.unregisterPlayer(player.getName());
            } else if (player.getCurrentServer() != null) {
                Registry.updatePlayer(player.getName(), player.getCurrentServer().getName());
            }
        }
    }

}
