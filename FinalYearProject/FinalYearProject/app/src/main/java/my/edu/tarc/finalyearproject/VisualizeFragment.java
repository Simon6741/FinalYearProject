package my.edu.tarc.finalyearproject;

import android.Manifest;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.transform.Result;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static android.os.Environment.getExternalStoragePublicDirectory;

public class VisualizeFragment extends Fragment {

    ImageView camera, video, image;
    String pathFile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_visualize, container, false);


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        camera = (ImageView) getView().findViewById(R.id.CameraVisualize);
        if(Build.VERSION.SDK_INT>=23){
            requestPermissions(new String[]{Manifest.permission.CAMERA, WRITE_EXTERNAL_STORAGE},2);
        }
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchPictureTakerAction();
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
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

}

