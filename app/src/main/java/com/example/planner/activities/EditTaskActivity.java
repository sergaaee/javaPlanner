package com.example.planner.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.planner.R;
import com.example.planner.api.Tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Locale;

public class EditTaskActivity extends AppCompatActivity {

    String prev_name;
    private int year, month, day,  yearE, monthE, dayE;
    private String hour, minutes,hourE, minutesE ;
    private String dateTime = "";
    private String dateTimeOld = "";
    private String dateTimeEOld = "";
    private String dateTimeE = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_task);
        Bundle extras = getIntent().getExtras();
        String taskName = extras.getString("name");
        EditText name = findViewById(R.id.editTextNewTaskName);
        name.setText(taskName);
        String taskSTime = extras.getString("stime");
        EditText sTime = findViewById(R.id.editTextNewTaskSTime);
        sTime.setText(taskSTime);
        String taskETime = extras.getString("etime");
        EditText eTime = findViewById(R.id.editTextNewTaskETime);
        eTime.setText(taskETime);
        String taskDesc = extras.getString("desc");
        EditText desc = findViewById(R.id.editTextNewTaskDesc);
        desc.setText(taskDesc);
        dateTimeOld = taskSTime;
        dateTimeEOld = taskETime;
        prev_name = taskName;

    }


    public void setNewSTimeData(View view) {

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view1, year, monthOfYear, dayOfMonth) -> {
                    if (monthOfYear + 1 < 10){
                        if (dayOfMonth < 10){
                            dateTime = year + "-0" + (monthOfYear + 1) + "-0" + dayOfMonth;
                        }
                        else{
                            dateTime = year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth;
                        }
                    }
                    else {
                        if (dayOfMonth < 10){
                            dateTime = year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth;
                        }
                        else {
                            dateTime = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        }
                    }
                    //*************Call Time Picker Here ********************
                    timePicker();
                }, year, month, day);
        datePickerDialog.show();
    }

    private void timePicker(){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        minutes = String.valueOf(c.get(Calendar.MINUTE));

        // Launch Time Picker Dialog
        @SuppressLint("SetTextI18n") TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    if (hourOfDay < 10){
                        hour = "0" + hourOfDay;
                    }
                    else {
                        hour = String.valueOf(hourOfDay);
                    }
                    if (minute < 10){
                        minutes = "0" + minute;
                    }
                    else {
                        minutes = String.valueOf(minute);
                    }
                    EditText sTimeField = findViewById(R.id.editTextNewTaskSTime);
                    sTimeField.setText(dateTime + " " + hour + ":" + minutes);
                }, Integer.parseInt(hour), Integer.parseInt(minutes), false);
        timePickerDialog.show();
    }

    public void setNewETimeData(View view) {

        final Calendar c = Calendar.getInstance();
        yearE = c.get(Calendar.YEAR);
        monthE = c.get(Calendar.MONTH);
        dayE = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view1, year, monthOfYear, dayOfMonth) -> {

                    if (monthOfYear + 1 < 10){
                        if (dayOfMonth < 10){
                            dateTimeE = year + "-0" + (monthOfYear + 1) + "-0" + dayOfMonth;
                        }
                        else{
                            dateTimeE = year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth;
                        }
                    }
                    else {
                        if (dayOfMonth < 10){
                            dateTimeE = year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth;
                        }
                        else {
                            dateTimeE = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        }
                    }
                    //************* Call Time Picker Here ********************
                    timePickerE();
                }, yearE, monthE, dayE);
        datePickerDialog.show();
    }

    private void timePickerE(){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        hourE = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        minutesE = String.valueOf(c.get(Calendar.MINUTE));

        // Launch Time Picker Dialog
        @SuppressLint("SetTextI18n") TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {

                    if (hourOfDay < 10){
                        hourE = "0" + hourOfDay;
                    }
                    else {
                        hourE = String.valueOf(hourOfDay);
                    }
                    if (minute < 10){
                        minutesE = "0" + minute;
                    }
                    else {
                        minutesE = String.valueOf(minute);
                    }
                    EditText eTimeField = findViewById(R.id.editTextNewTaskETime);
                    eTimeField.setText(dateTimeE + " " + hourE + ":" + minutesE);
                }, Integer.parseInt(hourE), Integer.parseInt(minutesE), false);
        timePickerDialog.show();
    }


    public void updateTask(View view){
        EditText nameEdit = findViewById(R.id.editTextNewTaskName);
        String newName = nameEdit.getText().toString();
        EditText sTimeEdit = findViewById(R.id.editTextNewTaskSTime);
        String newSTime = sTimeEdit.getText().toString();
        EditText eTimeEdit = findViewById(R.id.editTextNewTaskETime);
        String newETime = eTimeEdit.getText().toString();
        EditText descEdit = findViewById(R.id.editTextNewTaskDesc);
        String newDesc = descEdit.getText().toString();
        Spinner status = findViewById(R.id.spinnerStatusEdit);
        String newStatus = status.getSelectedItem().toString();
        DateTimeFormatter df = new DateTimeFormatterBuilder().appendPattern("uuuu-MM-dd HH:mm").toFormatter();
        LocalDateTime now = LocalDateTime.now();
        if (!dateTimeOld.equals(newSTime)) {
            boolean dateTimeSNewBeforeNow = LocalDateTime.parse(dateTime + " " + hour + ":" + minutes, df).isBefore(now);
            if (dateTimeSNewBeforeNow) {
                dateTimeWarning(newSTime, newETime, newName, newStatus, newDesc);
            } else {
                boolean dateTimeENewBeforeNow;
                boolean sBeforeE;

                if (!dateTimeEOld.equals(newETime)) {
                    dateTimeENewBeforeNow = LocalDateTime.parse(dateTimeE + " " + hourE + ":" + minutesE, df).isBefore(now);
                    if (dateTimeENewBeforeNow) {
                        dateTimeWarning(newSTime, newETime, newName, newStatus, newDesc);
                    } else if (LocalDateTime.parse(dateTime + " " + hour + ":" + minutes, df).isAfter(LocalDateTime.parse(dateTimeE + " " + hourE + ":" + minutesE, df))) {
                        sBeforeE = true;
                        if (sBeforeE) {
                            dateTimeWarning(newSTime, newETime, newName, newStatus, newDesc);
                        }
                    } else {
                        updateTaskApi(newSTime, newETime, newName, newStatus, newDesc);
                    }
                } else {
                    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MMMM d, yyyy HH:mm:ss", Locale.US);
                    if (LocalDateTime.parse(dateTime + " " + hour + ":" + minutes, df).isAfter(LocalDateTime.parse(newETime, dateFormat))){
                        dateTimeWarning(newSTime, newETime, newName, newStatus, newDesc);
                    }
                    else {
                        updateTaskApi(newSTime, newETime, newName, newStatus, newDesc);
                    }
                }
            }
        } else if (!dateTimeEOld.equals(newETime)) {
            // same start time, but different end time
            boolean sBeforeE;
            boolean dateTimeENewBeforeNow = LocalDateTime.parse(dateTimeE + " " + hourE + ":" + minutesE, df).isBefore(now);
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MMMM d, yyyy HH:mm:ss", Locale.US);

            if (dateTimeENewBeforeNow) {
                dateTimeWarning(newSTime, newETime, newName, newStatus, newDesc);
            } else if (LocalDateTime.parse(newSTime, dateFormat).isAfter(LocalDateTime.parse(dateTimeE + " " + hourE + ":" + minutesE, df))) {
                sBeforeE = true;
                if (sBeforeE) {
                    dateTimeWarning(newSTime, newETime, newName, newStatus, newDesc);
                }
            } else {
                if (LocalDateTime.parse(newSTime, dateFormat).isAfter(LocalDateTime.parse(dateTimeE + " " + hourE + ":" + minutesE, df))){
                    dateTimeWarning(newSTime, newETime, newName, newStatus, newDesc);
                }
                else {
                    updateTaskApi(newSTime, newETime, newName, newStatus, newDesc);
                }
            }

        } else {
            updateTaskApi(newSTime, newETime, newName, newStatus, newDesc);
        }

    }

    private void dateTimeWarning(String newSTime, String newETime, String newName, String newStatus, String newDesc){
        new AlertDialog.Builder(this)
                .setTitle("")
                .setMessage(getString(R.string.out_time))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.ok, (dialog, whichButton) -> {
                    updateTaskApi(newSTime, newETime, newName, newStatus, newDesc);
                    Intent intent = new Intent(EditTaskActivity.this,
                            AllTasksActivity.class);
                    startActivity(intent);
                })
                .setNegativeButton(android.R.string.cancel, null).show();
    }

    private void updateTaskApi(String newSTime, String newETime, String newName, String newStatus, String newDesc){
        try {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MMMM d, yyyy HH:mm:ss", Locale.US);
            newSTime = LocalDateTime.parse(newSTime, dateFormat).toString();
        } catch (DateTimeParseException ignored) {
        }
        try {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MMMM d, yyyy HH:mm:ss", Locale.US);
            newETime = LocalDateTime.parse(newETime, dateFormat).toString();
        } catch (DateTimeParseException ignored) {
        }
        String response = Tasks.taskUpdate(getSharedPreferences("activities.AuthActivity", MODE_PRIVATE).getString("access_token", "null"), prev_name, newName, newSTime, newETime, newDesc, newStatus);

        if (response.equals("Success")) {
            Toast.makeText(EditTaskActivity.this, getString(R.string.successfully_edited), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(EditTaskActivity.this,
                    AllTasksActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(EditTaskActivity.this, getString(R.string.went_wrong), Toast.LENGTH_LONG).show();
        }
    }

    public void delTask(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.delete_task));
        builder.setMessage(getString(R.string.delete_confirmation));
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton(android.R.string.ok, (dialog, whichButton) -> {
            Tasks.taskDel(getSharedPreferences("activities.AuthActivity", MODE_PRIVATE).getString("access_token", "null"),
                    prev_name);
            Intent intent = new Intent(EditTaskActivity.this,
                    AllTasksActivity.class);
            startActivity(intent);
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.show();
    }


    public void backToAllTasks(View view){

        Intent intent = new Intent(EditTaskActivity.this,
                AllTasksActivity.class);
        startActivity(intent);

    }

}