package com.example.jsonweather;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class AdapterWeather extends ArrayAdapter<ModalWeather> {
    Activity context;
    int resource;
    List<ModalWeather> objects;
    public AdapterWeather(@NonNull Activity context, int resource, @NonNull List<ModalWeather> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=this.context.getLayoutInflater();
        View row=inflater.inflate(this.resource,null);
        TextView txt=row.findViewById(R.id.textView2);
        txt.setText(this.objects.get(position).toString());
        return row;
    }
}
