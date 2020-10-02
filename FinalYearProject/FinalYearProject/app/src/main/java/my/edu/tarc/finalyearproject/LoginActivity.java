package my.edu.tarc.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity  {

    Button Login;
    EditText Email, Password;
    TextView SignUp,forgetPassword;
    FirebaseAuth mFireBaseAuth;

    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Email = (EditText) findViewById(R.id.text_email);
        Password =(EditText) findViewById(R.id.edit_text_password);
        Login = (Button) findViewById(R.id.button_sign_in);

        mFireBaseAuth = FirebaseAuth.getInstance();
        SignUp = (TextView) findViewById(R.id.text_view_register);
        forgetPassword = (TextView) findViewById(R.id.text_view_forget_password);

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rgt = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(rgt);
            }
        });


   mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFireBaseAuth.getCurrentUser();
                if(mFirebaseUser!= null){
                    Toast.makeText(LoginActivity.this,"Login succesfull", Toast.LENGTH_SHORT).show() ;
                    Intent i = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(i);
                }

            }
        };

    forgetPassword.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LoginActivity.this, ResetPassword.class);
            startActivity(intent);
        }
    });

    Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email  = Email.getText().toString();
                String pwd    = Password.getText().toString();

                if(email.isEmpty()){
                    Email.setError("Please enter the email");
                    Email.requestFocus();

                }
                else if (pwd.isEmpty()){
                    Password.setError("Please enter the password");
                    Password.requestFocus();
                }
                else if (email.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Fill in all the Box", Toast.LENGTH_SHORT).show() ;

                }
              else if (!(email.isEmpty() && pwd.isEmpty())){
                    mFireBaseAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {

                               Toast.makeText(LoginActivity.this, "Login Error,  " + task.getException(), Toast.LENGTH_SHORT).show();
                            } else {
                               // PreferenceUtils.saveEmail(email, this);
                               // PreferenceUtils.savePassword(pwd, this);
                                finish();
                                Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                Intent hme = new Intent(LoginActivity.this,MainActivity.class);
                                hme.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(hme);
                               // hme.putExtra("EMAIL", Email.getText().toString().trim());
                              //  emptyInputEditText();
                              //  startActivity(hme);
                                finish();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
    private void emptyInputEditText(){
       Email.setText(null);
       Password.setText(null);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mFireBaseAuth.getCurrentUser()!= null){
            finish();
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        }

    }
}
