package my.edu.tarc.finalyearproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import my.edu.tarc.finalyearproject.Utils.BitmapUtils;

public class VisualizeActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_STORAGE_PERMISSION = 1;

    private static final String FILE_PROVIDER_AUTHORITY = "com.example.android.provider";

    private AppExecutor mAppExcutor;

    private ImageView mImageView;

    private Button mStartCamera;

    private String mTempPhotoPath;

    private Bitmap mResultsBitmap;

    private Button mClear,mSave,mShare;
    LinearLayout gallery;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.takephoto);

        mAppExcutor = new AppExecutor();

        mImageView = findViewById(R.id.imageView);
        mClear = findViewById(R.id.clear);
        mSave = findViewById(R.id.Save);
       // mShare = findViewById(R.id.Share);
        mStartCamera = findViewById(R.id.startCamera);
        gallery = (LinearLayout)findViewById(R.id.gallary);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.item,gallery,false);



        mImageView.setVisibility(View.GONE);
//        mShare.setVisibility(View.GONE);
        mSave.setVisibility(View.GONE);
        mClear.setVisibility(View.GONE);
        gallery.setVisibility(View.GONE);


        ImageView red = view.findViewById(R.id.colorview);

        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int red = Color.parseColor("#A6FF0000");
                mImageView.setColorFilter(red);

            }
        });


        TextView textView2 = view.findViewById(R.id.itemText2);
        ImageView green = view.findViewById(R.id.colorview2);
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int green = Color.parseColor("#A600FF00");
                mImageView.setColorFilter(green);
            }

        });

        TextView textView3 = view.findViewById(R.id.itemText3);

        ImageView blue = view.findViewById(R.id.colorview3);
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int blue = Color.parseColor("#A6ADD8E6");
                mImageView.setColorFilter(blue);
            }
        });

        ImageView purple = view.findViewById(R.id.colorview4);
        purple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int purple = Color.parseColor("#A66a0dad");
                mImageView.setColorFilter(purple);
            }
        });
        ImageView grey = view.findViewById(R.id.colorview5);
        grey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int grey = Color.parseColor("#A6808080");
                mImageView.setColorFilter(grey);
            }
        });

        gallery.addView(view);


        mStartCamera.setOnClickListener(v -> {
            // Check for the external storage permission
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // If you do not have permission, request it
                ActivityCompat.requestPermissions(VisualizeActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_STORAGE_PERMISSION);
            } else {
                // Launch the camera if the permission exists
                launchCamera();
            }
        });

        mSave.setOnClickListener((View v) -> {
            mAppExcutor.diskIO().execute(() -> {
                // Delete the temporary image file
                BitmapDrawable drawable = (BitmapDrawable) mImageView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                BitmapUtils.deleteImageFile(VisualizeActivity.this, mTempPhotoPath);

                // Save the image
                BitmapUtils.saveImage(VisualizeActivity.this,bitmap );

            });

            Toast.makeText(VisualizeActivity.this,"Image Save",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(VisualizeActivity.this,ImageVisualize.class);
            startActivity(intent);

        });

        mClear.setOnClickListener(v -> {
            // Clear the image and toggle the view visibility
            mImageView.setImageResource(0);
            mStartCamera.setVisibility(View.VISIBLE);
            mSave.setVisibility(View.GONE);
          //  mShare.setVisibility(View.GONE);

            mClear.setVisibility(View.GONE);
            gallery.setVisibility(View.GONE);

            mAppExcutor.diskIO().execute(() -> {
                // Delete the temporary image file
                BitmapUtils.deleteImageFile(VisualizeActivity.this, mTempPhotoPath);
            });

        });

        /*mShare.setOnClickListener((View v) -> {

            mAppExcutor.diskIO().execute(() ->{
                // Delete the temporary image file
                BitmapUtils.deleteImageFile(VisualizeActivity.this, mTempPhotoPath);

                // Save the image
                BitmapUtils.saveImage(VisualizeActivity.this, mResultsBitmap);

            });

            // Share the image
            BitmapUtils.shareImage(VisualizeActivity.this, mTempPhotoPath);

        });
*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // Called when you request permission to read and write to external storage
        switch (requestCode) {
            case REQUEST_STORAGE_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // If you get permission, launch the camera
                    launchCamera();
                } else {
                    // If you do not get permission, show a Toast
                    Toast.makeText(VisualizeActivity.this, "Permisssion denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If the image capture activity was called and was successful
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Process the image and set it to the TextView
            processAndSetImage();
        } else {

            // Otherwise, delete the temporary image file
            BitmapUtils.deleteImageFile(VisualizeActivity.this, mTempPhotoPath);
        }
    }

    /**
     * Creates a temporary image file and captures a picture to store in it.
     */
    private void launchCamera() {

        // Create the capture image intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the temporary File where the photo should go
            File photoFile = null;
            try {
                photoFile = BitmapUtils.createTempImageFile(this);
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                // Get the path of the temporary file
                mTempPhotoPath = photoFile.getAbsolutePath();

                // Get the content URI for the image file
                Uri photoURI = FileProvider.getUriForFile(VisualizeActivity.this,
                        FILE_PROVIDER_AUTHORITY,
                        photoFile);

                // Add the URI so the camera can store the image
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                // Launch the camera activity
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }


    /**
     * Method for processing the captured image and setting it to the TextView.
     */
    private void processAndSetImage() {

        // Toggle Visibility of the views
        mStartCamera.setVisibility(View.GONE);
        mSave.setVisibility(View.VISIBLE);
//        mShare.setVisibility(View.VISIBLE);
        mClear.setVisibility(View.VISIBLE);
        mImageView.setVisibility(View.VISIBLE);
        gallery.setVisibility(View.VISIBLE);

        // Resample the saved image to fit the ImageView
        mResultsBitmap = BitmapUtils.resamplePic(VisualizeActivity.this, mTempPhotoPath);


        // Set the new bitmap to the ImageView
        mImageView.setImageBitmap(mResultsBitmap);
    }




}
