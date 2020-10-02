package my.edu.tarc.finalyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import my.edu.tarc.finalyearproject.Utils.PreferenceUtils;

public class HomeFragment extends Fragment {

    ImageButton VisualizeButton;
    TextView ShowName;
    FirebaseAuth mAuth;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       return inflater.inflate(R.layout.fragment_home,container,false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ShowName = (TextView) getView().findViewById(R.id.textView2);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        if(user!= null){
            ShowName.setText(user.getDisplayName());

        }


        VisualizeButton = (ImageButton) getView().findViewById(R.id.IbButton);
        VisualizeButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                Intent vbButton = new Intent(getActivity(),VisualizeFragment.class);

            }
        });
    }
}

