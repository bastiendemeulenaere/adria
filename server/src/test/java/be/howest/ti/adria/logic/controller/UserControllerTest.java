package be.howest.ti.adria.logic.controller;

import be.howest.ti.adria.logic.data.Repositories;
import be.howest.ti.adria.logic.domain.User;
import be.howest.ti.adria.logic.domain.UserCreateObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest extends BaseControllerTest {
    private UserController userController;
    private static final String TEST_USER_ID = "test";

    @BeforeEach
    void setup() {
        this.userController = new UserController(Repositories.getInstance().getUserRepository());

        // Create a test user
        UserCreateObject userCreateObject = new UserCreateObject(TEST_USER_ID, "firstname", "lastname", "aboutme");
        userController.createUser(userCreateObject);
    }

    @Test
    void getUserById() {
        // Test for getUserById method
        // Arrange
        // Assuming the user with TEST_USER_ID exists (created in setup)

        // Act
        User user = userController.getUserById(TEST_USER_ID);

        // Assert
        assertNotNull(user);
        assertEquals(TEST_USER_ID, user.getId());
    }

    @Test
    void doesUserExist() {
        // Test for doesUserExist method
        // Arrange
        // Assuming the user with TEST_USER_ID exists (created in setup)

        // Act
        boolean userExists = userController.doesUserExist(TEST_USER_ID);

        // Assert
        assertTrue(userExists);
    }

    @Test
    void createUser() {
        // Test for createUser method
        // Arrange
        UserCreateObject newUser = new UserCreateObject("newuser", "John", "Doe", "Hello, I'm a new user!");

        // Act
        User createdUser = userController.createUser(newUser);

        // Assert
        assertNotNull(createdUser);
        assertEquals(newUser.adriaId(), createdUser.getId());
    }

    @Test
    void editAboutMe() {
        // Test for editAboutMe method
        // Arrange
        String newAboutMe = "Updated about me text";

        // Act
        String updatedAboutMe = userController.editAboutMe(TEST_USER_ID, newAboutMe);

        // Assert
        assertNotNull(updatedAboutMe);
        assertEquals(newAboutMe, updatedAboutMe);
    }
}
