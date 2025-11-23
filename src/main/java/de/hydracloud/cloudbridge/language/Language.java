package de.hydracloud.cloudbridge.language;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Language {

    GERMAN("German", List.of("de_DE", "ger", "Deutsch")),
    ENGLISH("English", List.of("en_US", "en", "Englisch"));

    public static Language current() {
        return get("");
    }

    public static Language fallback() {
        return get("en");
    }

    public static Language get(String name) {
        for (Language language : values()) {
            if (language.getName().equals(name) || language.getAliases().contains(name)) return language;
        }
        return fallback();
    }

    @Getter
    private final String name;
    @Getter
    private final List<String> aliases;
    private Map<String, String> messages = new HashMap<>();

    Language(String name, List<String> aliases) {
        this.name = name;
        this.aliases = aliases;
    }

    public void sync(Map<String, String> messages) {
        this.messages = messages;
    }

    public String translate(String key, String ...params) {
        String message = messages.getOrDefault(key, key).replace("{PREFIX}", messages.getOrDefault("inGame.prefix", ""));
        for (int i = 0; i < params.length; i++) message = message.replace("%" + i + "%", params[i]);
        return message;
    }
}
