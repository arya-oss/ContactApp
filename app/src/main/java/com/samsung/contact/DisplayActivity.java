package com.samsung.contact;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.net.URI;

/**
 * Created by Rajmani on 15-06-16.
 */
public class DisplayActivity extends AppCompatActivity {
    private static final String TAG = "DisplayActivity";

    private ImageView imageView;
    private EditText first_name;
    private EditText last_name;
    private EditText phone;
    private EditText email;
    private EditText location;
    private DBHelper dbHelper;
    private int id=0;
    private static int RESULT_LOAD_IMAGE = 1;
    private String picture_path = "";

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_display);
        imageView = (ImageView)findViewById(R.id.display_view_image);
        first_name = (EditText) findViewById(R.id.display_first_name);
        last_name = (EditText) findViewById(R.id.display_last_name);
        phone = (EditText) findViewById(R.id.display_phone);
        email = (EditText) findViewById(R.id.display_email);
        location = (EditText) findViewById(R.id.display_latlng);
        id = getIntent().getIntExtra(DBHelper.COLUMN_ID, 0);
        verifyStoragePermissions(this);
        if (id == 0) {
            Log.d(TAG, "Not any id came from parent.");
        } else{
            dbHelper = new DBHelper(this);
            Cursor cursor = dbHelper.getData(id);
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                Log.d(TAG, "SQL results return 0");
                return;
            }
            first_name.setText(cursor.getString(1));
            last_name.setText(cursor.getString(2));
            phone.setText(cursor.getString(3));
            email.setText(cursor.getString(4));
            location.setText(cursor.getString(5));
            Log.d(TAG, cursor.getString(6));
            File file = new File(cursor.getString(6));
            imageView.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.show_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);

        switch(item.getItemId()) {
            case R.id.show_map:
                Log.d(TAG, "Wants to check it on Map");
                break;
            case R.id.show_save:
                Log.d(TAG, "Wants to Save it.");
                updateButton();
                break;
            case R.id.show_delete:
                Log.d(TAG, "Wants to delete it.");
                dbHelper.deleteContact(id);
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void setImageAndPath(View view) {
        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != intent) {
            Uri selectedImage = intent.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePath, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePath[0]);
            picture_path = cursor.getString(columnIndex);
            cursor.close();
            Bitmap bmp = null;
            try {
                bmp = getBitmapFromUri(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(null == bmp) {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.user));
            } else {
                imageView.setImageBitmap(bmp);
            }
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public void updateButton() {
        String fname = first_name.getText().toString();
        String lname = last_name.getText().toString();
        String ph = phone.getText().toString();
        String eml = email.getText().toString();
        String latlng = location.getText().toString();
        dbHelper.updateContact(id, fname, lname, ph, eml, latlng, picture_path);
        Toast.makeText(this, "Contact Updated", Toast.LENGTH_SHORT).show();
    }

    /**
     * Checks if the app has permission to write to device storage
     * If the app does not has permission then the user will be prompted to grant permission
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
        }
    }
}
