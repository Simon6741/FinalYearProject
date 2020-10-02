package my.edu.tarc.finalyearproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.ar.core.Config;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import com.bumptech.glide.Glide;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import de.hdodenhof.circleimageview.CircleImageView;
import my.edu.tarc.finalyearproject.Domain.ImageUploadDomain;
import my.edu.tarc.finalyearproject.Prevalent.Prevalent;

import static android.app.Activity.RESULT_OK;
import static android.os.Build.VERSION_CODES.O;

public class AccountFragment extends Fragment  {

    private static final int CHOOSE_IMAGE = 101;

    Button CallLogin,callRegister;
    TextView profileName,profileEmail,profileAddress;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
  //  Button ViewAccount;
    Button logout,saveChange,btnOrder;
    EditText ShowName;
    String profileImageUrl;
    Uri uriProfileImage;
    ImageView ProfilePicture;
    ProgressBar progressBarpic,progressBar;
    TextView EditEmail,EditPassword,EditPhone;
    TextView VerifyEmail;
    private DatabaseReference mDatabaseRef;
    String url;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       return inflater.inflate(R.layout.fragment_profile,container,false);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       // ViewAccount =(Button) getView().findViewById(R.id.ViewAccount);
        //logout = (Button) getView().findViewById(R.id.button2);
        ProfilePicture =(ImageView) getView().findViewById(R.id.image_view);
        ShowName = (EditText)getView().findViewById(R.id.edit_text_name);
        saveChange = (Button) getView().findViewById(R.id.button_save);
        progressBarpic =(ProgressBar) getView().findViewById(R.id.progressbar_pic);
        progressBar =(ProgressBar) getView().findViewById(R.id.progressbar);
        EditEmail = (TextView) getView().findViewById(R.id.text_email);
        EditPassword = (TextView) getView().findViewById(R.id.text_password);
        EditPhone = (TextView) getView().findViewById(R.id.text_phone);
        VerifyEmail =(TextView) getView().findViewById(R.id.text_not_verified);
        btnOrder = (Button) getView().findViewById(R.id.button_myorder);
        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("image");

        EditPhone.setText(Prevalent.currentOnlineUser.getPhone());

/*        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            ImageUploadDomain user = dataSnapshot.child("image").child("LvZF_T7TWLCDJCf7obv").getValue(ImageUploadDomain.class);
                                                            url=user.getUrl();

                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });*/


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();



        ProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();
            }
        });

        EditEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ChangeEmail.class);
                startActivity(intent);
            }
        });

        EditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UpdatePassword.class);
                startActivity(intent);
            }
        });


        loadUserInformation();



        saveChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveUserInformation();
            }
        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadFragment(new OrderFragment());
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            getActivity().finish();
            startActivity(new Intent(getActivity(), MainActivity.class));
        }
    }

    private void loadUserInformation() {
        final FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            EditEmail.setText(user.getEmail());

            if (user.getPhotoUrl() != null) {


                Glide.with(getContext())
                        .load(url)
                        .fitCenter()
                        .into(ProfilePicture);
            }

            if (user.getDisplayName() != null) {
                ShowName.setText(Prevalent.currentOnlineUser.getName());
            }

           if (user.isEmailVerified()) {
              VerifyEmail.setVisibility(View.VISIBLE);
               VerifyEmail.setText("Email Verified");
            } else {
               VerifyEmail.setVisibility(View.VISIBLE);
                VerifyEmail.setText("Email Not Verified (Click to Verify)");
                VerifyEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getActivity(), "Verification Email Sent", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }
    }

    public void updateName(){
        String displayName = ShowName.getText().toString();

        if (displayName.isEmpty()) {
            ShowName.setError("Name required");
            ShowName.requestFocus();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .build();

            progressBar.setVisibility(View.VISIBLE);

            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(getActivity(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();

                        }
                    });
        }

    }

    private void saveUserInformation() {


        String displayName = ShowName.getText().toString();

        if (displayName.isEmpty()) {
            ShowName.setError("Name required");
            ShowName.requestFocus();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null && profileImageUrl != null) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(Prevalent.currentOnlineUser.getName())
                    .setPhotoUri(Uri.parse(profileImageUrl))
                    .build();

            progressBar.setVisibility(View.VISIBLE);

            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(getActivity(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();

                        }
                    });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uriProfileImage);
                ProfilePicture.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebaseStorage() {
        StorageReference profileImageRef =
                FirebaseStorage.getInstance().getReference("profilepics/" + System.currentTimeMillis() + ".jpg");

        if (uriProfileImage != null) {
            progressBarpic.setVisibility(View.VISIBLE);
            profileImageRef.putFile(uriProfileImage)
                    .addOnSuccessListener(getActivity(),new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            {

                                progressBarpic.setVisibility(View.GONE);
                                  profileImageUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();

                            }
                        }
                    }

                    )
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBarpic.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "error" +e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);
    }



  /*  public void uploadPhoto(){



        String displayName = ShowName.getText().toString();
        FirebaseUser user = mAuth.getCurrentUser();

        if (displayName.isEmpty()) {
            ShowName.setError("Name required");
            ShowName.requestFocus();
            return;
        }

        if (user != null && profileImageUrl != null) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setPhotoUri(Uri.parse(profileImageUrl))
                    .build();

            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }


  public void loadUserInformation(){
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null){
            if (user.getPhotoUrl() != null) {
                Glide.with(getActivity())
                        .load(user.getPhotoUrl().toString())
                        .into(ProfilePicture);
            }

            if (user.getDisplayName() != null) {
                ShowName.setText(user.getDisplayName());
            }
            else{
                Intent intent =  new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();
       if (mAuth.getCurrentUser() == null) {
                getActivity().finish();
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uriProfileImage);
                ProfilePicture.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebaseStorage() {
        StorageReference profileImageRef =
                FirebaseStorage.getInstance().getReference("profilepics/" + System.currentTimeMillis() + ".jpg");

        if (uriProfileImage != null) {
            progressBar.setVisibility(View.VISIBLE);
            profileImageRef.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.GONE);
                            profileImageUrl = profileImageRef.getDownloadUrl().toString();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }



    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);
    }*/
  private void loadFragment(Fragment fragment) {
      // load fragment
      FragmentManager fragmentManager = getFragmentManager();
      FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
      fragmentTransaction.replace(R.id.fragment_container, fragment);
      fragmentTransaction.addToBackStack(null);
      fragmentTransaction.commit();
  }

}

