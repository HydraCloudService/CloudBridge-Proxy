package de.hydracloud.cloudbridge.network;

import de.hydracloud.cloudbridge.CloudBridge;
import de.hydracloud.cloudbridge.event.NetworkCloseEvent;
import de.hydracloud.cloudbridge.event.NetworkConnectEvent;
import de.hydracloud.cloudbridge.event.NetworkPacketReceiveEvent;
import de.hydracloud.cloudbridge.event.NetworkPacketSendEvent;
import de.hydracloud.cloudbridge.network.packet.CloudPacket;
import de.hydracloud.cloudbridge.network.packet.ResponsePacket;
import de.hydracloud.cloudbridge.network.packet.handler.PacketSerializer;
import de.hydracloud.cloudbridge.network.packet.pool.PacketPool;
import de.hydracloud.cloudbridge.network.request.RequestManager;
import de.hydracloud.cloudbridge.util.GeneralSettings;
import de.hydracloud.cloudbridge.util.Utils;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.logger.MainLogger;
import lombok.Getter;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

@Getter
public class Network implements Runnable {

    @Getter
    private static Network instance;

    private final PacketPool packetPool;
    private final RequestManager requestManager;
    private final InetSocketAddress address;

    private DatagramSocket socket;
    private volatile boolean connected = false;

    public Network(InetSocketAddress address) {
        instance = this;
        this.address = address;
        this.packetPool = new PacketPool();
        this.requestManager = new RequestManager();

        MainLogger.getLogger().info("Trying to connect to §e" + address + "§r...");
        connect();
        CloudBridge.getInstance().getThreadPool().submit(this);
    }

    @Override
    public void run() {
        while (connected) {
            try {
                String raw = read();
                if (raw == null) continue;

                CloudPacket packet = PacketSerializer.decode(raw);
                if (packet == null) {
                    MainLogger.getLogger().warning("§cReceived unknown packet from cloud!");
                    String data = GeneralSettings.isNetworkEncryptionEnabled()
                            ? Utils.decompress(raw.getBytes(StandardCharsets.UTF_8))
                            : raw;
                    MainLogger.getLogger().debug(data);
                    continue;
                }

                NetworkPacketReceiveEvent event = new NetworkPacketReceiveEvent(packet);
                ProxyServer.getInstance().getEventManager().callEvent(event);
                if (event.isCancelled()) {
                    MainLogger.getLogger().warning("Packet processing was cancelled.");
                    continue;
                }

                packet.handle();

                if (packet instanceof ResponsePacket response) {
                    RequestManager.getInstance().callThen(response);
                    RequestManager.getInstance().removeRequest(response.getRequestId());
                }

            } catch (Exception e) {
                MainLogger.getLogger().error("§cError while processing a packet!", e);
            }
        }
    }

    public void connect() {
        if (connected) return;
        try {
            ProxyServer.getInstance().getEventManager().callEvent(new NetworkConnectEvent(address));

            socket = new DatagramSocket();
            socket.connect(address);
            socket.setSendBufferSize(8 * 1024 * 1024);
            socket.setReceiveBufferSize(8 * 1024 * 1024);

            connected = true;

            MainLogger.getLogger().info("Successfully connected to §e" + address + "§r!");
            MainLogger.getLogger().info("§cWaiting for incoming packets...");
        } catch (SocketException e) {
            MainLogger.getLogger().error("Failed to connect to §e" + address + "§r!", e);
        }
    }

    public boolean write(String buffer) {
        byte[] bytes = buffer.getBytes(StandardCharsets.UTF_8);
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address);
        try {
            socket.send(packet);
            return true;
        } catch (IOException e) {
            MainLogger.getLogger().error("Failed to send packet", e);
            return false;
        }
    }

    public String read() {
        if (!connected) return null;

        byte[] buffer = new byte[8 * 1024 * 1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        try {
            socket.receive(packet);
            return new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8).trim();
        } catch (IOException e) {
            MainLogger.getLogger().error("Failed to receive packet", e);
            return null;
        }
    }

    public void close() {
        if (!connected) return;
        connected = false;

        ProxyServer.getInstance().getEventManager().callEvent(new NetworkCloseEvent());

        socket.disconnect();
        socket.close();
    }

    public boolean sendPacket(CloudPacket packet) {
        if (!connected) return false;

        try {
            String json = PacketSerializer.encode(packet);

            NetworkPacketSendEvent event = new NetworkPacketSendEvent(packet);
            ProxyServer.getInstance().getEventManager().callEvent(event);

            return !event.isCancelled() && write(json);
        } catch (Exception e) {
            MainLogger.getLogger().error("Failed to send CloudPacket", e);
            return false;
        }
    }

}