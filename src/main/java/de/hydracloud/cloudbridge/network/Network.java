package de.hydracloud.cloudbridge.network;

import de.hydracloud.cloudbridge.CloudBridge;
import de.hydracloud.cloudbridge.event.NetworkCloseEvent;
import de.hydracloud.cloudbridge.event.NetworkConnectEvent;
import de.hydracloud.cloudbridge.event.NetworkPacketReceiveEvent;
import de.hydracloud.cloudbridge.event.NetworkPacketSendEvent;
import de.hydracloud.cloudbridge.network.packet.CloudPacket;
import de.hydracloud.cloudbridge.network.packet.RequestPacket;
import de.hydracloud.cloudbridge.network.packet.ResponsePacket;
import de.hydracloud.cloudbridge.network.packet.handler.PacketSerializer;
import de.hydracloud.cloudbridge.network.packet.pool.PacketPool;
import de.hydracloud.cloudbridge.network.request.RequestManager;
import de.hydracloud.cloudbridge.util.GeneralSettings;
import de.hydracloud.cloudbridge.util.Utils;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.logger.MainLogger;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Network implements Runnable {

    private static Network instance;
    private final PacketPool packetPool;
    private final RequestManager requestManager;
    private final InetSocketAddress address;
    private DatagramSocket socket;
    private boolean connected = false;

    public Network(InetSocketAddress address) {
        instance = this;
        this.address = address;
        this.packetPool = new PacketPool();
        requestManager = new RequestManager();

        MainLogger.getLogger().info("Try to connect to §e" + address.toString() + "§r...");
        connect();
        CloudBridge.getInstance().getThreadPool().submit(this);
    }

    @Override
    public void run() {
        while (connected) {
            try {
                String buffer;
                if ((buffer = read()) != null) {
                    CloudPacket packet = PacketSerializer.decode(buffer);
                    if (packet != null) {
                        NetworkPacketReceiveEvent ev = new NetworkPacketReceiveEvent(packet);
                        ProxyServer.getInstance().getEventManager().callEvent(ev);
                        if (ev.isCancelled()) {
                            MainLogger.getLogger().warning("Packet processing was cancelled");
                            continue;
                        }

                        packet.handle();

                        if (packet instanceof ResponsePacket) {
                            RequestManager.getInstance().callThen(((ResponsePacket) packet));
                            RequestManager.getInstance().removeRequest(((ResponsePacket) packet).getRequestId());
                        }
                    } else {
                        MainLogger.getLogger().warning("§cReceived an unknown packet from the cloud!");
                        MainLogger.getLogger().debug(GeneralSettings.isNetworkEncryptionEnabled() ? Utils.decompress(buffer.getBytes(StandardCharsets.UTF_8)) : buffer);
                    }
                }
            } catch (Exception e) {
                MainLogger.getLogger().error("§cSomething went wrong while processing a packet!", e);
            }
        }
    }

    public void connect() {
        if (connected) return;
        try {
            ProxyServer.getInstance().getEventManager().callEvent(new NetworkConnectEvent(address));
            socket = new DatagramSocket();
            socket.connect(address);
            socket.setSendBufferSize(1024 * 1024 * 8);
            socket.setReceiveBufferSize(1024 * 1024 * 8);
            connected = true;
            MainLogger.getLogger().info("Successfully connected to §e" + address.toString() + "§r!");
            MainLogger.getLogger().info("§cWaiting for incoming packets...");
        } catch (SocketException e) {
            ProxyServer.getInstance().getLogger().error("Failed to connect", e);
        }
    }

    public boolean write(String buffer) {
        DatagramPacket packet = new DatagramPacket(buffer.getBytes(StandardCharsets.UTF_8), buffer.getBytes(StandardCharsets.UTF_8).length, address);
        try {
            socket.send(packet);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public String read() {
        if (!connected) return null;
        byte[] buffer = new byte[1024 * 1024 * 8];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        try {
            socket.receive(packet);
        } catch (IOException e) {
            ProxyServer.getInstance().getLogger().error("Failed to receive a packet", e);
            return null;
        }
        return new String(packet.getData(), 0, packet.getLength()).trim();
    }

    public void close() {
        if (!connected) return;
        connected = false;
        ProxyServer.getInstance().getEventManager().callEvent(new NetworkCloseEvent());
        socket.disconnect();
        socket.close();
    }

    public boolean sendPacket(CloudPacket packet) {
        if (connected) {
            try {
                String json = PacketSerializer.encode(packet);
                NetworkPacketSendEvent ev = new NetworkPacketSendEvent(packet);
                ProxyServer.getInstance().getEventManager().callEvent(ev);

                if (!ev.isCancelled()) return write(json);
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public static Network getInstance() {
        return instance;
    }

    public PacketPool getPacketPool() {
        return packetPool;
    }

    public RequestManager getRequestManager() {
        return requestManager;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public boolean isConnected() {
        return connected;
    }
}