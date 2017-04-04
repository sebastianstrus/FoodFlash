package iths.com.food;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by sebastianstrus on 11/12/16. WelcomeActivity class represents a launch screen.
 * When animations finish loads MainActivity automatically.
 */
public class WelcomeActivity extends AppCompatActivity {

    protected AlphaAnimation fadeIn;
    AnimatorSet mAnimationSet;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //fade in intro text
        TextView tvIntro = (TextView) findViewById(R.id.intro_text);
        fadeIn = new AlphaAnimation(0.0f , 1.0f );
        fadeIn.setDuration(3000);
        tvIntro.startAnimation(fadeIn);
        //fadeIn.setStartOffset(1000);

        // Logo animation
        final ImageView img = (ImageView) findViewById(R.id.imageView);
        final Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);

        // Logo fade out automatically
        final Animation fadeOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_fade_out);
        img.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {

                //ObjectAnimator fadeIn = ObjectAnimator.ofFloat(myView, "alpha", .3f, 1f);
                ObjectAnimator fadeIn = ObjectAnimator.ofFloat(img, "alpha", .3f, 1f);
                fadeIn.setDuration(500);
                ObjectAnimator fadeOut = ObjectAnimator.ofFloat(img, "alpha",  1f, .3f);
                fadeOut.setDuration(500);

                mAnimationSet = new AnimatorSet();
                mAnimationSet.play(fadeIn).after(fadeOut);
                mAnimationSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        //mAnimationSet.start();
                    }
                });
                mAnimationSet.start();

                // When animation finish loads MainActivity
                img.startAnimation(fadeOutAnimation);
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }
}
