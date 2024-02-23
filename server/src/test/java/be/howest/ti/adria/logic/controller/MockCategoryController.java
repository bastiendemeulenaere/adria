package be.howest.ti.adria.logic.controller;

import be.howest.ti.adria.logic.domain.Category;

import java.util.List;

public class MockCategoryController implements CategoryController {
    private static Category sportsCategory = new Category(1, "sports");
    private static Category musicCategory = new Category(2, "music");

    @Override
    public Category getCategoryById(int categoryId) {
        switch (categoryId){
            case 1 -> {
                return sportsCategory;
            }
            case 2 -> { return musicCategory;}
        }
        return null;
    }

    @Override
    public List<Category> getCategories() {
        return null;
    }
}
