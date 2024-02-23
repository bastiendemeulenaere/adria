package be.howest.ti.adria.logic.controller;

import be.howest.ti.adria.logic.data.Repositories;
import be.howest.ti.adria.logic.domain.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InterestControllerTest extends BaseControllerTest{
    private static final String TEST_USER_ID = "test";
    private InterestController interestController;
    @BeforeEach
    void setup(){
        interestController = new InterestControllerImpl(Repositories.getInstance().getInterestRepository());
    }
    @Test
    void addUserInterests() {
        // Arrange (Create interest IDs and add to user interests)
        List<Integer> interestIdsToAdd = Arrays.asList(2, 4, 6);

        // Act
        interestController.setUserInterests(interestIdsToAdd, TEST_USER_ID);

        // Assert (Check if interests were added to the user)
        List<Category> userInterests = interestController.getUserInterests(TEST_USER_ID);
        assertNotNull(userInterests);
        List<Integer> userInterestsIds = userInterests.stream().map(Category::getCategoryId).toList();
        for (Integer interestId : interestIdsToAdd) {
            assertTrue(userInterestsIds.contains(interestId));
        }
    }
    @Test
    void getInterests(){
        List<Integer> interestIds = Arrays.asList(1, 3, 5);
        interestController.setUserInterests(interestIds, TEST_USER_ID);
        List<Category> userInterests= interestController.getUserInterests(TEST_USER_ID);
        assertNotNull(userInterests);
        assertFalse(userInterests.isEmpty());
    }


}
