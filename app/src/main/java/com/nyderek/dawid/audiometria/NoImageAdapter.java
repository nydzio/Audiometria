package com.nyderek.dawid.audiometria;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class NoImageAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] date;
    private final String[] firstName;
    private final String[] lastName;

    public NoImageAdapter(Activity context, String[] date, String[] firstName, String[] lastName) {
        super(context, R.layout.custom_row, date);
        this.context = context;
        this.date = date;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View customView = layoutInflater.inflate(R.layout.no_image_custom_row, null, true);

        TextView txtDate = (TextView) customView.findViewById(R.id.mediumText);
        TextView txtFirstName = (TextView) customView.findViewById(R.id.smallText);
        TextView txtLastName = (TextView) customView.findViewById(R.id.smallText2);
        txtDate.setText(date[position]);
        txtFirstName.setText(firstName[position]);
        txtLastName.setText(lastName[position]);
        return customView;

    }
}
