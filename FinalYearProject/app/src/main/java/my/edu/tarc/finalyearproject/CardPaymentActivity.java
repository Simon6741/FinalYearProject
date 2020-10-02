package my.edu.tarc.finalyearproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import my.edu.tarc.finalyearproject.Prevalent.Prevalent;
import my.edu.tarc.finalyearproject.Prevalent.Sum;

public class CardPaymentActivity extends AppCompatActivity {

    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private EditText txtAmt;
    private Button btnBuy;
    private DatabaseReference mStockRef;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_form);
        final CardForm cardForm = findViewById(R.id.card_form);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("image");
        btnBuy = (Button)findViewById(R.id.btnBuy);
        Button buy = findViewById(R.id.btnBuy);
        txtAmt = findViewById(R.id.txtAmt);
        txtAmt.setText(String.format("RM%.2f", Sum.tp));
        txtAmt.setEnabled(false);
        buy.setText("Pay ");

        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("SMS is required on this number")
                .setup(CardPaymentActivity.this);
        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cardForm.isValid()) {

                    ConfirmOrder();

                } else {
                    Toast.makeText(CardPaymentActivity.this, "Please complete the form", Toast.LENGTH_LONG).show();
                }

            }

        });

/*        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(CardPaymentActivity.this);
        alertBuilder.setTitle("Confirm before purchase");
        alertBuilder.setMessage("Card number: " + cardForm.getCardNumber() + "\n" +
                "Card expiry date: " + cardForm.getExpirationDateEditText().getText().toString() + "\n" +
                "Card CVV: " + cardForm.getCvv() + "\n" +
                "Postal code: " + cardForm.getPostalCode() + "\n" +
                "Phone number: " + cardForm.getMobileNumber());
        alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();*/
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
                    removeStock();
                    removeFromFirebase(FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View"),
                            ordersRef,uid);
                    Toast.makeText(CardPaymentActivity.this,"your order has been placed Successfully" , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                }
            }
        });
    }

    private void removeStock() {
        DatabaseReference mStockRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View");

        mStockRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
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
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("image");

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
            stockMap.put("description", "(" + qty + ") had been purchase by " + username + " - Card");
            stockMap.put("date", saveCurrentDate);
            stockMap.put("time", saveCurrentTime);
            stockMap.put("availableStock", stockLeft);

        }else{
            Toast.makeText(CardPaymentActivity.this,"Sorry, the product is Out of Stock",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(CardPaymentActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }


        mStockRef.child(historyID).updateChildren(stockMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){

                    /*Toast.makeText(CardPaymentActivity.this, "Loading...", Toast.LENGTH_SHORT).show();*/


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
