package be.howest.ti.adria.logic.data;

import be.howest.ti.adria.logic.domain.Category;

import java.util.List;

public abstract class CategoryRepository extends Repository {
    protected CategoryRepository(H2Connection h2Connection) {
        super(h2Connection);
    }
    public abstract List<Category> getCategories();
    public abstract Category getCategory(int id);

}
