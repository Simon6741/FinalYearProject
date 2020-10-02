package my.edu.tarc.finalyearproject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import my.edu.tarc.finalyearproject.Prevalent.ProductID;

public class DeliveredFragment extends Fragment {

    private DatabaseReference mDatabaseRef;
    private ArrayList<DeliverHistory> deliverList;
    private RecyclerView mRecyclerView;
    private deliverHistoryAdapter mAdapter;
    private ProgressDialog progressDialog;
    private ValueEventListener mDBListener;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_deliver_home,container,false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createImageList();
        buildRecyclerView();

    }

    public void createImageList(){

        deliverList = new ArrayList<>();
        mRecyclerView = getView().findViewById(R.id.deliver_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new deliverHistoryAdapter(getContext(),deliverList);
        mRecyclerView.setAdapter(mAdapter);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait loading list image...");
        progressDialog.show();

        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("deliver_history").child(mAuth.getUid())/*.child("gg")*/;
    }


    public void buildRecyclerView(){
        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                progressDialog.dismiss();
                deliverList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    DeliverHistory o = snapshot.getValue(DeliverHistory.class);
                    o.setKey(snapshot.getKey());
                    ProductID.pid = snapshot.getKey();

                    mDatabaseRef.child(o.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                DeliverHistory o = snapshot.getValue(DeliverHistory.class);
                                o.setKey(snapshot.getKey());


                                deliverList.add(o);

                            }
                            mAdapter.notifyDataSetChanged();


                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                            progressDialog.dismiss();
                        }
                    });
                }
                mAdapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                progressDialog.dismiss();
            }
        });


    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }


}

