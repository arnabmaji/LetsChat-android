package io.github.arnabmaji19.letschat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatActivity extends AppCompatActivity {

    private EditText userMessageEditText;
    private DatabaseReference databaseReference;
    private ChatMessagesAdapter adapter;
    private String username;
    private RecyclerView chatMessagesRecyclerView;
    private String currentUserEmail;
    private String chatWithUserEmail;
    private String currentUserId;
    private String chatWithUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Linking views of layout
        userMessageEditText = findViewById(R.id.user_message_edittext);
        //Setting up the RecyclerView
        chatMessagesRecyclerView = findViewById(R.id.chatMessages);
        chatMessagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Determining the chat preferences
        currentUserEmail = getSharedPreferences(getPackageName(),MODE_PRIVATE).getString("current_user_email",null);
        chatWithUserEmail = getIntent().getStringExtra("chat_with_email");
        String chatId = getChatId(currentUserEmail,chatWithUserEmail);
        //Getting database reference for current chat
        databaseReference = FirebaseDatabase.getInstance().getReference().child("chats").child(chatId);
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
            InstantMessage chat = new InstantMessage(currentUserId,userMessage);
            databaseReference.push().setValue(chat);
            userMessageEditText.setText(""); //Clears the current message from edit text
            hideKeyBoardFromWindow(); //Hides keyboard from window
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter = new ChatMessagesAdapter(currentUserId, databaseReference);
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

    //Determines the chat id for these two users
    private String getChatId(String currentUserEmail, String chatWithUserEmail){
        currentUserId = currentUserEmail.split("@")[0];
        chatWithUserId = chatWithUserEmail.split("@")[0];
        String chatId = "";
        if(currentUserEmail.compareTo(chatWithUserEmail) > 0){
            chatId = getASCIIString(currentUserId) + "_AND_" + getASCIIString(chatWithUserId);
        } else {
            chatId = getASCIIString(chatWithUserId) + "_AND_" + getASCIIString(currentUserId);
        }
        return chatId;
    }
    //Generates ASCII String of Integers
    private String getASCIIString(String str){
        StringBuilder builder = new StringBuilder();
        for(int i=0;i<str.length();i++){
            builder.append((int)str.charAt(i));
        }
        return builder.toString();
    }
}
