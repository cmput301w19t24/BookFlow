package com.example.bookflow;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.regex.Pattern;

/**
 * This activity is for user to edit their user name, user phone and user self intro and picture
 * only yourself can edit your profile, other cannot see the button
 */
public class EditProfileActivity extends AppCompatActivity {
    private TextView username, selfIntro,phone;
    private String userUid;
    private ImageView userIcon;
    private Uri imageUri;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private DatabaseReference dbRef;
    private boolean iconChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        userIcon = findViewById(R.id.edit_profile_img);
        username = findViewById(R.id.edit_username);
        phone = findViewById(R.id.edit_phone);
        selfIntro = findViewById(R.id.edit_self_intro);

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        userUid = mAuth.getCurrentUser().getUid();

        userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
        changeImageView();
    }


    @Override
    protected void onStart() {
        super.onStart();
        dbRef = FirebaseDatabase.getInstance().getReference();

        ValueEventListener userInfoListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String phoneHint = "";
                String nameHint = "";
                String introHint = "";

                for (DataSnapshot eachUser: dataSnapshot.getChildren()) {
                    String getUid = eachUser.child("uid").getValue().toString();
                    if (getUid.equals(userUid)) {
                        phoneHint = eachUser.child("phoneNumber").getValue().toString();
                        nameHint = eachUser.child("username").getValue().toString();
                        introHint = eachUser.child("selfIntro").getValue().toString();
                        break;
                    }
                }

                username.setText(nameHint);
                selfIntro.setText(introHint);
                phone.setText(phoneHint);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("listener cancelled", databaseError.toException());
            }
        };

        dbRef.child("Users").addListenerForSingleValueEvent(userInfoListener2);
    }

    /**
     * Choose your icon from album
     */
    private void update() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setFixAspectRatio(true)
                .setAspectRatio(1, 1)
                .start(EditProfileActivity.this);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                imageUri = result.getUri();
                Glide.with(EditProfileActivity.this).load(imageUri).into(userIcon);
                iconChanged = true;
            }
        }
    }


    /**
     * download user image from storage and load into image view
     */
    private void changeImageView() {
        // download user image from storage and update
        StorageReference storageRef = storage.getReference().child("users").child(userUid);
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(EditProfileActivity.this).load(uri).into(userIcon);
            }
        });
    }

    /**
     * delete the old image and put a new image in, they have the same file name
     */
    private void updateStorage() {
        final StorageReference storageRef;
        try {
            storageRef = storage.getReference().child("users").child(userUid);
        } catch (Exception e) {
            return ;
        }

        storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // file deleted
            }
        });

        storageRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    storageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        // store the user's profile to database and storage
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            String imageurl = task.getResult().toString();
                            FirebaseDatabase.getInstance().getReference().child("Users").child(userUid).child("imageurl").setValue(imageurl);
                            // start main activity
                            Intent intent_main = new Intent(EditProfileActivity.this, MainActivity.class);
                            startActivity(intent_main);
                        }
                    });
                } else {
                    Toast.makeText(EditProfileActivity.this, "Failed to add icon", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /**
     * The onClick method for button "CONFIRM"
     * It checks if each input is in correct format then call update
     * @param view onClick method needed
     */
    public void confirm_button(View view) {

        if (iconChanged) {
            updateStorage();
        }


        String usernameStr = username.getText().toString();
        String introStr = selfIntro.getText().toString();
        String phoneStr = phone.getText().toString();

        boolean valid = true;

        // check if user name is entered in correct format
        String usernamePat = "^([a-z0-9A-Z]{3,20})$";
        if (!Pattern.matches(usernamePat, usernameStr)) {
            username.setError("username should more 6 and less than 20 characters with only letters or numbers");
            valid = false;
        }

        // check if self introduction is valid
        if (introStr.length() > 25) {
            selfIntro.setError("self introduction cannot exceed 25 characters");
            valid = false;
        }

        // check if phone is valid
        String phonePat = "[0-9]+";
        if (!Pattern.matches(phonePat, phoneStr)) {
            valid = false;
        }

        if (!valid)
            return ;

        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();
        dbRef.child("Users").child(uid).child("username").setValue(usernameStr);
        dbRef.child("Users").child(uid).child("selfIntro").setValue(introStr);
        dbRef.child("Users").child(uid).child("phoneNumber").setValue(phoneStr);

        Intent intent = new Intent(EditProfileActivity.this, UserProfileActivity.class);
        startActivity(intent);
    }
}
