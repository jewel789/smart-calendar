package com.example.smartcalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.agik.AGIKSwipeButton.Controller.OnSwipeCompleteListener;
import com.agik.AGIKSwipeButton.View.Swipe_Button_View;

public class ActivityAlarm extends AppCompatActivity {

    Swipe_Button_View swipe_button_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        swipe_button_view = findViewById(R.id.start);

        swipe_button_view.setOnSwipeCompleteListener_forward_reverse(new OnSwipeCompleteListener() {
            @Override
            public void onSwipe_Forward(Swipe_Button_View swipe_button_view) {
                AudioPlay.stopAudio();
                Toast.makeText(ActivityAlarm.this, "Alarm Turn Off", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onSwipe_Reverse(Swipe_Button_View swipe_button_view) {

            }
        });
    }
}
