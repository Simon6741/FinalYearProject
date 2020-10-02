package my.edu.tarc.finalyearproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import my.edu.tarc.finalyearproject.Prevalent.ProductID;

public class StoreFragment extends Fragment {

    private DatabaseReference mDatabaseRef;
    private ArrayList<ImageUpload> imgList;
    private RecyclerView mRecyclerView;
    private productListAdapter mAdapter;
    private ProgressDialog progressDialog;
    private ValueEventListener mDBListener;
    private String searchInput;
    private FirebaseAuth mAuth;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_store, container, false);


    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        createProductList();
        buildRecyclerView();

        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAuth.getCurrentUser().getUid()!=null){

                    loadFragment(new CartFragment());
                }else {
                    loadFragment(new AccountFragment());
                    Toast.makeText(getContext(),"Login First", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    public void createProductList() {

        imgList = new ArrayList<>();
        mRecyclerView = getView().findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new productListAdapter(getContext(), imgList);
        mRecyclerView.setAdapter(mAdapter);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait loading list image...");
        progressDialog.show();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference(UploadActivity.FB_DATABASE_PATH);
    }


    public void buildRecyclerView() {
        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                progressDialog.dismiss();
                imgList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ImageUpload img = snapshot.getValue(ImageUpload.class);
                    img.setKey(snapshot.getKey());
                    imgList.add(img);

                }
                mAdapter.notifyDataSetChanged();
                mAdapter.setOnItemClickListener(new productListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {

                        ImageUpload selectedItem = imgList.get(position);
                        final String selectedId = selectedItem.getId();
                        ProductID.pid = selectedId;
                        loadFragment(new ProductDetailFragment());
                            /*Intent intent = new Intent(getActivity(), DeliverMapActivity.class);
                            startActivity(intent);*/
                    }

                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                progressDialog.dismiss();
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onStart(){
        super.onStart();


    }
}





