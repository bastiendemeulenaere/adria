package be.howest.ti.adria.web.dto;


import be.howest.ti.adria.logic.domain.Category;
import com.fasterxml.jackson.annotation.JsonGetter;

public class InterestDto {
    private final String name;
    private final Category category;

    public InterestDto(String name, Category category) {
        this.name = name;
        this.category = category;
    }
    @JsonGetter
    public String getName() {
        return name;
    }
    @JsonGetter
    public Category getCategory() {
        return category;
    }
}
