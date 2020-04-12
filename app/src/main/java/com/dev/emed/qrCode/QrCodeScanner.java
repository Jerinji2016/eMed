package com.dev.emed.qrCode;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.dev.emed.R;
import com.dev.emed.doctor.PatientDetailsActivity;
import com.dev.emed.patient.PrescriptionDetailsActivity;
import com.dev.emed.qrCode.helper.EncryptionHelper;
import com.dev.emed.qrCode.models.PatientDetailObject;
import com.dev.emed.qrCode.models.PrescriptionObject;
import com.dev.emed.qrCode.models.UserObject;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.Collections;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QrCodeScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final int MY_CAMERA_REQUEST_CODE = 6515;
    private static final String HUAWEI = "huawei";
    ZXingScannerView qrCodeScanner;
    private String userType;

    private static final String TAG = "QrCodeScanner";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_scanner);

        Intent i = getIntent();
        userType = i.getStringExtra("userType");
        Log.d(TAG, "onCreate: ====================="+userType);

        qrCodeScanner = findViewById(R.id.qr_code_scanner);
        setScannerProperties();
    }

    private void setScannerProperties() {
        qrCodeScanner.setFormats(Collections.singletonList(BarcodeFormat.QR_CODE));
        qrCodeScanner.setAutoFocus(true);
        qrCodeScanner.setLaserColor(R.color.colorAccent);
        qrCodeScanner.setMaskColor(R.color.colorAccent);

        if (Build.MANUFACTURER.equalsIgnoreCase(HUAWEI))
            qrCodeScanner.setAspectTolerance(0.5f);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
            }
        }
        qrCodeScanner.startCamera();
        qrCodeScanner.setResultHandler(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCodeScanner.stopCamera();
    }


    @Override
    public void handleResult(Result result) {
        if (result != null) {
            Toast.makeText(this, "Scan Complete", Toast.LENGTH_SHORT).show();
            String decryptedString = EncryptionHelper
                    .getInstance()
                    .getDecryptionString(result.getText());

            if (userType.equals("Doctor")) {
                Log.d(TAG, "handleResult: DOCTOR ===========***********===========");
                Intent i = new Intent(getApplicationContext(), PatientDetailsActivity.class);
                i.putExtra("dcy_text", decryptedString);
                startActivity(i);
            } else {
                Log.d(TAG, "handleResult: PATIENT==========************============");
                Intent i = new Intent(getApplicationContext(), PrescriptionDetailsActivity.class);
                i.putExtra("dcy_text", decryptedString);
                startActivity(i);
            }
        } else {
            Toast.makeText(this, "No Result, Please scan again!", Toast.LENGTH_SHORT).show();
        }
    }
}
