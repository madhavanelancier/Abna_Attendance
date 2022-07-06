package com.elancier.abnaattendance;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomeSpinner extends ArrayAdapter<String> {

    public CustomeSpinner(Context context,
                          ArrayList<String> algorithmList)
    {
        super(context, 0, algorithmList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable
            View convertView, @NonNull ViewGroup parent)
    {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable
            View convertView, @NonNull ViewGroup parent)
    {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView,
                          ViewGroup parent)
    {
        // It is used to set our custom view.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_spinner, parent, false);
        }

        try {
            TextView textViewName = convertView.findViewById(R.id.textView35);
            String currentItem = getItem(position);

            // It is used the name to the TextView when the
            // current item is not null.
            if (currentItem != null) {
                textViewName.setText(currentItem);

                    textViewName.setTextColor(Color.parseColor("#000000"));



            }
        }
        catch(Exception e){

    }
        return convertView;
    }
}
