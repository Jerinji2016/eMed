package com.dev.emed.qrCode;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.dev.emed.R;
import com.dev.emed.qrCode.helper.QRCodeHelper;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Objects;

public class OpenQrDialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.display_qr_dialog, container, true);

        String enc = getArguments().getString("enc_text");
        Button dismisDialog = view.findViewById(R.id.dismiss_dialog);

        dismisDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        Bitmap bmp = QRCodeHelper.newInstance(getActivity())
                .setContent(enc)
                .setErrorCorrectionLevel(ErrorCorrectionLevel.Q)
                .setMargin(2)
                .getQRCOde();
        ImageView qrImage = view.findViewById(R.id.qr_code_image);
        qrImage.setImageBitmap(bmp);
        return view;
    }
}
