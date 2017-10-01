package com.dannextech.apps.hotelsystem;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginForm extends AppCompatActivity implements View.OnClickListener {

    Button btnLogin;
    TextView tvForgotPass;
    EditText etUserName,etPassword;

    ProgressDialog progressDialog;

    private static final String TAG = "EmailPassword";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_form);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        tvForgotPass = (TextView) findViewById(R.id.tvForgotPassword);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);

        btnLogin.setOnClickListener(this);
        tvForgotPass.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        //Tracking when the user signs in or out...
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!=null){
                    Log.w(TAG,"onAuthStateChanged:signed_in:"+user.getUid());
                }else{
                    Log.w(TAG,"onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Attaching the State Listener to our Firebase Auth instance
        mAuth.addAuthStateListener(mAuthListener);
        //hideProgressDialog();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null){
            //Removing the state Listerner.
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();

        if (i == R.id.btnLogin){
            showProgressDialog();
            String email = etUserName.getText().toString();
            String pass = etPassword.getText().toString();

            if (TextUtils.isEmpty(email)||TextUtils.isEmpty(pass)){
                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(pass)) {
                    etUserName.setError("Please enter an Email here");
                    etPassword.setError("Please enter your Password here");
                }else if (TextUtils.isEmpty(email)){
                    etUserName.setError("Please enter an Email here");
                }else{
                    etPassword.setError("Please enter your Password here");
                }
            }else {
                signIn(email,pass);
            }


        }else if (i == R.id.tvForgotPassword){
            showAlertDialog();
        }
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //sign is success, update ui with the signed in user
                    Log.w(TAG, "signInWithEmail: Success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    Log.d(TAG, "signInUserId: " + user.getUid().toString());
                    Log.d(TAG, "signInUserId: " + user.getEmail().toString());
                    Log.d(TAG, "signInUserId: " + user.getDisplayName().toString());
                    Log.d(TAG, "signInUserId: " + user.getPhotoUrl().toString());
                    Log.d(TAG, "signInUserId: " + user.getProviderId().toString());
                    //updateUI(user);
                } else {
                    //if sign in fails, display a message to the user.
                    Log.e(TAG, "SignInWithEmail: Failure", task.getException());
                    Toast.makeText(getApplicationContext(), "Authentication Failed", Toast.LENGTH_LONG).show();
                    //updateUI(null);
                }
                hideProgressDialog();
            }
        });
    }

    private void showProgressDialog() {
        progressDialog = ProgressDialog.show(LoginForm.this,"Logging In","Please Wait",true);
    }

    private void hideProgressDialog() {
        progressDialog.dismiss();
    }

    private void showAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginForm.this);
        builder.setTitle("Alert");
        builder.setMessage("Please contact the administrator for assistance");

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
