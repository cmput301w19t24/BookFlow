package com.example.bookflow;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

/**
 * Request holder class
 * handles displaying of details for items
 * in the request lists
 *
 */
public class RequestHolder extends RecyclerView.ViewHolder{

    private final ImageView request_item_icon;
    private final TextView request_status;
    private final TextView request_item_text;
    FirebaseDatabase mDatabase;

    public RequestHolder(@NonNull View itemView) {
        super(itemView);
        request_item_icon = itemView.findViewById(R.id.request_item_icon);
        request_item_text = itemView.findViewById(R.id.request_item_text);
        request_status = itemView.findViewById(R.id.request_header);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());

            }
        });
    }

    private RequestHolder.ClickListener mClickListener;

    //Interface to send callbacks...
    public interface ClickListener{
        public void onItemClick(View view, int position);
    }

    public void setOnClickListener(RequestHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }
    /**
     * set requester icon function
     * sets icon of requester
     * @param s storage reference
     */
    public void setRequesterIcon(StorageReference s) {
        Glide.with(itemView.getContext())
                .load(s).into(request_item_icon);
    }

    /**
     * set request item text
     * sets text information in a request item
     */
    public void setRequestItemText(String ownerId, String bookId, String requesterId, String type, String status) {
        final String ownId = ownerId;
        final String myBookId = bookId;
        final String reqId = requesterId;
        final String stat = status;
        mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference userRef = mDatabase.getReference("Users");

        final String myType = type;
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String ownerName = dataSnapshot.child(ownId).child("username").getValue().toString();
                Log.e("testing", ownerName);
                final String requesterName = dataSnapshot.child(reqId).child("username").getValue().toString();
                Log.e("testing", requesterName);
                DatabaseReference bookRef = mDatabase.getReference("Books");

                ValueEventListener bookListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String bookTitle;
                        try {
                            bookTitle = dataSnapshot.child(myBookId).child("title").getValue().toString();
                        }
                        catch (Exception e) {
                            bookTitle = "title not found";
                        }
                        String bookImageRef;
                        try {
                            bookImageRef = dataSnapshot.child(myBookId).child("photoUri").getValue().toString();
                        }
                        catch (Exception e) {
                            bookImageRef = "android.resource://com.example.bookflow/" + R.drawable.image_placeholder;
                        }
                        String outString = "";
                        if (myType.equals("received")) {
                            if(stat.equals("Pending")) {
                                outString = requesterName + " has requested \"" + bookTitle + "\".";
                            }
                            else if (stat.equals("Accepted")){
                                outString =  "You have accepted " +requesterName + "'s request for \"" + bookTitle + "\"";
                            }
                            else if (stat.equals("Cancelled")){
                                outString = requesterName + " has cancelled their request for \"" + bookTitle + "\"";
                            }
                        }
                        else if (myType.equals("sent")) {
                            if(stat.equals("Pending")) {
                                outString = "You have requested \"" + bookTitle + "\" from " + ownerName + ".";
                            }
                            else if (stat.equals("Rejected")){
                                outString = ownerName + " has rejected your request for \"" + bookTitle + "\"";
                            }
                            else if (stat.equals("Accepted")){
                                outString = ownerName + " has accepted your request for \"" + bookTitle + "\"";
                            }
                            Glide.with(itemView.getContext())
                                    .load(bookImageRef).into(request_item_icon);
                        }
                        request_item_text.setText(outString);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("cancelled", databaseError.toException());
                    }
                };
                bookRef.addListenerForSingleValueEvent(bookListener);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("cancelled", databaseError.toException());
            }
        };
        userRef.addListenerForSingleValueEvent(userListener);
    }
    /**
     * set status function
     * displays status information in request item on list
     */
    public void setStatus(String status) {
        request_status.setText(status);
        if (status.equals("Pending")) {
            request_status.setTextColor(Color.parseColor("#FFA500"));
        }
        else if (status.equals("Rejected")){
            request_status.setTextColor(Color.parseColor("#FF1A00"));
        }
        else if (status.equals("Cancelled")){
            request_status.setTextColor(Color.parseColor("#FF1A00"));
        }
        else if (status.equals("Accepted")){
            request_status.setTextColor(Color.parseColor("#7CFC00"));
        }
    }
}
