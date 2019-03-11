package com.example.bookflow.Util;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.example.bookflow.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PhotoUtility {

    private static String TAG = "PhotoUtility";

    /**
     * Request Code for choosing photo from album
     */
    public static int RC_PHOTO_PICKER = 0;

    /**
     * Request Code for taking a photo
     */
    public static int RC_IMAGE_CAPTURE = 1;

    /**
     * fires a dialog that allows the user to choose from album or pick a photo with camera.
     * @param act the activity the calls this function.
     */
    public static void showPickPhotoDialog(final Activity act) {
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setTitle(R.string.pick_a_photo)
                .setItems(R.array.pick_photo_array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {

                            case 0: {
                                // pick from album
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.setType("image/jpeg");
                                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                                act.startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
                            }
                            break;
                            case 1: {
                                // take a photo
                                checkCameraPermission(act);
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (intent.resolveActivity(act.getPackageManager()) != null) {
                                    act.startActivityForResult(intent, RC_IMAGE_CAPTURE);
                                }
                            }
                            break;
                        }
                    }
                })
                .show();
    }

    /**
     * convert a bitmap to an URI by storing the bitmap to a
     * temporary space.
     * @param act the activity that calls this function.
     * @param bitmap the bitmap to be converted
     * @return a URI pointed to the temporary store location.
     */
    public static Uri bitmapToUri(Activity act, Bitmap bitmap) {
        if (!checkWriteExternalPermission(act)) {
            return null;
        }

        // create a temporary file
        File tempDir= Environment.getExternalStorageDirectory();
        tempDir=new File(tempDir.getAbsolutePath()+"/.temp/");
        tempDir.mkdirs();
        File tempFile = null;
        try {
            tempFile = File.createTempFile("temp", ".jpg", tempDir);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(act, act.getString(R.string.we_need_write_permission), Toast.LENGTH_LONG)
                    .show();
            return null;
        }

        // store the Bitmap to a temporary file
        try {
            FileOutputStream fos = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return Uri.fromFile(tempFile);
    }


    /**
     * check the external storage permission before proceeding.
     * Should be used before storing the picture to a temporary
     * location.
     * @param act the Activity that calls this function
     * @return a boolean indicating if the permission is currently invoked.
     */
    public static boolean checkWriteExternalPermission(Activity act){
        if (ContextCompat.checkSelfPermission(act, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG,"Permission is revoked");
            ActivityCompat.requestPermissions(act, new String[]{Manifest.permission.CAMERA}, 1);
            return false;
        }
        return true;
    }

    /**
     * check the external storage permission before proceeding.
     * Should be used before storing the picture to a temporary
     * location.
     *
     * @param act the Activity that calls this function
     * @return a boolean indicating if the permission is currently invoked.
     */
    public static boolean checkCameraPermission(Activity act){
        if (ContextCompat.checkSelfPermission(act, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG,"Permission is revoked");
            ActivityCompat.requestPermissions(act, new String[]{Manifest.permission.CAMERA}, 1);
            return false;
        }
        return true;
    }

}
