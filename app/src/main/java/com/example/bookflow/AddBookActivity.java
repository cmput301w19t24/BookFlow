package com.example.bookflow;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


/**
 * AddBookActivity
 * This activity handles add book logic, and can be
 * jumped by pressing the "add" button of the bottom bar.
 */
public class AddBookActivity extends AppCompatActivity {

    private static final String TAG = "AddBookActivity";

    private FirebaseIO mFirebaseIO;

    /**
     * Uri of the selected photo. If the photo is taken
     * with the camera, then Uri points to a
     * temporary location where the picture is stored.
     */
    private Uri mSelectedPhotoUri;

    /**
     *  UI components
     */
    private ImageView mPhotoImageView;
    private EditText mBookTitleEditText;
    private EditText mAuthorEditText;
    private EditText mIsbnEditText;
    private EditText mDescriptionEditText;
    private ProgressBar mProgressbar;
    private Button mScanButton;


    /**
     * create the activity, and registers listeners for
     * add photo button (image view), save button and scan button.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        mFirebaseIO = FirebaseIO.getInstance();

        mPhotoImageView = findViewById(R.id.add_book_image_iv);
        mBookTitleEditText = findViewById(R.id.add_book_title_et);
        mAuthorEditText = findViewById(R.id.add_book_author_name_et);
        mIsbnEditText = findViewById(R.id.add_book_isbn_et);
        mDescriptionEditText = findViewById(R.id.add_book_description_et);
        mProgressbar = findViewById(R.id.add_book_progress_bar);
        mScanButton = findViewById(R.id.add_book_scan_button);

        mPhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setFixAspectRatio(true)
                        .setAspectRatio(1, 1)
                        .start(AddBookActivity.this);
            }
        });

        mScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddBookActivity.this, ScanActivity.class);
                startActivityForResult(intent, ScanUtility.RC_SCAN);
            }
        });

        mProgressbar.setVisibility(View.GONE);

        // ask for permission
        PhotoUtility.checkCameraPermission(this);
        PhotoUtility.checkWriteExternalPermission(this);
    }

    /**
     * Inflate the menu; this adds items to the action bar if it is present.
     * @param menu the menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_book, menu);
        return true;
    }

    /**
     * buttons on the action bar are selected
     * @param item selected button
     * @return return true if success
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_book_save:
                Book mybook = extractBookInfo();
                if (mybook != null) {
                    mProgressbar.setVisibility(View.VISIBLE);

                    mFirebaseIO.saveBook(mybook, mSelectedPhotoUri, new OnCompleteListener<Void>() {
                        // upon completion, hide the loading panel and prompt user
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), getString(R.string.add_book_success), Toast.LENGTH_LONG).show();
                            } else {
                                task.getException().printStackTrace();
                                Toast.makeText(getApplicationContext(), getString(R.string.add_book_error), Toast.LENGTH_LONG).show();
                            }
                            mProgressbar.setVisibility(View.GONE);

                            finish();
                        }
                    });
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
        String description = mDescriptionEditText.getText().toString().trim();

        if (bookTitle.equals("") || author.equals("") || isbn.equals("")) {
            Toast.makeText(this, getString(R.string.add_book_invalid_info), Toast.LENGTH_SHORT).show();
            return null;
        }

        Book book = new Book(bookTitle, author, isbn);
        book.setDescription(description);

        return book;
    }

    /**
     * handles the return of other activities, including "choose from album",
     * "take a photo", and "scan"
     *
     * @param requestCode request code
     * @param resultCode result code
     * @param data data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Uri imgUri = result.getUri();
                mSelectedPhotoUri = imgUri;
                // display photo on the image view
                Glide.with(mPhotoImageView.getContext())
                        .load(imgUri)
                        .into(mPhotoImageView);
            }

        } else if (requestCode == ScanUtility.RC_SCAN) {
            if (resultCode == RESULT_OK) {
                String isbn = data.getStringExtra(ScanActivity.SCAN_RESULT);
                if (isbn != null) {
                    Toast.makeText(this, "ISBN: " + isbn, Toast.LENGTH_LONG).show();
                    mIsbnEditText.setText(isbn);
                }
            }
        }
    }
}
