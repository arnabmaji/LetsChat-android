package io.github.arnabmaji19.letschat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UsersListActivity extends AppCompatActivity {

    private TextView currentUserTextView;
    private RecyclerView usersListRecyclerView;
    private UsersListAdapter listAdapter;
    private DatabaseReference reference;
    private String currentUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);
        //Linking views
        currentUserTextView = findViewById(R.id.currentUserTextView);
        usersListRecyclerView = findViewById(R.id.usersListRecyclerView);
        usersListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        currentUserEmail = getSharedPreferences(getPackageName(),MODE_PRIVATE).getString("current_user_email",null);
        String currentUser = "Current User:\n" + currentUserEmail;
        currentUserTextView.setText(currentUser);
        reference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onStart() {
        super.onStart();
        listAdapter = new UsersListAdapter(UsersListActivity.this,currentUserEmail, reference);
        usersListRecyclerView.setAdapter(listAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        listAdapter.cleanUp();
    }
}
