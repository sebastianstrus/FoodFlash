package iths.com.food.model;

import java.util.ArrayList;


public interface ICategory {

    String getName();

    double getAverageScore();

    ArrayList<IMeal> getMeals();

    int getIconId();
}
