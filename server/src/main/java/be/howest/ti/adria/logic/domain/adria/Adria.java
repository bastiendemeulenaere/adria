package be.howest.ti.adria.logic.domain.adria;

import be.howest.ti.adria.logic.util.RandomTimeStampGenerator;
import be.howest.ti.adria.logic.domain.StartEndTimestamp;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Adria {
    private static final Logger LOGGER = Logger.getLogger(Adria.class.getName());
    private final World world;
    private static final double MAX_PERCENTAGE = .50;
    private static final double MIN_PERCENTAGE = .20;
    private final Random random = new SecureRandom();
    private ScheduledExecutorService scheduler;
    public Adria(World world) {
        this.world = world;
    }

    public void start() {
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::updateDangerousZones, 1, 1, TimeUnit.SECONDS);
    }

    public void stop() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }

    private void updateDangerousZones() {
        addRandomDangerZone();
        removeRandomDangerZonesInSector();
    }

    private void removeRandomDangerZonesInSector() {
        try {
            int randomSectorIndex = random.nextInt(world.getWorldSectors().size());
            WorldSector sector = world.getWorldSectors().get(randomSectorIndex);

            if (random.nextBoolean()) {
                world.removeDangerousZonesInSector(sector);
            }

        } catch (Exception e) {
            LOGGER.severe("Error in updateDangerousZones: " + e.getMessage());
        }
    }
    private int lastRandomSectorIndex = -1;

    private int getRandomSector(boolean allowRepeatingSector) {
        int sectorCount = world.getWorldSectors().size();
        int randomSectorIndex = random.nextInt(1, sectorCount);
        if (!allowRepeatingSector) {
            while (randomSectorIndex == lastRandomSectorIndex) {
                randomSectorIndex = random.nextInt(1, sectorCount);
            }
        }
        lastRandomSectorIndex = randomSectorIndex;
        return randomSectorIndex;
    }

    private void addRandomDangerZone() {
        try {
            int randomSectorIndex = getRandomSector(false);
            Sector sectorType = Sector.from(randomSectorIndex);

            WorldSector sector = world.getSector(sectorType);

            // 50% change something happens
            if (random.nextBoolean()) {
                DangerousArea newDangerousArea = generateRandomDangerousArea(sector);
                world.addDangerousZoneToSector(sectorType, newDangerousArea);
            }


        } catch (Exception e) {
            LOGGER.severe("Error in updateDangerousZones: " + e.getMessage());
        }
    }

    private DangerousArea generateRandomDangerousArea(WorldSector sector) {
        CoordinateRange sectorRange = sector.getCoordinateRange();

        int sectorWidth = sectorRange.end().x() - sectorRange.start().x();
        int sectorHeight = sectorRange.end().y() - sectorRange.start().y();

        int minAreaWidth = (int) (sectorWidth * MIN_PERCENTAGE);
        int minAreaHeight = (int) (sectorHeight * MIN_PERCENTAGE);

        int maxAreaWidth = Math.max((int) (sectorWidth * MAX_PERCENTAGE), minAreaWidth);
        int maxAreaHeight = Math.max((int) (sectorHeight * MAX_PERCENTAGE), minAreaHeight);

        int areaWidth = random.nextInt(maxAreaWidth - minAreaWidth + 1) + minAreaWidth;
        int areaHeight = random.nextInt(maxAreaHeight - minAreaHeight + 1) + minAreaHeight;

        int startX = sectorRange.start().x() + random.nextInt(sectorWidth - areaWidth + 1);
        int startY = sectorRange.start().y() + random.nextInt(sectorHeight - areaHeight + 1);

        int endX = startX + areaWidth;
        int endY = startY + areaHeight;

        CoordinateRange areaCoordinateRange = new CoordinateRange(
                new Coordinate(startX, startY),
                new Coordinate(endX, endY)
        );
        StartEndTimestamp startEndTimestamp = RandomTimeStampGenerator.generateCustomStartEndTimestamp(5, 10, 1, 8);
        return new DangerousArea(Dangers.getRandomDanger().getName(), areaCoordinateRange, startEndTimestamp.start(), startEndTimestamp.end());
    }


    public World getWorld() {
        return this.world;
    }
}