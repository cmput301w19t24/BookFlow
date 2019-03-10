/**
 * DEPRECATED - DO NOT USE!
 */

package com.example.bookflow;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;

public class AddPhotoActivity extends AppCompatActivity {

    public static final String BITMAP_KEY = "bitmap";
    private final int REQUEST_IMAGE_CAPTURE = 1;

    private Bitmap selectedImg;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);

        gridView = findViewById(R.id.add_photo_image_grid);
        selectedImg = null;

    }

    public void clickDoneButton(View v) {
        Intent intent = new Intent();
        if (selectedImg != null) {
            intent.putExtra(BITMAP_KEY, selectedImg);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            // TODO: popup a dialog saying you haven't selected an image
        }
    }

    public void clickTakePhotoButton(View v) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            selectedImg = (Bitmap) extras.get("data");
        }
    }

}
