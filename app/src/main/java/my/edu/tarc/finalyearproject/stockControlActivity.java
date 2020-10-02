package my.edu.tarc.finalyearproject;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class stockControlActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseRef;
    private ArrayList<StockControl> controlList;
    private RecyclerView mRecyclerView;
    private stockControlAdapter mAdapter;
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

        controlList = new ArrayList<>();
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new stockControlAdapter(stockControlActivity.this,controlList);
        mRecyclerView.setAdapter(mAdapter);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait loading list image...");
        progressDialog.show();

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Stock Control");
    }


    public void buildRecyclerView(){
        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                progressDialog.dismiss();
                controlList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    StockControl img = snapshot.getValue(StockControl.class);
                    img.setKey(snapshot.getKey());
                    controlList.add(img);

                }
                mAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                progressDialog.dismiss();
            }
        });

    }

}
