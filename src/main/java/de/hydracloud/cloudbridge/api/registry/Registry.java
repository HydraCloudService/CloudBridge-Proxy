package de.hydracloud.cloudbridge.api.registry;

import de.hydracloud.cloudbridge.api.player.CloudPlayer;
import de.hydracloud.cloudbridge.api.server.CloudServer;
import de.hydracloud.cloudbridge.api.server.status.ServerStatus;
import de.hydracloud.cloudbridge.api.template.Template;
import dev.waterdog.waterdogpe.ProxyServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Registry {

    private static final HashMap<String, CloudServer> servers = new HashMap<>();
    private static final HashMap<String, Template> templates = new HashMap<>();
    private static final HashMap<String, CloudPlayer> players = new HashMap<>();

    public static void registerServer(CloudServer server) {
        if (!servers.containsKey(server.getName())) servers.put(server.getName(), server);
    }

    public static void registerTemplate(Template template) {
        if (!templates.containsKey(template.getName())) templates.put(template.getName(), template);
    }

    public static void registerPlayer(CloudPlayer player) {
        if (!players.containsKey(player.getName())) players.put(player.getName(), player);
    }

    public static void unregisterServer(String server) {
        servers.remove(server);
    }

    public static void unregisterTemplate(String template) {
        templates.remove(template);
    }

    public static void unregisterPlayer(String player) {
        players.remove(player);
    }

    public static void updateServer(String serverName, ServerStatus serverStatus) {
        CloudServer server = servers.get(serverName);
        if (server == null) return;
        server.setServerStatus(serverStatus);
    }

    public static void updateTemplate(String templateName, Map<?,?> newData) {
        Template template = templates.get(templateName);
        if (template == null) return;
        try {
            template.apply(newData);
        } catch (Exception e) {
            ProxyServer.getInstance().getLogger().error("Failed to apply settings to a template", e);
        }
    }

    public static void updatePlayer(String playerName, String serverName) {
        CloudPlayer player = players.get(playerName);
        if (player == null) return;
        player.setCurrentServer(servers.get(serverName));
    }

    public static ArrayList<CloudServer> getServers() {
        return new ArrayList<>(servers.values());
    }

    public static ArrayList<Template> getTemplates() {
        return new ArrayList<>(templates.values());
    }

    public static ArrayList<CloudPlayer> getPlayers() {
        return new ArrayList<>(players.values());
    }
}

