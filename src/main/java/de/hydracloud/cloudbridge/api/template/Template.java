package de.hydracloud.cloudbridge.api.template;

import de.hydracloud.cloudbridge.util.Utils;

import java.util.Map;

public class Template {

    private String name;
    private boolean lobby;
    private boolean maintenance;
    private boolean staticServers;
    private int maxPlayerCount;
    private int minServerCount;
    private int maxServerCount;
    private double startNewPercentage;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isLobby() {
        return lobby;
    }

    public void setLobby(boolean lobby) {
        this.lobby = lobby;
    }

    public boolean isMaintenance() {
        return maintenance;
    }

    public void setMaintenance(boolean maintenance) {
        this.maintenance = maintenance;
    }

    public boolean isStaticServers() {
        return staticServers;
    }

    public void setStaticServers(boolean staticServers) {
        this.staticServers = staticServers;
    }

    public int getMaxPlayerCount() {
        return maxPlayerCount;
    }

    public void setMaxPlayerCount(int maxPlayerCount) {
        this.maxPlayerCount = maxPlayerCount;
    }

    public int getMinServerCount() {
        return minServerCount;
    }

    public void setMinServerCount(int minServerCount) {
        this.minServerCount = minServerCount;
    }

    public int getMaxServerCount() {
        return maxServerCount;
    }

    public void setMaxServerCount(int maxServerCount) {
        this.maxServerCount = maxServerCount;
    }

    public double getStartNewPercentage() {
        return startNewPercentage;
    }

    public void setStartNewPercentage(double startNewPercentage) {
        this.startNewPercentage = startNewPercentage;
    }

    public boolean isAutoStart() {
        return autoStart;
    }

    public void setAutoStart(boolean autoStart) {
        this.autoStart = autoStart;
    }

    public String getTemplateType() {
        return templateType;
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
