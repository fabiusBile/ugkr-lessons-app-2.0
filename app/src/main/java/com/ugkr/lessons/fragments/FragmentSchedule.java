package com.ugkr.lessons.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.Calendar;
import java.util.List;
import java.util.function.Predicate;

import com.ugkr.lessons.LinksLists.LinksAdapter;
import com.ugkr.lessons.LinksLists.LinksList;
import com.ugkr.lessons.LinksLists.NameCodePair;
import com.ugkr.lessons.MainActivity;
import com.ugkr.lessons.R;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentSchedule.ScheduleFragmentInterface} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class FragmentSchedule extends Fragment implements View.OnClickListener, Spinner.OnItemSelectedListener {

    View view;

    ScheduleFragmentInterface scheduleFragmentInterface;
    Button today, tomorrow, onDate;
    public  Spinner linksSpinner;
    List<NameCodePair> linksList;
    String code;
    boolean isGroup;

    SharedPreferences sPref;


    public interface ScheduleFragmentInterface {
        void OnDateSelected(Calendar calendar, String code, boolean isGroup);
        void OnDateSelected(Calendar calendar, String code, boolean isGroup, boolean today);
        void OnCalendarOpened(String code, boolean isGroup);
    }


    public FragmentSchedule() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        int selectedRowNum=0;
        String selectedCode = sPref.getString("selectedCode","");
        if (selectedCode.length()>0){
            for (int i=0;i!=linksList.size();i++){
                if (linksList.get(i).code.equals(code)){
                    selectedRowNum = i;
                    break;
                }
            }
        }
        linksSpinner.setSelection(selectedRowNum);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_schedule, container, false);
        sPref = getActivity().getPreferences(MODE_PRIVATE);

        today = (Button) view.findViewById(R.id.today);
        today.setOnClickListener(this);
        tomorrow = (Button) view.findViewById(R.id.tomorrow);
        tomorrow.setOnClickListener(this);
        onDate = (Button) view.findViewById(R.id.onDate);
        onDate.setOnClickListener(this);
        linksSpinner = (Spinner) view.findViewById(R.id.linksSpinner);

        int act = sPref.getInt("act",1);
        Boolean onlyFav = sPref.getBoolean("onlyFav",false);
        Boolean isGroup = act == 1;

        LinksList ll = new LinksList(getActivity());
        linksList = ll.GetLinks(isGroup,onlyFav);
        if (linksList.isEmpty() && !onlyFav){
            MainActivity activity = (MainActivity) getActivity();
            activity.onUpdateLinksClick();
            linksList = ll.GetLinks(isGroup,onlyFav);
        }

        //ArrayAdapter<NameCodePair> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, linksList);
        LinksAdapter adapter = new LinksAdapter(getActivity(),linksList);

        linksSpinner.setAdapter(adapter);
        linksSpinner.setOnItemSelectedListener(this);

        return view;
        // Inflate the layout for this fragment
    }

    @Override
    public void onClick(View v) {
        Calendar cal = Calendar.getInstance();
        switch (v.getId()){
            case R.id.today:
                scheduleFragmentInterface.OnDateSelected(cal,code,isGroup,true);
                break;
            case R.id.tomorrow:
                cal.add(Calendar.DAY_OF_MONTH,1);
                scheduleFragmentInterface.OnDateSelected(cal,code,isGroup);
                break;
            case R.id.onDate:
                scheduleFragmentInterface.OnCalendarOpened(code,isGroup);
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        code = linksList.get(position).code;
        isGroup = linksList.get(position).isGroup;
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString("selectedCode",code);
        editor.apply();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ScheduleFragmentInterface) {
            scheduleFragmentInterface = (ScheduleFragmentInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ScheduleFragmentInterface");
        }
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ScheduleFragmentInterface) {
            scheduleFragmentInterface = (ScheduleFragmentInterface) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement ScheduleFragmentInterface");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        scheduleFragmentInterface = null;
    }

}
