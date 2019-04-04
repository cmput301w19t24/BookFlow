package com.example.bookflow;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.bookflow.Model.InAppNotif;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class InitActivity extends AppCompatActivity {

    static private InAppNotif notif = new InAppNotif();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
    }

    static public InAppNotif getNotif() {
        return notif;
    }

    static void pushData(String user_id) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("InAppNotif").child(user_id);
        dbRef.setValue(notif);
    }

    static void popData(String user_id) {
        Query query = FirebaseDatabase.getInstance().getReference().child("InAppNotif").child(user_id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notif = dataSnapshot.getValue(InAppNotif.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
