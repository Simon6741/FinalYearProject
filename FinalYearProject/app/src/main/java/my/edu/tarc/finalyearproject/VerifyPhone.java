/*
package my.edu.tarc.finalyearproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;


public class VerifyPhone extends AppCompatActivity {

    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        LinearLayout verifyPhone,VerifyCode;
        ProgressBar progressBar;
        EditText phoneNo,code;
        Button SendCode,Verify;
        CountryCodePicker CountryCode;

        verifyPhone = (LinearLayout)findViewById(R.id.layoutPhone);
        VerifyCode = (LinearLayout) findViewById(R.id.layoutVerification);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        phoneNo = (EditText) findViewById(R.id.edit_text_phone);
        SendCode = (Button) findViewById(R.id.button_send_verification);
        Verify = (Button) findViewById(R.id.button_verify);
        code = (EditText)findViewById(R.id.edit_text_code);
        CountryCode = (CountryCodePicker)findViewById(R.id.ccp);

        verifyPhone.setVisibility(View.VISIBLE);
        VerifyCode.setVisibility(View.GONE);


        SendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = phoneNo.getText().toString().trim();

                if(phone.isEmpty()){
                    phoneNo.setError("Cannot be empty");
                    phoneNo.requestFocus();
                }

                String phonenumber =CountryCode.getSelectedCountryCodeWithPlus()+phone;

                //PhoneAuthProvider.getInstance().verifyPhoneNumber(phonenumber,60, TimeUnit.SECONDS,requiredActivity(),phone);

                verifyPhone.setVisibility(View.GONE);
                VerifyCode.setVisibility(View.VISIBLE);



            }
        });

            Verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String Code = code.getText().toString().trim();

                    if(Code.isEmpty()){
                        code.setError("Code required");
                        code.requestFocus();
                    }
                    Strg

                    VerifyPhoneFragment.this.verificationId;
                }
            });
    }

    private final <undefinedtype> phoneAuthCallbacks = new OnVerificationStateChangedCallbacks() {
        public void onVerificationCompleted(@Nullable PhoneAuthCredential phoneAuthCredential) {
            if (phoneAuthCredential != null) {
                boolean var3 = false;
                boolean var4 = false;
                int var6 = false;
                VerifyPhoneFragment.this.addPhoneNumber(phoneAuthCredential);
            }

        }
}
*/
