package com.dannextech.apps.hotelsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInForm extends AppCompatActivity {

    private static final String TAG = "Firebase Information Around Here:";

    private EditText etEmail,etPassword,etConfPassword;
    private Button btSignInUser;
    private ProgressBar pbLoading;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_form);

        etEmail = (EditText) findViewById(R.id.etNewUserEmail);
        etPassword = (EditText) findViewById(R.id.etNewUserPassword);
        etConfPassword = (EditText) findViewById(R.id.etNewUserPasswordConf);
        btSignInUser = (Button) findViewById(R.id.btNewUserSignUp);
        pbLoading = (ProgressBar) findViewById(R.id.pbLoading);



        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    Log.d(TAG, "onAuthStateChanged: Signed_In: " + user.getUid());
                }else{
                    Log.d(TAG, "onAuthStateChanged: Signed_Out");
                }
            }
        };

        btSignInUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String confPassword = etConfPassword.getText().toString();

                if (TextUtils.isEmpty(email))
                    etEmail.setError("Email is Required");
                else if(TextUtils.isEmpty(password))
                    etPassword.setError("Password is Required");
                else if (TextUtils.isEmpty(confPassword))
                    etConfPassword.setError("Confirmation Password is Required");
                else if (!password.equals(confPassword))
                    Toast.makeText(getApplicationContext(),"Passwords do not Match...",Toast.LENGTH_LONG).show();
                else{
                    createAccount(email,password);
                    Intent myIntent = new Intent(getApplicationContext(),CreateNewUser.class);
                    startActivity(myIntent);
                }

            }
        });

    }

    private void createAccount(String email, String password){
        Log.w(TAG, "Create Account: "+email);

        showProgressDialog();

        //Start Creating User with Email.
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //Sign in success, update UI with the signed in user's information
                    Log.w(TAG, "signInWithEmail: success");
                    FirebaseUser user = mAuth.getCurrentUser();

                    sendEmailVerifaction();
                    //updateUI(user);
                }else{
                    //if sign in fails, display a message to the user.
                    Log.e(TAG,"signInWithEmail:Failure", task.getException());
                    Toast.makeText(getApplicationContext(),"Authentication Failed",Toast.LENGTH_LONG).show();
                    //updateUI(null);
                }

                //Start_Exclude
                hideProgressDialog();
                //End_Exclude

            }
        });//End Create User with Email
    }

    private void hideProgressDialog() {
        pbLoading.setVisibility(View.INVISIBLE);
    }

    private void showProgressDialog() {
        pbLoading.setVisibility(View.VISIBLE);
    }

    //Sending Verification Email for new users
    private void sendEmailVerifaction(){

        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.d(TAG,"Sent a verification email...");
                    Toast.makeText(getApplicationContext(),"Verification email sent to "+ user.getEmail(),Toast.LENGTH_LONG).show();
                }else {
                    Log.e(TAG, "sendEmailVerification",task.getException());
                    Toast.makeText(getApplicationContext(),"Failed to send verification email", Toast.LENGTH_LONG).show();
                }
            }
        });//End send email verification
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
