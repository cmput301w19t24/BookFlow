package com.example.bookflow;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

/**
 * Notification Holder Class
 */
public class NotificationHolder extends RecyclerView.ViewHolder{

    private final TextView notification_text;
    private final TextView notification_type;
    private final ImageView notification_sender_icon;
    private final TextView notification_timestamp;
    FirebaseDatabase mDatabase;
    private NotificationHolder.ClickListener mClickListener;

    public NotificationHolder(@NonNull View itemView) {
        super(itemView);
        notification_text = itemView.findViewById(R.id.notification_text);
        notification_type = itemView.findViewById(R.id.notification_type);
        notification_sender_icon = itemView.findViewById(R.id.notification_sender_icon);
        notification_timestamp = itemView.findViewById(R.id.notification_timestamp);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());

            }
        });
    }

    /**
     * Click Listener Interface
     */
    public interface ClickListener{
        public void onItemClick(View view, int position);
//        public void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(NotificationHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }

    public void setNotificationText(String s) {
        notification_text.setText(s);
    }

    public void setNotificationType(String s) {
        notification_type.setText(s);
    }

    public void setNotificationSenderIcon(StorageReference s) {
        Glide.with(itemView.getContext())
                .load(s).into(notification_sender_icon);
    }
    public void setNotificationTimestamp(String s) {
        notification_timestamp.setText(s);
    }

    public void testNotificationText(String requesterId) {
        final String reqId = requesterId;
        mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference userRef = mDatabase.getReference("Users");

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String requesterName = dataSnapshot.child(reqId).child("username").getValue().toString();
                notification_text.setText(requesterName);
                Log.e("help", requesterName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("cancelled", databaseError.toException());
            }
        };
        userRef.addListenerForSingleValueEvent(userListener);
    }
}
