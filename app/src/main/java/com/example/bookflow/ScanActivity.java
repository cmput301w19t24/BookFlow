/**
    ScanActivity: the isbn scanner
 */

package com.example.bookflow;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.bookflow.Util.PhotoUtility;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

/**
 *  Scan the barcode on-the-fly with the rear camera.
 *
 *  Return the ISBN string to the caller activity with an intent.
 *  To retrieve the ISBN, do the following in onActivityResult:
 *       String isbn = data.getStringExtra(ScanActivity.SCAN_RESULT);
 */
public class ScanActivity extends AppCompatActivity {

    protected static final String SCAN_RESULT = "scan_result";
    private static final String TAG = "ScanActivity";

    private SurfaceView mCameraView;

    private CameraSource mCameraSource;
    private BarcodeDetector mBarcodeDetector;


    /**
     * Initialize UI elements, barcode detector, camera source and camera view.
     * @param savedInstanceState saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);


        mCameraView = findViewById(R.id.scan_camera_view);

        mBarcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.EAN_13)// ISBN 13 CODE
                .build();

        mCameraSource = new CameraSource
                .Builder(this, mBarcodeDetector)
                .setAutoFocusEnabled(true)
                .build();

        mCameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                try {
                    // check CAMERA permission
                    if (!PhotoUtility.checkCameraPermission(ScanActivity.this)) {
                        return;
                    }
                    mCameraSource.start(mCameraView.getHolder());
                } catch (IOException ie) {
                    Log.e(TAG, ie.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mCameraSource.stop();
            }
        });


        mBarcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            /**
             * callback function that is invoked when new detections are available.
             * Only retrieve the first detection and return to the previous activity
             * with an intent that holds the result ISBN code as a string.
             * @param detections
             */
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() != 0) {
                    String retBarcode = barcodes.valueAt(0).displayValue;
                    Log.i(TAG, "ISBN result: " + retBarcode);

                    // go back to the caller activity and return the ISBN with an intent.
                    Intent resIntent = new Intent();
                    resIntent.putExtra(SCAN_RESULT, retBarcode);
                    setResult(Activity.RESULT_OK, resIntent);
                    finish();
                }
            }
        });
    }


}
