package my.edu.tarc.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavDirections;

import android.os.Bundle;
import android.os.PatternMatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.core.view.Change;

public class ChangeEmail extends AppCompatActivity {

    ProgressBar progressBar;
    LinearLayout layoutpassword,layoutEmail;
    EditText editEmail,EditPassword;
    Button ConfirmPassword,ConfirmEmail;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_update_email);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        layoutpassword = (LinearLayout) findViewById(R.id.layoutPassword);
        layoutEmail = (LinearLayout) findViewById(R.id.layoutUpdateEmail);
        editEmail = (EditText) findViewById(R.id.edit_text_email);
        EditPassword = (EditText) findViewById(R.id.edit_text_password);
        ConfirmPassword = (Button) findViewById(R.id.button_authenticate);
        ConfirmEmail = (Button) findViewById(R.id.button_update);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        layoutpassword.setVisibility(View.VISIBLE);
        layoutEmail.setVisibility(View.GONE);

        ConfirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = EditPassword.getText().toString().trim();
                if(password.isEmpty()){
                    EditPassword.setError("Password Required");
                    EditPassword.requestFocus();

                }
                if(user!=null){
                    AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(),password);
                    progressBar.setVisibility(View.VISIBLE);
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.GONE);
                            if(task.isSuccessful()){
                                layoutpassword.setVisibility(View.GONE);
                                layoutEmail.setVisibility(View.VISIBLE);
                            }

                        }
                    });
                }
            }
        });

        ConfirmEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString().trim();

                if(email.isEmpty()){
                    editEmail.setError("Email required");
                    editEmail.requestFocus();
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    editEmail.setError("invalid email");
                    editEmail.requestFocus();

                }

                progressBar.setVisibility(View.VISIBLE);
                if(user!=null){
                    user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.GONE);
                            if(task.isSuccessful()){
                                Toast.makeText(ChangeEmail.this, "Successfull,  " , Toast.LENGTH_SHORT).show();

                            }else
                                Toast.makeText(ChangeEmail.this, "Error  "+task.getException() , Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });





    }
}
