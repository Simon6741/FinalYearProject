package my.edu.tarc.finalyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import my.edu.tarc.finalyearproject.Prevalent.Prevalent;
import my.edu.tarc.finalyearproject.Prevalent.Sum;

public class ConfirmOrderFragment extends Fragment {
    private EditText nameEditText, phoneEditText, addressEditText, cityEditText;
    private Button confirmButton, ewalletButton;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_confirm_order,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toast.makeText(getContext(), String.format("Total Price = RM%.2f", Sum.tp), Toast.LENGTH_SHORT).show();

        confirmButton = (Button)getView().findViewById(R.id.btnConfirmOrder);
        ewalletButton = (Button)getView().findViewById(R.id.btnEwalletPayment);
        nameEditText = (EditText)getView().findViewById(R.id.shipment_name);
        phoneEditText = (EditText)getView().findViewById(R.id.shipment_phone);
        addressEditText = (EditText)getView().findViewById(R.id.shipment_address);

        nameEditText.setText(Prevalent.currentOnlineUser.getName());
        phoneEditText.setText(Prevalent.currentOnlineUser.getPhone());
        addressEditText.setText(Prevalent.currentOnlineUser.getAddress());
        nameEditText.setEnabled(false);
        phoneEditText.setEnabled(false);
        addressEditText.setEnabled(false);
        mAuth = FirebaseAuth.getInstance();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Check();
            }
        });

        ewalletButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), EwalletPayment.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
    }
    private void Check(){
        if (TextUtils.isEmpty(nameEditText.getText().toString())){
            nameEditText.setError("Please provide your full name.");
            nameEditText.requestFocus();
        }else if (TextUtils.isEmpty(phoneEditText.getText().toString())){
            phoneEditText.setError("Please provide your phone number.");
            phoneEditText.requestFocus();
        }else if (TextUtils.isEmpty(addressEditText.getText().toString())){
            addressEditText.setError("Please provide your Delivery Address.");
            addressEditText.requestFocus();
        }else {
            /*ConfirmOrder();*/
            Intent i = new Intent(getContext(), CardPaymentActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }
}
