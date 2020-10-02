package my.edu.tarc.finalyearproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Calculate_paint extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.paintcalculate);

        EditText Height1,Height2,Height3,Height4,Width1,Width2,Width3,Width4,Lcell,Wcell;
        Button Calculate;
        TextView CalResult,RequiredPaint,RequiredPrice;

        Height1=findViewById(R.id.Height1);
        Height2 = findViewById(R.id.Height2);
        Height3 = findViewById(R.id.Height3);
        Height4 = findViewById(R.id.Height4);
        Width1 = findViewById(R.id.Width1);
        Width2 = findViewById(R.id.Width2);
        Width3 = findViewById(R.id.Width3);
        Width4 = findViewById(R.id.Width4);
        Calculate= findViewById(R.id.Recommed);
        CalResult = findViewById(R.id.result);
        RequiredPaint = findViewById(R.id.requiredpaint);
        RequiredPrice = findViewById(R.id.paintPrice);
        Lcell = findViewById(R.id.cellLength);
        Wcell = findViewById(R.id.cellWidth);



        Calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double height1 =Double.parseDouble(Height1.getText().toString());
                double width1 =Double.parseDouble(Width1.getText().toString());
                double height2 = Double.parseDouble(Height2.getText().toString());
                double width2 =Double.parseDouble(Width2.getText().toString());
                double height3 =Double.parseDouble(Height3.getText().toString());
                double width3 =Double.parseDouble(Width3.getText().toString());
                double height4 = Double.parseDouble(Height4.getText().toString());
                double width4 =Double.parseDouble(Width4.getText().toString());
                double lcell =Double.parseDouble(Lcell.getText().toString());
                double wcell =Double.parseDouble(Wcell.getText().toString());


                double Result1 = height1*width1;
                double Result2 = height2*width2;
                double Result3 = height3*width3;
                double Result4 = height4*width4;
                double Result5 = lcell*wcell;

                double FinalResult=Result1+Result2+Result3+Result4+Result5;

                double paint = FinalResult/1000;

                double price = paint*20;
                String fResult = String.format("%.2f", FinalResult);
                String fpaint = String.format("%.2f", paint);
                String fprice = String.format("%.2f", price);



                CalResult.setText("Total surface area = "+ fResult +"cm2");
                RequiredPaint.setText("The required paint is "+fpaint+"ML");
                RequiredPrice.setText("The required price is RM"+fprice);


            }
        });



    }
}
