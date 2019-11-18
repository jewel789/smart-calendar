package com.example.smartcalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class EditTaskActivity extends AppCompatActivity implements View.OnClickListener {

    private int taskPosition;
    private Task task;
    private Account account;

    private TextView taskEditPage;
    private TextView showTaskName;
    private TextView showTaskTime;

    private DatabaseReference dbReference;

    private EditText reNameTask;
    private Button saveTaskButton;
    private Button deleteButton;
    private TextView rePickDate, rePickTime;

    private StringBuilder timeT = new StringBuilder();
    private StringBuilder dateT = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        account = (Account) getIntent().getSerializableExtra("account");
        taskPosition = getIntent().getIntExtra("taskPos", 0);

        task = account.getAllTasks().get(taskPosition);

        taskEditPage = findViewById(R.id.taskEditPage);
        showTaskName = findViewById(R.id.showTaskName);
        showTaskTime = findViewById(R.id.showTaskTime);

        reNameTask = findViewById(R.id.reNameTask);
        rePickTime = findViewById((R.id.rePickTime));
        rePickDate = findViewById(R.id.rePickDate);

        saveTaskButton = findViewById(R.id.saveTaskButton);
        deleteButton = findViewById(R.id.deleteTaskButton);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        dbReference = FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(user).getUid());

        rePickTime.setOnClickListener(this);
        rePickDate.setOnClickListener(this);
        saveTaskButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        
        showInfo();
    }

    @Override
    public void onClick(View view) {

        if(view == rePickTime){
            chooseTime();
        }

        if(view == rePickDate){
            chooseDate();
        }

        if(view == saveTaskButton) {
            saveData();
        }

        if(view == deleteButton) {
            deleteDB();
        }
    }

    private void chooseTime() {
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
                            rePickTime.setText(sdf2.format(Objects.requireNonNull(sdf.parse(timeT.toString()))));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, timePicker.getCurrentHour(), timePicker.getCurrentMinute(), false);

        timePickerDialog.show();
    }

    private void chooseDate() {
        DatePicker datePicker = new DatePicker(this);
        int SelectedDay = datePicker.getDayOfMonth();
        int SelectedMonth = datePicker.getMonth();
        int SelectedYear = datePicker.getYear();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,

                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateT.append(dayOfMonth).append("/").append(month + 1).append("/").append(year);
                        rePickDate.setText(dateT.toString());
                    }
                }, SelectedYear, SelectedMonth, SelectedDay);


        datePickerDialog.show();
    }

    private void deleteDB() {
        Toast.makeText(this, "Task Deleted", Toast.LENGTH_SHORT).show();
        account.getAllTasks().remove(taskPosition);
        dbReference.setValue(account);
        onBackPressed();
    }

    private void saveData() {
        String name = reNameTask.getText().toString().trim();
        String timeString = timeT.toString();
        String dateString = dateT.toString();

        if(!TextUtils.isEmpty(name)) {
            task.setName(name);
            Toast.makeText(this, "Task Name Updated", Toast.LENGTH_SHORT).show();
            dbReference.setValue(account);
        }

        if(!TextUtils.isEmpty(dateString) && !TextUtils.isEmpty(timeString)) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault());
            Date date = null;
            try {
                date = sdf.parse(dateString + " " + timeString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            task.setDate(date);
            Collections.sort(account.getAllTasks(), new dateCmp());
            for(int i = 0; i < account.getAllTasks().size(); i++) {
                if(account.getAllTasks().get(i).equals(task)) {
                    taskPosition = i;
                }
            }
            Toast.makeText(this, "Task Date & Time Updated", Toast.LENGTH_SHORT).show();
            dbReference.setValue(account);
        }

        showInfo();
    }

    @SuppressLint("SetTextI18n")
    private void showInfo() {
        taskEditPage.setText("Task No #" + (taskPosition + 1));
        showTaskName.setText("Task Name : " + task.getName());
        showTaskTime.setText("Task Scheduled at : " + task.getTime());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ShowTasksActivity.class);
        intent.putExtra("account", account);
        startActivity(intent);
        finish();
    }
}
