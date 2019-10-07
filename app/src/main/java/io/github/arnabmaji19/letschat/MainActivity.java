package io.github.arnabmaji19.letschat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText userMessageEditText;
    private DatabaseReference databaseReference;
    private ChatMessagesAdapter adapter;
    private String username;
    private RecyclerView chatMessagesRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Linking views of layout
        userMessageEditText = findViewById(R.id.user_message_edittext);
        //Setting up the RecyclerView
        chatMessagesRecyclerView = findViewById(R.id.chatMessages);
        layoutManager = new LinearLayoutManager(this);
        chatMessagesRecyclerView.setLayoutManager(layoutManager);

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
            hideKeyBoardFromWindow(); //Hides keyboard from window
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter = new ChatMessagesAdapter(username, databaseReference);
        chatMessagesRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.cleanUp();
    }

    private void hideKeyBoardFromWindow(){
        ConstraintLayout layout = findViewById(R.id.mainChatLayout);
        InputMethodManager methodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        methodManager.hideSoftInputFromWindow(layout.getWindowToken(),0);
    }
}
