package be.howest.ti.adria.logic.controller;

import be.howest.ti.adria.logic.data.Repositories;
import be.howest.ti.adria.logic.domain.Category;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CategoryControllerTest extends BaseControllerTest {

    @Test
    void getCategory() {
        // Arrange
        CategoryController controller = new CategoryControllerImpl(Repositories.getInstance().getCategoryRepository());

        // Act
        Category category = controller.getCategoryById(1);

        //Assert
        assertTrue(category != null && StringUtils.isNoneBlank(category.getName()));
    }

    @Test
    void getCategories() {
        // Arrange
        CategoryController controller = new CategoryControllerImpl(Repositories.getInstance().getCategoryRepository());

        // Act
        List<Category> categories = controller.getCategories();

        //Assert
        assertTrue(categories != null && categories.size() > 1);
    }

}
