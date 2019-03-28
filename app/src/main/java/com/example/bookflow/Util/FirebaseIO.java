package com.example.bookflow.Util;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.bookflow.Model.Book;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * A utility class that encapsulates the interaction with
 * the Firebase Storage and Firebase Realtime Database.
 *
 * This is a singleton class.
 */
public class FirebaseIO {

    private static final String TAG = "FirebaseIO";

    private static FirebaseIO INSTANCE = null;

    /**
     * Firebase
     */
    private FirebaseStorage mFirebaseStorage;
    private FirebaseDatabase mFirebaseDatabase;

    private StorageReference mBookPhotoStorageReference;
    private DatabaseReference mBookDatabaseReference;


    /**
     * Get the unique instance of the FirebaseIO object.
     * @return
     */
    public static FirebaseIO getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FirebaseIO();
        }

        return INSTANCE;
    }

    /**
     * Initialize Firebase storage and database, setup proper
     * references.
     */
    private FirebaseIO() {

        mFirebaseStorage = FirebaseStorage.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mBookPhotoStorageReference = mFirebaseStorage.getReference().child("book_photos");
        mBookDatabaseReference = mFirebaseDatabase.getReference().child("Books");
    }

    /**
     * get book async. User needs to provide a ViwEventListener that handles
     * logic when book is available.
     * @param bookid
     * @param listener
     */
    public void getBook(String bookid, ValueEventListener listener) {
        mBookDatabaseReference
                .child(bookid)
                .addValueEventListener(listener);
    }


    /**
     * save the book information to firebase. This method fires a series of async tasks
     * first check if the user has uploaded a photo. if so, upload the photo to
     * Firebase Storage, and add the URI to Book
     *
     * @param mybook the book object to be saved
     */
    public void saveBook(final Book mybook, Uri localUri, OnCompleteListener<Void> listener) {
        if (localUri != null) {

            Log.d(TAG, "mSelectedPhotoUri: " + localUri.toString());
            Log.d(TAG, "mSelectedPhotoUri.getLast...: " + localUri.getLastPathSegment());
            Log.i(TAG, "mybook.getBookId = " + mybook.getBookId());

            final StorageReference photoRef = mBookPhotoStorageReference.child(localUri.getLastPathSegment());
            UploadTask uploadTask = photoRef.putFile(localUri);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                // upon completion, start to retrieve Uri
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return photoRef.getDownloadUrl();
                }
            }).continueWithTask(new Continuation<Uri, Task<Void>>() {
                // upon completion, start to upload to database
                @Override
                public Task<Void> then(@NonNull Task<Uri> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // TODO: delete the original image first
                    Uri downloadUri = task.getResult();
                    Log.d(TAG, "downloadUri = " + downloadUri.toString());
                    mybook.setPhotoUri(downloadUri.toString());

                    String myuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    mybook.setOwnerId(myuid);

                    DatabaseReference bookRef = null;
                    if (mybook.getBookId() != null) {
                        // this is an existed book
                        bookRef = mBookDatabaseReference.child(mybook.getBookId());

                    } else {
                        // this is a new book
                        bookRef = mBookDatabaseReference.push();
                        String bookId = bookRef.getKey();
                        mybook.setBookId(bookId);
                    }

                    return bookRef.setValue(mybook);
                }
            }).addOnCompleteListener(listener);

        } else {
            // no photo uploaded, so just upload to the database
            String myuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            mybook.setOwnerId(myuid);

            DatabaseReference bookRef = null;
            if (mybook.getBookId() != null) {
                // this is an existed book
                bookRef = mBookDatabaseReference.child(mybook.getBookId());
            } else {
                // this is a new book
                bookRef = mBookDatabaseReference.push();
                String bookId = bookRef.getKey();
                mybook.setBookId(bookId);
            }

            bookRef.setValue(mybook)
                    .addOnCompleteListener(listener);
        }
    }


    /**
     * update the book information to firebase. This method fires a series of async tasks
     * first check if the user has uploaded a photo. if so, upload the photo to
     * Firebase Storage, and add the URI to Book
     *
     * @param mybook the book object to be updated
     */
    public void updateBook(final Book mybook, Uri localUri, OnCompleteListener<Void> listener) {
        if (localUri != null) {

            Log.d(TAG, "mSelectedPhotoUri: " + localUri.toString());
            Log.d(TAG, "mSelectedPhotoUri.getLast...: " + localUri.getLastPathSegment());
            Log.i(TAG, "mybook.getBookId = " + mybook.getBookId());

            final StorageReference photoRef = mBookPhotoStorageReference.child(localUri.getLastPathSegment());
            UploadTask uploadTask = photoRef.putFile(localUri);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                // upon completion, start to retrieve Uri
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return photoRef.getDownloadUrl();
                }
            }).continueWithTask(new Continuation<Uri, Task<Void>>() {
                // upon completion, start to upload to database
                @Override
                public Task<Void> then(@NonNull Task<Uri> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // TODO: delete the original image first
                    Uri downloadUri = task.getResult();
                    Log.d(TAG, "downloadUri = " + downloadUri.toString());
                    mybook.setPhotoUri(downloadUri.toString());

                    DatabaseReference bookRef = null;
                    if (mybook.getBookId() != null) {
                        // this is an existed book
                        bookRef = mBookDatabaseReference.child(mybook.getBookId());
                    }
                    return bookRef.setValue(mybook);
                }
            }).addOnCompleteListener(listener);

        } else {
            // no photo uploaded

            DatabaseReference bookRef = null;
            if (mybook.getBookId() != null) {
                // this is an existed book
                bookRef = mBookDatabaseReference.child(mybook.getBookId());
            }

            bookRef.setValue(mybook)
                    .addOnCompleteListener(listener);
        }
    }

}
