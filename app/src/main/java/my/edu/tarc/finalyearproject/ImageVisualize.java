package my.edu.tarc.finalyearproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import us.technerd.tnimageview.TNImageView;

public class ImageVisualize extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    Uri imageUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visualizeimage);
        LinearLayout gallery = findViewById(R.id.gallary);
        LayoutInflater inflater = LayoutInflater.from(this);
        ImageView GallaryPic = (ImageView) findViewById(R.id.galleryPicture);
        ImageView Smallbox = (ImageView) findViewById(R.id.plainColor);


    //    convertToBitmap(Smallbox,20,20)
       BitmapDrawable drawable = (BitmapDrawable) Smallbox.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        // Initialize the TNImageView object
        TNImageView tnImage = new TNImageView();

    // pass your ImageView which you want to make rotatable and scaleable
        tnImage.makeRotatableScalable(Smallbox);


             GallaryPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galarry = new Intent();
                galarry.setType("image/*");
                galarry.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galarry,"Select picture"),PICK_IMAGE);
            }
        });

            View view = inflater.inflate(R.layout.item,gallery,false);
            ImageView red = view.findViewById(R.id.colorview);

            red.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int red = Color.parseColor("#A6FF0000");
                    Smallbox.setColorFilter(red);
                }
            });


            TextView textView2 = view.findViewById(R.id.itemText2);
            ImageView green = view.findViewById(R.id.colorview2);
            green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int green = Color.parseColor("#A600FF00");
                Smallbox.setColorFilter(green);
            }

        });

             TextView textView3 = view.findViewById(R.id.itemText3);

             ImageView blue = view.findViewById(R.id.colorview3);
             blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int blue = Color.parseColor("#A6ADD8E6");
                Smallbox.setColorFilter(blue);
            }
        });

             ImageView purple = view.findViewById(R.id.colorview4);
             purple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int purple = Color.parseColor("#A66a0dad");
                Smallbox.setColorFilter(purple);
            }
        });
              ImageView grey = view.findViewById(R.id.colorview5);
        grey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int grey = Color.parseColor("#A6808080");
                Smallbox.setColorFilter(grey);
            }
        });

            gallery.addView(view);

    }

    public static Bitmap convertToBitmap(ColorDrawable drawable,
                                         int widthPixels, int heightPixels) {
        Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels,
                heightPixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);//from w  w w  .ja v a 2  s. c o m
        return mutableBitmap;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView GallaryPic = (ImageView) findViewById(R.id.galleryPicture);


        if(requestCode == PICK_IMAGE && resultCode ==RESULT_OK){
            imageUrl = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUrl);
                GallaryPic.setImageBitmap(bitmap);
            }catch (IOException e ){
                e.printStackTrace();
            }
        }
    }
}
