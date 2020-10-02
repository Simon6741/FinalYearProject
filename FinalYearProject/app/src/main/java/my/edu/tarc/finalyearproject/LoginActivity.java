package my.edu.tarc.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import my.edu.tarc.finalyearproject.Prevalent.Prevalent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity  {

    Button Login;
    EditText Email, Password;
    TextView SignUp,forgetPassword;
    FirebaseAuth mFireBaseAuth;
    private String parentDbName = "Users";
    private DatabaseReference mFirebaseRef;
    private ProgressDialog loadingBar;
    private TextView Admin, notAdmin;
    private TextView deliver;

    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Email = (EditText) findViewById(R.id.text_email);
        Password =(EditText) findViewById(R.id.edit_text_password);
        Login = (Button) findViewById(R.id.button_sign_in);

        mFirebaseRef = FirebaseDatabase.getInstance().getReference();
        mFireBaseAuth = FirebaseAuth.getInstance();
        SignUp = (TextView) findViewById(R.id.text_view_register);
        forgetPassword = (TextView) findViewById(R.id.text_view_forget_password);
        Admin = (TextView)findViewById(R.id.admin_panel);
        notAdmin = (TextView)findViewById(R.id.user_panel);
        deliver = (TextView)findViewById(R.id.txtdeliver);
        loadingBar = new ProgressDialog(LoginActivity.this);

        Admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Login.setText("Login Admin");
                Admin.setVisibility(View.INVISIBLE);
                notAdmin.setVisibility(View.VISIBLE);
                parentDbName = "Admins";
            }
        });

        notAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login.setText("Login");
                Admin.setVisibility(View.VISIBLE);
                notAdmin.setVisibility(View.INVISIBLE);
                parentDbName = "Users";
            }
        });

        deliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, deliverLoginActivity.class);
                startActivity(i);
            }
        });

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
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Email.setError("invalid email");
                    Email.requestFocus();

                }

                else if (!(email.isEmpty() && pwd.isEmpty())){
                    /*mFireBaseAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
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
                    });*/
                    if (parentDbName.equals("Users")){

                        UserLogin(email,pwd);
                    } else if (parentDbName.equals("Admins")) {

                        AdminLogin(email,pwd);
                    }
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

    public void UserLogin(String email, String pwd){

        mFireBaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    mFirebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            UserData user = dataSnapshot.child("Users").child(mFireBaseAuth.getUid()).getValue(UserData.class);
                            Prevalent.currentOnlineUser = user;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    loadingBar.dismiss();
                    Intent hme = new Intent(LoginActivity.this,MainActivity.class);
                    hme.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(hme);
                }else{
                    Toast.makeText(LoginActivity.this, "Email and Password are not match", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }
        });
    }

    private void AdminLogin(final String email, final String pwd) {


        final DatabaseReference userDB;
        userDB = FirebaseDatabase.getInstance().getReference();

        userDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    UserData user = dataSnapshot.child(parentDbName).getValue(UserData.class);

                    if (user.getEmail().equals(email)) {
                        if (user.getPassword().equals(pwd)) {

                            if (parentDbName.equals("Admins")) {
                                Toast.makeText(LoginActivity.this, "(Admin)Login Successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent i = new Intent(LoginActivity.this, adminHomeActivity.class);
                                startActivity(i);
                            } else if (parentDbName.equals("Users")) {
                                Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Prevalent.currentOnlineUser = user;
                                /*Intent i = new Intent(getContext(), HomeActivity.class);
                                startActivity(i);*/
                                Intent hme = new Intent(LoginActivity.this, MainActivity.class);
                                hme.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(hme);
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Email and Password are not match", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
