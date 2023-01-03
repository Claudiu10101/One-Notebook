package com.example.onenotebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;


public class LVAdapter extends BaseAdapter {

    Context context;
    public ArrayList<ListModel> elements;
    LayoutInflater inflater;

    public LVAdapter(Context context, ArrayList<ListModel> elements) {
        this.context = context;
        this.elements = elements;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return elements.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.list_item,null);
        TextView lessonName,lessonDate;
        lessonName = (TextView) view.findViewById(R.id.lessonName);
        lessonDate = (TextView) view.findViewById(R.id.lessonDate);

        lessonName.setText(elements.get(i).Name);
        lessonDate.setText(elements.get(i).Date);
        return view;
    }

    public static class ListModel {



        public String Name,Date;

        public ListModel(@NonNull String raw){
            String[] strings = raw.split("/");
            Name = strings[0];
            if(strings.length == 4)
                Date = strings[1]+"/"+strings[2]+"/"+strings[3];
            else
                Date = "";
        }

        public ListModel(@NonNull String name, String date) {
            Name = name;
            Date = date;
        }

        public String rawString(){
            return Name+"/"+Date;
        }

    }
}