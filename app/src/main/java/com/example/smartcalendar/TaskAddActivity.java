package com.example.smartcalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TaskAddActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference databaseReference;

    private EditText taskName;
    private Button addTaskButton;
    private TextView datePick,timePick;

    private Account account;

    private StringBuilder timeT = new StringBuilder();
    private StringBuilder dateT = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_add);

        account = (Account) getIntent().getSerializableExtra("account");

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        taskName = findViewById(R.id.taskName);
        addTaskButton = findViewById(R.id.addTaskButton);
        timePick = findViewById((R.id.pickTime));
        datePick = findViewById(R.id.pickDate);

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

            timeT.replace(0, timeT.length(),"\0");

            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            int hour = hourOfDay;
                            if(hour > 12) hour = hour - 12;
                            if(hour == 0) hour = 12;

                            if(hour < 10) timeT.append("0");
                            timeT.append(hour).append(":");

                            if(minute < 10) timeT.append("0");
                            timeT.append(minute);

                            if(hourOfDay < 12) timeT.append(" AM");
                            else timeT.append(" PM");

                            timePick.setText(timeT.toString());
                        }
                    }, timePicker.getCurrentHour(), timePicker.getCurrentMinute(), false);

            timePickerDialog.show();
        }

        if(view == datePick){
            DatePicker datePicker = new DatePicker(this);
            int SelectedDay = datePicker.getDayOfMonth();
            int SelectedMonth = datePicker.getMonth();
            int SelectedYear = datePicker.getYear();

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
            if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(dateString) && !TextUtils.isEmpty(timeString)) {

                Task task = new Task(timeString,dateString,name);

                databaseReference.child(user.getUid()).child("tasks/task" + (account.getTaskcount() + 1)).setValue(task);
                databaseReference.child(user.getUid()).child("taskcount").setValue(account.getTaskcount() + 1);
                Toast.makeText(this, "Task added & saved", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}

