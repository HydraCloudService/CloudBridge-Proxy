package de.hydracloud.cloudbridge.listener;

import de.hydracloud.cloudbridge.api.CloudAPI;
import de.hydracloud.cloudbridge.api.player.CloudPlayer;
import de.hydracloud.cloudbridge.language.Language;
import de.hydracloud.cloudbridge.network.Network;
import de.hydracloud.cloudbridge.network.packet.impl.normal.PlayerSwitchServerPacket;
import de.hydracloud.cloudbridge.network.packet.impl.normal.PlayerConnectPacket;
import de.hydracloud.cloudbridge.network.packet.impl.normal.PlayerDisconnectPacket;
import de.hydracloud.cloudbridge.network.packet.impl.request.CheckPlayerMaintenanceRequestPacket;
import de.hydracloud.cloudbridge.network.packet.impl.response.CheckPlayerMaintenanceResponsePacket;
import de.hydracloud.cloudbridge.network.request.RequestManager;
import dev.waterdog.waterdogpe.event.defaults.PlayerDisconnectedEvent;
import dev.waterdog.waterdogpe.event.defaults.PlayerLoginEvent;
import dev.waterdog.waterdogpe.event.defaults.ServerTransferRequestEvent;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;

public class EventListener {

    public static void onLogin(PlayerLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        Network.getInstance().sendPacket(new PlayerConnectPacket(new CloudPlayer(player.getName(), player.getAddress().getAddress().getHostAddress() + ":" + player.getAddress().getPort(), player.getXuid(), player.getUniqueId().toString(), null, null)));

        if (CloudAPI.getInstance().getCurrentTemplate() == null) return;
        if (CloudAPI.getInstance().getCurrentTemplate().isMaintenance()) {
            RequestManager.getInstance().sendRequest(new CheckPlayerMaintenanceRequestPacket(player.getName())).then(responsePacket -> {
                CheckPlayerMaintenanceResponsePacket checkPlayerMaintenanceResponsePacket = (CheckPlayerMaintenanceResponsePacket) responsePacket;
                if (!checkPlayerMaintenanceResponsePacket.isValue()) {
                    player.disconnect((CharSequence) Language.current().translate("inGame.template.kick.maintenance"));
                }
            });
        }
    }

    public static void onDisconnected(PlayerDisconnectedEvent event) {
        ProxiedPlayer player = event.getPlayer();
        Network.getInstance().sendPacket(new PlayerDisconnectPacket(player.getName()));
    }

    public static void onTransfer(ServerTransferRequestEvent event) {
        Network.getInstance().sendPacket(new PlayerSwitchServerPacket(event.getPlayer().getName(), event.getTargetServer().getServerName()));
    }
}
