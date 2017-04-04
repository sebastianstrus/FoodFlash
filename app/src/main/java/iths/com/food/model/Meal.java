package iths.com.food.model;

/**
 * A model class for a Meal. It has a corresponding table in the database. But it has no column for
 * total score. This is calculated by following formula: (tasteScore + healthyScore) / 2
 */

public class Meal implements IMeal{
    private String name;
    private String category;
    private String dateTime;
    private String description;
    private String imagePath;
    private double latitude;
    private double longitude;
    private long id;
    private int healthyScore;
    private int tasteScore;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHealthyScore() {
        return healthyScore;
    }

    public void setHealthyScore(int healthyScore) {
        this.healthyScore = healthyScore;
    }

    public int getTasteScore() {
        return tasteScore;
    }

    public void setTasteScore(int tasteScore) {
        this.tasteScore = tasteScore;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public double getTotalScore() {
        return (tasteScore + healthyScore) / 2.0;
    }
}
