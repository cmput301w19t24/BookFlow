package com.example.bookflow;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

/**
 * Activity responsible for editing the book detail.
 * This activity should be started by other activity
 * with an intent containing the book id which can be
 * retrieved by key INTENT_KEY (i.e. "bookid").
 */
public class EditBookDetailActivity extends BasicActivity {

    public static final String INTENT_KEY = "bookid";

    private static final String TAG = "EditBookDetailActivity";

    private FirebaseIO mFirebaseIO;

    private Uri mSelectedPhotoUri;

    private ImageView mPhotoImageView;
    private ImageView mSaveImageView;
    private EditText mBookTitleEditText;
    private EditText mAuthorEditText;
    private EditText mIsbnEditText;
    private EditText mDescriptionEditText;
    private ProgressBar mProgressbar;
    private Button mScanButton;

    private String mBookId;

    /**
     * initialize UI elements, firebase and register listeners
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_edit_book_detail);

        /* initialize Firebase */
        mFirebaseIO = FirebaseIO.getInstance();

        /* initialize UI elements */
        mPhotoImageView = findViewById(R.id.edit_book_image_iv);
        mSaveImageView = findViewById(R.id.edit_book_save_iv);
        mBookTitleEditText = findViewById(R.id.edit_book_title_et);
        mAuthorEditText = findViewById(R.id.edit_book_author_name_et);
        mIsbnEditText = findViewById(R.id.edit_book_isbn_et);
        mDescriptionEditText = findViewById(R.id.edit_book_description_it);
        mProgressbar = findViewById(R.id.edit_book_progress_bar);
        mScanButton = findViewById(R.id.edit_book_scan_button);

        mProgressbar.setVisibility(View.GONE);


        /* register listeners */
        mPhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(EditBookDetailActivity.this);
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
                        /**
                         * upon completion, hide the loading panel and prompt user
                         * @param task
                         */
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


        /* populate existing book info to edit views */
        mBookId = getIntent().getStringExtra(INTENT_KEY);
        mFirebaseIO.getBook(mBookId, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Book thisBook = dataSnapshot.getValue(Book.class);
                propagateBookToView(thisBook);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // ask for permission
        PhotoUtility.checkCameraPermission(this);
        PhotoUtility.checkWriteExternalPermission(this);
    }

    /**
     * propagate the existing book info to the text views
     * @param book the book to be propagated
     */
    private void propagateBookToView(Book book) {
        if (book == null) {
            Log.e(TAG, "book is null!");
            return;
        }

        String title = book.getTitle();
        if (title != null) {
            mBookTitleEditText.setText(title);
        }

        String author = book.getAuthor();
        if (author != null) {
            mAuthorEditText.setText(author);
        }

        String isbn = book.getIsbn();
        if (isbn != null) {
            mIsbnEditText.setText(isbn);
        }

        String description = book.getDescription();
        if (description != null) {
            mDescriptionEditText.setText(description);
        }

        // populate book photo
        Glide.with(mPhotoImageView.getContext())
                .load(book.getPhotoUri())
                .into(mPhotoImageView);

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
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Uri imgUri = result.getUri();
            mSelectedPhotoUri = imgUri;
            // display photo on the image view
            Glide.with(mPhotoImageView.getContext())
                    .load(imgUri)
                    .into(mPhotoImageView);

        } else if (requestCode == ScanUtility.RC_SCAN) {
            String isbn = data.getStringExtra(ScanActivity.SCAN_RESULT);
            if (isbn != null) {
                Toast.makeText(this, "ISBN: " + isbn, Toast.LENGTH_LONG).show();
                mIsbnEditText.setText(isbn);
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
        String detail = mDescriptionEditText.getText().toString().trim();

        if (bookTitle.equals("") || author.equals("") || isbn.equals("")) {
            Toast.makeText(this, getString(R.string.add_book_invalid_info), Toast.LENGTH_SHORT).show();
            return null;
        }

        Book book = new Book(bookTitle, author, isbn);
        book.setDescription(detail);

        return book;
    }


}
