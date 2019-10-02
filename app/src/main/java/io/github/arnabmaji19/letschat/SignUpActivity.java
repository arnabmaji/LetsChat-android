package io.github.arnabmaji19.letschat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private EditText userNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private FirebaseAuth firebaseAuth;
    private Snackbar signingUpSnackBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //Linking EditTexts to views
        userNameEditText = findViewById(R.id.username_edittext);
        emailEditText = findViewById(R.id.email_edittext);
        passwordEditText = findViewById(R.id.password_edittext);
        confirmPasswordEditText = findViewById(R.id.confirm_password_edittext);
        firebaseAuth = FirebaseAuth.getInstance();
        signingUpSnackBar = Snackbar.make(findViewById(android.R.id.content),"Signing you up...",Snackbar.LENGTH_INDEFINITE);
    }

    //When the user clicks Sign Up Button
    public void signUpUser(View view){
        String userName =  userNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        boolean isAllInformationValid = true;
        if(!isEmailValid(email)){
            emailEditText.setError("Email Id not valid");
            isAllInformationValid = false;
            emailEditText.requestFocus();
        }
        if(!isPasswordMatched(password,confirmPassword)){
            passwordEditText.setError("password too short or does not match");
            isAllInformationValid = false;
            passwordEditText.requestFocus();
        }
        if(isAllInformationValid){
            //Create new User in FireBase
            createNewUserAccount(email,password);
        } else{
            Toast.makeText(this,"Sign up information invalid for " + userName, Toast.LENGTH_SHORT).show();
        }

    }

    //Email Id format validation
    private boolean isEmailValid(String email){
        return email.contains("@");
    }

    //Password match validation
    private boolean isPasswordMatched(String password, String confirmPassword){
        return password.equals(confirmPassword) && password.length() > 5;
    }
    //Create new user account
    private void createNewUserAccount(String email, String password){
        signingUpSnackBar.show();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                signingUpSnackBar.dismiss();
                if(task.isSuccessful()){
                    Snackbar.make(findViewById(android.R.id.content),"Successfully Signed up!",Snackbar.LENGTH_SHORT)
                            .show();
                    startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                } else {
                    Snackbar.make(findViewById(android.R.id.content),"Oops! Something went wrong",Snackbar.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }
}
