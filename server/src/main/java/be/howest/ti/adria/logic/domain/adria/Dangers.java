package be.howest.ti.adria.logic.domain.adria;

import java.security.SecureRandom;
import java.util.Random;

public enum Dangers {
    FIRESTORM("Firestorm"),
    ACID_RAIN("Acid rain"),
    TOXIC_FUMES("Toxic fumes"),
    RADIOACTIVE_CONTAMINATION("Radioactive contamination"),
    DEPLETED_OXYGEN_ZONES("Depleted oxygen zones"),
    SIZZLING_HEATWAVES("Heatwaves"),
    DUST_STORMS("Dust storms"),
    FIREBALLS("Falling fireballs");
    private final String name;
    private static final Random random = new SecureRandom();
    Dangers(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public static Dangers getRandomDanger() {
        return values()[random.nextInt(values().length)];
    }
}
