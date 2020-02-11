package com.uek335.done.activity;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
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

    FloatingActionButton updateTaskInDb;
    /* buttons for category */
    private List<Button> categoryButtons = new ArrayList<>();
    private Button buttonToUnfocus;
    private int[] buttonIds = {R.id.btnWork, R.id.btnSchool, R.id.btnPrivate};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task_view);
        Intent intent = getIntent();
        int taskId = intent.getIntExtra("TaskId", 1);

        String dateFormat = "dd/MM/yy";
        android.icu.text.SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

        Task actualTask = AppDatabase.getAppDatabase(getApplicationContext()).taskDao().getTask(taskId);

        // get Back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView txtDescription = (TextView)findViewById(R.id.txtDescription);
        TextView txtTitle = (TextView)findViewById(R.id.txtTitle);
        TextView datepicker = (TextView)findViewById(R.id.datepicker);

        txtDescription.setText(actualTask.getContent());
        txtTitle.setText(actualTask.getTitle());
        datepicker.setText(actualTask.getEndDateString());

        // initialize category buttons
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


        // add action to Fab
        updateTaskInDb = findViewById(R.id.createNewTask);
        updateTaskInDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText titleView = findViewById(R.id.txtTitle);
                EditText contentView = findViewById(R.id.txtDescription);
                EditText datePickerView = findViewById(R.id.datepicker);
                AppDatabase.getAppDatabase(getApplicationContext()).taskDao().updateTask(new Task() {
                    {
                        setTitle(titleView.getText().toString().trim());
                        setContent(contentView.getText().toString().trim());
                        setCategory(categoryButtons.indexOf(buttonToUnfocus));
                        setId(taskId);
                        try {
                            setEndDate(sdf.parse(datePickerView.getText().toString()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
                // return to main page
                Intent returnToStart = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(returnToStart);
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){
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
