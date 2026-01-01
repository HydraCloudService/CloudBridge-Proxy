package de.hydracloud.cloudbridge.api.server.data;

import lombok.Getter;
import lombok.Setter;

@Getter
public class CloudServerData {

    private final int port;
    private final int maxPlayers;
    @Setter
    private int processId;

    public CloudServerData(int port, int maxPlayers, int processId) {
        this.port = port;
        this.maxPlayers = maxPlayers;
        this.processId = processId;
    }

}
