package de.hydracloud.cloudbridge.api.server.data;

public class CloudServerData {

    private final int port;
    private final int maxPlayers;
    private int processId;

    public CloudServerData(int port, int maxPlayers, int processId) {
        this.port = port;
        this.maxPlayers = maxPlayers;
        this.processId = processId;
    }

    public int getPort() {
        return port;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getProcessId() {
        return processId;
    }

    public void setProcessId(int processId) {
        this.processId = processId;
    }
}
