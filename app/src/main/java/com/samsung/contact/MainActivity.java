package com.samsung.contact;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private ListView listView;
    private DBHelper dbHelper;
    private MyListAdapter myListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showList();
    }

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
                intent = new Intent(getApplicationContext(), DisplayActivity.class);
                intent.putExtra(DBHelper.COLUMN_ID, position+1);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ArrayList<HashMap<String, String>> contacts = dbHelper.getAllContacts();
        myListAdapter = new MyListAdapter(this, contacts);
        myListAdapter.notifyDataSetChanged();
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
                startActivity(new Intent(this, AddActivity.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
