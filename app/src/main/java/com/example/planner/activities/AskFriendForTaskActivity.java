package com.example.planner.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.planner.R;
import com.example.planner.api.Friends;
import com.example.planner.api.Tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Calendar;

public class AskFriendForTaskActivity extends AppCompatActivity {

    private int year, month, day, yearE, monthE, dayE;
    private String hour, minutes, hourE, minutesE;
    private String dateTime = "";
    private String dateTimeE = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ask_friend_for_task);
    }

    public void setSTimeData(View view) {

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view1, year, monthOfYear, dayOfMonth) -> {
                    if (monthOfYear + 1 < 10) {
                        if (dayOfMonth < 10) {
                            dateTime = year + "-0" + (monthOfYear + 1) + "-0" + dayOfMonth;
                        } else {
                            dateTime = year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth;
                        }
                    } else {
                        if (dayOfMonth < 10) {
                            dateTime = year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth;
                        } else {
                            dateTime = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        }
                    }
                    //*************Call Time Picker Here ********************
                    timePicker();
                }, year, month, day);
        datePickerDialog.show();
    }

    private void timePicker() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        minutes = String.valueOf(c.get(Calendar.MINUTE));

        // Launch Time Picker Dialog
        @SuppressLint("SetTextI18n") TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    if (hourOfDay < 10) {
                        hour = "0" + hourOfDay;
                    } else {
                        hour = String.valueOf(hourOfDay);
                    }
                    if (minute < 10) {
                        minutes = "0" + minute;
                    } else {
                        minutes = String.valueOf(minute);
                    }
                    EditText sTimeField = findViewById(R.id.editTextTaskSTime);
                    sTimeField.setText(dateTime + " " + hour + ":" + minutes);
                }, Integer.parseInt(hour), Integer.parseInt(minutes), false);
        timePickerDialog.show();
    }

    public void setETimeData(View view) {

        final Calendar c = Calendar.getInstance();
        yearE = c.get(Calendar.YEAR);
        monthE = c.get(Calendar.MONTH);
        dayE = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view1, year, monthOfYear, dayOfMonth) -> {

                    if (monthOfYear + 1 < 10) {
                        if (dayOfMonth < 10) {
                            dateTimeE = year + "-0" + (monthOfYear + 1) + "-0" + dayOfMonth;
                        } else {
                            dateTimeE = year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth;
                        }
                    } else {
                        if (dayOfMonth < 10) {
                            dateTimeE = year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth;
                        } else {
                            dateTimeE = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        }
                    }
                    //************* Call Time Picker Here ********************
                    timePickerE();
                }, yearE, monthE, dayE);
        datePickerDialog.show();
    }

    private void timePickerE() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        hourE = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        minutesE = String.valueOf(c.get(Calendar.MINUTE));

        // Launch Time Picker Dialog
        @SuppressLint("SetTextI18n") TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {

                    if (hourOfDay < 10) {
                        hourE = "0" + hourOfDay;
                    } else {
                        hourE = String.valueOf(hourOfDay);
                    }
                    if (minute < 10) {
                        minutesE = "0" + minute;
                    } else {
                        minutesE = String.valueOf(minute);
                    }
                    EditText eTimeField = findViewById(R.id.editTextTaskETime);
                    eTimeField.setText(dateTime + " " + hourE + ":" + minutesE);
                }, Integer.parseInt(hourE), Integer.parseInt(minutesE), false);
        timePickerDialog.show();
    }

    public void createTaskWithFriend(View view) {
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch add = findViewById(R.id.switchAdd);
        EditText sTimeField = findViewById(R.id.editTextTaskSTime);
        EditText eTimeField = findViewById(R.id.editTextTaskETime);
        EditText name = findViewById(R.id.editTextTaskName);
        String nameTask = name.getText().toString();
        EditText desc = findViewById(R.id.editTextTaskDesc);
        String descTask = desc.getText().toString();
        DateTimeFormatter df = new DateTimeFormatterBuilder().appendPattern("uuuu-MM-dd HH:mm").toFormatter();
        String sTime = "";
        String eTime = "";
        String statusTask = "";
        String token = getSharedPreferences("activities.AuthActivity", MODE_PRIVATE).getString("access_token", "null");
        if (dateTime.length() < 2 || LocalDateTime.parse(dateTime + " " + hour + ":" + minutes, df).isBefore(LocalDateTime.now())) {
            sTimeField.setError(getString(R.string.incorrect_time));
        } else if (dateTimeE.length() < 2 || LocalDateTime.parse(dateTimeE + " " + hourE + ":" + minutesE, df).isBefore(LocalDateTime.now())) {
            eTimeField.setError(getString(R.string.incorrect_time));
        } else if (LocalDateTime.parse(dateTime + " " + hour + ":" + minutes, df).isAfter(LocalDateTime.parse(dateTimeE + " " + hourE + ":" + minutesE, df))) {
            sTimeField.setError(getString(R.string.incorrect_time));
            eTimeField.setError(getString(R.string.incorrect_time));
        } else {
            sTime = dateTime + "T" + hour + ":" + minutes;
            eTime = dateTimeE + "T" + hourE + ":" + minutesE;
            Spinner status = findViewById(R.id.spinnerStatus);
            statusTask = status.getSelectedItem().toString();
        }
        Bundle extras = getIntent().getExtras();
        if (add.isChecked()) {
            String response = Tasks.taskNew(token, nameTask, sTime, eTime, descTask, statusTask, LocalDateTime.now().toString(), extras.getString("friend_id"));
            if (response.equals("Success")) {
                Toast successToast = Toast.makeText(AskFriendForTaskActivity.this, getString(R.string.successfully_added), Toast.LENGTH_LONG);
                successToast.show();
            } else {
                Toast errorToast = Toast.makeText(AskFriendForTaskActivity.this, getString(R.string.name_already_exists), Toast.LENGTH_LONG);
                errorToast.show();
            }
        }
        String response = Friends.addTaskToFriend(token, extras.getString("friend_id"), nameTask, sTime, eTime, descTask, statusTask, LocalDateTime.now().toString());
        if (response.equals("Success")) {
            Intent intent = new Intent(AskFriendForTaskActivity.this,
                    MainPageActivity.class);
            startActivity(intent);
            Toast successToast = Toast.makeText(AskFriendForTaskActivity.this, getString(R.string.successfully_added), Toast.LENGTH_LONG);
            successToast.show();
        } else {
            Toast errorToast = Toast.makeText(AskFriendForTaskActivity.this, getString(R.string.name_already_exists), Toast.LENGTH_LONG);
            errorToast.show();
        }
    }

    public void backToMainPage(View view) {
        startActivity(new Intent(AskFriendForTaskActivity.this, MainPageActivity.class));
    }
}
