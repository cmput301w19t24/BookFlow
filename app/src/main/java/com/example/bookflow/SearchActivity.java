/**
 * author: Yuhan Ye
 * date: 2019/3/11
 * version: 1.0
 */
package com.example.bookflow;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookflow.Model.Book;
import com.example.bookflow.Model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Search page
 * User could search user, search book by author, title and isbn
 * There are also filters for book status so that user could search for books available
 */
public class SearchActivity extends BasicActivity {
    public static final String EXTRA_MESSAGE = "com.example.bookflow.MESSAGE";
    private EditText search_Text;
    private RadioGroup radioGroup;
    private RadioButton checkAvailableButton, checkAcceptedButton, checkRequestedButton, checkBorrowedButton, selectedButton;
    private DatabaseReference mDatabase;
    private Spinner spinner;
    private RecyclerView recyclerView;
    FirebaseRecyclerAdapter<User, UserViewHolder> firebaseUserRecyclerAdapter;
    FirebaseRecyclerAdapter<Book, BookViewHolder> firebaseBookRecyclerAdapter;
    private ArrayList<Book> books;
    private ArrayList<Book> filtered_books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setAlpha(.5f);
        checkAcceptedButton = findViewById(R.id.checkAccepted);
        checkAvailableButton = findViewById(R.id.checkAvailable);
        checkRequestedButton = findViewById(R.id.checkRequested);
        checkBorrowedButton = findViewById(R.id.checkBorrowed);
        recyclerView = findViewById(R.id.searchViewList);
        checkBorrowedButton.setClickable(false);
        checkAvailableButton.setClickable(false);
        checkRequestedButton.setClickable(false);
        checkAcceptedButton.setClickable(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference();
        search_Text = findViewById(R.id.searchText);
        spinner = findViewById(R.id.spinner);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    radioGroup.setAlpha(.5f);
                    checkBorrowedButton.setClickable(false);
                    checkAvailableButton.setClickable(false);
                    checkRequestedButton.setClickable(false);
                    checkAcceptedButton.setClickable(false);
                } else {
                    radioGroup.setAlpha(1.0f);
                    checkBorrowedButton.setClickable(true);
                    checkAvailableButton.setClickable(true);
                    checkRequestedButton.setClickable(true);
                    checkAcceptedButton.setClickable(true);
                    checkAvailableButton.toggle();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });
        books = new ArrayList<Book>();
        filtered_books = new ArrayList<Book>();
        mDatabase.child("Books").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Book book = (Book) dataSnapshot.getValue(Book.class);
                books.add(book);
//                Log.i("booklist", "add book name = " + book.getTitle());
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
        });


    }

    public class LinearAdapter extends RecyclerView.Adapter <LinearAdapter.LinearViewHolder>{
        //context
        private Context mContext;
        public LinearAdapter(Context context){
            this.mContext=context;
       ;
        }

        public LinearAdapter(SearchActivity context) {
            this.mContext=context;

        }

        @Override
        public LinearAdapter.LinearViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.searchitem,parent,false));
        }


        @Override
        public void onBindViewHolder(LinearAdapter.LinearViewHolder holder, int position) {
            holder.setData(mContext,position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }


        @Override
        public int getItemCount() {
            return filtered_books.size();
        }


        class LinearViewHolder extends RecyclerView.ViewHolder{
            private TextView title;
            private TextView isbn;
            private TextView author;
            private ImageView photo;

            public LinearViewHolder(View itemView){
                super(itemView);
                title = itemView.findViewById(R.id.searchDetail1);
                isbn = itemView.findViewById(R.id.searchDetail2);
                author = itemView.findViewById(R.id.searchDetail3);
                photo = itemView.findViewById(R.id.searchItemImage);
            }
            public void setData(Context ctx,int i){
                title.setText(filtered_books.get(i).getTitle());
                isbn.setText(filtered_books.get(i).getIsbn());
                author.setText(filtered_books.get(i).getAuthor());
                Glide.with(ctx).load(filtered_books.get(i).getPhotoUri()).into(photo);
            }

        }
    }



    /**
     * User view holder class for search user
     */
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        View mView;

        /**
         * Constructor for view holder
         *
         * @param itemView
         */
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        /**
         * get view element and set details
         *
         * @param ctx             current context
         * @param userName
         * @param userEmail
         * @param userPhoneNumber
         * @param userImage
         */
        public void setDetails(Context ctx, String userName, String userEmail, String userPhoneNumber, String userImage) {
            TextView user_name = (TextView) mView.findViewById(R.id.searchDetail1);
            TextView user_email = (TextView) mView.findViewById(R.id.searchDetail2);
            TextView user_phoneNumber = (TextView) mView.findViewById(R.id.searchDetail3);
            ImageView user_image = (ImageView) mView.findViewById(R.id.searchItemImage);
            user_name.setText(userName);
            user_email.setText(userEmail);
            user_phoneNumber.setText(userPhoneNumber);
            Glide.with(ctx).load(userImage).into(user_image);
        }
    }

    /**
     * search User function
     *
     * @param searchText input keyword of User to search
     */
    public void searchUser(String searchText) {
        Toast.makeText(SearchActivity.this, "Started Search", Toast.LENGTH_LONG).show();
        Query firebaseUserSearchQuery = mDatabase.child("Users").orderByChild("username").startAt(searchText).endAt(searchText + "\uf8ff");

        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(firebaseUserSearchQuery, User.class).build();
        firebaseUserRecyclerAdapter =
                new FirebaseRecyclerAdapter<User, UserViewHolder>(options) {
                    @NonNull
                    @Override
                    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.searchitem, viewGroup, false);
                        return new UserViewHolder(view);
                    }

                    /**
                     * bind content to view holder
                     * @param holder user view holder
                     * @param position position in the view list
                     * @param model User model
                     */
                    @Override
                    protected void onBindViewHolder(@NonNull UserViewHolder holder, final int position, @NonNull User model) {
                        holder.setDetails(getApplicationContext(), model.getUsername(), model.getEmail(), model.getPhoneNumber(), model.getImageurl());
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            /**
                             * item click on method
                             * jump to user profile page
                             * @param v
                             */
                            @Override
                            public void onClick(View v) {
                                String user_id = getRef(position).getKey();
                                Log.i("userid", user_id);
                                Intent intent = new Intent(SearchActivity.this, UserProfileActivity.class);
                                intent.putExtra(EXTRA_MESSAGE, user_id);
                                startActivity(intent);
                            }
                        });
                    }

                };
        recyclerView.setAdapter(firebaseUserRecyclerAdapter);
        try {
            firebaseBookRecyclerAdapter.stopListening();
        } catch (Exception e) {

        }

        firebaseUserRecyclerAdapter.startListening();
    }

    /**
     * Book view holder class
     */
    public static class BookViewHolder extends RecyclerView.ViewHolder {
        View mView;

        /**
         * basic constructor
         *
         * @param itemView
         */
        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        /**
         * get view element and set details
         *
         * @param ctx       current context
         * @param isbn
         * @param title
         * @param author
         * @param bookImage
         */
        public void setDetails(Context ctx, String isbn, String title, String author, String bookImage) {
            TextView book_isbn = (TextView) mView.findViewById(R.id.searchDetail1);
            TextView book_title = (TextView) mView.findViewById(R.id.searchDetail2);
            TextView book_author = (TextView) mView.findViewById(R.id.searchDetail3);
            ImageView book_image = (ImageView) mView.findViewById(R.id.searchItemImage);
            book_isbn.setText(isbn);
            book_title.setText(title);
            book_author.setText(author);
            Glide.with(ctx).load(bookImage).into(book_image);
        }
    }

    /**
     * search book function
     *
     * @param searchText keyword input of User to search
     * @param constriant option constraint for search
     */
    public void searchBook(String searchText, String constriant, String status) {
        Toast.makeText(SearchActivity.this, "Started Search", Toast.LENGTH_LONG).show();
        Log.i("bookstatus", status);
        Query firebaseSearchQuery = mDatabase.child("Books").orderByChild(constriant).startAt(searchText).endAt(searchText + "\uf8ff");
        FirebaseRecyclerOptions<Book> options = new FirebaseRecyclerOptions.Builder<Book>()
                .setQuery(firebaseSearchQuery, Book.class).build();
        firebaseBookRecyclerAdapter =
                new FirebaseRecyclerAdapter<Book, BookViewHolder>(options) {
                    @NonNull
                    @Override
                    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.searchitem, viewGroup, false);
                        return new BookViewHolder(view);
                    }

                    /**
                     * bind content to book view holder
                     * @param holder book view holder
                     * @param position position in view list
                     * @param model book model class
                     */
                    @Override
                    protected void onBindViewHolder(@NonNull BookViewHolder holder, final int position, @NonNull Book model) {
                        holder.setDetails(getApplicationContext(), model.getIsbn(), model.getTitle(), model.getAuthor(), model.getPhotoUri());
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            /**
                             * item click on method
                             * jump to book detail page
                             * @param v
                             */
                            @Override
                            public void onClick(View v) {
                                String book_id = getRef(position).getKey();
                                Intent intent = new Intent(SearchActivity.this, BookDetailActivity.class);
                                intent.putExtra(BookDetailActivity.INTENT_EXTRA, book_id);
                                startActivity(intent);
                            }
                        });
                    }

                };
        recyclerView.setAdapter(firebaseBookRecyclerAdapter);
        try {
            firebaseUserRecyclerAdapter.stopListening();
        } catch (Exception e) {

        }
        firebaseBookRecyclerAdapter.startListening();
    }


    private void searchBook(String searchText,String status){
        try {
            firebaseUserRecyclerAdapter.stopListening();
        } catch (Exception e) {

        }
        filterBooks(searchText,status);
        Log.i("filter",filtered_books.toString());
        LinearAdapter madapter = new LinearAdapter(this);
        recyclerView.setAdapter(madapter);

    }

    /**
     * search button "GO" click on method
     * get user input and filter options
     *
     * @param v
     */
    public void search(View v) {
        String searchOption = spinner.getSelectedItem().toString();
        String searchText = search_Text.getText().toString();
        String constraint = "title";
        String status;
        switch (searchOption) {
            case "search user":
                searchUser(searchText);
                break;
            case "search by book name":
                selectedButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                status = selectedButton.getText().toString().toUpperCase();
                constraint = "title";
                searchBook(searchText, status);
                break;
            case "search by book author":
                selectedButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                status = selectedButton.getText().toString().toUpperCase();
                constraint = "author";
                searchBook(searchText, status);
                break;
            case "search by book ISBN":
                selectedButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                status = selectedButton.getText().toString().toUpperCase();
                constraint = "isbn";
                searchBook(searchText, status);
        }

    }

    private void filterBooks(String searchText, String status){
        String[] words = searchText.split("\\s");
        filtered_books.clear();
        for(Book book:books){
            filtered_books.add(book);
        }
        for(Book book:books) {
            if(!(book.getStatus().toString().equals(status))){
                filtered_books.remove(book);
            }
        }

        for(Book book:filtered_books){
            Log.i("status",book.getStatus().toString());
        }
    }
}
