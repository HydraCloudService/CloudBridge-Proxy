package de.hydracloud.cloudbridge.network.packet.impl.normal;

import de.hydracloud.cloudbridge.network.packet.CloudPacket;
import de.hydracloud.cloudbridge.network.packet.impl.types.CommandExecutionResult;
import de.hydracloud.cloudbridge.network.packet.data.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommandSendAnswerPacket extends CloudPacket {

    private CommandExecutionResult result;

    public CommandSendAnswerPacket(CommandExecutionResult commandExecutionResult) {
        this.result = commandExecutionResult;
    }

    @Override
    protected void encodePayload(PacketData packetData) {
        super.encodePayload(packetData);
        packetData.writeCommandExecutionResult(result);
    }

    @Override
    protected void decodePayload(PacketData packetData) {
        super.decodePayload(packetData);
        result = packetData.readCommandExecutionResult();
    }

    @Override
    public void handle() {}

}
