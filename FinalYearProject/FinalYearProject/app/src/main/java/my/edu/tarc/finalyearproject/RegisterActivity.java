package my.edu.tarc.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    Button Register;
    EditText Email, Address,Password,Name,confirmPass;
    TextView goLogin;
    ProgressBar progressBar;
     FirebaseAuth mFireBaseAuth;
     DatabaseReference userDB;

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
        goLogin = (TextView) findViewById(R.id.text_view_login);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        confirmPass = (EditText) findViewById(R.id.edit_text_password2);
        Register.setOnClickListener(this);
        mFireBaseAuth = FirebaseAuth.getInstance();
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
       // String address = Address.getText().toString().trim();
        //  name = Name.getText().toString().trim();

        if(email.isEmpty()){
            Email.setError("Please enter the email");
            Email.requestFocus();

        }
        else if (pwd.isEmpty()){
            Password.setError("Please enter the password");
            Password.requestFocus();
        }

      /*  if(pwd!=pwd2){
            confirmPass.setError("Password not match");
            confirmPass.requestFocus();
        }*/

        else if (!(email.isEmpty() && pwd.isEmpty())){
            mFireBaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this,"Sign Up Unsuccessful" + task.getException(), Toast.LENGTH_SHORT).show() ;
                    }
                    else {
                        Toast.makeText(RegisterActivity.this,"Sign Up successful", Toast.LENGTH_SHORT).show() ;
                        startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                    }
                }
            });

        }



        else {
            Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();

        }
    }


}
