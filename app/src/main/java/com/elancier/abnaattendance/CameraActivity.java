package com.elancier.abnaattendance;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;


public class CameraActivity extends AppCompatActivity {

    public static final String TAG = CameraActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.squarecamera__CameraFullScreenTheme);
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.squarecamera__activity_camera);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, CameraFragment.newInstance(), CameraFragment.TAG)
                    .commit();
        }
    }

    public void returnPhotoUri(Bitmap uri) {
        Intent data = new Intent();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        uri.compress(Bitmap.CompressFormat.JPEG, 70, bytes);
        String path="";
        if(Build.VERSION.SDK_INT<=28) {
             path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), uri, "Title", null);
        }
        else{
             path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), uri, "IMG_" + Calendar.getInstance().getTime(), null);
        }
        data.setData(Uri.parse(path));
        Log.e("data",uri.toString());
        if (getParent() == null) {
            setResult(RESULT_OK, data);
        } else {
            getParent().setResult(RESULT_OK, data);
        }

        finish();
    }

    public void onCancel(View view) {
        getSupportFragmentManager().popBackStack();
    }
}
