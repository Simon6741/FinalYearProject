package my.edu.tarc.finalyearproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import my.edu.tarc.finalyearproject.Utils.BitmapUtils;
import my.edu.tarc.finalyearproject.Utils.BitmapUtils2;

import static android.app.Activity.RESULT_OK;
import static android.os.Environment.getExternalStoragePublicDirectory;
import static android.os.Environment.getRootDirectory;

public class VisualizeFragment extends Fragment {

    ImageView camera, video, image;
    String pathFile;
    private Bitmap mResultsBitmap;

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private static final int REQUEST_STORAGE_PERMISSION = 1;
    private static final String FILE_PROVIDER_AUTHORITY = "com.example.android.fileprovider";
    private String mTempPhotoPath;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_visualize, container, false);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        camera = (ImageView) getView().findViewById(R.id.CameraVisualize);


        if(Build.VERSION.SDK_INT >=22) {
            requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
        }
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =  new Intent(getActivity(),VisualizeActivity.class);
                startActivity(intent);
                //dispatchPictureTakerAction();
                // Check for the external storage permission
/*                if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // If you do not have permission, request it
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_STORAGE_PERMISSION);
                } else {
                    // Launch the camera if the permission exists
                    launchCamera();
                }*/
            }
        });

        image = (ImageView) getView().findViewById(R.id.ImageVisualize);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ImageVisualize.class);
                startActivity(intent);
            }
        });
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
                    Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

  /*  @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If the image capture activity was called and was successful
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Process the image and set it to the TextView
            processAndSetImage();
        } else {
            // Otherwise, delete the temporary image file
            BitmapUtils.deleteImageFile(getActivity(), mTempPhotoPath);
        }
    }*/



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE){
            if(requestCode == 1){
                Bitmap bitmap = BitmapFactory.decodeFile(pathFile);
            }
        }
    }

    private void dispatchPictureTakerAction() {
        Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePic.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            photoFile = createPhotoFile();
            if(photoFile!=null) {
                pathFile = photoFile.getAbsolutePath();
                 Uri phtoUrl = FileProvider.getUriForFile(getActivity(),"com.thecodecity.cameraandroid.fileprovider",photoFile);
                takePic.putExtra(MediaStore.EXTRA_OUTPUT,phtoUrl);
                startActivityForResult(takePic,1);
            }

        }
    }
    private File createPhotoFile(){
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDiz = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) ;
        File image = null;
        try {
             image  = File.createTempFile(name,".jpg",storageDiz);
        } catch (IOException e) {
            Log.d("my Log","Excep:"+e.toString());
        }
        return  image;
    }

    private void launchCamera() {

        // Create the capture image intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the temporary File where the photo should go
            File photoFile = null;
            try {
                photoFile = BitmapUtils.createTempImageFile(getActivity());
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                // Get the path of the temporary file
                mTempPhotoPath = photoFile.getAbsolutePath();

                // Get the content URI for the image file
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        FILE_PROVIDER_AUTHORITY,
                        photoFile);

                // Add the URI so the camera can store the image
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                // Launch the camera activity
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void processAndSetImage() {

        // Resample the saved image to fit the ImageView
        mResultsBitmap = BitmapUtils.resamplePic(getActivity(), mTempPhotoPath);


        // Set the new bitmap to the ImageView
        //mImageView.setImageBitmap(mResultsBitmap);
    }



}

