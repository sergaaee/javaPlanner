package com.example.planner;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Locale;

public class EditTaskActivity extends AppCompatActivity {

    String prev_name;
    private int year, month, day, hour, minutes, yearE, monthE, dayE, hourE, minutesE;
    private String dateTime = "";
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
        prev_name = taskName;

    }


    public void setNewSTimeData(View view) {

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
                    EditText sTimeField = findViewById(R.id.editTextNewTaskSTime);
                    sTimeField.setText(dateTime + " " + hour + ":" + minutes);
                }, hour, minutes, false);
        timePickerDialog.show();
    }

    public void setNewETimeData(View view) {

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
                    EditText eTimeField = findViewById(R.id.editTextNewTaskETime);
                    eTimeField.setText(dateTimeE + " " + hourE + ":" + minutesE);
                }, hourE, minutesE, true);
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
        try {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MMMM d, yyyy HH:mm:ss", Locale.US);
            newSTime = LocalDateTime.parse(newSTime, dateFormat).toString();
        } catch (DateTimeParseException ignored){}
        try {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MMMM d, yyyy HH:mm:ss", Locale.US);
            newETime = LocalDateTime.parse(newETime, dateFormat).toString();
        } catch (DateTimeParseException ignored){}

        String response = Tasks.taskUpdate(AuthActivity.getAccessToken(), prev_name, newName, newSTime, newETime, newDesc);

        if (response.equals("Success")){
            Toast successToast = Toast.makeText(EditTaskActivity.this, getString(R.string.successfully_edited), Toast.LENGTH_LONG);
            successToast.show();
            Intent intent = new Intent(EditTaskActivity.this,
                    AllTasksActivity.class);
            startActivity(intent);
        }

        else {
            Toast errorToast = Toast.makeText(EditTaskActivity.this, getString(R.string.went_wrong), Toast.LENGTH_LONG);
            errorToast.show();
        }

    }


    public void delTask(View view){
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.delete_task))
                .setMessage(getString(R.string.delete_confirmation))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Tasks.taskDel(AuthActivity.getAccessToken(), prev_name);
                        Intent intent = new Intent(EditTaskActivity.this,
                                AllTasksActivity.class);
                        startActivity(intent);
                    }})
                .setNegativeButton(android.R.string.cancel, null).show();
    }


    public void backToAllTasks(View view){

        Intent intent = new Intent(EditTaskActivity.this,
                AllTasksActivity.class);
        startActivity(intent);

    }

}