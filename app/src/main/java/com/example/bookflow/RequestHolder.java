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

public class RequestHolder extends RecyclerView.ViewHolder{

    //private final TextView request_header; // don't need until accept implemented, currently hardcoded string
    private final TextView requester_name;
    private final ImageView requester_icon;
    private final TextView request_text;
    private final TextView request_item_book_title;
    FirebaseDatabase mDatabase;

    public RequestHolder(@NonNull View itemView) {
        super(itemView);
        request_item_book_title = itemView.findViewById(R.id.request_item_book_title);
        requester_name = itemView.findViewById(R.id.requester_name);
        requester_icon = itemView.findViewById(R.id.requester_icon);
        request_text = itemView.findViewById(R.id.request_text);
    }

//    public void setRequesterName(String s) {
//    }

    public void setBookTitle(String bookId) {
        final String myBookId = bookId;
        mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference bookRef = mDatabase.getReference("Books");

        ValueEventListener bookListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String bookTitle = dataSnapshot.child(myBookId).child("title").getValue().toString();
                request_item_book_title.setText(bookTitle);
                //Log.e("help", requesterName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("cancelled", databaseError.toException());
            }
        };
        bookRef.addListenerForSingleValueEvent(bookListener);

    }

    public void setRequesterIcon(StorageReference s) {
        Glide.with(itemView.getContext())
                .load(s).into(requester_icon);
    }

    public void setRequesterName(String requesterId) {
        final String reqId = requesterId;
        mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference userRef = mDatabase.getReference("Users");

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String requesterName = dataSnapshot.child(reqId).child("username").getValue().toString();
                requester_name.setText(requesterName);
                //request_text.setText("has requested");
                Log.e("help", requesterName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("cancelled", databaseError.toException());
            }
        };
        userRef.addListenerForSingleValueEvent(userListener);
    }

    public void setRequestText(String s) {
        request_text.setText(s);
    }

    public void setBookTitleForSentRequest(String bookId) {
        final String myBookId = bookId;
        mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference bookRef = mDatabase.getReference("Books");

        ValueEventListener bookListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String bookTitle = dataSnapshot.child(myBookId).child("title").getValue().toString();
                requester_name.setText("You have requested " + bookTitle);
                //Log.e("help", requesterName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("cancelled", databaseError.toException());
            }
        };
        bookRef.addListenerForSingleValueEvent(bookListener);
    }
    public void setOwnerNameForSentRequest(String ownerId){
        final String ownId = ownerId;
        mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference userRef = mDatabase.getReference("Users");

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String ownerName = dataSnapshot.child(ownId).child("username").getValue().toString();
                request_item_book_title.setText(ownerName);
                //request_text.setText("has requested");
                //Log.e("help", requesterName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("cancelled", databaseError.toException());
            }
        };
        userRef.addListenerForSingleValueEvent(userListener);
    }
}
