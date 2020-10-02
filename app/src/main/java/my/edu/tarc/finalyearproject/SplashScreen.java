package my.edu.tarc.finalyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user == null) {
                    Intent logoIntent = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(logoIntent);
                    finish();
                }else{
                    Intent logoIntent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(logoIntent);
                    finish();
                }


            }
        },SPLASH_TIME_OUT);
    }
}
