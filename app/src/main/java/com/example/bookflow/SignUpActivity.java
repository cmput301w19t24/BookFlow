package com.example.bookflow;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.regex.Pattern;

public class SignUpActivity extends BasicActivity {


    private EditText id, email, password, repassword,phone;
    private ImageView profile;
    private Button btn;
    private static final int PICK_FROM_ALBUM = 10;
    private Uri imageUri = null;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        profile = findViewById(R.id.img_profile);
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        id = findViewById(R.id.id);
        password = findViewById(R.id.password);
        repassword = findViewById(R.id.repassword);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        btn = findViewById(R.id.signup);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });

    }

    public void signUp(View v) {
        id = findViewById(R.id.id);
        password = findViewById(R.id.password);
        repassword = findViewById(R.id.repassword);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        btn = findViewById(R.id.signup);

        boolean valid = true;
//        regex
        String usernamePat = "^([a-z0-9]{3,20})$";
        if (!Pattern.matches(usernamePat, id.getText().toString())) {
            id.setError("username should more 6 and less than 20 characters with only letters or numbers");
            id = null;
            valid = false;
        }
        String passwordPat = "^(.{6,20})$";
        if (!Pattern.matches(passwordPat, password.getText().toString())) {
            password.setError("password should more 6 and less than 20 characters");
            password = null;
            valid = false;
        }
        if (!Pattern.matches(passwordPat, repassword.getText().toString())) {
            repassword.setError("password should more 6 and less than 20 characters");
            repassword = null;
            valid = false;
        }
        String emailPat = "[0-9a-z]+@[0-9a-z]+.[a-z]+";
        if (!Pattern.matches(emailPat, email.getText().toString())) {
            email.setError("Invalid email address");
            email = null;
            valid = false;
        }
        String phonePat = "[0-9]+";
        if (!Pattern.matches(phonePat, phone.getText().toString())) {
            phone.setError("Invalid phone number");
            phone = null;
            valid = false;
        }

        if (!valid)
            return;

        if (!password.getText().toString().equals(repassword.getText().toString())) {
            password.setError("Password not match!");
            repassword.setError("Password not match!");
            password = null;
            repassword = null;
            return;
        }

        mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    final String uid = mAuth.getCurrentUser().getUid();
                    final StorageReference storageRef = storage.getReference("users").child(uid);
                    // if the user did not upload an icon
                    if (imageUri==null) {
                        Toast.makeText(SignUpActivity.this, "Please upload an icon", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    storageRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                storageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    // store the user's profile to database and storage
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        String imageurl = task.toString();
                                        User user = new User();
                                        user.setUsername(id.getText().toString());
                                        user.setEmail(email.getText().toString());
                                        user.setPhoneNumber(phone.getText().toString());
                                        user.setImageurl(imageurl);
                                        user.setUid(uid);

                                        FirebaseDatabase.getInstance().getReference().child("Users").child(uid).setValue(user);
                                        Intent intent_signedUp = new Intent(SignUpActivity.this, LoginActivity.class);
                                        startActivity(intent_signedUp);
                                    }
                                });
                            } else {
                                Toast.makeText(SignUpActivity.this, "Failed to add icon", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(SignUpActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void upload() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK) {
            profile.setImageURI(data.getData());
            imageUri = data.getData();
        }
    }
}
