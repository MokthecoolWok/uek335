package com.uek335.done.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uek335.done.MainActivity;
import com.uek335.done.R;
import com.uek335.done.activity.helper.DatePickerUtil;
import com.uek335.done.model.AppDatabase;
import com.uek335.done.model.Task;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.uek335.done.R.drawable.button_blue;
import static com.uek335.done.R.drawable.button_disabled;
import static com.uek335.done.R.drawable.button_green;
import static com.uek335.done.R.drawable.button_orange;

public class EditTaskView extends AppCompatActivity {
    /* ID of task */
    int taskId;

    FloatingActionButton updateTaskInDb;
    /* Buttons for category */
    private List<Button> categoryButtons = new ArrayList<>();
    private Button buttonToUnfocus;
    private int[] buttonIds = {R.id.btnWork, R.id.btnSchool, R.id.btnPrivate};
    private int[] colors = {button_green, button_orange, button_blue};
    /* Datepicker */
    final Calendar calendar = Calendar.getInstance();
    EditText endDate;
    DatePickerDialog.OnDateSetListener date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task_view);

        /* Get ID of editing Task */
        taskId = getIntent().getIntExtra("TaskId", 1);

        /* Initialize Back button in Actionbar */
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /* Initialize category buttons */
        initializeCategories();

        /* Initialize DatePicker */
        initializeDatePicker();

        /* Add action to Fab */
        updateTaskInDb = findViewById(R.id.createNewTask);
        updateTaskInDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTask();
            }
        });

        /* Populate View with Data from DB */
        populateView(taskId);
    }

    /**
     * Get Data from DB according to provided taskId
     *
     * @param taskId int value which provides ID of task
     */
    private void populateView(int taskId) {
        Task taskData = AppDatabase.getAppDatabase(getApplicationContext()).taskDao().getTask(taskId);

        TextView txtDescription = (TextView) findViewById(R.id.txtDescription);
        TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
        TextView datepicker = (TextView) findViewById(R.id.datepicker);

        /* Colour active category */
        if (taskData.getCategory() != -1){
            categoryButtons.forEach( button -> button.setBackgroundResource(button_disabled));
            categoryButtons.get(taskData.getCategory()).setBackgroundResource(colors[taskData.getCategory()]);
            buttonToUnfocus = categoryButtons.get(taskData.getCategory());
        }

        txtDescription.setText(taskData.getContent());
        txtTitle.setText(taskData.getTitle());
        datepicker.setText(taskData.getEndDateString());
    }

    /**
     * Initialize Category buttons
     */
    private void initializeCategories() {
        for (int i = 0; i < buttonIds.length; i++) {
            categoryButtons.add((Button) findViewById(buttonIds[i]));
            categoryButtons.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    categoryButtons.get(0).setBackgroundResource(button_green);
                    categoryButtons.get(1).setBackgroundResource(button_orange);
                    categoryButtons.get(2).setBackgroundResource(button_blue);

                    // check if button is selected a second time
                    if (buttonToUnfocus != null && v.getId() == buttonToUnfocus.getId()) {
                        unsetButtonFocus((Button) v);
                    } else {
                        // switch focus
                        switch (v.getId()) {
                            case R.id.btnWork:
                                setButtonFocus(buttonToUnfocus, categoryButtons.get(0));
                                break;

                            case R.id.btnSchool:
                                setButtonFocus(buttonToUnfocus, categoryButtons.get(1));
                                break;

                            case R.id.btnPrivate:
                                setButtonFocus(buttonToUnfocus, categoryButtons.get(2));
                                break;
                        }
                    }
                }
            });
        }

        /* Initialize Colours */
        categoryButtons.get(0).setBackgroundResource(button_green);
        categoryButtons.get(1).setBackgroundResource(button_orange);
        categoryButtons.get(2).setBackgroundResource(button_blue);
    }

    /**
     * Initialize DatePicker Dialog
     */
    private void initializeDatePicker() {
        endDate = findViewById(R.id.datepicker);
        date = (view, year, month, day) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            updateLabel();
        };

        /* add datepicker to onclick on endDate textField */
        endDate.setOnClickListener(v -> new DatePickerDialog(EditTaskView.this, date,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                .show());
    }

    /**
     * @Section Datepicker functionality
     */
    private void updateLabel() {
        endDate.setText(DatePickerUtil.getSdf().format(calendar.getTime()));
    }

    /**
     * Send updated task to DB
     */
    private void editTask() {
        final EditText titleView = findViewById(R.id.txtTitle);

        /* Check if title is set */
        if (!titleView.getText().toString().trim().isEmpty()) {
            EditText contentView = findViewById(R.id.txtDescription);
            EditText datePickerView = findViewById(R.id.datepicker);
            AppDatabase.getAppDatabase(getApplicationContext()).taskDao().updateTask(new Task() {
                {
                    setId(taskId);
                    setTitle(titleView.getText().toString().trim());
                    setContent(contentView.getText().toString().trim());
                    setCategory(categoryButtons.indexOf(buttonToUnfocus));
                    try {
                        setEndDate(DatePickerUtil.getSdf().parse(datePickerView.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });

            /* Return to main page */
            Intent returnToStart = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(returnToStart);
        } else {
            /* throw alert dialog here */
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Please add a title to your task!")
                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent returnIntent = new Intent(getApplicationContext(), TaskDetailView.class);
        startActivityForResult(returnIntent, 0);
        return true;
    }

    /**
     * @Section Category button functionality here
     */

    /**
     * Set focus for category button
     *
     * @param unfocusedButton Button to unfocus
     * @param focusedButton   Newly focused button
     */
    private void setButtonFocus(Button unfocusedButton, final Button focusedButton) {
        categoryButtons.forEach(button -> {
            if (!focusedButton.equals(button)) {
                button.setBackgroundResource(button_disabled);
            }
        });

        this.buttonToUnfocus = focusedButton;
    }

    /**
     * Unfocus already focused button
     *
     * @param focusedButton Newly focused button
     */
    private void unsetButtonFocus(Button focusedButton) {
        this.buttonToUnfocus = null;
    }
}
