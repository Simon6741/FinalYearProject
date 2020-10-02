package my.edu.tarc.finalyearproject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import my.edu.tarc.finalyearproject.Prevalent.OrderID;

public class ReceivedFragment extends Fragment {

    private DatabaseReference mDatabaseRef;
    private ArrayList<Order> orderList;
    private RecyclerView mRecyclerView;
    private receivedAdapter mAdapter;
    private ProgressDialog progressDialog;
    private FirebaseStorage mStorage;
    private ValueEventListener mDBListener;
    private TextView myOrder;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_order,container,false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myOrder = (TextView) getView().findViewById(R.id.myOrder);
        mAuth = FirebaseAuth.getInstance();
        TextView myReceived = (TextView) getView().findViewById(R.id.myReceived);
        createImageList();
        buildRecyclerView();
        myReceived.setTextColor(getResources().getColor(R.color.red));

        myOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new OrderFragment());
            }
        });

    }

    public void createImageList(){

        orderList = new ArrayList<>();
        mRecyclerView = getView().findViewById(R.id.order_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new receivedAdapter(getContext(),orderList);
        mRecyclerView.setAdapter(mAdapter);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait loading list image...");
        progressDialog.show();

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Delivered").child(mAuth.getUid());

    }


    public void buildRecyclerView(){
        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                progressDialog.dismiss();
                orderList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Order o = snapshot.getValue(Order.class);
                    o.setKey(snapshot.getKey());
                    orderList.add(o);

                }
                mAdapter.notifyDataSetChanged();
                mAdapter.setOnItemClickListener(new receivedAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {

                        Order selectedItem = orderList.get(position);
                        final String selectedId = selectedItem.getKey();
                        OrderID.oid = selectedId;
                        loadFragment(new OrderHistoryFragment());
                        /*mDatabaseRef.child(selectedId).child("state").setValue("GG");*/
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
    public void onDestroy(){
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
}

