package quinton.terence.eugynefamous;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import quinton.terence.eugynefamous.common.LogInActivity;

public class SplashScreenActivity extends AppCompatActivity {

    //delay time
    private static int SPLASH_TIMER  = 5000;

    //variables
    private ImageView splashOrangey;
    private TextView splashname;

    private Animation sideAnim, bottomAnim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        //line for removing the status bar this one directly down after you changed in styles to noactionbar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hides the action bar
        getSupportActionBar().hide();
        setContentView(R.layout.activity_screen);

        //hooks
        splashOrangey = findViewById(R.id.splash_image);
        splashname = findViewById(R.id.splash_text);


        //animation hooks
        sideAnim = AnimationUtils.loadAnimation(this, R.anim.side_anim);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);

        //setting the animations
        splashOrangey.setAnimation(sideAnim);
        splashname.setAnimation(bottomAnim);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //for it to go to the next screen lets say to the user dashboard we use an intent
                //creating a new intent
                //in intent(...this is called first , then this comes after)
                Intent intent =  new Intent( SplashScreenActivity.this, MainActivity.class);
                //start activity usually comes at the end of declaring an intent for starting an activity
                startActivity(intent);
                //this prevents one from returning from the splash screen so it destroys the activity after its called
                finish();

            }
        },SPLASH_TIMER);

        //the SPLASH_TIMER variable is used to pass the time for the delay up there


    }
}