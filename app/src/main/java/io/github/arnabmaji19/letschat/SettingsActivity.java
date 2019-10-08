package io.github.arnabmaji19.letschat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //Getting current user's info
        preferences = getSharedPreferences(getPackageName(),MODE_PRIVATE);
        String userEmail = preferences.getString("current_user_email","");

        //Linking the views
        listView = findViewById(R.id.settingsListView);
        TextView userInfoTextView = findViewById(R.id.userInfoTextView);
        String info = userEmail.split("@")[0]+"\n"+userEmail;
        userInfoTextView.setText(info);

        //Settings up list view
        String[] options = {"Support"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,options);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0){
                    startActivity(new Intent(SettingsActivity.this,SupportActivity.class));
                }
            }
        });
    }

    public void logOutCurrentUser(View view){
        Toast.makeText(this,"User Logged Out",Toast.LENGTH_SHORT).show();
        //Clearing out saved Login info
        preferences.edit()
                .putString("current_user_email",null)
                .putString("current_user_password",null)
                .apply();
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }
}
