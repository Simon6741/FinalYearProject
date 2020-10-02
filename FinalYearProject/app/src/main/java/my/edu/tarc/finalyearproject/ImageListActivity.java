package my.edu.tarc.finalyearproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ImageListActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseRef;
    private DatabaseReference mStockRef;
    private ArrayList<ImageUpload> imgList;
    private RecyclerView mRecyclerView;
    private ImageListAdapter mAdapter;
    private ProgressDialog progressDialog;
    private FirebaseStorage mStorage;
    private ValueEventListener mDBListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);

        createImageList();
        buildRecyclerView();

    }

    public void createImageList(){

        imgList = new ArrayList<>();
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ImageListAdapter(ImageListActivity.this,imgList);
        mRecyclerView.setAdapter(mAdapter);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait loading list image...");
        progressDialog.show();

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(UploadActivity.FB_DATABASE_PATH);
    }


    public void buildRecyclerView(){
        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                progressDialog.dismiss();
                imgList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ImageUpload img = snapshot.getValue(ImageUpload.class);
                    img.setKey(snapshot.getKey());
                    imgList.add(img);

                }
                mAdapter.notifyDataSetChanged();
                mAdapter.setOnItemClickListener(new ImageListAdapter.OnItemClickListener() {
                    @Override
                    public void onEditClick(int position) {

                        ImageUpload selectedItem = imgList.get(position);
                        showUpdateDialog(selectedItem.getKey(), selectedItem.getName(),selectedItem.getUrl(),selectedItem.getDesc(),selectedItem.getPrice(),selectedItem.getCategory(), selectedItem.getAvailableStock());


                    }

                    @Override
                    public void onDeleteClick(int position) {

                        ImageUpload selectedItem = imgList.get(position);
                        final String selectedKey = selectedItem.getKey();

                        StorageReference imageref = mStorage.getReferenceFromUrl(selectedItem.getUrl());
                        imageref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                stockControl(selectedItem.getKey(),selectedItem.getName(),selectedItem.getAvailableStock());
                                mDatabaseRef.child(selectedKey).removeValue();
                            }
                        });
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                progressDialog.dismiss();
            }
        });

    }

    private void showUpdateDialog(final String pid, final String pName, final String url, final String pDesc, final double p, final String cat, final int stock){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ImageListActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);

        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText)dialogView.findViewById(R.id.txtPName);
        final EditText editTextDesc = (EditText)dialogView.findViewById(R.id.txtPDesc);
        final Spinner spinnerGenres = (Spinner)dialogView.findViewById(R.id.categorySpinnerP);
        final EditText editTextPrice = (EditText)dialogView.findViewById(R.id.txtPPrice);
        final EditText editTextStock = (EditText)dialogView.findViewById(R.id.txtPStock);
        final Button btnUpdate = (Button) dialogView.findViewById(R.id.btnUpate);
        String pr = String.valueOf(p);
        String ps = String.valueOf(stock);

        editTextName.setText(pName);
        editTextDesc.setText(pDesc);
        spinnerGenres.setTag(cat);
        editTextPrice.setText(pr);
        editTextStock.setText(ps);

        dialogBuilder.setTitle("Updating Product Details of " + pName);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String name = editTextName.getText().toString().trim();
                String description = editTextDesc.getText().toString().trim();
                String category = spinnerGenres.getSelectedItem().toString();
                String price = editTextPrice.getText().toString().trim();
                String stock = editTextStock.getText().toString().trim();
                double Price = Double.parseDouble(price);
                int Stock = Integer.parseInt(stock);

                if (TextUtils.isEmpty(name)) {
                    editTextName.setError("Name Required");
                    return;
                }else if (TextUtils.isEmpty(description)) {
                    editTextDesc.setError("Description Required");
                    return;
                }else if (TextUtils.isEmpty(price)) {
                    editTextPrice.setError("Price Required");
                    return;
                }else if (TextUtils.isEmpty(stock)) {
                    editTextPrice.setError("Price Required");
                    return;
                }

                updateArtist(pid, name, url, description, Price, category, Stock);
                Intent i = new Intent(ImageListActivity.this, ImageListActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });



    }

    public boolean updateArtist(String id, String pName, String url, String pDesc, double price, String pCat, int pStock){
        DatabaseReference updateDatabaseRef = FirebaseDatabase.getInstance().getReference("image").child(id);

        ImageUpload product = new ImageUpload(id,pName,url, pDesc, price, pCat, pStock);

        updateDatabaseRef.setValue(product);

        updateControl(id,pName,pStock);

        return true;
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

    private void stockControl(String productID, String productName, int stockAvailable){
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
        stockMap.put("description", " had been remove from Database");
        stockMap.put("date", saveCurrentDate);
        stockMap.put("time", saveCurrentTime);
        stockMap.put("availableStock", stockAvailable);

        mStockRef.child(historyID).updateChildren(stockMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){

                    Toast.makeText(ImageListActivity.this, productName + " had been remove from Database", Toast.LENGTH_SHORT).show();


                }
            }
        });
    }

    private void updateControl(String productID, String productName, int stockAvailable){
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
        stockMap.put("description", " details have been updated");
        stockMap.put("date", saveCurrentDate);
        stockMap.put("time", saveCurrentTime);
        stockMap.put("availableStock", stockAvailable);

        mStockRef.child(historyID).updateChildren(stockMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){

                    Toast.makeText(ImageListActivity.this, productName + " details have been updated", Toast.LENGTH_SHORT).show();


                }
            }
        });
    }
}
