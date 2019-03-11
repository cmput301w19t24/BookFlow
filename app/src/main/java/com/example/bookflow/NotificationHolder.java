package com.example.bookflow;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

public class NotificationHolder extends RecyclerView.ViewHolder{

    private final TextView notification_text;
    private final TextView notification_type;
    private final ImageView notification_sender_icon;

    public NotificationHolder(@NonNull View itemView) {
        super(itemView);
        notification_text = itemView.findViewById(R.id.notification_text);
        notification_type = itemView.findViewById(R.id.notification_type);
        notification_sender_icon = itemView.findViewById(R.id.notification_sender_icon);
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
}
