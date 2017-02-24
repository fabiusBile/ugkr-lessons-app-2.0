package com.ugkr.lessons.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ugkr.lessons.R;

public class FragmentBells extends Fragment {


    public FragmentBells() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_bells, container, false);
        // Inflate the layout for this fragment
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                getActivity(), R.array.bells, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                getActivity(), R.array.bellsSaturday, android.R.layout.simple_spinner_item);
        ListView l1 = (ListView) view.findViewById(R.id.bells);
        l1.setAdapter(adapter1);
        ListView l2 = (ListView)view.findViewById(R.id.bellsSaturday);
        l2.setAdapter(adapter2);

        return view;
    }



    @Override
    public void onDetach() {
        super.onDetach();
    }

}
