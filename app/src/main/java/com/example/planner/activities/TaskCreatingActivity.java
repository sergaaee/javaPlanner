package com.example.planner.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.planner.R;
import com.example.planner.api.Tasks;

import java.time.LocalDateTime;
import java.util.Calendar;

public class TaskCreatingActivity extends AppCompatActivity {

    private int year, month, day, hour, minutes, yearE, monthE, dayE, hourE, minutesE;
    private String dateTime = "";
    private String dateTimeE = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_task_creating);
        if (ActivityCompat.checkSelfPermission(TaskCreatingActivity.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT > 32) {
                if (!shouldShowRequestPermissionRationale("112")){
                    getNotificationPermission();
                }
            }
        }

        InputMethodManager imm = (InputMethodManager) getSystemService(
                Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

    }

    public void getNotificationPermission(){
        try {
            if (Build.VERSION.SDK_INT > 32) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        112);
            }
        }catch (Exception ignored){

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 112) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(TaskCreatingActivity.this, "Success", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(TaskCreatingActivity.this, "You can enable notifications in app's settings.", Toast.LENGTH_LONG).show();
            }
        }

    }

    public void backToMainPage(View view) {

        Intent intent = new Intent(TaskCreatingActivity.this,
                MainPageActivity.class);
        startActivity(intent);

    }

    public void setSTimeData(View view) {

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view1, year, monthOfYear, dayOfMonth) -> {

                    dateTime = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    //*************Call Time Picker Here ********************
                    timePicker();
                }, year, month, day);
        datePickerDialog.show();
    }

    private void timePicker(){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minutes = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        @SuppressLint("SetTextI18n") TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {

                    hour = hourOfDay;
                    minutes = minute;
                    EditText sTimeField = findViewById(R.id.editTextTaskSTime);
                    sTimeField.setText(dateTime + " " + hour + ":" + minutes);
                }, hour, minutes, false);
        timePickerDialog.show();
    }

    public void setETimeData(View view) {

        final Calendar c = Calendar.getInstance();
        yearE = c.get(Calendar.YEAR);
        monthE = c.get(Calendar.MONTH);
        dayE = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view1, year, monthOfYear, dayOfMonth) -> {

                    dateTimeE = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    //************* Call Time Picker Here ********************
                    timePickerE();
                }, yearE, monthE, dayE);
        datePickerDialog.show();
    }

    private void timePickerE(){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        hourE = c.get(Calendar.HOUR_OF_DAY);
        minutesE = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        @SuppressLint("SetTextI18n") TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {

                    hourE = hourOfDay;
                    minutesE = minute;
                    EditText eTimeField = findViewById(R.id.editTextTaskETime);
                    eTimeField.setText(dateTimeE + " " + hourE + ":" + minutesE);
                }, hourE, minutesE, true);
        timePickerDialog.show();
    }

    public void createTask(View view) {

        EditText name = findViewById(R.id.editTextTaskName);
        String nameTask = name.getText().toString();
        EditText desc = findViewById(R.id.editTextTaskDesc);
        String descTask = desc.getText().toString();
        String sTime = dateTime + "T" + hour + ":" + minutes;
        String eTime = dateTimeE + "T" + hourE + ":" + minutesE;
        Spinner status = findViewById(R.id.spinnerStatus);
        String statusTask = status.getSelectedItem().toString();
        SharedPreferences mPrefs = getSharedPreferences("AuthActivity", MODE_PRIVATE);
        String token = mPrefs.getString("access_token", "None");
        String response = Tasks.taskNew(token, nameTask, sTime, eTime, descTask, statusTask, LocalDateTime.now().toString());
        if (response.equals("Success")) {
            Intent intent = new Intent(TaskCreatingActivity.this,
                    MainPageActivity.class);
            startActivity(intent);
            Toast successToast = Toast.makeText(TaskCreatingActivity.this, getString(R.string.successfully_added), Toast.LENGTH_LONG);
            successToast.show();
        }
        else {
            Toast errorToast = Toast.makeText(TaskCreatingActivity.this, getString(R.string.name_already_exists), Toast.LENGTH_LONG);
            errorToast.show();
        }
    }
}