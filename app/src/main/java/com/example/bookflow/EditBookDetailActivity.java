package com.example.bookflow;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookflow.Model.Book;
import com.example.bookflow.Util.FirebaseIO;
import com.example.bookflow.Util.PhotoUtility;
import com.example.bookflow.Util.ScanUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class EditBookDetailActivity extends BasicActivity {

    private static final String TAG = "EditBookDetailActivity";

    private FirebaseIO mFirebaseIO;

    private Uri mSelectedPhotoUri;

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

        mSaveImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check all required field and save the data
                Book mybook = extractBookInfo();
                if (mybook != null) {
                    mProgressbar.setVisibility(View.VISIBLE);

                    mFirebaseIO.saveBook(mybook, mSelectedPhotoUri, new OnCompleteListener<Void>() {
                        // upon completion, hide the loading panel and prompt user
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), getString(R.string.edit_book_success), Toast.LENGTH_LONG).show();
                            } else {
                                task.getException().printStackTrace();
                                Toast.makeText(getApplicationContext(), getString(R.string.edit_book_error), Toast.LENGTH_LONG).show();
                            }
                            mProgressbar.setVisibility(View.GONE);

                            finish();
                        }
                    });
                }
            }
        });


        mScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditBookDetailActivity.this, ScanActivity.class);
                startActivityForResult(intent, ScanUtility.RC_SCAN);
            }
        });

        mProgressbar.setVisibility(View.GONE);

        // ask for permission
        PhotoUtility.checkCameraPermission(this);
        PhotoUtility.checkWriteExternalPermission(this);
    }

    /**
     * handles the return of other activities, including "choose from album",
     * "take a photo", and "scan"
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == PhotoUtility.RC_PHOTO_PICKER) {

                Uri imgUri = data.getData();
                Log.d(TAG, imgUri.toString());

                // display photo on the image view
                Glide.with(mPhotoImageView.getContext())
                        .load(imgUri)
                        .into(mPhotoImageView);

                mSelectedPhotoUri = imgUri;

            } else if (requestCode == PhotoUtility.RC_IMAGE_CAPTURE) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");

                Uri imgUri = PhotoUtility.bitmapToUri(this, imageBitmap);

                Glide.with(mPhotoImageView.getContext())
                        .load(imageBitmap)
                        .into(mPhotoImageView);

                mSelectedPhotoUri = imgUri;

            } else if (requestCode == ScanUtility.RC_SCAN) {
                String isbn = data.getStringExtra(ScanActivity.SCAN_RESULT);
                if (isbn != null) {
                    Toast.makeText(this, "ISBN: " + isbn, Toast.LENGTH_LONG).show();
                    mIsbnEditText.setText(isbn);
                }
            }
        }
    }

    /**
     * extract book information from editTexts
     * @return a Book object
     */
    private Book extractBookInfo() {

        String bookTitle = mBookTitleEditText.getText().toString().trim();
        String author = mAuthorEditText.getText().toString().trim();
        String isbn = mIsbnEditText.getText().toString().trim();
        String detail = mDetailEditText.getText().toString().trim();

        if (bookTitle.equals("") || author.equals("") || isbn.equals("")) {
            Toast.makeText(this, getString(R.string.add_book_invalid_info), Toast.LENGTH_SHORT).show();
            return null;
        }

        Book book = new Book(bookTitle, author, isbn);
        book.setDescription(detail);

        return book;
    }


}
