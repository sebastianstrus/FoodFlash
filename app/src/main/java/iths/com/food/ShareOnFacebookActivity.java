package iths.com.food;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;

import java.util.Collections;
import java.util.List;

import iths.com.food.helper.db.DatabaseHelper;
import iths.com.food.model.IMeal;


/**
 * This class handle FB sharing and can publish an image
 */
public class ShareOnFacebookActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private long current_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_on_facebook);

        Intent intent = getIntent();
        current_id = intent.getLongExtra("id",0);

        // Runs the facebook sharing thing
        this.shareOnFacebook();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Login to FB, look for permissions to share things on FB and call publishImage on success
     */
    public void shareOnFacebook() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        List<String> permissionNeeds = Collections.singletonList("publish_actions");

        LoginManager manager = LoginManager.getInstance();
        manager.logInWithPublishPermissions(this, permissionNeeds);
        manager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                publishImage();
            }

            @Override
            public void onCancel() {
                Log.d("test", "cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("test", error.getCause().toString());
            }
        });
    }

    /**
     * Runs when we tap on "Go back"
     * @param view - the view that we tapped on
     */
    public void fbGoBack(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Specifies what image and text to publish on FB
     */
    private void publishImage(){

        DatabaseHelper db = new DatabaseHelper(this);
        IMeal meal = db.getMeal(current_id);

        Bitmap image = BitmapFactory.decodeFile(meal.getImagePath());
        Log.d("LOGTAG", "meal image path i fbactivity: " + meal.getImagePath());
        Log.d("LOGTAG", "Is Bitmap image null? " + (image == null));

        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .setCaption(meal.getName()+" - "+ meal.getDescription())
                .build();

        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        ShareApi.share(content, null);

        TextView fbShareStatus = (TextView) findViewById(R.id.fb_share_status);
        fbShareStatus.setText(String.format("%s is now shared on facebook.", meal.getName()));
    }
}
