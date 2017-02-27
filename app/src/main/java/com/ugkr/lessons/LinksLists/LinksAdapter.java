package com.ugkr.lessons.LinksLists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ugkr.lessons.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fabius on 24.02.17.
 */

public class LinksAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    List<NameCodePair> list;
    LinksList ll;

    @Override
    public int getCount() {
        return list.size();
    }

    public LinksAdapter(Context ctx, List<NameCodePair> list) {
        this.ctx = ctx;
        this.list = list;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.group_row_w_fav, parent, false);
        }
        ll = new LinksList(ctx);

        NameCodePair row =  list.get(position);
        TextView name  = (TextView) view.findViewById(R.id.groupName);
        CheckBox favCheckBox = (CheckBox) view.findViewById(R.id.favCheckBox);

        name.setText(row.name);
        favCheckBox.setOnCheckedChangeListener(favCheckListener);
        favCheckBox.setTag(position);
        favCheckBox.setChecked(row.favourite);

        return view;
    }

    CompoundButton.OnCheckedChangeListener favCheckListener = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            NameCodePair row = (NameCodePair )getItem((Integer) buttonView.getTag());
            row.favourite = isChecked;
            ll.addNameCodePair(row.name,row.code,row.favourite,row.isGroup);
        }
    };
}
