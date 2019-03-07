package com.example.bookflow;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddBookActivity extends BasicActivity {

    private final int ADD_PHOTO_REQUEST_CODE = 0;
    private ImageView photoImageView;

    private final int RC_PHOTO_PICKER = 1;
    private final int RC_IMAGE_CAPTURE = 2;

    private static final String TAG = "AddBookActivity";

    // Firebase
    private FirebaseStorage mFirebaseStorage;
    private FirebaseDatabase mFirebaseDatabase;

    private StorageReference mBookPhotoStorageReference;
    private DatabaseReference mUserDatabaseReference;

    private Uri selectedPhotoUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        photoImageView = findViewById(R.id.imageView4);

        // initialize Firebase
        mFirebaseStorage = FirebaseStorage.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mBookPhotoStorageReference = mFirebaseStorage.getReference().child("book_photos");
        mUserDatabaseReference = mFirebaseDatabase.getReference().child("Books");
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
        if (requestCode == RC_PHOTO_PICKER || requestCode == RC_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Uri imgUri = data.getData();
                Log.d(TAG, imgUri.toString());
                final StorageReference imgRef = mBookPhotoStorageReference.child(imgUri.getLastPathSegment());

                imgRef.putFile(imgUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                selectedPhotoUri = uri;
                                Log.d(TAG, "selectedPhotoUri: " + selectedPhotoUri.toString());
                            }
                        });
                    }
                });

            }
        }
    }
}
