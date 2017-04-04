package iths.com.food.helper.db;

import android.content.Context;

// Database
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// Util
import android.util.Log;
import java.util.ArrayList;

// Model
import iths.com.food.model.*;

/**
 * Helper class for CRUD operations on the database.
 */

public class DatabaseHelper extends SQLiteOpenHelper implements IDatabaseHelper {

    private static final String TAG = "DatabaseHelper";

    public DatabaseHelper(Context context) {
        super(context, DatabaseContract.DATABASE_NAME, null, DatabaseContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String sql: DatabaseContract.SQL_CREATE_DB_SCRIPT.split(";")) {
            db.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    /**
     * Inserts a meal
     *
     * @param meal - the meal to be inserted
     * @return - the row ID of the inserted row, or -1 if an error occurred
     */
    public long insertMeal(IMeal meal){

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.MealEntry.COLUMN_NAME, meal.getName());
        values.put(DatabaseContract.MealEntry.COLUMN_CATEGORY, meal.getCategory());
        values.put(DatabaseContract.MealEntry.COLUMN_DATE_TIME, meal.getDateTime());
        values.put(DatabaseContract.MealEntry.COLUMN_DESCRIPTION, meal.getDescription());
        values.put(DatabaseContract.MealEntry.COLUMN_HEALTHY_SCORE, meal.getHealthyScore());
        values.put(DatabaseContract.MealEntry.COLUMN_TASTE_SCORE, meal.getTasteScore());
        values.put(DatabaseContract.MealEntry.COLUMN_LONGITUDE, meal.getLongitude());
        Log.d(TAG, "insertMeal longitud: "+meal.getLongitude());
        values.put(DatabaseContract.MealEntry.COLUMN_LATITUDE, meal.getLatitude());
        values.put(DatabaseContract.MealEntry.COLUMN_IMAGE_PATH, meal.getImagePath());

        return getWritableDatabase().insert(DatabaseContract.MealEntry.TABLE, null, values);
    }

    /**
     * Inserts a category
     *
     * @param name - the name of the category
     * @param iconID - the ID of the icon for that category
     * @return - the row ID of the inserted row, or -1 if an error occurred
     */
    public long insertCategory(String name, int iconID){

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.CategoryEntry.COLUMN_NAME, name);
        values.put(DatabaseContract.CategoryEntry.COLUMN_ICON_ID, iconID);

        return getWritableDatabase().insert(DatabaseContract.CategoryEntry.TABLE, null, values);
    }

    public IMeal getMeal(long id){
        String[] projection = {
                DatabaseContract.MealEntry.COLUMN_NAME,
                DatabaseContract.MealEntry.COLUMN_CATEGORY,
                DatabaseContract.MealEntry.COLUMN_DATE_TIME,
                DatabaseContract.MealEntry.COLUMN_DESCRIPTION,
                DatabaseContract.MealEntry.COLUMN_HEALTHY_SCORE,
                DatabaseContract.MealEntry.COLUMN_TASTE_SCORE,
                DatabaseContract.MealEntry.COLUMN_LONGITUDE,
                DatabaseContract.MealEntry.COLUMN_LATITUDE,
                DatabaseContract.MealEntry.COLUMN_IMAGE_PATH
        };

        String selection = DatabaseContract.MealEntry.COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };
        String sortOrder = DatabaseContract.MealEntry.COLUMN_TASTE_SCORE + " DESC";
        IMeal meal = new Meal();
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(
                    DatabaseContract.MealEntry.TABLE,         // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            meal.setId(id);
            while (cursor.moveToNext()) {
                meal.setName( cursor.getString( cursor.getColumnIndexOrThrow(DatabaseContract.MealEntry.COLUMN_NAME)));
                meal.setCategory( cursor.getString( cursor.getColumnIndexOrThrow(DatabaseContract.MealEntry.COLUMN_CATEGORY)));
                meal.setDescription( cursor.getString( cursor.getColumnIndexOrThrow(DatabaseContract.MealEntry.COLUMN_DESCRIPTION)));
                meal.setHealthyScore( cursor.getInt( cursor.getColumnIndexOrThrow(DatabaseContract.MealEntry.COLUMN_HEALTHY_SCORE)));
                meal.setTasteScore( cursor.getInt( cursor.getColumnIndexOrThrow(DatabaseContract.MealEntry.COLUMN_TASTE_SCORE)));
                meal.setDateTime( cursor.getString( cursor.getColumnIndexOrThrow(DatabaseContract.MealEntry.COLUMN_DATE_TIME)));
                meal.setImagePath( cursor.getString( cursor.getColumnIndexOrThrow(DatabaseContract.MealEntry.COLUMN_IMAGE_PATH)));
                meal.setLongitude( cursor.getDouble( cursor.getColumnIndexOrThrow(DatabaseContract.MealEntry.COLUMN_LONGITUDE)));
                meal.setLatitude( cursor.getDouble( cursor.getColumnIndexOrThrow(DatabaseContract.MealEntry.COLUMN_LATITUDE)));
            }
        }
        catch (Exception e) {
            Log.d(TAG, e.getMessage());
            meal =  null;
        }
        finally {
            if(cursor != null)
                cursor.close();
        }

        return meal;
    }

    /**
     * Returns all categories
     *
     * @return - all categories
     */
    public ArrayList<ICategory> getCategories(){
        ArrayList<ICategory> categories = new ArrayList<>();
        String[] projection = {
                DatabaseContract.CategoryEntry.COLUMN_NAME
        };

        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(
                    DatabaseContract.CategoryEntry.TABLE,         // The table to query
                    projection,                               // The columns to return
                    null,                                // The columns for the WHERE clause
                    null,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            while (cursor.moveToNext()) {
                String categoryName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.CategoryEntry.COLUMN_NAME));
                ICategory category = getCategory(categoryName);
                categories.add(category);
            }
        }
        catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        finally {
            if(cursor != null)
                cursor.close();
        }

        return categories;
    }

    /**
     * Updates a meal
     *
     * @param meal - the meal to be updated
     * @return - the number of rows affected
     */
    public int updateMeal(IMeal meal){
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.MealEntry.COLUMN_NAME, meal.getName());
        values.put(DatabaseContract.MealEntry.COLUMN_CATEGORY, meal.getCategory());
        values.put(DatabaseContract.MealEntry.COLUMN_DESCRIPTION, meal.getDescription());
        values.put(DatabaseContract.MealEntry.COLUMN_HEALTHY_SCORE, meal.getHealthyScore());
        values.put(DatabaseContract.MealEntry.COLUMN_TASTE_SCORE, meal.getTasteScore());
        values.put(DatabaseContract.MealEntry.COLUMN_IMAGE_PATH, meal.getImagePath());

        String selection = DatabaseContract.MealEntry.COLUMN_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(meal.getId()) };

        return getWritableDatabase().update(
                DatabaseContract.MealEntry.TABLE,
                values,
                selection,
                selectionArgs);
    }

    /**
     * Deletes a meal
     *
     * @param id - the id of the meal to be deleted
     * @return - the number of rows affected
     */
    public int deleteMeal(long id){
        String selection = DatabaseContract.MealEntry.COLUMN_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(id) };
        return getWritableDatabase().delete(DatabaseContract.MealEntry.TABLE, selection, selectionArgs);
    }

    /**
     * Deletes a category
     *
     * @param categoryName - the name of the category to be deleted
     */
    public void deleteCategory(String categoryName){

        String deleteMealsSQL = "DELETE FROM " + DatabaseContract.MealEntry.TABLE +
                                " WHERE " + DatabaseContract.MealEntry.COLUMN_CATEGORY + "='" + categoryName + "'";
        getWritableDatabase().execSQL(deleteMealsSQL);

        String deleteCategorySQL = "DELETE FROM " + DatabaseContract.CategoryEntry.TABLE +
                " WHERE " + DatabaseContract.CategoryEntry.COLUMN_NAME + "='" + categoryName + "'";
        getWritableDatabase().execSQL(deleteCategorySQL);
    }

    /**
     * Returns a category given a category name
     *
     * @param categoryName - the name of the category to return
     * @return - the category to return
     */
    public ICategory getCategory(String categoryName){
        int iconID = 0;
        ArrayList<IMeal> meals = new ArrayList<>();
        String[] projection = {DatabaseContract.MealEntry.COLUMN_ID};
        String selection = DatabaseContract.MealEntry.COLUMN_CATEGORY + " = ?";
        String[] selectionArgs = {categoryName };

        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(
                    DatabaseContract.MealEntry.TABLE,         // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            while (cursor.moveToNext()) {
                IMeal meal = getMeal(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.MealEntry.COLUMN_ID)));
                meals.add(meal);
            }
        }
        catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        finally {
            if(cursor != null)
                cursor.close();
        }

        String[] projection2 = {DatabaseContract.CategoryEntry.COLUMN_ICON_ID};
        String selection2 = DatabaseContract.CategoryEntry.COLUMN_NAME + " = ?";
        String[] selectionArgs2 = {categoryName };

        Cursor cursor2 = null;
        try {
            cursor2 = getReadableDatabase().query(
                    DatabaseContract.CategoryEntry.TABLE,         // The table to query
                    projection2,                               // The columns to return
                    selection2,                                // The columns for the WHERE clause
                    selectionArgs2,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            while (cursor2.moveToNext()) {
                iconID = cursor2.getInt(cursor2.getColumnIndexOrThrow(DatabaseContract.CategoryEntry.COLUMN_ICON_ID));
            }
        }
        catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        finally {
            if(cursor2 != null)
                cursor2.close();
        }
        return new Category(categoryName, meals, iconID);
    }
}
