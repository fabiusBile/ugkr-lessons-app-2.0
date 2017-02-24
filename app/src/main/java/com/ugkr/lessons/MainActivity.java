package
        com.ugkr.lessons;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import com.ugkr.lessons.LinksLists.LinksList;
import com.ugkr.lessons.fragments.DatePickerFragment;
import com.ugkr.lessons.fragments.FragmentBells;
import com.ugkr.lessons.fragments.FragmentSchedule;
import com.ugkr.lessons.fragments.FragmentSettings;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentSchedule.ScheduleFragmentInterface, DatePickerFragment.OnDateSelectedListener, FragmentSettings.UpdateLinksListeenr {
    FragmentSchedule fschedule;
    FragmentBells fbells ;
    FragmentSettings fsettings;

    FragmentTransaction ftrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ftrans = getFragmentManager().beginTransaction();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fschedule = new FragmentSchedule();
        fbells = new FragmentBells();
        fsettings = new FragmentSettings();

        ftrans.replace(R.id.content_main,fschedule);
        ftrans.commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        ftrans = getFragmentManager().beginTransaction();


        if (id == R.id.schedule) {
            ftrans.replace(R.id.content_main,fschedule);
        } else if (id == R.id.bells) {
            ftrans.replace(R.id.content_main,fbells);
        } else if (id == R.id.settings) {
            ftrans.replace(R.id.content_main,fsettings);
        }
       // ftrans.addToBackStack(null);
        ftrans.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void OpenScheduleActivity(int year, int month, int day, final String code, final Boolean today) {
        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String date = String.format(Locale.getDefault(), "%d-%d-%d", year, month, day);
        String baseUrl = getString(R.string.url);
        final SharedPreferences sPref = this.getPreferences(MODE_PRIVATE);
        String act = Integer.toString(sPref.getInt("act", 1));
        String url = baseUrl + "?action=getSchedule&act=" + act + "&code=" + code + "&date=" + date;

        final Intent intent = new Intent(this, ScheduleActivity.class);

        String savedDate = sPref.getString("savedDate", "");
        String savedCode = sPref.getString("savedCode","");
        if (savedDate.equals(date)&&savedCode.equals(code)) {
            String savedRasp = sPref.getString("savedRasp","");

            try {
                JSONArray jsonRasp = new JSONArray(savedRasp);
                ArrayList<Lesson> lessons = JsonToLessonsArray(jsonRasp);
                intent.putParcelableArrayListExtra("schedule", lessons);
                intent.putExtra("date", date);
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {

            final ProgressDialog loadingDialog = new ProgressDialog(this);

            loadingDialog.setMessage("Расписание загружается...");
            loadingDialog.setCancelable(true);
            loadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    queue.cancelAll("GET_SCHEDULE");
                }
            });

            loadingDialog.show();

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("response",response);
                            loadingDialog.dismiss();
                            if (!response.equals("NO_SCHEDULE")) {
                                try {
                                    JSONArray responseJson = new JSONArray(response);
                                    ArrayList<Lesson> lessons = JsonToLessonsArray(responseJson);
                                    intent.putParcelableArrayListExtra("schedule", lessons);
                                    intent.putExtra("date", date);
                                    if (today){
                                        Log.d("today!","today!");
                                        SharedPreferences.Editor editor = sPref.edit();
                                        editor.putString("savedDate",date);
                                        editor.putString("savedRasp",response);
                                        editor.putString("savedCode",code);
                                        editor.apply();
                                    }
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e("e", e.toString());
                                    showError();
                                }
                            } else {
                                showError("Расписание отсутствует");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loadingDialog.dismiss();
                    showError("Ошибка соединения с сервером");
                }
            });

            stringRequest.setTag("GET_SCHEDULE");
            queue.add(stringRequest);
        }
    }
    @Override
    public void OnDateSelected(Calendar calendar, String code, Boolean today) {
        int day = calendar.get(Calendar.DATE);
        int month =calendar.get(Calendar.MONTH)+1;
        int year = calendar.get(Calendar.YEAR);
        OpenScheduleActivity(year,month,day, code,today);
    }
    @Override
    public void OnDateSelected(Calendar calendar, String code) {
        OnDateSelected(calendar,code,false);
    }

    @Override
    public void OnDateSelected(int year, int month, int day, String code) {
        OpenScheduleActivity(year,month+1,day, code,false);
    }
    @Override
    public void OnCalendarOpened(String code) {
        DatePickerFragment calendarFragment = new DatePickerFragment();
        calendarFragment.code = code;
        calendarFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void showError(String text){
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
    }
    private void showError(){
        showError("Произошла ошибка");
    }

    private ArrayList<Lesson> JsonToLessonsArray(JSONArray json){
        ArrayList<Lesson> array = new ArrayList<Lesson>();
        for (int i=0;i!=json.length();i++){
            try {
                String num = json.getJSONObject(i).getString("num");
                String lesson = json.getJSONObject(i).getString("lesson");
                array.add(i, new Lesson(num,lesson));
            } catch (JSONException e) {
                Log.e("e",e.toString());
                e.printStackTrace();
            }
        }
        return  array;
    }

    @Override
    public void onUpdateLinksClick() {
        final RequestQueue queue = Volley.newRequestQueue(this);
        String baseUrl = getString(R.string.url);
        String url = baseUrl+"?action=getLinks";


        final ProgressDialog loadingDialog = new ProgressDialog(this);

        loadingDialog.setMessage("Загружается список групп...");
        loadingDialog.setCancelable(true);
        loadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                queue.cancelAll("GET_LINKS");
            }
        });

        loadingDialog.show();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loadingDialog.dismiss();
                        if (!response.equals("NO_SCHEDULE")){
                            try {
                                JSONObject responseJson = new JSONObject(response);
                                LinksToDatabase(responseJson);
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("e",e.toString());
                                showError();
                            }
                        } else {
                            showError("Расписание отсутствует");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dismiss();
                showError("Ошибка соединения с сервером");
            }
        });

        stringRequest.setTag("GET_LINKS");
        queue.add(stringRequest);

    }
    private void LinksToDatabase(JSONObject json){
        LinksList ll = new LinksList(this);
        try {
            String[] tables = {"groups","teachers"};
            for (String table: tables) {
                JSONArray array = json.getJSONArray(table);
                for (int i=0;i!=array.length();i++){
                    JSONObject row = array.getJSONObject(i);
                    ll.addNameCodePair(table,row.getString("name"),row.getString("code"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
