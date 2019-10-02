package io.github.arnabmaji19.letschat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private FirebaseAuth firebaseAuth;
    private Snackbar loggingInSnackBar;
    private static final String TAG = "LetsChat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.email_edittext);
        passwordEditText = findViewById(R.id.password_edittext);
        firebaseAuth = FirebaseAuth.getInstance();
        loggingInSnackBar = Snackbar.make(findViewById(android.R.id.content),"Logging in...",Snackbar.LENGTH_INDEFINITE);
    }

    public void logInExistingUser(View view){
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        boolean isLogInInformationValid = true;
        if(!isEmailValid(email) || password.equals("")){
            isLogInInformationValid = false;
        }

        if(isLogInInformationValid){
            //Log In the user
            loggingInSnackBar.show();
            attemptLogIn(email,password);
        } else {
            Toast.makeText(this,"Information not valid!",Toast.LENGTH_SHORT).show();
        }
    }

    //Email Id format validation
    private boolean isEmailValid(String email){
        return email.contains("@");
    }

    //Redirects the user to Sign Up page for creating new account.
    public void signUpNewUser(View view){
        startActivity(new Intent(this,SignUpActivity.class));
    }

    //Log In the user
    private void attemptLogIn(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                loggingInSnackBar.dismiss();
                if(task.isSuccessful()){
                   Toast.makeText(LoginActivity.this,"User Logged In",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this,"Oops! Something error occurred!",Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onComplete: "+task.getException());
                }
            }
        });
    }
}
