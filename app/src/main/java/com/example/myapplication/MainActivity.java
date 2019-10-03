package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

   //Date today = Calendar.getInstance().getTime();
     private Button loginButton;
     private  TextView DateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         loginButton=(Button) findViewById(R.id.logIn_button);

         loginButton.setOnClickListener(this);
    /*   DateView=(TextView)findViewById(R.id.CurrentDate);

       String date=today.toString();
       String Day=date.substring(0,3);


       DateView.setText(date.substring(11,20)+"\n"+date.substring(0,3)+" " +date.substring(8,10)+ " " +date.substring(4,8)+" "+date.substring(30,34));

  */
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.logIn_button){
            setContentView(R.layout.calendar_layout);
        }

    }
}
