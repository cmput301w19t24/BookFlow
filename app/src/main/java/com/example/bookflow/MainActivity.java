package com.example.bookflow;
import com.bumptech.glide.Glide;
import com.example.bookflow.Model.Book;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainActivity extends BasicActivity {

    private static ListView myBookList;
    private static ListView myBorrowList;
    private FirebaseListAdapter adapterBook;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bookList();
        myBorrowList = (ListView) findViewById(R.id.myBorrowList);

    }

    private void bookList() {
        myBookList = (ListView) findViewById(R.id.myBookList);
        Query query = database.getReference().child("Books");
        FirebaseListOptions<Book> options = new FirebaseListOptions.Builder<Book>()
                .setLayout(R.layout.main_listitem)
                .setLifecycleOwner(MainActivity.this)
                .setQuery(query,Book.class).build();
        adapterBook = new FirebaseListAdapter<Book>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Book model, int position) {
                TextView mauthor = v.findViewById(R.id.iauthor);
                TextView mtitle = v.findViewById(R.id.ititle);
                ImageView mphoto = findViewById(R.id.iphoto);

                Book book = (Book) model;
                mauthor.setText(book.getAuthor());
                mtitle.setText(book.getTitle());
//                Glide.with(MainActivity.this).load(book.getPhotoUri()).into(mphoto);
//                mphoto.setImageURI();
            }

//            @Override
//            public int getCount() {
//                return 3;
//            }
        };
        myBookList.setAdapter(adapterBook);
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        adapterBook.startListening();
//
//        // TODO: delete after testing firebase
////        DatabaseReference myRef = database.getReference("Books");
////        myRef.child("Author").setValue("1", null);
//
//
//    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        adapterBook.stopListening();
//    }

    public void seeMoreBooks(View moreBookView) {
        Intent intent_booklist = new Intent(this, BookListActivity.class);
        startActivity(intent_booklist);
    }

    public void seeMoreBorrows(View moreBorrowView) {
        Intent intent_borrowlist = new Intent(this, BorrowListActivity.class);
        startActivity(intent_borrowlist);
    }
}
