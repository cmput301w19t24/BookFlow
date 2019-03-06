package com.example.bookflow;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class AddBookActivity extends BasicActivity {

    private final int ADD_PHOTO_REQUEST_CODE = 0;
    private ImageView photoImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        photoImageView = findViewById(R.id.imageView4);
    }

    public void clickAddPhotoImage(View v) {
        Intent intent = new Intent(this, AddPhotoActivity.class);
        startActivityForResult(intent, ADD_PHOTO_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_PHOTO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = data.getParcelableExtra(AddPhotoActivity.BITMAP_KEY);
                photoImageView.setImageBitmap(bitmap);
            }
        }
    }
}
