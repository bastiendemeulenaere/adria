package be.howest.ti.adria.logic.controller;

import be.howest.ti.adria.logic.data.CategoryRepository;
import be.howest.ti.adria.logic.domain.Category;

import java.util.List;
import java.util.NoSuchElementException;

public class CategoryControllerImpl implements CategoryController {
    private static final String MSG_UNKNOWN = "No %s available";
    private static final String MSG_CATEGORY_ID_UNKNOWN = "No category with id: %d";
    private final CategoryRepository categoryRepository;

    public CategoryControllerImpl(CategoryRepository repository) {
        this.categoryRepository = repository;
    }

    @Override
    public Category getCategoryById(int categoryId) {
        Category category = categoryRepository.getCategory(categoryId);

        if (null == category)
            throw new NoSuchElementException(String.format(MSG_CATEGORY_ID_UNKNOWN, categoryId));

        return category;
    }
    @Override
    public List<Category> getCategories() {
        List<Category> categories = categoryRepository.getCategories();

        if (null == categories)
            throw new NoSuchElementException(String.format(MSG_UNKNOWN, "category"));

        return categories;
    }
}
