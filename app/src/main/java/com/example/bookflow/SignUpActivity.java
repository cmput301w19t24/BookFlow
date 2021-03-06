/**
 * SignUpActivity Class
 *
 * Copyright 2019 Shengyao Lu
 *
 * @author shengyao
 * @version 1.0
 * @created 2019-03-01
 */

package com.example.bookflow;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bookflow.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private EditText id, email, password, repassword,phone;
    private Button btn;
    private ImageView profile;
    private static final int PICK_FROM_ALBUM = 10;
    private Uri imageUri = null;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        profile = findViewById(R.id.img_profile);
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });
    }

    /**
     * Sign up method, when "sign up" button is clicked
     * @param v
     */
    public void signUp(View v) {
        id = findViewById(R.id.id);
        password = findViewById(R.id.password);
        repassword = findViewById(R.id.repassword);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        btn = findViewById(R.id.signup);

        boolean valid = true;
        // regex username
        String usernamePat = "^([a-z0-9A-Z]{3,20})$";
        if (!Pattern.matches(usernamePat, id.getText().toString())) {
            id.setError("username should more 6 and less than 20 characters with only letters or numbers");
            id.setText("");
            valid = false;
        }
        // regex password
        String passwordPat = "^(.{6,20})$";
        if (!password.getText().toString().equals(repassword.getText().toString())) {
            password.setError("Password not match!");
            repassword.setError("Password not match!");
            password.setText("");
            repassword.setText("");
            valid = false;
        } else {
            if (!Pattern.matches(passwordPat, password.getText().toString())) {
                password.setError("password should more 6 and less than 20 characters");
                password.setText("");
                valid = false;
            }
            if (!Pattern.matches(passwordPat, repassword.getText().toString())) {
                repassword.setError("password should more than 6 and less than 20 characters");
                repassword.setText("");
                valid = false;
            }
        }
        // regex email
        String emailPat = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$";
        if (!Pattern.matches(emailPat, email.getText().toString())) {
            email.setError("Invalid email address");
            email.setText("");
            valid = false;
        }
        // regex phone
        String phonePat = "[0-9]+";
        if (!Pattern.matches(phonePat, phone.getText().toString())) {
            phone.setError("Invalid phone number");
            phone.setText("");
            valid = false;
        }

        if (!valid)
            return;

        // sign up to firebase
        mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // pop up message saying "signed up"
                    Toast.makeText(SignUpActivity.this, "Signed Up", Toast.LENGTH_SHORT).show();
                    // get necessary info
                    final String uid = mAuth.getCurrentUser().getUid();
                    final StorageReference storageRef = storage.getReference("users").child(uid);
                    final User user = new User();

                    // store user information to firebase
                    user.setUsername(id.getText().toString());
                    user.setEmail(email.getText().toString());
                    user.setPhoneNumber(phone.getText().toString());
                    user.setUid(uid);
                    user.setSelfIntro("No introduction");
                    FirebaseDatabase.getInstance().getReference().child("Users").child(uid).setValue(user);

                    // add user icon
                    if (imageUri!=null) {
                        storageRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    storageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        // store the user's profile to database and storage
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            String imageurl = task.getResult().toString();
                                            FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("imageurl").setValue(imageurl);
                                            // start main activity
                                            Intent intent_main = new Intent(SignUpActivity.this, MainActivity.class);
                                            startActivity(intent_main);
                                        }
                                    });
                                } else {
                                    Toast.makeText(SignUpActivity.this, "Failed to add icon", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        // start main activity
                        Intent intent_main = new Intent(SignUpActivity.this, MainActivity.class);
                        startActivity(intent_main);
                    }
                } else {
                    task.getException().printStackTrace();
                    Toast.makeText(SignUpActivity.this, "Email address already registered.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * upload image from the album
     */
    private void upload() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setFixAspectRatio(true)
                .setAspectRatio(1, 1)
                .start(SignUpActivity.this);
    }

    /**
     * get image data
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                imageUri = result.getUri();
                profile.setImageURI(imageUri);
            }
        }
    }
}