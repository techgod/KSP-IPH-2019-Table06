package com.ksp.donut.uca;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.scottyab.aescrypt.AESCrypt;

public class MainActivity extends AppCompatActivity {
    String TAG = MainActivity.class.getSimpleName();
    NavController mNavController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()!=null)
        {
            Toast.makeText(getApplication(),"Signed in as: "+firebaseAuth.getCurrentUser().getPhoneNumber(),Toast.LENGTH_SHORT).show();
        }


        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        TabLayout tabLayout = findViewById(R.id.tabs);
        mNavController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if(destination.getId()==R.id.nav_chat || destination.getId()==R.id.nav_contact)
                {
                    tabLayout.setVisibility(View.GONE);
                }
                else
                {
                    tabLayout.setVisibility(View.VISIBLE);
                }
            }

        });


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        mNavController.navigate(R.id.nav_task);
                        break;
                    case 1:
                        mNavController.navigate(R.id.nav_dm);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}