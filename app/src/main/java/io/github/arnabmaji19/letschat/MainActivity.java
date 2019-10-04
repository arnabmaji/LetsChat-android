package io.github.arnabmaji19.letschat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText userMessageEditText;
    private ImageView sendButton;
    private ListView messagesListView;
    private DatabaseReference databaseReference;
    private ChatListAdapter adapter;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Linking views of layout
        userMessageEditText = findViewById(R.id.user_message_edittext);
        sendButton = findViewById(R.id.send_message_button);
        messagesListView = findViewById(R.id.messages_listview);
        //Getting database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //Retrieving username
        username = getSharedPreferences(getPackageName(),MODE_PRIVATE).getString("username",null);
        if(username == null){
            username = "Anonymous";
        }
    }

    //Send a message to firebase cloud
    public void sendMessage(View view){
        String userMessage = userMessageEditText.getText().toString();
        if(!userMessage.equals("")){
            InstantMessage chat = new InstantMessage(username,userMessage);
            databaseReference.child("messages").push().setValue(chat);
            userMessageEditText.setText(""); //Clears the current message from edit text
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter = new ChatListAdapter(MainActivity.this,username,databaseReference);
        messagesListView.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.cleanUp();
    }
}
