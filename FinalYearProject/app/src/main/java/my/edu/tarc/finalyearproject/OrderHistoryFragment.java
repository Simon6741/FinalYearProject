package my.edu.tarc.finalyearproject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import my.edu.tarc.finalyearproject.Prevalent.OrderID;

public class OrderHistoryFragment extends Fragment {
    private DatabaseReference mDatabaseRef;
    private ArrayList<Cart> cartList;
    private RecyclerView mRecyclerView;
    private OrderDetailsAdapter mAdapter;
    private ProgressDialog progressDialog;
    private FirebaseStorage mStorage;
    private ValueEventListener mDBListener;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_cart,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fab = (FloatingActionButton)getView().findViewById(R.id.fabSum);
        Button nextButton = (Button)getView().findViewById(R.id.nextProcessBtn);
        TextView title = (TextView)getView().findViewById(R.id.cartTotalPrice);
        createImageList();
        buildRecyclerView();
        fab.setVisibility(View.INVISIBLE);
        nextButton.setVisibility(View.INVISIBLE);
        title.setText("Order History");
    }

    public void createImageList(){

        cartList = new ArrayList<>();
        mRecyclerView = getView().findViewById(R.id.cart_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new OrderDetailsAdapter(getContext(),cartList);
        mRecyclerView.setAdapter(mAdapter);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait loading list image...");
        progressDialog.show();

        mStorage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Delivered").child(mAuth.getUid()).child(OrderID.oid).child("Product Details");

    }


    public void buildRecyclerView(){
        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                progressDialog.dismiss();
                cartList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Cart c = snapshot.getValue(Cart.class);
                    c.setKey(snapshot.getKey());
                    cartList.add(c);

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
