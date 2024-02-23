package be.howest.ti.adria.web.listeners;

import be.howest.ti.adria.logic.domain.adria.Coordinate;
import be.howest.ti.adria.web.dto.DangerZoneInfoDto;
import be.howest.ti.adria.web.dto.EventDto;
import be.howest.ti.adria.web.dto.SuggestedLocationDto;
import be.howest.ti.adria.web.dto.adria.DangerousAreaDto;

public interface NotificationListener {
    void sendDangerZoneNotification(String adriaId, DangerZoneInfoDto info);
    void sendSuggestLocationNotification(String adriaId, SuggestedLocationDto suggestedLocationDto, EventDto eventDto);
    void sendLocationNotification(String adriaId, Coordinate suggestedLocationDto, EventDto dto);
    void sendCanceledNotification(String adriaId, EventDto eventDto);
    void sendDangerNotification(String adriaId, EventDto eventDto, DangerousAreaDto dangerousAreaDto);
}
