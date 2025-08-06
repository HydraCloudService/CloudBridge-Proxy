package de.hydracloud.cloudbridge.util;

import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.command.ConsoleCommandSender;
import dev.waterdog.waterdogpe.utils.types.TextContainer;
import dev.waterdog.waterdogpe.utils.types.TranslationContainer;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class CloudCommandSender extends ConsoleCommandSender {

    public CloudCommandSender() {
        super(ProxyServer.getInstance());
    }

    private final ArrayList<String> cachedMessages = new ArrayList<>();

    @Override
    public void sendMessage(String message) {
        super.sendMessage(message);
        cachedMessages.add(message);
    }

    @Override
    public void sendMessage(TextContainer message) {
        super.sendMessage(message);
        String msg;
        if (message instanceof TranslationContainer) msg = ((TranslationContainer) message).getTranslated();
        else msg = message.getMessage();
        cachedMessages.add(msg);
    }

    @Override
    public String getName() {
        return "CLOUD";
    }
}
