package com.example.bookflow;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

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

    // storage
    private Uri mCameraImgUri;

    // UI
    private RelativeLayout mLoadingPanel;
    private ImageView mPhotoImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        mPhotoImageView = findViewById(R.id.imageView4);
        mLoadingPanel = findViewById(R.id.loading_panel);
        mLoadingPanel.setVisibility(View.GONE);

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
        mLoadingPanel.setVisibility(View.VISIBLE);
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {

            Uri imgUri = data.getData();

            Log.d(TAG, imgUri.toString());
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


        } else if (requestCode == RC_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap selectedImg = (Bitmap) extras.get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            selectedImg.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] ba = baos.toByteArray();

            String uuid = UUID.randomUUID().toString();
            final StorageReference imgRef = mBookPhotoStorageReference.child(uuid);
            imgRef.putBytes(ba).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {

                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            onUploadPhotoSuccess(uri);
                        }
                    });
                }
            });
        }

    }

    /**
     * onUploadPhotoSuccess
     * @param uri
     */
    private void onUploadPhotoSuccess(Uri uri) {
        mSelectedPhotoUri = uri;
        mLoadingPanel.setVisibility(View.GONE);

        // display photo on the image view
        Glide.with(mPhotoImageView.getContext())
                .load(uri)
                .into(mPhotoImageView);
    }

}
