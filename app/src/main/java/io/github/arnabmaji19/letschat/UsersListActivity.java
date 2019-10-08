package io.github.arnabmaji19.letschat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UsersListActivity extends AppCompatActivity {
    
    private RecyclerView usersListRecyclerView;
    private UsersListAdapter listAdapter;
    private DatabaseReference reference;
    String currentUserEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);
        //Linking views
        TextView currentUserEmailTextView = findViewById(R.id.currentUseremailTextViewUsersListActivity);
        TextView currentUserIdTextView = findViewById(R.id.currentUserIdTextViewUsersListActivity);
        usersListRecyclerView = findViewById(R.id.usersListRecyclerView);
        usersListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        currentUserEmail = getSharedPreferences(getPackageName(),MODE_PRIVATE).getString("current_user_email","");
        currentUserEmailTextView.setText(currentUserEmail);
        currentUserIdTextView.setText(currentUserEmail.split("@")[0]);
        reference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.settings){
            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        listAdapter = new UsersListAdapter(UsersListActivity.this, reference, currentUserEmail);
        usersListRecyclerView.setAdapter(listAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        listAdapter.cleanUp();
    }
}
