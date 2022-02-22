package Models;

import Database.Repository.AgeCategoryRepository;
import Util.Column;
import Util.Entity;

@Entity(name = "age_category", repository = AgeCategoryRepository.class)
public class AgeCategory extends AbstractEntity {
    @Column(name = "age")
    private final int age;

    @Column(name = "icon_path")
    private final String iconPath;

    public AgeCategory(int age, String iconPath) {
        this.age = age;
        this.iconPath = iconPath;
    }

    public int getAge() {
        return age;
    }

    public String getIconPath() {
        return iconPath;
    }
}
