package com.samsung.contact;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * <p>MainActivity for contact app</p>
 * It shows all contacts and options for adding new contacts in menu
 * shows contact list and onClick on list item it goes to DisplayActivity
 */
public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private ListView listView;
    private DBHelper dbHelper;
    private MyListAdapter myListAdapter;

    public static final int REQUEST_RESULT_ADD = 2;
    public static final int REQUEST_RESULT_DELETE = 3;
    public static final int REQUEST_RESULT_UPDATE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);
        showList();
    }

    /**
     * Add all contacts list to current activity by getting all contacts from db.
     */
    private void showList() {
        dbHelper = new DBHelper(this);
        ArrayList<HashMap<String, String>> contacts = dbHelper.getAllContacts();
        listView = (ListView)findViewById(R.id.listView);
        myListAdapter = new MyListAdapter(this, contacts);
        listView.setAdapter(myListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Clicked on item "+position);
                Intent intent;
                int _tmp_id = Integer.parseInt(view.getTag(MyListAdapter.ROW_ID).toString());
                intent = new Intent(getApplicationContext(), DisplayActivity.class);
                intent.putExtra(DBHelper.COLUMN_ID, _tmp_id);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        super.onActivityResult(requestCode, resultCode, intent);
//        if (resultCode == RESULT_OK && null != intent) {
//            Bundle bundle = intent.getExtras();
//            switch (requestCode){
//                case REQUEST_RESULT_ADD:
//                    Log.d(TAG, "add "+bundle.get(DBHelper.COLUMN_ID));
//                    break;
//                case REQUEST_RESULT_DELETE:
//                    Log.d(TAG, "delete "+bundle.get(DBHelper.COLUMN_ID));
//                    break;
//                case REQUEST_RESULT_UPDATE:
//                    Log.d(TAG, "update "+bundle.get(DBHelper.COLUMN_ID));
//                    break;
//                default:
//                    Log.d(TAG, "unidentified activity result");
//            }
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);

        switch(item.getItemId()) {
            case R.id.add:
                startActivity(new Intent(this, AddActivity.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

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
