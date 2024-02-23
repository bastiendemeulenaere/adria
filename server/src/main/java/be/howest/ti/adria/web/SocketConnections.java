package be.howest.ti.adria.web;

import java.util.*;

public class SocketConnections {
    private final Map<String, String> joinIdToAdriaId = new HashMap<>();

    public void put(String joinId, String adriaId) {
        joinIdToAdriaId.put(joinId, adriaId);
    }

    public String getFromJoinId(String joinId) {
        return joinIdToAdriaId.get(joinId);
    }

    public void remove(String joinId) {
        joinIdToAdriaId.remove(joinId);
    }

}
