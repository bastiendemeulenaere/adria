package be.howest.ti.adria.web.dto;

public class UserDto extends SimpleUserDto {
    private String aboutMe;
    public String getAboutMe() {
        return aboutMe;
    }
    public UserDto(String id, String firstName, String lastName, String aboutMe) {
        super(id, firstName, lastName);
        this.aboutMe = aboutMe;
    }
    public UserDto(SimpleUserDto simpleDto, String aboutMe) {
        super(simpleDto.getId(), simpleDto.getFirstName(), simpleDto.getLastName());
        this.aboutMe = aboutMe;
    }
}
