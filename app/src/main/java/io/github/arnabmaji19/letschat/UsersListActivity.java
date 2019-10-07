package io.github.arnabmaji19.letschat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UsersListActivity extends AppCompatActivity {

    private TextView currentUseremailTextView;
    private TextView currentUserIdTextView;
    private RecyclerView usersListRecyclerView;
    private UsersListAdapter listAdapter;
    private DatabaseReference reference;
    private String currentUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);
        //Linking views
        currentUseremailTextView = findViewById(R.id.currentUseremailTextView);
        currentUserIdTextView = findViewById(R.id.currentUserIdTextView);
        usersListRecyclerView = findViewById(R.id.usersListRecyclerView);
        usersListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        currentUserEmail = getSharedPreferences(getPackageName(),MODE_PRIVATE).getString("current_user_email",null);
        currentUseremailTextView.setText(currentUserEmail);
        currentUserIdTextView.setText(currentUserEmail.split("@")[0]);
        reference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onStart() {
        super.onStart();
        listAdapter = new UsersListAdapter(UsersListActivity.this, reference);
        usersListRecyclerView.setAdapter(listAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        listAdapter.cleanUp();
    }
}
