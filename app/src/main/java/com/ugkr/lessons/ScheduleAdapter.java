package com.ugkr.lessons;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by fabiusbile on 10.02.17.
 */

public class ScheduleAdapter extends BaseAdapter {

    ArrayList<Lesson> lessons;
    Context context;
    LayoutInflater layoutInflater;

    public ScheduleAdapter( ArrayList<Lesson>  lessons, Context context) {
        this.lessons = lessons;
        this.context = context;
        this.layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return lessons.size();
    }

    @Override
    public Object getItem(int position) {
        return lessons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null){
            view = layoutInflater.inflate(R.layout.lesson,parent,false);
        }

        Lesson lesson = (Lesson) getItem(position);

        TextView lessonNumber = (TextView) view.findViewById(R.id.number);
        lessonNumber.setText(lesson.num);

        TextView lessonText = (TextView) view.findViewById(R.id.lesson);
        lessonText.setText(lesson.lesson);
        return view;
    }
}
