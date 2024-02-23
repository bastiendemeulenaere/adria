package be.howest.ti.adria.web.dto;


public class EventCreateDto {

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getAmountOfPeople() {
        return amountOfPeople;
    }

    public int getCategoryId() {
        return categoryId;
    }
    public int getSectorId() {
        return sectorId;
    }

    public long getStartDateTime() {
        return startDateTime;
    }

    public int getHours() {
        return hours;
    }

    private final String name;
    private final String description;
    private final int amountOfPeople;
    private final int categoryId;
    private final int sectorId;
    private final long startDateTime;
    private final int hours;


    public EventCreateDto(String name, String description, int amountOfPeople, int categoryId, int sectorId, long startDateTime, int hours) {
        this.name = name;
        this.description = description;
        this.amountOfPeople = amountOfPeople;
        this.categoryId = categoryId;
        this.sectorId = sectorId;
        this.startDateTime = startDateTime;
        this.hours = hours;
    }
}
