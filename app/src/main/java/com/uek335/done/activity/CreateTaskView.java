package com.uek335.done.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uek335.done.MainActivity;
import com.uek335.done.R;
import com.uek335.done.model.AppDatabase;
import com.uek335.done.model.Task;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class CreateTaskView extends AppCompatActivity {
    /* database connection */
    AppDatabase database;

    /* dateformat */
    String dateFormat = "dd/MM/yy";
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

    /* buttons for category */
    private List<Button> categoryButtons = new ArrayList<Button>();
    private Button buttonToUnfocus;
    private int[] buttonIds = {R.id.btnWork, R.id.btnSchool, R.id.btnPrivate};

    /* datepicker */
    final Calendar calendar = Calendar.getInstance();
    EditText endDate;
    DatePickerDialog.OnDateSetListener date;

    /* fab button */
    FloatingActionButton createTaskInDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task_view);

        // add Database context
        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "task_databasa")
                .allowMainThreadQueries()
                .build();

        // get Back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // initialize category buttons
        for(int i = 0; i < categoryButtons.size(); i++){
            categoryButtons.add((Button) findViewById(buttonIds[i]));
            categoryButtons.get(i).setBackgroundColor(Color.rgb(207, 207, 207));
            categoryButtons.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // check if button is selected a second time
                    if(buttonToUnfocus != null && v.getId() == buttonToUnfocus.getId()){
                        unsetButtonFocus((Button) v);
                    } else {
                        // switch focus
                        switch (v.getId()){
                            case R.id.btnWork :
                                setButtonFocus(buttonToUnfocus, categoryButtons.get(0));
                                break;

                            case R.id.btnSchool :
                                setButtonFocus(buttonToUnfocus, categoryButtons.get(1));
                                break;

                            case R.id.btnPrivate :
                                setButtonFocus(buttonToUnfocus, categoryButtons.get(2));
                                break;
                        }
                    }
                }
            });
        }

        endDate = (EditText) findViewById(R.id.dateEndDate);
        // initialize datepicker
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };
        // add datepicker to onclick on endDate textfield
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateTaskView.this, date,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        // add action to Fab
        createTaskInDb = findViewById(R.id.createNewTask);
        createTaskInDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTask();
            }
        });
    }

    private void createTask() {
        final EditText titleView = findViewById(R.id.txtTitle);

        // check if title is set
        if(!titleView.getText().toString().isEmpty()){
            // post new entry to db
            database.taskDao().insertTask(new Task() {
                {
                    // set title
                    setTitle(titleView.getText().toString());
                    // set description
                    EditText contentView = findViewById(R.id.txtDescription);
                    setContent(contentView.getText().toString());
                    // set category
                    int indexOfCategory = categoryButtons.indexOf(buttonToUnfocus);
                    setCategory(indexOfCategory);
                    setCategory(1);
                    // set enddate
                    EditText endDate = findViewById(R.id.dateEndDate);
                    try {
                        setEndDate(sdf.parse(endDate.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            });

            // return to main page
            Intent returnToStart = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(returnToStart);
        } else {
            // throw alert dialog here

        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent returnIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(returnIntent, 0);
        return true;
    }

    /**
     * @Section Category button functionality here
     */

    /**
     * Set focus for category button
     * @param unfocusedButton   Button to unfocus
     * @param focusedButton     Newly focused button
     */
    private void setButtonFocus(Button unfocusedButton, Button focusedButton){
        if(buttonToUnfocus != null){
            unfocusedButton.setTextColor(Color.rgb(49, 50, 51));
            unfocusedButton.setBackgroundColor(Color.rgb(207, 207, 207));
        }
        focusedButton.setTextColor(Color.rgb(255, 255, 255));
        focusedButton.setBackgroundColor(Color.rgb(3, 106, 150));
        this.buttonToUnfocus = focusedButton;
    }

    /**
     * Unfocus already focused button
     * @param focusedButton     Newly focused button
     */
    private void unsetButtonFocus(Button focusedButton){
        if(focusedButton != null){
            focusedButton.setTextColor(Color.rgb(49, 50, 51));
            focusedButton.setBackgroundColor(Color.rgb(207, 207, 207));
            this.buttonToUnfocus = null;
        }
    }

    /**
     * @Section Datepicker
     */
    private void updateLabel() {
        endDate.setText(sdf.format(calendar.getTime()));
    }
}
