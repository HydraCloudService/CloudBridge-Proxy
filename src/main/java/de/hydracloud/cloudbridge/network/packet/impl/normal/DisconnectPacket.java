package de.hydracloud.cloudbridge.network.packet.impl.normal;

import de.hydracloud.cloudbridge.network.packet.CloudPacket;
import de.hydracloud.cloudbridge.network.packet.data.PacketData;
import de.hydracloud.cloudbridge.network.packet.impl.types.DisconnectReason;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.logger.MainLogger;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DisconnectPacket extends CloudPacket {

    private DisconnectReason disconnectReason;

    public DisconnectPacket(DisconnectReason disconnectReason) {
        this.disconnectReason = disconnectReason;
    }

    @Override
    protected void encodePayload(PacketData packetData) {
        super.encodePayload(packetData);
        packetData.writeDisconnectReason(disconnectReason);
    }

    @Override
    protected void decodePayload(PacketData packetData) {
        super.decodePayload(packetData);
        disconnectReason = packetData.readDisconnectReason();
    }

    @Override
    public void handle() {
        if (this.disconnectReason == DisconnectReason.CLOUD_SHUTDOWN) {
            MainLogger.getLogger().warning("§4Cloud was stopped! Shutdown...");
            ProxyServer.getInstance().shutdown();
        } else {
            MainLogger.getLogger().warning("§4Server shutdown was ordered by the cloud! Shutdown...");
            ProxyServer.getInstance().shutdown();
        }
    }

}
