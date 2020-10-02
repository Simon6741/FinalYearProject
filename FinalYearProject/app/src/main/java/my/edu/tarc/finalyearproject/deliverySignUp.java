package my.edu.tarc.finalyearproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class deliverySignUp extends AppCompatActivity {

    private Button signUp,login;
    private EditText nameInput, phoneInput, emailInput,passwordInput, addressInput;

    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_sign_up);
        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);
        nameInput = (EditText)findViewById(R.id.Dname);
        phoneInput = (EditText)findViewById(R.id.DPhone);
        emailInput = (EditText)findViewById(R.id.DEmail);
        passwordInput = (EditText)findViewById(R.id.Dpassword);
        addressInput = (EditText)findViewById(R.id.DAddress);
        login = (Button)findViewById(R.id.btnLoginD);
        signUp = (Button)findViewById(R.id.signupDelivery);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(deliverySignUp.this, deliverLoginActivity.class);
                startActivity(i);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerDeliver();
            }
        });
    }

    private void registerDeliver() {


        final String name = nameInput.getText().toString();
        final String phone = phoneInput.getText().toString();
        final String email = emailInput.getText().toString();
        final String password = passwordInput.getText().toString();
        final String address = addressInput.getText().toString();

        if (!name.equals("")&&!phone.equals("")&&!email.equals("")&&!password.equals("")&&!address.equals("")){

            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        final DatabaseReference rootRef;
                        rootRef = FirebaseDatabase.getInstance().getReference();

                        String did = mAuth.getCurrentUser().getUid();

                        HashMap<String, Object> deliverMap = new HashMap<>();
                        deliverMap.put("did", did);
                        deliverMap.put("name", name);
                        deliverMap.put("phone", phone);
                        deliverMap.put("email", email);
                        deliverMap.put("address", address);
                        deliverMap.put("password", password);

                        rootRef.child("Deliver").child(did).updateChildren(deliverMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                loadingBar.dismiss();
                                Toast.makeText(deliverySignUp.this, "Register Successfully", Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(deliverySignUp.this, deliverLoginActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();
                            }
                        });
                    }
                }
            });
        }else {

            Toast.makeText(deliverySignUp.this,"Please complete the registration form", Toast.LENGTH_SHORT).show();
        }
    }
}
