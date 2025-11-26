package de.hydracloud.cloudbridge.util;

import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.utils.config.YamlConfig;
import java.util.HashMap;

public class GeneralSettings {

    private static final HashMap<String, Object> data = new HashMap<>();

    public static void sync() {
        YamlConfig config = new YamlConfig(ProxyServer.getInstance().getDataPath().toString() + "/config.yml");
        data.put("port", config.getInt("cloud-port"));
        data.put("encryption", Boolean.parseBoolean(config.getString("encryption")));
        data.put("server_name", config.getString("server-name"));
        data.put("template_name", config.getString("template"));
        data.put("language", config.getString("cloud-language"));
        data.put("cloud_path", config.getString("cloud-path"));
        data.put("cloud_password", config.getString("cloud-password"));
    }

    public static int getNetworkPort() {
        return (int) data.get("port");
    }

    public static String getServerName() {
        return (String) data.get("server_name");
    }

    public static String getTemplateName() {
        return (String) data.get("template_name");
    }

    public static String getLanguage() {
        return (String) data.get("language");
    }

    public static String getCloudPath() {
        return (String) data.get("cloud_path");
    }

    public static String getCloudPassword() {
        return (String) data.get("cloud_password");
    }

    public static boolean isNetworkEncryptionEnabled() {
        return (boolean) data.get("encryption");
    }
}
