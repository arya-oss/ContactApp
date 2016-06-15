package com.samsung.contact;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

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
        int id = getIntent().getIntExtra(DBHelper.COLUMN_ID, 0);
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
            imageView.setImageBitmap(BitmapFactory.decodeFile(cursor.getString(6)));
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
                Log.d(TAG, "Want to check it on Map");
                break;
            case R.id.show_save:
                Log.d(TAG, "Want to Save it.");
                break;
            case R.id.show_delete:
                Log.d(TAG, "Want to delete it.");
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
