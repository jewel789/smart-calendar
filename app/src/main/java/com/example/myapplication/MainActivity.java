package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

   Date today = Calendar.getInstance().getTime();

      TextView DateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       DateView=(TextView)findViewById(R.id.CurrentDate);

       String date=today.toString();
       String Day=date.substring(0,3);


       DateView.setText(date.substring(11,20)+"\n"+date.substring(0,3)+" " +date.substring(8,10)+ " " +date.substring(4,8)+" "+date.substring(30,34));


    }
}
