package de.hydracloud.cloudbridge.api.template;

import de.hydracloud.cloudbridge.util.Utils;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
public class Template {

    @Setter
    private String name;
    @Setter
    private boolean lobby;
    @Setter
    private boolean maintenance;
    @Setter
    private boolean staticServers;
    @Setter
    private int maxPlayerCount;
    @Setter
    private int minServerCount;
    @Setter
    private int maxServerCount;
    @Setter
    private double startNewPercentage;
    @Setter
    private boolean autoStart;
    private final String templateType;

    public Template(String name, boolean lobby, boolean maintenance, boolean staticServers, int maxPlayerCount, int minServerCount, int maxServerCount, double startNewPercentage, boolean autoStart, String templateType) {
        this.name = name;
        this.lobby = lobby;
        this.maintenance = maintenance;
        this.staticServers = staticServers;
        this.maxPlayerCount = maxPlayerCount;
        this.minServerCount = minServerCount;
        this.maxServerCount = maxServerCount;
        this.startNewPercentage = startNewPercentage;
        this.autoStart = autoStart;
        this.templateType = templateType;
    }

    public boolean isStatic() {
        return staticServers;
    }

    public void setStatic(boolean staticServers) {
        this.staticServers = staticServers;
    }

    public void apply(Map<?,?> data) {
        name = (String) data.get("name");
        lobby = (Boolean) data.get("lobby");
        maintenance = (Boolean) data.get("maintenance");
        staticServers = (Boolean) data.get("static");
        maxPlayerCount = ((Number) data.get("maxPlayerCount")).intValue();
        minServerCount = ((Number) data.get("minServerCount")).intValue();
        maxServerCount = ((Number) data.get("maxServerCount")).intValue();
        startNewPercentage = (double) data.get("startNewPercentage");
        autoStart = (Boolean) data.get("autoStart");
    }

    public Map<String, Object> toArray() {
        return Map.of(
                "name", name,
                "lobby", lobby,
                "maintenance", maintenance,
                "static", staticServers,
                "maxPlayerCount", maxPlayerCount,
                "minServerCount", minServerCount,
                "maxServerCount", maxServerCount,
                "startNewPercentage", startNewPercentage,
                "autoStart", autoStart,
                "templateType", templateType
        );
    }

    public static Template fromArray(Map<?,?> map) {
        if (!Utils.containKeys(map, "name", "lobby", "maintenance", "static", "maxPlayerCount", "minServerCount", "maxServerCount", "startNewPercentage", "autoStart", "templateType")) return null;
        return new Template(
                (String) map.get("name"),
                (Boolean) map.get("lobby"),
                (Boolean) map.get("maintenance"),
                (Boolean) map.get("static"),
                ((Number) map.get("maxPlayerCount")).intValue(),
                ((Number) map.get("minServerCount")).intValue(),
                ((Number) map.get("maxServerCount")).intValue(),
                (double) map.get("startNewPercentage"),
                (Boolean) map.get("autoStart"),
                (String) map.get("templateType")
        );
    }
}
