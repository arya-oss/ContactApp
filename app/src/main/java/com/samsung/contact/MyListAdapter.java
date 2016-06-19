package com.samsung.contact;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Rajmani on 19-06-16.
 */
public class MyListAdapter extends BaseAdapter {
    public static final int ROW_ID = 1;
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater layoutInflater;
    private ImageView imageView;

    /**
     *
     * @param activity
     * @param data
     */
    public MyListAdapter(Activity activity, ArrayList<HashMap<String, String>> data) {
        this.activity = activity;
        this.data = data;
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageView = new ImageView(activity.getApplicationContext());
    }

    public void refreshList(ArrayList<HashMap<String, String>> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null)
            view = layoutInflater.inflate(R.layout.list_row, null);
        TextView fullName = (TextView) view.findViewById(R.id.list_title);
        final TextView phone = (TextView) view.findViewById(R.id.list_phone);
        ImageView imageView = (ImageView) view.findViewById(R.id.list_image);
        HashMap<String, String> row = data.get(position);
        fullName.setText(row.get(DBHelper.COLUMN_FNAME) + " " + row.get(DBHelper.COLUMN_LNAME));
        phone.setText(row.get(DBHelper.COLUMN_PHONE));
        ImageView calBtn = (ImageView) view.findViewById(R.id.callBtn);
        calBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(MainActivity.TAG, "Call Button Clicked ");
                if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone.getText().toString()));
                    activity.startActivity(intent);
                    return;
                }
            }
        });
        Bitmap bmp = null;
        File file = new File(row.get(DBHelper.COLUMN_PICTURE));
        bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
        if(null == bmp) {
            imageView.setImageDrawable(view.getResources().getDrawable(R.drawable.user));
        } else {
            imageView.setImageBitmap(bmp);
        }
        return view;
    }
}
