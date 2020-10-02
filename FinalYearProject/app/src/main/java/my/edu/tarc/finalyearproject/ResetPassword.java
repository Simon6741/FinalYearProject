package my.edu.tarc.finalyearproject;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import my.edu.tarc.finalyearproject.R;

public class ResetPassword extends AppCompatActivity {

    Button resetPassword;
    EditText Email;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        resetPassword = (Button) findViewById(R.id.button_reset_password);
        Email = (EditText) findViewById(R.id.text_email);
        progressBar =(ProgressBar) findViewById(R.id.progressbar);

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Email.getText().toString().trim();
                if(email.isEmpty()){
                    Email.setError("Please enter the email");
                    Email.requestFocus();

                }

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Email.setError("invalid email");
                    Email.requestFocus();

                }


                progressBar.setVisibility(View.VISIBLE);
                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                        if(task.isSuccessful()){
                            Toast.makeText(ResetPassword.this, "Check Your Email  " , Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(ResetPassword.this, "Error,  " + task.getException(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }


        });
    }

}
