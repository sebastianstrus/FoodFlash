package iths.com.food.helper.db;

import android.provider.BaseColumns;

/**
 * Defines the database and its tables Meal and Category, and the SQL statement for creating those.
 */

final class DatabaseContract {

    static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "food.db";

    static final String SQL_CREATE_DB_SCRIPT =
            "CREATE TABLE " + MealEntry.TABLE + " (" +
                    MealEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    MealEntry.COLUMN_NAME + " TEXT," +
                    MealEntry.COLUMN_CATEGORY + " TEXT," +
                    MealEntry.COLUMN_DATE_TIME + " TEXT," +
                    MealEntry.COLUMN_DESCRIPTION + " TEXT," +
                    MealEntry.COLUMN_HEALTHY_SCORE + " INTEGER," +
                    MealEntry.COLUMN_TASTE_SCORE + " INTEGER," +
                    MealEntry.COLUMN_LONGITUDE + " DOUBLE," +
                    MealEntry.COLUMN_IMAGE_PATH + " TEXT," +
                    MealEntry.COLUMN_LATITUDE + " DOUBLE);" +
            "CREATE TABLE " + CategoryEntry.TABLE + " ( " +
                    CategoryEntry.COLUMN_NAME + " TEXT PRIMARY KEY, " +
                    CategoryEntry.COLUMN_ICON_ID + " INTEGER);" +
                    "INSERT INTO " + CategoryEntry.TABLE + " (" + CategoryEntry.COLUMN_NAME + ", "+CategoryEntry.COLUMN_ICON_ID+") " +
                    "VALUES ('Meat','12');" +
                    "INSERT INTO " + CategoryEntry.TABLE + " (" + CategoryEntry.COLUMN_NAME + ", "+CategoryEntry.COLUMN_ICON_ID+") " +
                    "VALUES ('Veggie','11');" +
                    "INSERT INTO " + CategoryEntry.TABLE + " (" + CategoryEntry.COLUMN_NAME + ", "+CategoryEntry.COLUMN_ICON_ID+") " +
                    "VALUES ('Fish','17');";


    private DatabaseContract() {}

    static class MealEntry implements BaseColumns{
        static final String TABLE = "meal";
        static final String COLUMN_ID = "_id";
        static final String COLUMN_NAME = "name";
        static final String COLUMN_DESCRIPTION = "description";
        static final String COLUMN_HEALTHY_SCORE = "healthy_score";
        static final String COLUMN_TASTE_SCORE = "taste_score";
        static final String COLUMN_CATEGORY = "category";
        static final String COLUMN_DATE_TIME = "date_time";
        static final String COLUMN_LONGITUDE = "longitude";
        static final String COLUMN_LATITUDE = "latitude";
        static final String COLUMN_IMAGE_PATH = "image_path";
    }

    static class CategoryEntry implements BaseColumns{
        static final String TABLE = "category";
        static final String COLUMN_NAME = "name";
        static final String COLUMN_ICON_ID = "icon_id";
    }
}
