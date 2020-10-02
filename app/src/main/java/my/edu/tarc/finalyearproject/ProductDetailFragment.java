package my.edu.tarc.finalyearproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import my.edu.tarc.finalyearproject.Prevalent.ProductID;

public class ProductDetailFragment extends Fragment {

    private Button addToCartButton;
    private ImageView productImage,addBtn,subtractBtn;
    private TextView productName, productDesc, productCat, productPrice;
    public int Stock;
    int counter;
    private String productID = "";
    TextView showQuantity;

    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_product_detail,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        productID = ProductID.pid;
        showQuantity = (TextView) getView().findViewById(R.id.quantity);

        addToCartButton = (Button) getView().findViewById(R.id.add_to_cart_button);
        addBtn = (ImageView) getView().findViewById(R.id.add);
        subtractBtn = (ImageView) getView().findViewById(R.id.subtract);
        productImage = (ImageView)getView().findViewById(R.id.productDetail);
        productName = (TextView)getView().findViewById(R.id.productDetailName);
        productDesc = (TextView)getView().findViewById(R.id.productDetailDesc);
        productCat = (TextView)getView().findViewById(R.id.productDetailCat);
        productPrice = (TextView)getView().findViewById(R.id.productDetailPrice);
        mAuth = FirebaseAuth.getInstance();

        getProductDeatails(productID);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (counter==0) {
                    Toast.makeText(getContext(),"Out of Stock",Toast.LENGTH_SHORT).show();
                }else{

                    counter++;
                    showQuantity.setText(Integer.toString(counter));
                    if (counter >= Stock) {
                        String s = String.valueOf(Stock);
                        showQuantity.setText(s);
                        counter = Stock;
                    }
                }
            }
        });

        addBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (counter>0){
                    String s = String.valueOf(Stock);
                    showQuantity.setText(s);
                    counter=Stock;}else{
                    Toast.makeText(getContext(),"Out of Stock",Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        } );

        subtractBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (counter>0){
                    showQuantity.setText("1");
                    counter=1;}else{
                    Toast.makeText(getContext(),"Out of Stock",Toast.LENGTH_SHORT).show();
                }
                return true;

            }
        } );

        subtractBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter==0) {
                    Toast.makeText(getContext(),"Out of Stock",Toast.LENGTH_SHORT).show();
                }else{
                    counter--;
                    showQuantity.setText(Integer.toString(counter));
                    if (counter<=1) {
                        showQuantity.setText("1");
                        counter = 1;
                    }
                }
            }
        });

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (counter>0) {
                    if (mAuth.getCurrentUser().getUid() != null) {
                        addingToCartList();
                    } else {
                        loadFragment(new AccountFragment());
                        Toast.makeText(getContext(), "Login First", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getContext(),"Out of Stock",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void addingToCartList(){

        String saveCurrentTime, saveCurrentDate;
        TimeZone tz = TimeZone.getTimeZone("GMT+8:00");
        Calendar calForDate = Calendar.getInstance(tz);
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        saveCurrentTime = String.format("%02d" , calForDate.get(Calendar.HOUR_OF_DAY))+":"+
                String.format("%02d" , calForDate.get(Calendar.MINUTE))+":"+
                String.format("%02d" , calForDate.get(Calendar.SECOND));

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String, Object> cartMap = new HashMap<>();


        String s=productPrice.getText().toString();
        double i= Double.parseDouble(s.replaceAll("[\\D]", ""));
        cartMap.put("pid", productID);
        cartMap.put("pname", productName.getText().toString());
        cartMap.put("pprice", i/100);
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", counter);
        cartMap.put("discount", "");

        cartListRef.child("User View").child(mAuth.getUid()).child(productID).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){

                    Toast.makeText(getContext(), "Added to Cart List", Toast.LENGTH_SHORT).show();

                    loadFragment(new StoreFragment());

                }
            }
        });
    }

    private void getProductDeatails(String productID) {
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("image");
        mDatabaseRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (getActivity() == null) {
                    return;
                }else if (dataSnapshot.exists()){
                    ImageUpload product = dataSnapshot.getValue(ImageUpload.class);
                    productName.setText(product.getName());
                    productDesc.setText(product.getDesc());
                    productCat.setText(product.getCategory());
                    productPrice.setText(String.format("RM%.2f", product.getPrice()));
                    Stock = product.getAvailableStock();
                    if (Stock>=1) {
                        counter = 1;
                    }else{
                        counter=0;}
                    Glide.with(getContext())
                            .load(product.getUrl())
                            .placeholder(R.drawable.blank_image_icon)
                            .into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}