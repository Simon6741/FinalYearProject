package my.edu.tarc.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    Button Register;
    EditText Email, Address,Password,Name,confirmPass,Phone;
    TextView goLogin;
    ProgressBar progressBar;
     FirebaseAuth mFireBaseAuth;
     DatabaseReference userDB;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userDB = FirebaseDatabase.getInstance().getReference();

        Register = (Button) findViewById(R.id.button_register);
        Email = (EditText) findViewById(R.id.text_email);
       // Address = (EditText) findViewById(R.id.EtAddress);
        Password = (EditText) findViewById(R.id.edit_text_password);
        //Name = (EditText)findViewById (R.id.Etname);
        Address = (EditText) findViewById(R.id.edit_text_address);
        Name = (EditText)findViewById (R.id.edit_text_name);
        Phone = (EditText)findViewById(R.id.edit_text_phone);
        goLogin = (TextView) findViewById(R.id.text_view_login);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        confirmPass = (EditText) findViewById(R.id.edit_text_password2);
        Register.setOnClickListener(this);
        mFireBaseAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(RegisterActivity.this);

        goLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }



    @Override
    public void onClick(View v) {
       autentication();
    }

/*
    public void addData(){
        String email  = Email.getText().toString().trim();
        String pwd    = Password.getText().toString().trim();
      //  String address = Address.getText().toString().trim();
        //String  name = Name.getText().toString().trim();

        if(!TextUtils.isEmpty(name)){
            String id = userDB.push().getKey();
            UserData userData = new UserData(name,email,pwd,address);
            userDB.child(name).setValue(userData);
        }

    }
*/

    public void autentication(){

        String email  = Email.getText().toString().trim();
        String pwd    = Password.getText().toString().trim();
        String pwd2 = confirmPass.getText().toString().trim();
        String name  = Name.getText().toString().trim();
        String address    = Address.getText().toString().trim();
        String phone  = Phone.getText().toString().trim();
       // String address = Address.getText().toString().trim();
        //  name = Name.getText().toString().trim();

        if(email.isEmpty()){
            Email.setError("Please enter the email");
            Email.requestFocus();

        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Email.setError("invalid email");
            Email.requestFocus();

        }

        else if (pwd.isEmpty()){
            Password.setError("Please enter the password");
            Password.requestFocus();
        }

       if(!pwd.equals(pwd2)){
            confirmPass.setError("Password not match");
            confirmPass.requestFocus();
        }

        else if (!(email.isEmpty() && pwd.isEmpty())){
            mFireBaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this,"Error"+task.getException(), Toast.LENGTH_SHORT).show() ;
                    }
                    else {
                        Toast.makeText(RegisterActivity.this,"Sign Up successful", Toast.LENGTH_SHORT).show() ;
                        ValidateEmail( name, pwd, email, address, phone);
                    }
                }
            });

        }


        else {
            Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();

        }
    }

    private void ValidateEmail(final String name, final String password, final String email, final String address, final String phone) {

        final DatabaseReference userDB;
        userDB = FirebaseDatabase.getInstance().getReference();
        final String uid = mFireBaseAuth.getCurrentUser().getUid();

        userDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!(dataSnapshot.child("Users").child(uid).exists())){

                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("name", name);
                    userdataMap.put("password", password);
                    userdataMap.put("email", email);
                    userdataMap.put("address", address);
                    userdataMap.put("phone", phone);

                    userDB.child("Users").child(uid).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Account created", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent hme = new Intent(RegisterActivity.this,LoginActivity.class);
                                hme.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(hme);

                            }else {
                                loadingBar.dismiss();
                                Toast.makeText(RegisterActivity.this, "Network Not Stable. Please try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(RegisterActivity.this, "This " + email + " already exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please try again by using different email", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
