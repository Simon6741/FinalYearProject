package my.edu.tarc.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    ImageButton VisualizeButton;
    Button CallLogin,callRegister;
    FirebaseAuth mFIreBaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation_view);
        bottomNav.setOnNavigationItemSelectedListener(navlister);

        loadFragment(new HomeFragment());

    }
   private BottomNavigationView.OnNavigationItemSelectedListener navlister =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectFragment =null;

            switch (menuItem.getItemId()){
                     case R.id.nav_account:
                         selectFragment = new AccountFragment();
                         break;
                     case R.id.nav_Wallet:
                    selectFragment = new EwalletFragment();
                    break;
                case R.id.nav_Visualize:
                    selectFragment = new VisualizeFragment();
                    break;

                case R.id.nav_Store:
                    selectFragment = new StoreFragment();
                    break;

                case R.id.nav_home:
                   selectFragment = new HomeFragment();
                    break;


            }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectFragment).commit();
                 return true;
                }
            };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFIreBaseAuth = FirebaseAuth.getInstance();

        if (mFIreBaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuLogout:
                mFIreBaseAuth.getInstance().signOut();
                finish();
               startActivity(new Intent(this, LoginActivity.class));

                break;
        }

        return true;
    }


}




