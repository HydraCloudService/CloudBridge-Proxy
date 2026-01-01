package de.hydracloud.cloudbridge.network.packet.impl.normal;

import de.hydracloud.cloudbridge.CloudBridge;
import de.hydracloud.cloudbridge.network.Network;
import de.hydracloud.cloudbridge.network.packet.CloudPacket;
import de.hydracloud.cloudbridge.util.Utils;
import dev.waterdog.waterdogpe.logger.MainLogger;

public class KeepAlivePacket extends CloudPacket {

    @Override
    public void handle() {
        CloudBridge.getInstance().lastKeepALiveCheck = Utils.time();
        Network.getInstance().sendPacket(new KeepAlivePacket());
    }
}