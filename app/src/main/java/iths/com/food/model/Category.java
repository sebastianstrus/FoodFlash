package iths.com.food.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A model class for Category. A Category contains Meals that are sorted by dateTime.
 * It has a corresponding table in the database. But it has no column for meals.
 */

public class Category implements ICategory{
    private String name;
    private ArrayList<IMeal> meals;
    private int iconId;

    /**
     * Constructor for category
     *
     * @param name - the name of the category
     * @param meals - the list containing the meals
     * @param iconId - the image icons id belonging to this category
     */
    public Category(String name, ArrayList<IMeal> meals, int iconId) {
        this.name = name;
        this.meals = meals;
        this.iconId = iconId;

        sortMeals();
    }

    /**
     * Return the name of the category
     * @return - the name of the category
     */
    public String getName() {
        return name;
    }

    public double getAverageScore() {
        double sum = 0;
        for (IMeal meal: meals) {
            sum += meal.getTotalScore();
        }

        if(meals.size() == 0) {
            return 0;
        } else {
            return sum / meals.size();
        }
    }

    /**
     * Returns a list with all meals of that category
     * @return - a list with meals
     */
    public ArrayList<IMeal> getMeals() {
        return meals;
    }

    /**
     * Returns the image icons id belonging to this category
     * @return - the image icons id of this category
     */
    public int getIconId() {
        return iconId;
    }

    // Sorts the meals by dateTime, the earliest first
    private void sortMeals() {
        Collections.sort(meals, new Comparator<IMeal>() {
            @Override
            public int compare(IMeal m1, IMeal m2) {
                return m2.getDateTime().compareTo(m1.getDateTime());
            }
        });
    }
}
