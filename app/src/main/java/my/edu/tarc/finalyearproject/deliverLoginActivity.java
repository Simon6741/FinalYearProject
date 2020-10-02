package my.edu.tarc.finalyearproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class deliverLoginActivity extends AppCompatActivity {

    private Button login;
    private TextView signUp;
    private EditText emailInput,passwordInput;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_login);

        login = (Button)findViewById(R.id.deliverLogin);
        signUp = (TextView)findViewById(R.id.deliverSignUp);
        emailInput = (EditText)findViewById(R.id.deliverLoginEmail);
        passwordInput = (EditText)findViewById(R.id.deliverLoginPassword);
        loadingBar = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginDeliver();
            }
        });
        
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(deliverLoginActivity.this, deliverySignUp.class);
                startActivity(i);
            }
        });
    }

    private void loginDeliver() {

        final String email = emailInput.getText().toString();
        final String password = passwordInput.getText().toString();

        if (!email.equals("")&&!password.equals("")) {

            loadingBar.setTitle("Deliver Account Login");
            loadingBar.setMessage("Please wait, while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){

                        Intent i = new Intent(deliverLoginActivity.this, DeliverHomeActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }
                }
            });

        }else{
            Toast.makeText(deliverLoginActivity.this,"Please complete the login form", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
