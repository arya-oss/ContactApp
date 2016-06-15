package com.samsung.contact;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.FileDescriptor;
import java.io.IOException;

/**
 * Created by Rajmani on 15-06-16.
 */
public class AddActivity extends AppCompatActivity {
    private ImageView imageView;
    private EditText first_name;
    private EditText last_name;
    private EditText phone;
    private EditText email;
    private EditText location;
    private String picture_path = "";
    private static int RESULT_LOAD_IMAGE = 1;

    private DBHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_add);
        imageView = (ImageView)findViewById(R.id.edit_view_image);
        first_name = (EditText) findViewById(R.id.edit_first_name);
        last_name = (EditText) findViewById(R.id.edit_last_name);
        phone = (EditText) findViewById(R.id.edit_phone);
        email = (EditText) findViewById(R.id.edit_email);
        location = (EditText) findViewById(R.id.edit_latlng);
        dbHelper = new DBHelper(this);
    }

    public void setEmpty() {
        Drawable drawable = getResources().getDrawable(R.drawable.user);
        imageView.setImageDrawable(drawable);
        picture_path = "";
        first_name.setText("");
        last_name.setText("");
        phone.setText("");
        email.setText("");
        email.setText("");
    }

    public void resetButton(View v) {
        setEmpty();
    }

    public void saveButton(View v) {
        String fname = first_name.getText().toString();
        String lname = last_name.getText().toString();
        String ph = phone.getText().toString();
        String eml = email.getText().toString();
        String latlng = location.getText().toString();
        dbHelper.insertContact(fname, lname, ph, eml, latlng, picture_path);
        setEmpty();
    }

    /**
     * Used for setting profile pic from gallery
     * @param view
     */
    public void setImageAndPath(View view) {
        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode ==RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != intent) {
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
            imageView.setImageBitmap(bmp);
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
}
