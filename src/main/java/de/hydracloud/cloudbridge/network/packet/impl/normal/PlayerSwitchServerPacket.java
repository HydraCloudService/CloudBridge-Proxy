package de.hydracloud.cloudbridge.network.packet.impl.normal;

import de.hydracloud.cloudbridge.network.packet.CloudPacket;
import de.hydracloud.cloudbridge.network.packet.data.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PlayerSwitchServerPacket extends CloudPacket {

    private String player;
    private String newServer;

    public PlayerSwitchServerPacket(String player, String newServer) {
        this.player = player;
        this.newServer = newServer;
    }

    @Override
    protected void encodePayload(PacketData packetData) {
        super.encodePayload(packetData);
        packetData.write(player);
        packetData.write(newServer);
    }

    @Override
    protected void decodePayload(PacketData packetData) {
        super.decodePayload(packetData);
        player = packetData.readString();
        newServer = packetData.readString();
    }

    @Override
    public void handle() {}

}
