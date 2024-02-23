package be.howest.ti.adria.logic.controller;

import be.howest.ti.adria.logic.domain.Category;

import java.util.List;

public interface CategoryController {
    Category getCategoryById(int categoryId);

    List<Category> getCategories();
}
