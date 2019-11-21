package com.example.smartcalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class TaskAddActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseUser user;
    private DatabaseReference databaseReference;

    private EditText taskName;
    private Button addTaskButton;
    private TextView datePick, timePick;

    private Switch aSwitch;

    private RadioGroup radioGroup;

    private Account account;

    private StringBuilder timeT = new StringBuilder();
    private StringBuilder dateT = new StringBuilder();

    public static final long HOUR = 3600*1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_add);

        account = (Account) getIntent().getSerializableExtra("account");

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        taskName = findViewById(R.id.taskName);
        addTaskButton = findViewById(R.id.saveTaskButton);
        timePick = findViewById(R.id.pickTime);
        datePick = findViewById(R.id.pickDate);
        aSwitch = findViewById(R.id.alarmSwitch);
        radioGroup = findViewById(R.id.radioGroup);

        timePick.setOnClickListener(this);
        datePick.setOnClickListener(this);

        addTaskButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        String timeString;
        String dateString;

        if(view == timePick){

            TimePicker timePicker = new TimePicker(this);

            timeT = new StringBuilder();

            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            timeT.append(hourOfDay).append(":").append(minute);
                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.US);
                            SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a", Locale.US);
                            try {
                                timePick.setText(sdf2.format(Objects.requireNonNull(sdf.parse(timeT.toString()))));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, timePicker.getCurrentHour(), timePicker.getCurrentMinute(), false);

            timePickerDialog.show();
        }

        if(view == datePick){
            DatePicker datePicker = new DatePicker(this);
            int SelectedDay = datePicker.getDayOfMonth();
            int SelectedMonth = datePicker.getMonth();
            int SelectedYear = datePicker.getYear();

            dateT = new StringBuilder();

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,

                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            dateT.append(dayOfMonth).append("/").append(month + 1).append("/").append(year);
                            datePick.setText(dateT.toString());
                        }
                    }, SelectedYear, SelectedMonth, SelectedDay);

            datePickerDialog.show();
        }

        if(view == addTaskButton) {
            String name = taskName.getText().toString().trim();
            timeString = timeT.toString();
            dateString = dateT.toString();
            RadioButton radioButton = findViewById(radioGroup.getCheckedRadioButtonId());

            if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(dateString) && !TextUtils.isEmpty(timeString)) {

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault());
                Date date = null;
                try {
                    date = sdf.parse(dateString + " " + timeString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Task task = new Task(name, date, radioButton.getText().toString());

                task.setAlarm(aSwitch.isChecked());     //alarm
                account.addTask(task);
                setAlarm(task);
                for(int i = 1; i < 5; i++) {
                    if(!task.getRepeat().equals("None")) {
                        Date newDate = null;
                        if(task.getRepeat().equals("Daily")) newDate = new Date(task.getDate().getTime() + i * 24 * HOUR);
                        else if(task.getRepeat().equals("Weekly")) newDate = new Date(task.getDate().getTime() + i * 24 * 7 * HOUR);
                        Task newTask = new Task(task.getName(), newDate, task.getRepeat());
                        account.addTask(newTask);
                        setAlarm(newTask);
                    }
                }

		        databaseReference.child(user.getUid()).setValue(account);
                Toast.makeText(this, "Task Added Successfully", Toast.LENGTH_SHORT).show();
		        finish();
            }
        }
    }

    private void setAlarm(Task task) {
        if(task.isAlarm()){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(task.getDate());

            Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
            AlarmManager alarmManager  = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if(alarmManager != null) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            }
        }
    }
}
