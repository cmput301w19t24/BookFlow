package com.example.bookflow;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookflow.Model.Book;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;



public class AddBookActivity extends BasicActivity {

    private final int ADD_PHOTO_REQUEST_CODE = 0;


    private final int RC_PHOTO_PICKER = 1;
    private final int RC_IMAGE_CAPTURE = 2;

    private static final String TAG = "AddBookActivity";

    // Firebase
    private FirebaseStorage mFirebaseStorage;
    private FirebaseDatabase mFirebaseDatabase;

    private StorageReference mBookPhotoStorageReference;
    private DatabaseReference mUserDatabaseReference;

    private Uri mSelectedPhotoUri;

    // UI
    private RelativeLayout mLoadingPanel;
    private ImageView mPhotoImageView;
    private ImageView mSaveImageView;
    private EditText mBookTitleEditText;
    private EditText mAuthorEditText;
    private EditText mIsbnEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        mPhotoImageView = findViewById(R.id.add_book_image_iv);
        mLoadingPanel = findViewById(R.id.add_book_loading_panel);
        mBookTitleEditText = findViewById(R.id.book_title_et);
        mAuthorEditText = findViewById(R.id.add_book_author_name_et);
        mIsbnEditText = findViewById(R.id.add_book_isbn_et);
        mSaveImageView = findViewById(R.id.add_book_save_iv);

        mSaveImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check all required field and save the data
                Book mybook = extractBookInfo();
                if (mybook != null) {
                    saveBook(mybook);
                }
            }
        });

        mLoadingPanel.setVisibility(View.GONE);

        // initialize Firebase
        mFirebaseStorage = FirebaseStorage.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mBookPhotoStorageReference = mFirebaseStorage.getReference().child("book_photos");
        mUserDatabaseReference = mFirebaseDatabase.getReference().child("Books");
    }

    /**
     * save the book information to firebase
     */
    private void saveBook(final Book mybook) {
        // first check if the user has uploaded a photo.
        // if so, upload the photo to FirebaseStorage, and
        // add the URI to Book
        if (mSelectedPhotoUri != null) {
            final StorageReference photoRef = mBookPhotoStorageReference.child(mSelectedPhotoUri.getLastPathSegment());
            UploadTask uploadTask = photoRef.putFile(mSelectedPhotoUri);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return photoRef.getDownloadUrl();
                }
            }).continueWithTask(new Continuation<Uri, Task<Void>>() {
                @Override
                public Task<Void> then(@NonNull Task<Uri> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    Uri downloadUri = task.getResult();
                    mybook.addPhoto();
                }
            });

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                        }
                    })
                }
            });

            mLoadingPanel.setVisibility(View.VISIBLE);
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

        if (bookTitle.equals("") || author.equals("") || isbn.equals("")) {
            Toast.makeText(this, "Please enter required fields", Toast.LENGTH_SHORT).show();
            return null;
        }

        return new Book(bookTitle, author, isbn);
    }

    public void clickAddPhotoImage(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.pick_a_photo)
                .setItems(R.array.colors_array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {

                            case 0: {
                                // pick from album
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.setType("image/jpeg");
                                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
                            }
                            break;
                            case 1: {
                                // take a photo
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (intent.resolveActivity(getPackageManager()) != null) {
                                    startActivityForResult(intent, RC_IMAGE_CAPTURE);
                                }

                            }
                            break;
                        }
                    }
                })
                .show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == RC_PHOTO_PICKER) {

                Uri imgUri = data.getData();
                Log.d(TAG, imgUri.toString());

                // display photo on the image view
                Glide.with(mPhotoImageView.getContext())
                        .load(imgUri)
                        .into(mPhotoImageView);

                mSelectedPhotoUri = imgUri;

                final StorageReference imgRef = mBookPhotoStorageReference.child(imgUri.getLastPathSegment());

                imgRef.putFile(imgUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                onUploadPhotoSuccess(uri);
                            }
                        });
                    }
                });
            } else if (requestCode == RC_IMAGE_CAPTURE) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");

                // display photo on the image view
                mPhotoImageView.setImageBitmap(imageBitmap);


                // create a temporary file
                File tempDir= Environment.getExternalStorageDirectory();
                tempDir=new File(tempDir.getAbsolutePath()+"/.temp/");
                tempDir.mkdir();
                File tempFile = null;
                try {
                    tempFile = File.createTempFile("temp", ".jpg", tempDir);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

                // store the Bitmap to a temporary
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] ba = baos.toByteArray();

                try {
                    FileOutputStream fos = new FileOutputStream(tempFile);
                    fos.write(ba);
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

                mSelectedPhotoUri = Uri.fromFile(tempFile);
            }
        }
    }

    /**
     * onUploadPhotoSuccess
     * @param uri
     */
    private void onUploadPhotoSuccess(Uri uri) {
        mSelectedPhotoUri = uri;
        mLoadingPanel.setVisibility(View.GONE);
    }

}
