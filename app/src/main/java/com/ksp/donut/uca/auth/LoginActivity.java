package com.ksp.donut.uca.auth;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.ksp.donut.uca.MainActivity;
import com.ksp.donut.uca.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Making notification bar transparent


        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        Fragment fragment = null;
        FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(this, MainActivity.class));
        }
        else {
            fragment = new GettingStarted();
            displayFragment(fragment);
        }

    }


    private void displayFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.login, fragment)
                    .addToBackStack("FRAG_BACK")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }
    }
}
