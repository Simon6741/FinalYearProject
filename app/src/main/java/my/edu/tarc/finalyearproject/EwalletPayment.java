package my.edu.tarc.finalyearproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import my.edu.tarc.finalyearproject.Domain.Transaction;
import my.edu.tarc.finalyearproject.Prevalent.Prevalent;
import my.edu.tarc.finalyearproject.Prevalent.Sum;

public class EwalletPayment extends AppCompatActivity {

    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    ActivityAdapter activityAdapter;
    DatabaseReference activityRoot;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference mStockRef;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference cartRef;
    private DatabaseReference roots;
    private Double amount;
    private String message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ewallet_payment);
        EditText txtAmt = (EditText)findViewById(R.id.amtTotalE);
        Button payment = (Button)findViewById(R.id.ewalletPayment);
        Button btn_topUp = (Button)findViewById(R.id.btnTopUp);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        cartRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View");
        roots = FirebaseDatabase.getInstance().getReference().getRoot().child("Ewallet/" + uid);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("image");
        activityRoot = roots.child("Activity");
        amount = getIntent().getDoubleExtra("Amount", 0.0);
        txtAmt.setText(String.format("RM%.2f", Sum.tp));
        txtAmt.setEnabled(false);
        final TextView txtAmount = (TextView)findViewById(R.id.Account_Credit);

        try {
            roots.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("Amount")) {
                        amount = dataSnapshot.child("Amount").getValue(Double.class);
                        txtAmount.setText(String.format("%.2f", amount));
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception ex) {

        }

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String saveCurrentTime, saveCurrentDate;
                final String recordTime, recordDate;

                TimeZone tz = TimeZone.getTimeZone("GMT+8:00");
                Calendar calForDate = Calendar.getInstance(tz);
                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
                saveCurrentDate = currentDate.format(calForDate.getTime());

                saveCurrentTime = String.format("%02d" , calForDate.get(Calendar.HOUR_OF_DAY))+":"+
                        String.format("%02d" , calForDate.get(Calendar.MINUTE))+":"+
                        String.format("%02d" , calForDate.get(Calendar.SECOND));

                if (amount>Sum.tp) {
                    if (Sum.tp>0) {
                        amount -= Sum.tp;
                        final String tempkey = activityRoot.push().getKey();
                        final Transaction transaction = new Transaction();
                        transaction.setTransactionAmt(Sum.tp);
                        transaction.setTransactionDesc("E-pay");
                        transaction.setTransactionID(tempkey);
                        transaction.setTransactionType("E-wallet");
                        transaction.setDatetime(saveCurrentDate + " " + saveCurrentTime);
                        roots.child("Amount").setValue(amount).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                roots.child("Amount").setValue(amount);
                                activityRoot.child(tempkey).setValue(transaction).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(), "Pay Successfully!" + String.format("%.2f", amount) + " remove from your account", Toast.LENGTH_LONG).show();
                                        ConfirmOrder();
                                    }
                                });

                            }
                        });
                    }else {
                        Toast.makeText(getApplicationContext(), "Please Choose at least 1 product", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Please Top Up your E-wallet", Toast.LENGTH_LONG).show();
                }
            }

        });

        btn_topUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EwalletPayment.this, CreditCardForm.class);
                intent.putExtra("Amount", Double.parseDouble(txtAmount.getText().toString()));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }


    private void ConfirmOrder() {

        final String saveCurrentTime, saveCurrentDate;
        final String recordTime, recordDate;

        TimeZone tz = TimeZone.getTimeZone("GMT+8:00");
        Calendar calForDate = Calendar.getInstance(tz);
        SimpleDateFormat currentDate = new SimpleDateFormat("ddMMyyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        saveCurrentTime = String.format("%02d" , calForDate.get(Calendar.HOUR_OF_DAY))+
                String.format("%02d" , calForDate.get(Calendar.MINUTE))+
                String.format("%02d" , calForDate.get(Calendar.SECOND));

        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
        recordDate = formatDate.format(calForDate.getTime());

        recordTime = String.format("%02d" , calForDate.get(Calendar.HOUR_OF_DAY))+":"+
                String.format("%02d" , calForDate.get(Calendar.MINUTE))+":"+
                String.format("%02d" , calForDate.get(Calendar.SECOND));

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(uid).child("P"+ saveCurrentTime + saveCurrentDate);

        HashMap<String, Object> ordersMap = new HashMap<>();

        ordersMap.put("totalAmount", Sum.tp);
        ordersMap.put("name", Prevalent.currentOnlineUser.getName());
        ordersMap.put("phone", Prevalent.currentOnlineUser.getPhone());
        ordersMap.put("address", Prevalent.currentOnlineUser.getAddress());
        ordersMap.put("date", recordDate);
        ordersMap.put("time", recordTime);
        ordersMap.put("state", "Preparing for shipped");
        ordersMap.put("email", Prevalent.currentOnlineUser.getEmail());
        ordersMap.put("Uid",uid);

        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    sendMail();
                    Sum.tp=0;
                    removeStock();
                    removeFromFirebase(cartRef, ordersRef,uid);

                    Toast.makeText(EwalletPayment.this,"your order has been placed Successfully" , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    finish();
                    startActivity(intent);


                }
            }
        });
    }

    private void removeStock() {


        cartRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Cart c = ds.getValue(Cart.class);

                    mDatabaseRef.child(c.getPid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Long product = (Long)dataSnapshot.child("availableStock").getValue();
                            stockControl(c.getPid(), c.getPname(), product, c.getQuantity(), Prevalent.currentOnlineUser.getName());
                            mDatabaseRef.child(c.getPid()).child("availableStock").setValue(product - c.getQuantity());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("The read failed: " + databaseError.getCode());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void removeFromFirebase(final DatabaseReference fromPath, final DatabaseReference toPath, final String key) {
        fromPath.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            // Now "DataSnapshot" holds the key and the value at the "fromPath".
            // Let's move it to the "toPath". This operation duplicates the
            // key/value pair at the "fromPath" to the "toPath".
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.child("Product Details")
                        .setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError == null) {
                                    /*Log.i(TAG , "onComplete: success");*/
                                    // In order to complete the move, we are going to erase
                                    // the original copy by assigning null as its value.
                                    fromPath.child(key).setValue(null);
                                }
                                else {
                                    /*Log.e(TAG, "onComplete: failure:" + databaseError.getMessage() + ": "
                                            + databaseError.getDetails());*/
                                }
                            }
                        });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                /*Log.e(TAG, "onCancelled: " + databaseError.getMessage() + ": "
                        + databaseError.getDetails());*/
            }
        });
    }

    private void stockControl(String productID, String productName, long stockAvailable, int qty, String username){
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

        long stockLeft = stockAvailable-qty;
        if(stockLeft>=0) {
            stockMap.put("ID",historyID);
            stockMap.put("pid", productID);
            stockMap.put("name", productName );
            stockMap.put("description", "(" + qty + ") had been purchase by " + username + " - E-wallet");
            stockMap.put("date", saveCurrentDate);
            stockMap.put("time", saveCurrentTime);
            stockMap.put("availableStock", stockLeft);

        }else{
            Toast.makeText(EwalletPayment.this,"Sorry, the product is Out of Stock",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(EwalletPayment.this, MainActivity.class);
            startActivity(i);
            finish();
        }


        mStockRef.child(historyID).updateChildren(stockMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){


                }
            }
        });
    }

    private void sendMail() {

        final String saveCurrentTime, saveCurrentDate;

        TimeZone tz = TimeZone.getTimeZone("GMT+8:00");
        Calendar calForDate = Calendar.getInstance(tz);
        SimpleDateFormat currentDate = new SimpleDateFormat("ddMMyyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        saveCurrentTime = String.format("%02d" , calForDate.get(Calendar.HOUR_OF_DAY))+
                String.format("%02d" , calForDate.get(Calendar.MINUTE))+
                String.format("%02d" , calForDate.get(Calendar.SECOND));

        String subject = "Payment Successful";
        String message = "";

        message=Prevalent.currentOnlineUser.getName()+"\n"+Prevalent.currentOnlineUser.getPhone()
                +"\n"+Prevalent.currentOnlineUser.getAddress()+String.format("\nRM%.2f",Sum.tp)
                + "\nFor the details of the Order please refers to P"+ saveCurrentTime + saveCurrentDate + " inside the application.\nThank you .";

        //Send Mail
        JavaMailAPI javaMailAPI = new JavaMailAPI(this,Prevalent.currentOnlineUser.getEmail(),subject,message);

        javaMailAPI.execute();

    }

}
