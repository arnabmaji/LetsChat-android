package io.github.arnabmaji19.letschat;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ChatListAdapter extends BaseAdapter {
    private Activity activity;
    private String username;
    private DatabaseReference databaseReference;
    private ArrayList<DataSnapshot> dataSnapshots;
    private ChildEventListener eventListener;

    public ChatListAdapter(Activity activity, String username, DatabaseReference databaseReference) {
        this.activity = activity;
        this.username = username;
        eventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                dataSnapshots.add(dataSnapshot);
                notifyDataSetChanged();
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
        this.databaseReference = databaseReference.child("messages");
        databaseReference.addChildEventListener(eventListener);
        dataSnapshots = new ArrayList<>();
    }

    static class ViewHolder{
        TextView author;
        TextView message;
        LinearLayout.LayoutParams params;
    }

    @Override
    public int getCount() {
        return dataSnapshots.size();
    }

    @Override
    public InstantMessage getItem(int i) {
        DataSnapshot snapshot = dataSnapshots.get(i);
        return snapshot.getValue(InstantMessage.class);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.chat_message_row,viewGroup,false);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.author = view.findViewById(R.id.authorTextView);
            viewHolder.message = view.findViewById(R.id.messageTextView);
            viewHolder.params = (LinearLayout.LayoutParams) viewHolder.author.getLayoutParams();
            view.setTag(viewHolder);
        }

        final InstantMessage instantMessage = getItem(i);
        final ViewHolder viewHolder = (ViewHolder) view.getTag();

        String author = instantMessage.getAuthor();
        viewHolder.author.setText(author);

        String message = instantMessage.getMessage();
        viewHolder.message.setText(message);
        return view;
    }

    void cleanUp(){
        databaseReference.removeEventListener(eventListener);
    }
}
