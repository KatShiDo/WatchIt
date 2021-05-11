package com.example.watchit.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.watchit.R;

import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<ListItemClass> {
    private List<ListItemClass> list_item;
    private LayoutInflater inflater;

    public CustomArrayAdapter(@NonNull Context context, int resource, List<ListItemClass> list_item, LayoutInflater inflater) {
        super(context, resource, list_item);
        this.list_item = list_item;
        this.inflater = inflater;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ListItemClass listItemMain = this.list_item.get(position);
        convertView = inflater.inflate(R.layout.list_item, parent, false);
        TextView label = convertView.findViewById(R.id.item_text);
        label.setText(listItemMain.getText());
        ImageView iconImageView = convertView.findViewById(R.id.item_image);
        iconImageView.setImageBitmap(listItemMain.getImage());

        return convertView;
    }
}
