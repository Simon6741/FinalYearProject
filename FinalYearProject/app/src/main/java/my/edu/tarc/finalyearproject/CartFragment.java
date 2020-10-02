package my.edu.tarc.finalyearproject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import my.edu.tarc.finalyearproject.Prevalent.ProductID;
import my.edu.tarc.finalyearproject.Prevalent.Sum;

public class CartFragment extends Fragment {
    private DatabaseReference mDatabaseRef;
    private ArrayList<Cart> cartList;
    private RecyclerView mRecyclerView;
    private cartAdapter mAdapter;
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

        Button nextButton = (Button)getView().findViewById(R.id.nextProcessBtn);
        final FloatingActionButton fab = (FloatingActionButton)getView().findViewById(R.id.fabSum);
        Sum.tp=0;


        createImageList();
        buildRecyclerView();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView tp = (TextView)getView().findViewById(R.id.cartTotalPrice);
                tp.setText(String.format("Total Price = RM%.2f", Sum.tp));
                /*fab.setVisibility(View.INVISIBLE);*/
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Sum.tp > 0) {

                    loadFragment(new ConfirmOrderFragment());
                } else {
                    loadFragment(new StoreFragment());
                    Toast.makeText(getContext(), "Please select at least 1 Product", Toast.LENGTH_SHORT).show();
                }
            }
                /*getActivity().finish();*/

        });

    }

    public void createImageList(){

        cartList = new ArrayList<>();
        mRecyclerView = getView().findViewById(R.id.cart_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new cartAdapter(getContext(),cartList);
        mRecyclerView.setAdapter(mAdapter);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait loading list image...");
        progressDialog.show();
        mStorage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(mAuth.getUid());

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

                mAdapter.setOnItemClickListener(new cartAdapter.OnItemClickListener() {
                    @Override
                    public void onEditClick(int position) {

                        Cart selectedItem = cartList.get(position);
                        final String selectedKey = selectedItem.getPid();
                        ProductID.pid=selectedKey;
                        loadFragment(new ProductDetailFragment());
                    }

                    @Override
                    public void onDeleteClick(int position) {

                        Cart selectedItem = cartList.get(position);
                        final String selectedKey = selectedItem.getKey();

                        mDatabaseRef.child(selectedKey).removeValue();
                        Toast.makeText(getContext(), "Item deleted", Toast.LENGTH_SHORT).show();
                        Sum.tp=0;
                        loadFragment(new CartFragment());
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
