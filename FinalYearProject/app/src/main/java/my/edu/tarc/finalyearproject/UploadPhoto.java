package my.edu.tarc.finalyearproject;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import my.edu.tarc.finalyearproject.Domain.ImageUploadDomain;

public class UploadPhoto extends AppCompatActivity {

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private ImageView imageView;
    private EditText txtImageName;
    private EditText txtDescription;
    private EditText txtPrice;
    private Spinner categorySpinner;
    private Uri imgUri;
    Button upload;
    private Context mContext;
    private ArrayList<ImageUploadDomain> mImgList;

    public static final String FB_STORAGE_PATH = "image/";
    public static final String FB_DATABASE_PATH = "image";
    public static final int REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);

        imageView = (ImageView) findViewById(R.id.image_view);
       // txtImageName = findViewById(R.id.txtImageName);
       // txtDescription = findViewById(R.id.txtDescription);
       // txtPrice = findViewById(R.id.txtPrice);
        // categorySpinner = findViewById(R.id.categorySpinner);
        upload = (Button) findViewById(R.id.button_save);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select image"), REQUEST_CODE);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgUri!=null){
                    final ProgressDialog dialog = new ProgressDialog(UploadPhoto.this);
                    dialog.setTitle("Uploading image");
                    dialog.show();

                    StorageReference ref = mStorageRef.child(FB_STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(imgUri));
                    ref.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            dialog.dismiss();

                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri downloadUrl = urlTask.getResult();

                            final String sdownload_url = String.valueOf(downloadUrl);
//                            Double price = Double.parseDouble(txtPrice.getText().toString().trim());

                            String uploadId = mDatabaseRef.push().getKey();
                            ImageUploadDomain imageUpload = new ImageUploadDomain( uploadId , sdownload_url);
                            mDatabaseRef.child(uploadId).setValue(imageUpload);
                            Toast.makeText(getApplicationContext(), "Image uploaded", Toast.LENGTH_LONG).show();
                    /*Toast.makeText(getApplicationContext(), "Image uploaded", Toast.LENGTH_LONG).show();
                    ImageUploadDomain imageUpload = new ImageUploadDomain(txtImageName.getText().toString(), taskSnapshot.getStorage().getDownloadUrl().toString());
                    String uploadId = mDatabaseRef.push().getKey();
                    mDatabaseRef.child(uploadId).setValue(imageUpload);*/
                        }

                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    dialog.dismiss();
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                                    double progress = (100*taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                    dialog.setMessage("Upload" + (int)progress + "@");
                                }
                            });
                }else{
                    Toast.makeText(getApplicationContext(), "Please select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



    public void btnBrowse_Click(View v){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image"), REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null){
            imgUri = data.getData();

            try{
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                imageView.setImageBitmap(bm);
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public String getImageExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @SuppressWarnings("VisibleForTests")
    public void btnUpload_Click(View v){
        if (imgUri!=null){
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Uploading image");
            dialog.show();

            StorageReference ref = mStorageRef.child(FB_STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(imgUri));
            ref.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    dialog.dismiss();

                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful());
                    Uri downloadUrl = urlTask.getResult();

                    final String sdownload_url = String.valueOf(downloadUrl);
                   // Double price = Double.parseDouble(txtPrice.getText().toString().trim());

                    String uploadId = mDatabaseRef.push().getKey();
                    ImageUploadDomain imageUpload = new ImageUploadDomain( uploadId,sdownload_url);
                    mDatabaseRef.child(uploadId).setValue(imageUpload);
                    Toast.makeText(getApplicationContext(), "Image uploaded", Toast.LENGTH_LONG).show();
                    /*Toast.makeText(getApplicationContext(), "Image uploaded", Toast.LENGTH_LONG).show();
                    ImageUploadDomain imageUpload = new ImageUploadDomain(txtImageName.getText().toString(), taskSnapshot.getStorage().getDownloadUrl().toString());
                    String uploadId = mDatabaseRef.push().getKey();
                    mDatabaseRef.child(uploadId).setValue(imageUpload);*/
                }

            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100*taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            dialog.setMessage("Upload" + (int)progress + "@");
                        }
                    });
        }else{
            Toast.makeText(getApplicationContext(), "Please select an image", Toast.LENGTH_SHORT).show();
        }
    }

    public void btnShowListImage_Click(View v){
        //Intent i = new Intent(this, ImageListActivity.class);
        //startActivity(i);
    }

    public void btnLogout(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
