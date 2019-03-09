package com.example.bookflow;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class ScanActivity extends AppCompatActivity {

    private static final String TAG = "ScanActivity";

    private SurfaceView mCameraView;
    private TextView mBarcodeInfoTextView;

    private CameraSource mCameraSource;
    private BarcodeDetector mBarcodeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);


        mCameraView = findViewById(R.id.scan_camera_view);
        mBarcodeInfoTextView = findViewById(R.id.scan_txt_content);


        mBarcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.CODE_128)//QR_CODE)
                .build();

        mCameraSource = new CameraSource
                .Builder(this, mBarcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .build();

        mCameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                try {
                    // check CAMERA permission
                    if (ActivityCompat.checkSelfPermission(ScanActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ScanActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
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

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() != 0) {
                    mBarcodeInfoTextView.post(new Runnable() {
                        public void run() {
                            mBarcodeInfoTextView.setText(
                                    barcodes.valueAt(0).displayValue
                            );
                        }
                    });
                }
            }
        });
    }
}
