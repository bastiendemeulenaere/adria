package be.howest.ti.adria.logic.domain;

import be.howest.ti.adria.logic.domain.event.Event;
import be.howest.ti.adria.web.dto.SimpleUserDto;
import be.howest.ti.adria.web.dto.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {
    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<Event> getEvents() {
        return events;
    }

    public List<Category> getInterests() {
        return interests;
    }

    private String id;
    private final String aboutMe;
    private String firstName;
    private String lastName;

    private List<Event> events = new ArrayList<>();

    private List<Category> interests = new ArrayList<>();

    public User(String id, String firstName, String lastName, String aboutMe) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.aboutMe = aboutMe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public SimpleUserDto toSimpleDto() {
        return new SimpleUserDto(this.id, this.firstName, this.lastName);
    }
    public UserDto toDto(){
        return new UserDto(toSimpleDto(), aboutMe);
    }
}
