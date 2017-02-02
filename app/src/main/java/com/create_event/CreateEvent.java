package com.create_event;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;



public class CreateEvent extends AppCompatActivity implements OnClickListener {

    private EditText event_name;
    private EditText event_location;
    private EditText event_date;
    private EditText event_time;
    private EditText event_description;

    private static final int REQUEST_CODE_Maps_Activity = 1;

    private Button location;
    private Button time_button;
    private DatePickerDialog event_date_PickerDialog;
    private SimpleDateFormat dateFormatter;
    private Button date_button;
    private int CalendarHour, CalendarMinute;
    private String format;
    private Calendar calendarForTime;
    private TimePickerDialog timepickerdialog;
    private MaterialBetterSpinner materialBetterSpinner ;

    String[] SPINNER_DATA = {"Social Welfare","Education","Food","Environment"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);


        materialBetterSpinner = (MaterialBetterSpinner)findViewById(R.id.material_spinner1);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateEvent.this, android.R.layout.simple_dropdown_item_1line, SPINNER_DATA);

        materialBetterSpinner.setAdapter(adapter);




        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        //find views
        event_name = (EditText)findViewById(R.id.event_name_id);
        event_location = (EditText)findViewById(R.id.event_loaction_id);
        event_description = (EditText)findViewById(R.id.event_description_id);

        event_location.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMapActivity();
            }
        });
        location = (Button)findViewById(R.id.location_button_id);
        location.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMapActivity();
            }
        });

        event_date = (EditText)findViewById(R.id.event_date_id);
        event_date.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setDateTimeField();
                event_date_PickerDialog.show();
            }
        });



        date_button = (Button)findViewById(R.id.date_button_id);
        date_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setDateTimeField();
                event_date_PickerDialog.show();
            }
        });

        event_time=(EditText)findViewById(R.id.event_time_id);
        time_button=(Button)findViewById(R.id.time_button_id);



        //listeners
        event_time.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setTimeDialog();
            }
        });
        time_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setTimeDialog();
            }
        });
        getSupportActionBar().setTitle("Create Event");

        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    private void launchMapActivity() {
        Intent mapActivity = new Intent(this,MapsActivity.class);
        this.startActivityForResult(mapActivity, REQUEST_CODE_Maps_Activity); //starting map activity for result
    }

    private void setTimeDialog() {
        calendarForTime = Calendar.getInstance();
        CalendarHour = calendarForTime.get(Calendar.HOUR_OF_DAY);
        CalendarMinute = calendarForTime.get(Calendar.MINUTE);

        timepickerdialog = new TimePickerDialog(CreateEvent.this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        if (hourOfDay == 0) {

                            hourOfDay += 12;

                            format = "AM";
                        } else if (hourOfDay == 12) {

                            format = "PM";

                        } else if (hourOfDay > 12) {

                            hourOfDay -= 12;

                            format = "PM";

                        } else {

                            format = "AM";
                        }

                        event_time.setText(hourOfDay + ":" + minute +" "+ format);

                    }
                }, CalendarHour, CalendarMinute, false);
        timepickerdialog.show();

    }


    private void setDateTimeField() {
        final Calendar newCalendar = Calendar.getInstance();
        event_date_PickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                String[] days = new String[] {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };

                int dayOfWeek = newDate.get(Calendar.DAY_OF_WEEK);
                String day = days[dayOfWeek-1];
                event_date.setText(day+", "+dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
 }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id)
        {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                Toast.makeText(this,"Cannot go back",Toast.LENGTH_LONG).show();
                return true;
        }

        return super.onOptionsItemSelected(item);


    }
    public void done(MenuItem item) {
        Toast.makeText(this, "Event Created Successfully", Toast.LENGTH_LONG).show();

    }


    @Override
    public void onClick(View view) {

    }

    //save status before calling map activity
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putString("event_name", event_name.getText().toString());
        savedInstanceState.putString("event_location", event_location.getText().toString());
        savedInstanceState.putString("event_date", event_date.getText().toString());
        savedInstanceState.putString("event_time", event_time.getText().toString());
        savedInstanceState.putString("event_desc", event_description.getText().toString());
    }
    //retrieve status when this activity called again
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        event_name.setText(savedInstanceState.getString("event_name"));
        event_location.setText(savedInstanceState.getString("event_location"));
        event_date.setText(savedInstanceState.getString("event_date"));
        event_time.setText(savedInstanceState.getString("event_time"));
        event_description.setText(savedInstanceState.getString("event_desc"));
    }

    //returning from MapsActivity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (REQUEST_CODE_Maps_Activity) : {
                if (resultCode == MapsActivity.RESULT_OK) {
                    String event_address = data.getStringExtra("event_address");
                    event_location.setText(event_address);  //setting address to event_location EditText
                }
                break;
            }
        }
    }
}
