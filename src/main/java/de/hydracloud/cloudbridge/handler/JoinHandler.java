package de.hydracloud.cloudbridge.handler;

import de.hydracloud.cloudbridge.api.CloudAPI;
import de.hydracloud.cloudbridge.api.server.CloudServer;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.network.connection.handler.IJoinHandler;
import dev.waterdog.waterdogpe.network.serverinfo.ServerInfo;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;

public class JoinHandler implements IJoinHandler {

    @Override
    public ServerInfo determineServer(ProxiedPlayer player) {
        CloudServer lobby = CloudAPI.getInstance().getFreeLobby();
        if (lobby == null) return null;
        return ProxyServer.getInstance().getServerInfo(lobby.getName());
    }
}
