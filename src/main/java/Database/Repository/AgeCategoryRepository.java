package Database.Repository;

import Models.AgeCategory;

public class AgeCategoryRepository extends AbstractRepository<AgeCategory> {
    public AgeCategoryRepository() {
        super(AgeCategory.class);
    }
}
