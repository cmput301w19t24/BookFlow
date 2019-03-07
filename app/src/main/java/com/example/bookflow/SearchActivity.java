package com.example.bookflow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;

public class SearchActivity extends BasicActivity {

    private EditText searchText;
    private CheckBox checkAccepted;
    private CheckBox checkAvailable;
    private CheckBox checkRequested;
    private CheckBox checkBorrowed;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private Spinner spinner;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchText = findViewById(R.id.searchText);
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        spinner = findViewById(R.id.spinner);
        checkAccepted = findViewById(R.id.checkAccepted);
        checkAvailable = findViewById(R.id.checkAvailable);
        checkRequested = findViewById(R.id.checkRequested);
        checkBorrowed = findViewById(R.id.checkBorrowed);

        boolean isCheckedAccepted = false;
        boolean isCheckedAvailable = false;
        boolean isCheckedRequested = false;
        boolean isCheckedBorrowed = false;


    }

    public void searchUser(){

    }

    public void searchByBookName(){}
    public void searchByAuthor(){}
    public void searchByISBN(){}

    public void search(View v){
        String searchOption = spinner.getSelectedItem().toString();
        switch (searchOption){
            case "search user":
                searchUser();
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
