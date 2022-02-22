package Database.Repository;

import Models.Category;

public class CategoryRepository extends AbstractRepository<Category> {
    public CategoryRepository() {
        super(Category.class);
    }
}
