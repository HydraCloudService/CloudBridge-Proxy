package de.hydracloud.cloudbridge;

import de.hydracloud.cloudbridge.api.CloudAPI;
import de.hydracloud.cloudbridge.handler.JoinHandler;
import de.hydracloud.cloudbridge.handler.ReconnectHandler;
import de.hydracloud.cloudbridge.language.Language;
import de.hydracloud.cloudbridge.listener.EventListener;
import de.hydracloud.cloudbridge.network.Network;
import de.hydracloud.cloudbridge.network.packet.impl.normal.DisconnectPacket;
import de.hydracloud.cloudbridge.network.packet.impl.types.DisconnectReason;
import de.hydracloud.cloudbridge.task.ChangeStatusTask;
import de.hydracloud.cloudbridge.task.TimeoutTask;
import de.hydracloud.cloudbridge.util.GeneralSettings;
import de.hydracloud.cloudbridge.util.Utils;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.event.defaults.PlayerDisconnectedEvent;
import dev.waterdog.waterdogpe.event.defaults.PlayerLoginEvent;
import dev.waterdog.waterdogpe.event.defaults.ServerTransferRequestEvent;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import dev.waterdog.waterdogpe.plugin.Plugin;
import lombok.Getter;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CloudBridge extends Plugin {

    @Getter
    private static CloudBridge instance;
    @Getter
    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    @Getter
    private Network network;
    public long lastKeepALiveCheck = 0;

    @Override
    public void onStartup() {
        instance = this;
        GeneralSettings.sync();

        try {
            network = new Network(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), GeneralSettings.getNetworkPort()));
        } catch (UnknownHostException e) {
            ProxyServer.getInstance().getLogger().error("Failed to build the network", e);
        }
    }

    @Override
    public void onEnable() {
        ProxyServer.getInstance().getEventManager().subscribe(PlayerLoginEvent.class, EventListener::onLogin);
        ProxyServer.getInstance().getEventManager().subscribe(PlayerDisconnectedEvent.class, EventListener::onDisconnected);
        ProxyServer.getInstance().getEventManager().subscribe(ServerTransferRequestEvent.class, EventListener::onTransfer);

        lastKeepALiveCheck = Utils.time();
        ProxyServer.getInstance().getScheduler().scheduleRepeating(new ChangeStatusTask(), 20);
        ProxyServer.getInstance().getScheduler().scheduleRepeating(new TimeoutTask(), 20);

        ProxyServer.getInstance().setJoinHandler(new JoinHandler());
        ProxyServer.getInstance().setReconnectHandler(new ReconnectHandler());

        CloudAPI.getInstance().processLogin();
    }

    @Override
    public void onDisable() {
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers().values()) player.disconnect((CharSequence) Language.current().translate("inGame.proxy.stopped"));
        network.sendPacket(new DisconnectPacket(DisconnectReason.SERVER_SHUTDOWN));
        network.close();
        threadPool.shutdownNow();
    }
}