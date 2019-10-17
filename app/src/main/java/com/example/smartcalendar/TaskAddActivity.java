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

    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;

    private Account account;

    StringBuilder timet =new StringBuilder();
    StringBuilder datet=new StringBuilder();
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
        timePick=findViewById((R.id.pickTime));
        datePick=findViewById(R.id.pickDate);

        timePick.setOnClickListener(this);
        datePick.setOnClickListener(this);

        addTaskButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String timeString ="\0";
        String dateString="\0";

        if(view == addTaskButton) {
            String name   = taskName.getText().toString().trim();
            if(!TextUtils.isEmpty(name)) {
                Task task = new Task(timeString,dateString,name);

                databaseReference.child(user.getUid()).child("tasks/task" + (account.getTaskcount() + 1)).setValue(task);
                databaseReference.child(user.getUid()).child("taskcount").setValue(account.getTaskcount() + 1);
                Toast.makeText(this, "Task added & saved", Toast.LENGTH_LONG).show();

                finish();
            }
        }
        if(view== timePick){

            TimePicker timePicker = new TimePicker(this);
            timet.replace(0,timet.length(),"\0");
            // timeString=timePicker.getCurrentHour().toString()+":"+timePicker.getCurrentMinute().toString();

            //  timet.append(timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute());

            timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            if(hourOfDay<12) timet.append("AM");
                            else timet.append("PM");

                            timePick.setText(timet.toString());

                        }
                    }, timePicker.getCurrentHour(), timePicker.getCurrentMinute(), false);

            timePickerDialog.show();
            timeString=timet.toString();
        }
        if(view==datePick){
            DatePicker datePicker = new DatePicker(this);
            int SelectedDay = datePicker.getDayOfMonth();
            int SelectedMonth = datePicker.getMonth();
            int SelectedYear = datePicker.getYear();

            datePickerDialog = new DatePickerDialog(this,

                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            datePick.setText(  "  " + dayOfMonth + "/" + (month+1 ) + "/" + year);
                            datet.append(Integer.toString(dayOfMonth)+"/"+Integer.toString(month+1)+"/"+Integer.toString(year));
                        }
                    }, SelectedYear, SelectedMonth, SelectedDay);


            datePickerDialog.show();
            dateString= datet.toString();
        }







    }

}

