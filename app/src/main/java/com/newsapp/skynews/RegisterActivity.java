package com.newsapp.skynews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = RegisterActivity.class.getSimpleName();

    private FirebaseAuth mAuthenticate;

    private FirebaseAuth.AuthStateListener mAuthenticateListener;

    @BindView(R.id.createUserButton) Button mCreateUserButton;
    @BindView(R.id.nameEditText) EditText mNameEditText;
    @BindView(R.id.emailEditText) EditText mEmailEditText;
    @BindView(R.id.passwordEditText) EditText mPasswordEditText;
    @BindView(R.id.confirmPasswordEditText) EditText mConfirmPasswordEditText;
    @BindView(R.id.loginTextView) TextView mLoginTextView;
    @BindView(R.id.firebaseProgressBar) ProgressBar mSignInProgressBar;
    @BindView(R.id.loadingTextView) TextView mLoadingSignUp;
    private String mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        mLoginTextView.setOnClickListener(this);
        mCreateUserButton.setOnClickListener(this);

        mAuthenticate = FirebaseAuth
                .getInstance();

        createAuthStateListeners();
    }

    private void createAuthStateListeners() {
        mAuthenticateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        if (v == mLoginTextView){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        if (v == mCreateUserButton){
            createAUser();
        }
    }

    private void ProgressBar(){
        mSignInProgressBar.setVisibility(View.VISIBLE);
        mLoadingSignUp.setVisibility(View.VISIBLE);
        mLoadingSignUp.setText("Wait as we sign you in.");
    }

    private void hideProgressBar(){
        mSignInProgressBar.setVisibility(View.GONE);
        mLoadingSignUp.setVisibility(View.GONE);
    }

    private void createAUser() {
        final String name = mNameEditText.getText().toString().trim();
        final String email = mEmailEditText.getText().toString().trim();
        String passWord = mPasswordEditText.getText().toString().trim();
        String confirmPassWord = mConfirmPasswordEditText.getText().toString().trim();
        mName = mNameEditText.getText().toString().trim();

        // implement the validation
        boolean validName = isValidName(mName);
        boolean validUserEmail = isValidEmail(email);
        boolean validUserName = isValidName(name);
        boolean validUserPassword = isValidPassWord(passWord, confirmPassWord);
        if (!validUserName || !validUserEmail || !validUserPassword) return;

        ProgressBar();

        mAuthenticate.createUserWithEmailAndPassword(email, passWord)
                .addOnCompleteListener(this, task -> {
                    hideProgressBar();
                    if (task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "Authentication successful.", Toast.LENGTH_LONG).show();
                        createUserProfile(Objects.requireNonNull(task.getResult().getUser()));
                    } else {
                        Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void createUserProfile(final FirebaseUser user){
        UserProfileChangeRequest addProfileName = new UserProfileChangeRequest.Builder()
                .setDisplayName(mName)
                .build();
        user.updateProfile(addProfileName)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "Your profile name has been set", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private boolean isValidEmail(String email){
        boolean validEmail = (email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!validEmail){
            mEmailEditText.setError("Please enter a valid email address");
            return false;
        }
        return validEmail;
    }

    private boolean isValidName(String name){
        if (name.equals("")){
            mNameEditText.setError("Please enter your name");
            return false;
        }
        return true;
    }

    private boolean isValidPassWord(String password, String confirmPassword){
        if (password.length()<6){
            mPasswordEditText.setError("Please create a password containing at 6 characters");
            return false;
        } else if (!password.equals(confirmPassword)){
            mPasswordEditText.setError("Passwords do not match");
            return false;
        }
        return true;
    }


    @Override
    protected void onStart(){
        super.onStart();
        mAuthenticate.addAuthStateListener(mAuthenticateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuthenticate.removeAuthStateListener(mAuthenticateListener);
    }
}


//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.util.Patterns;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.UserProfileChangeRequest;
//
//import java.util.Objects;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
//public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
//    public static final String TAG = RegisterActivity.class.getSimpleName();
//    private FirebaseAuth mAuth;
//    private FirebaseAuth.AuthStateListener mAuthListener;
//
//    @BindView(R.id.createUserButton) Button mCreateUserButton;
//    @BindView(R.id.nameEditText) EditText mNameEditText;
//    @BindView(R.id.emailEditText) EditText mEmailEditText;
//    @BindView(R.id.passwordEditText) EditText mPasswordEditText;
//    @BindView(R.id.confirmPasswordEditText) EditText mConfirmPasswordEditText;
//    @BindView(R.id.loginTextView) TextView mLoginTextView;
//    @BindView(R.id.firebaseProgressBar) ProgressBar mSignInProgressBar;
//    @BindView(R.id.loadingTextView) TextView mLoadingSignUp;
//
//    private String mName;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);
//        ButterKnife.bind(this);
//
//        mAuth = FirebaseAuth.getInstance();
//
//        mLoginTextView.setOnClickListener(this);
//        mCreateUserButton.setOnClickListener(this);
//
//        createAuthStateListener();
//    }
//
//    @Override
//    public void onClick(View view) {
//        if (view == mLoginTextView) {
//            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//            finish();
//        }
//
//        if (view == mCreateUserButton) {
//            createNewUser();
//        }
//    }
//
//    private void createNewUser() {
//        final String name = mNameEditText.getText().toString().trim();
//        final String email = mEmailEditText.getText().toString().trim();
//        mName = mNameEditText.getText().toString().trim();
//        String password = mPasswordEditText.getText().toString().trim();
//        String confirmPassword = mConfirmPasswordEditText.getText().toString().trim();
//
//        boolean validmName = isValidName(mName);
//        boolean validEmail = isValidEmail(email);
//        boolean validName = isValidName(name);
//        boolean validPassword = isValidPassword(password, confirmPassword);
//        if (!validEmail || !validName || !validPassword) return;
//
//        showProgressBar();
//
//        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
//            hideProgressBar();
//            if (task.isSuccessful()){
//
//                createFirebaseUserProfile(Objects.requireNonNull(task.getResult().getUser()));
//                Log.d(TAG, "Authentication successful");
//            }else {
//                Toast.makeText(RegisterActivity.this,"Authentication failed.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void createAuthStateListener(){
//        mAuthListener = firebaseAuth -> {
//            final FirebaseUser user = firebaseAuth.getCurrentUser();
//            if (user != null){
//                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//                finish();
//            }
//        };
//    }
//
//    @Override
//    public void onStart(){
//        super.onStart();
//        mAuth.addAuthStateListener(mAuthListener);
//    }
//
//    @Override
//    public void onStop(){
//        super.onStop();
//        if(mAuthListener != null){
//            mAuth.removeAuthStateListener(mAuthListener);
//        }
//    }
//
//    private boolean isValidEmail(String email) {
//        boolean isGoodEmail = (email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches());
//        if(!isGoodEmail){
//            mEmailEditText.setError("Please enter a valid email address");
//            return false;
//        }
//        return isGoodEmail;
//    }
//
//    private boolean isValidName(String name){
//        if(name.equals("")){
//            mNameEditText.setError("Please enter your name");
//            return false;
//        }
//        return true;
//    }
//
//    private boolean isValidPassword(String password, String confirmPassword){
//        if(password.length() < 6){
//            mPasswordEditText.setError("Please create a password containing at least 6 characters");
//            return false;
//        } else if (!password.equals(confirmPassword)){
//            mPasswordEditText.setError("Passwords do not match");
//            return false;
//        }
//        return true;
//    }
//
//    private void showProgressBar() {
//        mSignInProgressBar.setVisibility(View.VISIBLE);
//        mLoadingSignUp.setVisibility(View.VISIBLE);
//        mLoadingSignUp.setText("Sign Up process in Progress");
//    }
//
//    private void hideProgressBar() {
//        mSignInProgressBar.setVisibility(View.GONE);
//        mLoadingSignUp.setVisibility(View.GONE);
//    }
//
//    private void createFirebaseUserProfile(final FirebaseUser user){
//        UserProfileChangeRequest addProfileName = new UserProfileChangeRequest.Builder()
//                .setDisplayName(mName)
//                .build();
//
//        user.updateProfile(addProfileName)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if(task.isSuccessful()){
//                            Log.d(TAG, Objects.requireNonNull(user.getDisplayName()));
//                            Toast.makeText(RegisterActivity.this, "The display name has ben set", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
//    }
//}