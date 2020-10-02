package my.edu.tarc.finalyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import my.edu.tarc.finalyearproject.Prevalent.Prevalent;
import my.edu.tarc.finalyearproject.Prevalent.SelectedB;

public class shelfActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelf);
    }

    public void btnShowListImage_Click(View v){
        Intent i = new Intent(this, ImageListActivity.class);
        startActivity(i);
    }

    public void btnA(View v) {
        SelectedB.b = "A";
        Intent i = new Intent(this, ShelfProductListActivity.class);
        startActivity(i);
    }

    public void btnB(View v) {
        SelectedB.b = "B";
        Intent i = new Intent(this, ShelfProductListActivity.class);
        startActivity(i);
    }

    public void btnC(View v) {
        SelectedB.b = "C";
        Intent i = new Intent(this, ShelfProductListActivity.class);
        startActivity(i);
    }

    public void btnD(View v) {
        SelectedB.b = "D";
        Intent i = new Intent(this, ShelfProductListActivity.class);
        startActivity(i);
    }

    public void btnE(View v) {
        SelectedB.b = "E";
        Intent i = new Intent(this, ShelfProductListActivity.class);
        startActivity(i);
    }

    public void btnF(View v) {
        SelectedB.b = "F";
        Intent i = new Intent(this, ShelfProductListActivity.class);
        startActivity(i);
    }

}
