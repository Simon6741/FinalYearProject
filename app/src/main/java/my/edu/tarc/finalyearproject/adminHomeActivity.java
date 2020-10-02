package my.edu.tarc.finalyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class adminHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
    }

    public void btnUpload_Click(View v){
        Intent i = new Intent(this, UploadActivity.class);
        startActivity(i);
    }


    public void btnDeliverSignUp(View v){
        Intent i = new Intent(this, deliverySignUp.class);
        startActivity(i);
    }
    public void btnStockControl(View v){
        Intent i = new Intent(this, stockControlActivity.class);
        startActivity(i);
    }

    public void btnShelf(View v){
        Intent i = new Intent(this, shelfActivity.class);
        startActivity(i);
    }
    public void btnLogout(View v){
        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

}
