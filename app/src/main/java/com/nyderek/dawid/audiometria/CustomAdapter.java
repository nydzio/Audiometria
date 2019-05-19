package com.nyderek.dawid.audiometria;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


class CustomAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] choices;
    private final String[] descriptions;
    private final Integer[] imgID;

    public CustomAdapter(Activity context, String[] choices, String[] descriptions, Integer[] imgID) {
        super(context, R.layout.custom_row, choices);
        this.context = context;
        this.choices = choices;
        this.descriptions = descriptions;
        this.imgID = imgID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View customView = layoutInflater.inflate(R.layout.custom_row, null, true);

        TextView txtTitle = (TextView) customView.findViewById(R.id.mediumText);
        ImageView imageView = (ImageView) customView.findViewById(R.id.plusHolder);
        TextView txtDescription = (TextView) customView.findViewById(R.id.smallText);

        txtTitle.setText(choices[position]);
        imageView.setImageResource(imgID[position]);
        txtDescription.setText(descriptions[position]);
        return customView;

    }
}
