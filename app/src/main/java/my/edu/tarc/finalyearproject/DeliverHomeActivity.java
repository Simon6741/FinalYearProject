package my.edu.tarc.finalyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class DeliverHomeActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_home);

        loadFragment(new HomeFragment());
        BottomNavigationView bottomNav = findViewById(R.id.nav_view);
        bottomNav.setOnNavigationItemSelectedListener(navlister);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navlister =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectFragment =null;

                    switch (menuItem.getItemId()){
                        case R.id.navigation_deliver_home:
                            selectFragment = new DeliverHomeFragment();
                            break;
                        case R.id.navigation_dashboard:
                            selectFragment = new DeliveredFragment();
                            break;


                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.deliver_container,selectFragment).commit();
                    return true;
                }
            };

    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){

            loadFragment(new DeliverHomeFragment());
        }else{
            Toast.makeText(DeliverHomeActivity.this, "PLEASE LOGIN", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(DeliverHomeActivity.this, deliverLoginActivity.class);
            startActivity(i);
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
                mAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, LoginActivity.class));

                break;
        }

        return true;
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.deliver_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}
