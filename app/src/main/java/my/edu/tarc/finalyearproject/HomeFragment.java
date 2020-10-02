package my.edu.tarc.finalyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import my.edu.tarc.finalyearproject.Prevalent.Prevalent;
import my.edu.tarc.finalyearproject.Utils.PreferenceUtils;

public class HomeFragment extends Fragment {

    ImageButton VisualizeButton;
    TextView ShowName;
    FirebaseAuth mAuth;
    ImageView goCal,goEwallet,goColourPicker,goPaint;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //ShowName = (TextView) getView().findViewById(R.id.textView2);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        if (user != null) {
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserData user = dataSnapshot.getValue(UserData.class);
                    Prevalent.currentOnlineUser = user;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
        }


        VisualizeButton = (ImageButton) getView().findViewById(R.id.IbButton);
        VisualizeButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

            loadFragment(new VisualizeFragment());
            }
        });

        goCal = (ImageView) getView().findViewById(R.id.Calculatepaint);
        goPaint =(ImageView) getView().findViewById(R.id.PaintStore);
        goEwallet = (ImageView) getView().findViewById(R.id.HomeEwallet);
        goColourPicker =(ImageView) getView().findViewById(R.id.ColorPicker) ;



        goCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(),Calculate_paint.class);
                startActivity(intent);
            }
        });

        goColourPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity().getApplicationContext(),ColorPicker2.class);
                startActivity(intent);
            }

        });

        goEwallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new EwalletFragment());
            }
        });

        goPaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new StoreFragment());
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

