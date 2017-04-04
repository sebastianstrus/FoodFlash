package iths.com.food.fragments;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import iths.com.food.R;
import iths.com.food.helper.CategoryAdapter;
import iths.com.food.helper.DialogHandler;
import iths.com.food.helper.GPSHelper;
import iths.com.food.helper.SwipeDismissListViewTouchListener;
import iths.com.food.helper.db.DatabaseHelper;
import iths.com.food.model.ICategory;

public class CategoryFragment extends Fragment {

    public static final String CHOSEN_CATEGORY = "category";
    public ArrayList<String> foodTypesArrayList;
    ListAdapter listAdapter;
    DatabaseHelper db;
    CategoryAdapter adapter;
    private MediaPlayer mySound;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mySound = MediaPlayer.create(getActivity(), R.raw.swipe);
        View v = inflater.inflate(R.layout.fragment_category, container, false);
        setHasOptionsMenu(true);
        Toolbar myToolbar = (Toolbar) v.findViewById(R.id.category_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        db = new DatabaseHelper(this.getActivity().getApplicationContext());
        ArrayList<ICategory> categories = db.getCategories();
        categories = sortCategories(categories);
        foodTypesArrayList = new ArrayList<>(categories.size());
        for (int i = 0; i < categories.size(); i++) {
            foodTypesArrayList.add(categories.get(i).getName());
        }

        adapter = new CategoryAdapter(getActivity(), foodTypesArrayList);
        ListView listView = (ListView) v.findViewById(R.id.fragmentCategory);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String chosenCategory = String.valueOf(adapterView.getItemAtPosition(i));
                        showCategory(chosenCategory);
                    }
                }
        );

        /**
         * Instance of the class SwipeDismissListViewTouchListener required to remove categories
         * with TouchListener (swiping).
         */
        SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(
                listView,
                new SwipeDismissListViewTouchListener.DismissCallbacks() {
                    @Override
                    public boolean canDismiss(int position) {
                        return true;
                    }
                    @Override
                    public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                        for (int position : reverseSortedPositions) {
                            openDialogHandler(position);
                        }
                    }
                });

            listView.setOnTouchListener(touchListener);
            db.close();
            return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.meal_category_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_category_item:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new NewCategoryFragment()).commit();
                break;
            default:
                System.out.println("error inflating the menu");
        }
        return true;
    }

    @Override
    public void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        ((BaseAdapter) listAdapter).notifyDataSetChanged();
    }

    /**
     * This method opens a confirmation window for deleting of categories.
     * @param position - position of category in list.
     */
    public void openDialogHandler(int position) {
        DialogHandler appDialog = new DialogHandler();

        int numMeals = db.getCategory(adapter
                .getItem(position))
                .getMeals().size();

        appDialog.Confirm(getActivity(), "Are you sure you want to delete?", "There is " + numMeals+ " meals in this category.",
                "Cancel", "OK", okPressed(position), cancelPressed());
    }

    /**
     * This method allows to delete categories.
     * @param position - the position of category in the list.
     * @return Runnable - the object to execute.
     */
    public Runnable okPressed(int position){
        final int finalPosition = position;
        return new Runnable() {
            public void run() {
                db.deleteCategory(foodTypesArrayList.get(finalPosition));
                CategoryFragment newFragment = new CategoryFragment();
                getFragmentManager().beginTransaction().replace(R.id.container, newFragment).addToBackStack(null).commit();
                mySound.start();
            }
        };
    }

    /**
     * This method allows to cancel the removal of categories.
     * @return Runnable - the object to execute.
     */
    public Runnable cancelPressed(){
        return new Runnable() {
            public void run() {
            }
        };
    }

    public ArrayList<ICategory> sortCategories(ArrayList<ICategory> categories){
        Collections.sort(categories, new Comparator<ICategory>() {
            @Override
            public int compare(ICategory c1, ICategory c2) {
                if (c1.getAverageScore() > c2.getAverageScore())
                    return -1;
                else if (c1.getAverageScore() < c2.getAverageScore())
                    return 1;
                else return 0;
            }
        });
        return categories;
    }

    private void showCategory(String category) {
        MealListFragment newFragment = new MealListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CHOSEN_CATEGORY, category);
        newFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.container, newFragment).addToBackStack(null).commit();
    }
}
