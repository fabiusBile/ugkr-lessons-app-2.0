package com.ugkr.lessons;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by fabiusbile on 23.01.17.
 */
public class ScheduleActivity extends AppCompatActivity {

    ScheduleAdapter scheduleAdapter;

    @Override

    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_activity);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        TextView dateHeader = (TextView) findViewById(R.id.dateHeader);
        String date = intent.getStringExtra("date");
        dateHeader.setText(date);

        ArrayList<Lesson> ls = intent.getParcelableArrayListExtra("schedule");

        scheduleAdapter = new ScheduleAdapter(ls,this);

        ListView listView = (ListView) findViewById(R.id.scheduleListView);
        listView.setAdapter(scheduleAdapter);

    }

    @Override
    public void finish() {
        super.finish();
        //overridePendingTransition(R.anim.left_out, R.anim.left_out);
    }

}
