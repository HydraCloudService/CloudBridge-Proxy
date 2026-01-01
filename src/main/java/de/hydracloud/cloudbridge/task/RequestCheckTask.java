package de.hydracloud.cloudbridge.task;

import de.hydracloud.cloudbridge.network.packet.RequestPacket;
import de.hydracloud.cloudbridge.network.request.RequestManager;
import de.hydracloud.cloudbridge.util.Utils;
import dev.waterdog.waterdogpe.scheduler.Task;
import lombok.Getter;

@Getter
public class RequestCheckTask extends Task {

    private final RequestPacket requestPacket;

    public RequestCheckTask(RequestPacket requestPacket) {
        this.requestPacket = requestPacket;
    }

    @Override
    public void onRun(int currentTick) {
        if (RequestManager.getInstance().getRequest(requestPacket.getRequestId()) != null) {
            if ((requestPacket.getSentTime() + 10) < Utils.time()) {
                RequestManager.getInstance().callFailure(requestPacket);
                RequestManager.getInstance().removeRequest(requestPacket);
                cancel();
            } else {
                cancel();
            }
        } else {
            cancel();
        }
    }

    @Override
    public void onCancel() {

    }

}
