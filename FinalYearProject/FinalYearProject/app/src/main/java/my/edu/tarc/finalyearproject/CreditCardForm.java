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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import my.edu.tarc.finalyearproject.Domain.CentralizedFunctions;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import my.edu.tarc.finalyearproject.Domain.Transaction;

public class CreditCardForm extends AppCompatActivity {

    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    CentralizedFunctions centralizedFunction = new CentralizedFunctions();
    private DatabaseReference root, activityRoot;
    private EditText txtAmt;
    private Double amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_form);
        CardForm cardForm = findViewById(R.id.card_form);
        Button buy = findViewById(R.id.btnBuy);
        txtAmt = findViewById(R.id.txtAmt);
        root = FirebaseDatabase.getInstance().getReference().getRoot().child("Ewallet/" + uid).child("Amount");
        activityRoot = FirebaseDatabase.getInstance().getReference().getRoot().child("Ewallet/" + uid).child("Activity");
        amount = getIntent().getDoubleExtra("Amount", 0.0);

        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("SMS is required on this number")
                .setup(CreditCardForm.this);
        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cardForm.isValid()) {
                    Double amt = Double.parseDouble(txtAmt.getText().toString());
                    amount += amt;
                    String tempkey = activityRoot.push().getKey();
                    Transaction transaction = new Transaction();
                    transaction.setTransactionAmt(Double.parseDouble(txtAmt.getText().toString()));
                    transaction.setTransactionDesc("Top Up From" + " XXXX-XXXX-XXXX-" + cardForm.getCardNumber().substring(12, 16));
                    transaction.setTransactionID(tempkey);
                    transaction.setTransactionType("Credit");
                    transaction.setDatetime(centralizedFunction.getTodayDateTimeStr());
                    root.setValue(amount).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            activityRoot.child(tempkey).setValue(transaction).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(), "Top up Successfully!" + String.format("%.2f", amt) + " added to your account!", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            });

                        }
                    });


                } else {
                    Toast.makeText(CreditCardForm.this, "Please complete the form", Toast.LENGTH_LONG).show();
                }

            }

        });

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(CreditCardForm.this);
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
        alertDialog.show();
    }
}
