package de.hydracloud.cloudbridge.network.packet.impl.normal;

import de.hydracloud.cloudbridge.network.packet.CloudPacket;
import de.hydracloud.cloudbridge.network.packet.impl.types.LogType;
import de.hydracloud.cloudbridge.network.packet.data.PacketData;
import dev.waterdog.waterdogpe.logger.MainLogger;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ConsoleTextPacket extends CloudPacket {

    private String text;
    private LogType logType;

    public ConsoleTextPacket(String text, LogType logType) {
        this.text = text;
        this.logType = logType;
    }

    @Override
    protected void encodePayload(PacketData packetData) {
        super.encodePayload(packetData);
        packetData.write(text);
        packetData.writeLogType(logType);
    }

    @Override
    protected void decodePayload(PacketData packetData) {
        super.decodePayload(packetData);
        text = packetData.readString();
        logType = packetData.readLogType();
    }

    @Override
    public void handle() {
        if (logType == LogType.INFO) MainLogger.getLogger().info(text);
        else if (logType == LogType.DEBUG) MainLogger.getLogger().debug(text);
        else if (logType == LogType.WARN) MainLogger.getLogger().warning(text);
        else if (logType == LogType.ERROR) MainLogger.getLogger().error(text);
    }

}
