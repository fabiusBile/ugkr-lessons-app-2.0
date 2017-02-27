package com.ugkr.lessons.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.ugkr.lessons.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UpdateLinksListeenr} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class FragmentSettings extends Fragment   implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener{

    Button updateLinksButton;
    View view;
    SharedPreferences sPref;
    private UpdateLinksListeenr updateLinksListener;

    public FragmentSettings() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        updateLinksButton = (Button) view.findViewById(R.id.updateSettingsButton);
        updateLinksButton.setOnClickListener(this);

        sPref = getActivity().getPreferences(MODE_PRIVATE);
        int act = sPref.getInt("act",1);
        boolean onlyFav = sPref.getBoolean("onlyFav",false);

        RadioGroup rg = (RadioGroup) view.findViewById(R.id.typeRadioGroup);
        rg.setOnCheckedChangeListener(this);
        Switch favSwitch = (Switch) view.findViewById(R.id.onlyFav);
        favSwitch.setChecked(onlyFav);
        favSwitch.setOnCheckedChangeListener(this);

        RadioButton rb;
        if (act==1){
            rb = (RadioButton) view.findViewById(R.id.radioButtonForStudents);
        } else{
            rb = (RadioButton) view.findViewById(R.id.radioButtonForTeachers);
        }
        rb.setChecked(true);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UpdateLinksListeenr) {
            updateLinksListener = (UpdateLinksListeenr) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement UpdateSettingsListener");
        }
    }
    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof UpdateLinksListeenr) {
            updateLinksListener = (UpdateLinksListeenr) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement UpdateSettingsListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        updateLinksListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.updateSettingsButton:
                updateLinksListener.onUpdateLinksClick();
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        int act;
        if (checkedId==R.id.radioButtonForStudents){
            act = 1;
        } else {
            act = 4;
        }
        SharedPreferences.Editor editor = sPref.edit();
        editor.putInt("act",act);
        editor.putInt("selectedRowNum",0);
        editor.apply();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences.Editor editor = sPref.edit();
        editor.putBoolean("onlyFav",isChecked);
        editor.apply();
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training Lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface UpdateLinksListeenr{
        // TODO: Update argument type and name
        void onUpdateLinksClick();
    }
}
