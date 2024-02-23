package be.howest.ti.adria.web.socketevents;

import be.howest.ti.adria.web.dto.DangerZoneInfoDto;

public class SocketDangerEvent extends SocketEvent {

    private final DangerZoneInfoDto info;

    public DangerZoneInfoDto getInfo() {
        return info;
    }

    public SocketDangerEvent(DangerZoneInfoDto info) {
        super(SocketEventType.DANGERZONEINFO);
        this.info = info;
    }
}
