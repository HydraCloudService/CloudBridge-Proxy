package de.hydracloud.cloudbridge.api.player;

import de.hydracloud.cloudbridge.api.CloudAPI;
import de.hydracloud.cloudbridge.api.server.CloudServer;
import de.hydracloud.cloudbridge.util.Utils;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CloudPlayer {

    private final String name;
    private final String host;
    private final String xboxUserId;
    private final String uniqueId;
    @Setter
    private CloudServer currentServer;
    @Setter
    private CloudServer currentProxy;

    public CloudPlayer(String name, String host, String xboxUserId, String uniqueId, CloudServer currentServer, CloudServer currentProxy) {
        this.name = name;
        this.host = host;
        this.xboxUserId = xboxUserId;
        this.uniqueId = uniqueId;
        this.currentServer = currentServer;
        this.currentProxy = currentProxy;
    }

    public HashMap<String, Object> toArray() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("host", host);
        map.put("xboxUserId", xboxUserId);
        map.put("uniqueId", uniqueId);
        map.put("currentServer", (currentServer == null ? null : currentServer.getName()));
        map.put("currentProxy", (currentProxy == null ? null : currentProxy.getName()));
        return map;
    }

    public static CloudPlayer fromArray(Map<?,?> map) {
        if (!Utils.containKeys(map, "name", "host", "xboxUserId", "uniqueId")) return null;
        return new CloudPlayer(
                (String) map.get("name"),
                (String) map.get("host"),
                (String) map.get("xboxUserId"),
                (String) map.get("uniqueId"),
                (!map.containsKey("currentServer") ? null : CloudAPI.getInstance().getServerByName((String)map.get("currentServer"))),
                (!map.containsKey("currentProxy") ? null : CloudAPI.getInstance().getServerByName((String)map.get("currentProxy")))
        );
    }
}
