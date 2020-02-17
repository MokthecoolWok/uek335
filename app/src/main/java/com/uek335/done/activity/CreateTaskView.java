package com.uek335.done.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class CreateTaskView extends AppCompatActivity {
    /* Buttons for category */
    private List<Button> categoryButtons = new ArrayList<>();
    private Button buttonToUnfocus;
    private int[] buttonIds = {R.id.btnWork, R.id.btnSchool, R.id.btnPrivate};

    /* Datepicker */
    final Calendar calendar = Calendar.getInstance();
    EditText endDate;
    DatePickerDialog.OnDateSetListener date;

    /* fabButton */
    FloatingActionButton createTaskInDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task_view);

        /* Add Back button to ActionBar */
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /* Initialize CategoryButtons */
        initializeCategories();

        /* Initialize datepicker */
        initializeDatePicker();

        /* Add action to Fab */
        createTaskInDb = findViewById(R.id.createNewTask);
        createTaskInDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTask();
            }
        });
    }

    /**
     * Initialize Category Buttons
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
                        unsetButtonFocus();
                    } else {
                        // switch focus
                        switch (v.getId()) {
                            case R.id.btnWork:
                                setButtonFocus(categoryButtons.get(0));
                                break;

                            case R.id.btnSchool:
                                setButtonFocus(categoryButtons.get(1));
                                break;

                            case R.id.btnPrivate:
                                setButtonFocus(categoryButtons.get(2));
                                break;
                        }
                    }
                }
            });
        }
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

        /* add datepicker to onclick on endDate textfield */
        endDate.setOnClickListener(v -> new DatePickerDialog(CreateTaskView.this, date,
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
     * Create task in DB
     */
    private void createTask() {
        final EditText titleView = findViewById(R.id.txtTitle);

        /* Check if title is set */
        if (!titleView.getText().toString().trim().isEmpty()) {
            // post new entry to db
            AppDatabase.getAppDatabase(getApplicationContext()).taskDao().insertTask(new Task() {
                {
                    // set title
                    setTitle(titleView.getText().toString().trim());
                    // set description
                    EditText contentView = findViewById(R.id.txtDescription);
                    setContent(contentView.getText().toString().trim());
                    // set category
                    setCategory(categoryButtons.indexOf(buttonToUnfocus));
                    // set end date
                    EditText endDate = findViewById(R.id.datepicker);
                    try {
                        setEndDate(DatePickerUtil.getSdf().parse(endDate.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });

            /* Return to main page */
            Intent returnToStart = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(returnToStart);
        } else {
            // throw alert dialog here
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
        Intent returnIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(returnIntent, 0);
        return true;
    }

    /**
     * @Section Category button functionality here
     */

    /**
     * Set focus for category button
     *
     * @param focusedButton Newly focused button
     */
    private void setButtonFocus(final Button focusedButton) {
        categoryButtons.forEach(button -> {
            if (!focusedButton.equals(button)) {
                button.setBackgroundResource(button_disabled);
            }
        });

        this.buttonToUnfocus = focusedButton;
    }

    /**
     * Unfocus already focused button
     */
    private void unsetButtonFocus() {
        this.buttonToUnfocus = null;
    }
}
