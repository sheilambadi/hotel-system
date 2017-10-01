package com.dannextech.apps.hotelsystem;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CreateNewUser extends AppCompatActivity {

    private StorageReference mStorageRef;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myDBRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private static final String TAG = "Firebase Authentication Status";

    private ImageButton btProfilePic;
    private EditText etFullNames,etIdNumber,etYOB,etGender,etPhone,etPosition;
    private Button btSignUp;
    private LinearLayout lvCreateNewUser;

    private final int GALLERY_ACTIVITY_CODE = 2;
    private String imagePath,userId;
    private Uri url;


    EditText etEmail,etPassword;
    Button submit;

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(getApplicationContext(),"Network is "+ isNetworkAvailable(),Toast.LENGTH_SHORT).show();

        inputEmailAddress();



    }

    private void inputPassword(final String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this,R.style.myDialog));
        builder.setTitle("Verification Details");

        //set up the inputs
        etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(etPassword);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (etPassword.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter a password",Toast.LENGTH_SHORT).show();
                }else {
                    createAccount(s,etPassword.getText().toString());
                }
            }
        });
        builder.show();
    }

    private void inputEmailAddress() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Verification Details");

        //set up the inputs
        etEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        etEmail.setHint("Enter Email Adress");

        builder.setView(etEmail);

        builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (etEmail.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter a valid email address",Toast.LENGTH_SHORT).show();
                }else {

                    inputPassword(etEmail.getText().toString());
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_user);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        etFullNames = (EditText) findViewById(R.id.etFullNames);
        etIdNumber = (EditText) findViewById(R.id.etIDNo);
        etYOB = (EditText) findViewById(R.id.etYOB);
        etGender = (EditText) findViewById(R.id.etGender);
        etPhone = (EditText) findViewById(R.id.etPhoneNo);
        etPosition = (EditText) findViewById(R.id.etPosition);
        btSignUp = (Button) findViewById(R.id.btSignUp);
        btProfilePic = (ImageButton) findViewById(R.id.ibtProfilePic);
        lvCreateNewUser = (LinearLayout) findViewById(R.id.lvCreateNewUser);

        submit = new Button(this);

        etEmail = new EditText(this);
        etPassword = new EditText(this);

        etEmail.setHint("Enter Email Address");
        etPassword.setHint("Enter Password");
        submit.setText("Submit");


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = etEmail.getText().toString().trim();
                String pass = etPassword.getText().toString().trim();
                if (mail.isEmpty() || pass.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                    builder.setMessage("Please make sure that you have filled all fields and that you have entered a valid email address")
                            .setTitle("Error Signing Up")
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    setUserId(createAccount(mail,pass));
                    getUserData();
                }
            }
        });


    mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    setUserId(user.getUid());
                    Log.d(TAG, "onAuthStateChanged: Signed_In: " + user.getUid());
                    Toast.makeText(getApplicationContext(),"user Signed In",Toast.LENGTH_SHORT).show();
                }else{
                    Log.d(TAG, "onAuthStateChanged: Signed_Out");
                    Toast.makeText(getApplicationContext(),"user is out there",Toast.LENGTH_SHORT).show();
                }
            }
        };



        btProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery_intent = new Intent(Intent.ACTION_GET_CONTENT);
                gallery_intent.setType("image/*");
                startActivityForResult(gallery_intent,GALLERY_ACTIVITY_CODE);
            }
        });

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                getUserData();
            }
        });
    }

    private String createAccount(String email, String password) {
        Log.w(TAG, "Create Account: " + email);
        final String[] userId = new String[1];

        //showProgressDialog();

        //Start Creating User with Email.
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //Sign in success, update UI with the signed in user's information
                    Log.w(TAG, "signInWithEmail: success");
                    FirebaseUser user = mAuth.getCurrentUser();

                    sendEmailVerifaction();
                    //updateUI(user);

                    userId[0] = user.getUid().toString();
                } else {
                    //if sign in fails, display a message to the user.
                    Log.e(TAG, "signInWithEmail:Failure", task.getException());
                    Toast.makeText(getApplicationContext(), "Authentication Failed", Toast.LENGTH_LONG).show();
                    //updateUI(null);
                }


                //Start_Exclude
                // hideProgressDialog();
                //End_Exclude

            }
        });//End Create User with Email
        return userId[0];
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

    private void getUserData() {
        String name = etFullNames.getText().toString();
        String idNo = etIdNumber.getText().toString();
        String yob = etYOB.getText().toString();
        String gender = etGender.getText().toString();
        String phone = etPhone.getText().toString();
        String position = etPosition.getText().toString();
        //String imgPath = null;
        if (url!=null){
            uploadImage(url);
        }else {
            Toast.makeText(getApplicationContext(),"Image is required",Toast.LENGTH_LONG).show();
        }

        if (!TextUtils.isEmpty(name)||!TextUtils.isEmpty(idNo)||!TextUtils.isEmpty(yob)||!TextUtils.isEmpty(gender)||!TextUtils.isEmpty(phone)||!TextUtils.isEmpty(position)||imagePath!=null){
            addEmployee(name,idNo,yob,gender,phone,position,imagePath,getUserId());
        }else {
            Toast.makeText(getApplicationContext(),"All fields are required",Toast.LENGTH_LONG).show();
        }

    }

    private void addEmployee(String name, String idNo, String yob, String gender, String phone, String position, String image, String userId) {
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance();
        myDBRef = mDatabase.getReference().child("Employees").push();

        DatabaseReference nameRef = myDBRef.child("Name");
        DatabaseReference idNoRef = myDBRef.child("ID Number");
        DatabaseReference yobRef = myDBRef.child("YOB");
        DatabaseReference genderRef = myDBRef.child("Gender");
        DatabaseReference phoneRef = myDBRef.child("Phone");
        DatabaseReference positionRef = myDBRef.child("Position");
        DatabaseReference userIdRef = myDBRef.child("User Id");
        DatabaseReference imageRef = myDBRef.child("Image");

        nameRef.setValue(name);
        idNoRef.setValue(idNo);
        yobRef.setValue(yob);
        genderRef.setValue(gender);
        phoneRef.setValue(phone);
        positionRef.setValue(position);
        userIdRef.setValue(userId);
        imageRef.setValue(image);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_ACTIVITY_CODE && resultCode == RESULT_OK){
           url = data.getData();
            btProfilePic.setImageURI(url);

            Toast.makeText(getApplicationContext(),url.toString(),Toast.LENGTH_LONG).show();

        }
    }
    private String uploadImage(Uri url){
        StorageReference filePath = mStorageRef.child("UserPhotos").child(url.getLastPathSegment());
        String path = mStorageRef.getPath();

        filePath.putFile(url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Get a URL to the uploaded Content
               Uri photoUrl = taskSnapshot.getDownloadUrl();
                setImagePath(photoUrl.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Error: Check Your Internet Connection",Toast.LENGTH_LONG).show();
            }
        });
        return path;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public String getImagePath(){
        return imagePath;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserId(){
        return userId;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
