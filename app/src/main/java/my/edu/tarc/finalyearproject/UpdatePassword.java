package my.edu.tarc.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdatePassword extends AppCompatActivity {

    LinearLayout CheckPassword,UpdatePassword;
    Button VerifyPassword,ConfirmUpdate;
    EditText password1,password2,oldpassword;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_update_password);

        CheckPassword = (LinearLayout) findViewById(R.id.layoutPassword);
        UpdatePassword= (LinearLayout) findViewById(R.id.layoutUpdatePassword);
        VerifyPassword = (Button) findViewById(R.id.button_authenticate);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        ConfirmUpdate =(Button) findViewById(R.id.button_update);
        oldpassword = (EditText) findViewById(R.id.edit_text_password);
        password1 = (EditText) findViewById(R.id.edit_text_new_password);
        password2 = (EditText) findViewById(R.id.edit_text_new_password_confirm);
        FirebaseAuth mAuth =FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        CheckPassword.setVisibility(View.VISIBLE);
        UpdatePassword.setVisibility(View.GONE);
        VerifyPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UpdatePassword.this, "Error ", Toast.LENGTH_SHORT).show();

                String password = oldpassword.getText().toString().trim();

                if(password.isEmpty()) {
                    oldpassword.setError("Password required");
                    oldpassword.requestFocus();
                }
                if(user!=null){
                    AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(),password);
                    progressBar.setVisibility(View.VISIBLE);
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressBar.setVisibility(View.GONE);
                                if(task.isSuccessful()){
                                    CheckPassword.setVisibility(View.GONE);
                                    UpdatePassword.setVisibility(View.VISIBLE);
                                }
                                else
                                    Toast.makeText(UpdatePassword.this, "Error " +task.getException(), Toast.LENGTH_SHORT).show();



                            }
                        });
                    }

            }
        });

        ConfirmUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = password1.getText().toString().trim();
                String cpass = password2.getText().toString().trim();
                if(password.isEmpty()||password.length()<6){
                    password1.setError("At least 6 character required");
                    password1.requestFocus();
                }

                if(!password.equals(cpass)){
                    password2.setError("password do not match");
                    password2.requestFocus();
                }

                if(user!=null){
                    progressBar.setVisibility(View.VISIBLE);
                    user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(UpdatePassword.this, "Successfull,  " , Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(UpdatePassword.this, AccountFragment.class);
                                startActivity(intent);
                            }

                            else {
                                Toast.makeText(UpdatePassword.this, "Error " +task.getException(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }

            }
        });





    }
}
