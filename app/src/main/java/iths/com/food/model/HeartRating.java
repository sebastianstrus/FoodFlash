package iths.com.food.model;

import static iths.com.food.MainActivity.PACKAGE_NAME;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import iths.com.food.R;


public class HeartRating {

    private Activity activity;
    private Context context;
    private static int healthGrade;
    private static int tasteGrade;
    private View layoutView;

    public HeartRating(View layoutView, Context context, Activity activity) {
        this.layoutView = layoutView;
        this.context = context;
        this.activity = activity;
    }

    public void fillHearts(View view) {
        ImageView iv = (ImageView) view;
        ViewGroup viewParent = (ViewGroup) iv.getParent();
        int resourceID = view.getId();
        int heartNr;
        String idStr = context.getResources().getResourceEntryName(resourceID);
        String healthOrTaste;

        if( (viewParent).getId() == R.id.edit_health_hearts) {
            healthOrTaste = "edit_heart_health_";
            heartNr = Integer.parseInt(idStr.replace(healthOrTaste, ""));
            healthGrade = heartNr;
        } else {
            healthOrTaste = "edit_heart_taste_";
            heartNr = Integer.parseInt(idStr.replace(healthOrTaste, ""));
            tasteGrade = heartNr;
        }
        for(int i = 1; i <= 10; i++) {
            int imgId = context.getResources().getIdentifier(healthOrTaste + i, "id", PACKAGE_NAME);
            ImageView heart = (ImageView) activity.findViewById(imgId);
            if(i <= heartNr) {
                heart.setImageResource(R.drawable.filled_heart);
            } else {
            heart.setImageResource(R.drawable.empty_heart);
            }
        }
        setAverageGrade(true);
    }


    public void setHearts(boolean isEditScreen, int healthGrade, int tasteGrade) {

        this.healthGrade = healthGrade;
        this.tasteGrade = tasteGrade;

        String edit = "";
        if(isEditScreen) {
            edit = "edit_";
        }

        for(int i = 1; i <= 10; i++) {
            int imgId = context.getResources().getIdentifier(edit + "heart_health_" + i, "id", PACKAGE_NAME);
            ImageView heart = (ImageView) layoutView.findViewById(imgId);
            if(i <= healthGrade) {
                heart.setImageResource(R.drawable.filled_heart);
            } else {
                heart.setImageResource(R.drawable.empty_heart);
            }
        }

        for(int i = 1; i <= 10; i++) {
            int imgId = context.getResources().getIdentifier(edit + "heart_taste_" + i, "id", PACKAGE_NAME);
            ImageView heart = (ImageView) layoutView.findViewById(imgId);
            if(i <= tasteGrade) {
                heart.setImageResource(R.drawable.filled_heart);
            } else {
                heart.setImageResource(R.drawable.empty_heart);
            }
        }

        setAverageGrade(isEditScreen);
    }

    private void setAverageGrade(boolean isEditScreen) {
        TextView averageGradeTV;
        if(isEditScreen) {
            averageGradeTV = (TextView) layoutView.findViewById(R.id.edit_average_number);
        } else {
            averageGradeTV = (TextView) layoutView.findViewById(R.id.average_number);
        }
        double averageGrade = ((double) (healthGrade + tasteGrade) ) / 2;
        averageGradeTV.setText(Double.toString(averageGrade));
    }

    public static int getHealthGrade() {
        return healthGrade;
    }
    public static int getTasteGrade() {
        return tasteGrade;
    }
}