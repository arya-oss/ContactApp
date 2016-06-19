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
import java.util.StringTokenizer;

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
        final ArrayList<HashMap<String, String>> contacts = dbHelper.getAllContacts();
        listView = (ListView)findViewById(R.id.listView);
        myListAdapter = new MyListAdapter(this, contacts);
        listView.setAdapter(myListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                HashMap<String, String> _tmp = (HashMap<String, String>) myListAdapter.getItem(position);
                int _tmp_id = Integer.parseInt(_tmp.get(DBHelper.COLUMN_ID));
                Log.d(TAG, "Clicked on item "+position+ " id " + _tmp_id);
                intent = new Intent(getApplicationContext(), DisplayActivity.class);
                intent.putExtra(DBHelper.COLUMN_ID, _tmp_id);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        myListAdapter.refreshList(dbHelper.getAllContacts());
    }

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
                Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                startActivity(intent);
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
