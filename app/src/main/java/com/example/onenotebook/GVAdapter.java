package com.example.onenotebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;


public class GVAdapter extends ArrayAdapter<GVAdapter.GridModel> {

    public GVAdapter(@NonNull Context context, ArrayList<GridModel> gridModelArrayList) {
        super(context, 0, gridModelArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item, parent, false);
        }



        GridModel gridModel = getItem(position);
        TextView courseTV = listitemView.findViewById(R.id.CourseName);

        courseTV.setText(gridModel.getCourse_name());
        return listitemView;
    }

    public static class GridModel {

        private String course_name;


        public GridModel(@NonNull String course_name) {

            this.course_name = course_name;
        }

        public String getCourse_name() {

            return course_name;
        }


        public void setCourse_name(String course_name) {
            this.course_name = course_name;
        }
    }
}