package be.howest.ti.adria.web.dto;

import com.fasterxml.jackson.annotation.JsonGetter;

public class SimpleUserDto {
    public SimpleUserDto(String id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @JsonGetter("id")
    public String getId() {
        return id;
    }

    @JsonGetter("firstName")
    public String getFirstName() {
        return firstName;
    }

    @JsonGetter("lastName")
    public String getLastName() {
        return lastName;
    }

    private String id;
    private String firstName;
    private String lastName;
}
