package my.edu.tarc.finalyearproject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class RegisterFragment extends Fragment {

    private Button Register;
    private EditText Email, Address,Password,Name,Phone;
    FirebaseAuth mFireBaseAuth;
    DatabaseReference userDB;

    private ProgressDialog loadingBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_register,container,false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Register = (Button) getView().findViewById(R.id.signup);
        Email = (EditText) getView().findViewById(R.id.EtEmail);
        Address = (EditText) getView().findViewById(R.id.edit_text_address);
        Password = (EditText) getView().findViewById(R.id.Etpassword);
        Name = (EditText)getView().findViewById (R.id.edit_text_name);
        Phone = (EditText)getView().findViewById(R.id.edit_text_phone);
        loadingBar = new ProgressDialog(getContext());
        userDB = FirebaseDatabase.getInstance().getReference();
        mFireBaseAuth = FirebaseAuth.getInstance();

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
            }
        });

    }

    public void addData(){
        final String email  = Email.getText().toString();
        final String password    = Password.getText().toString();
        final String address = Address.getText().toString();
        final String name = Name.getText().toString();
        final String phone = Phone.getText().toString();

        if(TextUtils.isEmpty(name)){

            Toast.makeText(getContext(),"Please write your Name...", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(password)){

            Toast.makeText(getContext(),"Please write your Account Password...", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(email)){

            Toast.makeText(getContext(),"Please write your Email Address...", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(address)){

            Toast.makeText(getContext(),"Please write your Delivery Address...", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(phone)){

            Toast.makeText(getContext(),"Please write your Phone number...", Toast.LENGTH_SHORT).show();
        }else if (!name.equals("")&&!phone.equals("")&&!email.equals("")&&!password.equals("")&&!address.equals("")){
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mFireBaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {


                        ValidateEmail( name, password, email, address, phone);
                        }
                    }
                });
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
                                Toast.makeText(getContext(), "Account created", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                loadFragment(new AccountFragment());

                            }else {
                                loadingBar.dismiss();
                                Toast.makeText(getContext(), "Network Not Stable. Please try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(getContext(), "This " + email + " already exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(getContext(), "Please try again by using different email", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
