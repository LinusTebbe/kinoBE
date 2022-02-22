package Models;

import Database.Repository.CategoryRepository;
import Util.Column;
import Util.Entity;

@Entity(name = "category", repository = CategoryRepository.class)
public class Category extends AbstractEntity {
    @Column(name = "name")
    private final String name;

    public Category(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }
}
