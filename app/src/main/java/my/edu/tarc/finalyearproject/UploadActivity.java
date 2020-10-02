package my.edu.tarc.finalyearproject;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import my.edu.tarc.finalyearproject.Prevalent.Prevalent;
import my.edu.tarc.finalyearproject.Prevalent.Sum;

public class UploadActivity extends AppCompatActivity {

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mStockRef;
    private ImageView imageView;
    private EditText txtImageName;
    private EditText txtDescription;
    private EditText txtPrice;
    private EditText txtStock;
    private Spinner categorySpinner, shelfSpinner;
    private Uri imgUri;

    public static final String FB_STORAGE_PATH = "image/";
    public static final String FB_DATABASE_PATH = "image";
    public static final int REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);

        imageView = (ImageView) findViewById(R.id.imageView);
        txtImageName = findViewById(R.id.txtImageName);
        txtDescription = findViewById(R.id.txtDescription);
        txtPrice = findViewById(R.id.txtPrice);
        txtStock = findViewById(R.id.txtStock);
        categorySpinner = findViewById(R.id.categorySpinner);
        shelfSpinner = findViewById(R.id.shelfSpinner);
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
                    Double price = Double.parseDouble(txtPrice.getText().toString().trim());
                    int stock = Integer.parseInt(txtStock.getText().toString().trim());

                    String uploadId = mDatabaseRef.push().getKey();
                    ImageUpload imageUpload = new ImageUpload( uploadId, txtImageName.getText().toString(), sdownload_url, txtDescription.getText().toString(), price,categorySpinner.getSelectedItem().toString(),stock,shelfSpinner.getSelectedItem().toString());
                    mDatabaseRef.child(uploadId).setValue(imageUpload);
                    FirebaseDatabase.getInstance().getReference("Shelf").child(shelfSpinner.getSelectedItem().toString()).child(uploadId).setValue(imageUpload);
                    stockControl(uploadId,txtImageName.getText().toString(),stock, shelfSpinner.getSelectedItem().toString());
                    Intent i = new Intent(UploadActivity.this, adminHomeActivity.class);
                    startActivity(i);
                    finish();
                    /*Toast.makeText(getApplicationContext(), "Image uploaded", Toast.LENGTH_LONG).show();
                    ImageUpload imageUpload = new ImageUpload(txtImageName.getText().toString(), taskSnapshot.getStorage().getDownloadUrl().toString());
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
        Intent i = new Intent(this, adminHomeActivity.class);
        startActivity(i);
    }

    public void btnLogout(View v){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    private void stockControl(String productID, String productName, int stockAvailable, String productShelf){
        mStockRef = FirebaseDatabase.getInstance().getReference().child("Stock Control");

        String saveCurrentTime, saveCurrentDate;
        TimeZone tz = TimeZone.getTimeZone("GMT+8:00");
        Calendar calForDate = Calendar.getInstance(tz);
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        saveCurrentTime = String.format("%02d" , calForDate.get(Calendar.HOUR_OF_DAY))+":"+
                String.format("%02d" , calForDate.get(Calendar.MINUTE))+":"+
                String.format("%02d" , calForDate.get(Calendar.SECOND));


        String historyID = mStockRef.push().getKey();
        final HashMap<String, Object> stockMap = new HashMap<>();

        stockMap.put("ID",historyID);
        stockMap.put("pid", productID);
        stockMap.put("name", productName );
        stockMap.put("description", " had been added to Database");
        stockMap.put("date", saveCurrentDate);
        stockMap.put("time", saveCurrentTime);
        stockMap.put("availableStock", stockAvailable);

        mStockRef.child(historyID).updateChildren(stockMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){

                    Toast.makeText(UploadActivity.this, productName + " had been added to Shelf " + productShelf, Toast.LENGTH_SHORT).show();


                }
            }
        });
    }



}
