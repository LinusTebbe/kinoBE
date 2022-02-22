package Models;

import Database.Repository.MovieRepository;
import Util.Column;
import Util.Entity;
import Util.ManyToOneRelation;
import Util.OneToManyRelation;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.Collection;
import java.util.List;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Entity(name = "movies", repository = MovieRepository.class)
public class Movie extends AbstractEntity {

    @Column(name = "title")
    private String title;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "long_description")
    private String longDescription;

    @Column(name = "release_date")
    private String releasedDate;

    @Column(name = "duration")
    private String duration;

    @OneToManyRelation(localField = "category_id")
    private Category category;

    @OneToManyRelation(localField = "age_category_id")
    private AgeCategory ageCategory;

    @Column(name = "price_standard")
    private float priceStandard;

    @Column(name = "price_lodge")
    private float priceLodge;

    @Column(name = "price_parquet")
    private float priceParquet;

    @Column(name = "is_3d")
    private boolean is3D;

    @Column(name = "is_dolby_atmos")
    private boolean isDolbyAtmos;

    @ManyToOneRelation(remoteField = "movie_id", targetClass = Presentation.class)
    public List<Presentation> presentations;

    public Movie(String title, String imagePath, String shortDescription, String longDescription, String releasedDate, String duration, Category category, AgeCategory ageCategory, float priceStandard, float priceLodge, float priceParquet, boolean is3D, boolean isDolbyAtmos) {
        this.title = title;
        this.imagePath = imagePath;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.releasedDate = releasedDate;
        this.duration = duration;
        this.category = category;
        this.ageCategory = ageCategory;
        this.priceStandard = priceStandard;
        this.priceLodge = priceLodge;
        this.priceParquet = priceParquet;
        this.is3D = is3D;
        this.isDolbyAtmos = isDolbyAtmos;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getReleasedDate() {
        return releasedDate;
    }

    public void setReleasedDate(String releasedDate) {
        this.releasedDate = releasedDate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public AgeCategory getAgeCategory() {
        return ageCategory;
    }

    public void setAgeCategory(AgeCategory ageCategory) {
        this.ageCategory = ageCategory;
    }

    public float getPriceStandard() {
        return priceStandard;
    }

    public void setPriceStandard(float priceStandard) {
        this.priceStandard = priceStandard;
    }

    public float getPriceLodge() {
        return priceLodge;
    }

    public void setPriceLodge(float priceLodge) {
        this.priceLodge = priceLodge;
    }

    public float getPriceParquet() {
        return priceParquet;
    }

    public void setPriceParquet(float priceParquet) {
        this.priceParquet = priceParquet;
    }

    public boolean isIs3D() {
        return is3D;
    }

    public void setIs3D(boolean is3D) {
        this.is3D = is3D;
    }

    public boolean isDolbyAtmos() {
        return isDolbyAtmos;
    }

    public void setDolbyAtmos(boolean dolbyAtmos) {
        isDolbyAtmos = dolbyAtmos;
    }
}
