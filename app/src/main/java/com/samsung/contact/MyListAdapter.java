package com.samsung.contact;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Rajmani on 15-06-16.
 */
public class MyListAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater layoutInflater;
    private ImageView imageView;

    public MyListAdapter(Activity activity, ArrayList<HashMap<String,String>> data) {
        this.activity = activity;
        this.data = data;
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageView = new ImageView(activity.getApplicationContext());
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null)
            view = layoutInflater.inflate(R.layout.list_row, null);
        TextView fullName = (TextView)view.findViewById(R.id.list_title);
        TextView phone = (TextView)view.findViewById(R.id.list_phone);
        ImageView imageView = (ImageView)view.findViewById(R.id.list_image);
        HashMap<String, String> row = data.get(position);
        fullName.setText(row.get(DBHelper.COLUMN_FNAME)+" "+row.get(DBHelper.COLUMN_LNAME));
        phone.setText(row.get(DBHelper.COLUMN_PHONE));
        Bitmap bmp = null;
        bmp = BitmapFactory.decodeFile(row.get(DBHelper.COLUMN_PICTURE));
        if(null == bmp) {
            imageView.setImageDrawable(view.getResources().getDrawable(R.drawable.user));
        } else {
            imageView.setImageBitmap(bmp);
        }
        return view;
    }
}
