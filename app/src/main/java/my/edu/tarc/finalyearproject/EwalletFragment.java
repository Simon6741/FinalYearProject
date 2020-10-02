package my.edu.tarc.finalyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import my.edu.tarc.finalyearproject.Domain.Transaction;

public class EwalletFragment extends Fragment {

    ActivityAdapter activityAdapter;
    RecyclerView recyclerView;
    DatabaseReference activityRoot;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference roots;
    private Button btn_topUp,btn_Epay;
    private TextView txtAmount;
    private List<Transaction> topUpList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container,  Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_ewallet, null);
        recyclerView = root.findViewById(R.id.transactionview);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        String uid = firebaseUser.getUid();
        roots = FirebaseDatabase.getInstance().getReference().getRoot().child("Ewallet/" + uid);
        activityRoot = roots.child("Activity");
        btn_topUp = root.findViewById(R.id.btnTopUp);
        btn_Epay = root.findViewById(R.id.button);
        txtAmount = root.findViewById(R.id.Account_Credit);

        try {
            roots.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("Amount")) {
                        Double amount = dataSnapshot.child("Amount").getValue(Double.class);
                        txtAmount.setText(String.format("%.2f", amount));
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception ex) {

        }
        btn_topUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreditCardForm.class);
               intent.putExtra("Amount", Double.parseDouble(txtAmount.getText().toString()));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        btn_Epay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EwalletPayment.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        activityRoot.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                appendTransaction(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        recyclerView.setLayoutManager(layoutManager);
        return root;

    }

    private void appendTransaction(DataSnapshot dataSnapshot) {
        Iterator i = dataSnapshot.getChildren().iterator();
        while (i.hasNext()) {
            Transaction topupActivity = new Transaction();
            topupActivity.setDatetime((String) ((DataSnapshot) i.next()).getValue());
            double transactionAmt = Double.valueOf(String.valueOf(((DataSnapshot) i.next()).getValue()));
            topupActivity.setTransactionAmt(transactionAmt);
            topupActivity.setTransactionDesc((String) ((DataSnapshot) i.next()).getValue());
            topupActivity.setTransactionID((String) ((DataSnapshot) i.next()).getValue());
            topupActivity.setTransactionType((String) ((DataSnapshot) i.next()).getValue());
            topUpList.add(topupActivity);
            Collections.sort(topUpList, new Comparator<Transaction>() {
                public int compare(Transaction o1, Transaction o2) {
                    return o2.getDatetime().compareTo(o1.getDatetime());
                }
            });
            activityAdapter = new ActivityAdapter(getContext(), topUpList);
            recyclerView.setAdapter(activityAdapter);
        }
    }
}

