package de.hydracloud.cloudbridge.api.server;

import de.hydracloud.cloudbridge.api.CloudAPI;
import de.hydracloud.cloudbridge.api.server.data.CloudServerData;
import de.hydracloud.cloudbridge.api.server.status.ServerStatus;
import de.hydracloud.cloudbridge.api.template.Template;
import de.hydracloud.cloudbridge.util.Utils;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
public class CloudServer {

    private final int id;
    private final String uuid;
    private final Template template;
    private final CloudServerData cloudServerData;
    @Setter
    private ServerStatus serverStatus;

    public CloudServer(int id, String uuid, Template template, CloudServerData cloudServerData, ServerStatus serverStatus) {
        this.id = id;
        this.uuid = uuid;
        this.template = template;
        this.cloudServerData = cloudServerData;
        this.serverStatus = serverStatus;
    }

    public String getName() {
        return template.getName() + "-" + id;
    }

    public Map<String, Object> toArray() {
        return Map.of(
                "name", getName(),
                "id", id,
                "uuid", uuid,
                "template", template.getName(),
                "port", cloudServerData.getPort(),
                "maxPlayers", cloudServerData.getMaxPlayers(),
                "processId", cloudServerData.getProcessId(),
                "serverStatus", serverStatus.getName()
        );
    }

    public static CloudServer fromArray(Map<?,?> map) {
        if (!Utils.containKeys(map, "name", "id", "uuid", "template", "port", "maxPlayers", "processId", "serverStatus")) return null;
        Template template;
        if ((template = CloudAPI.getInstance().getTemplateByName((String)map.get("template"))) == null) return null;
        ServerStatus serverStatus;
        if ((serverStatus = ServerStatus.getServerStatusByName((String)map.get("serverStatus"))) != null) serverStatus = ServerStatus.ONLINE;
        return new CloudServer(
                ((Number) map.get("id")).intValue(),
                map.get("uuid").toString(),
                template,
                new CloudServerData(((Number) map.get("port")).intValue(), ((Number) map.get("maxPlayers")).intValue(), ((Number) map.get("processId")).intValue()),
                serverStatus
        );
    }
}
