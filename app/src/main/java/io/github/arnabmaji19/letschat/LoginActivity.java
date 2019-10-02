package io.github.arnabmaji19.letschat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    //Redirects the user to Sign Up page for creating new account.
    public void signUpNewUser(View view){
        startActivity(new Intent(this,SignUpActivity.class));
    }
}
