package io.github.arnabmaji19.letschat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ChatMessagesAdapter extends RecyclerView.Adapter<ChatMessagesAdapter.MessageViewHolder> {

    private ArrayList<DataSnapshot> snapshots;
    private DatabaseReference databaseReference;
    private ChildEventListener eventListener;
    private String currentUserId;
    private RecyclerView view;

    ChatMessagesAdapter(String username , DatabaseReference databaseReference, final RecyclerView view) {
        snapshots = new ArrayList<>();
        this.view = view;
        this.currentUserId = username;
        this.databaseReference = databaseReference;
        eventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                snapshots.add(dataSnapshot);
                notifyDataSetChanged();
                view.smoothScrollToPosition(snapshots.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        this.databaseReference.addChildEventListener(eventListener);
    }


    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.chat_message_row,parent,false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        final DataSnapshot dataSnapshot = snapshots.get(position);
        InstantMessage instantMessage = dataSnapshot.getValue(InstantMessage.class);
        String author = instantMessage.getAuthor();
        if(author.equals(currentUserId)){
            //If this user has sent the message
            //Show the right
            holder.thisAuthorTextView.setVisibility(View.VISIBLE);
            holder.thisMessageTextView.setVisibility(View.VISIBLE);
            holder.thisAuthorTextView.setText(author);
            holder.thisMessageTextView.setText(instantMessage.getMessage());
            //hide the left
            holder.otherAuthorTextView.setVisibility(View.GONE);
            holder.otherMessageTextView.setVisibility(View.GONE);
        } else {
            //hide the right
            holder.thisAuthorTextView.setVisibility(View.GONE);
            holder.thisMessageTextView.setVisibility(View.GONE);
            //show the left
            holder.otherAuthorTextView.setVisibility(View.VISIBLE);
            holder.otherMessageTextView.setVisibility(View.VISIBLE);
            holder.otherAuthorTextView.setText(author);
            holder.otherMessageTextView.setText(instantMessage.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return snapshots.size();
    }

    void cleanUp(){
        databaseReference.removeEventListener(eventListener);
    }

    //Inner class for Message View Holder
    static class MessageViewHolder extends RecyclerView.ViewHolder{
        TextView otherAuthorTextView;
        TextView otherMessageTextView;
        TextView thisAuthorTextView;
        TextView thisMessageTextView;

        MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            otherAuthorTextView = itemView.findViewById(R.id.otherAuthorTextView);
            otherMessageTextView = itemView.findViewById(R.id.otherMessageTextView);
            thisAuthorTextView = itemView.findViewById(R.id.thisAuthorTextView);
            thisMessageTextView = itemView.findViewById(R.id.thisMessageTextView);
        }
    }
}
