package iths.com.food.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import iths.com.food.helper.db.DatabaseHelper;
import iths.com.food.model.IMeal;
import iths.com.food.R;


public class MealAdapter extends ArrayAdapter<String> {

    public MealAdapter(Context context, ArrayList<String> mealIds) {
        super(context, R.layout.custom_row, mealIds);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.custom_row, parent, false);

        String mealID = getItem(position);
        Long id = Long.valueOf(mealID);
        DatabaseHelper db = new DatabaseHelper(getContext());
        IMeal meal = db.getMeal(id);

        TextView textView = (TextView) customView.findViewById(R.id.categoryName);
        textView.setText(meal.getName());

        String imagePath = meal.getImagePath();
        Uri imageUri = Uri.parse(imagePath);
        ImageView imageView = (ImageView) customView.findViewById(R.id.iconThumbnail);
        Bitmap image = BitmapFactory.decodeFile(MyCamera.getThumbnailFilePath(imageUri.getPath()));
        try {
            imageView.setImageBitmap(image);
        } catch (Exception e) {
            imageView.setImageResource(R.drawable.testmeal_thumbnail);
        }

        TextView averageScore = (TextView) customView.findViewById(R.id.average_grade_text);
        averageScore.setText(String.format("Score: %s", meal.getTotalScore()));

        RatingBar ratingbar = (RatingBar) customView.findViewById(R.id.categoryRatingBar);
        ratingbar.setRating((float)meal.getTotalScore());

        TextView dateUploaded = (TextView) customView.findViewById(R.id.date_of_meal);
        dateUploaded.setText(meal.getDateTime());

        db.close();

        return customView;

    }
}
