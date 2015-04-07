package com.mediapp.ft.mediapp;

/**
 * Created by torreta on 07/04/2015.
 */

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mediapp.ft.db.Treatment;

public class TreatmentAdapter extends ArrayAdapter<Treatment> {
    List<Treatment> TreatmentList;
    LayoutInflater vi;
    int Resource;
    ViewHolder holder;

    public TreatmentAdapter(Context context, int resource, List<Treatment> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        TreatmentList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // convert view = design
        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();
            v = vi.inflate(Resource, null);
            holder.ItemIcon = (ImageView) v.findViewById(R.id.item_icon);
            holder.ItemName = (TextView) v.findViewById(R.id.item_name);
            holder.ItemFinish = (TextView) v.findViewById(R.id.item_date);
//            holder.ItemFrequency = (TextView) v.findViewById(R.id.i);
            holder.ItemHour = (TextView) v.findViewById(R.id.item_hour);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        holder.ItemIcon.setImageResource(R.drawable.pill);
        holder.ItemName.setText(TreatmentList.get(position).getName());
        holder.ItemFinish.setText(TreatmentList.get(position).getFinish());
        holder.ItemHour.setText(TreatmentList.get(position).getHour());
//        holder.tvCountry.setText(TreatmentList.get(position).getCountry());
//        holder.tvHeight.setText("Height: " + TreatmentList.get(position).getHeight());
//        holder.tvSpouse.setText("Spouse: " + TreatmentList.get(position).getSpouse());
//        holder.tvChildren.setText("Children: " + TreatmentList.get(position).getChildren());
        return v;

    }

    static class ViewHolder {
        public ImageView ItemIcon;
        public TextView ItemName;
        public TextView ItemStart;
        public TextView ItemFinish;
        public TextView ItemHour;
        public TextView ItemFrequency;
    }


}