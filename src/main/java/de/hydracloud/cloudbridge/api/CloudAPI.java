package de.hydracloud.cloudbridge.api;

import de.hydracloud.cloudbridge.api.player.CloudPlayer;
import de.hydracloud.cloudbridge.api.registry.Registry;
import de.hydracloud.cloudbridge.api.server.CloudServer;
import de.hydracloud.cloudbridge.api.server.status.ServerStatus;
import de.hydracloud.cloudbridge.api.template.Template;
import de.hydracloud.cloudbridge.language.Language;
import de.hydracloud.cloudbridge.network.Network;
import de.hydracloud.cloudbridge.network.packet.RequestPacket;
import de.hydracloud.cloudbridge.network.packet.impl.normal.CloudServerSavePacket;
import de.hydracloud.cloudbridge.network.packet.impl.normal.CloudServerStatusChangePacket;
import de.hydracloud.cloudbridge.network.packet.impl.normal.KeepAlivePacket;
import de.hydracloud.cloudbridge.network.packet.impl.request.CloudServerStartRequestPacket;
import de.hydracloud.cloudbridge.network.packet.impl.request.CloudServerStopRequestPacket;
import de.hydracloud.cloudbridge.network.packet.impl.request.ServerHandshakeRequestPacket;
import de.hydracloud.cloudbridge.network.packet.impl.response.ServerHandshakeResponsePacket;
import de.hydracloud.cloudbridge.network.packet.impl.types.VerifyStatus;
import de.hydracloud.cloudbridge.network.request.RequestManager;
import de.hydracloud.cloudbridge.util.GeneralSettings;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.logger.MainLogger;
import dev.waterdog.waterdogpe.network.serverinfo.ServerInfo;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CloudAPI {

    private static CloudAPI instance = null;
    private VerifyStatus verified = VerifyStatus.NOT_APPLIED;

    public CloudAPI() {
        instance = this;
    }

    public void processLogin() {
        if (verified == VerifyStatus.VERIFIED) return;
        RequestManager.getInstance().sendRequest(new ServerHandshakeRequestPacket(GeneralSettings.getServerName(), GeneralSettings.getCloudPassword(), (int) ProcessHandle.current().pid(), ProxyServer.getInstance().getConfiguration().getMaxPlayerCount())).then(responsePacket -> {
            ServerHandshakeResponsePacket loginResponsePacket = (ServerHandshakeResponsePacket) responsePacket;
            if (loginResponsePacket.getVerifyStatus() == VerifyStatus.VERIFIED) {
                MainLogger.getLogger().info(Language.current().translate("inGame.server.verified"));
                verified = VerifyStatus.VERIFIED;
                Network.getInstance().sendPacket(new KeepAlivePacket());
            } else {
                verified = VerifyStatus.DENIED;
                MainLogger.getLogger().error(Language.current().translate("inGame.server.verify.denied"));
                ProxyServer.getInstance().shutdown();
            }
        }).failure(packet -> {
            verified = VerifyStatus.DENIED;
            MainLogger.getLogger().error(Language.current().translate("inGame.server.verify.failed"));
            ProxyServer.getInstance().shutdown();
        });
    }

    public void changeStatus(ServerStatus serverStatus) {
        Network.getInstance().sendPacket(new CloudServerStatusChangePacket(serverStatus));
    }

    public RequestPacket startServer(Template template) {
        return startServer(template, 1);
    }

    public RequestPacket startServer(String template) {
        return startServer(template, 1);
    }

    public RequestPacket startServer(Template template, int count) {
        return startServer(template.getName(), count);
    }

    public RequestPacket startServer(String template, int count) {
        return RequestManager.getInstance().sendRequest(new CloudServerStartRequestPacket(template, count));
    }

    public RequestPacket stopServer(CloudServer server) {
        return stopServer(server.getName());
    }

    public RequestPacket stopServer(String server) {
        return RequestManager.getInstance().sendRequest(new CloudServerStopRequestPacket(server));
    }

    public RequestPacket stopTemplate(Template template) {
        return stopTemplate(template.getName());
    }

    public RequestPacket stopTemplate(String template) {
        return RequestManager.getInstance().sendRequest(new CloudServerStopRequestPacket(template));
    }

    public void saveCurrentServer() {
        Network.getInstance().sendPacket(new CloudServerSavePacket());
    }

    public boolean transferPlayer(ProxiedPlayer player, CloudServer server) {
        CloudPlayer cloudPlayer = getPlayerByName(player.getName());
        if (server.getTemplate().getTemplateType().equals("SERVER")) {
            ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(server.getName());
            if (serverInfo != null) {
                player.connect(serverInfo);
                return true;
            }
        }
        return false;
    }

    public CloudServer getFreeLobby(String... exclude) {
        List<CloudServer> lobbyServices = getServers().stream().filter(service -> service.getTemplate().isLobby() && service.getServerStatus() == ServerStatus.ONLINE).collect(Collectors.toList());
        if (lobbyServices.isEmpty()) return null;
        for (String excludingName : exclude) lobbyServices.removeIf(server -> server.getName().equals(excludingName));
        if (lobbyServices.isEmpty()) return null;
        Collections.shuffle(lobbyServices);
        return lobbyServices.get(0);
    }

    public List<CloudServer> getServersByTemplate(Template template) {
        return getServers().stream().filter(server -> server.getTemplate().getName().equals(template.getName())).collect(Collectors.toList());
    }

    public List<CloudPlayer> getPlayersOfTemplate(Template template) {
        return getPlayers().stream().filter(player -> {
            if (template.getTemplateType().equals("PROXY")) return (player.getCurrentProxy() != null && player.getCurrentProxy().getTemplate().getName().equals(template.getName()));
            else return (player.getCurrentServer() != null && player.getCurrentServer().getTemplate().getName().equals(template.getName()));
        }).collect(Collectors.toList());
    }

    public CloudServer getServerByName(String name) {
        return getServers().stream().filter(server -> server.getName().equals(name)).findFirst().orElse(null);
    }

    public Template getTemplateByName(String name) {
        return getTemplates().stream().filter(template -> template.getName().equals(name)).findFirst().orElse(null);
    }

    public CloudPlayer getPlayerByName(String name) {
        return getPlayers().stream().filter(player -> player.getName().equals(name)).findFirst().orElse(null);
    }

    public CloudPlayer getPlayerByUniqueId(String uniqueId) {
        return getPlayers().stream().filter(player -> player.getUniqueId().equals(uniqueId)).findFirst().orElse(null);
    }

    public CloudPlayer getPlayerByXboxUserId(String xboxUserId) {
        return getPlayers().stream().filter(player -> player.getXboxUserId().equals(xboxUserId)).findFirst().orElse(null);
    }

    public CloudServer getCurrentServer() {
        return getServerByName(GeneralSettings.getServerName());
    }

    public Template getCurrentTemplate() {
        return getTemplateByName(GeneralSettings.getTemplateName());
    }

    public ArrayList<CloudServer> getServers() {
        return Registry.getServers();
    }

    public ArrayList<Template> getTemplates() {
        return Registry.getTemplates();
    }

    public ArrayList<CloudPlayer> getPlayers() {
        return Registry.getPlayers();
    }

    public boolean isVerified() {
        return verified == VerifyStatus.VERIFIED;
    }

    public static CloudAPI getInstance() {
        if (instance == null) instance = new CloudAPI();
        return instance;
    }
}
