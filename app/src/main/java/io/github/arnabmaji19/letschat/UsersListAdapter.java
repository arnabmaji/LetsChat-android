package io.github.arnabmaji19.letschat;

import android.app.Activity;
import android.content.Intent;
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

public class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.UserViewHolder> {
    private DatabaseReference databaseReference;
    private ArrayList<DataSnapshot> usersSnapshots;
    private ChildEventListener eventListener;
    private Activity activity;
    private String currentUserEmail;

    UsersListAdapter(Activity activity, DatabaseReference reference, final String currentUserEmail){
        this.activity = activity;
        this.currentUserEmail = currentUserEmail;
        usersSnapshots = new ArrayList<>();
        databaseReference = reference.child("users");
        eventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = dataSnapshot.getValue(User.class);
                if(!user.getEmail().equals(currentUserEmail)){
                    usersSnapshots.add(dataSnapshot);
                    notifyDataSetChanged();
                }
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
        databaseReference.addChildEventListener(eventListener);
    }

    @NonNull
    @Override
    public UsersListAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.users_list_row,parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersListAdapter.UserViewHolder holder, final int position) {
        final DataSnapshot userSnapshot = usersSnapshots.get(position);
        User user = userSnapshot.getValue(User.class);
        holder.usernameTextView.setText(user.getUsername());
        holder.emailTextView.setText(user.getEmail());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataSnapshot snapshot = usersSnapshots.get(position);
                User user = snapshot.getValue(User.class);
                startChatWith(user.getEmail());
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersSnapshots.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder{
        TextView usernameTextView;
        TextView emailTextView;
        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usersListRowusername);
            emailTextView = itemView.findViewById(R.id.usersListRowemail);
        }
    }

    public void cleanUp(){
        databaseReference.removeEventListener(eventListener);
    }

    private void startChatWith(String email){
        Intent chatIntent = new Intent(activity,ChatActivity.class);
        chatIntent.putExtra("chat_with_email",email);
        activity.startActivity(chatIntent);
    }
}
