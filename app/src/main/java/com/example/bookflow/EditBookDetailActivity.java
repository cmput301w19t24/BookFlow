package com.example.bookflow;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.bookflow.Model.Book;
import com.example.bookflow.Util.FirebaseIO;
import com.example.bookflow.Util.PhotoUtility;

public class EditBookDetailActivity extends BasicActivity {

    private FirebaseIO mFirebaseIO;

    private ImageView mPhotoImageView;
    private ImageView mSaveImageView;
    private EditText mBookTitleEditText;
    private EditText mAuthorEditText;
    private EditText mIsbnEditText;
    private EditText mDetailEditText;
    private ProgressBar mProgressbar;
    private Button mScanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_edit_book_detail);

        // Firebase
        mFirebaseIO = FirebaseIO.getInstance();

        // UI
        mPhotoImageView = findViewById(R.id.edit_book_image_iv);
        mSaveImageView = findViewById(R.id.edit_book_save_iv);
        mBookTitleEditText = findViewById(R.id.edit_book_title_et);
        mAuthorEditText = findViewById(R.id.edit_book_author_name_et);
        mIsbnEditText = findViewById(R.id.edit_book_isbn_et);
        mDetailEditText = findViewById(R.id.edit_book_detail_it);
        mProgressbar = findViewById(R.id.edit_book_progress_bar);
        mScanButton = findViewById(R.id.edit_book_scan_button);



        mPhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoUtility.showPickPhotoDialog(EditBookDetailActivity.this);
            }
        });

    }


}
