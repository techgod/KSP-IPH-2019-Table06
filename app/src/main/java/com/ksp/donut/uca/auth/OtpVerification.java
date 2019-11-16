package com.ksp.donut.uca.auth;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.ksp.donut.uca.MainActivity;
import com.ksp.donut.uca.R;

import static android.content.ContentValues.TAG;
import static com.ksp.donut.uca.auth.Signup.mVerificationId;


/**
 * A simple {@link Fragment} subclass.
 */
public class OtpVerification extends Fragment implements View.OnClickListener,OnCompleteListener<AuthResult> {

    private FirebaseAuth mAuth;
    private EditText editText;

    public OtpVerification() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_otp_verification, container, false);


        mAuth = FirebaseAuth.getInstance();
        editText = view.findViewById(R.id.enter_otp);
        view.findViewById(R.id.confirm).setOnClickListener(this);

        return view;
    }


    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        Log.i("signIn","ss");
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), this);
    }

    @Override
    public void onClick(View v) {

        verifyPhoneNumberWithCode(mVerificationId,editText.getText().toString());

    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {

        if (task.isSuccessful()) {
            // Sign in success, update UI with the signed-in user's information
            Log.d(TAG, "signInWithCredential:success");

            FirebaseUser user = task.getResult().getUser();
            startActivity(new Intent(getActivity(), MainActivity.class));

            // ...
        } else {
            // Sign in failed, display a message and update the UI
            Log.w(TAG, "signInWithCredential:failure", task.getException());
            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                // The verification code entered was invalid
            }
        }

    }
}
