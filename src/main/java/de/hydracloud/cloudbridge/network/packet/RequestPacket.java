package de.hydracloud.cloudbridge.network.packet;

import de.hydracloud.cloudbridge.network.packet.data.PacketData;
import de.hydracloud.cloudbridge.util.Utils;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Consumer;

@Getter
@NoArgsConstructor
public class RequestPacket extends CloudPacket {

    private String requestId;
    private double sentTime;
    private final ArrayList<Consumer<ResponsePacket>> then = new ArrayList<>();
    private Consumer<RequestPacket> failure = null;

    public void prepare() {
        requestId = UUID.randomUUID().toString();
        sentTime = Utils.time();
    }

    @Override
    public void encode(PacketData packetData) {
        super.encode(packetData);
        packetData.write(requestId);
    }

    @Override
    public void decode(PacketData packetData) {
        super.decode(packetData);
        requestId = packetData.readString();
    }

    public RequestPacket then(Consumer<ResponsePacket> then) {
        this.then.add(then);
        return this;
    }

    public void failure(Consumer<RequestPacket> failure) {
        this.failure = failure;
    }

    final public void handle() {}

}
