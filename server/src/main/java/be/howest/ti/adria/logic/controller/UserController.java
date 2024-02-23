package be.howest.ti.adria.logic.controller;

import be.howest.ti.adria.logic.data.UserRepository;
import be.howest.ti.adria.logic.domain.User;
import be.howest.ti.adria.logic.domain.UserCreateObject;

public class UserController {
    private final UserRepository userRepository;
    public UserController(UserRepository repository) {
        this.userRepository = repository;
    }
    public User getUserById(String adriaId) {
        return userRepository.getUserById(adriaId);
    }
    public boolean doesUserExist(String adriaId){
        return userRepository.userExists(adriaId);
    }
    public User createUser(UserCreateObject user) {
        return userRepository.createUser(user);
    }
    public String editAboutMe(String adriaId, String aboutMe) {
        return userRepository.editAboutMe(adriaId, aboutMe);
    }
}
