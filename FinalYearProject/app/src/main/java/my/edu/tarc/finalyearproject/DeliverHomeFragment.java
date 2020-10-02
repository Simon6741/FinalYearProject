package my.edu.tarc.finalyearproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import my.edu.tarc.finalyearproject.Prevalent.OrderID;
import my.edu.tarc.finalyearproject.Prevalent.ProductID;

public class DeliverHomeFragment extends Fragment {

    private DatabaseReference mDatabaseRef;
    private DatabaseReference mUserRef;
    private ArrayList<Order> deliverList;
    private RecyclerView mRecyclerView;
    private deliverAdapter mAdapter;
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
        mAdapter = new deliverAdapter(getContext(),deliverList);
        mRecyclerView.setAdapter(mAdapter);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait loading list image...");
        progressDialog.show();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Orders")/*.child("gg")*/;
        mAuth = FirebaseAuth.getInstance();



    }


    public void buildRecyclerView(){
        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                progressDialog.dismiss();
                deliverList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Order o = snapshot.getValue(Order.class);
                    o.setKey(snapshot.getKey());
                    ProductID.pid = snapshot.getKey();

                    mDatabaseRef.child(o.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                Order o = snapshot.getValue(Order.class);
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
                mAdapter.setOnItemClickListener(new deliverAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {

                        String saveCurrentTime, saveCurrentDate;
                        TimeZone tz = TimeZone.getTimeZone("GMT+8:00");
                        Calendar calForDate = Calendar.getInstance(tz);
                        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
                        saveCurrentDate = currentDate.format(calForDate.getTime());

                        saveCurrentTime = String.format("%02d" , calForDate.get(Calendar.HOUR_OF_DAY))+":"+
                                String.format("%02d" , calForDate.get(Calendar.MINUTE))+":"+
                                String.format("%02d" , calForDate.get(Calendar.SECOND));

                        Order selectedItem = deliverList.get(position);
                        final String selectedId = selectedItem.getKey();
                        final String selectedEmail = selectedItem.getUid();
                        final String selectedName = selectedItem.getName();
                        final String selectedAddress = selectedItem.getAddress();
                        OrderID.oid=selectedItem.getAddress();

                        /*loadFragment(new OrderDetailsFragment());*/
                        mDatabaseRef.child(selectedEmail).child(selectedId).child("state").setValue("Delivered");
                        mDatabaseRef.child(selectedEmail).child(selectedId).child("delivered_date").setValue(saveCurrentDate);
                        mDatabaseRef.child(selectedEmail).child(selectedId).child("delivered_time").setValue(saveCurrentTime);
                        deliverHistory(selectedId, selectedEmail,selectedName,selectedAddress,saveCurrentDate,saveCurrentTime);
                        removeFromFirebase(mDatabaseRef.child(selectedEmail),
                                FirebaseDatabase.getInstance().getReference().child("Delivered"),selectedId,selectedEmail);
                        Toast.makeText(getContext(),"Delivered Successfully" , Toast.LENGTH_SHORT).show();
                        /*mAuth.getCurrentUser().getEmail();*/
                        Intent intent = new Intent(getActivity(),DeliverMapActivity.class);
                        startActivity(intent);

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

    private void removeFromFirebase(final DatabaseReference fromPath, final DatabaseReference toPath, final String key1, final String key2) {
        fromPath.child(key1).addListenerForSingleValueEvent(new ValueEventListener() {
            // Now "DataSnapshot" holds the key and the value at the "fromPath".
            // Let's move it to the "toPath". This operation duplicates the
            // key/value pair at the "fromPath" to the "toPath".
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.child(key2).child(key1).setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError == null) {
                                    /*Log.i(TAG , "onComplete: success");*/
                                    // In order to complete the move, we are going to erase
                                    // the original copy by assigning null as its value.
                                    fromPath.child(key1).setValue(null);
                                }
                                else {
                                    Toast.makeText(getContext(),"Failure", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(),"Failure", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void deliverHistory(String ID, String email, String name, String address, String date, String time){

        final DatabaseReference deliverHistoryRef = FirebaseDatabase.getInstance().getReference().child("deliver_history");

        final HashMap<String, Object> cartMap = new HashMap<>();


        cartMap.put("pid", ID);
        cartMap.put("email", email);
        cartMap.put("name", name);
        cartMap.put("address", address);
        cartMap.put("date", date);
        cartMap.put("time", time);

        deliverHistoryRef.child(mAuth.getUid()).child(email).child(ID).updateChildren(cartMap);
    }
}

