package de.hydracloud.cloudbridge.network.packet.impl.normal;

import de.hydracloud.cloudbridge.language.Language;
import de.hydracloud.cloudbridge.network.packet.CloudPacket;
import de.hydracloud.cloudbridge.network.packet.data.PacketData;
import lombok.Getter;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

@Getter
public class LanguageSyncPacket extends CloudPacket {

    private String language;
    private Map<String, String> messages;

    @Override
    protected void encodePayload(PacketData packetData) {
        packetData.write(language);
        packetData.write(messages);
    }

    @Override
    protected void decodePayload(PacketData packetData) {
        language = packetData.readString();
        messages = (Map<String, String>) packetData.readMap();
    }

    @Override
    public void handle() {
        Language lang;
        if ((lang = Language.get(language)) != null) lang.sync(messages);
    }
}
