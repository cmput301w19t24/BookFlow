package com.example.bookflow;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookflow.Model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class SearchActivity extends BasicActivity {
    private EditText search_Text;
    private CheckBox checkAccepted;
    private CheckBox checkAvailable;
    private CheckBox checkRequested;
    private CheckBox checkBorrowed;
    private DatabaseReference mUserDatabase;
    private Spinner spinner;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        search_Text = findViewById(R.id.searchText);
        spinner = findViewById(R.id.spinner);
        checkAccepted = findViewById(R.id.checkAccepted);
        checkAvailable = findViewById(R.id.checkAvailable);
        checkRequested = findViewById(R.id.checkRequested);
        checkBorrowed = findViewById(R.id.checkBorrowed);
        recyclerView = findViewById(R.id.searchViewList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        boolean isCheckedAccepted = false;
        boolean isCheckedAvailable = false;
        boolean isCheckedRequested = false;
        boolean isCheckedBorrowed = false;
    }

    public void searchUser(String searchText){
        Toast.makeText(SearchActivity.this, "Started Search", Toast.LENGTH_LONG).show();
        Query firebaseSearchQuery = mUserDatabase.orderByChild("username").startAt(searchText).endAt(searchText + "\uf8ff");
        FirebaseRecyclerAdapter<User,UserViewHolder> firebaseRecyclerAdapter=
        new FirebaseRecyclerAdapter<User, UserViewHolder>(
                User.class,
                R.layout.searchitem,
                UserViewHolder.class,
                firebaseSearchQuery
        ) {
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, User model, final int position) {
                viewHolder.setDetails(getApplicationContext(),model.getUsername(),model.getEmail(),model.getPhoneNumber(),model.getImageurl());
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String user_id = getRef(position).getKey();
                        Intent intent = new Intent(SearchActivity.this,UserProfileActivity.class);
                        intent.putExtra("user_id",user_id);
                        startActivity(intent);
                    }
                });
            }

        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    // View Holder class
    public static class UserViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDetails(Context ctx, String userName, String userEmail, String userPhoneNumber, String userImage){
            TextView user_name = (TextView)mView.findViewById(R.id.searchDetail1);
            TextView user_email = (TextView)mView.findViewById(R.id.searchDetail2);
            TextView user_phoneNumber = (TextView)mView.findViewById(R.id.searchDetail3);
            ImageView user_image = (ImageView)mView.findViewById(R.id.searchItemImage);
            user_name.setText(userName);
            user_email.setText(userEmail);
            user_phoneNumber.setText(userPhoneNumber);
            Glide.with(ctx).load(userImage).into(user_image);
        }
    }



    public void searchByBookName(){}
    public void searchByAuthor(){}
    public void searchByISBN(){}

    public void search(View v){
        String searchOption = spinner.getSelectedItem().toString();
        String searchText = search_Text.getText().toString();
        switch (searchOption){
            case "search user":
                searchUser(searchText);
                break;
            case "search by book name":
                searchByBookName();
                break;
            case "search by book author":
                searchByAuthor();
                break;
            case "search by book ISBN":
                searchByISBN();
        }

    }


}
