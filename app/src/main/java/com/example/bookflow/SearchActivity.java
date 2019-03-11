package com.example.bookflow;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookflow.Model.Book;
import com.example.bookflow.Model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class SearchActivity extends BasicActivity {
    public static final String EXTRA_MESSAGE = "com.example.bookflow.MESSAGE";
    private EditText search_Text;
    private CheckBox checkAccepted;
    private CheckBox checkAvailable;
    private CheckBox checkRequested;
    private CheckBox checkBorrowed;
    private DatabaseReference mUserDatabase;
    private Spinner spinner;
    private RecyclerView recyclerView;
    FirebaseRecyclerAdapter<User,UserViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        checkAccepted = findViewById(R.id.checkAccepted);
        checkAvailable = findViewById(R.id.checkAvailable);
        checkRequested = findViewById(R.id.checkRequested);
        checkBorrowed = findViewById(R.id.checkBorrowed);
        checkAccepted.setVisibility(View.INVISIBLE);
        checkAvailable.setVisibility(View.INVISIBLE);
        checkRequested.setVisibility(View.INVISIBLE);
        checkBorrowed.setVisibility(View.INVISIBLE);
        recyclerView = findViewById(R.id.searchViewList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUserDatabase = FirebaseDatabase.getInstance().getReference();
        search_Text = findViewById(R.id.searchText);
        spinner = findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    checkAccepted.setVisibility(View.INVISIBLE);
                    checkAvailable.setVisibility(View.INVISIBLE);
                    checkRequested.setVisibility(View.INVISIBLE);
                    checkBorrowed.setVisibility(View.INVISIBLE);
                }
                else{
                    checkAccepted.setVisibility(View.VISIBLE);
                    checkAvailable.setVisibility(View.VISIBLE);
                    checkRequested.setVisibility(View.VISIBLE);
                    checkBorrowed.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

        boolean isCheckedAccepted = false;
        boolean isCheckedAvailable = false;
        boolean isCheckedRequested = false;
        boolean isCheckedBorrowed = false;
    }

    // User Holder class
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

    // search user function
    public void searchUser(String searchText){
        Toast.makeText(SearchActivity.this, "Started Search", Toast.LENGTH_LONG).show();
        Query firebaseSearchQuery = mUserDatabase.child("Users").orderByChild("username").startAt(searchText).endAt(searchText + "\uf8ff");

        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(firebaseSearchQuery,User.class).build();
        firebaseRecyclerAdapter=
        new FirebaseRecyclerAdapter<User, UserViewHolder>(options) {
            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.searchitem,viewGroup,false);
                return new UserViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, final int position, @NonNull User model) {
                holder.setDetails(getApplicationContext(),model.getUsername(),model.getEmail(),model.getPhoneNumber(),model.getImageurl());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String user_id = getRef(position).getKey();
                        Log.i("userid",user_id);
                        Intent intent = new Intent(SearchActivity.this,UserProfileActivity.class);
                        intent.putExtra(EXTRA_MESSAGE,user_id);
                        startActivity(intent);
                    }
                });
            }

        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    // BookName Holder class
    public static class BookTitleViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public BookTitleViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDetails(Context ctx, String isbn, String title, String author, String bookImage){
            TextView book_isbn = (TextView)mView.findViewById(R.id.searchDetail1);
            TextView book_title = (TextView)mView.findViewById(R.id.searchDetail2);
            TextView book_author = (TextView)mView.findViewById(R.id.searchDetail3);
            ImageView book_image = (ImageView)mView.findViewById(R.id.searchItemImage);
            book_isbn.setText(isbn);
            book_title.setText(title);
            book_author.setText(author);
            Glide.with(ctx).load(bookImage).into(book_image);
        }
    }

//    public void searchBook(String searchText,String constriant){
//        Toast.makeText(SearchActivity.this, "Started Search", Toast.LENGTH_LONG).show();
//        Query firebaseSearchQuery = mUserDatabase.child("Books").orderByChild(constriant).startAt(searchText).endAt(searchText + "\uf8ff");
//        FirebaseRecyclerAdapter<Book,BookTitleViewHolder> firebaseRecyclerAdapter=
//                new FirebaseRecyclerAdapter<Book,BookTitleViewHolder>(
//                        Book.class,
//                        R.layout.searchitem,
//                        BookTitleViewHolder.class,
//                        firebaseSearchQuery
//                ) {
//                    @NonNull
//                    @Override
//                    public BookTitleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//                        return null;
//                    }
//
//                    @Override
//                    protected void onBindViewHolder(@NonNull BookTitleViewHolder holder, final int position, @NonNull Book model) {
//                        holder.setDetails(getApplicationContext(),model.getIsbn(),model.getTitle(),model.getAuthor(),model.getPhotoUri());
//                        holder.itemView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                String book_id = getRef(position).getKey();
//                                Intent intent = new Intent(SearchActivity.this,BookDetailActivity.class);
//                                intent.putExtra("book_id",book_id);
//                                startActivity(intent);
//                            }
//                        });
//                    }
//
//                };
//        recyclerView.setAdapter(firebaseRecyclerAdapter);
//    }




    public void searchByAuthor(String searchText){}
    public void searchByISBN(String searchText){}


    public void search(View v){
        String searchOption = spinner.getSelectedItem().toString();
        String searchText = search_Text.getText().toString();
        String constraint;
        switch (searchOption){
            case "search user":
                searchUser(searchText);
                break;
            case "search by book name":
                constraint = "title";
                //searchBook(searchText,constraint);
                break;
            case "search by book author":
                constraint = "author";
                //searchByAuthor(searchText);
                break;
            case "search by book ISBN":
                constraint = "isbn";
                //searchByISBN(searchText);
        }

    }
}
