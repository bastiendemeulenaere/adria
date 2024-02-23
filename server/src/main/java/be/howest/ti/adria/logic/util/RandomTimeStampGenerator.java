package be.howest.ti.adria.logic.util;

import be.howest.ti.adria.logic.domain.StartEndTimestamp;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

public class RandomTimeStampGenerator {
    private static final Random random = new SecureRandom();

    private RandomTimeStampGenerator(){
        //util class
    }
    public static StartEndTimestamp generateCustomStartEndTimestamp(int minOffset, int maxOffset, int minDuration, int maxDuration) {
        Instant currentTime = Instant.now();

        // Generate a random offset within the specified range
        int randomOffset = random.nextInt(maxOffset - minOffset + 1) + minOffset;
        Instant randomTime = currentTime.plus(randomOffset, ChronoUnit.MINUTES);

        // Generate a random duration within the specified range
        int randomDuration = random.nextInt(maxDuration - minDuration + 1) + minDuration;
        Instant endTime = randomTime.plus(randomDuration, ChronoUnit.MINUTES);

        return new StartEndTimestamp(randomTime, endTime);
    }
}
