package com.example.bookflow;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class NotificationHolder extends RecyclerView.ViewHolder{

    private final TextView notification_text;

    public NotificationHolder(@NonNull View itemView) {
        super(itemView);
        notification_text = itemView.findViewById(R.id.notification_text);
    }

    public void setNotificationText(String s) {
        notification_text.setText(s);
    }
}
